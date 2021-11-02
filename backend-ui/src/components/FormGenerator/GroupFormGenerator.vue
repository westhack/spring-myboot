<template>
  <div>

    <a-form
      :layout="formLayout"
      :form="form"
      @submit="handleSubmit"
    >
      <div :key="index" v-for="(_formData, index) in formData">

        <a-card size="small" :title="_formData.label" style="margin-bottom: 10px">
          <div :key="index2" v-for="(item, index2) in _formData.children">
            <a-form-item
              v-bind="formItemLayout"
              :label="item.label || item.name"
              :extra="item.extra"
              :style="{'display': item.type === 'hidden' ? 'none' : 'block'}"
            >
              <FormItemGenerator
                @formChange="formChange"
                :item="item"
                :options="item.data"
                :defaultValue="item.value"
                v-decorator="[
                  `${item.name}`,
                  {
                    initialValue: item.value,
                    validateTrigger: ['change', 'blur'],
                    rules: item.rules
                  }
                ]"
              />

            </a-form-item>
            <slot name="form-item" :item="item"> </slot>
          </div>
        </a-card>

      </div>

      <slot name="form-body"> </slot>

      <slot name="footer" v-if="showFooter">
        <a-form-item
          v-bind="footerItemLayout"
        >
          <a-button
            type="primary"
            html-type="submit"
          >
            保存
          </a-button>
          <a-button
            :style="{ marginLeft: '8px' }"
            @click="handleReset"
          >
            重置
          </a-button>
        </a-form-item>
      </slot>
    </a-form>

  </div>
</template>

<script>
import FormItemGenerator from './FormItemGenerator'
import httpResponse from '@/mixins/httpResponse'
import { axios } from '@/utils/request'
import _ from 'lodash'
import moment from 'moment'

import { parseValueType, functionCallback } from '@/components/FormGenerator/utils'

