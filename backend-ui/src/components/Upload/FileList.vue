<template>
  <div>

    <a-modal :visible="previewVisible" :footer="null" @cancel="handleCancel">
      <img style="width: 100%" :src="preview" />
    </a-modal>
    <div style="display: flex;flex-direction: row;flex-wrap: wrap;">

      <div :key="item + index" v-for="(item, index) in dataSource" class="ant-upload-list ant-upload-list-picture-card">
        <div class="ant-upload-list-item ant-upload-list-item-done">
          <div class="ant-upload-list-item-info">
            <span>
              <a :href="item" target="_blank" rel="noopener noreferrer" class="ant-upload-list-item-thumbnail">
                <img :src="item.url" :alt="item.url">
              </a>
            </span>
          </div>
          <span class="ant-upload-list-item-actions">
            <a-icon type="eye-o" @click="handlePreview(item.url)"/>
            <a-popconfirm title="确定要删除?" @confirm="handleRemove(index)" okText="是" cancelText="否">
              <a-icon type="delete"/>
            </a-popconfirm>
          </span>
        </div>
        <a-checkbox class="checkbox" :checked="checked[item.url]" :value="item.url" @change="checkboxChange"></a-checkbox>
      </div>
    </div>
    <div class="page">
      <a-pagination
        size="small"
        v-model="page"
        :total="total"
        :pageSize="pageSize"
        :pageSizeOptions="pageSizeOptions"
        @change="onChange"
        @showSizeChange="showSizeChange"
        show-size-changer
        show-quick-jumper />
    </div>

    <div style="display: flex">
      <div :key="item + index" v-for="(item, index) in uploadValue" class="ant-upload-list ant-upload-list-picture-card">
        <div class="ant-upload-list-item ant-upload-list-item-done">
          <div class="ant-upload-list-item-info">
            <span>
              <a :href="item" target="_blank" rel="noopener noreferrer" class="ant-upload-list-item-thumbnail">
                <img :src="item" :alt="item">
              </a>
            </span>
          </div>
          <span class="ant-upload-list-item-actions">
            <a-icon type="eye-o" @click="handlePreview(item)"/>
            <a-popconfirm title="确定要删除?" @confirm="handleUploadRemove(item)" okText="是" cancelText="否">
              <a-icon type="delete"/>
            </a-popconfirm>
          </span>
        </div>
        <a-checkbox class="checkbox" :checked="checked[item]" :value="item" @change="checkboxChange"></a-checkbox>
      </div>

      <div v-show="false">
        <a-upload
          ref="upload"
          name="file"
          listType="picture-card"
          accept="image/*"
          class="image-uploader"
          :action="action"
          :headers="headers"
          :multiple="true"
          :showUploadList="false"
          :remove="handleRemove"
          @before-upload="beforeUpload"
          @preview="handlePreview"
          @change="handleChange"
        >

          <div>
            <a-icon :type="loading ? 'loading' : 'plus'" />
            <div class="ant-upload-text">选择图片</div>
          </div>

        </a-upload>
      </div>

    </div>
  </div>

</template>

<script>
import { UPLOAD_IMAGE_URL, uploadCallBack, getHeaders } from '@/modules/storage/api'
import { getUserFileList } from '@/modules/storage/api/filesystem'
import _ from 'lodash'
import MultipleImageUpload from '@/components/Upload/MultipleImageUpload'

