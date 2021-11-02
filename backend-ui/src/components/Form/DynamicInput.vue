<template>
  <div>
    <div>水平显示：<a-switch v-model="layoutType" @change="changeLayout"/></div>
    <draggable
      :list="inputValue"
      class="dragArea"
      @update="onDragItemEnd"
      :options="{animation: 120, filter: '.drag__nomove' }">
      <div
        v-for="(v, index) in inputValue"
        :key="index"
      >

        <div :class="inLayout == 'inline' ? 'ant-form ant-form-inline' : ''" style="margin-bottom:10px;display: flex; padding: 5px 10px;background: #f7fafc;cursor: move;border: 1px #e8e8e8 solid">
          <div style="width: 100%;position:relative" :class="inLayout == 'inline' ? 'form-inline' : ''">
            <i
              class="item-delete"
              v-if="inputValue.length > 1"
              :disabled="inputValue.length === 1"
              @click="() => remove(index)"
            >
              <a-icon type="close" />
            </i>
            <div :key="index2" v-for="(item, index2) in inputFields">

              <a-form-item
                v-bind="itemLayout"
                :label="item.label"
                :extra="item.extra"
                :style="{'display': ( item.type === 'hidden' || item.hidden === true )? 'none' : ''}"
              >
                <slot name="form-item-before" :item="item"> </slot>
                <s-form-item-generator
                  :item="item"
                  :options="item.data"
                  v-model="v[item.name]"
                />

              </a-form-item>
              <slot name="form-item-after" :item="item"> </slot>
            </div>

          </div>
        </div>

      </div>

    </draggable>
    <div>
      <a-button type="dashed" style="width: 100%;" @click="add" v-show="!addShow">
        <a-icon type="plus" /> 添加
      </a-button>
    </div>
  </div>
</template>

<script>

import _ from 'lodash'
import draggable from 'vuedraggable'
import { functionCallback, parseValueType } from '@/components/FormGenerator/utils'

export default {
  name: 'DynamicInput',
  components: {
    draggable
  },
  props: {
    limit: {
      type: Number,
      required: false,
      default: 0
    },
    layout: {
      type: String,
      required: false,
      default: '' // inline
    },
    itemLayout: {
      type: Object,
      default: function () {
        return {}
      }
    },
    value: {
      type: [String, Array],
      required: false,
      default: function () {
        return []
      }
    },
    isColor: {
      type: Boolean,
      default: true
    },
    fields: {
      type: [Array, String],
      default: function () {
        return [
          {
            'label': '图片名称',
            'name': 'imgName',
            'value': '',
            'type': 'input'
          },
          {
            'label': '图片地址',
            'name': 'imgUrl',
            'value': '',
            'type': 'single-select-image'
          },
          {
            'label': '连接地址',
            'name': 'linkUrl',
            'value': '',
            'type': 'input'
          }
        ]
      }
    }
  },
  data () {
    const vm = this
    const fields = vm.parseFields(vm.fields)
    const value = vm.parseValue(vm.value)
    return {
      keys: [],
      inputValue: value,
      inputFields: fields,
      inLayout: vm.layout,
      layoutType: vm.layout == 'inline'
    }
  },
  beforeCreate () {
  },
  watch: {
    'inputValue': function (val) {
    },
    'value': function (val) {
      this.inputValue = this.parseValue(this.value)

      this.emitValue()
    },
    'fields': function (val) {
      this.inputFields = this.parseFields(this.fields)
    }
  },
  computed: {
    addShow: function () {
      return this.limit != 0 && this.inputValue.length >= this.limit
    },
    list: function () {
      return this.inputValue
    }
  },
  methods: {
    onDragItemEnd () {

    },
    remove (index) {
      const newInputValue = this.inputValue.slice()
      newInputValue.splice(index, 1)
      this.inputValue = newInputValue
      this.emitValue()
    },

    add () {
      const val = {}

      _.each(this.inputFields, (o) => {
        if (o.name) {
          val[o.name] = o.value || null
        }
      })

      this.inputValue.push(val)
    },

    valueChange () {
      this.emitValue()
    },

    emitValue () {
      const values = this.inputValue
      let fields = this.inputFields
      if (_.isArray(fields)) {
        fields = _.keyBy(fields, 'name')
      }

      _.each(values, (item, i) => {
        _.each(item, (o, name) => {
          let _value = o
          const type = _.has(fields[name], 'valueType') ? fields[name]['valueType'] : null
          if (type !== null) {
            _value = parseValueType(name, _value, fields[name])
          }

          const valueCallback = _.has(fields[name], 'valueCallback') ? fields[name]['valueCallback'] : null
          if (valueCallback) {
            _value = functionCallback(valueCallback)(_value, fields[name], values)
          }

          values[i][name] = _value
        })
      })
      this.$emit('input', values)
      this.$emit('change', values)
    },

    parseValue (val) {
      let value = val || []
      if (value != '' && _.isString(value)) {
        value = JSON.parse(value)
        this.$emit('input', value)
      }

      return value
    },
    parseFields (val) {
      let value = val || []
      if (value != '' && _.isString(value)) {
        value = JSON.parse(value)
      }

      return value
    },

    formChange (val) {
      this.$emit('input', this.inputValue)
      this.$emit('change', this.inputValue)
    },
    changeLayout (b) {
      if (this.layoutType) {
        this.inLayout = 'inline'
      } else {
        this.inLayout = ''
      }
    }
  }
}
</script>
<style scoped>
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
/deep/ .item-delete {
  position: absolute;
  top: -14px;
  right: -17px;
  height: 16px;
  width: 16px;
  line-height: 16px;
  background: rgba(153, 153, 153, 0.7);
  color: #fff;
  border-radius: 50%;
  text-align: center;
  cursor: pointer;
  font-size: 12px;
  -webkit-transition: background-color .3s ease-out, border-color .3s ease-out;
  transition: background-color .3s ease-out, border-color .3s ease-out; }
/deep/ .item-delete:hover {
  background: #000; }

/deep/ .ant-divider-horizontal {
  display: block;
  clear: both;
  width: 100%;
  min-width: 100%;
  height: 1px;
  margin: 24px 0;
  margin-bottom: 5px;
  margin-top: 0px;
}
/deep/ .form-inline {
  display: flex;
}
</style>
