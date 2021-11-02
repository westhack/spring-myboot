import { axios } from '@/utils/request'

const API_VERSION = 'v1'

const api = {
  reloadSystem: API_VERSION + '/system/reloadSystem'
}

export function reloadSystem (parameter) {
  return axios({ url: api.reloadSystem, method: 'post', data: parameter })
}
