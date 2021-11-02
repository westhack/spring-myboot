
export const defaultFormData = {
  'id': {
    name: 'id',
    label: '',
    type: 'hidden',
    value: null,
    rules: []
  },
  'title': {
    name: 'title',
    label: '名称',
    type: 'input',
    value: '',
    change: null,
    rules: [{ required: true, message: '不能为空' }]
  },
  'name': {
    name: 'name',
    label: '角色名',
    type: 'input',
    value: '',
    rules: [{ required: true, message: '不能为空' }]
  },
  'parentId': {
    name: 'parentId',
    label: '上级角色',
    type: 'tree-select',
    value: 0,
    hidden: true,
    valueType: 'int',
    rules: [{ required: true, message: '不能为空' }]
  },
  'defaultRole': {
    name: 'defaultRole',
    label: '是否为注册默认角色',
    type: 'switch',
    value: false,
    rules: []
  },
  'permissions': {
    name: 'permissions',
    label: '菜单权限',
    type: 'tree',
    value: [],
    rules: [],
    multiple: true,
    placeholder: '角色所属菜单权限',
    treeCheckable: true,
    treeDefaultExpandAll: true,
    checkStrictly: true,
    labelName: 'title',
    valueName: 'id',
    parentName: 'parentId',
    idName: 'id',
    options: [],
    valueType: 'arrayInt'
  },
  'departments': {
    name: 'departments',
    label: '数据权限',
    type: 'tree',
    value: [],
    rules: [],
    method: 'post',
    url: '/v1/system/department/getAll',
    multiple: true,
    placeholder: '角色所属数据权限',
    treeCheckable: true,
    treeDefaultExpandAll: true,
    checkStrictly: true,
    labelName: 'title',
    valueName: 'id',
    parentName: 'parentId',
    idName: 'id',
    options: [],
    valueType: 'arrayInt',
    valueKey: 'id',
    parent: 0
  },
  'description': {
    name: 'description',
    label: '说明',
    type: 'textarea',
    value: '',
    rules: []
  }
}
