const router = {
  UserList: () => import('@/modules/system/views/user/Index'),
  AdminList: () => import('@/modules/system/views/admin/Index'),
  AccountBaseSettings: () => import('@/modules/system/views/account/settings/BaseSettings'),
  AccountPassword: () => import('@/modules/system/views/account/settings/Password'),
  AccountIndex: () => import('@/modules/system/views/account/settings/Index'),
  PermissionList: () => import('@/modules/system/views/permission/Index'),
  RoleList: () => import('@/modules/system/views/role/Index'),
  ApiList: () => import('@/modules/system/views/api/Index')
}

export default router
