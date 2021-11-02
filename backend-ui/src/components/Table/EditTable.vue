<template>
  <div>

    <div v-if="isSearch && !isEmpty(internalSearchFormData)">
      <search-form-generator
        v-show="showSearch"
        ref="search"
        :defaultShowCount="defaultShowSearchCount"
        :fields="internalSearchFormData"
        @handleSubmit="handleSearch"
      ></search-form-generator>
    </div>

    <div class="table-operator">
      <a-button type="primary" v-if="isSearch && !isEmpty(internalSearchFormData)" @click="() => { this.showSearch = !this.showSearch}">{{ showSearch ? '隐藏搜索' : '显示搜索' }}</a-button>
      <a-button type="primary" icon="plus" @click="handleAdd" v-if="isTableCreate">插入</a-button>
      <a-button type="primary" icon="plus" @click="handleCreate" v-if="isFormCreate">新建</a-button>
      <a-button type="primary" icon="download" @click="handleDownload" v-if="isDownload && hasShow" :loading="downloadLoading">导出选中</a-button>
      <a-button type="primary" icon="edit" @click="handleEdit" v-if="isFormUpdate && selectedRowKeys.length > 0">编辑选中</a-button>
      <a-button icon="reload" @click="handleReload">刷新</a-button>
      <a-button icon="copy" v-if="isClone && hasShow" @click="handleClone">复制</a-button>
      <a-button type="danger" icon="delete" v-if="isBatchDelete && hasShow && deleteByIdsApiUrl != ''" @click="handleDeleteSelect">删除</a-button>
      <a-button type="dashed" @click="tableOption" v-if="showAlert">{{ optionAlertShow && '关闭' || '开启' }} alert</a-button>

      <span v-if="tableHeaderButtons.length > 0" :key="i" v-for="(item, i) in tableHeaderButtons">
        <a-popconfirm v-if="item.confirm" :title="item.msg || '确定要' + item.label + '?'" okText="是" cancelText="否" @confirm="item.callback">
          <a-button :type="item.primary" :icon="item.icon"> {{ item.label }} </a-button>
        </a-popconfirm>
        <a-button v-else :type="item.primary" :icon="item.icon" @click="item.callback">{{ item.label }}</a-button>
      </span>

      <a-dropdown v-if="tableHeaderActionBars.length > 0 && hasShow">
        <a-button style="margin-left: 8px">
          批量操作 <a-icon type="down" />
        </a-button>
        <a-menu slot="overlay">
          <a-menu-item :key="bar.key" v-for="(bar) in tableHeaderActionBars">
            <a-popconfirm v-if="bar.confirm" :title="bar.msg || '确定要' + bar.label + '?'" okText="是" cancelText="否" @confirm="barCallback(bar.callback, selectedRows)">
              <a-icon :type="bar.icon" v-if="bar.icon"/> {{ bar.label }}
            </a-popconfirm>
            <a v-else @click="barCallback(bar.callback, selectedRows)"><a-icon :type="bar.icon" v-if="bar.icon" /> {{ bar.label }}</a>
          </a-menu-item>
        </a-menu>
      </a-dropdown>

      <a-popover placement="bottom" trigger="click">
        <template slot="content">
          <a-checkbox-group @change="columnChange" v-model="columnValue">
            <a-row v-for="(column, index) in defaultColumns" :key="index"><a-checkbox :value="column.dataIndex">{{ column.title }}</a-checkbox></a-row>
          </a-checkbox-group>
          <br/>
          <div style="text-align: right;margin-top: 10px">
            <a-input-number
              size="small"
              :min="500"
              :max="5000"
              :step="50"
              v-model="tableScroll.x"
              style="margin-right: 5px"/>
            <a-button size="small" @click="restColumn">重置</a-button></div>
        </template>
        <template slot="title">
          <span>列</span>
        </template>
        <a-button> <a-icon type="filter" /> 表格列</a-button>
      </a-popover>

      <slot name="table-operator"> </slot>

    </div>
    <a-form
      :form="form"
      @submit="handleSubmit"
    >
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
        :pageSizeOptions="pageSizeOptions"
      >

        <template
          v-for="col in tableColumns"
          :slot="col.name"
          slot-scope="text, record, index"
        >

          <div :key="col.name + index" v-if="has(text, 'editable') && text.editable === true && record[editKey]">
            <a-form-item>
              <form-item-generator
                :item="text"
                v-decorator="[
                  `editRow[${index}][${text.name}]`,
                  {
                    initialValue: text.value,
                    validateTrigger: ['change', 'blur'],
                    rules: text.rules
                  }
                ]"
              />
            </a-form-item>
          </div>

          <template v-else>
            <DataDetail :key="col.dataIndex + index" :detail="text" :showPopover="showPopover"></DataDetail>
          </template>

        </template>

        <span slot="detail" slot-scope="text, record, index">
          <DataDetail :key="index" :detail="text" :showPopover="showPopover"></DataDetail>
        </span>

        <span slot="action" slot-scope="text, record, index">
          <span v-if="isTableUpdate">
            <span v-if="record[editKey]">
              <a-popconfirm v-if="isTableSaveConfirm" title="确定要保存？" okText="是" cancelText="否" @confirm="handleSave(record, index)">
                <a>保存</a>
              </a-popconfirm>
              <a v-else @click="handleSave(record, index)">保存</a>
              <a-divider type="vertical" />
              <a-popconfirm v-if="isTableCancelConfirm" title="确定要取消？" okText="是" cancelText="否" @confirm="handleCancel(record, index)">
                <a>取消</a>
              </a-popconfirm>
              <a v-else @click="handleCancel(record, index)">取消</a>
            </span>
            <a v-else @click="handleUpdate(record, index)">编辑</a>
          </span>

          <a-divider type="vertical" v-if="isTableUpdate"/>
          <a @click="handleDeleteOne(record[rowKey])" v-if="isTableDelete && deleteApiUrl != ''">删除</a>
          <a-divider type="vertical" v-if="tableActionBars.length > 0"/>
          <a-dropdown v-if="tableActionBars.length > 0">
            <a class="ant-dropdown-link">更多</a>
            <a-menu slot="overlay" >
              <a-menu-item :key="bar.key" v-for="(bar) in tableActionBars">
                <a-popconfirm v-if="bar.confirm" :title="bar.msg || '确定要' + bar.label + '?'" okText="是" cancelText="否" @confirm="barCallback(bar.callback, record)">
                  <a-icon :type="bar.icon" v-if="bar.icon"/> {{ bar.label }}
                </a-popconfirm>
                <a v-else @click="barCallback(bar.callback, record)"><a-icon :type="bar.icon" v-if="bar.icon" /> {{ bar.label }}</a>
              </a-menu-item>
            </a-menu>
          </a-dropdown>
        </span>
      </s-table>
    </a-form>

    <div v-if="(isFormCreate || isFormUpdate) && internalFormData">
      <a-drawer
        :title="formTitle"
        :width="formWidth"
        @close="formClose"
        :visible="formVisible"
        :body-style="{ paddingBottom: '80px' }"
      >
        <form-generator ref="drawerForm" @handleSubmit="handleSubmit" :fields="internalFormData">
          <template slot="footer">
            <br>
            <div class="drawer-form-footer">
              <a-button type="primary" html-type="submit" :loading="formLoading">提交</a-button>
              <a-button :style="{ marginLeft: '8px' }" @click="formReset">重置</a-button>
              <a-button :style="{ marginLeft: '8px' }" @click="formClose">关闭</a-button>
            </div>
          </template>
        </form-generator>

      </a-drawer>

    </div>
  </div>
