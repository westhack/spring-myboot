import Vue from 'vue'
import { login, getInfo, logout } from '@/modules/system/api/auth'
import { getUserUnreadMessages, userMessageView } from '@/modules/system/api/user'
import { ACCESS_TOKEN } from '@/store/mutation-types'
import { welcome } from '@/utils/util'
import { httpResponseCode } from '@/constants/httpResponseCode'
import store from '@/store'
import storage from 'store'

const user = {
  state: {
    token: '',
    name: '',
    welcome: '',
    avatar: '',
    roles: [],
    permissions: [],
    info: {},
    messages: [],
    messageCount: 0
  },

  mutations: {
    SET_TOKEN: (state, token) => {
      state.token = token
    },
    SET_NAME: (state, { name, welcome }) => {
      state.name = name
      state.welcome = welcome
    },
    SET_AVATAR: (state, avatar) => {
      state.avatar = avatar
    },
    SET_ROLES: (state, roles) => {
      state.roles = roles
    },
    SET_PERMISSIONS: (state, permissions) => {
      // const _permissions = []
      if (permissions) {
        // permissions.forEach(p => {
        //   _permissions.push(p.name)
        // })

        state.permissions = permissions
      } else {
        state.permissions = []
      }
    },
    SET_INFO: (state, info) => {
      state.info = info
    },
    SET_USER_MESSAGE: (state, messages) => {
      state.messages = messages
    },
    SET_USER_MESSAGE_COUNT: (state, messageCount) => {
      state.messageCount = messageCount
    }
  },

  actions: {
    // 登录
    Login ({ commit }, userInfo) {
      return new Promise((resolve, reject) => {
        login(userInfo).then(response => {
          const data = response.data
          if (response.code === httpResponseCode.SUCCESS) {
            storage.set(ACCESS_TOKEN, data.token, data.expiresAt)
            commit('SET_TOKEN', data.token)
            resolve()
          }
          reject(new Error(response.message))
        }).catch(error => {
          reject(error)
        })
      })
    },

    // 获取用户信息
    GetUserInfo ({ commit }) {
      return new Promise((resolve, reject) => {
        getInfo().then(response => {
          const { userInfo } = response.data
          if (userInfo.roles) {
            commit('SET_ROLES', userInfo.roles)
            commit('SET_PERMISSIONS', userInfo.permissions)
            commit('SET_INFO', userInfo)
          } else {
            reject(new Error('getInfo: roles must be a non-null array !'))
          }

          commit('SET_NAME', { name: userInfo.username, welcome: welcome() })
          commit('SET_AVATAR', userInfo.avatar)

          resolve(response)
        }).catch(error => {
          reject(error)
        })
      })
    },

    GetUserMessages ({ commit, state }) {
      return new Promise((resolve) => {
        getUserUnreadMessages().then(res => {
          if (res.data) {
            commit('SET_USER_MESSAGE', res.data.items)
            commit('SET_USER_MESSAGE_COUNT', res.data.total)
          }
        }).catch(() => {
          resolve()
        })
      })
    },

    UserMessageView ({ commit, state }, id) {
      return new Promise((resolve) => {
        userMessageView({ id: id }).then(res => {
          if (res.code === httpResponseCode.SUCCESS) {
            commit('SET_USER_MESSAGE_COUNT', res.data.total)
            resolve(res)
          }
        }).catch(() => {
          resolve()
        })
      })
    },

    // 登出
    Logout ({ commit, state }) {
      return new Promise((resolve) => {
        logout(state.token).then(() => {
          resolve()
        }).catch(() => {
          resolve()
        }).finally(() => {
          commit('SET_TOKEN', '')
          commit('SET_ROLES', [])
          commit('SET_PERMISSIONS', [])
          storage.remove(ACCESS_TOKEN)
        })
      })
    }

  }
}

export default user
