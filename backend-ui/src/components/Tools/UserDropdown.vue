<template>
  <a-dropdown>
    <span class="action ant-dropdown-link user-dropdown-menu">
      <a-avatar class="avatar" size="small" :src="avatar"/>
      <span>{{ nickname }}</span>
    </span>
    <a-menu slot="overlay" class="user-dropdown-menu-wrapper">
      <a-menu-item key="0">
        <router-link :to="{ name: 'accountBaseSettings' }">
          <a-icon type="user"/>
          <span>个人中心</span>
        </router-link>
      </a-menu-item>
      <a-menu-item key="1">
        <router-link :to="{ name: 'accountPassword' }">
          <a-icon type="setting"/>
          <span>修改密码</span>
        </router-link>
      </a-menu-item>
      <a-menu-divider/>
      <a-menu-item key="3">
        <a href="javascript:;" @click="handleLogout">
          <a-icon type="logout"/>
          <span>退出登录</span>
        </a>
      </a-menu-item>
    </a-menu>
  </a-dropdown>
</template>

<script>

export default {
  name: 'UserDropdown',
  components: {
  },
  props: {
    avatar: {
      type: String,
      default: '',
      required: false
    },
    nickname: {
      type: String,
      default: '',
      required: false
    }
  },
  methods: {
    handleLogout () {
      this.$emit('handleLogout')
    }
  }
}
</script>
