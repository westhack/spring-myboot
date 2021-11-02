<template>
  <div>
    <div v-if="showPopover">

      <a-popover placement="topLeft">
        <template slot="content">
          <p :style="popoverCss">{{ value }}</p>
        </template>
        <template slot="title">
          <span>
            详细内容
            <a title="复制">
              <a-icon type="copy" @click="copy(value)"/>
            </a>
          </span>
        </template>
        <div class="text">{{ value }}</div>
      </a-popover>

    </div>
    <div v-else class="text">
      {{ value }}
    </div>
  </div>
</template>

<script>
export default {
  name: 'InputText',
  props: {
    value: {
      type: [Object, Array, String, Number, Boolean],
      required: true
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
  computed: {
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
