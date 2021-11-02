import { axios } from '@/utils/request'

const API_VERSION = '/v1/system'

const dict = {
  dictList: API_VERSION + '/dict/getList',
  dictCreate: API_VERSION + '/dict/create',
  dictUpdate: API_VERSION + '/dict/update',
  dictDelete: API_VERSION + '/dict/delete',
  dictDeleteByIds: API_VERSION + '/dict/deleteByIds',
  dictAll: API_VERSION + '/dict/getAll',
  saveDictDetail: API_VERSION + '/dict/saveDetail'
}

export function getDictList (parameter) {
  return axios({ url: dict.dictList, method: 'post', data: parameter })
}

export function dictCreate (parameter) {
  return axios({ url: dict.dictCreate, method: 'post', data: parameter })
}

export function dictUpdate (parameter) {
  return axios({ url: dict.dictUpdate, method: 'post', data: parameter })
}

export function dictDelete (parameter) {
  return axios({ url: dict.dictDelete, method: 'post', data: parameter
  })
}

export function dictDeleteByIds (parameter) {
  return axios({ url: dict.dictDeleteByIds, method: 'post', data: parameter
  })
}

export function dictAll (parameter) {
  return axios({ url: dict.dictAll, method: 'get', params: parameter })
}

export function saveDictDetail (parameter) {
  return axios({ url: dict.saveDictDetail, method: 'post', data: parameter })
}
