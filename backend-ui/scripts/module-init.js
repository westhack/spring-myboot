// 手动添加完模块后执行此脚本进行初始化动作
const fs = require('fs-extra')
// eslint-disable-next-line import/no-extraneous-dependencies
const path = require('path')
const chalk = require('chalk')
const shell = require('shelljs')
const inquirer = require('inquirer')
const getAllModule = require('./lib/module-get-all')
const semverValidate = require('./lib/semver-validate')
const installDep = require('./lib/install-dep')

const projectPackage = require('../package.json')

const modulesPath = path.resolve(__dirname, '../src/modules')
// 检测是否有模块文件夹
if (!fs.existsSync(modulesPath)) {
  console.log(chalk.red('未找到模块文件夹目录, 请确认 src 文件夹中是否有 modules 目录'))
  process.exit(1)
}

const moduleList = getAllModule(modulesPath)
console.log(moduleList)
// 将数组 forEach 异步化
async function asyncForEach (array, callback) {
  for (let index = 0; index < array.length; index++) {
    // eslint-disable-next-line
    await callback(array[index], index, array)
  }
}

// 监测 yarn 是否已安装
if (!shell.which('yarn')) {
  console.log(chalk.red('检测到未安装 yarn, 请先安装 yarn 再重新执行, 查看: https://classic.yarnpkg.com/zh-Hans/docs/install/'))
  process.exit(1)
}

async function handler () {
  const questions = [
    {
      type: 'checkbox',
      name: 'modules',
      choices: moduleList.map(item => ({ name: item.name, value: item })),
      message: '请选择需要初始化的模块\n'
    }
  ]

  const { modules } = await inquirer.prompt(questions)
  console.log('选择的', modules)
  if (modules.length === 0) {
    console.log('未选择需要初始化的模块')
    return
  }

  console.log(chalk.green(`开始初始化模块 ${modules.map(item => item.name).join(', ')}`))

  await asyncForEach(modules, async ({ name, packageCtx }) => {
    console.log(`* 模块 ${name}`)
    const keys = ['dependencies', 'devDependencies']
    let hasError = false

    await asyncForEach(keys, async key => {
      await asyncForEach(Object.keys(packageCtx[key]), async pkg => {
        const v1 = packageCtx[key][pkg]
        const v2 = projectPackage[key][pkg]
        if (v1 && v2) {
          if (!semverValidate(v1, v2)) {
            return
          }
        }
        try {
          await installDep(pkg, v1, projectPackage, key === 'devDependencies')
        } catch (e) {
          hasError = true
          console.log(chalk.red(e.message))
        }
        // const result = await installDep(pkg, v1, projectPackage, (key === 'devDependencies'))
        // if (result instanceof Error) {
        //   hasError = true
        //   console.log(chalk.red(result.message))
        // }
      })
    })

    if (hasError) {
      console.log(chalk.yellow('模块初始化结束, 但存在问题, 请手动解决'))
    }
  })
}

handler().then((res) => {
  // eslint-disable-next-line
 // require('./module-get-config')
  console.log(chalk.green('模块初始化结束'))
})
