const files = require.context('@/modules', true, /components\.js$/)
const components = {}

files.keys().forEach(key => {
  Object.assign(components, files(key).default)
})

export default components
