<template>
  <div class="user-wrapper">

    <div class="content-box">
      <span class="action" v-for="(lay, i) in layouts['headerCenter']" :key="i">
        <component :is="lay"/>
      </span>
      <span class="action"><FullScreen v-model="fullscreen"></FullScreen></span>
      <span class="action"><select-lang :class="prefixCls" /></span>
      <UserDropdown @handleLogout="handleLogout" :avatar="avatar()" :nickname="nickname()"></UserDropdown>
      <span class="action" v-for="(lay, i) in layouts['headerAfter']" :key="i">
        <component :is="lay"/>
      </span>
    </div>
  </div>
</template>

<script>
import NoticeIcon from '@/components/NoticeIcon'
import FullScreen from './FullScreen'
import UserDropdown from './UserDropdown'
import { mapActions, mapGetters } from 'vuex'
import SelectLang from '@/components/SelectLang'

export default {
  name: 'UserMenu',
  props: {
    prefixCls: {
      type: String,
      default: 'ant-pro-global-header-index-action'
    },
    layouts: {
      type: [Array, Object],
      default: () => {
        return []
      }
    }
  },
  components: {
    NoticeIcon,
    FullScreen,
    SelectLang,
    UserDropdown
  },
  data () {
    return {
      visible: false,
      fullscreen: true,
      messages: this.$store.getters.messages,
      shop: this.$store.getters.shop,
      merchants: []
    }
  },
  computed: {
    messageCount () {
      return this.$store.getters.messageCount
    }
  },
  methods: {
    ...mapActions(['Logout', 'UserMessageView']),
    ...mapGetters(['nickname', 'avatar']),
    handleFullscreen () {
      this.fullscreen = !this.fullscreen
    },
    handleMessageView () {
      const id = []
      this.$store.getters.messages.map(res => {
        if (res.status == 0) {
          id.push(res.id)
        }
      })

      if (id.length > 0) {
        this.UserMessageView(id)
      }
    },
    handleClick (item) {
      this.shop = item
    },
    handleLogout () {
      const that = this

      this.$confirm({
        title: '提示',
        content: '真的要注销登录吗 ?',
        onOk () {
          return that.Logout({}).then(() => {
            window.location.reload()
          }).catch(err => {
            that.$message.error({
              title: '错误',
              description: err.message
            })
          })
        },
        onCancel () {
        }
      })
    }
  }
}
</script>

<style lang="less" scoped>
  .item-group {
    padding: 20px 0 8px 24px;
    font-size: 0;
    a {
      color: rgba(0, 0, 0, 0.65);
      display: inline-block;
      font-size: 14px;
      margin-bottom: 13px;
      width: 25%;
    }
    .selected {
      color: red;
    }
  }
</style>
