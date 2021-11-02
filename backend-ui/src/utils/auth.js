/**
 * 弃用
 */
import { setStore, getStore, clearStore } from '@/utils/storage'
import { ACCESS_TOKEN } from '@/store/mutation-types'

export function getToken () {
  return getStore(ACCESS_TOKEN)
}

export function setToken (token) {
  // key, token, timeout = 86400s
  return setStore(ACCESS_TOKEN, token, 86400)
}

export function removeToken () {
  return clearStore(ACCESS_TOKEN)
}
