
import vm from '@/main'
import storage from 'store'
import { types } from '@/components/FormGenerator/Types'
import { operators } from '@/components/FormGenerator/Operators'
import { api } from '../api/api'

export const fields = [
  {
    'label': '字段名称',
    'name': 'fieldName',
    'value': '',
    'type': 'input'
  },
  {
    'label': '字段类型',
    'name': 'fieldType',
    'value': 'string',
    'type': 'select',
    'options': vm.$store.getters.dict['fieldType']
  },
  {
    'label': 'json 名称',
    'name': 'fieldJson',
    'value': '',
    'type': 'input'
  },
  {
    'label': '数据库字段',
    'name': 'columnName',
    'value': '',
    'type': 'input'
  },
  {
    'label': '数据库类型',
    'name': 'dataType',
    'value': 'varchar',
    'type': 'select',
    'options': vm.$store.getters.dict['dataType']
  },
  {
    'label': '数据库长度',
    'name': 'dataTypeLong',
    'value': '',
    'type': 'input'
  },
  {
    'label': '数据库描述',
    'name': 'columnComment',
    'value': '',
    'type': 'input'
  },
  {
    'label': '表单类型',
    'name': 'inputType',
    'value': 'input',
    'type': 'select',
    'options': [{ label: '不设置', value: '' }].concat(types)
  },
  {
    'label': '表单规则',
    'name': 'inputRules',
    'value': [],
    'type': 'multiple-select',
    'options': [{ label: '不能为空', value: '{ required: true, message: \'不能为空\' }' }]
  },
  {
    'label': '搜索条件',
    'name': 'fieldSearchType',
    'value': '=',
    'type': 'select',
    'options': [{ label: '不设置', value: '' }].concat(operators)
  },
  {
    'label': '字典',
    'name': 'dictType',
    'value': '',
    'type': 'select',
    'options': [{ name: '不设置', type: '' }].concat(storage.get('DICT')),
    'labelName': 'name',
    'valueName': 'type'
  },
  {
    'label': '隐藏 Popover',
    'name': 'hiddenPopover',
    'value': true,
    'type': 'boolean'
  },
  {
    'label': '表格宽度',
    'name': 'tableWidth',
    'value': '100px',
    'type': 'input'
  },
  {
    'label': '表格对齐',
    'name': 'tableAlign',
    'value': 'left',
    'type': 'select',
    'options': [{ label: '居右', value: 'left' }, { label: '居中', value: 'center' }, { label: '居左', value: 'right' }]
  }
]

export const formData = {
  moduleName: {
    'label': '模块名称',
    'name': 'moduleName',
    'value': '',
    'type': 'select',
    'url': api.getModules,
    'options': [],
    'labelName': 'title',
    'valueName': 'name',
    'rules': [{ required: true, message: '不能为空' }]
  },
  className: {
    'label': '类名称',
    'name': 'className',
    'value': '',
    'type': 'input',
    'rules': [{ required: true, message: '不能为空' }]
  },
  primaryKeyType: {
    'label': '主键类型',
    'name': 'primaryKeyType',
    'value': 'Long',
    'type': 'select',
    'options': ['Integer', 'Long', 'String'],
    'rules': [{ required: true, message: '不能为空' }]
  },
  tableName: {
    'label': '表名称',
    'name': 'tableName',
    'value': '',
    'type': 'input',
    'rules': [{ required: true, message: '不能为空' }]
  },
  packageName: {
    'label': '包名称',
    'name': 'packageName',
    'value': '',
    'type': 'input',
    'hidden': true,
    'rules': []
  },
  fileName: {
    'label': '文件名称',
    'name': 'fileName',
    'value': '',
    'type': 'input',
    'rules': [{ required: true, message: '不能为空' }]
  },
  description: {
    'label': '中文名称',
    'name': 'description',
    'value': '',
    'type': 'input',
    'rules': [{ required: true, message: '不能为空' }]
  },
  autoCreateApiToSql: {
    'label': '是否自动创建api',
    'name': 'autoCreateApiToSql',
    'value': false,
    'type': 'boolean'
  },
  autoMoveFile: {
    'label': '是否自动移动文件',
    'name': 'autoMoveFile',
    'value': false,
    'type': 'boolean'
  },
  rowKey: {
    'label': '表格 Key',
    'name': 'rowKey',
    'value': 'id',
    'type': 'select',
    'options': vm.columns,
    'valueName': 'fieldJson',
    'labelName': 'fieldJson'
  },
  isSearch: {
    'label': '是否开启搜索',
    'name': 'isSearch',
    'value': true,
    'type': 'boolean'
  },
  isTableCreateUpdate: {
    'label': '是否开启表格编辑',
    'name': 'isTableCreateUpdate',
    'value': true,
    'type': 'boolean'
  },
  isFormCreateUpdate: {
    'label': '是否表单编辑',
    'name': 'isFormCreateUpdate',
    'value': false,
    'type': 'boolean'
  },
  isDblclickUpdate: {
    'label': '是否开启双击编辑',
    'name': 'isDblclickUpdate',
    'value': false,
    'type': 'boolean'
  },
  isBatchDelete: {
    'label': '是否开启批量删除',
    'name': 'isBatchDelete',
    'value': true,
    'type': 'boolean'
  },
  isTableDelete: {
    'label': '是否开启表格删除',
    'name': 'isTableDelete',
    'value': true,
    'type': 'boolean'
  },
  isDownload: {
    'label': '是否开启导出',
    'name': 'isDownload',
    'value': true,
    'type': 'boolean'
  },
  showPopover: {
    'label': '显示 Popover',
    'name': 'showPopover',
    'value': true,
    'type': 'boolean'
  }
}

export const listColumns = [
  {
    title: '#',
    scopedSlots: { customRender: 'serial' },
    width: '50px',
    align: 'center',
    dataIndex: 'id',
    type: 'hidden'
  },
  {
    title: '表格名',
    dataIndex: 'tableName',
    align: 'center',
    width: '100px',
    type: 'input',
    value: ''
  },
  {
    title: '请求元素',
    dataIndex: 'requestMeta',
    width: '150px',
    align: 'center',
    type: 'input',
    value: ''
  },
  {
    title: '生成路径',
    dataIndex: 'autoCodePath',
    width: '80px',
    align: 'center',
    type: 'input',
    value: ''
  },
  {
    title: '生成内容',
    dataIndex: 'autoCode',
    width: '80px',
    align: 'center',
    type: 'input',
    value: ''
  },
  {
    title: '类名称',
    dataIndex: 'className',
    width: '150px',
    align: 'center',
    type: 'input',
    value: ''
  },
  {
    title: '中文名称',
    dataIndex: 'description',
    width: '150px',
    align: 'center',
    type: 'input',
    value: ''
  },
  {
    title: 'API IDs',
    dataIndex: 'apiIds',
    width: '150px',
    align: 'center',
    type: 'input',
    value: ''
  },
  {
    title: '状态',
    dataIndex: 'flag',
    width: '150px',
    align: 'center',
    type: 'input',
    value: ''
  },
  {
    title: '请求时间',
    dataIndex: 'createdAt',
    width: '150px',
    align: 'center',
    type: 'date-picker'
  },
  {
    title: '操作',
    width: '100px',
    dataIndex: 'action',
    scopedSlots: { customRender: 'action' },
    align: 'center',
    fixed: 'right'
  }
]
