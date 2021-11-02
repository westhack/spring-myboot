
import { axios } from '@/utils/request'

const API_VERSION = '/v1/common'

const api = {
  Login: API_VERSION + '/auth/login',
  Logout: API_VERSION + '/auth/logout',
  Register: API_VERSION + '/auth/register',
  ImgCaptcha: API_VERSION + '/captcha/get',
  SendSms: API_VERSION + '/captcha/sendSms',
  UserInfo: API_VERSION + '/auth/getUserInfo',
  GetRouterByUser: API_VERSION + '/auth/getUserMenus'
}

export function login (parameter) {
  return axios({ url: api.Login, method: 'post', data: parameter })
}

export function register (parameter) {
  return axios({ url: api.Register, method: 'post', data: parameter })
}

export function getSmsCaptcha (parameter) {
  return axios({ url: api.SendSms, method: 'post', data: parameter
  })
}

export function getInfo () {
  return axios({ url: api.UserInfo, method: 'post' })
}

export function logout () {
  return axios({ url: api.Logout, method: 'post' })
}

/**
 * @returns {Promise}
 */
export const getRouterByUser = () => {
  return axios({ url: api.GetRouterByUser, method: 'post' })
}

export function getImgCaptcha (parameter) {
  return axios({ url: api.ImgCaptcha, method: 'get', data: parameter })
}
