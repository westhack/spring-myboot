import T from 'ant-design-vue/es/table/Table'
import get from 'lodash.get'
import _ from 'lodash'

delete T.props.pagination.validator

export default {
  data () {
    return {
      needTotalList: [],

      selectedRows: [],
      selectedRowKeys: [],

      localLoading: false,
      localDataSource: [],
      localPagination: Object.assign({}, T.props.pagination)
    }
  },
  props: Object.assign({}, T.props, {
    rowKey: {
      type: [String, Function],
      default: 'id'
    },
    data: {
      type: Function,
      required: true
    },
    pageNum: {
      type: Number,
      default: 1
    },
    pageSize: {
      type: Number,
      default: 10
    },
    showSizeChanger: {
      type: Boolean,
      default: true
    },
    size: {
      type: String,
      default: 'default'
    },
    /**
     * {
     *   show: true,
     *   clear: Function
     * }
     */
    alert: {
      type: [Object, Boolean],
      default: null
    },
    rowSelection: {
      type: Object,
      default: null
    },
    /** @Deprecated */
    showAlertInfo: {
      type: Boolean,
      default: false
    },
    showPagination: {
      type: String | Boolean,
      default: 'auto'
    },
    pageSizeOptions: {
      type: Array,
      required: false,
      default: function () {
        return ['10', '20', '30', '50', '100', '500']
      }
    }
  }),
  watch: {
    'localPagination.current' (val) {
      this.$router.push({
        name: this.$route.name,
        params: Object.assign({}, this.$route.params, {
          page: val
        })
      })
    },
    pageNum (val) {
      Object.assign(this.localPagination, {
        current: val
      })
    },
    pageSize (val) {
      Object.assign(this.localPagination, {
        pageSize: val
      })
    },
    showSizeChanger (val) {
      Object.assign(this.localPagination, {
        showSizeChanger: val
      })
    }
  },
  created () {
    this.localPagination = ['auto', true].includes(this.showPagination) && Object.assign({}, this.localPagination, {
      current: this.pageNum,
      pageSize: this.pageSize,
      showSizeChanger: this.showSizeChanger,
      showTotal: total => `总：${total} 条`
    })
    this.needTotalList = this.initTotalList(this.columns)
    this.loadData()
  },
  methods: {
    /**
     * 表格重新加载方法
     * 如果参数为 true, 则强制刷新到第一页
     * @param Boolean bool
     */
    refresh (bool = false) {
      bool && (this.localPagination = Object.assign({}, {
        current: 1, pageSize: this.pageSize
      }))
      this.loadData()
    },
    /**
     * 加载数据方法
     * @param {Object} pagination 分页选项器
     * @param {Object} filters 过滤条件
     * @param {Object} sorter 排序条件
     */
    loadData (pagination, filters, sorter) {
      this.localLoading = true
      const parameter = Object.assign({
        page: (pagination && pagination.current) || this.localPagination.current || this.pageNum,
        pageSize: (pagination && pagination.pageSize) || this.localPagination.pageSize || this.pageSize
      },
      (sorter && sorter.order && {
        sortOrder: { order: sorter.order, column: sorter.field }
      }) || {}, {
        ...filters
      })
      const result = this.data(parameter)
      // 对接自己的通用数据接口需要修改下方代码中的 r.page, r.totalCount, r.data
      // eslint-disable-next-line
      if ((typeof result === 'object' || typeof result === 'function') && typeof result.then === 'function') {
        result.then(r => {
          this.localPagination = Object.assign({}, this.localPagination, {
            current: r.page, // 返回结果中的当前分页数
            total: r.total, // 返回结果中的总记录数
            showSizeChanger: this.showSizeChanger,
            pageSize: (pagination && pagination.pageSize) || this.localPagination.pageSize
          })
          // 为防止删除数据后导致页面当前页面数据长度为 0 ,自动翻页到上一页
          if (r.items.length === 0 && this.localPagination.current > 1) {
            this.localPagination.current--
            this.loadData()
            return
          }

          // 这里用于判断接口是否有返回 r.totalCount 或 this.showPagination = false
          // 当情况满足时，表示数据不满足分页大小，关闭 table 分页功能

          (!this.showPagination || !r.total && this.showPagination === 'auto') && (this.localPagination = false)
          this.localDataSource = r.items // 返回结果中的数组数据
          this.localLoading = false
        })
      }
    },
    initTotalList (columns) {
      const totalList = []
      columns && columns instanceof Array && columns.forEach(column => {
        if (column.needTotal) {
          totalList.push({
            ...column,
            total: 0
          })
        }
      })

      return totalList
    },
    /**
     * 用于更新已选中的列表数据 total 统计
     * @param selectedRowKeys
     * @param selectedRows
     */
    updateSelect (selectedRowKeys, selectedRows) {
      this.selectedRows = selectedRows
      this.selectedRowKeys = selectedRowKeys
      const list = this.needTotalList
      this.needTotalList = list.map(item => {
        return {
          ...item,
          total: selectedRows.reduce((sum, val) => {
            const total = sum + parseInt(get(val, item.dataIndex))
            return isNaN(total) ? 0 : total
          }, 0)
        }
      })
    },
    /**
     * 清空 table 已选中项
     */
    clearSelected () {
      if (this.rowSelection) {
        this.rowSelection.onChange([], [])
        this.updateSelect([], [])
      }
    },
    /**
     * 处理交给 table 使用者去处理 clear 事件时，内部选中统计同时调用
     * @param callback
     * @returns {*}
     */
    renderClear (callback) {
      if (this.selectedRowKeys.length <= 0) return null
      return (
        <a style="margin-left: 24px" onClick={() => {
          callback()
          this.clearSelected()
        }}>清空</a>
      )
    },
    renderAlert () {
      if (!this.alert.show) return null
      // 绘制统计列数据
      const needTotalItems = this.needTotalList.map((item) => {
        return (<span style="margin-right: 12px">
          {item.title}总计 <a style="font-weight: 600">{!item.customRender ? item.total : item.customRender(item.total)}</a>
        </span>)
      })

      // 绘制 清空 按钮
      const clearItem = (typeof this.alert.clear === 'boolean' && this.alert.clear) ? (
        this.renderClear(this.clearSelected)
      ) : (this.alert !== null && typeof this.alert.clear === 'function') ? (
        this.renderClear(this.alert.clear)
      ) : null

      // 绘制 alert 组件
      return (
        <a-alert showIcon={true} style="margin-bottom: 16px">
          <template slot="message">
            {/* <span style="margin-right: 12px">已选择: <a style="font-weight: 600">{this.selectedRows.length}</a></span> */}
            <span style="margin-right: 12px">已选择: <a style="font-weight: 600">{this.selectedRowKeys.length}</a></span>
            {needTotalItems}
            {clearItem}
          </template>
        </a-alert>
      )
    },

    parseColumns (columns) {
      const parseColumns = []

      const serial = (text, record, index) => {
        return <div>{(index + 1) + (this.pageNum - 1) * this.pageSize}</div>
      }

      const image = (text, record, index) => {
        if (_.isArray(text)) {
          return text.map((item) => {
            return <a><img src={item} style="width: 15px;height: 15px;border: #e8e8e8 solid 1px" /></a>
          })
        }

        return <a>
          <img src={text} style="width: 15px;height: 15px;border: #e8e8e8 solid 1px" />
        </a>
      }

      columns.forEach((column) => {
        if (column['scopedSlots'] !== undefined) {
          if (column['scopedSlots']['customRender'] === 'serial') {
            column['customRender'] = serial
          } else if (column['scopedSlots']['customRender'] === 'image') {
            column['customRender'] = image
          }
        }
        parseColumns.push(column)
      })

      return parseColumns
    },

    addItem (item) {
      this.localPagination.total++
      this.localPagination.pageSize++
      this.localDataSource.unshift(item)
    },
    removeItem (key, keyName) {
      this.localPagination.total--
      this.localPagination.pageSize--
      this.localDataSource = this.localDataSource.filter((value, index, array) => {
        return value[keyName] != key
      })
    },
    getDataSource () {
      return this.localDataSource
    }
  },

  render () {
    const props = {}
    const localKeys = Object.keys(this.$data)
    const showAlert = (typeof this.alert === 'object' && this.alert !== null && this.alert.show) && typeof this.rowSelection.selectedRowKeys !== 'undefined' || this.alert

    Object.keys(T.props).forEach(k => {
      const localKey = `local${k.substring(0, 1).toUpperCase()}${k.substring(1)}`
      if (localKeys.includes(localKey)) {
        props[k] = this[localKey]
        return props[k]
      }
      if (k === 'rowSelection') {
        if (showAlert && this.rowSelection) {
          // 如果需要使用alert，则重新绑定 rowSelection 事件
          props[k] = {
            selectedRows: this.selectedRows,
            selectedRowKeys: this.selectedRowKeys,
            onChange: (selectedRowKeys, selectedRows) => {
              this.updateSelect(selectedRowKeys, selectedRows)
              typeof this[k].onChange !== 'undefined' && this[k].onChange(selectedRowKeys, selectedRows)
            }
          }

          if (this.rowSelection !== null && this.rowSelection.getCheckboxProps !== undefined) {
            props[k]['getCheckboxProps'] = this.rowSelection.getCheckboxProps
          }
          if (this.rowSelection !== null && this.rowSelection.type !== undefined) {
            props[k]['type'] = this.rowSelection.type
          }

          return props[k]
        } else if (!this.rowSelection) {
          // 如果没打算开启 rowSelection 则清空默认的选择项
          props[k] = null
          return props[k]
        }
      }

      props[k] = this[k]
      return props[k]
    })
    if (props['pagination'] != false) {
      props['pagination']['pageSizeOptions'] = this.pageSizeOptions
    }

    props.columns = this.parseColumns(props.columns)
    // console.log(this.localDataSource)
    // console.log(props.dataSource)
    // props.dataSource = this.localDataSource
    const table = (
      <a-table {...{ props, scopedSlots: { ...this.$scopedSlots } }} onChange={this.loadData}>
        { Object.keys(this.$slots).map(name => (<template slot={name}>{this.$slots[name]}</template>)) }
      </a-table>
    )

    return (
      <div class="table-wrapper">
        { showAlert ? this.renderAlert() : null }
        { table }
      </div>
    )
  }
}
