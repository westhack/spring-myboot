// eslint-disable-next-line
import { RouteView } from '@/layouts'

const demoRouter = {
  path: '/demo',
  redirect: '/demo/view1',
  component: RouteView,
  hidden: false,
  meta: { title: '演示页面', icon: 'form', permission: [] },
  children: [
    {
      path: 'view1',
      name: 'DemoView1',
      component: () => import('@/modules/demo/views/view1.vue'),
      meta: { title: '演示页面1', keepAlive: true, permission: [] }
    },
    {
      path: 'view2',
      name: 'DemoView2',
      component: () => import('@/modules/demo/views/view2.vue'),
      meta: { title: '演示页面2', keepAlive: true, permission: [] }
    }
  ]
}

export default demoRouter
