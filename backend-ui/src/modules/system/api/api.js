import { axios } from '@/utils/request'

const API_VERSION = '/v1/system'

export const api = {
  list: API_VERSION + '/api/getList',
  create: API_VERSION + '/api/create',
  update: API_VERSION + '/api/update',
  delete: API_VERSION + '/api/delete',
  deleteByIds: API_VERSION + '/api/delete'
}

export function getApiList (parameter) {
  return axios({ url: api.list, method: 'post', data: parameter })
}

export function apiCreate (parameter) {
  return axios({ url: api.create, method: 'post', data: parameter })
}

export function apiUpdate (parameter) {
  return axios({ url: api.update, method: 'post', data: parameter })
}

export function apiDelete (parameter) {
  return axios({ url: api.delete, method: 'post', data: parameter })
}

export function apiDeleteByIds (parameter) {
  return axios({ url: api.deleteByIds, method: 'post', data: parameter })
}
