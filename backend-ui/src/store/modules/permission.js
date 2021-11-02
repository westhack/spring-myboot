import { constantRouterMap } from '@/config/router.config'
import moduleConfig from '@/router/module.router.config'
import { generatorDynamicRouter } from '@/router/generatorRouters'

const permission = {
  state: {
    routers: constantRouterMap,
    addRouters: []
  },
  mutations: {
    SET_ROUTERS: (state, routers) => {
      if (routers.length > 0) {
        routers[0].children = routers[0].children.concat(moduleConfig)
        state.addRouters = routers
        state.routers = constantRouterMap.concat(routers)
      }
    }
  },
  actions: {
    /**
     * 构建获取和构建路由和菜单信息
     * @param commit
     * @param data
     * @returns {Promise<any>}
     * @constructor
     */
    GenerateRoutes ({ commit }, data) {
      return new Promise(resolve => {
        generatorDynamicRouter(data).then(routers => {
          commit('SET_ROUTERS', routers)
          resolve()
        })
      })
    }
  }
}

export default permission
