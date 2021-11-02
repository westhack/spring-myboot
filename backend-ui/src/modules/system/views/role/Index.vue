<template>
  <a-card :bordered="false">
    <div>
      <div class="table-operator">
        <a-button type="primary" icon="plus" @click="handleCreate(0)">添加角色</a-button>
        <a-button type="default" icon="plus" @click="handleCreate(selectKey)" v-show="selectKey">添加子角色</a-button>
        <a-button type="default" icon="copy" @click="handleClone(selectKey)" v-show="selectKey">复制</a-button>
        <a-button type="danger" icon="delete" @click="handleDel" v-if="checkedKeys.length > 0 || (checkedKeys.checked && checkedKeys.checked.length > 0)" v-permission:permissionListdelete>删除</a-button>
        是否关联：<a-switch checkedChildren="" unCheckedChildren="关" defaultChecked v-model="checkStrictly"/>
        <a-button type="default" icon="reload" @click="handleReload">刷新</a-button>

      </div>

      <a-row :gutter="24">
        <a-col :xs="24" :md="6" :lg="7" v-permission:role_query>
          <div style="max-height: calc(100vh - 180px);overflow:auto;">
            <a-skeleton active :loading="loading">
              <div>
                <a-input-search style="margin-bottom: 8px" placeholder="搜索" @search="handleSearch" />
                <a-tree
                  checkable
                  @expand="onExpand"
                  :expandedKeys="expandedKeys"
                  :autoExpandParent="autoExpandParent"
                  :treeData="models"
                  :checkStrictly="!checkStrictly"
                  v-model="checkedKeys"
                  @select="handleSelect"
                >
                  <template slot="title" slot-scope="{title, key}">
                    <span :key="key">{{ title }}</span>
                  </template>
                </a-tree>
              </div>
            </a-skeleton>
          </div>
        </a-col>
        <a-col :xs="24" :md="18" :lg="17">
          <a-card :title="title" v-if="this.visible" id="form">
            <a-skeleton active :loading="formLoading">
              <form-generator
                ref="form"
                @handleSubmit="handleSubmit"
                :fields="formData"
                :showFooter="true"
                :formLayout="formLayout"
              >
                <template slot="form-item-before" slot-scope="{item}">
                  <div v-if="item.name == 'permissions'">
                    是否关联：
                    <a-switch checkedChildren="" unCheckedChildren="关" defaultChecked v-model="treeCheckStrictly"/>
                  </div>
                </template>
                <template slot="footer">
                  <br>
                  <br>
                  <footer-tool-bar>
                    <a-button
                      type="primary"
                      html-type="submit"
                      :loading="submitLoading"
                    >
                      提交
                    </a-button>
                    <a-button
                      :style="{ marginLeft: '8px' }"
                      @click="handleReset"
                    >
                      重置
                    </a-button>
                  </footer-tool-bar>
                </template>
              </form-generator>
            </a-skeleton>
          </a-card>
        </a-col>
      </a-row>
    </div>

  </a-card>
</template>

<script>
import { httpResponseCode } from '@/constants/httpResponseCode'
import {
  getRoleList, roleCreate, roleUpdate, roleDelete
} from '@/modules/system/api/role'
import { permissionAll } from '@/modules/system/api/permission'
import { STable } from '@/components'
import FormGenerator from '@/components/FormGenerator/FormGenerator'
import FooterToolBar from '@/components/FooterToolbar'
import httpResponse from '@/mixins/httpResponse'
import draggable from 'vuedraggable'
import ATextarea from 'ant-design-vue/es/input/TextArea'
import { defaultFormData } from './form'
import _ from 'lodash'
import { setFormValue, treeParseData, treeToList } from '@/utils/util'
// import pinyin from 'pinyin'

