<template>
  <a-list
    size="large"
    rowKey="id"
    :loading="loading"
    itemLayout="vertical"
    :dataSource="data"
  >
    <a-list-item :key="item.id" slot="renderItem" slot-scope="item">
      <a-list-item-meta>
        <a slot="title" href="#">{{ item.title }}</a>
      </a-list-item-meta>

      <div class="antd-pro-components-article-list-content-index-listContent">
        <div class="description">
          {{ item.content }}
        </div>
        <div class="extra">
          <a-avatar :src="item.icon" size="small" />
          <a>{{ item.formUser.username }}</a>
          发送于
          <em>{{ item.createdAt }}</em>
        </div>
      </div>
      <a slot="actions" v-if="!item.status" @click="handleView(item)" style="color: #f03c3c">未读</a>
      <a slot="actions" v-else>已读</a>
      <a slot="actions">
        <a-popconfirm
          title="确定要删除?"
          ok-text="是"
          cancel-text="否"
          @confirm="handleDelete(item.id)"
        >
          <a href="#">删除</a>
        </a-popconfirm>
      </a>
    </a-list-item>
    <div slot="footer" v-if="data.length > 0 && isMore" style="text-align: center; margin-top: 16px;">
      <a-button @click="loadMore" :loading="loadingMore">加载更多</a-button>
    </div>
  </a-list>
</template>

<script>
import { ArticleListContent } from '@/components'
import { getUserMessages, userMessageDelete } from '@/modules/system/api/user'
import IconText from '@/modules/system/components/IconText'
import { httpResponseCode } from '@/constants/httpResponseCode'

export default {
  name: 'Message',
  components: {
    ArticleListContent,
    'icon-text': IconText
  },
  data () {
    return {
      loading: true,
      loadingMore: false,
      data: [],
      queryParam: {
        page: 1,
        pageSize: 10
      },
      isMore: true
    }
  },
  created () {
    this.getList()
  },
  methods: {
    getList () {
      getUserMessages(this.queryParam).then(res => {
        console.log('res', res)
        this.data = res.data.items
        this.loading = false
        if (this.data.length < this.queryParam.pageSize) {
          this.isMore = false
        } else {
          this.queryParam.page++
        }
      })
    },
    loadMore () {
      this.loadingMore = true

      getUserMessages(this.queryParam).then(res => {
        if (res.data.items) {
          this.data = this.data.concat(res.data.items)
          this.queryParam.page++
        } else {
          this.isMore = false
        }
      }).finally(() => {
        this.loadingMore = false
      })
    },
    handleView (item) {
      this.$store.dispatch('UserMessageView', item.id).then(res => {
        item.status = true
      })
    },
    handleDelete (id) {
      userMessageDelete({ id: id }).then(res => {
        if (res.code == httpResponseCode.SUCCESS) {
          this.$message.success(res.message)

          this.$store.commit('SET_USER_MESSAGE_COUNT', res.data.total)

          this.data.forEach((res, index) => {
            if (res.id == id) {
              const newData = this.data.slice()
              newData.splice(index, 1)
              this.data = newData
            }
          })
        } else {
          this.$message.error(res.message)
        }
      })
    }
  }
}
</script>

<style lang="less" scoped>

.antd-pro-components-article-list-content-index-listContent {
  .description {
    max-width: 720px;
    line-height: 22px;
  }
  .extra {
    margin-top: 16px;
    color: #333333;
    line-height: 22px;

    & /deep/ .ant-avatar {
      position: relative;
      top: 1px;
      width: 20px;
      height: 20px;
      margin-right: 8px;
      vertical-align: top;
    }

    & > em {
      margin-left: 0px;
      color: #cccccc;
      font-style: normal;
    }
  }
}

@media screen and (max-width: 480px) {
  .antd-pro-components-article-list-content-index-listContent {
    .extra {
      & > em {
        display: block;
        margin-top: 8px;
        margin-left: 0;
      }
    }
  }
}

/deep/ .ant-list-vertical .ant-list-item-action {
  margin-top: 16px;
  margin-left: auto;
  display: flex;
  justify-content: flex-end;
}
</style>
