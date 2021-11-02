<template>
  <div>
    <div class="editable-cell">
      <div v-if="editable" class="editable-cell-input-wrapper">
        <a-input-number
          :step="step"
          :precision="precision"
          size="small"
          :min="min"
          :max="max"
          :value="value"
          @change="handleChange"
          @pressEnter="check"
          _formatter="value => `${value}`.replace(/\B(?=(\d{3})+(?!\d))/g, ',')"
          style="text-align: right"/>
        <a-icon
          v-if="false"
          type="check"
          class="editable-cell-icon-check"
          @click="check"
        />
      </div>
      <div v-else class="editable-cell-text-wrapper">
        {{ value || ' ' }}
        <a-icon type="edit" class="editable-cell-icon" @click="edit" />
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: 'EditableCell',
  props: {
    step: {
      type: [String, Number],
      required: false,
      default: 1
    },
    precision: {
      type: [Number],
      required: false,
      default: 0
    },
    min: {
      type: [Number],
      required: false,
      default: 0
    },
    max: {
      type: [Number],
      required: false,
      default: 1000000
    },
    text: [String, Number]
  },
  data () {
    return {
      value: this.text,
      editable: true
    }
  },
  watch: {
    text: function (val) {
      this.value = this.text
      this.$emit('change', this.value)
    }
  },
  methods: {
    handleChange (value) {
      // const value = e.target.value
      this.value = value
      this.$emit('change', this.value)
    },
    check () {
      this.editable = false
      this.$emit('change', this.value)
    },
    edit () {
      this.editable = true
    }
  }
}
</script>

<style scoped>

</style>
