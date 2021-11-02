export default (url) => () => import(`@/modules/${url}.vue`)
