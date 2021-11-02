import { SUPER_ADMIN } from '@/constants/common'
import _ from 'lodash'

const PERMISSION_ENUM = {
  'create': { value: 'create', label: '新增' },
  'delete': { value: 'delete', label: '删除' },
  'update': { value: 'update', label: '修改' },
  'query': { value: 'query', label: '查询' },
  'get': { value: 'get', label: '详情' },
  'enable': { value: 'enable', label: '启用' },
  'disable': { value: 'disable', label: '禁用' },
  'import': { value: 'import', label: '导入' },
  'export': { value: 'export', label: '导出' }
}

function plugin (Vue) {
  if (plugin.installed) {
    return
  }

  /**
   *  <a-tab-pane v-if="$permission('dashboard.add')" tab="Tab 1">
   *    some context..
   *  </a-tab-pane>
   */
  !Vue.prototype.$permission && Object.defineProperties(Vue.prototype, {
    $permission: {
      get () {
        const _this = this
        return (permission) => {
          const roles = _this.$store.getters.roles
          let allow = false
          _.each(roles, (val) => {
            if (val.name == SUPER_ADMIN || val.name == permission) {
              allow = true
            }
          })

          if (allow === true) {
            return allow
          }

          const permissions = _this.$store.getters.permissions

          _.each(permissions, (val) => {
            if (val.name === permission) {
              allow = true
            }
          })

          return allow
        }
      }
    }
  })

  !Vue.prototype.$enum && Object.defineProperties(Vue.prototype, {
    $enum: {
      get () {
        // const _this = this;
        return (val) => {
          let result = PERMISSION_ENUM
          val && val.split('.').forEach(v => {
            result = result && result[v] || null
          })
          return result
        }
      }
    }
  })
}

export default plugin
