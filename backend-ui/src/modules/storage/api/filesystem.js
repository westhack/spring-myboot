import { axios } from '@/utils/request'

const API_VERSION = '/v1/storage/upload'

export const api = {
  list: API_VERSION + '/getList',
  create: API_VERSION + '/create',
  update: API_VERSION + '/update',
  delete: API_VERSION + '/delete',
  deleteByIds: API_VERSION + '/deleteByIds',
  getUserFileList: API_VERSION + '/getUserFileList'
}

export function getFileList (parameter) {
  return axios({ url: api.list, method: 'post', data: parameter })
}

export function fileCreate (parameter) {
  return axios({ url: api.create, method: 'post', data: parameter })
}

export function fileUpdate (parameter) {
  return axios({ url: api.update, method: 'post', data: parameter })
}

export function fileDelete (parameter) {
  return axios({ url: api.delete, method: 'post', data: parameter })
}

export function fileDeleteByIds (parameter) {
  return axios({ url: api.deleteByIds, method: 'post', data: parameter })
}

export function getUserFileList (parameter) {
  return axios({ url: api.getUserFileList, method: 'post', data: parameter })
}
