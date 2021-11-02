<template>

  <div style="background: #ffffff;padding: 10px">
    <div>
      <a-form layout="inline">
        <a-form-item label="数据库">
          <a-select style="width: 300px" @change="dbChange" v-model="dbName" show-search>
            <a-select-option :value="v.value" v-for="(v, i) in databases" :key="i">
              {{ v.label }}
            </a-select-option>
          </a-select>
        </a-form-item>

        <a-form-item label="表">
          <a-select style="width: 300px;margin-left: 10px" @change="tableChange" v-model="tableName" show-search :filter-option="filterOption">
            <a-select-option :value="v.value" v-for="(v, i) in tables" :key="i">
              {{ v.label }}
            </a-select-option>
          </a-select>
        </a-form-item>

      </a-form>
    </div>

    <a-card
      style="width:100%"
      :bordered="false"
      :tabList="tabListNoTitle"
      :activeTabKey="noTitleKey"
      @tabChange="key => handleTabChange(key, 'noTitleKey')"
    >
      <div v-show="noTitleKey === 'form'">
        <form-generator
          ref="form"
          :fields="formData"
          :showFooter="false"
          :formLayout="formLayout"
          :formItemLayout="formItemLayout"
        >
        </form-generator>
      </div>

      <div v-show="noTitleKey === 'fields'">
        <div style="padding: 10px 0px;">
          <div data-simplebar-auto-hide="false">
            <DynamicInput
              :fields="fields"
              v-model="columns"
              :layout="layout"
              :itemLayout="itemLayout"
              @change="onChange"></DynamicInput>
            <br/>
          </div>
        </div>
      </div>

      <div v-show="noTitleKey === 'preview'">

        <a-tabs default-active-key="0" tab-position="left">
          <a-tab-pane :key="i" :tab="i" v-for="(source, i) in sources">
            <a-button icon="copy" @click="copy(source)">复制</a-button>
            <vue-markdown :source="'```\n' + source + '\n```'" style="background: #e8e8e8;padding: 5px;margin-top: 5px"></vue-markdown>
          </a-tab-pane>
        </a-tabs>

      </div>

    </a-card>

    <div style="height: 100px"></div>

    <a-drawer
      title="生成"
      :width="720"
      @close="onClose"
      :visible="visible"
      :body-style="{ paddingBottom: '80px', height: '100%' }"
    >
      <div style="height: 50%">
        <a-textarea v-model="jsonStr" @change="handleJsonToJavaPojo" rows="5" placeholder="json" style="height: 100%"></a-textarea>
      </div>
      <div style="height: 50%;margin-top: 5px">
        <a-textarea v-model="javaPojo" rows="5" placeholder="Java POJO" readonly style="height: 100%"></a-textarea>
      </div>

      <div
        class="drawer-form-footer"
      >
        <a-button
          @click="copy(jsonStr)"
        >
          复制 JSON
        </a-button>
        <a-button
          :style="{ marginLeft: '8px' }"
          @click="copy(javaPojo)"
        >
          复制 Java POJO
        </a-button>
        <a-button
          :style="{ marginLeft: '8px' }"
          @click="setFields"
        >
          JSON 设置字段
        </a-button>
        @ApiModelProperty <a-switch v-model="isApi" @change="handleJsonToJavaPojo"></a-switch>
      </div>

    </a-drawer>

    <footer-tool-bar>

      <a-button
        type="primary"
        @click="handleSubmit"
        :loading="submitLoading"
      >
        生成
      </a-button>

      <a-button
        style="margin-left: 10px"
        type="primary"
        @click="handleCodePreview"
        :loading="codePreviewLoading"
      >
        预览
      </a-button>

      <a-button
        type="primary"
        @click="showDrawer"
        style="margin-left: 10px"
      >
        Json To Java
      </a-button>
    </footer-tool-bar>

  </div>
</template>

<script>
import { codePreview, createTemp, getColumns, getDatabases, getTables } from '@/modules/generator/api/api'
import FooterToolBar from '@/components/FooterToolbar'
import DynamicInput from '@/components/Form/DynamicInput'
import { fields, formData } from './form'
import { httpResponseCode } from '@/constants/httpResponseCode'
import _ from 'lodash'
import simplebar from 'simplebar-vue'
import 'simplebar/dist/simplebar.min.css'
import VueMarkdown from 'vue-markdown'
import storage from 'store'
import jsonToPojoConverter from '../utils/jsonToJavaPojo'
import { toSQLLine, toUpperCaseHump } from '@/utils/str'

