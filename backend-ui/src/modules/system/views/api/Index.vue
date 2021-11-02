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
      :isDownload="true"
      :defaultFormWidth="'300px'"
      :searchFormData="searchFormData"
      :isTableUpdate="true"
      :isTableSaveConfirm="false"
      :isTableCancelConfirm="false"
    ></edit-table>

  </a-card>
</template>

<script>

import { api } from '@/modules/system/api/api'
import { STable } from '@/components'
import httpResponse from '@/mixins/httpResponse'
import _ from 'lodash'
import { defaultFormData, defaultSearchFormData, columns } from './form'

export default {
  name: 'Api',
  components: {
    STable
  },
  mixins: [httpResponse],
  data () {
    const vm = this

    return {
      tableVal: [6, 7],
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
      tableHeaderActionBars: [
        {
          'key': 'delete',
          'icon': '',
          'label': '批量删除',
          'value': 1,
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
          'value': 1,
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
          'value': 1,
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
          'value': 1,
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
          'value': 1,
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
          'value': 1,
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
  mounted () {

  },
  methods: {
    onSelectChange (selectedRowKeys, selectedRows) {
      this.selectedRowKeys = selectedRowKeys
      this.selectedRows = selectedRows
    },

    onChange (selectedRowKeys, selectedRows) {
      this.selectedRowKeys = selectedRowKeys
      this.selectedRows = selectedRows
    },

    handleSubmit (e) {
    },

    handleReset () {
    },

    handleSearchReset () {
    }

  },
  watch: {
    tableVal: function () {
      console.log(this.tableVal)
    }
  }
}
</script>

<style scoped>
</style>
