export const httpResponseCode = {
  SUCCESS: 200,
  ERROR: 500,
  AUTHENTICATED: 400, // 请求错误
  UNAUTHORIZED: 401, // 未登录
  UNAUTHENTICATED: 403, // 未授权
  NOT_FOUND: 404, // 内容不存在
  METHOD_NOT_ALLOWED: 405 // 方法不允许
}
