<template>
  <div>
    <a-cascader
      v-model="select"
      :options="options"
      :placeholder="placeholder"
      change-on-select
      :size="size"
      :showSearch="showSearch"
      @change="onChange">
    </a-cascader>
  </div>
</template>

<script>
import areaData from 'area-data'
import util from './util'
import _ from 'lodash'

const levelShow = (level, code) => {
  if (level === 2 || level === 3) {
    return Object.keys(areaData['86']).indexOf(code) > -1
  } else if (level === 4) {
    return true
  }
}

export default {
  name: 'AreaCalendar',
  props: {
    value: {
      type: [Array],
      required: false,
      default: () => {
        return []
      }
    },
    placeholder: {
      type: String,
      required: false,
      default: '请选择'
    },
    disabled: {
      type: Boolean,
      default: false
    },
    showSearch: {
      type: Boolean,
      default: false
    },
    dataType: {
      type: String,
      default: 'all',
      validator: val => {
        return util.oneOf(val, util.dataType)
      }
    },
    level: {
      type: [Number, String],
      default: 3,
      validator: val => {
        return util.checkLevel(parseInt(val))
      }
    },
    size: {
      type: String,
      default: 'default'
    }
  },
  data () {
    return {
      select: [],
      oldOptions: [],
      options: []
    }
  },
  mounted () {
    if (this.showSearch) {
      this.initAll()
    } else {
      this.init()
    }
    if (this.canEmit(this.value)) {
      this.setDefaultValue()
    }
  },
  watch: {
    value (val) {
      if (this.canEmit(this.value)) {
        this.setDefaultValue()
      }
    }
  },
  computed: {
    showLevel () {
      return parseInt(this.level)
    }
  },
  methods: {
    parseValue () {

    },
    init () {
      const proData = []
      for (const p in areaData['86']) {
        const proitem = {
          value: p,
          label: areaData['86'][p],
          isLeaf: true
        }
        if (this.showLevel > 0 && areaData[p]) {
          proitem.isLeaf = false
          proitem.loading = false
        }
        proData.push(proitem)
      }

      if (this.value.length > 1) {
        _.each(proData, (p, i) => {
          if (p.value == this.value[0]) {
            proData[i] = this.getChildren(this.value[0], proData[i], 2)
          }
        })
      }

      this.options = proData
    },
    getChildren (code, parent, level) {
      const area = areaData[code]
      level = level + 1
      _.each(area, (label, code) => {
        let p = {
          value: code,
          label: label,
          children: []
        }

        if (this.showLevel > level && areaData[code]) {
          p = this.getChildren(code, p, level)
        } else {
          delete p.children
        }
        if (!parent['children']) {
          parent['children'] = []
        }
        parent['children'].push(p)
      })

      return parent
    },
    initAll () {
      const proData = []
      for (const p in areaData['86']) {
        let proitem = {
          value: p,
          label: areaData['86'][p],
          isLeaf: true,
          children: []
        }
        if (this.showLevel > 0 && areaData[p]) {
          proitem.isLeaf = false
          proitem.loading = false
          proitem = this.getChildren(p, proitem, 1)
        }
        proData.push(proitem)
      }

      this.options = proData
    },
    setDefaultValue () {
      if (this.value[0]) {
        const select = []
        if (isNaN(parseInt(this.value[0]))) {
          let i = 0
          let index = ''
          while (this.value[i] && i <= this.showLevel) {
            if (i === 0) {
              index = util.getIndex(areaData['86'], this.value[0])
            } else {
              index = util.getIndex(areaData[index], this.value[i])
            }
            select.push(index)
            i++
          }
          this.select = select
        } else {
          this.select = this.value
        }
        const res = this.procesValue(this.select)
        this.oldOptions = res
        this.$emit('input', res)
        this.$emit('on-change', res)
        this.$emit('change', res)
      }
    },
    loadData (selectedOptions) {
      const targetOption = selectedOptions[selectedOptions.length - 1]
      targetOption.loading = true

      const childData = []
      const childs = areaData[targetOption.value]

      for (const c in childs) {
        const childitem = {
          value: c,
          label: areaData[targetOption.value][c],
          isLeaf: true
        }
        if (areaData[childitem.value] && levelShow(this.showLevel, targetOption.value)) {
          childitem.loading = false
          childitem.isLeaf = false
        }
        childData.push(childitem)
        targetOption.children = childData
      }

      targetOption.loading = false
    },
    procesValue (arr) {
      let i = 0
      const res = []
      while (arr[i]) {
        let name = ''
        if (i === 0) {
          name = areaData['86'][arr[i]]
        } else {
          name = areaData[arr[i - 1]][arr[i]]
        }
        let item
        if (this.dataType === 'all') {
          item = {
            code: arr[i],
            name: name
          }
        } else if (this.dataType === 'name') {
          item = name
        } else {
          item = arr[i]
        }
        res.push(item)
        i++
      }

      return res
    },
    canEmit (res) {
      let ifEmit = false
      if (this.value && this.value.length !== 0) {
        if (typeof res[0] === 'string') {
          if (this.value[this.value.length - 1] !== this.oldOptions[this.oldOptions.length - 1]) {
            ifEmit = true
          }
        } else {
          if (this.oldOptions && this.oldOptions.length !== 0 && this.value[this.value.length - 1].code !== this.oldOptions[this.oldOptions.length - 1].code) {
            ifEmit = true
          }
        }
      } else {
        ifEmit = true
      }
      return ifEmit
    },
    onChange (resArr) {
      const res = this.procesValue(resArr)
      this.oldOptions = res
      this.$emit('input', res)
      this.$emit('on-change', res)
      this.$emit('change', res)
    }
  }
}
</script>

<style scoped>

</style>
