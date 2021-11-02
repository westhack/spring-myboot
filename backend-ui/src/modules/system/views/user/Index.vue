<template>
  <a-card :bordered="false">
    <search-form-generator
      ref="search"
      :defaultShowCount="1"
      :fields="searchFormData"
      @handleSubmit="handleSearch"
      v-permission:userListquery></search-form-generator>

    <div class="table-operator">
      <a-button type="primary" icon="plus" @click="handleCreate" v-permission:user_create>新建</a-button>
      <a-button type="primary" icon="reload" @click="() => { this.$refs['table'].refresh() }">刷新</a-button>
      <a-popconfirm title="密码重置为 123456 吗?" okText="是" cancelText="否" @confirm="handleResetPassword" v-show="this.selectedRowKeys.length > 0">
        <a-button type="danger" icon="unlock">重置密码</a-button>
      </a-popconfirm>
      <a-button type="dashed" @click="tableOption">{{ optionAlertShow && '关闭' || '开启' }} alert</a-button>
      <a-dropdown v-if="selectedRowKeys.length > 0">
        <a-button style="margin-left: 8px">
          批量操作 <a-icon type="down" />
        </a-button>
        <a-menu slot="overlay">
          <a-menu-item key="delete" v-permission:user_delete>
            <a-popconfirm title="确定删除选择数据?" okText="是" cancelText="否" @confirm="handleTableTopBarAction({ key: 'delete'})">
              <div><a-icon type="delete" /> 删除</div>
            </a-popconfirm>
          </a-menu-item>

          <a-menu-item key="status_enable" v-permission:user_enable>
            <a-popconfirm title="确定设置正常?" okText="是" cancelText="否" @confirm="handleTableTopBarAction({ key: 'status_enable'})">
              <div ><a-icon type="eye" /> 正常</div>
            </a-popconfirm>
          </a-menu-item>

          <a-menu-item key="status_disable" v-permission:user_disable>
            <a-popconfirm title="确定设置禁用?" okText="是" cancelText="否" @confirm="handleTableTopBarAction({ key: 'status_disable'})">
              <div ><a-icon type="eye-invisible" /> 禁用</div>
            </a-popconfirm>
          </a-menu-item>

          <a-menu-item key="status_disable" v-permission:user_disable>
            <a-popconfirm title="确定设置待审核?" okText="是" cancelText="否" @confirm="handleTableTopBarAction({ key: 'status_uncheck'})">
              <div ><a-icon type="eye-invisible" /> 禁用</div>
            </a-popconfirm>
          </a-menu-item>
        </a-menu>
      </a-dropdown>

    </div>

    <s-table
      ref="table"
      rowKey="id"
      bordered
      size="small"
      :columns="columns"
      :data="loadData"
      :alert="options.alert"
      :rowSelection="options.rowSelection"
      :scroll="tableScroll"
    >

      <span slot="status" slot-scope="text, record">
        <a-tag :color="text | statusFilter">{{ record.statusText }}</a-tag>
      </span>

      <span slot="action" slot-scope="text, record">
        <a @click="handleEdit(record)" v-show="record.id !== 1 || userInfo.id === 1" v-permission:userListedit>编辑</a>
        <a-divider type="vertical" />
        <a-dropdown v-show="record.id !== 1">
          <a class="ant-dropdown-link">更多</a>
          <a-menu slot="overlay" >

            <a-menu-item key="delete" v-permission:userListdelete>
              <a-popconfirm title="确定删除?" okText="是" cancelText="否" @confirm="handleTableRowBarAction('delete', record)">
                <a-icon type="delete" /> 删除
              </a-popconfirm>
            </a-menu-item>

            <a-menu-item key="status_enable" v-if="record.status != status_enable" v-permission:user_enable>
              <a-popconfirm title="确定设置正常?" okText="是" cancelText="否" @confirm="handleTableRowBarAction('status_enable', record)">
                <a-icon type="eye" /> 正常
              </a-popconfirm>
            </a-menu-item>

            <a-menu-item key="status_disable" v-if="record.status != status_disable" v-permission:user_disable>
              <a-popconfirm title="确定设置禁用?" okText="是" cancelText="否" @confirm="handleTableRowBarAction('status_disable', record)">
                <a-icon type="eye-invisible" /> 禁用
              </a-popconfirm>
            </a-menu-item>

            <a-menu-item key="status_uncheck" v-if="record.status != status_uncheck" v-permission:user_disable>
              <a-popconfirm title="确定设置待审核?" okText="是" cancelText="否" @confirm="handleTableRowBarAction('status_uncheck', record)">
                <a-icon type="eye-invisible" /> 待审核
              </a-popconfirm>
            </a-menu-item>

          </a-menu>
        </a-dropdown>
      </span>
    </s-table>

    <a-drawer
      :title="formTitle"
      :width="formWidth"
      @close="formClose"
      :visible="formVisible"
      :body-style="{ paddingBottom: '80px' }"
    >
      <form-generator ref="form" @handleSubmit="handleSubmit" :fields="formData">
        <template slot="footer">
          <br>
          <div class="drawer-form-footer">
            <a-button type="primary" html-type="submit" :loading="submitLoading">提交</a-button>
            <a-button :style="{ marginLeft: '8px' }" @click="formClose">关闭</a-button>
          </div>
        </template>
      </form-generator>

    </a-drawer>

  </a-card>
