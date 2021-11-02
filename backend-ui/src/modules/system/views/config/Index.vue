<template>
  <a-card :bordered="false">

    <div class="table-operator">
      <a-button type="primary" icon="plus" @click="handleCreate()">添加</a-button>
      <a-button type="primary" icon="plus" @click="handleUpdate(selectKey)" v-show="selectKey">修改</a-button>
      <a-button type="default" icon="plus" @click="handleClone(selectKey)" v-show="selectKey">复制</a-button>
      <a-button type="danger" icon="delete" @click="handleDel(selectKey)" v-show="selectKey">删除</a-button>
      <a-button type="default" icon="reload" @click="handleReload">刷新</a-button>
    </div>

    <a-row :gutter="8" v-permission:config_query>
      <a-col :span="5">
        <a-affix :offsetTop="10">
          <a-skeleton active :loading="loading">
            <div style="max-height: calc(100vh - 120px);overflow:auto">
              <s-tree
                :dataSource="modules"
                :openKeys.sync="openKeys"
                :search="true"
                :toolBar="toolBar"
                @click2="handleClick"
                @titleClick="handleTitleClick"
                @toolBarClick="handleBar"
                @searchClick="handleSearch"
              ></s-tree>
            </div>
          </a-skeleton>
        </a-affix>
      </a-col>

      <a-col :span="19">
        <a-skeleton active :loading="loading">
          <a-form
            :form="form"
            @submit="handleSubmitUpdateValue"
          >
            <a-collapse
              v-for="(module, key) in modules"
              :class="selectKey === module.name ? 'select-box' : ''"
              :title="module.label || module.name"
              :id="module.name"
              :key="key"
              :bordered="true"
              v-model="activeKey"
              :expandIconPosition="`right`"
            >
              <a-collapse-panel
                :key="module.name"
                :header="module.label"
                style="background: #f7f7f7;border-radius: 4px;margin-bottom: 24px;border: 1px;overflow: hidden"
              >
                <div
                  v-for="(field, index2) in module.children"
                  :class="selectKey === field.name ? 'select-box' : ''"
                  :key="index2"
                  style="padding: 5px;"
                >
                  <a-form-item
                    :label="field.label || field.name"
                    :required="false"
                    :extra="getExtra(field)"
                    :id="field.name"
                  >

                    <div style="display: flex;">
                      <form-item-generator
                        style="flex: 1"
                        :key="index2"
                        :item="field"
                        :options="field.data"
                        :itemChange="handleChange"
                        :defaultValue="field.value"
                        v-decorator="[
                          `${field.name}`,
                          {
                            initialValue: field.value,
                            validateTrigger: ['change', 'blur'],
                            rules: field.rules
                          }
                        ]"
                      />
                      <div style="width: 10px">
                        <a class="btn" style="width: 10px; padding: 0px 10px">
                          <a-dropdown>
                            <a-icon type="ellipsis"/>
                            <a-menu slot="overlay">
                              <a-menu-item key="edit" @click="handleBar('edit', field)" v-permission:config_update>
                                修改
                              </a-menu-item>

                              <a-menu-item key="del" v-permission:config_delete>
                                <a-popconfirm title="确定要删除?" @confirm="handleBar('del', field)" okText="是" cancelText="否">
                                  <div>删除</div>
                                </a-popconfirm>
                              </a-menu-item>
                            </a-menu>
                          </a-dropdown>
                        </a>
                      </div>
                    </div>

                  </a-form-item>
                </div>

              </a-collapse-panel>

            </a-collapse>

            <footer-tool-bar>

              <a-button
                type="primary"
                html-type="submit"
                :loading="saveLoading"
              >
                保存
              </a-button>
              <a-button
                type="primary"
                :style="{ marginLeft: '8px' }"
                @click="handleWrite"
              >
                写入配置
              </a-button>
              <a-button
                :style="{ marginLeft: '8px' }"
                @click="handleReset"
              >
                重置
              </a-button>

              <a-button
                :style="{ marginLeft: '8px' }"
                @click="handleReload"
                :loading="reloadLoading"
              >
                刷新
              </a-button>

            </footer-tool-bar>
          </a-form>
        </a-skeleton>
      </a-col>
    </a-row>

    <a-drawer
      title="修改"
      :width="720"
      @close="onClose"
      :visible="visible"
      :body-style="{ paddingBottom: '80px' }"
    >

      <form-generator
        ref="formConfig"
        @handleSubmit="handleSubmit"
        :fields="formData"
        :showFooter="true"
        :formLayout="formLayout"
      >
        <template slot="form-item-after" slot-scope="{item}">
          <div v-if="item.name == 'name'">
            &nbsp;<a-button size="small" @click="genPinyin">生成</a-button>
          </div>
        </template>
        <template slot="footer">
          <br>
          <div
            class="drawer-form-footer"
          >
            <a-button
              type="primary"
              html-type="submit"
              :loading="submitLoading"
            >
              提交
            </a-button>
            <a-button
              :style="{ marginLeft: '8px' }"
              @click="onClose"
            >
              关闭
            </a-button>
          </div>
        </template>
      </form-generator>

    </a-drawer>

  </a-card>
