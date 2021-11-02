<template>
  <div>
    <div
      v-for="(item, index) in inputValue"
      :key="index"
      style="display: flex; padding: 5px 10px;flex-direction:column"
    >
      <div style="display: flex; padding: 5px 0px">
        <div style="width: 100px">
          <a-input
            @change="nameChange"
            v-model="item['skuName']"
            placeholder="名称"
            style="width: 100px;"
          />
        </div>
        <div style="width: 100%; margin-left: 10px">
          <a-select v-model="item['skuItems']" mode="tags" style="width: 100%" placeholder="内容" @change="itemsChange"></a-select>
        </div>

        <div style="margin-left: 5px">
          <a-icon
            v-if="inputValue.length > 1"
            class="dynamic-delete-button"
            type="minus-circle-o"
            :disabled="inputValue.length === 1"
            @click="() => remove(index)"
          />
        </div>
      </div>
    </div>
    <div style="padding-left: 5px;padding-right: 5px">
      <a-button type="dashed" style="width: 100%;" @click="add">
        <a-icon type="plus" /> 添加
      </a-button>
    </div>
    <div>
      <SkuItem :columns="columns" v-model="dataSource"></SkuItem>
    </div>
  </div>
</template>

<script>

import _ from 'lodash'
import SkuTag from './SkuTag'
import SkuItem from './SkuItem'
import { sku } from './utlis'

const selectSku = [
  { skuName: '容量', skuItems: ['16G', '64G', '128G'] },
  { skuName: '颜色', skuItems: ['土豪金', '银色', '黑色', 'pink'] },
  { skuName: '网络', skuItems: ['联通', '移动', '电信'] }
]

export default {
  name: 'SkuInput',
  components: {
    SkuItem,
    SkuTag
  },
  props: {
    value: {
      type: [String, Object],
      required: false,
      default: function () {
        return {
          sku: selectSku,
          items: []
        }
      }
    }
  },
  data () {
    const vm = this
    const value = vm.parseValue(vm.value)

    return {
      keys: [],
      inputValue: value['sku'],
      dataSource: value['items'],
      columns: []
    }
  },
  mounted () {
    this.gen(this.inputValue)
  },
  watch: {
    'inputValue': function (val) {
    },
    'value': function (val) {
      const value = this.parseValue(val)
      this.inputValue = value['sku']
      this.dataSource = value['items']
      this.emitValue()
    }
  },
  methods: {
    remove (k) {
      this.inputValue = this.inputValue.filter((val, key) => key !== k)

      this.emitValue()
    },

    add () {
      const val = {
        skuName: '',
        skuItems: []
      }

      this.inputValue.push(val)
    },

    emitValue () {
      this.gen(this.inputValue)

      const value = {
        sku: this.inputValue,
        items: this.dataSource
      }
      this.$emit('input', value)
      this.$emit('change', value)
    },

    parseValue (val) {
      let value = val || []
      if (value != '' && _.isString(value)) {
        value = JSON.parse(value)
      }

      return value
    },
    nameChange () {
      this.emitValue()
    },
    itemsChange (value) {
      this.emitValue()
    },
    gen (inputValue) {
      if (_.isEmpty(inputValue)) {
        return
      }

      const dataSource = []
      const items = sku.gen(inputValue)

      _.each(items, (o, i) => {
        const values = _.values(o)
        dataSource[i] = {
          'id': i + 1
        }
        _.each(values, (v, k) => {
          dataSource[i]['s_' + k] = v
        })

        dataSource[i]['num'] = null
        dataSource[i]['price'] = null
        dataSource[i]['originalPrice'] = null
      })
      const columns = []

      columns.push({
        title: '编号',
        dataIndex: 'id',
        width: '60px',
        align: 'center'
      })

      _.each(this.inputValue, (o, i) => {
        columns.push({
          title: o.skuName,
          dataIndex: 's_' + i,
          width: '100px',
          align: 'center'
        })
      })

      columns.push({
        title: '数量',
        dataIndex: 'num',
        width: '100px',
        align: 'center',
        scopedSlots: { customRender: 'num' }
      })
      columns.push({
        title: '价格',
        dataIndex: 'price',
        width: '100px',
        align: 'center',
        scopedSlots: { customRender: 'price' }
      })
      columns.push({
        title: '原价',
        dataIndex: 'originalPrice',
        width: '100px',
        align: 'center',
        scopedSlots: { customRender: 'originalPrice' }
      })
      this.columns = columns
      this.dataSource = []
      this.dataSource = dataSource
    }
  }
}
</script>
<style>
.dynamic-delete-button {
  cursor: pointer;
  position: relative;
  top: 4px;
  font-size: 24px;
  color: #999;
  transition: all 0.3s;
}
.dynamic-delete-button:hover {
  color: #777;
}
.dynamic-delete-button[disabled] {
  cursor: not-allowed;
  opacity: 0.5;
}
</style>
