// eslint-disable-next-line
import { RouteView } from '@/layouts'

const captchaRouter = {
  path: '/captcha',
  redirect: '/captcha/view1',
  component: RouteView,
  hidden: false,
  meta: { title: '验证码演示', icon: 'form', permission: [] },
  children: [
    {
      path: 'SliderFixed',
      name: 'SliderFixed',
      component: () => import('@/modules/captcha/views/useOnline/SliderFixed.vue'),
      meta: { title: '滑动验证-嵌入', keepAlive: true, permission: [] }
    },
    {
      path: 'SliderPop',
      name: 'SliderPop',
      component: () => import('@/modules/captcha/views/useOnline/SliderPop.vue'),
      meta: { title: '滑动验证-弹出', keepAlive: true, permission: [] }
    },
    {
      path: 'PointFixed',
      name: 'PointFixed',
      component: () => import('@/modules/captcha/views/useOnline/PointFixed.vue'),
      meta: { title: '点选验证-嵌入', keepAlive: true, permission: [] }
    },
    {
      path: 'PointPop',
      name: 'PointPop',
      component: () => import('@/modules/captcha/views/useOnline/PointPop.vue'),
      meta: { title: '点选验证-弹出', keepAlive: true, permission: [] }
    }
  ]
}

export default captchaRouter