</template>

<script>
import { httpResponseCode } from '@/constants/httpResponseCode'
import { getConfigList, configCreate, configDelete, configUpdate, configBatchUpdateValue, configWrite } from '@/modules/system/api/config'
import { STable } from '@/components'
import FooterToolBar from '@/components/FooterToolbar'
import STree from '@/modules/system/components/tree/Tree'
import httpResponse from '@/mixins/httpResponse'
import draggable from 'vuedraggable'
import { defaultFormData, formatConfigDataSource, formatConfigRule } from './form'
import _ from 'lodash'
import pinyin from 'pinyin'
import { listToTree2 } from '@/utils/util'
import { parseValueType } from '@/components/FormGenerator/utils'
import SFormItemGenerator from '@/components/FormGenerator/SFormItemGenerator'

export default {
  name: 'ConfigList',
  mixins: [httpResponse],
  components: {
    STable,
    draggable,
    FooterToolBar,
    STree,
    SFormItemGenerator
  },
  data () {
    return {
      loading: true,
      submitLoading: false,
      saveLoading: false,
      reloadLoading: false,
      formLayout: 'vertical',
      visible: false,
      openKeys: [],
      modules: [],
      items: [],
      selectKey: '',
      advancedSettings: [],
      formData: {},
      parentNames: [],
      activeKey: [],
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
  beforeCreate () {
    this.form = this.$form.createForm(this)
  },
  created () {
    this.getConfigList().then((res) => { this.loading = false })

    // defaultFormData.label.change = (val, e) => {
    //   if (this.formData.id.value == '') {
    //     this.formData.name.value = this.pinyin(val)
    //   }
    // }
  },
  methods: {
    async getConfigList (params) {
      const res = await getConfigList(params)
      const modules = _.cloneDeep(res.data.items)
      this.items = _.cloneDeep(res.data.items)
      modules.forEach((m) => {
        if (m.type === 'select') {
          m.data = formatConfigDataSource(m)
        }
        m.rules = formatConfigRule(m)
      })

      this.modules = listToTree2(modules, 'name', 'parentName', '')
      this.modules.forEach((children) => {
        this.parentNames.push({
          label: children.label,
          value: children.name
        })
        this.activeKey.push(children.name)
      })

      defaultFormData.parentName.options = this.parentNames
    },

    handleClick ({ key }) {
      // this.selectKey = key
      // this.$nextTick(() => {
      //   const anchor = this.$el.querySelector('#' + key)
      //   document.documentElement.scrollTop = anchor.offsetTop
      // })
    },

    handleTitleClick (item) {
      this.selectKey = item.name

      this.activeKey.push(item.name)
      this.$nextTick(() => {
        let anchor = this.$el.querySelector('#' + item.name)
        if (anchor.offsetTop > 0) {
          document.documentElement.scrollTop = anchor.offsetTop
        } else {
          let tTop = 0
          while (anchor.offsetParent) {
            tTop += anchor.offsetTop
            anchor = anchor.offsetParent
          }
          document.documentElement.scrollTop = tTop - 150
        }
      })
    },

    handleBar (key, item) {
      if (key === 'edit') {
        this.handleUpdate(item.name)
      } else if (key === 'add') {
        this.handleCreate()
      } else if (key === 'del') {
        this.handleDel(item.name)
      }
    },

    handleSearch (val) {
      this.getConfigList({ label: val })
    },

    handleDel (key) {
      if (!this.$permission('config_delete')) {
        this.$message.error('暂无权限')
        return
      }
      const that = this
      this.$confirm({
        title: '确认删除选择数据?',
        content: '删除后无法恢复',
        okText: '确认',
        okType: 'danger',
        cancelText: '取消',
        onOk () {
          configDelete({ name: key })
            .then((res) => that.submitSuccess(res))
            .then((res) => {
              if (res.code === httpResponseCode.SUCCESS) {
                this.getConfigList()
              }
            })
            .catch(err => that.requestFailed(err))
            .finally(() => {})
        },
        onCancel () {
        }
      })
    },

    handleSubmitUpdateValue (e) {
      e.preventDefault()
      this.saveLoading = true

      this.form.validateFields((err, values) => {
        if (!err) {
          const fields = _.keyBy(this.items, 'name')
          _.each(values, (_value, name) => {
            const type = fields[name]['valueType'] || null
            if (type !== null) {
              values[name] = parseValueType(name, _value, fields[name])
            }
          })
          configBatchUpdateValue(values)
            .then((res) => this.submitSuccess(res))
            .catch(err => this.requestFailed(err))
            .finally(() => { this.saveLoading = false })
        }
        this.saveLoading = false
      })
    },

    handleWrite () {
      const values = this.form.getFieldsValue()
      configWrite(values).then(res => {
        console.log(res)
        if (res.code == httpResponseCode.SUCCESS) {
          this.$message.success(res.message)
        } else {
          this.$message.error(res.message)
        }
      })
    },

    handleReset () {
      this.form.resetFields()
    },

    handleReload () {
      this.reloadLoading = true
      this.getConfigList().then(() => { this.reloadLoading = false })
    },

    handleClone (key) {
      const item = _.find(this.items, (o) => { return o.name == key })
      if (!this.$permission('config_create')) {
        this.$message.error('暂无权限')
        return
      }

      this.formData = _.cloneDeep(defaultFormData)

      delete this.formData.value
      _.each(item, (value, name) => {
        if (this.formData[name] !== undefined) {
          if (name === 'value') {
            this.formData[name]['value'] = JSON.stringify(value, null, 2)
          } else {
            this.formData[name]['value'] = value
          }
        }
      })

      this.formData[name]['id']['value'] = null

      this.visible = true
    },

    handleCreate () {
      if (!this.$permission('config_create')) {
        this.$message.error('暂无权限')
        return
      }

      this.formData = _.cloneDeep(defaultFormData)
      this.visible = true
    },

    handleUpdate (key) {
      const item = _.find(this.items, (o) => { return o.name === key })
      if (!this.$permission('config_update')) {
        this.$message.error('暂无权限')
        return
      }

      this.formData = _.cloneDeep(defaultFormData)

      delete this.formData.value
      _.each(item, (value, name) => {
        if (this.formData[name] !== undefined) {
          if (name === 'value') {
            this.formData[name]['value'] = JSON.stringify(value, null, 2)
          } else {
            this.formData[name]['value'] = value
          }
        }
      })

      this.visible = true
    },

    handleChange () {
    },

    showDrawer () {
      this.visible = true
    },
    onClose () {
      this.visible = false
    },

    handleSubmit (e) {
      e.preventDefault()

      this.$refs['formConfig'].validateFields((err, values) => {
        if (!err) {
          this.submitLoading = true
          if (values.id > 0) {
            configUpdate(values)
              .then((res) => this.submitSuccess(res))
              .then((res) => {
                if (res.code === httpResponseCode.SUCCESS) {
                  this.getConfigList()
                }
              })
              .catch(err => this.requestFailed(err))
              .finally(() => { this.submitLoading = false })
          } else {
            configCreate(values)
              .then((res) => this.submitSuccess(res))
              .then((res) => {
                if (res.code === httpResponseCode.SUCCESS) {
                  this.getConfigList()
                }
              })
              .catch(err => this.requestFailed(err))
              .finally(() => { this.submitLoading = false })
          }
        }

        this.submitLoading = false
      })
    },

    genPinyin () {
      const label = this.$refs['formConfig'].getFieldValue('label')
      if (label != '') {
        this.formData.name.value = this.pinyin(label)
      }
    },

    pinyin (value) {
      return (pinyin(value, { style: pinyin.STYLE_NORMAL })).toString().replace(/,/g, '_')
    },

    getExtra (field) {
      return field.description
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
</style>
