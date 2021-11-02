<template>
  <a-card :bordered="false">
    <div>
      <div class="table-operator">
        <a-button type="primary" icon="plus" @click="handleCreate(0)" v-permission:permissionListadd>添加一级菜单</a-button>
        <a-button type="default" icon="plus" @click="handleCreate(selectKey)" v-show="selectKey" v-permission:permissionListadd>添加子级菜单</a-button>
        <a-button type="default" icon="plus" @click="handleCreateButton(selectKey)" v-show="selectKey && !isButton" v-permission:permissionListadd>添加按钮操作</a-button>
        <a-button type="default" icon="copy" @click="handleClone(selectKey)" v-show="selectKey" v-permission:permissionListadd>复制</a-button>
        <a-button type="danger" icon="delete" @click="handleDel" v-if="checkedKeys.length > 0 || (checkedKeys.checked && checkedKeys.checked.length > 0)" v-permission:permissionListdelete>删除</a-button>
        是否关联：<a-switch checkedChildren="" unCheckedChildren="关" defaultChecked v-model="checkStrictly"/>
        <a-button type="default" icon="reload" @click="handleReload">刷新</a-button>
      </div>

      <split-pane :min-percent="30" :default-percent="30" split="vertical" style="min-width: 300px; height: calc(100vh - 180px)">
        <template slot="paneL">
          <div style="max-height: calc(100vh - 180px);overflow:auto; margin-right: 10px" v-permission:permissionListquery>
            <a-skeleton active :loading="loading">
              <div>
                <a-input-search style="margin-bottom: 8px" placeholder="Search" @change="handleSearch" />
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
                  <template slot="title" slot-scope="{title, key, isButton}">
                    <span v-if="searchValue != '' && title.indexOf(searchValue) > -1">
                      {{ title.substr(0, title.indexOf(searchValue)) }}
                      <span style="color: #f50">{{ searchValue }}</span>
                      {{ title.substr(title.indexOf(searchValue) + searchValue.length) }}
                    </span>
                    <span v-else>{{ title }} <a-icon v-if="isButton" style="color: #1890ff" type="bold" /> </span>
                  </template>
                </a-tree>
              </div>

            </a-skeleton>
          </div>
        </template>
        <template slot="paneR">
          <a-card :title="title" v-if="this.visible" style="margin-left: 10px; " id="form">
            <a-skeleton active :loading="formLoading">
              <form-generator
                ref="form"
                @handleSubmit="handleSubmit"
                :fields="formData"
                :showFooter="true"
                :formLayout="formLayout"
              >
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
        </template>
      </split-pane>
    </div>
  </a-card>
</template>

<script>
import { httpResponseCode } from '@/constants/httpResponseCode'
import { permissionApis, getPermissionList, permissionCreate, permissionUpdate, permissionDeleteByIds } from '@/modules/system/api/permission'
import SMenu from '@/modules/system/components/menu/Menu'
import { STable } from '@/components'
import FormGenerator from '@/components/FormGenerator/FormGenerator'
import FormItemGenerator from '@/components/FormGenerator/FormItemGenerator'
import FooterToolBar from '@/components/FooterToolbar'
import httpResponse from '@/mixins/httpResponse'
import draggable from 'vuedraggable'
import ATextarea from 'ant-design-vue/es/input/TextArea'
import { defaultFormData, defaultButtonFormData } from './form'
import _ from 'lodash'
import { setFormValue, treeParseData } from '@/utils/util'
import { constantRouterComponents } from '@/router/generatorRouters'

const dataList = []
const generateList = (data) => {
  for (let i = 0; i < data.length; i++) {
    const node = data[i]
    dataList.push(node)
    if (node.children) {
      generateList(node.children, node.key)
    }
  }
}

const getParentKey = (key, tree) => {
  let parentKey
  for (let i = 0; i < tree.length; i++) {
    const node = tree[i]
    if (node.children) {
      if (node.children.some(item => item.key === key)) {
        parentKey = node.key
      } else if (getParentKey(key, node.children)) {
        parentKey = getParentKey(key, node.children)
      }
    }
  }
  return parentKey
}

