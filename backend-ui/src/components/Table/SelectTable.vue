<template>
  <div>
    <div>
      <a-button @click="drawerShow">选择</a-button>
      <span style="margin-left: 12px;margin-right: 12px">已选择: <a style="font-weight: 600">{{ rowKeys.length }}</a></span>
      <span v-if="valueType == false && rowKeys.length > 0">{{ rowKeys }}</span>
      <a @click="clearSelected" v-if="hasSelected">清空</a>
    </div>
    <div>
      <div :key="index" v-for="(row, index) in selectedRows" style="border: #e8e8e8 solid 1px;padding: 5px;margin-top: 5px;">
        <div :key="name" v-for="(col, name) in viewColumns">
          <DataViews :detail="col" :value="row[name]"></DataViews>
        </div>
      </div>
    </div>
    <a-drawer
      :title="drawerTitle"
      :width="drawerWidth"
      @close="drawerClose"
      :visible="drawerVisible"
      :body-style="{ paddingBottom: '80px' }"
    >

      <div v-if="isSearch && internalSearchFormData">
        <search-form-generator
          ref="search"
          :defaultShowCount="defaultShowSearchCount"
          :fields="internalSearchFormData"
          @handleSubmit="handleSearch"
        ></search-form-generator>
      </div>

      <s-table
        ref="table"
        :rowKey="rowKey"
        bordered
        size="small"
        :columns="tableColumns"
        :data="loadData"
        :alert="options.alert"
        :rowSelection="options.rowSelection"
        :scroll="tableScroll"
        :customRow="customRow"
      >
      </s-table>

      <br>
      <div class="drawer-form-footer">
        <a-button icon="reload" @click="handleReload">刷新</a-button>
        <a-button type="dashed" @click="tableOption" v-if="showAlert">{{ optionAlertShow && '关闭' || '开启' }} alert</a-button>
        <a-button type="primary" @click="handleSelect">提交</a-button>
        <a-button :style="{ marginLeft: '8px' }" @click="drawerClose">关闭</a-button>
      </div>

    </a-drawer>

  </div>
</template>

<script>

import { STable } from '@/components'
import httpResponse from '@/mixins/httpResponse'
import Down from './Down'
import _ from 'lodash'
import { axios } from '@/utils/request'
import DataViews from '@/components/DataView/DataViews'

