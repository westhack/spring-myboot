export const defaultSearchFormData = [
  {
    name: 'id',
    label: 'id',
    type: 'minimum',
    value: null,
    operator: 'between'
  },
  {
    name: 'apiGroup',
    label: '分组',
    type: 'input',
    value: '',
    operator: '='
  },
  {
    name: 'path',
    label: '路径',
    type: 'input',
    value: '',
    operator: 'like'
  },
  {
    name: 'method',
    label: '方法',
    type: 'input',
    value: '',
    operator: '='
  },
  {
    name: 'description',
    label: '描述',
    type: 'input',
    value: '',
    operator: '='
  },
  {
    name: 'createdAt',
    label: '创建时间',
    type: 'range-picker',
    value: null,
    operator: 'between'
  }
]

export const defaultFormData = {
  'id': {
    name: 'id',
    label: '',
    type: 'hidden',
    value: '',
    valueType: 'int',
    rules: []
  },
  'apiGroup': {
    name: 'apiGroup',
    label: '分组',
    type: 'input',
    value: '',
    rules: []
  },
  'path': {
    name: 'path',
    label: '手机号',
    type: 'input',
    value: '',
    rules: []
  },
  'method': {
    name: 'method',
    label: '方法',
    type: 'input',
    value: '',
    rules: [],
    extra: ''
  },
  'description': {
    name: 'description',
    label: '中文描述',
    type: 'input',
    value: '',
    rules: []
  }
}

export const columns = [
  {
    title: '#',
    scopedSlots: { customRender: 'serial' },
    width: '50px',
    align: 'center',
    dataIndex: 'id',
    type: 'hidden',
    isSearch: false,
    isForm: true
  },
  {
    title: '分组',
    dataIndex: 'apiGroup',
    align: 'center',
    width: '50px',
    type: 'input',
    value: '',
    editable: true,
    isSearch: true,
    isForm: true
  },
  {
    title: '路径',
    dataIndex: 'path',
    width: '150px',
    align: 'left',
    type: 'input',
    value: '',
    editable: true,
    isSearch: true,
    isForm: true
  },
  {
    title: '方法',
    dataIndex: 'method',
    width: '150px',
    align: 'center',
    type: 'input',
    value: '',
    editable: true,
    isSearch: true,
    isForm: true
  },
  {
    title: '中文描述',
    dataIndex: 'description',
    width: '150px',
    align: 'center',
    type: 'input',
    value: '',
    editable: true,
    isSearch: true,
    isForm: true
  },
  {
    title: '创建时间',
    dataIndex: 'createdAt',
    width: '150px',
    align: 'center',
    isSearch: true,
    type: 'date-picker'
  },
  {
    title: '操作',
    width: '200px',
    dataIndex: 'action',
    scopedSlots: { customRender: 'action' },
    align: 'center',
    fixed: 'right'
  }
]
