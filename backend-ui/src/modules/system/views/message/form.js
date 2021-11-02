import context from '@/main'

export const defaultSearchFormData = [
  {
    name: 'userId',
    label: '用户ID',
    type: 'input',
    value: '',
    operator: '='
  },
  {
    name: 'fromUserId',
    label: '发送用户ID',
    type: 'input',
    value: '',
    operator: '='
  },
  {
    name: 'type',
    label: '消息类型',
    type: 'select',
    value: null,
    operator: '=',
    options: context.$store.getters.dict['messageType']
  },
  {
    name: 'createdAt',
    label: '消息时间',
    type: 'range-picker',
    value: null,
    operator: 'between'
  }
]

export const defaultFormData = [
  {
    name: 'type',
    label: '消息类型',
    type: 'select',
    value: null,
    options: context.$store.getters.dict['messageType'],
    valueType: 'int',
    rules: [{ required: true, message: '不能为空' }]
  },
  {
    type: 'table-select',
    label: '接收用户',
    name: 'userId',
    multiple: true,
    valueType: true,
    value: [],
    tableScroll: {
      x: 300
    },
    columns: [
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
        title: '昵称',
        dataIndex: 'nickname',
        align: 'center',
        width: '100px',
        type: 'input',
        value: '',
        editable: true,
        isSearch: true,
        isForm: true
      },
      {
        title: '用户名',
        dataIndex: 'username',
        align: 'center',
        width: '100px',
        type: 'input',
        value: '',
        editable: true,
        isSearch: true,
        isForm: true
      },
      {
        title: '手机号',
        dataIndex: 'mobile',
        align: 'center',
        width: '100px',
        type: 'input',
        value: '',
        editable: true,
        isSearch: true,
        isForm: true
      }
    ],
    viewColumns: {
      username: {
        label: '用户名',
        name: 'username',
        type: 'input',
        value: ''
      },
      mobile: {
        label: '手机号',
        name: 'mobile',
        type: 'input',
        value: ''
      }
    },
    apiUrl: '/v1/system/user/getList',
    rules: [{ required: true, message: '不能为空' }]
  },
  {
    name: 'formUserId',
    label: '发送用户ID',
    type: 'input-number',
    value: null,
    extra: '0 表示 系统',
    rules: [{ required: true, message: '不能为空' }]
  },
  {
    name: 'icon',
    label: '消息图标',
    type: 'image',
    value: '/assets/images/message.png'
  },
  {
    name: 'image',
    label: '消息缩略图',
    type: 'image',
    value: null
  },
  {
    name: 'title',
    label: '消息标题',
    type: 'input',
    value: null,
    rules: [{ required: true, message: '不能为空' }]
  },
  {
    name: 'content',
    label: '消息内容',
    type: 'textarea',
    value: null,
    rules: [{ required: true, message: '不能为空' }]
  }
]

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
    title: '用户ID',
    dataIndex: 'userId',
    align: 'right',
    width: '100px',
    type: 'input',
    value: '',
    hiddenPopover: true
  },
  {
    title: '用户',
    dataIndex: 'user',
    align: 'center',
    width: '100px',
    value: '',
    valueKey: 'username',
    hiddenPopover: true
  },
  {
    title: '发送用户ID',
    dataIndex: 'fromUserId',
    align: 'right',
    width: '100px',
    type: 'input',
    value: '',
    hiddenPopover: true
  },
  {
    title: '发送用户',
    dataIndex: 'formUser',
    align: 'center',
    width: '100px',
    type: 'input',
    value: '',
    valueKey: 'username',
    hiddenPopover: true
  },
  {
    title: '消息标题',
    dataIndex: 'title',
    width: '150px',
    align: 'center',
    type: 'input',
    value: '',
    hiddenPopover: true
  },
  {
    title: '消息类型',
    dataIndex: 'type',
    width: '100px',
    align: 'center',
    type: 'select',
    value: '',
    options: context.$store.getters.dict['messageType'],
    hiddenPopover: true
  },
  {
    title: '消息内容',
    dataIndex: 'content',
    width: '150px',
    align: 'center',
    type: 'input',
    value: ''
  },
  {
    title: '消息时间',
    dataIndex: 'createdAt',
    width: '150px',
    align: 'center',
    type: 'date-picker',
    hiddenPopover: true
  },
  {
    title: '是否查看',
    dataIndex: 'status',
    width: '150px',
    align: 'center',
    type: 'boolean',
    value: '',
    hiddenPopover: true
  },
  {
    title: '查看时间',
    dataIndex: 'viewTime',
    width: '150px',
    align: 'center',
    type: 'input',
    value: '',
    hiddenPopover: true
  },
  {
    title: '数据Id',
    dataIndex: 'dataId',
    width: '100px',
    align: 'center',
    type: 'input',
    value: '',
    hiddenPopover: true
  },
  {
    title: '数据类型',
    dataIndex: 'dataType',
    width: '160px',
    align: 'center',
    type: 'input',
    value: '',
    hiddenPopover: true
  },
  {
    title: '数据',
    dataIndex: 'data',
    width: '200px',
    align: 'center',
    type: 'input',
    value: ''
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
