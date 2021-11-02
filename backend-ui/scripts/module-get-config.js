const fs = require('fs-extra')
// eslint-disable-next-line import/no-extraneous-dependencies
const path = require('path')
const chalk = require('chalk')
const ejs = require('ejs')
const getAllPlugin = require('./lib/module-get-all')

const targetDir = path.resolve(__dirname, '../src/router/module.router.config.js')
const modulesPath = path.resolve(__dirname, '../src/modules')
const templatePath = path.resolve(__dirname, './template/module-router-config.js.ejs')

// eslint-disable-next-line
console.log(chalk.green('配置模块...'))

const template = fs.readFileSync(templatePath, 'utf8')
const moduleList = getAllPlugin(modulesPath)
const result = ejs.render(template, { modules: moduleList })
console.log(result)
// fs.writeFile(targetDir, result)

// eslint-disable-next-line
console.log(chalk.green(`模块配置完成: ${targetDir}\n`))
