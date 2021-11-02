import { axios } from '@/utils/request'

const API_VERSION = '/v1/system'

export const user = {
  list: API_VERSION + '/user/getList',
  create: API_VERSION + '/user/create',
  update: API_VERSION + '/user/update',
  delete: API_VERSION + '/user/delete',
  deleteByIds: API_VERSION + '/user/deleteByIds',
  resetPassword: API_VERSION + '/user/resetPassword',
  changeStatus: API_VERSION + '/user/changeStatus',
  getUserMessages: API_VERSION + '/message/getUserMessages',
  getUserUnreadMessages: API_VERSION + '/message/getUserUnreadMessages',
  userMessageView: API_VERSION + '/message/userView',
  userMessageDelete: API_VERSION + '/message/userDelete'
}

export function getUserList (parameter) {
  return axios({ url: user.list, method: 'post', data: parameter })
}

export function userCreate (parameter) {
  return axios({ url: user.create, method: 'post', data: parameter })
}

export function userUpdate (parameter) {
  return axios({ url: user.update, method: 'post', data: parameter })
}

export function userDelete (parameter) {
  return axios({ url: user.delete, method: 'post', data: parameter })
}

export function userDeleteByIds (parameter) {
  return axios({ url: user.deleteByIds, method: 'post', data: parameter })
}

export function resetPassword (parameter) {
  return axios({ url: user.resetPassword, method: 'post', data: parameter })
}

export function changeStatus (parameter) {
  return axios({ url: user.changeStatus, method: 'post', data: parameter })
}

export function getUserMessages (parameter) {
  return axios({ url: user.getUserMessages, method: 'post', data: parameter })
}

export function getUserUnreadMessages (parameter) {
  return axios({ url: user.getUserUnreadMessages, method: 'post', data: parameter })
}

export function userMessageView (parameter) {
  return axios({ url: user.userMessageView, method: 'post', data: parameter })
}

export function userMessageDelete (parameter) {
  return axios({ url: user.userMessageDelete, method: 'post', data: parameter })
}
