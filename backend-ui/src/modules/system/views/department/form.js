
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
  'parentId': {
    name: 'parentId',
    label: '上级部门',
    type: 'tree-select',
    value: null,
    rules: [],
    method: 'post',
    url: '/v1/system/department/getAll',
    multiple: false,
    placeholder: '上级部门',
    treeCheckable: false,
    treeDefaultExpandAll: true,
    checkStrictly: true,
    labelName: 'title',
    valueName: 'id',
    parentName: 'parentId',
    parent: 0,
    idName: 'id',
    options: [],
    valueType: 'int',
    valueKey: 'id',
    pushParent: {
      'key': 0,
      'value': 0,
      'title': '顶级',
      'label': '顶级',
      'children': []
    }
  },
  'mainHeader': {
    name: 'mainHeader',
    label: '主要负责人',
    type: 'select',
    value: [],
    rules: [],
    method: 'get',
    url: '/v1/system/admin/getAll',
    multiple: true,
    labelName: 'username',
    valueName: 'id',
    options: [],
    valueType: 'arrayInt'
  },
  'viceHeader': {
    name: 'viceHeader',
    label: '次要负责人',
    type: 'select',
    value: [],
    rules: [],
    method: 'get',
    url: '/v1/system/admin/getAll',
    multiple: true,
    labelName: 'username',
    valueName: 'id',
    options: [],
    valueType: 'arrayInt'
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
