import { RouteView } from '@/layouts'

const files = require.context('@/modules', true, /(.*)router\.config\.js$/)
const moduleConfig = []

files.keys().forEach(key => {
  if (files(key).default) {
    moduleConfig.push(files(key).default)
  }
})

const module = {
  path: '/module',
  name: 'module',
  redirect: '',
  component: RouteView,
  hidden: false,
  meta: { title: '系统扩展', icon: 'cluster', permission: [] },
  children: moduleConfig
}

export default module
