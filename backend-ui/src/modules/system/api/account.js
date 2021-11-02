import { axios } from '@/utils/request'
import { UPLOAD_IMAGE_URL } from '@/modules/storage/api'

const API_VERSION = 'v1/common'

const user = {
  accountAvatarUpload: UPLOAD_IMAGE_URL,
  accountResetPassword: API_VERSION + '/auth/changePassword',
  accountUpdate: API_VERSION + '/auth/update'
}

export function accountUpdate (parameter) {
  return axios({
    url: user.accountUpdate,
    method: 'post',
    data: parameter
  })
}

export function accountResetPassword (parameter) {
  return axios({
    url: user.accountResetPassword,
    method: 'post',
    data: parameter
  })
}
