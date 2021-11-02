// eslint-disable-next-line
import { RouteView } from '@/layouts'

const generatorRouter = {
  path: '/generator',
  redirect: '/generator/index',
  component: RouteView,
  hidden: false,
  meta: { title: '页面生成器', icon: 'bg-colors', permission: ['admin'] },
  children: [
    {
      path: 'index',
      name: 'generatorIndex',
      component: () => import('@/modules/generator/views/Index.vue'),
      meta: { title: '代码生成', keepAlive: true, permission: ['admin'] }
    },
    {
      path: 'list',
      name: 'generatorList',
      component: () => import('@/modules/generator/views/List.vue'),
      meta: { title: '代码生成记录', keepAlive: true, permission: ['admin'] }
    }
  ]
}

export default generatorRouter
