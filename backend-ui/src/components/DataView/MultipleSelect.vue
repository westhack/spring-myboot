<template>
  <div>
    <div v-if="showPopover">
      <a-popover placement="topLeft">
        <template slot="content">
          <p :style="popoverCss">
            <span v-for="(d, i) in detail" :key="i">
              <a-tag :color="(inputOptions[d] ? inputOptions[d].color : '' )">{{ inputOptions[d] ? inputOptions[d].label : d }}</a-tag>
            </span>
          </p>
        </template>
        <template slot="title">
          <span>
            详细内容
            <a title="复制">
              <a-icon type="copy" @click="copy(value)"/>
            </a>
          </span>
        </template>
        <div class="text">
          <span v-for="(d, i) in detail" :key="i">
            <a-tag :color="(inputOptions[d] ? inputOptions[d].color : '' )">{{ inputOptions[d] ? inputOptions[d].label : d }}</a-tag>
          </span>
        </div>
      </a-popover>

    </div>
    <div v-else class="text">
      <span v-for="(d, i) in detail" :key="i">
        <a-tag :color="(inputOptions[d] ? inputOptions[d].color : '' )">{{ inputOptions[d] ? inputOptions[d].label : d }}</a-tag>
      </span>
    </div>

  </div>
</template>

<script>
import _ from 'lodash'
export default {
  name: 'MultipleSelect',
  props: {
    value: {
      type: [Object, Array, String, Number, Boolean],
      required: true
    },
    options: {
      type: [Array, Object],
      required: false,
      default: () => {
        return []
      }
    },
    showPopover: {
      type: Boolean,
      required: false,
      default: true
    },
    popoverWidth: {
      type: [Number, String],
      required: false,
      default: '300px'
    }
  },
  data () {
    const vm = this
    return {
      popoverCss: { maxWidth: vm.popoverWidth, wordWrap: 'break-word', wordBreak: 'normal' }
    }
  },
  created () {
  },
  computed: {
    detail () {
      const vm = this
      let value = vm.value
      if (!_.isArray(vm.value)) {
        if (value != '') {
          value = value.toString().split(',')
        } else {
          value = []
        }
      }

      return value
    },
    inputOptions () {
      const options = this.options
      return _.keyBy(options, 'value')
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
