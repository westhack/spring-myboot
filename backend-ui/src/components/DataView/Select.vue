<template>
  <div class="text">
    <a-tag :color="detail.color">{{ detail.label || value }}</a-tag>
  </div>
</template>

<script>
import _ from 'lodash'
export default {
  name: 'Select',
  props: {
    value: {
      type: [Object, Array, String, Number, Boolean],
      required: false
    },
    options: {
      type: [Array, Object],
      required: false,
      default: () => {
        return []
      }
    }
  },
  data () {
    return {
    }
  },
  created () {
  },
  computed: {
    detail () {
      const res = _.find(this.options, (o) => { return o.value == this.value })
      return res || { value: this.value, label: this.value, color: '' }
    }
  },
  methods: {
    copy (text) {
      this.$copyText(text).then(message => {
        console.log('copy', message)
        this.$message.success('复制完毕')
      }).catch(err => {
        console.log('copy.err', err)
        this.$message.error('复制失败')
      })
    }
  }
}
</script>

<style scoped>
.text {
  overflow: hidden; white-space: nowrap;text-overflow: ellipsis;word-break: break-all
}
</style>
