const fs = require('fs-extra')
// eslint-disable-next-line import/no-extraneous-dependencies
const path = require('path')
const inquirer = require('inquirer')
const ejs = require('ejs')
const chalk = require('chalk')
const yaml = require('js-yaml')
const dirTree = require('directory-tree')
const validatePName = require('validate-npm-package-name')
const semver = require('semver')
// const came = require('./lib/util')

const came = str => `${str}`.replace(/-\D/g, match => match.charAt(1).toUpperCase())

const questions = []

questions.push({
  type: 'input',
  name: 'name',
  message: '请输入模块名, 多个单词以 "-" 连接, 如: user-permission\n',
  validate (value) {
    const done = this.async()
    setTimeout(() => {
      if (!value) {
        done('不可为空')
        return
      }

      const regex = /^[a-z][a-z0-9]*(-[a-z][a-z0-9]*)*/

      if (!regex.test(value)) {
        done('检测到格式错误, 多个单词以 "-" 连接, 如: user-permission')
        return
      }

      const filePath = path.resolve(__dirname, `../src/modules/${value}`)
      if (fs.existsSync(filePath)) {
        done('项目中已存在该模块, 请更换其他模块名')
        return
      }

      const v = validatePName(`myboot-module-${value}`)
      if (!v.validForNewPackages) {
        done(v.errors.join(', '))
        return
      }
      done(null, true)
    })
  }
})

questions.push({
  type: 'input',
  name: 'title',
  message: '请输入模块标题, 7个字内\n',
  default: '模块标题'
})

questions.push({
  type: 'input',
  name: 'version',
  message: '请输入版本号\n',
  default: '1.0.0',
  validate (value) {
    const done = this.async()
    if (!semver.valid(value)) {
      done('版本号不符合规范')
      return
    }
    done(null, true)
  }
})

questions.push({
  type: 'input',
  name: 'description',
  message: '简述模块处理的业务场景\n',
  default: ''
})

questions.push({
  type: 'input',
  name: 'author',
  message: '请输入作者\n',
  default: ''
})

const cachePath = path.resolve(__dirname, './.cache')
const cacheModulePath = path.resolve(__dirname, './.cache/modules')
const moduleTmpPath = path.resolve(__dirname, './template/module')
const moduleViewsPath = path.resolve(__dirname, './template/module/view')
const moduleStrPos = __dirname.length + '/template/'.length
const modulesPath = path.resolve(__dirname, '../src/modules')

// 检测是否有模块文件夹
if (!fs.existsSync(modulesPath)) {
  fs.mkdirSync(modulesPath)
}

inquirer
  .prompt(questions)
  .then(answers => {
    const result = answers
    result.camelCaseName = came(result.name)
    return result
  })
  .then(answers => {
    const config = { ...answers }

    // 创建缓存文件夹 .cache
    if (!fs.existsSync(cachePath)) {
      fs.mkdirSync(cachePath)
    }
    // 清空 module 文件夹
    if (fs.existsSync(cacheModulePath)) {
      fs.removeSync(cacheModulePath)
    }
    fs.mkdirSync(cacheModulePath)

    dirTree(moduleTmpPath, {}, item => {
      // 忽略隐藏文件
      if (item.extension === '' || item.name[0] === '.') {
        return
      }
      // 处理模板文件
      if (item.extension === '.ejs') {
        const template = fs.readFileSync(item.path, 'utf8')
        const fileConfig = { ...config }
        // 舞台 view 文件配置处理
        if (item.path.slice(moduleStrPos).split(path.sep)[1] === 'view' && item.name.slice(-8) === '.vue.ejs') {
          const viewConfig = {}
          viewConfig.icon = 'iconfont icon-demo'
          viewConfig.name = fileConfig.camelCaseName + item.name.slice(0, -8)
          viewConfig.route = path
            .join(config.name, path.relative(moduleViewsPath, item.path))
            .split(path.sep)
            .join('/')
          viewConfig.route = `/${viewConfig.route.slice(0, -8)}`
          viewConfig.order = null
          viewConfig.inNav = true
          viewConfig.title = '舞台页'
          viewConfig.type = 'view'
          viewConfig.auths = {
            role: null,
            permission: null
          }
          viewConfig.needLogin = true
          fileConfig.configYml = yaml.safeDump(viewConfig)
        }
        const result = ejs.render(template, fileConfig)
        const targetPath1 = path.resolve(cacheModulePath, path.relative(moduleTmpPath, item.path).slice(0, -4))
        fs.outputFileSync(targetPath1, result)
        return
      }
      // 拷贝其他文件
      const targetPath1 = path.resolve(cacheModulePath, path.relative(moduleTmpPath, item.path))
      fs.copySync(item.path, targetPath1)
    })

    return config
  })
  .then(answers => {
    // 复制 .cache 到 module
    const sourcePath = path.resolve(__dirname, './.cache/modules')
    const targetPath = path.resolve(__dirname, `../src/modules/${answers.name}`)
    fs.copySync(sourcePath, targetPath)

    console.log(chalk.green(`创建模块 ${answers.name}: ${targetPath}`))
    // eslint-disable-next-line
  })
  .then(() => {
    // eslint-disable-next-line
    // require('./module-get-config.js')
    console.log(chalk.green(`模块配置完成\n`))
    // eslint-disable-next-line
  })
  .catch(err => {
    // eslint-disable-next-line
    console.log(chalk.red('创建模块失败'))
    console.error(err)
    process.exit(1)
  })
