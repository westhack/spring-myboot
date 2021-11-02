import _ from 'lodash'
import moment from 'moment'
import LTT from 'list-to-tree'

export function timeFix () {
  const time = new Date()
  const hour = time.getHours()
  return hour < 9 ? '早上好' : hour <= 11 ? '上午好' : hour <= 13 ? '中午好' : hour < 20 ? '下午好' : '晚上好'
}

export function welcome () {
  const arr = ['休息一会儿吧', '准备吃什么呢?', '要不要打一把 DOTA', '我猜你可能累了']
  const index = Math.floor(Math.random() * arr.length)
  return arr[index]
}

/**
 * 触发 window.resize
 */
export function triggerWindowResizeEvent () {
  const event = document.createEvent('HTMLEvents')
  event.initEvent('resize', true, true)
  event.eventType = 'message'
  window.dispatchEvent(event)
}

export function handleScrollHeader (callback) {
  let timer = 0

  let beforeScrollTop = window.pageYOffset
  callback = callback || function () {}
  window.addEventListener(
    'scroll',
    event => {
      clearTimeout(timer)
      timer = setTimeout(() => {
        let direction = 'up'
        const afterScrollTop = window.pageYOffset
        const delta = afterScrollTop - beforeScrollTop
        if (delta === 0) {
          return false
        }
        direction = delta > 0 ? 'down' : 'up'
        callback(direction)
        beforeScrollTop = afterScrollTop
      }, 50)
    },
    false
  )
}

/**
 * Remove loading animate
 * @param id parent element id or class
 * @param timeout
 */
export function removeLoadingAnimate (id = '', timeout = 1500) {
  if (id === '') {
    return
  }
  setTimeout(() => {
    document.body.removeChild(document.getElementById(id))
  }, timeout)
}

/**
 * This is just a simple version of deep copy
 * Has a lot of edge cases bug
 * If you want to use a perfect deep copy, use lodash's _.cloneDeep
 * @param {Object} source
 * @returns {Object}
 */
export function deepClone (source) {
  if (!source && typeof source !== 'object') {
    throw new Error('error arguments', 'deepClone')
  }
  const targetObj = source.constructor === Array ? [] : {}
  Object.keys(source).forEach(keys => {
    if (source[keys] && typeof source[keys] === 'object') {
      targetObj[keys] = deepClone(source[keys])
    } else {
      targetObj[keys] = source[keys]
    }
  })
  return targetObj
}

/**
 *
 * @param img
 * @param callback
 */
export function getFileBase64 (img, callback) {
  const reader = new FileReader()
  reader.addEventListener('load', () => callback(reader.result))
  reader.readAsDataURL(img)
}

export function fileReader (file, options) {
  options = options || {}
  return new Promise(function (resolve, reject) {
    const reader = new FileReader()

    reader.onload = function () {
      resolve(reader)
    }
    reader.onerror = reject

    if (options.accept && !new RegExp(options.accept).test(file.type)) {
      // eslint-disable-next-line prefer-promise-reject-errors
      reject({
        code: 1,
        msg: 'wrong file type'
      })
    }

    if (!file.type || /^text\//i.test(file.type)) {
      reader.readAsText(file)
    } else {
      reader.readAsDataURL(file)
    }
  })
}

export function setFormValue (items, values) {
  // if (_.isArray(items)) {
  _.each(items, (item, name) => {
    if (values[item['name']] !== undefined) {
      let value = values[item['name']]

      try {
        if (items[name]['path'] !== undefined && items[name]['path'] !== null) {
          const path = items[name]['path']
          const paths = path.split('.')
          paths.map(res => {
            value = value[res]
          })
        }
      } catch (e) {
      }

      if (
        items[name]['type'] === 'date-picker' ||
          items[name]['type'] === 'week-picker' ||
          items[name]['type'] === 'month-picker'
      ) {
        if (value != null) {
          items[name]['value'] = moment(value)
        } else {
          items[name]['value'] = null
        }
      } else if (items[name]['type'] === 'range-picker') {
        if (_.isArray(value) && value.length === 2) {
          items[name]['value'] = [
            moment(value[0]),
            moment(value[1])
          ]
        }
      } else if (items[name]['type'] === 'time-picker') {
        if (value != null) {
          items[name]['value'] = moment(value, items[name]['format'] ? items[name]['format'] : 'hh:mm:ss')
        } else {
          items[name]['value'] = null
        }
      } else if (items[name]['type'] === 'tree') {
        if (_.isArray(value)) {
          const ret = []
          _.each(value, (val) => {
            const valueKey = items[name]['valueKey'] || null
            if (_.isObject(val) && valueKey != null) {
              ret.push(_.get(val, valueKey) + '')
            } else {
              ret.push(val)
            }
          })
          value = ret
          items[name]['value'] = value
        }
      } else {
        items[name]['value'] = value
      }
    }
  })
  // } else {
  //   _.each(values, (value, name) => {
  //     try {
  //       if (items[name]['path'] !== undefined && items[name]['path'] !== null) {
  //         const path = items[name]['path']
  //         const paths = path.split('.')
  //         paths.map(res => {
  //           value = value[res]
  //         })
  //       }
  //     } catch (e) {
  //     }
  //
  //     if (items[name] !== undefined) {
  //       if (
  //         items[name]['type'] === 'date-picker' ||
  //         items[name]['type'] === 'week-picker' ||
  //         items[name]['type'] === 'month-picker'
  //       ) {
  //         if (value != null) {
  //           items[name]['value'] = moment(value)
  //         } else {
  //           items[name]['value'] = null
  //         }
  //       } else if (items[name]['type'] === 'range-picker') {
  //         if (_.isArray(value) && value.length === 2) {
  //           items[name]['value'] = [
  //             moment(value[0]),
  //             moment(value[1])
  //           ]
  //         }
  //       } else if (items[name]['type'] === 'time-picker') {
  //         if (value != null) {
  //           items[name]['value'] = moment(value, items[name]['format'] ? items[name]['format'] : 'hh:mm:ss')
  //         } else {
  //           items[name]['value'] = null
  //         }
  //       } else if (items[name]['type'] === 'tree') {
  //         if (_.isArray(value)) {
  //           const ret = []
  //           _.each(value, (val) => {
  //             const valueKey = items[name]['valueKey'] || null
  //             if (_.isObject(val) && valueKey != null) {
  //               ret.push(_.get(val, valueKey))
  //             } else {
  //               ret.push(val)
  //             }
  //           })
  //
  //           value = ret
  //         }
  //       } else {
  //         items[name]['value'] = value
  //       }
  //     }
  //   })
  // }

  return items
}

