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

    <div v-if="this.limit === 0 || inputValue.length < this.limit">
      <div class="ant-upload ant-upload-select ant-upload-select-picture-card" @click="select">
        <span role="button" tabindex="0" class="ant-upload">
          <a-icon :type="loading ? 'loading' : 'plus'" />
          <div class="ant-upload-text">选择图片</div>
        </span>
      </div>
    </div>

    <div>

      <a-drawer
        :title="drawerTitle"
        :width="drawerWidth"
        @close="drawerClose"
        :visible="visible"
        :body-style="{ paddingBottom: '80px' }"
      >
        <FileList ref="upload" v-model="inputValue" :multiple="multiple" :fileSize="fileSize" :limit="multiple == false ? 1 : limit"></FileList>

        <div class="drawer-form-footer">
          <a-input-search placeholder="搜索文件" style="width: 210px;margin-right: 10px" @search="search" v-model="fileName"/>
          <a-button @click="reload" style="margin-right: 10px">刷新</a-button>
          <a-button @click="upload" style="margin-right: 10px">上传</a-button>
          <a-button @click="submit" style="margin-right: 10px" type="primary">提交</a-button>
          <a-button @click="reset" style="margin-right: 10px">重置</a-button>
          <a-button @click="clear" style="margin-right: 10px">清空</a-button>
          <a-button :style="{ marginLeft: '8px' }" @click="drawerClose">关闭</a-button>
        </div>

      </a-drawer>

    </div>
  </div>
</template>

<script>
import { UPLOAD_IMAGE_URL, uploadCallBack, getHeaders } from '@/modules/storage/api'
import draggable from 'vuedraggable'
import _ from 'lodash'
import FileList from '@/components/Upload/FileList'

export default {
  name: 'SelectImageUpload',
  components: {
    FileList,
    draggable
  },
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
      drawerTitle: '选择图片',
      drawerWidth: 620,
      drawerLoading: false,
      loading: false,
      action: UPLOAD_IMAGE_URL,
      headers: getHeaders(),
      previewVisible: false,
      preview: '',
      inputValue: value,
      visible: false,
      fileName: ''
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
    },
    'inputValue': function (val) {
      this.emitInput(this.inputValue)
    }
  },
  methods: {
    emitInput (val) {
      this.$emit('input', val)
      this.$emit('change', val)
    },

    handleCancel () {
      this.previewVisible = false
    },

    handlePreview (url) {
      this.preview = url
      this.previewVisible = true
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

    select () {
      this.visible = true
    },

    upload () {
      this.$refs['upload'].upload()
    },
    submit () {
      console.log(this.inputValue)
    },
    clear () {
      this.$refs['upload'].clear()
    },
    reset () {
      this.$refs['upload'].reset()
    },
    reload () {
      this.$refs['upload'].reload()
    },
    search () {
      this.$refs['upload'].search(this.fileName)
    },

    drawerClose () {
      this.visible = false
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
  .upload-container {
    padding: 5px;
  }
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
