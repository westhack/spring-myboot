<template>
  <div class="upload-container">

    <a-modal :visible="previewVisible" :footer="null" @cancel="handleCancel" :zIndex="99999999">
      <img alt="example" style="width: 100%" :src="previewImage" />
    </a-modal>

    <a-button v-show="showBtn" :style="{background:color,borderColor:color}" size="small" type="primary" @click=" dialogVisible=true">
      <a-icon type="upload" />上传图片
    </a-button>

    <a-modal
      v-model="dialogVisible"
      title="上传图片"
      onOk="handleOk"
      :zIndex="99999999"
    >
      <a-upload
        name="file"
        :action="uploadImageUrl"
        listType="picture-card"
        :fileList="fileList"
        :headers="uploadHeaders"
        accept="image/*"
        :multiple="true"
        :beforeUpload="beforeUpload"
        :remove="handleRemove"
        @preview="handlePreview"
        @change="handleChange"

      >
        <div v-if="fileList.length < 100">
          <a-icon type="plus" />
          <div class="ant-upload-text">选择图片</div>
        </div>
      </a-upload>

      <template slot="footer">
        <a-button @click="dialogVisible = false">
          取 消
        </a-button>
        <a-button
          type="primary"
          @click="handleUpload"
          :disabled="fileList.length === 0"
          :loading="uploading"
          style="display: none"
        >
          {{ uploading ? '上传中' : '开始上传' }}
        </a-button>
        <a-button type="primary" @click="handleSubmit">
          确 定
        </a-button>
      </template>

    </a-modal>
  </div>
</template>

<script>
import storage from 'store'
import { axios } from '@/utils/request'
import { UPLOAD_IMAGE_URL } from '@/modules/storage/api'
import { ACCESS_TOKEN } from '@/store/mutation-types'
import { httpResponseCode } from '@/constants/httpResponseCode'

const SIZE_LIMIT = 1024 * 1024 * 3

export default {
  name: 'EditorSlideUpload',
  props: {
    color: {
      type: String,
      default: '#1890ff'
    },
    showBtn: {
      type: Boolean,
      default: false
    }
  },
  data () {
    const vm = this
    return {
      uploading: false,
      dialogVisible: false,
      listObj: {},
      fileList: [],
      uploadHeaders: { Authorization: 'Bearer ' + storage.get(ACCESS_TOKEN) },
      uploadImageUrl: UPLOAD_IMAGE_URL,
      previewVisible: false,
      previewImage: null,
      handleCancel () {
        vm.previewVisible = false
      },

      handlePreview (file) {
        vm.previewImage = file.url || file.thumbUrl
        vm.previewVisible = true
      }
    }
  },
  methods: {
    showUpload () {
      this.dialogVisible = true
    },

    hideUpload () {
      this.dialogVisible = false
    },

    checkAllSuccess () {
      return Object.keys(this.fileList).every(item => this.fileList[item].url)
    },

    handleSubmit () {
      const arr = Object.keys(this.fileList).map(v => this.fileList[v])

      if (!this.checkAllSuccess()) {
        this.$message.error('请等待所有图片上传成功 或 出现了网络问题，请刷新页面重新上传！')
        return
      }
      this.$emit('successCBK', arr)
      this.listObj = {}
      this.fileList = []
      this.dialogVisible = false
    },

    handleChange ({ fileList, file, event }) {
      if (file.size > SIZE_LIMIT) {
        return
      }
      const newList = [...fileList]

      for (const index in newList) {
        if (
          newList[index].response !== undefined &&
          newList[index].response.data !== undefined &&
          newList[index].response.data.path !== undefined
        ) {
          newList[index]['url'] = newList[index].response.data.path
        }
      }

      this.fileList = newList
    },

    handleRemove (file) {
      const index = this.fileList.indexOf(file)
      const newFileList = this.fileList.slice()
      newFileList.splice(index, 1)
      this.fileList = newFileList
    },

    beforeUpload (file) {
      if (file.size > SIZE_LIMIT) {
        this.$message.warning('请选择小于3M的图片')
        return false
      }

      this.fileList = [...this.fileList, file]

      console.log(this.fileList)
      return true // 返回false 手动上传
    },

    handleUpload () { // 手动上传图片
      const { fileList } = this
      console.log(fileList)
      const formData = new FormData()
      fileList.forEach((file) => {
        formData.append('files[]', file.originFileObj)
      })
      this.uploading = true

      const that = this
      axios({
        headers: {
          'Authorization': 'Bearer ' + storage.get(ACCESS_TOKEN),
          'Content-Type': 'multipart/form-data'
        },
        url: UPLOAD_IMAGE_URL,
        method: 'post',
        processData: false,
        data: formData
      }).then(function (response) {
        if (response.code === httpResponseCode.SUCCESS) {
          that.$message.success('upload successfully.')
        }
      }).catch(function (error) {
        that.$message.error('upload failed.')
      }).finally(() => {
        that.uploading = false
      })
    }
  }
}
</script>

<style lang="less" scoped>

</style>