export default {
  name: 'SelectTable',
  components: {
    STable,
    Down,
    DataViews
  },
  mixins: [httpResponse],
  props: {
    value: {
      type: [String, Array, Object, Number],
      required: false,
      default: () => {
        return null
      }
    },
    scroll: {
      type: [Object],
      required: false,
      default: () => {
        return { x: 1200 }
      }
    },
    width: {
      type: [String, Number],
      required: false,
      default: 720
    },
    rowKey: {
      type: [String],
      required: false,
      default: 'id'
    },
    listApiUrl: {
      type: [String],
      required: false,
      default: null
    },
    defaultShowSearchCount: {
      type: Number,
      default: 1
    },
    columns: {
      type: [Array, Object],
      required: true
    },
    viewColumns: {
      type: Object,
      required: false,
      default: () => {
        return null
      }
    },
    searchFormData: {
      type: [Array, Object],
      required: false,
      default: () => {
        return []
      }
    },
    multiple: {
      type: Boolean,
      required: false,
      default: false
    },
    valueType: {
      type: Boolean,
      required: false,
      default: false
    },
    isSearch: {
      type: Boolean,
      required: false,
      default: true
    }
  },
  data () {
    const vm = this

    return {
      tableScroll: vm.scroll,
      drawerTitle: '选择',
      drawerWidth: vm.width,
      drawerVisible: false,
      searchLoading: false,
      searchVisible: false,
      loading: false,
      showAlert: true,
      // 高级搜索 展开/关闭
      advanced: false,
      // 查询参数
      queryParam: null,
      // 表头
      tableColumns: [],
      dataSource: {
        items: [],
        page: 1,
        pageSize: 10,
        total: 0
      },
      // 加载数据方法 必须为 Promise 对象
      loadData: parameter => {
        vm.pageParam = parameter
        return vm.listRequest(Object.assign(parameter, { search: vm.queryParam }))
          .then(res => {
            if (res.data.items) {
              vm.dataSource = res.data
            }
            vm.setTableSelected()
            return vm.dataSource
          })
      },
      selectedRowKeys: [],
      selectedRows: [],
      // custom table alert & rowSelection
      options: {
        alert: { show: false, clear: () => { this.selectedRowKeys = [] } },
        rowSelection: {
          type: vm.multiple ? 'checkbox' : 'radio',
          selectedRowKeys: this.selectedRowKeys,
          onChange: this.onSelectChange,
          getCheckboxProps: record => ({
            props: {
              disabled: record.disabled === true
            }
          })
        }
      },
      optionAlertShow: false,
      pageParam: 1,
      internalSearchFormData: [],
      customRow: (record) => {
        return {
          props: {},
          on: { // 事件
            click: (event) => {
            }, // 点击行
            dblclick: (event, e) => {
            },
            contextmenu: (event) => {
            },
            mouseenter: (event) => {
            }, // 鼠标移入行
            mouseleave: (event) => {
            }
          }
        }
      },
      oldTableColumns: [],
      rowKeys: [],
      rows: []

    }
  },
  created () {
    this.parseTableColumns()
    if (this.valueType == true) {
      this.selectedRowKeys = _.keys(_.keyBy(this.value, this.rowKey))
      this.selectedRow = this.value
      this.rows = this.value
    } else {
      this.selectedRowKeys = this.value
    }
    this.rowKeys = this.getRowKeys()
  },
  mounted () {
  },
  computed: {
    hasSelected () {
      return this.selectedRowKeys.length > 0
      // return this.rowKeys.length > 0
    },
    type () {
      return this.multiple ? 'checkbox' : 'radio'
    }
    // rowKeys () {
    //   const val = []
    //   _.each(this.selectedRowKeys, (o) => {
    //     if (_.isObject(o) || _.isArray(o)) {
    //       val.push(o[this.rowKey])
    //     } else {
    //       val.push(o)
    //     }
    //   })
    //
    //   return val
    // }
  },
  methods: {
    parseTableColumns () {
      this.internalSearchFormData = this.searchFormData
      let isSearch = false

      if (_.isEmpty(this.internalSearchFormData)) {
        isSearch = true
      }

      const internalSearchFormData = []

      _.each(this.columns, (val) => {
        if (val['dataIndex'] == 'action') {
          val['filterDropdown'] = () => {
            return <Down options={this.columns} onChange={ this.filterDropdownChange }></Down>
          }
        }

        if (val['dataIndex'] != 'action' && isSearch && val['isSearch'] === true) {
          const val1 = _.cloneDeep(val)
          val1['label'] = val['title']
          val1['type'] = val['type'] ? val['type'] : 'input'
          val1['value'] = val['value'] ? val['value'] : null
          val1['operator'] = val['operator'] ? val['operator'] : '='
          val1['name'] = val['dataIndex']
          internalSearchFormData.push(val1)
        }

        this.tableColumns.push(val)
      })

      this.oldTableColumns = _.cloneDeep(this.tableColumns)
      if (isSearch) {
        this.internalSearchFormData = internalSearchFormData
      }
    },

    getRowKeys () {
      const val = []
      _.each(this.selectedRowKeys, (o) => {
        if (_.isObject(o) || _.isArray(o)) {
          if (_.has(o, this.rowKey)) {
            val.push(o[this.rowKey])
          }
        } else {
          val.push(o)
        }
      })

      return val
    },
    getRows () {
      const val = []
      _.each(this.selectedRows, (o) => {
        val.push(o)
      })

      return val
    },

    filterDropdownChange (val) {
      this.tableColumns = this.oldTableColumns.filter((o) => {
        if (val.find((c) => { return c == o.dataIndex })) {
          return o
        }
      })
    },

    setTableSelected () {
      const vm = this

      vm.$refs['table'].selectedRowKeys = vm.selectedRowKeys = vm.rowKeys
    },

    tableOption () {
      this.optionAlertShow = !this.optionAlertShow
      this.$set(this.options.alert, 'show', this.optionAlertShow)
    },

    onSelectChange (selectedRowKeys, selectedRows) {
      this.selectedRowKeys = selectedRowKeys
      this.selectedRows = selectedRows
    },

    toggleAdvanced () {
      this.advanced = !this.advanced
    },

    listRequest (parameter) {
      return axios({ url: this.listApiUrl, method: 'post', data: parameter })
    },

    handleSelect () {
      if (!this.hasSelected) {
        this.$message.error('请选择一条')
        return
      }
      if (!this.multiple) {
        if (this.valueType == true) {
          this.$emit('input', this.selectedRowKeys)
          this.$emit('change', this.selectedRowKeys)
        } else {
          this.$emit('input', this.selectedRows)
          this.$emit('change', this.selectedRows)
        }
      } else {
        if (this.valueType == true) {
          this.$emit('input', this.selectedRowKeys)
          this.$emit('change', this.selectedRowKeys)
        } else {
          this.$emit('input', this.selectedRows)
          this.$emit('change', this.selectedRows)
        }
      }

      this.drawerVisible = false
    },

    handleReload () {
      this.$refs['table'].refresh()
      this.$emit('handleReloadAfter')
    },

    handleSearch (e) {
      e.preventDefault()
      this.$refs['search'].validateFields((err, values) => {
        if (!err) {
          this.queryParam = this.$refs['search'].getFieldsValue()
          this.$refs['table'].refresh(true)
          this.$emit('handleSearchAfter', this.queryParam)
        }
      })
    },

    drawerShow () {
      this.drawerVisible = true
      // this.$refs['table'].selectedRowKeys = this.selectedRowKeys = []
    },

    drawerClose () {
      this.drawerVisible = false
    },

    searchShow () {
      this.searchVisible = true
    },

    searchClose () {
      this.searchVisible = false
    },

    clearSelected () {
      this.selectedRowKeys = []
      this.selectedRows = []
      if (this.$refs['table']) {
        this.$refs['table'].selectedRowKeys = []
        this.$refs['table'].selectedRows = []
      }
      this.emitValue()
    },
    emitValue () {
      if (this.valueType == true) {
        this.$emit('input', this.selectedRowKeys)
        this.$emit('change', this.selectedRowKeys)
      } else {
        this.$emit('input', this.selectedRows)
        this.$emit('change', this.selectedRows)
      }
    }

  },
  watch: {
    'selectedRowKeys': function (val) {
      this.rowKeys = this.selectedRowKeys
      this.emitValue()
    }
  }
}
</script>

<style scoped>
.drawer-form-footer button {
  margin-left: 10px;
}
</style>
