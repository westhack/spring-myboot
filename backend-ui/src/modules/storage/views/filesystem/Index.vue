<template>
  <a-card :bordered="false">

    <edit-table
      ref="editTable"
      rowKey="id"
      :columns="columns"
      :table-action-bars="tableActionBars"
      :list-api-url="api.list"
      :update-api-url="api.update"
      :delete-api-url="api.delete"
      :delete-by-ids-api-url="api.deleteByIds"
      :scroll="tableScroll"
      :show-popover="true"
      :isTableCreate="false"
      :isTableUpdate="true"
      :isFormCreate="false"
      :isFormUpdate="false"
      :isClone="false"
      :isDblclickUpdate="false"
      :isDownload="true"
    ></edit-table>

  </a-card>
</template>

<script>

import { api } from '@/modules/storage/api/filesystem'
import httpResponse from '@/mixins/httpResponse'
import { columns } from './form'

export default {
  name: 'Filesystem',
  mixins: [httpResponse],
  data () {
    const vm = this

    return {
      multiple: true,
      tableScroll: { x: 1600 },
      // 表头
      columns: columns,
      optionAlertShow: false,
      pageParam: 1,
      formData: [],
      searchFormData: [],
      api: api,
      selectedRowKeys: [],
      selectedRows: [],
      tableActionBars: [
        {
          'key': 'copy',
          'icon': 'copy',
          'label': '复制列',
          'value': 1,
          'callback': (e) => {
            vm.$copyText(JSON.stringify(e)).then(message => {
              console.log('copy', message)
              vm.$message.success('复制完毕')
            }).catch(err => {
              console.log('copy.err', err)
              vm.$message.error('复制失败')
            })
          },
          confirm: false,
          msg: ''
        }
      ]
    }
  },
  created () {
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
