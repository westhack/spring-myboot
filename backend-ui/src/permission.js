import storage from 'store'
import router from './router'
import store from './store'

import NProgress from 'nprogress' // progress bar
import 'nprogress/nprogress.css' // progress bar style
import notification from 'ant-design-vue/es/notification'
import { ACCESS_TOKEN } from '@/store/mutation-types'
import { httpResponseCode } from '@/constants/httpResponseCode'

NProgress.configure({ showSpinner: false }) // NProgress Configuration

const whiteList = ['404', 'login', 'register', 'registerResult', 'install'] // no redirect whitelist

router.beforeEach((to, from, next) => {
  NProgress.start() // start progress bar

  if (storage.get(ACCESS_TOKEN)) {
    /* has token */
    if (to.path === '/user/login') {
      next({ path: '/dashboard/workplace' })
      NProgress.done()
    } else if (to.path === '/404') {
      next()
      NProgress.done()
    } else {
      if (store.getters.addRouters.length === 0) {
        store.dispatch('GetUserInfo')
          .then(res => {
            const roles = res.result && res.result.role
            // 调用 vuex 的 从后端获取用户的路由菜单，动态添加可访问路由表
            store.dispatch('GenerateRoutes', { roles }).then(() => {
              // 把已获取到的路由菜单加入到路由表中
              router.addRoutes(store.getters.addRouters)
              if (store.getters.addRouters.length === 1) {
                next({ path: '/404', replace: true })
                NProgress.done()
                return
              }

              const redirect = decodeURIComponent(from.query.redirect || to.path)
              if (to.path === redirect) {
                // hack方法 确保addRoutes已完成 ,set the replace: true so the navigation will not leave a history record
                next({ ...to, replace: true })
              } else {
                // 跳转到目的路由
                next({ path: redirect })
              }
            })
          })
          .catch((e) => {
            console.log(e)

            notification.error({
              message: '错误',
              description: '请求用户信息失败，请重试'
            })

            if (e.code === httpResponseCode.UNAUTHORIZED) {
              store.dispatch('Logout').then(() => {
                next({ path: '/user/login', query: { redirect: to.fullPath } })
              })
            }
          })
      } else {
        next()
      }
    }

    store.dispatch('GetUserMessages')
  } else {
    console.log(to.name)
    if (whiteList.includes(to.name)) {
      // 在免登录白名单，直接进入
      next()
    } else {
      next({ path: '/user/login', query: { redirect: to.fullPath } })
      NProgress.done() // if current page is login will not trigger afterEach hook, so manually handle it
    }
  }
})

router.afterEach(() => {
  NProgress.done() // finish progress bar
})
