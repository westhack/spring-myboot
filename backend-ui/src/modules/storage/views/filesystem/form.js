import context from '@/main'

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
    title: '编号',
    dataIndex: 'uuid',
    width: '150px',
    align: 'center',
    type: 'input',
    value: '',
    editable: true,
    isSearch: true,
    isForm: true
  },
  {
    title: '存储类型',
    dataIndex: 'type',
    align: 'center',
    width: '100px',
    type: 'select',
    options: context.$store.getters.dict['upload_type'],
    value: '',
    editable: true,
    isSearch: true,
    isForm: true,
    hiddenPopover: true
  },
  {
    title: '文件名',
    dataIndex: 'name',
    align: 'center',
    width: '100px',
    type: 'input',
    value: '',
    editable: true,
    isSearch: true,
    isForm: true
  },
  {
    title: '文件地址',
    dataIndex: 'url',
    width: '300px',
    align: 'center',
    type: 'input',
    value: '',
    editable: true,
    isSearch: true,
    isForm: true
  },
  {
    title: '文件标签',
    dataIndex: 'tag',
    width: '80px',
    align: 'center',
    type: 'input',
    value: '',
    editable: true,
    isSearch: true,
    isForm: true,
    hiddenPopover: true
  },
  {
    title: '上传时间',
    dataIndex: 'createdAt',
    width: '150px',
    align: 'center',
    isSearch: true,
    type: 'range-picker',
    hiddenPopover: true
  },
  {
    title: '操作',
    width: '150px',
    dataIndex: 'action',
    scopedSlots: { customRender: 'action' },
    align: 'center',
    fixed: 'right'
  }
]
