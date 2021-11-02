import { Menu, Icon, Input } from 'ant-design-vue'
import './tree.less'

const { Item, ItemGroup, SubMenu } = Menu
const { Search } = Input

export default {
  name: 'Tree',
  props: {
    dataSource: {
      type: Array,
      required: true
    },
    keyName: {
      type: String,
      default: 'name'
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
    toolBar: {
      type: Array,
      default: () => []
    },
    level: {
      type: Number,
      default: 0
    }
  },
  created () {
    this.localOpenKeys = this.openKeys.slice(0)
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
      let toolBarDiv = ''
      if (this.toolBar.length > 0) {
        toolBarDiv = <div class="btn" style="width: 20px;z-index:1300;float:right">
          <a-dropdown>
            <a class="btn">
              <a-icon type="ellipsis" vOn:click={(e) => {
                e.stopPropagation()
              }}/>
            </a>
            {toolBar}
          </a-dropdown>
        </div>
      }

      return (
        <Item key={item[this.keyName]} onClick={ () => { this.$emit('titleClick', item) } }>
          { this.renderIcon(item.icon) }
          <div>
            { item.label }
          </div>
        </Item>
      )
    },
    renderItem (item) {
      return item.children && item.children.length > 0 ? this.renderSubItem(item, item[this.keyName]) : this.renderMenuItem(item, item[this.keyName])
    },
    renderItemGroup (item) {
      const childrenItems = item.children.map(o => {
        return this.renderItem(o)
      })

      let toolBar = ''
      if (this.toolBar.length > 0) {
        toolBar = <div class="btn">
          <a-dropdown>
            <a class="btn">
              <a-icon type="ellipsis" vOn:click={(e) => {
                e.stopPropagation()
              }}/>
            </a>
            <a-menu slot="overlay">
              <a-menu-item key="add" {...{ on: { click: () => this.handleToolBer('add', item) } }}>添加</a-menu-item>
            </a-menu>
          </a-dropdown>
        </div>
      }

      return (
        <ItemGroup key={item[this.keyName]}>
          <template slot="title">
            <span>{ item.label }</span>
            {toolBar}
          </template>
          { childrenItems }
        </ItemGroup>
      )
    },
    renderSubItem (item, key) {
      const childrenItems = item.children && item.children.length > 0 && item.children.map(o => {
        return this.renderItem(o)
      })

      let toolBarDiv = ''
      let toolBar = ''
      if (this.toolBar.length > 0) {
        toolBar = this.renderToolBer(item)

        toolBarDiv = <div class="btn" style="width: 20px;z-index:1300">
          <a-dropdown>
            <a class="btn">
              <a-icon type="ellipsis" vOn:click={(e) => {
                e.stopPropagation()
              }}/>
            </a>
            {toolBar}
          </a-dropdown>
        </div>
      }

      const title = (
        <span slot="title">
          { this.renderIcon(item.icon) }
          <div>
            { item.label }
          </div>

        </span>
      )

      if (item.group) {
        return this.renderItemGroup(item)
      }

      // <SubMenu key={key} onTitleClick={ this.handleTitleClick(item) }>
      return (
        <SubMenu key={key} onTitleClick={ () => { this.$emit('titleClick', item) } }>
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
        <Menu mode="inline"
          class="custom-tree" {...{ on: { click: item => this.$emit('click', item),
            'update:openKeys': val => { this.localOpenKeys = val } } }}
          defaultSelectedKeys={this.defaultSelectedKeys}
          openKeys={this.localOpenKeys}>
          { list }
        </Menu>
      </div>
    )
  }
}
