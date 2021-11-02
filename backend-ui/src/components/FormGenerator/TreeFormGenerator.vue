<template>
  <div>

    <a-form
      :layout="formLayout"
      :form="form"
      :model="formData"
      @submit="handleSubmit"
    >
      <a-card size="small" :title="_form.label" v-for="(_form, i) in formData" :key="i" style="margin-bottom: 10px">
        <TreeFormItem :fields="_form.children" :name="_form.name"></TreeFormItem>
      </a-card>
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
import TreeFormItem from './TreeFormItem'
import httpResponse from '@/mixins/httpResponse'
import { axios } from '@/utils/request'
import _ from 'lodash'
import moment from 'moment'

import { parseValueType, functionCallback, formatValue } from '@/components/FormGenerator/utils'

export default {
  name: 'TreeFormGenerator',
  components: {
    FormItemGenerator,
    TreeFormItem
  },
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
    }
  },
  beforeCreate () {

  },
  mounted () {
  },
  data () {
    const vm = this
    return {
      form: vm.$form.createForm(this),
      formData: vm.fields,
      oldFormData: [],
      spinning: false
    }
  },
  watch: {
    fields: function (val) {
      if (_.isEmpty(this.formData)) {
        this.oldFormData = _.cloneDeep(this.fields)
      }
      this.formData = this.fields
    },
    formData: function (val) {
      console.log(this.formData)
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

    handleReset () {
      console.log(this.formData)
      this.formData = _.cloneDeep(this.oldFormData)
      this.$refs['form'].resetFields()
      this.$emit('handleReset')
    },

    resetFields () {
      this.formData = _.cloneDeep(this.oldFormData)
      this.$refs['form'].resetFields()
    },

    getFieldsValue (fieldNames) {
      let _values = this.form.getFieldsValue(fieldNames)
      _values = this.treeParseData(this.formData, _values)
      return _values
    },

    treeParseData (val, values) {
      const _parseData = (tree, keyName) => {
        _.each(tree, (_tree) => {
          const keyName1 = keyName + '.' + _tree['name']
          if (_tree.children && _tree.children.length > 0) {
            _parseData(_tree.children, keyName1)
          } else {
            const v = _.get(values, keyName1)
            const v2 = formatValue(_tree, _tree['name'], v, values)
            _.set(values, keyName1, v2)
          }
        })
      }

      _.each(val, (tree, name) => {
        _parseData(tree.children, name)
      })

      return values
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
