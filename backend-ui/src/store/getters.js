const getters = {
  lang: state => state.app.lang,
  language: state => state.app.language,
  device: state => state.app.device,
  theme: state => state.app.theme,
  color: state => state.app.color,
  token: state => state.user.token,
  avatar: state => state.user.avatar,
  nickname: state => state.user.name,
  welcome: state => state.user.welcome,
  roles: state => state.user.roles,
  permissions: state => state.user.permissions,
  userInfo: state => state.user.info,
  addRouters: state => state.permission.addRouters,
  multiTab: state => state.app.multiTab,
  dict: state => state.core.dict,
  shop: state => state.user.shop,
  messages: state => state.user.messages,
  messageCount: state => state.user.messageCount
}

export default getters
