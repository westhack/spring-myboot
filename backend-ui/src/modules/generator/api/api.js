import { axios } from '@/utils/request'

const API_VERSION = '/generator'

export const api = {
  getDatabases: API_VERSION + '/autoCode/getDatabases',
  getTables: API_VERSION + '/autoCode/getTables',
  getColumns: API_VERSION + '/autoCode/getColumns',
  codePreview: API_VERSION + '/autoCode/preview',
  createTemp: API_VERSION + '/autoCode/createTemp',
  list: API_VERSION + '/autoCode/getList',
  delete: API_VERSION + '/autoCode/delete',
  deleteByIds: API_VERSION + '/autoCode/deleteByIds',
  rollback: API_VERSION + '/autoCode/rollback',
  getModules: API_VERSION + '/autoCode/getModules'
}

export default api

export function getDatabases (parameter) { return axios({ url: api.getDatabases, method: 'get', params: parameter }) }
export function getTables (parameter) { return axios({ url: api.getTables, method: 'get', params: parameter }) }
export function getColumns (parameter) { return axios({ url: api.getColumns, method: 'get', params: parameter }) }
export function codePreview (parameter) { return axios({ url: api.codePreview, method: 'post', data: parameter }) }
export function createTemp (parameter) { return axios({ url: api.createTemp, method: 'post', data: parameter, responseType: 'blob' }) }
export function rollback (parameter) { return axios({ url: api.rollback, method: 'post', data: parameter }) }
export function getModules (parameter) { return axios({ url: api.getModules, method: 'get', params: parameter }) }
