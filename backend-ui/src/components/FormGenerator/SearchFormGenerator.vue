<template>

  <a-form
    class="ant-advanced-search-form"
    :layout="formLayout"
    :form="form"
    @submit="handleSubmit"
  >
    <slot name="header"> </slot>
    <a-row :gutter="24">
      <a-col
        v-for="(item, i) in toArray(formItems)"
        :key="i"
        :span="span"
        :xs="xs"
        :md="md"
        :sm="sm"
        :style="{ display: i < count ? 'block' : 'none' }"
      >
        <a-form-item :label="item.label">

          <div style="display: flex">
            <a-select
              v-show="showOperator"
              :key="i"
              :options="operators"
              :size="size"
              :disabled="item.operatorDisabled"
              v-decorator="[
                item.name + '[operator]',
                { initialValue: item.operator ? item.operator : '=' }
              ]"
              style="width: 100px; text-align: center;margin-right: 10px"
            >
            </a-select>
            <a-select
              v-show="showType"
              :key="'T' + i"
              :options="types"
              :size="size"
              :disabled="item.typeDisabled"
              @change="(val) => typeChange(val, item)"
              v-decorator="[
                item.name + '[type]',
                { initialValue: item.type ? item.type : 'input' }
              ]"
              style="width: 100px; text-align: center;margin-right: 10px"
            >
            </a-select>
            <div style="flex: 1">
              <FormItemGenerator
                :width="'100%'"
                :item="item"
                :options="item.data"
                :defaultValue="item.value"
                :isSearch="true"
                :size="size"
                v-decorator="[item.name + '[value]',
                              {
                                initialValue: item.value,
                                validateTrigger: ['change', 'blur'],
                                rules: item.rules
                              }
                ]"
              />
            </div>
          </div>

        </a-form-item>
      </a-col>
      <a-col
        v-if="showFooter"
        :span="span"
        :xs="xs"
        :md="md"
        :sm="sm"
        :style="{ textAlign: 'right', float: 'right' }"
      >
        <a-button
          :size="size"
          type="primary"
          html-type="submit"
        >
          {{ submitText }}
        </a-button>
        <a-button
          :size="size"
          :style="{ marginLeft: '8px' }"
          @click="handleReset"
        >
          {{ resetText }}
        </a-button>
        <a
          :size="size"
          :style="{ marginLeft: '8px', fontSize: '12px' }"
          @click="toggle"
        >
          {{ expand ? collapseText : expandText }} <a-icon :type="expand ? 'up' : 'down'" />
        </a>
      </a-col>
    </a-row>
    <slot name="footer"></slot>
  </a-form>

</template>

<script>
import AFormItem from 'ant-design-vue/es/form/FormItem'
import { operators } from './Operators'
import { types } from './Types'
import FormItemGenerator from './FormItemGenerator'
import moment from 'moment'
import _ from 'lodash'
import { deepClone } from '@/utils/util'
import { functionCallback, parseValueType } from '@/components/FormGenerator/utils'

