<template>
  <div class="account-settings-info-view">
    <a-row :gutter="16">
      <a-col :md="24" :lg="16">

        <FormGenerator ref="form" :fields="formFields" @handleSubmit="handleSubmit" />

      </a-col>
      <a-col :md="24" :lg="8" :style="{ minHeight: '180px' }">
        <div class="ant-upload-preview" @click="$refs.modal.edit(1)" >
          <a-icon type="cloud-upload-o" class="upload-icon"/>
          <div class="mask">
            <a-icon type="plus" />
          </div>
          <img :src="options.img"/>
        </div>
      </a-col>

    </a-row>

    <avatar-modal ref="modal" :options="options" @change="avatarChange">

    </avatar-modal>
  </div>
</template>

<script>
import AvatarModal from './AvatarModal'
import FormGenerator from '@/components/FormGenerator/FormGenerator'
import { accountUpdate } from '@/modules/system/api/account'
import { httpResponseCode } from '@/constants/httpResponseCode'
import { setFormValue } from '@/utils/util'
import httpResponse from '@/mixins/httpResponse'

const defautFormFields = {
  'username': {
    label: '账号',
    name: 'username',
    value: '',
    // disabled: true,
    rules: [{ required: true, message: '不能为空' }]
  },
  'nickname': {
    label: '昵称',
    name: 'nickname',
    value: '',
    rules: [{ required: true, message: '不能为空' }]
  },
  'mobile': {
    label: '手机号',
    name: 'mobile',
    value: '',
    rules: [{ required: true, message: '不能为空' }]
  },
  'email': {
    label: '邮箱',
    name: 'email',
    value: '',
    rules: [{ required: true, message: '不能为空' }]
  },
  'avatar': {
    label: '头像',
    name: 'avatar',
    value: '',
    hidden: true
  }
}

export default {
  mixins: [httpResponse],
  components: {
    FormGenerator,
    AvatarModal
  },
  data () {
    const vm = this

    return {
      submitLoading: false,
      // cropper
      preview: {},
      userInfo: vm.$store.getters.userInfo,
      formFields: defautFormFields,
      avatar: vm.$store.getters.avatar,
      options: {
        img: vm.$store.getters.avatar,
        info: true,
        size: 1,
        outputType: 'jpeg',
        canScale: false,
        autoCrop: true,
        // 只有自动截图开启 宽度高度才生效
        autoCropWidth: 200,
        autoCropHeight: 200,
        fixedBox: true,
        // 开启宽度和高度比例
        fixed: true,
        fixedNumber: [1, 1]
      }
    }
  },
  created () {
    this.formFields = setFormValue(this.formFields, this.userInfo)
  },
  methods: {
    handleSubmit (e) {
      e.preventDefault()

      this.submitLoading = true
      this.$refs['form'].validateFields((err, values) => {
        if (!err) {
          values['avatar'] = this.avatar
          accountUpdate(values)
            .then((res) => this.submitSuccess(res))
            .then((res) => {
              if (res.code === httpResponseCode.SUCCESS) {
                this.$store.commit('SET_AVATAR', this.avatar)
              }
            })
            .catch(err => this.requestFailed(err))
            .finally(() => {
              this.submitLoading = true
            })
        } else {
          this.submitLoading = false
        }
      })
    },
    avatarChange (val) {
      this.avatar = val
    }
  }
}
</script>

<style lang="less" scoped>

  .avatar-upload-wrapper {
    height: 200px;
    width: 100%;
  }

  .ant-upload-preview {
    position: relative;
    margin: 0 auto;
    width: 100%;
    max-width: 180px;
    border-radius: 50%;
    box-shadow: 0 0 4px #ccc;

    .upload-icon {
      position: absolute;
      top: 0;
      right: 10px;
      font-size: 1.4rem;
      padding: 0.5rem;
      background: rgba(222, 221, 221, 0.7);
      border-radius: 50%;
      border: 1px solid rgba(0, 0, 0, 0.2);
    }
    .mask {
      opacity: 0;
      position: absolute;
      background: rgba(0,0,0,0.4);
      cursor: pointer;
      transition: opacity 0.4s;

      &:hover {
        opacity: 1;
      }

      i {
        font-size: 2rem;
        position: absolute;
        top: 50%;
        left: 50%;
        margin-left: -1rem;
        margin-top: -1rem;
        color: #d6d6d6;
      }
    }

    img, .mask {
      width: 100%;
      max-width: 180px;
      height: 100%;
      border-radius: 50%;
      overflow: hidden;
    }
  }
</style>