</template>

<script>

import { httpResponseCode } from '@/constants/httpResponseCode'
import { STable } from '@/components'
import httpResponse from '@/mixins/httpResponse'
import DataDetail from '@/components/DataView/DataDetail'
import _ from 'lodash'
import { setFormValue } from '@/utils/util'
import { axios } from '@/utils/request'
import moment from 'moment'
import { functionCallback, parseValueType } from '@/components/FormGenerator/utils'

export default {
  name: 'EditTable',
  components: {
    STable,
    DataDetail
  },
  mixins: [httpResponse],
  props: {
    scroll: {
      type: [Object],
      required: false,
      default: () => {
        return { x: 1200 }
      }
    },
    rowKey: {
      type: [String],
      required: false,
      default: 'id'
    },
    editKey: {
      type: String,
      required: false,
      default: '_editable'
    },
    isTableCreate: {
      type: Boolean,
      required: false,
      default: false
    },
    isTableUpdate: {
      type: Boolean,
      required: false,
      default: false
    },
    isFormCreate: {
      type: Boolean,
      required: false,
      default: false
    },
    isFormUpdate: {
      type: Boolean,
      required: false,
      default: false
    },
    isClone: {
      type: Boolean,
      required: false,
      default: false
    },
    isSearch: {
      type: Boolean,
      required: false,
      default: true
    },
    isBatchDelete: {
      type: Boolean,
      required: false,
      default: true
    },
    isTableDelete: {
      type: Boolean,
      required: false,
      default: true
    },
    isDblclickUpdate: {
      type: Boolean,
      required: false,
      default: false
    },
    isClickUpdate: {
      type: Boolean,
      required: false,
      default: false
    },
    isDownload: {
      type: Boolean,
      required: false,
      default: false
    },
    isTableSaveConfirm: {
      type: Boolean,
      required: false,
      default: true
    },
    isTableCancelConfirm: {
      type: Boolean,
      required: false,
      default: true
    },
    listApiUrl: {
      type: [String],
      required: true,
      default: null
    },
    createApiUrl: {
      type: [String],
      required: false,
      default: null
    },
    updateApiUrl: {
      type: [String],
      required: false,
      default: null
    },
    deleteApiUrl: {
      type: [String],
      required: false,
      default: null
    },
    deleteByIdsApiUrl: {
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
    formData: {
      type: [Array, Object],
      required: false,
      default: () => {
        return []
      }
    },
    searchFormData: {
      type: [Array, Object],
      required: false,
      default: () => {
        return []
      }
    },
    tableHeaderActionBars: {
      type: [Array],
      required: false,
      default: () => {
        return []
      }
    },
    tableHeaderButtons: {
      type: [Array],
      required: false,
      default: () => {
        return []
      }
    },
    tableActionBars: {
      type: [Array],
      required: false,
      default: () => {
        return []
      }
    },
    showPopover: {
      type: Boolean,
      required: false,
      default: false
    },
    defaultFormWidth: {
      type: [String, Number],
      required: false,
      default: '720px'
    },
    showAlert: {
      type: Boolean,
      required: false,
      default: false
    },
    pageSizeOptions: {
      type: Array,
      required: false,
      default: function () {
        return ['10', '20', '30', '50', '100', '500']
      }
    }
  },
  data () {
    const vm = this

    return {
      form: vm.$form.createForm(this),
      drawerForm: vm.$form.createForm(this),
      editable: false,
      tableScroll: vm.scroll,
      formTitle: '修改',
      formWidth: vm.defaultFormWidth,
      formVisible: false,
      formLoading: false,
      searchLoading: false,
      downloadLoading: false,
      searchVisible: false,
      loading: false,
      visible: false,
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
      items: [],
      // 加载数据方法 必须为 Promise 对象
      loadData: parameter => {
        vm.pageParam = parameter
        return vm.listRequest(Object.assign(parameter, { search: this.queryParam }))
          .then(res => {
            if (res.data.items) {
              vm.items = _.cloneDeep(res.data.items)
              const items = vm.parseItems(res.data.items)
              res.data.items = items
            }
            vm.dataSource = res.data

            return vm.dataSource
          })
      },
      selectedRowKeys: [],
      selectedRows: [],
      // custom table alert & rowSelection
      options: {
        alert: { show: false, clear: () => { this.selectedRowKeys = [] } },
        rowSelection: {
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
      internalFormData: [],
      oldFormData: [],
      internalSearchFormData: [],
      editRow: [],
      addKeys: [],
      customRow: (record, i) => {
        return {
          props: {},
          on: { // 事件
            click: (event) => {
              if (vm.isClickUpdate && vm.isTableUpdate) {
                vm.handleUpdate(record, i)
              }
            }, // 点击行
            dblclick: (event) => {
              if (vm.isDblclickUpdate && vm.isTableUpdate) {
                vm.handleUpdate(record, i)
              }
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
      columnValue: [],
      defaultColumns: [],
      showSearch: true
    }
  },
  created () {
    this.parseTableColumns()
    this.defaultColumns = _.cloneDeep(this.tableColumns)
    this.tableColumns.map(res => {
      this.columnValue.push(res.dataIndex)
    })

  },
  methods: {
    parseTableColumns () {
      this.oldFormData = _.cloneDeep(this.formData)
      this.internalFormData = this.formData
      this.internalSearchFormData = this.searchFormData
      let isForm = false
      let isSearch = false
      let isAction = true

      if (_.isEmpty(this.formData)) {
        isForm = true
      }
      if (_.isEmpty(this.internalSearchFormData)) {
        isSearch = true
      }

      const internalFormData = []
      const internalSearchFormData = []

      _.each(this.columns, (val) => {
        if (val['editable'] == true && val['dataIndex'] != this.rowKey) {
          val['editable'] = true
          val['label'] = ''
          val['value'] = val['value'] ? val['value'] : null
          val['type'] = val['type'] ? val['type'] : 'input'
          val['name'] = val['dataIndex']
          val['scopedSlots'] = { customRender: val['dataIndex'] }
        } else {
          if (val['dataIndex'] != this.rowKey) {
            // val['scopedSlots'] = { customRender: 'detail' }

            val['editable'] = false
            val['label'] = ''
            val['value'] = val['value'] ? val['value'] : null
            val['type'] = val['type'] ? val['type'] : 'input'
            val['name'] = val['dataIndex']
            val['scopedSlots'] = { customRender: val['dataIndex'] }
          }
        }

        if (val['dataIndex'] === 'action') {
          val['scopedSlots'] = { customRender: 'action' }
          isAction = false
        } else {
          if (isForm && val['isForm'] === true) {
            const val1 = _.cloneDeep(val)
            val1['label'] = val['title']
            val1['type'] = val['type'] ? val['type'] : 'input'
            val1['value'] = val['value'] ? val['value'] : null
            val1['operator'] = val['operator'] ? val['operator'] : '='
            val1['name'] = val['dataIndex']

            internalFormData.push(val1)
          }

          if (isSearch && val['isSearch'] === true) {
            const val1 = _.cloneDeep(val)
            val1['label'] = val['title']
            val1['type'] = val['type'] ? val['type'] : 'input'
            val1['value'] = val['value'] ? val['value'] : null
            val1['operator'] = val['operator'] ? val['operator'] : '='
            val1['name'] = val['dataIndex']
            internalSearchFormData.push(val1)
          }

          if (val['dataIndex'] == this.rowKey) {
            delete val['editable']
          }
        }

        if (_.isArray(val['options']) && _.isEmpty(val['filters'])) {
          const filters = []

          _.each(val['options'], (option) => {
            filters.push({
              'text': option['label'],
              'value': option['value']
            })
          })

          val['filters'] = filters
          val['onFilter'] = (value, record) => {
            return record[val['dataIndex']]['value'].indexOf(value) >= 0
          }
        }

        this.tableColumns.push(val)
      })

      if (isAction == true) {
        const val =
          {
            'title': '操作',
            'align': 'center',
            'width': '200px',
            'fixed': 'right',
            'scopedSlots': { customRender: 'action' }
          }

        this.tableColumns.push(val)
      }

      if (isForm) {
        this.oldFormData = _.cloneDeep(internalFormData)
        this.internalFormData = internalFormData
      }

      if (isSearch) {
        this.internalSearchFormData = internalSearchFormData
      }
    },

    tableOption () {
      this.optionAlertShow = !this.optionAlertShow
      this.$set(this.options.alert, 'show', this.optionAlertShow)
    },

    onSelectChange (selectedRowKeys, selectedRows) {
      this.selectedRowKeys = selectedRowKeys
      this.selectedRows = selectedRows
      this.$emit('change', selectedRowKeys, selectedRows)
      this.$emit('onChange', selectedRowKeys, selectedRows)
    },

    onChange (selectedRowKeys, selectedRows) {
      this.selectedRowKeys = selectedRowKeys
      this.selectedRows = selectedRows
    },

    toggleAdvanced () {
      this.advanced = !this.advanced
    },

    listRequest (parameter) {
      return axios({ url: this.listApiUrl, method: 'post', data: parameter })
    },
    createRequest (parameter) {
      return axios({ url: this.createApiUrl, method: 'post', data: parameter })
    },
    updateRequest (parameter) {
      return axios({ url: this.updateApiUrl, method: 'post', data: parameter })
    },
    deleteRequest (parameter) {
      return axios({ url: this.deleteApiUrl, method: 'post', data: parameter })
    },
    deleteByIdsRequest (parameter) {
      return axios({ url: this.deleteByIdsApiUrl, method: 'post', data: parameter })
    },

    parseItems (vals) {
      const items = []
      const vm = this
      _.each(vals, (item, i) => {
        item[this.editKey] = false
        const _item = {}
        _.each(item, (v, k) => {
          const col = _.find(vm.tableColumns, (obj) => { return obj.dataIndex === k && obj.dataIndex != vm.rowKey })
          if (k === vm.rowKey) {
            _item[k] = v
          } else if (col) {
            _item[k] = col
            _item[k]['value'] = v
          } else {
            _item[k] = v
          }
        })

        items[i] = _.cloneDeep(_item)
      })

      return items
    },

    handleSubmit (e) {
      e && e.preventDefault()

      if (this.createApiUrl != '') {
        this.drawerForm.validateFields((err, values) => {
          values = this.$refs['drawerForm'].getFieldsValue()
          if (!err) {
            if (values[this.rowKey]) {
              this.updateSubmit(values)
            } else {
              this.createSubmit(values)
            }
          }
        })
      } else {
        // eslint-disable-next-line handle-callback-err
        this.drawerForm.validateFields((err, values) => {
          values = this.$refs['drawerForm'].getFieldsValue()
          this.$emit('handleSubmit', values)
        })
      }
    },

    createSubmit (values) {
      delete values[this.rowKey]

      return new Promise((resolve, reject) => {
        this.createRequest(values)
          .then((res) => this.submitSuccess(res))
          .then((res) => {
            if (res.code == httpResponseCode.SUCCESS) {
              this.$refs['table'].refresh()
            }
            this.$emit('handleCreateAfter', res)
            resolve(res)
          })
          .catch(err => this.requestFailed(err))
          .finally((e) => {
            this.spinning = false
          })
      })
    },

    updateSubmit (values) {
      return new Promise((resolve, reject) => {
        this.updateRequest(values)
          .then((res) => this.submitSuccess(res))
          .then((res) => {
            if (res.code == httpResponseCode.SUCCESS) {
              this.$refs['table'].refresh()
            }
            this.$emit('handleUpdateAfter', res)
            resolve(res)
          })
          .catch(err => this.requestFailed(err))
          .finally(() => {
            this.spinning = false
          })
      })
    },

    deleteSubmit (p) {
      const that = this
      return new Promise((resolve, reject) => {
        that.deleteRequest(p)
          .then((res) => that.submitSuccess(res))
          .then((res) => {
            if (res.code === httpResponseCode.SUCCESS) {
              that.$refs['table'].refresh()
            }
            this.$emit('handleDeleteAfter', res)
            resolve(res)
          })
          .catch(err => that.requestFailed(err))
          .finally(() => {
          })
      })
    },

    deleteByIdsSubmit (p) {
      const that = this
      return new Promise((resolve, reject) => {
        that.deleteByIdsRequest(p)
          .then((res) => that.submitSuccess(res))
          .then((res) => {
            if (res.code === httpResponseCode.SUCCESS) {
              that.$refs['table'].refresh()
              that.selectedRowKeys = []
              that.selectedRows = []
            }
            this.$emit('handleDeleteAfter', res)
            resolve(res)
          })
          .catch(err => that.requestFailed(err))
          .finally(() => {
          })
      })
    },

    handleAdd () {
      const item1 = {}
      const item2 = {}
      const id = new Date().getTime()
      _.each(this.tableColumns, (col, k) => {
        if (col['dataIndex'] === this.rowKey) {
          item2[col['dataIndex']] = id
          item1[col['dataIndex']] = id
        } else {
          item2[col['dataIndex']] = col
          item2[col['dataIndex']]['value'] = null
          item1[col['dataIndex']] = null
        }
      })
      item2[this.editKey] = true
      this.addKeys[id] = true

      this.items.unshift(item1)
      this.$refs['table'].addItem(item2)
    },

    copyItem (item) {
      const _dataSource = {}
      const _item = {}
      _.each(item, (v, k) => {
        const col = _.find(this.tableColumns, (obj) => {
          return obj.dataIndex === k && obj.dataIndex != this.rowKey
        })
        if (k === this.rowKey) {
          const id = new Date().getTime()
          _dataSource[k] = id
          _item[k] = id
        } else if (col && col['editable'] == true) {
          _dataSource[k] = col
          _dataSource[k]['value'] = null
          _item[k] = col['value']
        } else {
          _dataSource[k] = v
          _item[k] = v
        }
      })

      _dataSource[this.editKey] = true

      return {
        'dataSource': _dataSource,
        'item': _item
      }
    },

    handleCreate () {
      this.formTitle = '新建'
      this.internalFormData = _.cloneDeep(this.oldFormData)
      this.formShow()
    },

    handleClone () {
      this.selectedRows.map((o) => {
        const item1 = _.cloneDeep(o)
        const item2 = {}
        const id = new Date().getTime()
        item1[this.rowKey] = id

        _.each(item1, (v, k) => {
          if (_.has(v, 'value')) {
            item2[k] = v['value']
          } else if (k === this.rowKey) {
            item2[k] = id
          } else {
            item2[k] = v
          }
        })
        item1[this.editKey] = true

        this.addKeys[id] = true

        this.items.unshift(item2)
        this.$refs['table'].addItem(item1)
      })
    },

    handleEdit () {
      this.formTitle = '编辑'
      if (this.selectedRows.length == 0) {
        this.$message.error('请选择一条数据')
      } else {
        const rowKey = this.selectedRows[0][this.rowKey]
        const item = _.find(this.items, (o) => { return o[this.rowKey] == rowKey })

        this.internalFormData = setFormValue(_.cloneDeep(this.oldFormData), item)
        this.formShow()
      }
    },

    handleUpdate (item, index) {
      item[this.editKey] = true
      this.editable = true

      this.$emit('handleUpdate', item)
    },

    handleDeleteOne (key) {
      if (this.addKeys[key] === true) {
        this.items = this.items.filter((value, index, array) => {
          return value[this.rowKey] != key
        })
        this.$refs['table'].removeItem(key, this.rowKey)
        return
      }

      const p = {}
      p[this.rowKey] = key
      if (this.deleteApiUrl) {
        this.handleDelete(p, 1)
      } else {
        this.$emit('handleDelete', p)
      }
    },

    handleDeleteSelect () {
      const ids = this.selectedRowKeys

      if (ids.length == 0) {
        this.$message.error('请选择一条数据')
      }

      _.each(ids, (key, i) => {
        if (this.addKeys[key] === true) {
          this.items = this.items.filter((value, index, array) => {
            return value[this.rowKey] != key
          })
          this.$refs['table'].removeItem(key, this.rowKey)
          delete ids[i]
        }
      })

      if (ids.length == 0) {
        return
      }

      const p = {}
      p[this.rowKey] = ids
      if (this.deleteByIdsApiUrl) {
        this.handleDelete(p, 2)
      } else {
        this.$emit('handleDeleteSelect', p)
      }
    },

    handleDelete (p, type) {
      const that = this
      if (this.deleteApiUrl) {
        this.$confirm({
          title: '确认删除选择数据?',
          content: '删除后无法恢复',
          okText: '确认',
          okType: 'danger',
          cancelText: '取消',
          onOk () {
            if (type == 1) {
              that.deleteSubmit(p)
            } else {
              that.deleteByIdsSubmit(p)
            }
          },
          onCancel () {
          }
        })
      } else {
        this.$emit('handleDelete', p)
      }
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

    formShow () {
      this.formVisible = true
    },

    formClose () {
      this.formVisible = false
    },

    formReset () {
      this.$refs['drawerForm'].resetFields()
    },

    searchShow () {
      this.searchVisible = true
    },

    searchClose () {
      this.searchVisible = false
    },

    handleReset () {
      this.$refs['drawerForm'].resetFields()
    },

    handleCancel (item, index) {
      item[this.editKey] = false
      this.editable = false
      const key = item[this.rowKey]
      const keys = _.keys(this.addKeys)
      const k = keys.find((v) => { return v == key })

      if (k) {
        this.items = this.items.filter((value, index, array) => {
          return value[this.rowKey] != key
        })
        this.$refs['table'].removeItem(key, this.rowKey)
      }

      this.$emit('handleCancelAfter', item)
    },

    findItem (key) {
      return _.find(this.items, (o) => { return o[this.rowKey] == key })
    },

    barCallback (call, record) {
      let val = null
      if (_.isArray(record)) {
        val = []
        _.each(record, (v, i) => {
          const key = v[this.rowKey]
          if (key) {
            const res = this.findItem(key)
            if (res) {
              val.push(res)
            }
          }
        })
      } else {
        val = this.findItem(record[this.rowKey])
      }

      if (_.isFunction(call)) {
        call(val)
      }
    },

    handleSave (item, index) {
      const updateData = this.getFieldsValue(index)
      const oldItem = this.findItem(item[this.rowKey])
      let newItem = _.cloneDeep(oldItem)
      this.editable = false

      _.each(updateData, (v, k) => {
        if (item[k]) {
          item[k]['value'] = v
          newItem[k] = v
        }
      })

      newItem = this.editableColumns(newItem)

      if (this.addKeys[newItem[this.rowKey]] === true) {
        const newKey = newItem[this.rowKey]
        if (this.createApiUrl) {
          this.createSubmit(newItem).then((res) => {
            if (res.code == httpResponseCode.SUCCESS) {
              item[this.editKey] = false
              delete this.addKeys[newKey]
            }
          })
        } else {
          this.$emit('handleCreate', oldItem, newItem)
        }
      } else {
        if (this.updateApiUrl) {
          this.updateSubmit(newItem).then(res => {
            if (res.code == httpResponseCode.SUCCESS) {
              item[this.editKey] = false
            }
          })
        } else {
          this.$emit('handleUpdate', oldItem, newItem)
        }
      }
    },

    editableColumns (newItem) {
      const tableColumns = this.tableColumns

      for (const key in newItem) {
        if (key === this.rowKey) {
          continue
        }

        const col = _.find(tableColumns, (o) => { return o['dataIndex'] === key })
        if (col && col['editable'] === true) {
          continue
        }

        delete newItem[key]
      }

      return newItem
    },

    getFieldsValue (index) {
      this.editRow = this.form.getFieldsValue()['editRow']
      const values = this.editRow[index]
      let fields = this.tableColumns
      if (_.isArray(this.tableColumns)) {
        fields = _.keyBy(this.tableColumns, 'name')
      }
      const ret = {}

      _.each(values, (value, name) => {
        let _value = null

        if (!_.isEmpty(value)) {
          if (_.isArray(value) && value.length === 2 && moment.isMoment(value[0]) && moment.isMoment(value[1])) {
            let format = 'YYYY-MM-DD'
            if (fields[name]['format'] !== undefined) {
              format = fields[name]['format']
            }

            const start = value[0].toString()
            const end = value[1].toString()

            _value = []
            _value[0] = moment(start).format(format).toString()
            _value[1] = moment(end).format(format).toString()
          } else if (moment.isMoment(value)) {
            let format = 'YYYY-MM-DD'
            if (fields[name]['format'] !== undefined) {
              format = fields[name]['format']
            } else if (fields[name]['type'] === 'time-picker') {
              format = fields[name]['format'] ? fields[name]['format'] : 'hh:mm:ss'
            }

            _value = moment(value).format(format).toString()
          } else {
            _value = value
          }
        } else {
          _value = value
        }

        const type = fields[name]['valueType'] || null
        if (type !== null) {
          _value = parseValueType(name, _value, fields[name])
        }

        const valueCallback = fields[name]['valueCallback'] || null
        if (valueCallback) {
          _value = functionCallback(valueCallback)(_value, fields[name])
        }

        ret[name] = _value
      })

      return ret
    },

    handleDownload () {
      if (this.selectedRowKeys.length == 0) {
        this.$message.error('选择一条记录')
        return
      }

      this.downloadLoading = true
      import('@/utils/Export2Excel').then(excel => {
        const tHeader = []
        const filterVal = []
        this.columns.map(res => {
          if (res.dataIndex != 'action') {
            tHeader.push(res.title)
            filterVal.push(res.dataIndex)
          }
        })

        const list = this.$refs['table']['localDataSource']
        const data = this.formatJson(filterVal, list)

        excel.export_json_to_excel({
          header: tHeader,
          data,
          filename: new Date().getTime(),
          autoWidth: true,
          bookType: 'xlsx'
        })
        this.downloadLoading = false
      })
    },
    formatJson (filterVal, jsonData) {
      const res = []

      jsonData.map(v => {
        if (_.find(this.selectedRowKeys, (o) => { return o == v[this.rowKey] })) {
          const row = filterVal.map(j => {
            if (v[j]['dataIndex'] != 'action') {
              return _.isObject(v[j]) ? v[j]['value'] : v[j]
            }
          })

          res.push(row)
        }
      })

      return res
    },

    columnChange (val) {
      const columns = []
      this.defaultColumns.map(res => {
        if (val.indexOf(res.dataIndex) != -1) {
          columns.push(res)
        }
      })

      this.tableColumns = columns
    },
    restColumn () {
      const columns = []
      const columnValue = []
      this.defaultColumns.map(res => {
        columns.push(res)
        columnValue.push(res.dataIndex)
      })

      this.tableColumns = columns
      this.columnValue = columnValue
    },

    has (o, name) {
      return _.has(o, name)
    },
    isEmpty (o) {
      return _.isEmpty(o)
    },
    refresh (b) {
      this.$refs['table'].refresh(b)
    }

  },
  watch: {
    'dataSource.items': function (val) {
    },
    'items': function (val) {
    },
    'selectedRowKeys': function () {

    }
  },
  computed: {
    'hasShow': function () {
      return this.selectedRowKeys.length > 0
    }
  }
}
</script>

<style scoped>

</style>
