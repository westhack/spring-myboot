<template>
  <a-card :bordered="false">

    <edit-table
      ref="editTable"
      rowKey="key"
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
      :formData="formData"
      :isTableUpdate="true"
      :isFormCreate="true"
      :isFormUpdate="true"
      :isTableSaveConfirm="false"
      :isTableCancelConfirm="false"
      :showPopover="true"
    ></edit-table>

  </a-card>
</template>

<script>

import { api } from '@/modules/system/api/redis'
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
      tableHeaderActionBars: [],
      tableActionBars: []
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

  }
}
</script>

<style scoped>
</style>
