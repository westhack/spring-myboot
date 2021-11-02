import { axios } from '@/utils/request'

const API_VERSION = '/v1/system'

const admin = {
  adminList: API_VERSION + '/admin/getList',
  adminCreate: API_VERSION + '/admin/create',
  adminUpdate: API_VERSION + '/admin/update',
  adminDelete: API_VERSION + '/admin/delete',
  deleteByIds: API_VERSION + '/admin/deleteByIds',
  resetPassword: API_VERSION + '/admin/resetPassword',
  changeStatus: API_VERSION + '/admin/changeStatus'
}

export function getAdminList (parameter) {
  return axios({ url: admin.adminList, method: 'post', data: parameter })
}

export function adminCreate (parameter) {
  return axios({ url: admin.adminCreate, method: 'post', data: parameter })
}

export function adminUpdate (parameter) {
  return axios({ url: admin.adminUpdate, method: 'post', data: parameter })
}

export function adminDelete (parameter) {
  return axios({ url: admin.adminDelete, method: 'post', data: parameter })
}

export function adminDeleteByIds (parameter) {
  return axios({ url: admin.deleteByIds, method: 'post', data: parameter })
}

export function adminResetPassword (parameter) {
  return axios({ url: admin.resetPassword, method: 'post', data: parameter })
}

export function adminChangeStatus (parameter) {
  return axios({ url: admin.changeStatus, method: 'post', data: parameter })
}
