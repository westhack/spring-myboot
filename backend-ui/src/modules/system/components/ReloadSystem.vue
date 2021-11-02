<template>
  <div v-permission:reload_system>
    <a-icon type="reload" @click="handleReload"/>
  </div>
</template>

<script>
import { reloadSystem } from '@/modules/system/api/system'

const { httpResponseCode } = require('@/constants/httpResponseCode')
export default {
  name: 'ReloadSystem',
  components: {
  },
  data () {
    return {

    }
  },
  computed: {

  },
  methods: {
    handleReload () {
      const that = this
      this.$confirm({
        title: '确认操作',
        content: '重新系统有风险，是否继续？',
        okText: '确认',
        okType: 'danger',
        cancelText: '取消',
        onOk () {
          reloadSystem().then(res => {
            if (res.code == httpResponseCode.SUCCESS) {
              that.$message.success(res.message)
            } else {
              that.$message.error(res.message)
            }
          })
        },
        onCancel () {}
      })
    }
  }
}
</script>

<style scoped>

</style>
