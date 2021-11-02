<template>

  <div class="upload-container">
    <a-modal :visible="previewVisible" :footer="null" @cancel="handleCancel">
      <img style="width: 100%" :src="preview" />
    </a-modal>
    <draggable v-model="inputValue" group="people" @start="drag=true" @end="drag=false" @change="draggableChange">
      <transition-group>
        <div :key="item + index" v-for="(item, index) in inputValue" class="ant-upload-list ant-upload-list-picture-card">
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
              <a-popconfirm title="确定要删除?" @confirm="handleRemove(item)" okText="是" cancelText="否">
                <a-icon type="delete"/>
              </a-popconfirm>
            </span>
          </div>
        </div>
      </transition-group>
    </draggable>

    <a-upload
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

      <div v-if="this.limit === 0 || inputValue.length < this.limit">
        <a-icon :type="loading ? 'loading' : 'plus'" />
        <div class="ant-upload-text">选择图片</div>
      </div>

    </a-upload>
  </div>
</template>

<script>
import { UPLOAD_IMAGE_URL, uploadCallBack, getHeaders } from '@/modules/storage/api'
import draggable from 'vuedraggable'
import _ from 'lodash'

export default {
  name: 'MultipleImageUpload',
  components: {
    draggable
  },
  props: {
    value: {
      type: [String, Array],
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
    }
  },
  data () {
    const vm = this

    const value = vm.parseValue(vm.value)
    return {
      loading: false,
      action: UPLOAD_IMAGE_URL,
      headers: getHeaders(),
      previewVisible: false,
      preview: '',
      inputValue: value || []
    }
  },
  mounted() {
    this.headers = getHeaders()
  },
  created: function () {
  },
  computed: {
  },
  watch: {
    'value': function (val) {
      this.inputValue = val
    }
  },
  methods: {
    emitInput (val) {
      this.$emit('input', val)
      this.$emit('change', val)
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

    handleCancel () {
      this.previewVisible = false
    },

    handlePreview (url) {
      this.preview = url
      this.previewVisible = true
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
          this.inputValue.push(url)
          this.loading = false
          this.emitInput(this.inputValue)
        }
      }
    },

    handleRemove (url) {
      const index = this.inputValue.indexOf(url)
      const newInputValue = this.inputValue.slice()
      newInputValue.splice(index, 1)
      this.inputValue = newInputValue
      this.emitInput(this.inputValue)
    },

    draggableChange () {
      this.emitInput(this.inputValue)
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

<style lang="less" scoped>
  .image-uploader > .ant-upload {
    width: 128px;
    height: 128px;
  }
  .ant-upload-select-picture-card i {
    font-size: 32px;
    color: #999;
  }

  .ant-upload-select-picture-card .ant-upload-text {
    margin-top: 8px;
    color: #666;
  }
</style>
