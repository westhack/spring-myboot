import _ from 'lodash'

/**
 * 商品属性类型
 * 一个属性个数是不确定的
 */
const Sku = function () {
  this.selectSku = []
  this.result = []
}

Sku.prototype = {
  gen: function (selectSku) {
    this.result = []
    this.selectSku = selectSku
    this.combine(0, {})

    return this.result
  },
  combine: function (index, current) {
    if (index < this.selectSku.length - 1) {
      const skuItem = this.selectSku[index]
      const keya = skuItem.skuName
      const items = skuItem.skuItems
      if (items.length == 0) {
        this.combine(index + 1, current)
      }
      for (let i = 0; i < items.length; i++) {
        if (!items[i]) continue
        let newMap = {}
        newMap = _.extend(newMap, current)
        newMap[keya] = items[i]
        this.combine(index + 1, newMap)
      }
    } else if (index == this.selectSku.length - 1) {
      const skuItem = this.selectSku[index]
      const keya = skuItem.skuName
      const items = skuItem.skuItems
      if (items.length == 0) {
        this.result.push(current)
      }
      for (let i = 0; i < items.length; i++) {
        if (!items[i]) continue
        let newMap = {}
        newMap = _.extend(newMap, current)
        newMap[keya] = items[i]
        this.result.push(newMap)
      }
    }
  }
}

export const sku = new Sku()

// const result = []// 组合成产品集
// /**
//  * 发布一款商品选择的一个属性，这是一个规格数组，数组个数不确定
//  * 根据这个选择的属性组合成不同的产品
//  */
// const selectSku = [
//   { skuName: '容量', skuItems: ['16G', '64G', '128G'] },
//   { skuName: '颜色', skuItems: ['土豪金', '银色', '黑色', 'pink'] },
//   { skuName: '网络', skuItems: ['联通', '移动', '电信'] }
// ]
//
// function combine (index, current) {
//   if (index < selectSku.length - 1) {
//     let skuItem = selectSku[index]
//     let keya = skuItem.skuName
//     let items = skuItem.skuItems
//     if (items.length == 0) {
//       combine(index + 1, current)
//     }
//     for (let i = 0; i < items.length; i++) {
//       if (!items[i]) continue
//       let newMap = {}
//       newMap = _.extend(newMap, current)
//       newMap[keya] = items[i]
//       combine(index + 1, newMap)
//     }
//   } else if (index == selectSku.length - 1) {
//     let skuItem = selectSku[index]
//     let keya = skuItem.skuName
//     let items = skuItem.skuItems
//     if (items.length == 0) {
//       result.push(current)
//     }
//     for (let i = 0; i < items.length; i++) {
//       if (!items[i]) continue
//       let newMap = {}
//       newMap = _.extend(newMap, current)
//       newMap[keya] = items[i]
//       result.push(newMap)
//     }
//   }
// }
// combine(0, {})
// console.info(result)
/** 组合成产品集
 * [Object { 容量="16G", 颜色="土豪金", 网络="联通"},
 * Object { 容量="16G", 颜色="土豪金", 网络="移动"},
 * Object { 容量="16G", 颜色="土豪金", 网络="电信"},
 * Object { 容量="16G", 颜色="银色", 网络="联通"},
 * Object { 容量="16G", 颜色="银色", 网络="移动"},
 * Object { 容量="16G", 颜色="银色", 网络="电信"},
 * Object { 容量="16G", 颜色="黑色", 网络="联通"},
 * Object { 容量="16G", 颜色="黑色", 网络="移动"},
 * Object { 容量="16G", 颜色="黑色", 网络="电信"},
 * Object { 容量="16G", 颜色="pink", 网络="联通"},
 * Object { 容量="16G", 颜色="pink", 网络="移动"},
 * Object { 容量="16G", 颜色="pink", 网络="电信"},
 * Object { 容量="64G", 颜色="土豪金", 网络="联通"},
 * Object { 容量="64G", 颜色="土豪金", 网络="移动"},
 * Object { 容量="64G", 颜色="土豪金", 网络="电信"},
 * Object { 容量="64G", 颜色="银色", 网络="联通"},
 * Object { 容量="64G", 颜色="银色", 网络="移动"},
 * Object { 容量="64G", 颜色="银色", 网络="电信"},
 * Object { 容量="64G", 颜色="黑色", 网络="联通"},
 * Object { 容量="64G", 颜色="黑色", 网络="移动"},
 * Object { 容量="64G", 颜色="黑色", 网络="电信"},
 * Object { 容量="64G", 颜色="pink", 网络="联通"},
 * Object { 容量="64G", 颜色="pink", 网络="移动"},
 * Object { 容量="64G", 颜色="pink", 网络="电信"},
 * Object { 容量="128G", 颜色="土豪金", 网络="联通"},
 * Object { 容量="128G", 颜色="土豪金", 网络="移动"},
 * Object { 容量="128G", 颜色="土豪金", 网络="电信"},
 * Object { 容量="128G", 颜色="银色", 网络="联通"},
 * Object { 容量="128G", 颜色="银色", 网络="移动"},
 * Object { 容量="128G", 颜色="银色", 网络="电信"},
 * Object { 容量="128G", 颜色="黑色", 网络="联通"},
 * Object { 容量="128G", 颜色="黑色", 网络="移动"},
 * Object { 容量="128G", 颜色="黑色", 网络="电信"},
 * Object { 容量="128G", 颜色="pink", 网络="联通"},
 * Object { 容量="128G", 颜色="pink", 网络="移动"},
 * Object { 容量="128G", 颜色="pink", 网络="电信"}]
 */
