export const defaultSearchFormData = [
  {
    name: 'username',
    label: '账号',
    type: 'input',
    value: '',
    operator: '='
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
    value: '',
    operator: '=',
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
    ]
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
    label: '账号',
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
    type: 'input',
    value: '',
    rules: [],
    extra: '不修改请留空'
  },
  'roles': {
    name: 'roles',
    label: '角色',
    type: 'select',
    value: [],
    rules: [],
    multiple: true,
    options: [],
    placeholder: '所属角色',
    valueName: 'id',
    labelName: 'title',
    valueKey: 'id',
    maxTagCount: 100
  },
  'departmentId': {
    name: 'departmentId',
    label: '部门',
    type: 'tree-select',
    method: 'post',
    url: '/v1/system/department/getAll',
    value: null,
    rules: [],
    multiple: false,
    options: [],
    placeholder: '所属部门',
    valueName: 'id',
    labelName: 'title',
    valueKey: 'id',
    parent: 0
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
    width: '100px',
    align: 'right',
    dataIndex: 'id'
  },
  {
    title: '头像',
    scopedSlots: { customRender: 'image' },
    dataIndex: 'avatar',
    align: 'center',
    width: '50px'
  },
  {
    title: '账号',
    dataIndex: 'username',
    width: '150px',
    align: 'center'
  },
  {
    title: '昵称',
    dataIndex: 'nickname',
    width: '150px',
    align: 'center'
  },
  {
    title: '手机号',
    dataIndex: 'mobile',
    width: '120px',
    align: 'center'
  },
  {
    title: '状态',
    dataIndex: 'status',
    scopedSlots: { customRender: 'status' },
    width: '70px',
    align: 'center'
  },
  {
    title: '角色',
    dataIndex: 'roles',
    scopedSlots: { customRender: 'roles' },
    ellipsis: true
  },
  {
    title: '注册时间',
    dataIndex: 'createdAt',
    width: '150px',
    align: 'center',
    ellipsis: true,
    sorter: true
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
