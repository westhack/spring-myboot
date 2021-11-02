import { axios } from '@/utils/request'

const API_VERSION = '/v1/system'

const department = {
  departmentList: API_VERSION + '/department/getList',
  departmentCreate: API_VERSION + '/department/create',
  departmentUpdate: API_VERSION + '/department/update',
  departmentDelete: API_VERSION + '/department/delete',
  departmentAll: API_VERSION + '/department/getAll'
}

export function getDepartmentList (parameter) {
  return axios({ url: department.departmentList, method: 'post', data: parameter })
}

export function departmentCreate (parameter) {
  return axios({ url: department.departmentCreate, method: 'post', data: parameter })
}

export function departmentUpdate (parameter) {
  return axios({ url: department.departmentUpdate, method: 'post', data: parameter })
}

export function departmentDelete (parameter) {
  return axios({ url: department.departmentDelete, method: 'post', data: parameter
  })
}

export function departmentAll (parameter) {
  return axios({ url: department.departmentAll, method: 'post', data: parameter })
}
