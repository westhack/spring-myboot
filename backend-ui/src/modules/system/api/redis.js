import { axios } from '@/utils/request'

const API_VERSION = '/v1/system/redis'

export const api = {
  list: API_VERSION + '/getList',
  create: API_VERSION + '/create',
  update: API_VERSION + '/update',
  delete: API_VERSION + '/delete',
  deleteByIds: API_VERSION + '/deleteByIds'
}

export function getRedisList (parameter) {
  return axios({ url: api.list, method: 'post', data: parameter })
}

export function redisCreate (parameter) {
  return axios({ url: api.create, method: 'post', data: parameter })
}

export function redisUpdate (parameter) {
  return axios({ url: api.update, method: 'post', data: parameter })
}

export function redisDelete (parameter) {
  return axios({ url: api.delete, method: 'post', data: parameter })
}

export function redisDeleteByIds (parameter) {
  return axios({ url: api.deleteByIds, method: 'post', data: parameter })
}
