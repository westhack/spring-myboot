<template>
  <a-modal
    title="修改头像"
    :visible="visible"
    :maskClosable="false"
    :confirmLoading="confirmLoading"
    :width="800"
    @cancel="cancelHandel">
    <a-row>
      <a-col :xs="24" :md="12" :style="{height: '350px'}">
        <vue-cropper
          ref="cropper"
          :img="cropperOptions.img"
          :info="true"
          :autoCrop="cropperOptions.autoCrop"
          :autoCropWidth="cropperOptions.autoCropWidth"
          :autoCropHeight="cropperOptions.autoCropHeight"
          :fixedBox="cropperOptions.fixedBox"
          @realTime="realTime"
        >
        </vue-cropper>
      </a-col>
      <a-col :xs="24" :md="12" :style="{height: '350px'}">
        <div class="avatar-upload-preview" @click="selectFileClick">
          <img :src="previews.url" :style="previews.img"/>
          <input style="display: none" type="file" @change="handleChange" ref="file"/>
        </div>
      </a-col>
    </a-row>

    <template slot="footer">
      <a-button @click="cancelHandel">取消</a-button>
      <a-button @click="selectFileClick">上传</a-button>
      <a-button key="submit" type="primary" :loading="confirmLoading" @click="okHandel">保存</a-button>
    </template>
  </a-modal>
</template>
<script>

import { fileReader } from '@/utils/util'
import httpResponse from '@/mixins/httpResponse'
import { httpResponseCode } from '@/constants/httpResponseCode'
import { uploadImage } from '@/modules/storage/api'

export default {
  mixins: [httpResponse],
  components: {
  },
  props: {
    options: {
      type: Object,
      default: function () {
        return {}
      }
    }
  },
  data () {
    const vm = this

    return {
      visible: false,
      id: null,
      confirmLoading: false,
      cropperOptions: vm.options,
      previews: {}
    }
  },
  created () {

  },
  methods: {
    edit (id) {
      this.visible = true
      this.id = id
      /* 获取原始头像 */
    },
    close () {
      this.id = null
      this.visible = false
    },
    cancelHandel () {
      this.close()
    },
    okHandel () {
      const vm = this

      vm.confirmLoading = true

      // 获取截图的base64 数据
      // this.$refs.cropper.getCropData((data) => {
      //   // do something
      //   console.log(data)
      // })

      // 获取截图的blob数据
      this.$refs.cropper.getCropBlob((data) => {
        const formData = new FormData()
        formData.append('file', data)
        uploadImage(formData)
          .then((res) => {
            if (res.code === httpResponseCode.SUCCESS) {
              this.$message.success('上传成功')
              this.$emit('change', res.data.path)
            } else {
              this.$message.error(res.message)
            }
          })
          .catch(err => { this.$message.$error('上传失败') })
          .finally(() => { this.confirmLoading = false })
      })
    },

    realTime (data) {
      this.previews = data
    },

    selectFileClick () {
      this.$refs.file.click()
    },

    handleChange () {
      const inputDOM = this.$refs.file
      // 通过DOM取文件数据
      const files = inputDOM.files

      fileReader(files[0])
        .then((reader) => {
          this.cropperOptions.img = reader.result
        })
    }
  }
}
</script>

<style lang="less" scoped>

  .avatar-upload-preview {
    position: absolute;
    top: 50%;
    transform: translate(50%, -50%);
    width: 180px;
    height: 180px;
    border-radius: 50%;
    box-shadow: 0 0 4px #ccc;
    overflow: hidden;

    img {
      width: 100%;
      height: 100%;
    }
  }
</style>
