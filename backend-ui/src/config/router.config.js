// eslint-disable-next-line
import { UserLayout, BlankLayout, BasicLayout } from '@/layouts'

/**
 * 基础路由
 * @type { *[] }
 */
export const constantRouterMap = [
  {
    path: '/user',
    component: UserLayout,
    redirect: '/user/login',
    hidden: true,
    children: [
      {
        path: 'login',
        name: 'login',
        component: () => import(/* webpackChunkName: "user" */ '@/modules/system/views/auth/Login')
      },
      {
        path: 'register',
        name: 'register',
        component: () => import(/* webpackChunkName: "user" */ '@/modules/system/views/auth/Register')
      },
      {
        path: 'registerResult',
        name: 'registerResult',
        component: () => import(/* webpackChunkName: "user" */ '@/modules/system/views/auth/RegisterResult')
      }
    ]
  },

  {
    path: '/404',
    name: '404',
    component: () => import(/* webpackChunkName: "fail" */ '@/views/exception/404')
  }
]
