import { axios } from '@/utils/request'

const API_VERSION = '/v1/system/message'

export const api = {
  list: API_VERSION + '/getList',
  create: API_VERSION + '/create',
  update: API_VERSION + '/update',
  delete: API_VERSION + '/delete',
  deleteByIds: API_VERSION + '/deleteByIds',
  deleteAll: API_VERSION + '/deleteAll'
}