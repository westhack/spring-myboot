<template>

  <div class="upload-container">
    <a-modal :visible="previewVisible" :footer="null" @cancel="handleCancel">
      <img style="width: 100%" :src="preview" />
    </a-modal>

    <a-upload
      v-show="!inputValue"
      name="file"
      listType="picture-card"
      accept="image/*"
      class="image-uploader"
      :action="action"
      :headers="headers"
      :multiple="false"
      :showUploadList="false"
      :remove="handleRemove"
      @before-upload="beforeUpload"
      @preview="handlePreview"
      @change="handleChange"
      ref="upload"
    >

      <div v-show="!inputValue">
        <a-icon :type="loading ? 'loading' : 'plus'" />
        <div class="ant-upload-text">选择图片</div>
      </div>

    </a-upload>

    <div v-show="inputValue" class="ant-upload-list ant-upload-list-picture-card">
      <div class="ant-upload-list-item ant-upload-list-item-done">
        <div class="ant-upload-list-item-info">
          <span>
            <a :href="inputValue" target="_blank" rel="noopener noreferrer" class="ant-upload-list-item-thumbnail">
              <img :src="inputValue" :alt="inputValue">
            </a>
          </span>
        </div>
        <span class="ant-upload-list-item-actions">
          <a-icon type="eye-o" @click="handlePreview()"/>
          <a-icon type="upload" @click="upload()"/>
          <a-popconfirm title="确定要删除?" @confirm="handleRemove()" okText="是" cancelText="否">
            <a-icon type="delete"/>
          </a-popconfirm>
        </span>
      </div>
    </div>
  </div>
</template>

<script>
import { UPLOAD_IMAGE_URL, uploadCallBack, getHeaders } from '@/modules/storage/api'

export default {
  name: 'SingleImageUpload',
  props: {
    value: {
      type: String,
      default: ''
    },
    fileSize: {
      type: Number,
      default: 1024 * 1024 * 3
    }
  },
  data () {
    const vm = this

    return {
      loading: false,
      action: UPLOAD_IMAGE_URL,
      headers: getHeaders(),
      previewVisible: false,
      preview: '',
      inputValue: vm.value
    }
  },
  mounted () {
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

      return true
    },

    handleCancel () {
      this.previewVisible = false
    },

    handlePreview () {
      this.preview = this.inputValue
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
          this.inputValue = url
          this.loading = false
          this.emitInput(this.inputValue)
        }
      }
    },

    handleRemove () {
      this.inputValue = ''
      this.emitInput('')
    },

    draggableChange () {
      this.emitInput(this.inputValue)
    },

    upload () {
      this.$refs['upload'].$refs['uploadRef'].$refs['uploaderRef'].onClick()
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

  .anticon-upload {
    color: #FFFFFF;
  }
</style>
