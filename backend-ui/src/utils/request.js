import storage from 'store'
import axios from 'axios'
import store from '@/store'
import { VueAxios } from './axios'
import notification from 'ant-design-vue/es/notification'
import { ACCESS_TOKEN } from '@/store/mutation-types'
import { httpResponseCode } from '@/constants/httpResponseCode'

// 创建 axios 实例
const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API_URL, // api base_url
  timeout: 12000 // 请求超时时间
})

const err = (error) => {
  if (error.response) {
    const data = error.response.data
    const token = storage.get(ACCESS_TOKEN)
    if (error.response.status === httpResponseCode.UNAUTHENTICATED) {
      notification.error({
        message: '提示消息',
        description: data.message
      })
    }
    if (error.response.status === httpResponseCode.UNAUTHORIZED) {
      notification.error({
        message: '提示消息',
        description: data.message
      })
      if (token) {
        store.dispatch('Logout').then(() => {
          setTimeout(() => {
            window.location.reload()
          }, 1500)
        })
      }
    }
  }
  return Promise.reject(error)
}

// request interceptor
service.interceptors.request.use(config => {
  const token = storage.get(ACCESS_TOKEN)
  if (token) {
    config.headers[ACCESS_TOKEN] = token // 让每个请求携带自定义 token 请根据实际情况自行修改
  }

  return config
}, err)

// response interceptor
service.interceptors.response.use((response) => {
  if (response.data.code === httpResponseCode.UNAUTHENTICATED) {
    notification.error({
      message: '提示消息',
      description: response.data.message
    })

    return Promise.reject(response.data)
  }

  if (response.data.code === httpResponseCode.UNAUTHORIZED) {
    notification.error({
      message: '提示消息',
      description: response.data.message
    })

    return Promise.reject(response.data)
  }
  return response.data
  // return new Promise((resolve, reject) => {
  //   resolve(response.data)
  // })
}, err)

const installer = {
  vm: {},
  install (Vue) {
    Vue.use(VueAxios, service)
  }
}

export {
  installer as VueAxios,
  service as axios
}
