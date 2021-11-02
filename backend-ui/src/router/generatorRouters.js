// eslint-disable-next-line
import { UserLayout, BasicLayout, RouteView, BlankLayout, PageView, IFrameView } from '@/layouts'
import components from '@/router/components'
import importComponent from '@/utils/importComponent'
import { getRouterByUser } from '@/modules/system/api/auth'

// 前端路由表
export const constantRouterComponents = {
  // 基础页面 layout 必须引入
  BasicLayout: BasicLayout,
  BlankLayout: BlankLayout,
  RouteView: RouteView,
  PageView: PageView,
  IFrameView: IFrameView,

  // 你需要动态引入的页面组件
  ...components
}

// 前端未找到页面路由（固定不用改）
const notFoundRouter = {
  path: '*', redirect: '/404', hidden: false
}

const rootRouter = {
  name: 'index', title: 'index', path: '', redirect: '/dashboard/workplace', hidden: false, component: BasicLayout
}

/**
 * 获取路由菜单信息
 *
 * 1. 调用 getRouterByUser() 访问后端接口获得路由结构数组
 *    @see https://github.com/sendya/ant-design-pro-vue/blob/feature/dynamic-menu/public/dynamic-menu.json
 * 2. 调用
 * @returns {Promise<any>}
 */
export const generatorDynamicRouter = () => {
  return new Promise((resolve, reject) => {
    // ajax
    getRouterByUser().then(res => {
      const { menus } = res.data
      let routers = generator(menus)

      rootRouter.children = routers
      routers = [rootRouter]
      routers.push(notFoundRouter)
      resolve(routers)
    }).catch(err => {
      reject(err)
    })
  })
}

/**
 * 格式化 后端 结构信息并递归生成层级路由表
 *
 * @param routerMap
 * @param parent
 * @returns {*}
 */
export const generator = (routerMap, parent) => {
  return routerMap.map(item => {
    let component
    let open = false
    let defaultPath = item.defaultPath || ''
    let path = item.path
    if (item.component == null || item.component === '') {
      const fdStart = (item.path).indexOf('http')
      if (fdStart === 0) {
        open = true
        path = `${parent && parent.name || ''}/${item.name}`
        path.replace('//', '/')
        path = ''
        defaultPath = item.path
      }
    } else {
      component = constantRouterComponents[item.component || item.path]
      if (!component && item.component) {
        component = () => import(`@/${item.component}`)
      }
      if (item.component === 'IFrameView') {
        defaultPath = path
        path = `${parent && parent.name || ''}/${item.name}`
      }
    }

    // if (!target) {
    //   path = `${parent && parent.path || ''}/${item.path}`
    //   path.replace('//', '/')
    // } else {
    //   defaultPath = path
    //   path = `${parent && parent.name || ''}/${item.name}`
    //   console.log('====>', path)
    // }

    const currentRouter = {
      // 路由地址 动态拼接生成如 /dashboard/workplace
      path: path,
      // 路由名称，建议唯一
      name: item.name || item.path || '',
      title: item.title || '未命名',
      // 该路由对应页面的 组件
      // component: (constantRouterComponents[item.component || item.key]) || (() => import(`@/${item.component}`)),
      component: component,
      // 是否显示
      hidden: item.hidden || false,
      // meta: 页面标题, 菜单图标, 页面权限(供指令权限用，可去掉)
      meta: {
        title: item.title || '未命名',
        icon: item.icon || 'menu',
        permission: item.permission && [ item.permission ] || null,
        hidden: item.hidden || true,
        keepAlive: item.keepAlive || false,
        hiddenHeader: false,
        open: open,
        defaultPath: defaultPath || ''
      }
    }
    // 为了防止出现后端返回结果不规范，处理有可能出现拼接出两个 反斜杠
    // currentRouter.path = currentRouter.path.replace('//', '/')
    // 重定向
    item.redirect && (currentRouter.redirect = item.redirect)
    // 是否有子菜单，并递归处理
    if (item.children && item.children.length > 0) {
      // Recursion
      currentRouter.children = generator(item.children, currentRouter)
    }
    return currentRouter
  })
}
