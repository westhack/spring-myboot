<template>
  <div style="margin-top: 10px; padding-left: 5px; padding-right: 5px">
    <div style="display: flex;margin-bottom: 10px;flex-direction: row;justify-content: flex-end;">
      <a-input-number v-model="num" placeholder="数量" style="margin-right: 10px"/>
      <a-input-number v-model="price" placeholder="价格" style="margin-right: 10px"/>
      <a-input-number v-model="originalPrice" placeholder="原价" style="margin-right: 10px"/>
      <a-button @click="allSet">批量设置</a-button>
    </div>
    <a-table
      rowKey="id"
      bordered
      :data-source="dataSource"
      :columns="tableColumns"
      size="small"
      :pagination="false">
      <template slot="num" slot-scope="text, record">
        <editable-cell :text="text" @change="onCellChange(record.key, 'num', $event)" />
      </template>
      <template slot="price" slot-scope="text, record">
        <editable-cell :text="text" @change="onCellChange(record.key, 'price', $event)" :step="0.01" :precision="2"/>
      </template>
      <template slot="originalPrice" slot-scope="text, record">
        <editable-cell :text="text" @change="onCellChange(record.key, 'originalPrice', $event)" :step="0.01" :precision="2"/>
      </template>
    </a-table>
  </div>
</template>
<script>

import _ from 'lodash'
import EditableCell from './EditableCell'
export default {
  name: 'SkuItem',
  components: {
    EditableCell
  },
  props: {
    value: {
      type: [String, Array],
      required: false,
      default: function () {
        return []
      }
    },
    columns: {
      type: Array,
      required: false,
      default: function () {
        return []
      }
    }
  },
  data () {
    const vm = this
    const value = vm.parseValue(vm.value)

    return {
      dataSource: value,
      tableColumns: vm.columns,
      num: null,
      price: null,
      originalPrice: null,
      count: 2
    }
  },
  watch: {
    value: function (val) {
      const value = this.parseValue(this.value)
      this.dataSource = value
    },
    columns: function (val) {
      this.tableColumns = this.columns
    }
  },
  methods: {
    onCellChange (key, dataIndex, value) {
      const dataSource = [...this.dataSource]
      const target = dataSource.find(item => item.key === key)
      if (target) {
        target[dataIndex] = value
        this.dataSource = dataSource
      }
    },
    onDelete (key) {
      const dataSource = [...this.dataSource]
      this.dataSource = dataSource.filter(item => item.key !== key)
    },
    handleAdd () {
      const { count, dataSource } = this
      const newData = {}
      this.dataSource = [...dataSource, newData]
      this.count = count + 1
    },
    parseValue (val) {
      let value = val || []
      if (value != '' && _.isString(value)) {
        value = JSON.parse(value)
      }

      return value
    },
    allSet () {
      const dataSource = [...this.dataSource]
      dataSource.map((o, i) => {
        o.num = this.num
        o.price = this.price
        o.originalPrice = this.originalPrice
      })
      this.dataSource = dataSource
    }
  }
}
</script>
<style scoped>
.editable-cell {
  position: relative;
}

.editable-cell-input-wrapper,
.editable-cell-text-wrapper {
  padding-right: 24px;
}

.editable-cell-text-wrapper {
  padding: 5px 24px 5px 5px;
}

.editable-cell-icon,
.editable-cell-icon-check {
  position: absolute;
  right: 0;
  width: 20px;
  cursor: pointer;
}

.editable-cell-icon {
  line-height: 18px;
  display: none;
}

.editable-cell-icon-check {
  line-height: 28px;
}

.editable-cell:hover .editable-cell-icon {
  display: inline-block;
}

.editable-cell-icon:hover,
.editable-cell-icon-check:hover {
  color: #108ee9;
}

.editable-add-btn {
  margin-bottom: 8px;
}
/deep/ .ant-input-number-input {
  text-align: right;
  padding-right: 21px;
}
</style>
