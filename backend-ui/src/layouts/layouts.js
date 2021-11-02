const files = require.context('@/modules', true, /layouts\.js$/)
import _ from 'lodash'
const layouts = {}

files.keys().forEach(key => {
  _.merge(layouts, files(key).default)
})

export default layouts
