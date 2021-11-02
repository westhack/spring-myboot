import { httpResponseCode } from '@/constants/httpResponseCode'

export default {
  methods: {
    submitSuccess (res) {
      const type = res.code === httpResponseCode.SUCCESS ? 'success' : 'error'
      this.$notification[type]({
        message: '消息提示',
        description: res.message
      })
      return res
    },

    requestFailed (err) {
      this.$notification['error']({
        message: '错误',
        description: err.message || ((err.response || {}).data || {}).message || '请求出现错误，请稍后再试',
        duration: 4
      })
    }
  }
}
