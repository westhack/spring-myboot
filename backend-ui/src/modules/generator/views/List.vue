<template>
  <a-card :bordered="false">

    <edit-table
      ref="editTable"
      rowKey="id"
      :columns="columns"
      :table-action-bars="tableActionBars"
      :table-header-action-bars="tableHeaderActionBars"
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

import { api, rollback } from '../api/api'
import { listColumns } from './form'
import httpResponse from '@/mixins/httpResponse'
import _ from 'lodash'
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
      columns: listColumns,
      optionAlertShow: false,
      pageParam: 1,
      formData: [],
      searchFormData: [],
      api: api,
      selectedRowKeys: [],
      selectedRows: [],
      tableHeaderButtons: [],
      tableHeaderActionBars: [],
      tableActionBars: [
        {
          'key': 'rollback',
          'icon': '',
          'label': '回滚',
          'callback': (e) => {
            vm.rollback(e.id)
          },
          confirm: true,
          msg: '确定要回滚'
        }
      ]
    }
  },
  created () {
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
    rollback (id) {
      return rollback({ id: id }).then(res => {
        if (res.code == httpResponseCode.SUCCESS) {
          this.$refs['editTable'].refresh()
          this.$message.success(res.message)
        } else {
          this.$message.error(res.message)
        }
      })
    }

  }
}
</script>

<style scoped>
</style>
