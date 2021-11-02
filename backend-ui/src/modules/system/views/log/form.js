export const defaultSearchFormData = [
  {
    name: 'ip',
    label: '请求IP',
    type: 'input',
    value: '',
    operator: '='
  },
  {
    name: 'method',
    label: '方法',
    type: 'select',
    value: '',
    operator: '=',
    options: [
      'GET',
      'POST',
      'DELETE',
      'PUT'
    ]
  },
  {
    name: 'requestUrl',
    label: '请求路径',
    type: 'input',
    value: '',
    operator: '='
  },
  {
    name: 'status',
    label: '请求状态',
    type: 'input-number',
    value: null,
    operator: '='
  },
  {
    name: 'userId',
    label: '用户ID',
    type: 'input-number',
    value: null,
    operator: '='
  }
]

export const defaultFormData = {}

export const columns = [
  {
    title: '#',
    scopedSlots: { customRender: 'serial' },
    width: '50px',
    align: 'center',
    dataIndex: 'id',
    type: 'hidden'
  },
  {
    title: '请求IP',
    dataIndex: 'ip',
    align: 'center',
    width: '100px',
    type: 'input',
    value: ''
  },
  {
    title: '路径',
    dataIndex: 'requestUrl',
    width: '150px',
    align: 'center',
    type: 'input',
    value: ''
  },
  {
    title: '方法',
    dataIndex: 'method',
    width: '80px',
    align: 'center',
    type: 'input',
    value: ''
  },
  {
    title: '请求状态',
    dataIndex: 'status',
    width: '80px',
    align: 'center',
    type: 'input',
    value: ''
  },
  {
    title: '请求用户Id',
    dataIndex: 'userId',
    width: '150px',
    align: 'center',
    type: 'input',
    value: ''
  },
  {
    title: '延迟',
    dataIndex: 'latency',
    width: '150px',
    align: 'center',
    type: 'input',
    value: ''
  },
  {
    title: '代理',
    dataIndex: 'agent',
    width: '150px',
    align: 'center',
    type: 'input',
    value: ''
  },
  {
    title: '请求内容',
    dataIndex: 'body',
    width: '150px',
    align: 'center',
    type: 'input',
    value: ''
  },
  {
    title: '响应内容',
    dataIndex: 'resp',
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
