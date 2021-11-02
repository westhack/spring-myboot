// eslint-disable-next-line import/no-extraneous-dependencies
const path = require('path')
const fs = require('fs-extra')
const chalk = require('chalk')
const { came } = require('./util')

// 验证是否是插件
function isModule (source) {
  let result = true
  if (!fs.lstatSync(source).isDirectory()) {
    return false
  }
  const routerPath = path.resolve(source, './router.config.js')
  const packagePath = path.resolve(source, './package.json')
  if (result && !fs.existsSync(routerPath)) {
    result = false
  }
  if (result && !fs.existsSync(packagePath)) {
    result = false
  }
  if (!result) {
    console.log(chalk.yellow(`${source} 不符合 myboot 模块规范`))
  }

  return result
}

function getModules (source) {
  if (!fs.existsSync(source)) {
    console.log(chalk.yellow(`目录不存在: ${source}`))
    return []
  }
  const folders = fs.readdirSync(source)
  const modulesList = []

  folders.forEach(item => {
    const itemPath = path.join(source, item)
    if (!isModule(itemPath)) {
      return
    }
    const config = {}
    config.name = item
    config.camelCaseName = came(item)
    config.path = path.resolve(__dirname, `../src/modules/${item}/`)
    config.packageCtx = JSON.parse(fs.readFileSync(path.resolve(itemPath, './package.json'), 'utf8'))
    modulesList.push(config)
  })

  return modulesList
}

module.exports = getModules
