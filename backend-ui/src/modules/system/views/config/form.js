
import { types } from '@/components/FormGenerator/Types'
import _ from 'lodash'

export const defaultFormData = {
  'id': {
    name: 'id',
    label: '',
    type: 'hidden',
    value: '',
    rules: []
  },
  'module': {
    name: 'module',
    label: '',
    type: 'hidden',
    value: '',
    rules: []
  },
  'parentName': {
    name: 'parentName',
    label: '分组',
    type: 'select',
    value: '',
    rules: []
  },
  'label': {
    name: 'label',
    label: '名称（中）',
    type: 'input',
    value: '',
    change: null,
    rules: [{ required: true, message: '不能为空' }]
  },
  'name': {
    name: 'name',
    label: '名称（英）',
    type: 'input',
    value: '',
    rules: [{ required: true, message: '不能为空' }]
  },
  'value': {
    name: 'value',
    label: '值',
    type: 'textarea',
    value: '',
    rules: []
  },
  'type': {
    name: 'type',
    label: '类型',
    type: 'select',
    value: 'input',
    options: types,
    // showSearch: true,
    rules: [{ required: true, message: '不能为空' }]
  },
  'valueType': {
    name: 'valueType',
    label: '值类型',
    type: 'select',
    value: '',
    options: [
      'string',
      'int',
      'JSONString'
    ],
    rules: []
  },
  'dataSource': {
    name: 'dataSource',
    label: '数据',
    type: 'textarea',
    value: '',
    rules: []
  },
  'ruleSource': {
    name: 'ruleSource',
    label: '规则',
    type: 'textarea',
    value: '',
    rules: []
  },
  'multiple': {
    name: 'multiple',
    label: '是否多值',
    type: 'switch',
    value: false,
    rules: []
  },
  'description': {
    name: 'description',
    label: '描述',
    type: 'input',
    value: '',
    rules: []
  },
  'sortOrder': {
    name: 'sortOrder',
    label: '排序',
    type: 'input-number',
    value: '99999',
    placeholder: '越小越靠前',
    rules: [{ required: true, message: '不能为空' }]
  }
}

export function formatConfigDataSource (item) {
  const values = []
  if (item.type === 'select') {
    const dataSource = item.dataSource

    if (dataSource == null || dataSource == '') {
      return null
    }

    const rows = dataSource.split('\n')

    _.each(rows, (row) => {
      const cols = row.split('=')
      if (cols.length >= 2) {
        values.push({
          'label': cols[1],
          'value': cols[0]
        })
      } else {
        values.push(row)
      }
    })
    item.data = values
  }

  return values
}

export function formatConfigValue (item) {
  const values = []
  if (item.type === 'select') {
    const dataSource = item.dataSource

    if (dataSource == null || dataSource == '') {
      return null
    }

    const rows = dataSource.split('\n')

    _.each(rows, (row) => {
      const cols = row.split('=')
      if (cols.length >= 2) {
        values.push({
          'label': cols[1],
          'value': cols[0]
        })
      } else {
        values.push(row)
      }
    })
    item.data = values
  }

  return values
}

export function formatConfigRule (item) {
  const values = []
  const ruleSource = item.ruleSource
  if (ruleSource == null || ruleSource == '') {
    return null
  }

  const rows = ruleSource.split('\n')

  _.each(rows, (row, i) => {
    const cols = row.split(',')

    const vv = {}
    _.each(cols, (col) => {
      if (values[i] != undefined) {
        values[i] = {}
      }

      const keyVal = col.split('=')

      if (keyVal.length >= 2) {
        vv[keyVal[0]] = keyVal[1]
      } else {
        vv[keyVal[0]] = keyVal[1]
      }

      values[i] = vv
    })
  })
  item.rules = values

  return values
}
