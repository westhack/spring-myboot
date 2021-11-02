import { axios } from '@/utils/request'
import { httpResponseCode } from '@/constants/httpResponseCode'
import { ACCESS_TOKEN } from '@/store/mutation-types'
import storage from 'store'
import vue from '@/main'

const UPLOAD_URL = '/v1/storage/upload/file'

export const UPLOAD_IMAGE_URL = process.env.VUE_APP_BASE_API_URL + UPLOAD_URL
export const UPLOAD_FILE_URL = process.env.VUE_APP_BASE_API_URL + UPLOAD_URL
export const UPLOAD_SIZE_LIMIT = 1024 * 1024 * 3

export function uploadImage (parameter) {
  const headers = {}
  headers[ACCESS_TOKEN + ''] = storage.get(ACCESS_TOKEN)
  headers['content-type'] = 'application/x-www-form-urlencoded'
  return axios({
    headers: headers,
    url: UPLOAD_URL,
    method: 'post',
    data: parameter
  })
}

export function uploadFile (parameter) {
  const headers = {}
  headers[ACCESS_TOKEN + ''] = storage.get(ACCESS_TOKEN)
  headers['content-type'] = 'application/x-www-form-urlencoded'
  return axios({
    headers: headers,
    url: UPLOAD_URL,
    method: 'post',
    data: parameter
  })
}

export function uploadCallBack (res) {
  if (res.code == httpResponseCode.SUCCESS) {
    return res.data.file.url
  } else {
    vue.$message.error(res.message)
    return null
  }
}
export function getHeaders () {
  const headers = {}
  headers[ACCESS_TOKEN + ''] = storage.get(ACCESS_TOKEN)

  return headers
}
