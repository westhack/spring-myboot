
export const defaultFormData = {
  'id': {
    name: 'id',
    label: '',
    type: 'hidden',
    value: '',
    valueType: 'int',
    rules: []
  },
  'name': {
    name: 'name',
    label: '字典名（中）',
    type: 'input',
    value: '',
    rules: [{ required: true, message: '不能为空' }]
  },
  'type': {
    name: 'type',
    label: '字典名（英）',
    type: 'input',
    value: '',
    change: null,
    rules: [{ required: true, message: '不能为空' }]
  },
  'sortOrder': {
    name: 'sortOrder',
    label: '排序',
    type: 'input-number',
    value: 99999,
    placeholder: '越小越靠前',
    rules: []
  },
  'status': {
    name: 'status',
    label: '状态',
    type: 'switch',
    value: true,
    operator: '='
  },
  'description': {
    name: 'description',
    label: '说明',
    type: 'textarea',
    value: '',
    rules: []
  }
}
