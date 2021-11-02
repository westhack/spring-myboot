<script>
export default {
  name: 'IFrameView',
  props: {
    keepAlive: {
      type: Boolean,
      default: true
    }
  },
  data () {
    return {}
  },
  computed: {
    key () {
      return this.$route.fullPath
    }
  },
  render () {
    const { $route: { meta }, $store: { getters } } = this
    const height = window.screen.height
    const inKeep = (
      <keep-alive>
        <iframe
          id="iframe"
          src={meta.defaultPath}
          frameBorder="0"
          width="100%"
          height={height}
          scrolling="auto"
        ></iframe>
      </keep-alive>
    )
    const notKeep = (
      <iframe
        id="iframe"
        src={meta.defaultPath}
        frameborder="0"
        width="100%"
        height={height}
        scrolling="auto"
      ></iframe>
    )
    // 这里增加了 multiTab 的判断，当开启了 multiTab 时
    // 应当全部组件皆缓存，否则会导致切换页面后页面还原成原始状态
    // 若确实不需要，可改为 return meta.keepAlive ? inKeep : notKeep
    if (!getters.multiTab && meta.keepAlive === false) {
      return notKeep
    }
    return this.keepAlive || getters.multiTab || meta.keepAlive ? inKeep : notKeep
  }
}
</script>
