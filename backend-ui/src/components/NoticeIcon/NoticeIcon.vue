<template>
  <i>
    <a-modal
      :title="item.title"
      :visible="show"
      :zIndex="10000"
      @ok="() => { this.show = false }"
      @cancel="() => { this.show = false }"
    >
      <p>{{ item.content }}</p>
    </a-modal>
    <a-popover
      v-model="visible"
      trigger="click"
      placement="bottomRight"
      overlayClassName="header-notice-wrapper"
      :autoAdjustOverflow="true"
      :arrowPointAtCenter="true"
      :overlayStyle="{ width: '300px', top: '50px' }"
    >
      <template slot="content">
        <a-spin :spinning="loading">
          <a-tabs>
            <a-tab-pane tab="通知" key="1">
              <a-list>
                <a-list-item v-for="(item, index) in messages" :key="index">
                  <a-list-item-meta :title="item.title" :description="item.content">
                    <a-avatar style="background-color: white" slot="avatar" :src="item.icon"/>
                  </a-list-item-meta>
                  <a slot="actions" @click="handleView(item)">查看</a>
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
              </a-list>
            </a-tab-pane>
          </a-tabs>
        </a-spin>
      </template>
      <span @click="fetchNotice" class="header-notice">
        <a-badge :count="count">
          <a-icon style="font-size: 16px; padding: 4px" type="bell" />
        </a-badge>
      </span>
    </a-popover>

  </i>

</template>

<script>

export default {
  name: 'HeaderNotice',
  props: {
    messages: {
      type: Array,
      default: function () {
        return []
      }
    },
    count: {
      type: Number,
      default: 0
    }
  },
  data () {
    return {
      loading: false,
      visible: false,
      show: false,
      item: {}
    }
  },

  methods: {
    fetchNotice () {
      this.$emit('click')
      if (!this.visible) {
        this.loading = true
        setTimeout(() => {
          this.loading = false
        }, 1000)
      } else {
        this.loading = false
      }
      this.visible = !this.visible
    },
    handleDelete (id) {
      this.$emit('delete', id)
    },
    handleView (item) {
      this.show = true
      this.item = item
      this.$store.dispatch('UserMessageView', item.id)
    }
  }
}
</script>

<style lang="css" scoped>
  .header-notice-wrapper {
    top: 50px !important;
  }
</style>
<style lang="less" scoped>
  .header-notice{
    display: inline-block;
    transition: all 0.3s;

    span {
      vertical-align: initial;
    }
  }
  /deep/ .ant-list-item-action {
    margin-left: 1px;
  }
</style>