export default {
  name: 'SearchFormGenerator',
  components: { AFormItem, FormItemGenerator },
  props: {
    fields: {
      type: [Array, Object],
      required: true
    },
    operators: {
      type: Array,
      default: function () {
        return operators
      }
    },
    types: {
      type: Array,
      default: function () {
        return types
      }
    },
    submitText: {
      type: [String],
      default: '搜索'
    },
    resetText: {
      type: [String],
      default: '重置'
    },
    expandText: {
      type: [String],
      default: '展开'
    },
    collapseText: {
      type: [String],
      default: '收起'
    },
    formItemLayout: {
      type: Object,
      default: function () {
        return {}
      }
    },
    formLayout: {
      type: String,
      default: 'inline'
    },
    showFooter: {
      type: Boolean,
      default: true
    },
    size: {
      type: String,
      default: 'default'
    },
    defaultShowCount: {
      type: Number,
      default: 3
    },
    span: {
      type: Number,
      default: 6
    },
    xs: {
      type: Number,
      default: 24
    },
    md: {
      type: Number,
      default: 12
    },
    sm: {
      type: Number,
      default: 6
    },
    showOperator: {
      type: Boolean,
      default: false
    },
    showType: {
      type: Boolean,
      default: false
    }
  },
  beforeCreate () {
    // this.form = this.$form.createForm(this)
  },
  data () {
    const vm = this
    return {
      form: this.$form.createForm(this),
      expand: false,
      formItems: vm.fields,
      oldFields: []
    }
  },
  created () {
    this.oldFields = _.cloneDeep(this.fields)
  },
  mounted () {
    // this.getFieldDecorator()
  },
  computed: {
    count () {
      const size = _.size(this.fields)
      return this.expand ? size : (this.defaultShowCount > 0 ? this.defaultShowCount : size)
    }
  },
  watch: {
    fields: function (val) {
      this.formData = this.fields
    }
  },
  methods: {
    handleSubmit (e) {
      e.preventDefault()
      // eslint-disable-next-line handle-callback-err
      this.form.validateFields((err, values) => {
        this.$emit('handleSubmit', e, this.getFieldsValue())
      })
    },

    handleReset () {
      this.formItems = deepClone(this.oldFields)
      this.$emit('handleReset')
    },

    resetFields () {
      this.formItems = deepClone(this.fields)
    },

    setFieldsValue (obj) {
      this.form.setFieldsValue(obj)
    },

    getFieldValue (fieldName) {
      this.form.getFieldValue(fieldName)
    },

    getFieldsValue (fieldNames) {
      const values = _.cloneDeep(this.form.getFieldsValue(fieldNames))
      const ret = []
      let i = 0
      _.each(this.formItems, (field) => {
        if ((field.type).indexOf('picker') !== -1) {
          if (_.isArray(values[field.name]['value']) && values[field.name]['value'].length > 1) {
            const format = field.format !== undefined ? field.format : 'YYYY-MM-DD'
            const start = (values[field.name]['value'][0]).toString()
            const end = (values[field.name]['value'][1]).toString()
            values[field.name]['value'][0] = moment(start).format(format).toString()
            values[field.name]['value'][1] = moment(end).format(format).toString()
          } else if (moment.isMoment(values[field.name]['value'])) {
            let format = null
            if ((field.type).indexOf('time') !== -1) {
              format = field.format !== undefined ? field.format : 'HH:mm:ss'
            } else {
              format = field.format !== undefined ? field.format : 'YYYY-MM-DD'
            }

            values[field.name]['value'] = (values[field.name]['value']).format(format).toString()
          }
        }

        field['value'] = values[field.name]['value']

        let _value = _.cloneDeep(values[field.name]['value'])
        const _field = _.cloneDeep(field)
        ret[i] = _field
        const type = _.has(_field, 'valueType') ? _field['valueType'] : null
        if (type !== null) {
          _value = parseValueType(_field.name, _value, _field)
        }
        const valueCallback = _.has(_field, 'valueCallback') ? _field['valueCallback'] : null
        if (valueCallback) {
          _value = functionCallback(valueCallback)(_value, _field, values)
        }
        // values[field.name]['value'] = _value

        ret[i]['name'] = field.name
        ret[i]['value'] = _value
        ret[i]['valueType'] = type

        i++
      })

      // _.each(this.formItems, (field, i) => {
      //   field['value'] = values[field.name]['value']
      //   ret[i] = field
      // })

      return ret
    },

    validateFields (callback) {
      if (callback || typeof callback === 'function') {
        this.form.validateFields(callback)
      }
    },

    toggle  () {
      this.expand = !this.expand
    },

    isEmpty (name) {
      const val = this.form.getFieldsValue()
      if (val[name] === undefined || val[name] === null || val[name]['value'] === undefined || val[name]['value'] === null) {
        return false
      }

      return val[name]['value']
    },

    emitEmpty (name) {
      this.$nextTick(() => {
        const obj = {}
        obj[name + '[value]'] = null
        this.form.setFieldsValue(obj)
      })
    },

    typeChange (val, item) {
      item.type = val
    },

    toArray (val) {
      return _.toArray(val)
    },
    getFieldDecorator () {
      _.each(this.fields, (field) => {
        this.form.getFieldDecorator(field.name)
      })
    }
  }
}

</script>

<style scoped>
  .ant-advanced-search-form >>> .ant-form-item-label {
    width: 100px;
  }

  .ant-advanced-search-form {
    _padding: 24px;
    _background: #fbfbfb;
    _border: 1px solid #d9d9d9;
    border-radius: 6px;
    padding-bottom: 10px;
  }

  .ant-advanced-search-form >>> .ant-form-item {
    display: flex;
  }

  .ant-advanced-search-form >>> .ant-form-item-control-wrapper {
    flex: 1;
  }

  .ant-advanced-search-form >>> .ant-form-item-control {
    line-height: unset!important;
  }

  #components-form-advanced-search >>> .ant-form {
    max-width: none;
  }
  #components-form-advanced-search >>> .search-result-list {
    margin-top: 16px;
    border: 1px dashed #e9e9e9;
    border-radius: 6px;
    background-color: #fafafa;
    min-height: 200px;
    text-align: center;
    padding-top: 80px;
  }

  #components-form-advanced-search >>>  .anticon-close-circle {
    cursor: pointer;
    color: #ccc;
    transition: color 0.3s;
    font-size: 12px;
  }
  #components-form-advanced-search >>>  .anticon-close-circle:hover {
    color: #999;
  }
  #components-form-advanced-search >>>  .anticon-close-circle:active {
    color: #666;
  }

</style>
