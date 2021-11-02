<template>
  <a-card :bordered="false" style="min-height: 100%">
    <div>
      <div class="table-operator">
        <a-button type="primary" icon="plus" @click="handleCreate()">新增字典</a-button>
        <a-button type="default" icon="edit" @click="handleUpdate(selectKey)" v-show="selectKey">编辑字典</a-button>
        <a-button type="default" icon="copy" @click="handleClone(selectKey)" v-show="selectKey">复制</a-button>
        <a-button type="danger" icon="delete" @click="handleDel" v-if="checkedKeys.length > 0 || (checkedKeys.checked && checkedKeys.checked.length > 0)">删除</a-button>
        <a-button type="default" icon="reload" @click="handleReload">刷新</a-button>
      </div>

      <a-row :gutter="24">
        <a-col :xs="24" :md="6" :lg="7">
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
          <a-card :title="detailTitle" v-if="detailVisible">
            <a-skeleton active :loading="formLoading">
              <a-form-item>
                <DictInput v-model="dictDetails"></DictInput>
                <a-button type="primary" style="width: 100%;" @click="submitDictDetail" :loading="saveLoading">保存</a-button>
              </a-form-item>
            </a-skeleton>
          </a-card>

          <a-drawer
            :title="title"
            :width="formWidth"
            @close="formClose"
            :visible="visible"
            :body-style="{ paddingBottom: '80px' }"
          >
            <form-generator
              ref="form"
              @handleSubmit="handleSubmit"
              :fields="formData"
              :showFooter="true"
              :formLayout="formLayout"
            >
              <template slot="form-item-before" slot-scope="{item}">
                <div v-if="item.name == 'type'">
                  &nbsp;<a-button size="small" @click="genPinyin">生成</a-button>
                </div>
              </template>
              <template slot="footer">
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
                    @click="handleReset"
                  >
                    重置
                  </a-button>
                </div>
              </template>
            </form-generator>
          </a-drawer>
        </a-col>
      </a-row>
    </div>

  </a-card>
</template>

<script>
import { httpResponseCode } from '@/constants/httpResponseCode'
import {
  getDictList, dictCreate, dictUpdate, dictDeleteByIds, saveDictDetail
} from '@/modules/system/api/dict'
import { STable } from '@/components'
import DictInput from '@/components/Form/DictInput'
import FooterToolBar from '@/components/FooterToolbar'
import httpResponse from '@/mixins/httpResponse'
import { defaultFormData } from './form'
import _ from 'lodash'
import { setFormValue, treeParseData } from '@/utils/util'
import pinyin from 'pinyin'

export default {
  name: 'DictList',
  mixins: [httpResponse],
  components: {
    STable,
    DictInput,
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
      detailVisible: false,
      models: [],
      items: [],
      selectKey: '',
      formData: {},
      formWidth: '600px',
      title: '编辑',
      detailTitle: '编辑字典内容',
      checkStrictly: false,
      treeCheckStrictly: false,
      expandedKeys: [],
      checkedKeys: [],
      autoExpandParent: false,
      dictDetails: []
    }
  },
  watch: {
  },
  created () {
    this.formData = defaultFormData
    this.getDictList().then((res) => { this.loading = false })
  },
  methods: {
    onExpand  (expandedKeys) {
      this.expandedKeys = expandedKeys
      this.autoExpandParent = false
    },

    handleSelect (selectedKeys) {
      this.loadingSkeleton(() => {
        if (selectedKeys[0]) {
          this.visible = false
          this.selectKey = selectedKeys[0]

          const item = _.find(this.items, (o) => { return o.id == this.selectKey })
          if (!item) {
            return
          }

          this.detailTitle = '编辑字典：' + item.name + ' 内容'
          const dictDetails = []
          _.each(item.dictDetails, (dictDetail) => {
            dictDetails.push({
              label: dictDetail.label,
              value: dictDetail.value,
              color: dictDetail.color,
            })
          })

          this.dictDetails = dictDetails
          this.detailVisible = true
        } else {
          this.selectKey = ''
        }
      }, 500)
    },

    submitDictDetail () {
      if (this.dictDetails.length == 0) {
        return
      }

      this.saveLoading = true
      const val = {
        'dictId': parseInt(this.selectKey),
        'dictDetails': this.dictDetails
      }

      saveDictDetail(val)
        .then((res) => this.submitSuccess(res))
        .then((res) => {
          if (res.code === httpResponseCode.SUCCESS) {
            this.getDictList()
          }
        })
        .catch(err => this.requestFailed(err))
        .finally(() => { this.saveLoading = false })
    },

    async getDictList (params) {
      const res = await getDictList(params)
      this.items = res.data.items
      this.models = treeParseData(res.data.items, 'id', 'name')
    },

    handleUpdate (key) {
      const item = _.find(this.items, (o) => { return o.id == key })
      if (item) {
        this.title = '编辑：' + item.name
        this.formData = setFormValue(_.cloneDeep(defaultFormData), item)
        this.visible = true
      }
    },

    handleSearch (val) {
      this.getDictList({ name: val })
    },

    handleDel () {
      if (!this.$permission('dict_delete')) {
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
          dictDeleteByIds({ id: id })
            .then((res) => that.submitSuccess(res))
            .then((res) => {
              if (res.code === httpResponseCode.SUCCESS) {
                that.checkedKeys = []
                that.getDictList()
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
      await this.getDictList().then(() => { this.reloadLoading = false })
      if (this.$refs['form']) {
        this.$refs['form'].setFormFields(this.formData)
      }
    },

    handleChange () {},
    formClose () {
      this.visible = false
    },

    handleSubmit (e) {
      e.preventDefault()

      this.submitLoading = true
      this.$refs['form'].validateFields((err, values) => {
        if (!err) {
          if (values.id > 0) {
            dictUpdate(values)
              .then((res) => this.submitSuccess(res))
              .then((res) => {
                if (res.code === httpResponseCode.SUCCESS) {
                  this.getDictList()
                }
              })
              .catch(err => this.requestFailed(err))
              .finally(() => {
                this.submitLoading = false
              })
          } else {
            dictCreate(values)
              .then((res) => this.submitSuccess(res))
              .then((res) => {
                if (res.code === httpResponseCode.SUCCESS) {
                  this.getDictList()
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

    handleCreate () {
      if (!this.$permission('dict_create')) {
        this.$message.error('暂无权限')
        return
      }
      this.title = '创建：'
      this.formData = _.cloneDeep(defaultFormData)
      this.visible = true
    },
    handleClone (key) {
      this.loadingSkeleton(() => {
        this.items.forEach((item) => {
          if (item.key === key) {
            this.formData = setFormValue(_.cloneDeep(defaultFormData), item)
            this.formData.id.value = null
            this.formData.permissions.value = _.keys(_.keyBy(item.permissions, 'id'))
            this.visible = true
            this.title = '复制'
          }
        })
      }, 100)
    },

    loadingSkeleton (callback, time) {
      this.formLoading = true
      callback()
      setTimeout(() => { this.formLoading = false }, time)
    },

    pinyin (value) {
      return (pinyin(value, { style: pinyin.STYLE_NORMAL })).toString().replace(/,/g, '_')
    },

    genPinyin () {
      const name = this.$refs['form'].getFieldValue('name')
      if (name != '') {
        this.formData.type.value = this.pinyin(name)
      }
    }

  }
}
</script>

<style lang="less" scoped>

</style>