export default {
  name: 'RoleList',
  mixins: [httpResponse],
  components: {
    ATextarea,
    STable,
    FormGenerator,
    draggable,
    FooterToolBar
  },
  data () {
    return {
      loading: true,
      formLoading: false,
      submitLoading: false,
      saveLoading: false,
      reloadLoading: false,
      formLayout: 'vertical',
      visible: false,
      openKeys: [],
      defaultSelectedKeys: [],
      models: [],
      items: [],
      selectKey: '',
      formData: {},
      roleOptions: [],
      permissionAll: [],
      guards: [],
      title: '编辑',
      checkStrictly: false,
      treeCheckStrictly: false,
      expandedKeys: [],
      checkedKeys: [],
      autoExpandParent: false,
      toolBar: [
        {
          key: 'add',
          label: '新增'
        },
        {
          key: 'edit',
          label: '修改'
        },
        {
          key: 'del',
          label: '删除'
        }
      ]
    }
  },
  watch: {
    treeCheckStrictly (val) {
      this.formData.permissions.checkStrictly = !val
    }
  },
  created () {
    this.formData = defaultFormData
    this.getRoleList().then((res) => { this.loading = false })
    this.getPermissionOptions()
  },
  methods: {
    onExpand  (expandedKeys) {
      this.expandedKeys = expandedKeys
      this.autoExpandParent = false
    },

    handleSelect (selectedKeys) {
      // this.formLoading = true

      if (selectedKeys[0]) {
        this.selectKey = selectedKeys[0]
        this.items.forEach((item) => {
          if (item.key === this.selectKey) {
            this.handleEdit(item)
          }
        })
      } else {
        this.selectKey = ''
      }
      setTimeout(() => { this.formLoading = false }, 500)
    },

    async getRoleList (params) {
      const res = await getRoleList(params)
      this.models = treeParseData(res.data.items, 'id', 'title')
      this.items = treeToList(res.data.items)
    },

    async getPermissionOptions (params) {
      const res = await permissionAll(params)
      this.permissionAll = defaultFormData.permissions.options = res.data.items
      this.formData.permissions.options = this.permissionAll
    },

    handleEdit (item) {
      this.title = '编辑角色：' + item.title
      this.formData = setFormValue(_.cloneDeep(defaultFormData), item)
      this.formData.permissions.options = this.permissionAll
      this.formData.permissions.value = _.keys(_.keyBy(item.permissions, 'id'))
      this.visible = true
    },

    handleAdd (key) {
      if (!this.$permission('role_add')) {
        this.$message.error('暂无权限')
        return
      }
      this.title = '创建角色：'
      if (key > 0) {
        const item = _.find(this.items, (o) => { return o.id == key })
        this.title = '创建 [' + item.title + ']  的子角色：'
      }
      this.formData = _.cloneDeep(defaultFormData)
      this.formData.permissions.options = this.permissionAll
      this.formData.permissions.value = []
      this.formData.parentId.value = key
      this.visible = true
    },

    handleClick ({ key }) {
      if (!this.$permission('role_edit')) {
        this.$message.error('暂无权限')
        return
      }

      this.selectKey = key
      const item = _.find(this.items, (o) => { return o.id == key })

      this.loadingSkeleton(() => {
        this.handleEdit(item)
      }, 500)
    },

    handleBar (key, item) {
      if (key === 'edit') {
        this.handleEdit(item)
      } else if (key === 'add') {
        this.handleAdd()
      } else if (key === 'del') {
        if (item.id === 1) {
          this.$message.error('无法删除系统管理员')

          return false
        }
        this.handleDel(item)
      }
    },

    handleSearch (val) {
      this.getRoleList({ name: val })
    },

    handleDel (item) {
      if (!this.$permission('role_delete')) {
        this.$message.error('暂无权限')
        return
      }

      let id = []
      if (this.checkStrictly === true) {
        id = this.checkedKeys
      } else {
        id = this.checkedKeys.checked
      }
      if (id == null || id.length === 0) {
        this.$message.error('请选择要删除的数据')

        return false
      }
      const that = this

      this.$confirm({
        title: '确认删除选择数据?',
        content: '删除后无法恢复',
        okText: '确认',
        okType: 'danger',
        cancelText: '取消',
        onOk () {
          roleDelete({ id: id })
            .then((res) => that.submitSuccess(res))
            .then((res) => {
              if (res.code === httpResponseCode.SUCCESS) {
                that.checkedKeys = []
                that.getRoleList()
              }
            })
            .catch(err => that.requestFailed(err))
            .finally(() => {})
        },
        onCancel () {
        }
      })
    },

    handleReset () {
      this.$refs['form'].resetFields()
    },

    async handleReload () {
      this.reloadLoading = true
      await this.getRoleList().then(() => { this.reloadLoading = false })
      await this.getPermissionOptions()
      if (this.$refs['form']) {
        this.$refs['form'].setFormFields(this.formData)
      }
    },

    handleChange () {},

    handleSubmit (e) {
      e.preventDefault()

      this.submitLoading = true
      this.$refs['form'].validateFields((err, values) => {
        if (!err) {
          if (values.id > 0) {
            roleUpdate(values)
              .then((res) => this.submitSuccess(res))
              .then((res) => {
                if (res.code === httpResponseCode.SUCCESS) {
                  this.getRoleList()
                }
              })
              .catch(err => this.requestFailed(err))
              .finally(() => {
                this.submitLoading = false
              })
          } else {
            roleCreate(values)
              .then((res) => this.submitSuccess(res))
              .then((res) => {
                if (res.code === httpResponseCode.SUCCESS) {
                  this.getRoleList()
                }
              })
              .catch(err => this.requestFailed(err))
              .finally(() => {
                this.submitLoading = false
              })
          }
        } else {
          this.submitLoading = false
        }
      })
    },

    handleCreate (key) {
      this.loadingSkeleton(() => {
        this.handleAdd(key)
      }, 500)
    },
    handleClone (key) {
      this.loadingSkeleton(() => {
        this.items.forEach((item) => {
          if (item.key === key) {
            this.formData = setFormValue(_.cloneDeep(defaultFormData), item)
            this.formData.id.value = null
            this.formData.permissions.value = _.keys(_.keyBy(item.permissions, 'id'))
            this.visible = true
            this.title = '复制角色'
          }
        })
      }, 500)
    },
    loadingSkeleton (callback, time) {
      this.formLoading = true
      callback()
      setTimeout(() => { this.formLoading = false }, time)
    }
    // pinyin (value) {
    //   return (pinyin(value, { style: pinyin.STYLE_NORMAL })).toString().replace(/,/g, '_')
    // }

  }
}
</script>

<style lang="less" scoped>

</style>