export function treeToList (tree, index = null, children = 'children') {
  const dataList = []
  const generateList = (data) => {
    for (let i = 0; i < data.length; i++) {
      const node = data[i]
      const id = node[index]
      if (index) {
        dataList[id] = node
      } else {
        dataList.push(node)
      }
      if (node[children]) {
        generateList(node[children], node[index])
      }
    }
  }

  generateList(tree)

  return dataList
}

export function treeParseData (val, key, title) {
  const _parseData = (tree) => {
    for (let i = 0; i < tree.length; i++) {
      tree[i]['key'] = tree[i].value = tree[i][key] + ''
      tree[i]['title'] = tree[i][title]
      tree[i]['scopedSlots'] = { title: 'title' }
      if (tree[i].children && tree[i].children.length > 0) {
        _parseData(tree[i].children)
      }
    }
    return tree
  }

  return _parseData(val)
}

export function listToTree (list, keyId, keyParent) {
  const ltt = new LTT(list, {
    key_id: (keyId || 'id'),
    key_parent: (keyParent || 'parentId'),
    key_child: 'children'
  })
  const tree = ltt.GetTree()

  return tree
}

export function listToTree2 (data, keyId, keyParent, parent) {
  const parents = []

  if (parent == undefined) {
    parent = null
  }

  const list = _.cloneDeep(data)

  list.forEach(item => {
    item.children = []

    if (parent == item[keyParent]) {
      parents.push(item)
    }
  })

  if (parents.length == 0) {
    if (parent == null) {
      list.forEach(item => {
        item.children = []

        if (item[keyParent] == 0) {
          parents.push(item)
        }
      })
    }
  }

  const getChildren = (p, objs) => {
    const children = []
    _.each(objs, (obj, k) => {
      if (p[keyId] == obj[keyParent]) {
        obj['children'] = getChildren(obj, objs)
        children.push(obj)
      }
    })
    return children
  }

  _.each(parents, (item, i) => {
    const c = getChildren(item, list)
    parents[i]['children'] = c
  })

  return parents
}

export const randomRgb = () => {
  const R = Math.floor(Math.random() * 130 + 110)
  const G = Math.floor(Math.random() * 130 + 110)
  const B = Math.floor(Math.random() * 130 + 110)
  return {
    background: 'rgb(' + R + ',' + G + ',' + B + ')'
  }
}

export const colorById = (i) => {
  if (i < 10)i = i * 302.3
  if (i < 100)i = i * 31.2
  for (;i > 255; i *= 0.98);
  const temp = i.toString().substring(i.toString().length - 3)
  i += parseInt(temp)
  for (;i > 255; i -= 255);
  i = parseInt(i)
  if (i < 10)i += 10

  let R = i * (i / 100)
  for (;R > 255; R -= 255);
  if (R < 50)R += 60
  R = parseInt(R).toString(16)

  let G = i * (i % 100)
  for (;G > 255; G -= 255);
  if (G < 50)G += 60
  G = parseInt(G).toString(16)

  let B = i * (i % 10)
  for (;B > 255; B -= 255);
  if (B < 50)B += 60
  B = parseInt(B).toString(16)

  console.log(i + ':' + R + ':' + G + ':' + B)
  return '#' + R + G + B
}

export const jsonFormat = (json) => {
  return JSON.stringify(json, null, '\t')
}

export const downloadImage = (imgsrc, name) => { // 下载图片地址和图片名
  const image = new Image()
  image.setAttribute('crossOrigin', 'anonymous')
  image.onload = function () {
    const canvas = document.createElement('canvas')
    canvas.width = image.width
    canvas.height = image.height
    const context = canvas.getContext('2d')
    context.drawImage(image, 0, 0, image.width, image.height)
    const url = canvas.toDataURL('image/png') // 得到图片的base64编码数据

    const a = document.createElement('a') // 生成一个a元素
    const event = new MouseEvent('click') // 创建一个单击事件
    a.download = name || 'photo' // 设置图片名称
    a.href = url // 将生成的URL设置为a.href属性
    a.dispatchEvent(event) // 触发a的单击事件
  }
  image.src = imgsrc
}