export default {
  name: 'Index',
  components: {
    DynamicInput,
    FooterToolBar,
    'vue-markdown': VueMarkdown,
    simplebar
  },
  data () {
    const vm = this
    return {
      active: 1,
      formLayout: 'inline',
      submitLoading: false,
      codePreviewLoading: false,
      visible: false,
      layout: 'inline',
      itemLayout: {},
      formItemLayout: {
        labelCol: { span: 4 },
        wrapperCol: { span: 14 }
      },
      dbName: '',
      tableName: '',
      databases: [],
      tables: [],
      columns: [],
      inputValue: [],
      tabListNoTitle: [
        {
          key: 'form',
          tab: '基础设置',
          count: 0
        },
        {
          key: 'fields',
          tab: '字段设置',
          count: 0
        },
        {
          key: 'preview',
          tab: '预览',
          count: 0
        }
      ],
      noTitleKey: 'form',
      fields: fields,
      formData: formData,
      sources: {},
      dictType: [],
      javaPojo: null,
      jsonStr: null,
      isApi: true
    }
  },
  async created () {
    const res = await getDatabases()
    this.databases = res.data.items
    this.dbName = res.data.dbName

    const res2 = await getTables({ 'dbName': this.dbName })
    this.tables = res2.data.items

    this.dictType = storage.get('DICT')
  },
  methods: {
    getTables (dbName) {
      getTables({ 'dbName': this.dbName }).then(res => {
        this.tables = res.data.items
      })
    },
    dbChange (e) {
      this.getTables(this.dbName)
    },
    tableChange () {
      this.formData.className.value = toUpperCaseHump(this.tableName)
      this.formData.tableName.value = this.tableName
      this.formData.fileName.value = this.tableName

      getColumns({ tableName: this.tableName, dbName: this.dbName }).then(res => {
        this.columns = []
        this.columns = res.data.items
        this.formData.rowKey.options = this.columns
        _.each(this.columns, (v, i) => {
          v['fieldName'] = _.camelCase(v['columnName'])
          v['fieldDesc'] = v['columnComment'] || _.camelCase(v['columnName'])
          v['fieldType'] = v['fieldType'] || 'String'
          v['fieldJson'] = _.camelCase(v['columnName'])
          v['inputType'] = 'input'
          v['fieldSearchType'] = '='
          v['tableAlign'] = 'left'
          v['tableWidth'] = '100px'
          v['inputRules'] = []
        })
      })
    },
    onChange () {

    },
    handleSubmit () {
      const v = this.formChange()
      if (v == false) {
        return
      }
      this.$refs['form'].validateFields((err, values) => {
        if (this.columns.length == 0) {
          this.$message.error('至少添加一个字段')
          return
        }
        values['fields'] = this.columns
        if (!err) {
          this.submitLoading = true
          this.createTemp(values)
        }
      })
    },
    handleCodePreview (val) {
      const v = this.formChange()
      if (v == false) {
        return
      }

      if (this.columns.length == 0) {
        this.$message.error('至少添加一个字段')
        return
      }

      this.$refs['form'].validateFields((err, values) => {
        values['fields'] = this.columns
        if (!err) {
          this.codePreview(values)
        }
      })
    },
    codePreview (val) {
      codePreview(val).then(res => {
        if (res.code === httpResponseCode.SUCCESS) {
          this.sources = res.data.autoCode
          this.noTitleKey = 'preview'
        } else {
          this.$message.error(res.message)
        }
      })
    },
    createTemp (val) {
      createTemp(val).then(data => {
        if (data != null) {
          if (data.size == 0) {
            this.$message.success('自动化代码创建成功')
            return
          }

          this.$message.success('自动化代码创建成功，正在下载')
          const blob = new Blob([data], { type: 'application/zip' })
          const fileName = 'autocode.zip'
          if ('download' in document.createElement('a')) {
            // 不是IE浏览器
            const url = window.URL.createObjectURL(blob)
            const link = document.createElement('a')
            link.style.display = 'none'
            link.href = url
            link.setAttribute('download', fileName)
            document.body.appendChild(link)
            link.click()
            document.body.removeChild(link) // 下载完成移除元素
            window.URL.revokeObjectURL(url) // 释放掉blob对象
          } else {
            // IE 10+
            window.navigator.msSaveBlob(blob, fileName)
          }
        } else {
          this.$message.error('生成失败')
        }
      }).finally(() => {
        this.submitLoading = false
      })
    },
    filterOption (input, option) {
      return (
        option.componentOptions.children[0].text.toLowerCase().indexOf(input.toLowerCase()) >= 0
      )
    },
    onClose () {
      this.visible = false
    },
    showDrawer () {
      this.visible = true
    },
    gen (item) {

    },
    layoutChange (e) {
      console.log(e)
      if (e == true) {
        this.layout = ''
        this.itemLayout = {
          labelCol: {
            xs: { span: 24 },
            sm: { span: 6 }
          },
          wrapperCol: {
            xs: { span: 24 },
            sm: { span: 18 }
          }
        }
      } else {
        this.layout = 'inline'
        this.itemLayout = {}
      }
    },
    handleTabChange (key, type) {
      this[type] = key
    },
    handleJsonToJavaPojo () {
      if (this.jsonStr == '') {
        this.javaPojo = ''
        return
      }
      const res = jsonToPojoConverter().convert(this.jsonStr, false, this.isApi)
      this.javaPojo = res
    },
    formChange () {
      if (this.formData.className.value == null || this.formData.className.value == '') {
        if (this.formData.tableName.value != '') {
          this.formData.className.value = this.formData.tableName.value
        }
      }
      if (this.formData.tableName.value == null || this.formData.tableName.value == '') {
        if (this.formData.className.value != '') {
          this.formData.tableName.value = this.formData.className.value
        }
      }
      if (this.formData.fileName.value == null || this.formData.fileName.value == '') {
        if (this.formData.tableName.value != '') {
          this.formData.fileName.value = this.formData.tableName.value
        }
      }

      if (this.formData.description.value == null || this.formData.description.value == '') {
        this.formData.description.value = this.formData.className.value
      }

      this.formData.className.value = toUpperCaseHump((this.formData.className.value))
      this.formData.tableName.value = toSQLLine(this.formData.tableName.value)
      this.formData.fileName.value = toSQLLine(this.formData.fileName.value)

      let isVi = true
      _.each(this.columns, (v, i) => {
        if (v['fieldName'] == '') {
          isVi = false
          this.$message.error('字段名称不能为空')
          return
        }

        const fieldName = _.camelCase(v['fieldName'])

        v['fieldName'] = fieldName

        if (v['fieldJson'] == '' && fieldName) {
          v['fieldJson'] = _.lowerFirst(fieldName)
        }
        if (v['columnName'] == '' && v['fieldJson']) {
          v['columnName'] = toSQLLine(v['fieldJson'])
        }

        v['fieldDesc'] = v['columnComment'] || _.camelCase(v['columnName'])
      })

      return isVi
    },
    setFields () {
      const objs = JSON.parse(this.jsonStr)
      if (_.isObject(objs)) {
        const columns = []
        const column = {
          'columnName': '',
          'dataType': 'string',
          'dataTypeLong': '',
          'columnComment': ''
        }
        _.each(objs, (v, k) => {
          const fieldName = _.camelCase(k)
          column['columnName'] = toSQLLine(k)
          column['fieldName'] = fieldName
          column['fieldDesc'] = fieldName
          column['fieldType'] = this.getType(v)
          column['fieldJson'] = _.camelCase(k)
          column['inputType'] = 'input'
          column['fieldSearchType'] = '='
          column['tableAlign'] = 'left'
          column['tableWidth'] = '100px'
          column['inputRules'] = []
          columns.push(_.cloneDeep(column))
        })

        this.columns = columns
        this.formData.rowKey.options = this.columns
        this.noTitleKey = 'fields'
      }
    },
    getType (val) {
      let typeInfo = typeof val
      const type = typeof val

      if (val == null) {
        typeInfo = 'String'
      }

      switch (type) {
        case 'object':
          typeInfo = 'Object'
          break
        case 'string':
          // eslint-disable-next-line no-useless-escape
          if (/(\d{2}|\d{4})[\-\\\/]\d{1,2}[\-\\\/]\d{1,2}/.test(val)) {
            typeInfo = 'Date'
          }

          break
        case 'number':
          if (Number.isInteger(val)) {
            typeInfo = 'Integer'
          } else {
            typeInfo = 'Double'
          }

          break
      }

      return typeInfo
    },
    copy (text) {
      text = text.toString().replace('```go', '').replace('```', '')
      this.$copyText(text).then(message => {
        console.log('copy', message)
        this.$message.success('复制完毕')
      }).catch(err => {
        console.log('copy.err', err)
        this.$message.error('复制失败')
      })
    }
  }
}
</script>

<style scoped>

</style>
