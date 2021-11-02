import { axios } from '@/utils/request'

const API_VERSION = '/v1/system'

const role = {
  roleList: API_VERSION + '/role/getList',
  roleCreate: API_VERSION + '/role/create',
  roleUpdate: API_VERSION + '/role/update',
  roleDelete: API_VERSION + '/role/delete',
  roleOptions: API_VERSION + '/role/getAll'
}

export function getRoleList (parameter) {
  return axios({ url: role.roleList, method: 'post', data: parameter })
}

export function roleCreate (parameter) {
  return axios({ url: role.roleCreate, method: 'post', data: parameter })
}

export function roleUpdate (parameter) {
  return axios({ url: role.roleUpdate, method: 'post', data: parameter })
}

export function roleDelete (parameter) {
  return axios({ url: role.roleDelete, method: 'post', data: parameter
  })
}

export function roleOptions (parameter) {
  return axios({ url: role.roleOptions, method: 'post', data: parameter })
}
