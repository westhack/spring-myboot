import { Menu, Icon, Input } from 'ant-design-vue'
import './Menu.less'
import _ from 'lodash'

const { Item, ItemGroup, SubMenu } = Menu
const { Search } = Input

export default {
  name: 'Menu',
  props: {
    dataSource: {
      type: Array,
      required: true
    },
    openKeys: {
      type: Array,
      default: () => []
    },
    defaultSelectedKeys: {
      type: Array,
      default: () => []
    },
    search: {
      type: Boolean,
      default: false
    },
    label: {
      type: String,
      default: 'label'
    },
    name: {
      type: String,
      default: 'name'
    },
    toolBar: {
      type: Array,
      default: () => []
    }
  },
  created () {
    this.localOpenKeys = this.openKeys.slice(0)
  },
  watch: {
    'openKeys': function (val) {
      this.localOpenKeys = this.openKeys
    }
  },
  data () {
    return {
      localOpenKeys: [],
      searchValue: ''
    }
  },
  methods: {
    handleOnInput (e) {
      this.searchValue = e.target.value
    },
    handleSearch () {
      this.$emit('searchClick', this.searchValue)
    },
    handleToolBer (key, item) {
      this.$emit('toolBarClick', key, item)
    },
    handleTitleClick (...args) {
      this.$emit('titleClick', { args })
    },

    renderToolBer (item) {
      const toolBar = this.toolBar.map(o => {
        if (o.key === 'del') {
          return <a-menu-item key="del">
            <a-popconfirm title="确定要删除?" {...{ on: { confirm: () => this.handleToolBer(o.key, item) } }} okText="是" cancelText="否">
              <div>删除</div>
            </a-popconfirm>
          </a-menu-item>
        }
        return <a-menu-item key={o.key} {...{ on: { click: () => this.handleToolBer(o.key, item) } }}>{o.label}</a-menu-item>
      })

      return <a-menu slot="overlay">
        {toolBar}
      </a-menu>
    },
    renderSearch () {
      return (
        <Search
          vModel={this.searchValue}
          {...{ on: { search: () => this.handleSearch() } }}
          placeholder="搜索"
          style="width: 100%; margin-bottom: 1rem"
        />
      )
    },
    renderIcon (icon) {
      return icon && (<Icon type={icon} />) || null
    },
    renderMenuItem (item) {
      const toolBar = this.renderToolBer(item)

      return (
        <Item key={item[this.name]}>
          { this.renderIcon(item.icon) }
          { item[this.label] }
          <div>
            <div class="btn" style="width: 20px;z-index:1300">
              <a-dropdown>
                <a class="btn">
                  <a-icon type="ellipsis" vOn:click={(e) => {
                    e.stopPropagation()
                  }}/>
                </a>
                {toolBar}
              </a-dropdown>
            </div>
          </div>

        </Item>
      )
    },
    renderItem (item) {
      return _.size(item.children) > 0 ? this.renderSubItem(item, item[this.name]) : this.renderMenuItem(item, item[this.name])
    },
    renderItemGroup (item) {
      const childrenItems = item.children.map(o => {
        return this.renderItem(o, o.name)
      })

      // const toolBar = this.renderToolBer(item)

      return (
        <ItemGroup key={item.name}>
          <template slot="title">
            <span>{ item[this.label] }</span>
            <div class="btn">
              <a-dropdown>
                <a class="btn">
                  <a-icon type="ellipsis" vOn:click={(e) => { e.stopPropagation() }}/>
                </a>
                <a-menu slot="overlay">
                  <a-menu-item key="add" {...{ on: { click: () => this.handleToolBer('add', item) } }}>添加</a-menu-item>
                </a-menu>
              </a-dropdown>
            </div>
          </template>
          { childrenItems }
        </ItemGroup>
      )
    },
    renderSubItem (item, key) {
      const childrenItems = item.children && item.children.map(o => {
        return this.renderItem(o, o.name)
      })

      const toolBar = this.renderToolBer(item)

      const title = (
        <span slot="title">
          { this.renderIcon(item.icon) }
          <span>{ item[this.label] }</span>

          <div class="btn" style="width: 20px;z-index:1300" >
            <a-dropdown>
              <a class="btn">
                <a-icon type="ellipsis" vOn:click={(e) => { e.stopPropagation() }}/>
              </a>
              { toolBar }
            </a-dropdown>
          </div>

        </span>
      )

      if (item.group) {
        return this.renderItemGroup(item)
      }

      // titleClick={this.handleTitleClick(item)}
      return (
        <SubMenu key={key}>
          { title }
          { childrenItems }
        </SubMenu>
      )
    }
  },
  render () {
    const { dataSource, search } = this.$props

    // this.localOpenKeys = openKeys.slice(0)
    const list = dataSource.map(item => {
      return this.renderItem(item)
    })

    return (
      <div class="tree-wrapper">
        { search ? this.renderSearch() : null }
        <Menu mode="inline" class="custom-tree" {...{ on: { click: item => this.$emit('click', item), 'update:openKeys': val => { this.localOpenKeys = val } } }}
          defaultSelectedKeys={this.defaultSelectedKeys}
          openKeys={this.localOpenKeys}>
          { list }
        </Menu>
      </div>
    )
  }
}
