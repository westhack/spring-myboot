
export const defaultFormData = {
  'id': {
    name: 'id',
    label: '',
    type: 'hidden',
    value: null,
    rules: []
  },
  'parentId': {
    name: 'parentId',
    label: '上级菜单',
    type: 'tree-select',
    value: null,
    options: [],
    showSearch: true,
    labelName: 'title',
    valueName: 'id',
    valueType: 'int',
    rules: [{ required: true, message: '不能为空' }]
  },
  'title': {
    name: 'title',
    label: '标题',
    type: 'input',
    value: '',
    rules: [{ required: true, message: '不能为空' }],
    extra: '用于菜单标题'
  },
  'name': {
    name: 'name',
    label: '名称',
    type: 'input',
    value: '',
    rules: [{ required: true, message: '不能为空' }],
    extra: '英文名称，唯一'
  },
  'icon': {
    name: 'icon',
    label: '图标',
    type: 'input',
    value: ''
  },

  'isButton': {
    name: 'isButton',
    label: '是否是按钮',
    type: 'switch',
    value: false,
    hidden: true,
    rules: [],
    extra: '不是按钮，必须输入前段组件'
  },

  'component': {
    name: 'component',
    label: '前端组件',
    type: 'auto-complete',
    value: '',
    options: [],
    showSearch: true,
    allowClear: true,
    rules: [{ required: false, message: '不能为空' }],
    extra: '要显示的前端组件， 如：modules/system/views/user/Index.vue'
  },

  'hidden': {
    name: 'hidden',
    label: '是否隐藏',
    type: 'switch',
    value: false,
    rules: [],
    extra: '隐藏后不会左侧菜单显示'
  },
  'path': {
    name: 'path',
    label: '前端路由',
    type: 'input',
    value: '',
    rules: [{ required: false, message: '不能为空' }],
    extra: '前端路由，支持正则'
  },
  'defaultPath': {
    name: 'defaultPath',
    label: '前端路由默认路由',
    type: 'input',
    value: '',
    rules: [{ required: false, message: '不能为空' }],
    extra: '如果前端路由是正则，请填写一个默认路由'
  },
  'redirect': {
    name: 'redirect',
    label: '前端跳转路由',
    type: 'input',
    value: '',
    rules: [],
    extra: '前端路由默认跳转路由'
  },

  'api': {
    name: 'api',
    label: '后端 API 权限路由',
    type: 'tags',
    value: [],
    maxTagCount: 10,
    showSearch: true,
    allowClear: true,
    // labelName: 'description',
    // valueName: 'path',
    valueType: 'string',
    rules: [{ required: false, message: '不能为空' }],
    extra: '关联后端 API 接口权限路由-可多选'
  },

  'sortOrder': {
    name: 'sortOrder',
    label: '排序',
    type: 'input-number',
    value: 99999,
    placeholder: '越小越靠前',
    rules: []
  },
  'keepAlive': {
    name: 'keepAlive',
    label: '前端组件持久化',
    type: 'switch',
    value: true,
    rules: []
  },

  'status': {
    name: 'status',
    label: '状态',
    type: 'switch',
    value: true,
    rules: []
  },

  'buttons': {
    name: 'buttons',
    label: '快捷添加按钮',
    type: 'checkbox',
    data: [],
    value: [],
    rules: []
  }
}

export const defaultButtonFormData = {
  'id': {
    name: 'id',
    label: '',
    type: 'hidden',
    value: '',
    rules: []
  },
  'parentId': {
    name: 'parentId',
    label: '上级菜单',
    type: 'hidden',
    value: '',
    options: [],
    showSearch: true,
    rules: [{ required: true, message: '不能为空' }]
  },
  'title': {
    name: 'title',
    label: '标题',
    type: 'input',
    value: '',
    rules: [{ required: true, message: '不能为空' }],
    extra: '用于菜单标题'
  },
  'name': {
    name: 'name',
    label: '名称',
    type: 'input',
    value: '',
    rules: [{ required: true, message: '不能为空' }],
    extra: '英文名称，唯一'
  },
  'isButton': {
    name: 'isButton',
    label: '是否是按钮',
    type: 'switch',
    hidden: true,
    value: true,
    rules: [],
    extra: '不是按钮，必须输入前段组件'
  },
  'api': {
    name: 'api',
    label: '后端 API 权限',
    type: 'tags',
    value: [],
    mode: 'multiple',
    maxTagCount: 10,
    showSearch: true,
    allowClear: true,
    // labelName: 'description',
    // valueName: 'path',
    valueType: 'array',
    rules: [{ required: false, message: '不能为空' }],
    extra: '关联后端 API 接口权限路由-可多选'
  },

  'status': {
    name: 'status',
    label: '状态',
    type: 'switch',
    value: true,
    rules: []
  },

  'sortOrder': {
    name: 'sortOrder',
    label: '排序',
    type: 'input-number',
    value: 99999,
    placeholder: '越小越靠前',
    rules: []
  }
}
