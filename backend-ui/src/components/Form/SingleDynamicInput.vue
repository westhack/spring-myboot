<template>
  <div>

    <div :class="layout == 'inline' ? 'ant-form ant-form-inline' : ''" style="display: flex; padding: 5px 10px;border: 1px #e8e8e8 solid">
      <div style="width: 100%;position:relative" :class="layout == 'inline' ? 'form-inline' : ''">
        <div :key="index" v-for="(item, index) in inputFields">

          <a-form-item
            :label="item.label || item.name"
            :extra="item.extra"
            :style="{'display': ( item.type === 'hidden' || item.hidden === true )? 'none' : ''}"
          >
            <slot name="form-item-before" :item="item"> </slot>
            <SFormItemGenerator
              :item="item"
              :options="item.data"
              v-model="inputValue[item.name]"
            />

          </a-form-item>
        </div>

      </div>
    </div>

  </div>
</template>

<script>

import _ from 'lodash'
import { functionCallback, parseValueType } from '@/components/FormGenerator/utils'
import SFormItemGenerator from '@/components/FormGenerator/SFormItemGenerator'

export default {
  name: 'SingleDynamicInput',
  components: {
    SFormItemGenerator
  },
  props: {
    layout: {
      type: String,
      required: false,
      default: '' // inline
    },
    value: {
      type: [String, Object],
      required: false,
      default: function () {
        return null
      }
    },
    isColor: {
      type: Boolean,
      default: true
    },
    fields: {
      type: [Array, String, Object],
      default: function () {
        return []
      }
    }
  },
  data () {
    const vm = this
    const fields = vm.parseFields(vm.fields)
    const value = vm.init(vm.value)
    return {
      keys: [],
      inputValue: value,
      inputFields: fields
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
  methods: {

    init (val) {
      this.inputFields = this.parseFields(this.fields)
      let value = val || {}
      if (value != '' && _.isString(value)) {
        value = JSON.parse(value)
      }
      const _value = {}
      if (_.isObject(this.inputFields) || _.isArray(this.inputFields)) {
        _.each(this.inputFields, (o) => {
          if (o.name) {
            _value[o.name] = o.value || null
          }
        })
      }

      value = _.merge(_value, value)
      this.$emit('change', value)
      this.$emit('input', value)

      return value
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

      _.each(values, (o, name) => {
        let _value = o
        const type = _.has(fields[name], 'valueType') ? fields[name]['valueType'] : null
        if (type !== null) {
          _value = parseValueType(name, _value, fields[name])
        }

        const valueCallback = _.has(fields[name], 'valueCallback') ? fields[name]['valueCallback'] : null
        if (valueCallback) {
          _value = functionCallback(valueCallback)(_value, fields[name], values)
        }
        values[name] = _value
      })

      this.$emit('input', values)
      this.$emit('change', values)
    },

    parseValue (val) {
      let value = val || {}
      if (value != '' && _.isString(value)) {
        value = JSON.parse(value)
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

    formChange () {
      this.$emit('input', this.inputValue)
      this.$emit('change', this.inputValue)
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
  top: -6px;
  right: -6px;
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
