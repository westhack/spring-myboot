export const defaultSearchFormData = [
  {
    name: 'username',
    label: '账号',
    type: 'input',
    value: '',
    operator: '='
  },
  {
    name: 'createdAt',
    label: '注册时间',
    type: 'range-picker',
    value: null,
    operator: 'between'
  },
  {
    name: 'nickname',
    label: '昵称',
    type: 'input',
    value: '',
    operator: '='
  },
  {
    name: 'mobile',
    label: '手机号',
    type: 'input',
    value: '',
    operator: '='
  },
  {
    name: 'status',
    label: '状态',
    type: 'select',
    value: null,
    options: [
      {
        label: '禁止',
        value: 0
      },
      {
        label: '正常',
        value: 1
      },
      {
        label: '待审核',
        value: 2
      }
    ],
    operator: '='
  }
]

export const defaultFormData = {
  'id': {
    name: 'id',
    label: '',
    type: 'hidden',
    value: null,
    rules: []
  },
  'username': {
    name: 'username',
    label: '用户名',
    type: 'input',
    value: '',
    rules: [{ required: true, message: '不能为空' }]
  },
  'mobile': {
    name: 'mobile',
    label: '手机号',
    type: 'input',
    value: '',
    rules: [{ required: true, message: '不能为空' }]
  },
  'email': {
    name: 'email',
    label: '邮箱',
    type: 'input',
    value: '',
    rules: [{ required: true, message: '不能为空' }]
  },
  'password': {
    name: 'password',
    label: '密码',
    type: 'password',
    value: '',
    rules: [],
    extra: '不修改请留空'
  },
  'nickname': {
    name: 'nickname',
    label: '昵称',
    type: 'input',
    value: '',
    rules: []
  },
  'avatar': {
    name: 'avatar',
    label: '头像',
    type: 'image',
    value: '',
    rules: []
  },
  'status': {
    name: 'status',
    label: '状态',
    type: 'radio',
    value: 1,
    options: [
      {
        label: '禁止',
        value: 0
      },
      {
        label: '正常',
        value: 1
      },
      {
        label: '待审核',
        value: 2
      }
    ],
    rules: []
  }
}

export const columns = [
  {
    title: '#',
    scopedSlots: { customRender: 'serial' },
    width: '50px',
    align: 'center',
    dataIndex: 'no'
  },
  {
    title: '用户ID',
    width: '50px',
    align: 'right',
    dataIndex: 'id'
  },
  {
    editable: true,
    title: '头像',
    scopedSlots: { customRender: 'image' },
    dataIndex: 'avatar',
    align: 'center',
    width: '50px'
  },
  {
    editable: true,
    title: '账号',
    dataIndex: 'username',
    scopedSlots: { customRender: 'username' },
    width: '150px',
    align: 'center',
    rules: []
  },
  {
    editable: true,
    title: '手机号',
    dataIndex: 'mobile',
    width: '120px',
    scopedSlots: { customRender: 'mobile' },
    align: 'center',
    rules: []
  },
  {
    editable: true,
    title: '邮箱',
    dataIndex: 'email',
    width: '120px',
    scopedSlots: { customRender: 'email' },
    align: 'center',
    rules: []
  },
  {
    editable: true,
    title: '昵称',
    dataIndex: 'nickname',
    scopedSlots: { customRender: 'nickname' },
    width: '150px',
    align: 'center',
    rules: []
  },
  {
    editable: true,
    title: '注册时间',
    dataIndex: 'createdAt',
    width: '150px',
    align: 'center',
    ellipsis: true,
    scopedSlots: { customRender: 'createdAt' },
    sorter: true,
    rules: []
  },
  {
    editable: true,
    title: '状态',
    dataIndex: 'status',
    scopedSlots: { customRender: 'status' },
    width: '70px',
    align: 'center',
    rules: []
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
