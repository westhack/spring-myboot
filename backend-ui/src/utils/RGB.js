function GRB () {
  this.hslArray = this.getHslArray()
}

GRB.prototype = {
  rgbArray: function (num) {
    this.hslArray = this.getHslArray(num)
    const self = this
    if (!self.hslArray.length) return []

    const rgbArray = self.hslArray.map(function (item) {
      return self.hslToRgb.apply(this, item)
    })

    return rgbArray.map(function (item) {
      return {
        value: item,
        style: { background: 'rgb(' + item.toString() + ')' }
      }
    })
  },
  /**
   * HSL颜色值转换为RGB
   * H，S，L 设定在 [0, 1] 之间
   * R，G，B 返回在 [0, 255] 之间
   *
   * @param H 色相
   * @param S 饱和度
   * @param L 亮度
   * @returns Array RGB色值
   */
  hslToRgb: function (H, S, L) {
    let R, G, B
    if (+S === 0) {
      R = G = B = L // 饱和度为0 为灰色
    } else {
      const hue2Rgb = function (p, q, t) {
        if (t < 0) t += 1
        if (t > 1) t -= 1
        if (t < 1 / 6) return p + (q - p) * 6 * t
        if (t < 1 / 2) return q
        if (t < 2 / 3) return p + (q - p) * (2 / 3 - t) * 6
        return p
      }
      const Q = L < 0.5 ? L * (1 + S) : L + S - L * S
      const P = 2 * L - Q
      R = hue2Rgb(P, Q, H + 1 / 3)
      G = hue2Rgb(P, Q, H)
      B = hue2Rgb(P, Q, H - 1 / 3)
    }
    return [Math.round(R * 255), Math.round(G * 255), Math.round(B * 255)]
  },

  // 获取随机HSL
  randomHsl: function () {
    const H = Math.random()
    const S = Math.random()
    const L = Math.random()
    return [H, S, L]
  },

  // 获取HSL数组
  getHslArray: function (num) {
    const HSL = []
    const hslLength = num // 获取数量
    for (let i = 0; i < hslLength; i++) {
      let ret = this.randomHsl()

      // 颜色相邻颜色差异须大于 0.25
      if (i > 0 && Math.abs(ret[0] - HSL[i - 1][0]) < 0.25) {
        i--
        continue // 重新获取随机色
      }
      ret[1] = 0.7 + (ret[1] * 0.2) // [0.7 - 0.9] 排除过灰颜色
      ret[2] = 0.4 + (ret[2] * 0.4) // [0.4 - 0.8] 排除过亮过暗色

      // 数据转化到小数点后两位
      ret = ret.map(function (item) {
        return parseFloat(item.toFixed(2))
      })

      HSL.push(ret)
    }
    return HSL
  }
}

export default new GRB()
