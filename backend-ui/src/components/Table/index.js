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
      showTotal: total => `??????${total} ???`
    })
    this.needTotalList = this.initTotalList(this.columns)
    this.loadData()
  },
  methods: {
    /**
     * ????????????????????????
     * ??????????????? true, ???????????????????????????
     * @param Boolean bool
     */
    refresh (bool = false) {
      bool && (this.localPagination = Object.assign({}, {
        current: 1, pageSize: this.pageSize
      }))
      this.loadData()
    },
    /**
     * ??????????????????
     * @param {Object} pagination ???????????????
     * @param {Object} filters ????????????
     * @param {Object} sorter ????????????
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
      // ??????????????????????????????????????????????????????????????? r.page, r.totalCount, r.data
      // eslint-disable-next-line
      if ((typeof result === 'object' || typeof result === 'function') && typeof result.then === 'function') {
        result.then(r => {
          this.localPagination = Object.assign({}, this.localPagination, {
            current: r.page, // ?????????????????????????????????
            total: r.total, // ??????????????????????????????
            showSizeChanger: this.showSizeChanger,
            pageSize: (pagination && pagination.pageSize) || this.localPagination.pageSize
          })
          // ??????????????????????????????????????????????????????????????? 0 ,????????????????????????
          if (r.items.length === 0 && this.localPagination.current > 1) {
            this.localPagination.current--
            this.loadData()
            return
          }

          // ??????????????????????????????????????? r.totalCount ??? this.showPagination = false
          // ??????????????????????????????????????????????????????????????? table ????????????

          (!this.showPagination || !r.total && this.showPagination === 'auto') && (this.localPagination = false)
          this.localDataSource = r.items // ??????????????????????????????
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
     * ???????????????????????????????????? total ??????
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
     * ?????? table ????????????
     */
    clearSelected () {
      if (this.rowSelection) {
        this.rowSelection.onChange([], [])
        this.updateSelect([], [])
      }
    },
    /**
     * ???????????? table ?????????????????? clear ??????????????????????????????????????????
     * @param callback
     * @returns {*}
     */
    renderClear (callback) {
      if (this.selectedRowKeys.length <= 0) return null
      return (
        <a style="margin-left: 24px" onClick={() => {
          callback()
          this.clearSelected()
        }}>??????</a>
      )
    },
    renderAlert () {
      if (!this.alert.show) return null
      // ?????????????????????
      const needTotalItems = this.needTotalList.map((item) => {
        return (<span style="margin-right: 12px">
          {item.title}?????? <a style="font-weight: 600">{!item.customRender ? item.total : item.customRender(item.total)}</a>
        </span>)
      })

      // ?????? ?????? ??????
      const clearItem = (typeof this.alert.clear === 'boolean' && this.alert.clear) ? (
        this.renderClear(this.clearSelected)
      ) : (this.alert !== null && typeof this.alert.clear === 'function') ? (
        this.renderClear(this.alert.clear)
      ) : null

      // ?????? alert ??????
      return (
        <a-alert showIcon={true} style="margin-bottom: 16px">
          <template slot="message">
            {/* <span style="margin-right: 12px">?????????: <a style="font-weight: 600">{this.selectedRows.length}</a></span> */}
            <span style="margin-right: 12px">?????????: <a style="font-weight: 600">{this.selectedRowKeys.length}</a></span>
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
          // ??????????????????alert?????????????????? rowSelection ??????
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
          // ????????????????????? rowSelection ???????????????????????????
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
