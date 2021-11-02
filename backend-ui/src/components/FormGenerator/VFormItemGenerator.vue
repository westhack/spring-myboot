<template>
  <div>
    <FormItemGenerator :item="item" :options="options" :defaultValue="inputValue" @change="onChange"></FormItemGenerator>
  </div>
</template>

<script>
import _ from 'lodash'
import FormItemGenerator from './FormItemGenerator'

export default {
  name: 'VFormItemGenerator',
  components: {
    FormItemGenerator
  },
  props: {
    item: {
      type: Object,
      required: true
    },
    options: {
      type: [String, Object, Array],
      default: null
    },
    value: {
      type: [String, Array, Object, Number, Function, Boolean],
      default: null
    }
  },
  data () {
    const vm = this
    const value = vm.value

    return {
      inputValue: _.cloneDeep(value),
      oldInputValue: _.cloneDeep(value)
    }
  },
  watch: {
    'value': function (val) {
      console.log(val)
    },
    'inputValue': function (val) {
      console.log(val)
    }
  },
  created () {
  },
  methods: {
    onChange (val) {
      this.$emit('input', val)
      this.$emit('change', val)
    }
  }
}

</script>

<style scoped>
#components-form-item >>> .anticon-close-circle {
  cursor: pointer;
  color: #ccc;
  transition: color 0.3s;
  font-size: 12px;
}

#components-form-item >>> .anticon-close-circle:hover {
  color: #999;
}

#components-form-item >>> .anticon-close-circle:active {
  color: #666;
}
</style>
