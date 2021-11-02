<template>
  <notice-icon :count="messageCount" :messages="messages" @click="handleMessageView" @delete="handleDelete"/>
</template>

<script>
import NoticeIcon from '@/components/NoticeIcon'
import { userMessageDelete } from '@/modules/system/api/user'
import { httpResponseCode } from '@/constants/httpResponseCode'
export default {
  name: 'Message',
  components: {
    NoticeIcon
  },
  mounted () {
    // this.$store.dispatch('GetUserMessages')
  },
  data () {
    return {
    }
  },
  computed: {
    messageCount () {
      return this.$store.getters.messageCount
    },
    messages () {
      return this.$store.getters.messages
    }
  },
  methods: {
    handleMessageView () {
      const id = []
      this.$store.getters.messages.map(res => {
        if (res.status == 0) {
          id.push(res.id)
        }
      })

      if (id.length > 0) {
        // this.$store.dispatch('UserMessageView', id)
      }
    },
    handleDelete (id) {
      userMessageDelete({ id: id }).then(res => {
        if (res.code == httpResponseCode.SUCCESS) {
          this.$message.success(res.message)
          this.$store.dispatch('GetUserMessages')
        } else {
          this.$message.error(res.message)
        }
      })
    }
  }
}
</script>

<style scoped>

</style>
