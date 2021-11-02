import { axios } from '@/utils/request'

const API_VERSION = '/v1/system'

export const api = {
  list: API_VERSION + '/log/getList',
  create: API_VERSION + '/log/create',
  update: API_VERSION + '/log/update',
  delete: API_VERSION + '/log/delete',
  deleteByIds: API_VERSION + '/log/deleteByIds',
  deleteAll: API_VERSION + '/log/deleteAll'
}

export function deleteAll (parameter) {
  return axios({ url: api.deleteAll, method: 'post', data: parameter })
}