</template>

<script>

import { httpResponseCode } from '@/constants/httpResponseCode'
import { getUserList, userCreate, userUpdate, userDeleteByIds, resetPassword, changeStatus } from '@/modules/system/api/user'
import { STable } from '@/components'
import httpResponse from '@/mixins/httpResponse'
import _ from 'lodash'
import { setFormValue } from '@/utils/util'
import { defaultFormData, defaultSearchFormData, columns } from './form'

const STATUS_ENABLE = 1
const STATUS_DISABLE = 0
const STATUS_UNCHECK = 2

export default {
  name: 'UserList',
  components: {
    STable
  },
  mixins: [httpResponse],
  data () {
    const vm = this

    return {
      tableScroll: { x: 1600 },
      formTitle: '修改',
      formWidth: '720',
      formVisible: false,
      submitLoading: false,
      searchLoading: false,
      searchVisible: false,
      loading: false,
      isEdit: 0,
      visible: false,
      // 高级搜索 展开/关闭
      advanced: false,
      // 查询参数
      queryParam: null,
      // 表头
      columns: columns,
      // 加载数据方法 必须为 Promise 对象
      loadData: parameter => {
        vm.pageParam = parameter
        return getUserList(Object.assign(parameter, { search: this.queryParam }))
          .then(res => {
            return res.data
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
              disabled: record.id === 1,
              id: record.id.toString()
            }
          })
        }
      },
      optionAlertShow: false,
      pageParam: 1,
      formData: defaultFormData,
      searchFormData: defaultSearchFormData,
      userInfo: this.$store.getters.userInfo,
      status_enable: 1,
      status_disable: 0,
      status_uncheck: 2
    }
  },
  filters: {
    statusFilter (status) {
      const statusMap = {
        0: '#f50',
        1: '#87d068'
      }

      return statusMap[status]
    },
    modalTitleFilter (status) {
      const statusMap = {
        0: '创建',
        1: '编辑'
      }
      return statusMap[status]
    }
  },
  created () {
    this.searchFormData = _.cloneDeep(defaultSearchFormData)
  },
  methods: {
    tableOption () {
      this.optionAlertShow = !this.optionAlertShow
      this.$set(this.options.alert, 'show', this.optionAlertShow)
    },

    onSelectChange (selectedRowKeys, selectedRows) {
      this.selectedRowKeys = selectedRowKeys
      this.selectedRows = selectedRows
    },

    onChange (selectedRowKeys, selectedRows) {
      this.selectedRowKeys = selectedRowKeys
      this.selectedRows = selectedRows
    },

    toggleAdvanced () {
      this.advanced = !this.advanced
    },

    restSearchForm () {
      this.searchFormData = _.cloneDeep(defaultSearchFormData)
    },

    handleTableTopBarAction ({ key }) {
      if (key === 'delete') {
        this.handleDelete(this.selectedRowKeys)
      } else if (key === 'status_enable') {
        this.handleStatus({ id: this.selectedRowKeys, status: STATUS_ENABLE })
      } else if (key === 'status_disable') {
        this.handleStatus({ id: this.selectedRowKeys, status: STATUS_DISABLE })
      } else if (key === 'status_uncheck') {
        this.handleStatus({ id: this.selectedRowKeys, status: STATUS_UNCHECK })
      }
    },

    handleTableRowBarAction (key, row) {
      if (key === 'delete') {
        this.handleDelete([row.id])
      } else if (key === 'status_enable') {
        this.handleStatus({ id: [row.id], status: STATUS_ENABLE })
      } else if (key === 'status_disable') {
        this.handleStatus({ id: [row.id], status: STATUS_DISABLE })
      } else if (key === 'status_uncheck') {
        this.handleStatus({ id: [row.id], status: STATUS_UNCHECK })
      }
    },

    handleCreate () {
      this.formTitle = '创建'
      this.formData = _.cloneDeep(defaultFormData)
      this.formData.password['rules'] = [{ required: true, message: '不能为空' }]
      this.formData.password['extra'] = ''

      this.formShow()
    },

    handleEdit (item) {
      this.formTitle = '编辑'
      this.formData = setFormValue(_.cloneDeep(defaultFormData), item)
      this.formShow()
    },

    handleDelete (ids) {
      const that = this
      userDeleteByIds({ id: ids })
        .then((res) => this.submitSuccess(res))
        .catch(err => this.requestFailed(err))
        .finally(() => {
          that.$refs['table'].refresh()
        })
    },

    handleStatus (req) {
      changeStatus(req)
        .then((res) => this.submitSuccess(res))
        .then((res) => {})
        .catch(err => this.requestFailed(err))
        .finally(() => {
          this.$refs['table'].refresh()
        })
    },

    handleSearch (e) {
      e.preventDefault()
      this.$refs['search'].validateFields((err, values) => {
        if (!err) {
          this.queryParam = this.$refs['search'].getFieldsValue()
          this.$refs['table'].refresh(true)
        }
      })
    },

    formShow () {
      this.formVisible = true
    },

    formClose () {
      this.formVisible = false
    },

    searchShow () {
      this.searchVisible = true
    },

    searchClose () {
      this.searchVisible = false
    },

    handleSubmit (e) {
      e.preventDefault()

      this.submitLoading = true
      this.$refs['form'].validateFields((err, values) => {
        values = this.$refs['form'].getFieldsValue()
        if (!err) {
          if (values.id > 0) {
            userUpdate(values)
              .then((res) => this.submitSuccess(res))
              .then((res) => {
                if (res.code === httpResponseCode.SUCCESS) {
                  this.$refs['table'].refresh()
                }
              })
              .catch(err => this.requestFailed(err))
              .finally(() => { this.submitLoading = false })
          } else {
            userCreate(values)
              .then((res) => this.submitSuccess(res))
              .then((res) => {
                if (res.code === httpResponseCode.SUCCESS) {
                  this.$refs['table'].refresh()
                }
              })
              .catch(err => this.requestFailed(err))
              .finally(() => { this.submitLoading = false })
          }
        } else {
          this.submitLoading = false
        }
      })
    },

    handleReset () {
      this.$refs['form'].resetFields()
    },

    handleSearchReset () {
      this.$refs['search'].resetFields()
    },

    async handleResetPassword () {
      if (this.selectedRowKeys.length == 0) {
        this.$message.error('选择一条数据')
        return
      }
      const res = await resetPassword({ id: this.selectedRowKeys })
      if (res.code === httpResponseCode.SUCCESS) {
        this.$message.success(res.message)
        this.$refs['table'].refresh()
      } else {
        this.$message.error(res.message)
      }
    }

  },
  watch: {
  }
}
</script>

<style scoped>
</style>
