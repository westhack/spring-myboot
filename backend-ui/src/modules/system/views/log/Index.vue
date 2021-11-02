<template>
  <a-card :bordered="false">

    <edit-table
      ref="editTable"
      rowKey="id"
      :columns="columns"
      :table-action-bars="tableActionBars"
      :table-header-action-bars="tableHeaderActionBars"
      :create-api-url="api.create"
      :update-api-url="api.update"
      :list-api-url="api.list"
      :delete-api-url="api.delete"
      :delete-by-ids-api-url="api.deleteByIds"
      :scroll="tableScroll"
      :searchFormData="searchFormData"
      :isDownload="true"
      :defaultFormWidth="'300px'"
      :tableHeaderButtons="tableHeaderButtons"
      :showPopover="true"
      @change="onChange"
    ></edit-table>

  </a-card>
</template>

<script>

import { api, deleteAll } from '@/modules/system/api/log'
import httpResponse from '@/mixins/httpResponse'
import _ from 'lodash'
import { defaultFormData, defaultSearchFormData, columns } from './form'
import { httpResponseCode } from '@/constants/httpResponseCode'

export default {
  name: 'Api',
  components: {
  },
  mixins: [httpResponse],
  data () {
    const vm = this

    return {
      multiple: true,
      tableScroll: { x: 1200 },
      // 表头
      columns: columns,
      optionAlertShow: false,
      pageParam: 1,
      formData: defaultFormData,
      searchFormData: defaultSearchFormData,
      api: api,
      selectedRowKeys: [],
      selectedRows: [],
      tableHeaderButtons: [
        {
          'key': 'deleteAll',
          'icon': 'delete',
          'label': '删除全部',
          'callback': () => {
            vm.deleteAll().then(res => {
              // eslint-disable-next-line no-undef
              if (res.code == httpResponseCode.SUCCESS) {
                vm.$message.success(res.message)
                vm.$refs['editTable'].refresh(true)
              } else {
                vm.$message.error(res.message)
              }
            })
          },
          confirm: true,
          msg: '确定要全部数据？'
        }
      ],
      tableHeaderActionBars: [
        {
          'key': 'delete',
          'icon': '',
          'label': '批量删除',
          'callback': (e) => {
            console.log(e)
          },
          confirm: true,
          msg: '确定要删除？'
        },
        {
          'key': 'statusYes',
          'icon': '',
          'label': '显示',
          'callback': (e) => {
            console.log(e)
          },
          confirm: false,
          msg: ''
        },
        {
          'key': 'statusNo',
          'icon': '',
          'label': '隐藏',
          'callback': (e) => {
            console.log(e)
          },
          confirm: false,
          msg: ''
        }
      ],
      tableActionBars: [
        {
          'key': 'delete',
          'icon': '',
          'label': '删除',
          'callback': (e) => {
            console.log(e)
          },
          confirm: true,
          msg: '删除无法恢复'
        },
        {
          'key': 'statusYes',
          'icon': '',
          'label': '显示',
          'callback': (e) => {
            console.log(e)
          },
          confirm: false,
          msg: ''
        },
        {
          'key': 'statusNo',
          'icon': '',
          'label': '隐藏',
          'callback': (e) => {
            console.log(e)
          },
          confirm: false,
          msg: ''
        }
      ]
    }
  },
  created () {
    this.searchFormData = _.cloneDeep(defaultSearchFormData)
  },
  mounted () {},
  methods: {
    onChange (selectedRowKeys, selectedRows) {
      this.selectedRowKeys = selectedRowKeys
      this.selectedRows = selectedRows
    },

    handleSubmit (e) {
    },

    handleReset () {
    },

    handleSearchReset () {
    },
    deleteAll () {
      return deleteAll()
    }

  }
}
</script>

<style scoped>
</style>
