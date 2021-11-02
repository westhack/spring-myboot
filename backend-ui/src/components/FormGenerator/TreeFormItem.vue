<template>
  <div>

    <a-card size="small" :title="title" style="margin-bottom: 10px">
      <div :key="index" v-for="(item, index) in formData">
        <a-form-item
          v-bind="formItemLayout"
          :label="item.label || item.name"
          :extra="item.extra"
          :style="{'display': item.type === 'hidden' ? 'none' : 'block'}"
          v-if="!(item.children && item.children.length)"
        >
          <FormItemGenerator
            @formChange="formChange"
            :item="item"
            :options="item.data"
            v-model="item.value"
            v-decorator="[
              `${name}[${item.name}]`,
              {
                initialValue: item.value,
                validateTrigger: ['change', 'blur'],
                rules: item.rules
              }
            ]"
          />

        </a-form-item>
        <div>
          <TreeFormItem
            v-if="item.children && item.children.length > 0"
            :fields="item.children"
            :title="item.label"
            :name="`${name}[${item.name}]`"></TreeFormItem>
        </div>

      </div>
    </a-card>

  </div>
</template>

<script>
import FormItemGenerator from './FormItemGenerator'
import httpResponse from '@/mixins/httpResponse'

export default {
  name: 'TreeFormItem',
  components: { FormItemGenerator, 'TreeFormItem': this },
  mixins: [httpResponse],
  props: {
    title: {
      type: String,
      default: ''
    },
    name: {
      type: String,
      default: ''
    },
    fields: {
      type: [Array, Object],
      required: true
    },
    formItemLayout: {
      type: Object,
      default: function () {
        return {
        }
      }
    }
  },
  beforeCreate () {

  },
  created () {
  },
  data () {
    const vm = this
    return {
      formData: vm.fields
    }
  },
  watch: {
    fields: function (val) {
      this.formData = this.fields
    }
  },
  methods: {
    formChange () {
      this.$emit('formChange')
    }
  }
}

</script>
