import Menu from 'ant-design-vue/es/menu'
import Icon from 'ant-design-vue/es/icon'

const { Item, SubMenu } = Menu

export default {
  name: 'SMenu',
  props: {
    menu: {
      type: Array,
      required: true
    },
    theme: {
      type: String,
      required: false,
      default: 'dark'
    },
    mode: {
      type: String,
      required: false,
      default: 'inline'
    },
    collapsed: {
      type: Boolean,
      required: false,
      default: false
    }
  },
  data () {
    return {
      openKeys: [],
      selectedKeys: [],
      cachedOpenKeys: []
    }
  },
  computed: {
    rootSubmenuKeys: vm => {
      const keys = []
      vm.menu.forEach(item => keys.push(item.path))
      return keys
    }
  },
  created () {
    this.updateMenu()
  },
  watch: {
    collapsed (val) {
      if (val) {
        this.cachedOpenKeys = this.openKeys.concat()
        this.openKeys = []
      } else {
        this.openKeys = this.cachedOpenKeys
      }
    },
    $route: function () {
      this.updateMenu()
    }
  },
  methods: {
    // select menu item
    onOpenChange (openKeys) {
      // 在水平模式下时执行，并且不再执行后续
      if (this.mode === 'horizontal') {
        this.openKeys = openKeys
        return
      }
      // 非水平模式时
      const latestOpenKey = openKeys.find(key => !this.openKeys.includes(key))
      if (!this.rootSubmenuKeys.includes(latestOpenKey)) {
        this.openKeys = openKeys
      } else {
        this.openKeys = latestOpenKey ? [latestOpenKey] : []
      }
    },
    updateMenu () {
      const routes = this.$route.matched.concat()
      if (routes.length >= 4 && this.$route.meta.hidden) {
        routes.pop()
        this.selectedKeys = [routes[2].path]
      } else {
        const p = routes.pop()
        // this.selectedKeys = [p.path, p.name]
        this.selectedKeys = [p.path]
        if (p.name && p.name != '') {
          this.selectedKeys.push(p.name)
        }
      }

      const openKeys = []
      if (this.mode === 'inline') {
        routes.forEach(item => {
          openKeys.push(item.path)
          if (item.name && item.name != '') {
            openKeys.push(item.name)
          }
        })
      }

      this.collapsed ? (this.cachedOpenKeys = openKeys) : (this.openKeys = openKeys)
    },

    // render
    renderItem (menu) {
      if (menu.meta.permission && menu.meta.permission.length > 0) {
        let ff = true
        const that = this
        menu.meta.permission.map(res => {
          if (that.$permission(res)) {
            ff = false
          }
        })

        if (ff) {
          return null
        }
      }

      if (!menu.hidden) {
        return menu.children && !menu.hideChildrenInMenu ? this.renderSubMenu(menu) : this.renderMenuItem(menu)
      }
      return null
    },
    renderMenuItem (menu) {
      const open = menu.meta.open || null
      const target = menu.meta.target || null
      let tag = target && 'a' || 'router-link'
      const props = { to: { name: menu.name } }
      const attrs = { href: menu.meta.defaultPath || menu.path, target: menu.meta.target }
      let on

      if (open) {
        tag = 'div'
        on = {
          click: ({ item, key, keyPath }) => {
            window.open(keyPath)
            return false
          }
        }
      } else {
        if (menu.meta.defaultPath && menu.meta.defaultPath.indexOf('http') === -1) {
          tag = 'div'
          if (menu.meta.defaultPath.indexOf('http') === -1) {
            on = {
              click: ({ item, key, keyPath }) => {
                this.$router.push({
                  path: keyPath.reverse().join('/')
                })
              }
            }
          }
        } else {
          on = {
            click: ({ item, key, keyPath }) => {
            }
          }
        }
      }

      return (
        <Item {...{ key: menu.meta.defaultPath || menu.path, on: on }}>
          <tag {...{ props, attrs }}>
            {this.renderIcon(menu.meta.icon)}
            <span>{menu.meta.title}</span>
          </tag>
        </Item>
      )
    },
    renderSubMenu (menu) {
      const itemArr = []
      if (menu.hidden) {
        return null
      }
      if (!menu.hideChildrenInMenu) {
        menu.children.forEach(item => itemArr.push(this.renderItem(item)))
      }
      return (
        <SubMenu {...{ key: menu.meta.defaultPath || menu.path }}>
          <span slot="title">
            {this.renderIcon(menu.meta.icon)}
            <span>{menu.meta.title}</span>
          </span>
          {itemArr}
        </SubMenu>
      )
    },
    renderIcon (icon) {
      if (icon === 'none' || icon === undefined) {
        return null
      }
      const props = {}
      typeof (icon) === 'object' ? props.component = icon : props.type = icon
      return (
        <Icon {... { props } }/>
      )
    }
  },
  render () {
    const { mode, theme, menu } = this
    const props = {
      mode: mode,
      theme: theme,
      openKeys: this.openKeys
    }
    const on = {
      select: obj => {
        this.selectedKeys = obj.selectedKeys
        this.$emit('select', obj)
      },
      // click: ({ item, key, keyPath }) => {
      //   console.log(item)
      //   console.log(key)
      //   console.log(keyPath)
      //   this.$router.push({
      //     path: keyPath.reverse().join('/')
      //   })
      // },
      openChange: this.onOpenChange
    }

    const menuTree = menu.map(item => {
      if (item.hidden) {
        return null
      }

      return this.renderItem(item)
    })
    // {...{ props, on: on }}
    return (
      <Menu vModel={this.selectedKeys} {...{ props, on: on }}>
        {menuTree}
      </Menu>
    )
  }
}
