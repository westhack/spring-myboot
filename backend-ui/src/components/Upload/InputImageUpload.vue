<template>

  <div class="upload-container">
    <a-modal :visible="previewVisible" :footer="null" @cancel="handleCancel">
      <img style="width: 100%" :src="preview" />
    </a-modal>

    <table>
      <tr>
        <td style="width: 35px">

          <img v-if="inputValue" :src="inputValue" :width="width" :height="height" style="margin-top: -5px" @click="handlePreview()">

          <a-upload
            v-else
            name="file"
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
          >

            <div v-if="!inputValue">
              <a-button>选择图片</a-button>
            </div>

          </a-upload>
        </td>
        <td style="width: 100px">
          <a-input v-model="inputValue" style="width: 200px" @change="draggableChange"/>
        </td>
        <td>
          <div v-if="inputValue">
            <a-popconfirm title="确定要删除?" @confirm="handleRemove()" okText="是" cancelText="否" style="width: 28px">
              <a-icon type="delete"/>
            </a-popconfirm>
          </div>
        </td>
      </tr>
    </table>

  </div>
</template>

<script>
import { UPLOAD_IMAGE_URL, uploadCallBack, getHeaders } from '@/modules/storage/api'

export default {
  name: 'InputImageUpload',
  props: {
    value: {
      type: String,
      default: ''
    },
    fileSize: {
      type: Number,
      default: 1024 * 1024 * 3
    },
    width: {
      type: [Number, String],
      default: '30px'
    },
    height: {
      type: [Number, String],
      default: '30px'
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

  .ant-upload-list-picture-card {
    width: 20px;
  }
</style>
