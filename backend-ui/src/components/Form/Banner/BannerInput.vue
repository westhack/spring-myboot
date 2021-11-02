<template>
  <div>
    <DynamicInput :fields="fields" v-model="inputValue" :layout="layout" @formChange="formChange"></DynamicInput>
  </div>
</template>

<script>

import _ from 'lodash'
import DynamicInput from '../DynamicInput'

export default {
  name: 'BannerInput',
  components: {
    DynamicInput
  },
  props: {
    value: {
      type: [String, Array],
      required: false,
      default: function () {
        return []
      }
    },
    layout: {
      type: String,
      required: false,
      default: '' // inline
    }
  },
  data () {
    const vm = this
    const value = vm.parseValue(vm.value)
    return {
      inputValue: value,
      fields: [
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
  },
  beforeCreate () {
  },
  watch: {
    'inputValue': (val) => {
    },
    'value': function (val) {
      this.inputValue = this.parseValue(val)
      this.emitValue()
    }
  },
  methods: {

    valueChange () {
      this.emitValue()
    },

    emitValue () {
      this.$emit('input', this.inputValue)
      this.$emit('change', this.inputValue)
    },

    parseValue (val) {
      let value = val || []
      if (value != '' && _.isString(value)) {
        value = JSON.parse(value)
      }

      return value
    },

    formChange () {
      this.emitValue()
      this.$emit('formChange')
    }
  }
}
</script>
<style scoped>
</style>