export default {
  name: 'PermissionList',
  mixins: [httpResponse],
  components: {
    ATextarea,
    STable,
    SMenu,
    FormGenerator,
    FormItemGenerator,
    draggable,
    FooterToolBar
  },
  data () {
    return {
      loading: true,
      submitLoading: false,
      formLoading: false,
      saveLoading: false,
      reloadLoading: false,
      formLayout: 'vertical',
      visible: false,
      models: [],
      dataList: [],
      formData: {},
      permissionOptions: [],
      permissionRoutes: [],
      routerComponents: [],
      title: '编辑',
      checkedKeys: [],
      expandedKeys: [],
      searchValue: '',
      checkStrictly: false,
      selectKey: '',
      isButton: false,
      autoExpandParent: false,
      buttons: []
    }
  },

  created () {
    this.getPermissionList().then((res) => { this.loading = false })
    this.getRouterComponents()
    this.getPermissionApis()
    const buttons = []
    this.buttons = this.$enum()
    _.forEach(this.buttons, (res) => {
      buttons.push({
        label: res.label,
        value: res.value + ':' + res.label
      })
    })
    this.buttons = buttons
  },
  methods: {

    onExpand  (expandedKeys) {
      this.expandedKeys = expandedKeys
      this.autoExpandParent = false
    },

    handleSearch (e) {
      const value = e.target.value
      const expandedKeys = dataList.map((item) => {
        if (item.title.indexOf(value) > -1) {
          return getParentKey(item.key, this.models)
        }
        return null
      }).filter((item, i, self) => item && self.indexOf(item) === i)

      Object.assign(this, {
        expandedKeys,
        searchValue: value,
        autoExpandParent: true
      })
    },

    buttonChange (val) {
      if (val === true) {
        this.formData.component.rules = [{ required: true, message: '不能为空' }]
      } else {
        this.formData.component.rules = []
      }
    },

    handleSelect (selectedKeys) {
      this.loadingSkeleton(() => {
        if (selectedKeys[0]) {
          this.selectKey = selectedKeys[0]
          dataList.forEach((item) => {
            if (item.key === this.selectKey) {
              if (!item.isButton) {
                const buttons = []
                const buttons2 = []
                const btnValue = []

                this.buttons.map(res => {
                  buttons.push({
                    label: res.label,
                    value: item.name + '_' + res.value
                  })
                })

                if (item.children && item.children.length && item.children.length > 0) {
                  item.children.map(children => {
                    if (children.isButton === true) {
                      let inset = true
                      buttons.map(res2 => {
                        if (res2.value === (children.name + ':' + children.title)) {
                          inset = false
                        }
                      })

                      if (inset === true) {
                        buttons2.push({
                          label: children.title,
                          value: children.name + ':' + children.title
                        })
                      }

                      btnValue.push(children.name + ':' + children.title)
                    }
                  })
                }

                defaultFormData.buttons.data = [...buttons, ...buttons2]

                item['buttons'] = btnValue
                this.handleEdit(item)
                this.isButton = false
              } else {
                this.handleEditButton(item)
                this.isButton = true
              }
            }
          })
        } else {
          this.selectKey = ''
        }
      }, 200)
    },

    async getPermissionList (params) {
      const res = await getPermissionList(params)
      this.models = treeParseData(res.data.items, 'id', 'title')
      generateList(this.models)
      this.permissionOptions = defaultFormData.parentId.options = [{ title: '顶级', id: '0' }, ...this.models]
    },

    async getPermissionApis (params) {
      const res = await permissionApis(params)
      this.permissionRoutes = defaultFormData.api.options = defaultButtonFormData.api.options = res.data.items
    },

    getRouterComponents () {
      _.each(constantRouterComponents, (res, index) => {
        this.routerComponents.push({ label: res.name, value: index })
      })

      defaultFormData.component.options = this.routerComponents
    },

    handleClick ({ key }) {
      this.selectKey = key
    },

    handleCreate (parentId) {
      this.loadingSkeleton(() => {
        this.formData = _.cloneDeep(defaultFormData)
        this.formData.parentId.value = parentId
        this.formData.isButton['change'] = this.buttonChange
        this.formData.isButton.value = false
        this.formData.buttons.data = this.buttons
        this.visible = true
        this.title = '添加菜单'
      }, 200)
    },

    handleCreateButton (parentId) {
      this.loadingSkeleton(() => {
        this.formData = _.cloneDeep(defaultButtonFormData)
        this.formData.parentId.value = parentId
        this.formData.isButton.value = true
        this.visible = true

        this.title = '添加按钮'
        let title = ''
        dataList.map(res => {
          if (res.id == parentId) {
            title = res.title
          }
        })

        this.title = title + ' - ' + this.title
      }, 200)
    },

    handleEdit (item) {
      this.formData = setFormValue(_.cloneDeep(defaultFormData), item)
      this.formData.isButton['change'] = this.buttonChange
      this.visible = true
      this.title = '编辑'
    },

    handleEditButton (item) {
      this.formData = setFormValue(_.cloneDeep(defaultButtonFormData), item)
      this.formData.isButton['change'] = this.buttonChange
      this.visible = true
      this.title = '编辑按钮'

      let title = ''
      dataList.map(res => {
        if (res.id == item.parentId) {
          title = res.title
        }
      })

      this.title = title + ' - ' + this.title
    },

    handleClone (parentId) {
      this.loadingSkeleton(() => {
        dataList.forEach((item) => {
          if (item.key === parentId) {
            if (item.isButton === false) {
              this.formData = setFormValue(_.cloneDeep(defaultFormData), item)
              this.formData.id.value = null
              this.formData.isButton['change'] = this.buttonChange
              this.formData.buttons.value = []
              this.formData.buttons.data = this.buttons
              this.visible = true
              this.title = '复制菜单'
            } else {
              this.formData = setFormValue(_.cloneDeep(defaultButtonFormData), item)
              this.formData.id.value = null
              this.formData.isButton['change'] = this.buttonChange
              this.visible = true
              this.title = '复制按钮'
            }
          }
        })
      }, 200)
    },

    handleDel () {
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
          permissionDeleteByIds({ id: id })
            .then((res) => that.submitSuccess(res))
            .then((res) => {
              if (res.code === httpResponseCode.SUCCESS) {
                that.checkedKeys = []
                that.getPermissionList()
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

    handleReload () {
      this.reloadLoading = true
      this.getPermissionList().then(() => { this.reloadLoading = false })
    },

    handleChange () {},

    handleSubmit (e) {
      e.preventDefault()

      this.$refs['form'].validateFields((err, values) => {
        if (!err) {
          this.submitLoading = true
          if (values.id > 0) {
            permissionUpdate(values)
              .then((res) => this.submitSuccess(res))
              .then((res) => {
                if (res.code === httpResponseCode.SUCCESS) {
                  this.getPermissionList()
                }
              })
              .catch(err => this.requestFailed(err))
              .finally(() => { this.submitLoading = false })
          } else {
            if (values['buttons']) {
              _.each(values['buttons'], (e, i) => {
                values['buttons'][i] = values['name'] + '_' + e
              })
            }

            permissionCreate(values)
              .then((res) => this.submitSuccess(res))
              .then((res) => {
                if (res.code === httpResponseCode.SUCCESS) {
                  this.getPermissionList()
                }
              })
              .catch(err => this.requestFailed(err))
              .finally(() => { this.submitLoading = false })
          }
        } else {
          this.submitLoading = false
        }

        _.each(err, (val) => {
          val = val['errors'][0]
          this.$message.error(this.formData[val['field']]['label'] + val['message'])

          return false
        })
      })
    },

    loadingSkeleton (callback, time) {
      this.formLoading = true
      callback()
      setTimeout(() => { this.formLoading = false }, time)
    }

  }
}
</script>

<style lang="less" scoped>
  .ant-card-wider-padding{
    .ant-card-body {
      padding: 10px;
    }
  }

  .select-box {
    border: 1px solid #1890ff;
  }

  .ant-form-item {
    margin-bottom: 0px;
  }

  .ant-divider {
    margin: 20px 0px 0px 0px;
  }

  /deep/ .ant-form-item-children {
    .btn {
      display: none;
    }

    &:hover {
      .btn {
        display: block;
      }
    }

  }

  /deep/ .ant-card .ant-card-body {
    padding: 10px;
  }
</style>
