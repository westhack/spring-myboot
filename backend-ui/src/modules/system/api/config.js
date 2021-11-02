import { axios } from '@/utils/request'

const API_VERSION = '/v1/system'

const api = {
  configList: API_VERSION + '/config/getList',
  configCreate: API_VERSION + '/config/create',
  configUpdate: API_VERSION + '/config/update',
  configDelete: API_VERSION + '/config/delete',
  configForceDelete: API_VERSION + '/config/force-delete',
  configBatchUpdateValue: API_VERSION + '/config/setValue',
  configOptions: API_VERSION + '/config/getAll',
  configWrite: API_VERSION + '/config/write'
}

export function getConfigList (parameter) {
  return axios({ url: api.configList, method: 'post', data: parameter })
}

export function configCreate (parameter) {
  return axios({ url: api.configCreate, method: 'post', data: parameter })
}

export function configUpdate (parameter) {
  return axios({ url: api.configUpdate, method: 'post', data: parameter })
}

export function configDelete (parameter) {
  return axios({ url: api.configDelete, method: 'post', data: parameter })
}

export function configForceDelete (parameter) {
  return axios({ url: api.configForceDelete, method: 'post', data: parameter })
}

export function configOptions (parameter) {
  return axios({ url: api.configOptions, method: 'post', data: parameter })
}

export function configBatchUpdateValue (parameter) {
  return axios({ url: api.configBatchUpdateValue, method: 'post', data: parameter })
}

export function configWrite (parameter) {
  return axios({ url: api.configWrite, method: 'post', data: parameter })
}
