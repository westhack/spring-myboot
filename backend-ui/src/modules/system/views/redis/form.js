export const defaultSearchFormData = [
  {
    name: 'key',
    label: '缓存key',
    type: 'input',
    value: null,
    operator: '='
  }
]

export const defaultFormData = {
  'key': {
    name: 'key',
    label: '键',
    type: 'input',
    value: '',
    rules: []
  },
  'value': {
    name: 'value',
    label: '值',
    type: 'input',
    value: '',
    rules: []
  },
  'expireTime': {
    name: 'expireTime',
    label: '缓存时间（秒）',
    type: 'input-number',
    value: '',
    rules: [],
    extra: ''
  }
}

export const columns = [
  {
    title: '#',
    scopedSlots: { customRender: 'serial' },
    width: '50px',
    align: 'center',
    dataIndex: 'no',
    type: 'hidden',
    isSearch: false,
    isForm: true
  },
  {
    title: '健',
    dataIndex: 'key',
    align: 'left',
    width: '600px',
    type: 'input',
    value: '',
    editable: true,
    isSearch: true,
    isForm: true,
    ellipsis: true
  },
  {
    title: '值',
    dataIndex: 'value',
    width: '600px',
    align: 'left',
    type: 'input',
    value: '',
    editable: true,
    isSearch: true,
    isForm: true,
    ellipsis: true
  },
  {
    title: '缓存时间',
    dataIndex: 'expireTime',
    width: '80px',
    align: 'center',
    type: 'input-number',
    value: '',
    editable: true,
    isSearch: true,
    isForm: true,
    ellipsis: true
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