export default {
  name: 'GroupFormGenerator',
  components: { FormItemGenerator },
  mixins: [httpResponse],
  props: {
    fields: {
      type: [Array, Object],
      required: true
    },
    action: {
      type: [String],
      default: null
    },
    formItemLayout: {
      type: Object,
      default: function () {
        return {
        }
      }
    },
    footerItemLayout: {
      type: Object,
      default: function () {
        return {
        }
      }
    },
    formLayout: {
      type: String,
      default: 'vertical'
    },
    showFooter: {
      type: Boolean,
      default: true
    },
    groupData: {
      type: Boolean,
      default: false
    }
  },
  beforeCreate () {

  },
  created () {
    this.getFieldDecorator()
  },
  data () {
    const vm = this
    return {
      form: vm.$form.createForm(this),
      formData: vm.fields,
      spinning: false
    }
  },
  watch: {
    fields: function (val) {
      this.formData = this.fields
    }
  },
  methods: {
    handleSubmit (e) {
      e && e.preventDefault()

      if (this.action != null) {
        this.form.validateFields((err, values) => {
          console.log(err)
          values = this.getFieldsValue()
          if (!err) {
            axios({
              url: this.action,
              method: 'post',
              data: values
            }).then((res) => this.submitSuccess(res))
              .then((res) => {
                this.$emit('handleSubmit', res)
              })
              .catch(err => this.requestFailed(err))
              .finally(() => { this.spinning = false })
          }
        })
      } else {
        this.$emit('handleSubmit', e)
      }
    },

    getFieldDecorator () {
      _.each(this.fields, (field) => {
        this.form.getFieldDecorator(field.name)
      })
    },

    getValues () {
      const values = {}
      _.each(this.fields, (field) => {
        values[field.name] = field.value
      })

      return values
    },

    handleReset () {
      this.formData = _.cloneDeep(this.fields)
      this.form.resetFields()
      this.$emit('handleReset')
    },

    resetFields () {
      this.formData = _.cloneDeep(this.fields)
      this.form.resetFields()
    },

    setFieldsValue (obj) {
      this.form.setFieldsValue(obj)
    },

    getFieldValue (fieldName) {
      return this.form.getFieldValue(fieldName)
    },

    getFieldsValue (fieldNames) {
      const _values = this.form.getFieldsValue(fieldNames)
      console.log(_values)
      let fields = {}
      _.each(this.fields, (o) => {
        if (_.isArray(o.children)) {
          const f = _.keyBy(o.children, 'name')
          fields = _.merge(fields, f)
        } else {
          fields = _.merge(fields, o.children)
        }
      })

      const ret = {}
      const values = {}
      _.each(fields, (field, name) => {
        const value = _.get(_values, name)
        values[name] = value
      })
      console.log(values)
      _.each(values, (value, name) => {
        let _value = null

        if (!_.isEmpty(value)) {
          if (_.isArray(value) && value.length === 2 && moment.isMoment(value[0]) && moment.isMoment(value[1])) {
            let format = 'YYYY-MM-DD'
            if (fields[name]['format'] !== undefined) {
              format = fields[name]['format']
            }

            const start = value[0].toString()
            const end = value[1].toString()

            _value = []
            _value[0] = moment(start).format(format).toString()
            _value[1] = moment(end).format(format).toString()
          } else if (moment.isMoment(value)) {
            let format = 'YYYY-MM-DD'
            if (fields[name]['format'] !== undefined) {
              format = fields[name]['format']
            } else if (fields[name]['type'] === 'time-picker') {
              format = fields[name]['format'] ? fields[name]['format'] : 'hh:mm:ss'
            }

            _value = moment(value).format(format).toString()
          } else {
            _value = value
          }
        } else {
          _value = value
        }
        const type = _.has(fields[name], 'valueType') ? fields[name]['valueType'] : null
        if (type !== null) {
          _value = parseValueType(name, _value, fields[name])
        }

        const valueCallback = _.has(fields[name], 'valueCallback') ? fields[name]['valueCallback'] : null
        if (valueCallback) {
          _value = functionCallback(valueCallback)(_value, fields[name], values)
        }

        ret[name] = _value
      })
      return ret
    },

    getFieldsValue (fieldNames) {
      const _values = this.form.getFieldsValue(fieldNames)
      console.log(_values)
      let fields = {}
      _.each(this.fields, (o) => {
        if (_.isArray(o.children)) {
          const f = _.keyBy(o.children, 'name')
          fields = _.merge(fields, f)
        } else {
          fields = _.merge(fields, o.children)
        }
      })

      const ret = {}
      const values = {}
      _.each(fields, (field, name) => {
        const value = _.get(_values, name)
        values[name] = value
      })
      console.log(values)
      _.each(values, (value, name) => {
        let _value = null

        if (!_.isEmpty(value)) {
          if (_.isArray(value) && value.length === 2 && moment.isMoment(value[0]) && moment.isMoment(value[1])) {
            let format = 'YYYY-MM-DD'
            if (fields[name]['format'] !== undefined) {
              format = fields[name]['format']
            }

            const start = value[0].toString()
            const end = value[1].toString()

            _value = []
            _value[0] = moment(start).format(format).toString()
            _value[1] = moment(end).format(format).toString()
          } else if (moment.isMoment(value)) {
            let format = 'YYYY-MM-DD'
            if (fields[name]['format'] !== undefined) {
              format = fields[name]['format']
            } else if (fields[name]['type'] === 'time-picker') {
              format = fields[name]['format'] ? fields[name]['format'] : 'hh:mm:ss'
            }

            _value = moment(value).format(format).toString()
          } else {
            _value = value
          }
        } else {
          _value = value
        }
        const type = _.has(fields[name], 'valueType') ? fields[name]['valueType'] : null
        if (type !== null) {
          _value = parseValueType(name, _value, fields[name])
        }

        const valueCallback = _.has(fields[name], 'valueCallback') ? fields[name]['valueCallback'] : null
        if (valueCallback) {
          _value = functionCallback(valueCallback)(_value, fields[name], values)
        }

        ret[name] = _value
      })
      return ret
    },

    validateFields (callback) {
      if (callback && typeof callback === 'function') {
        this.form.validateFields(callback)
      }
    },

    setFormFields (fields) {
      this.formData = fields
    },

    isFunction (f) {
      return _.isFunction(f)
    },

    formChange () {
      this.$emit('formChange')
    }
  }
}

</script>
