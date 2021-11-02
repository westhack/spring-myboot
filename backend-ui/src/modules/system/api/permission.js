import { axios } from '@/utils/request'

const API_VERSION = '/v1/system'

const permission = {
  permissionTree: API_VERSION + '/permission/tree',
  permissionList: API_VERSION + '/permission/getTree',
  permissionCreate: API_VERSION + '/permission/create',
  permissionUpdate: API_VERSION + '/permission/update',
  permissionDelete: API_VERSION + '/permission/delete',
  permissionDeleteByIds: API_VERSION + '/permission/deleteByIds',
  permissionApis: API_VERSION + '/api/getRoutes',
  permissionAll: API_VERSION + '/permission/getAll'
}

export function getPermissionTree (parameter) {
  return axios({ url: permission.permissionList, method: 'post', data: parameter })
}

export function getPermissionList (parameter) {
  return axios({ url: permission.permissionList, method: 'post', data: parameter })
}

export function permissionCreate (parameter) {
  return axios({ url: permission.permissionCreate, method: 'post', data: parameter })
}

export function permissionUpdate (parameter) {
  return axios({ url: permission.permissionUpdate, method: 'post', data: parameter })
}

export function permissionDelete (parameter) {
  return axios({ url: permission.permissionDelete, method: 'post', data: parameter })
}

export function permissionDeleteByIds (parameter) {
  return axios({ url: permission.permissionDeleteByIds, method: 'post', data: parameter })
}

export function permissionApis (parameter) {
  return axios({ url: permission.permissionApis, method: 'post', data: parameter })
}

export function permissionAll (parameter) {
  return axios({ url: permission.permissionAll, method: 'post', data: parameter })
}