export default {
  name: 'FileList',
  components: { MultipleImageUpload },
  props: {
    value: {
      type: [String, Array],
      required: false,
      default: function () {
        return []
      }
    },
    fileSize: {
      type: Number,
      default: 1024 * 1024 * 3
    },
    limit: {
      type: Number,
      default: 0
    },
    multiple: {
      type: Boolean,
      default: true
    }
  },
  data () {
    const vm = this
    const value = vm.parseValue(vm.value)
    return {
      loading: false,
      action: UPLOAD_IMAGE_URL,
      headers: getHeaders(),
      oldValue: _.cloneDeep(value),
      inputValue: value,
      dataSource: [],
      page: 1,
      total: 50,
      pageSize: 10,
      pageSizeOptions: ['10', '20', '30', '40'],
      previewVisible: false,
      preview: '',
      uploadValue: [],
      allInput: []
    }
  },
  computed: {
    checked: function (val) {
      const ret = []
      _.each(this.allInput, (o) => {
        ret[o] = true
      })
      return ret
    }
    // allInput: function () {
    //   return [...this.inputValue, ...this.uploadValue]
    // }
  },
  mounted () {
    this.getList()
    this.headers = getHeaders()
  },
  watch: {
    'value': function (val) {
      this.allInput = this.value
    }
  },
  methods: {
    getList (val) {
      const q = { page: this.page, pageSize: this.pageSize, ...val }

      getUserFileList(q).then(res => {
        this.dataSource = res.data.items
        this.total = res.data.total
        this.pageSize = res.data.pageSize
      })
    },
    onChange (current) {
      this.page = current
      this.getList()
    },
    showSizeChange (current) {
      this.pageSize = parseInt(this.pageSizeOptions[current])
      this.getList()
    },

    checkboxChange (e) {
      if (e.target.checked == true) {
        if (this.multiple == false && this.allInput.length > 0) {
          this.allInput = []
        }
        if (this.limit > 0 && this.allInput.length >= this.limit) {
          e.target.checked = false
          this.$message.error(`只能选择${this.limit}张图片`)
          return
        }
        this.allInput.push(e.target.value)
      } else {
        this.remove(e.target.value)
      }
      this.emitInput(this.allInput)
    },

    emitInput (values) {
      values = this.allInput
      this.$emit('input', values)
      this.$emit('change', values)
    },

    handleCancel () {
      this.previewVisible = false
    },

    handlePreview (url) {
      this.preview = url
      this.previewVisible = true
    },

    handleRemove (index) {
      const newInputValue = this.inputValue.slice()
      newInputValue.splice(index, 1)
      this.inputValue = newInputValue
      this.emitInput(this.inputValue)
    },

    upload () {
      this.$refs['upload'].$refs['uploadRef'].$refs['uploaderRef'].onClick()
    },

    beforeUpload (file, fileList) {
      if (file.size > this.fileSize) {
        this.$message.warning(`请选择小于${this.fileSize / 1024 / 1024}M的图片`)

        return false
      }

      if (this.limit > 0 && this.uploadValue.length > this.limit) {
        return false
      }

      return true
    },

    handleChange ({ fileList, file, event }) {
      if (file.size > this.fileSize) {
        return
      }

      if (file.status === 'uploading') {
        this.loading = true
        return
      }

      if (file.status === 'done') {
        const url = uploadCallBack(file.response)
        if (url) {
          this.uploadValue.push(url)
          this.loading = false
          this.emitInput(this.uploadValue)
        }
      }
    },

    remove (url) {
      const index = this.allInput.indexOf(url)
      const newInputValue = this.allInput.slice()
      newInputValue.splice(index, 1)
      this.allInput = newInputValue
      this.emitInput(this.allInput)
    },

    handleUploadRemove (url) {
      const index = this.uploadValue.indexOf(url)
      const newInputValue = this.uploadValue.slice()
      newInputValue.splice(index, 1)
      this.uploadValue = newInputValue
      this.emitInput(this.uploadValue)
    },

    reload () {
      this.getList()
    },
    clear () {
      this.allInput = []
      this.inputValue = []
      this.emitInput(this.allInput)
    },
    reset () {
      this.allInput = this.oldValue
      this.emitInput(this.allInput)
    },
    search (val) {
      this.page = 1
      this.getList({ search: [{ name: 'name', value: val, operator: 'like', type: 'input' }] })
    },
    parseValue (val) {
      let value = val || []
      if (value != '' && _.isString(value)) {
        value = value.split(',')
      }

      return value
    }
  }
}
</script>

<style scoped>

/deep/ .ant-upload-list-picture-card {
  position: relative;
}

/deep/ .checkbox {
  position: absolute;
  bottom: 7px;
  right: 9px;
}

/deep/ .ant-modal {
  width: 600px;
  height: 600px;
}

.page {
  display: flex;flex-direction: row; justify-content: flex-end;padding-top: 10px;padding-bottom: 10px
}

</style>
