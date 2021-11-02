<template>
  <div class="components-form-item" v-show="!hidden">

    <a-tree
      :checkable="item.checkable || item.treeCheckable"
      :treeData="dataSource"
      :defaultExpandedKeys="oldInputValue"
      :defaultSelectedKeys="oldInputValue"
      :defaultCheckedKeys="oldInputValue"
      :autoExpandParent="item.treeDefaultExpandAll"
      :defaultExpandAll="item.treeDefaultExpandAll"
      :checkStrictly="item.checkStrictly"
      @select="onSelect"
      @check="onCheck"
      v-model="inputValue"
    >
      <template slot="title" slot-scope="{title}">{{ title }}</template>
    </a-tree>
  </div>
</template>

<script>
import { axios } from '@/utils/request'
import _ from 'lodash'
import { listToTree2 } from '@/utils/util'

export default {
  name: 'FormItemTree',
  components: {},
  props: {
    item: {
      type: Object,
      required: true
    },
    isSearch: {
      type: Boolean,
      default: false
    },
    size: {
      type: String,
      default: 'default'
    },
    width: {
      type: [String, Number],
      default: '100%'
    },
    styles: {
      type: [String, Object],
      default: null
    },
    options: {
      type: [String, Object, Array],
      default: null
    },
    itemChange: {
      type: [String, Function],
      default: null
    },
    itemSelect: {
      type: [String, Function],
      default: null
    },
    itemSearch: {
      type: [String, Function],
      default: null
    },
    itemOpenChange: {
      type: [String, Function],
      default: null
    },
    itemSelectChange: {
      type: [String, Function],
      default: null
    },
    itemAfterChange: {
      type: [String, Function],
      default: null
    },
    value: {
      type: [String, Array, Object, Number, Function, Boolean],
      default: null
    }
  },
  data () {
    const vm = this
    let value = null

    if (vm.value !== undefined && vm.value != null) {
      value = this.parseValue()
    }

    return {
      mapVisible: false,
      inputValue: value,
      oldInputValue: _.cloneDeep(value),
      fetching: false,
      searchDataSource: null,
      isPassword: true
    }
  },
  async created () {
    await this.fetchData()
  },
  computed: {
    'hidden': function () {
      return this.item.hidden === true
    },
    'dataSource': function () {
      const dataSource = this.searchDataSource || this.options
      return this.parseData(dataSource)

      return dataSource
    },

    'onChange': function () {
      const callback = this.itemChange || this.item.change
      return this.callback(callback)
    },

    'onSelect': function () {
      const callback = this.itemSelect || this.item.select
      return this.callback(callback)
    },

    'filterOption': function () {
      const callback = this.itemSearch || this.item.search
      if (callback && _.isString(callback)) {
        return false
      }

      return true
    },

    'onSearch': function () {
      const callback = this.itemSearch || this.item.search
      if (callback && _.isString(callback)) {
        return this.getSearch
      }
      return this.callback(callback)
    },

    'onOpenChange': function () {
      const callback = this.itemOpenChange || this.item.openChange
      return this.callback(callback)
    },

    'onSelectChange': function () {
      const callback = this.itemSelectChange || this.item.selectChange
      return this.callback(callback)
    },

    'onAfterChange': function () {
      const callback = this.itemAfterChange || this.item.afterChange
      return this.callback(callback)
    },

    'onCheck': function () {
      const callback = this.itemCheck || this.item.check
      return this.callback(callback)
    }
  },
  mounted: function () {
  },
  watch: {
    'value': function (val) {
      this.inputValue = this.parseValue(val)
    },
    'inputValue': function (val) {
      if (this.item.checkStrictly === true && _.has(val, 'checked')) {
        this.$emit('input', val['checked'])
        this.$emit('change', val['checked'])
      } else {
        this.$emit('input', val)
        this.$emit('change', val)
      }
    }
  },
  methods: {

    cleanValue () {
      this.inputValue = null
    },
    getSearch (value) {
      const method = this.item.method || 'get'
      this.fetching = true
      const url = this.itemSearch || this.item.search
      axios({
        url: url,
        method: method,
        data: { keywords: value }
      }).then(res => {
        if (res.data.items) {
          let _callback = null
          if (this.item.callback !== false) {
            _callback = this.item.callback || null
          }

          if (_.isFunction(_callback)) {
            _callback(res.data.items)
          } else {
            this.searchDataSource = res.data.items
          }
        }
      }).finally(() => {
        // this.fetching = false
      })
    },

    getCallback () {
    },

    async fetchData (value) {
      this.fetching = true
      const url = this.item.url
      const method = this.item.method || 'get'

      let _callback = null

      if (this.item.callback !== false) {
        _callback = this.item.callback || this.getCallback()
      }

      if (url !== undefined && url !== '') {
        const res = await axios({ url: url, method: method, data: { keywords: value } })
        if (res.data.items) {
          if (_.isFunction(_callback)) {
            _callback(res.data.items)
          } else {
            this.searchDataSource = res.data.items
          }
        }
      }
    },

    parseValue (val) {
      let value = null

      value = _.cloneDeep(val)

      value = this.getTreeValue(value)

      return value
    },

    emitEmpty () {
      this.inputValue = null
    },

    isFunction (fun) {
      return _.isFunction(fun)
    },

    isArray (fun) {
      return _.isArray(fun)
    },

    callback (callback) {
      if (_.isFunction(callback)) {
        return callback
      }

      if (_.isString(callback) && callback.indexOf('function') > -1) {
        // eslint-disable-next-line no-new-func
        const fun = new Function('return ' + callback)
        return fun()
      }

      return (val) => {
      }
    },

    parseData (val) {
      const valueName = this.item['valueName'] != null ? this.item['valueName'] : 'value'
      const labelName = this.item['labelName'] != null ? this.item['labelName'] : 'label'

      if (this.item['parentName'] != null) {
        const parentName = this.item['parentName'] || 'parentId'
        const idName = this.item['idName'] || 'id'
        const parent = _.has(this.item, 'parent') ? this.item['parent'] : null
        _.each(val, (v, i) => {
          if (this.item.type == 'tree') {
            val[i]['key'] = val[i]['value'] = (val[i][valueName] == null ? null : val[i][valueName] + '')
          } else {
            val[i]['key'] = val[i]['value'] = (val[i][valueName] == null ? null : val[i][valueName])
          }

          val[i]['title'] = val[i][labelName]
          val[i]['children'] = []
          val[i]['checked'] = null
        })

        const val1 = listToTree2(_.cloneDeep(val), idName, parentName, parent)

        if (_.has(this.item, 'pushParent')) {
          const pushParent = this.item['pushParent']
          pushParent['children'] = val1
          return [pushParent]
        } else {
          return val1
        }
      } else {
        const _parseData = (tree) => {
          for (let i = 0; i < tree.length; i++) {
            if (this.item.type == 'tree') {
              tree[i]['key'] = tree[i]['value'] = (tree[i][valueName] == null ? null : tree[i][valueName] + '')
            } else {
              tree[i]['key'] = tree[i]['value'] = (tree[i][valueName] == null ? null : tree[i][valueName])
            }
            tree[i]['title'] = tree[i][labelName]
            tree[i]['checked'] = null

            if (tree[i].children && tree[i].children.length > 0) {
              _parseData(tree[i].children)
            }
          }
          return tree
        }

        if (val) {
          return _parseData(val)
        }
        return val
      }
    },

    formChange () {
      this.$emit('formChange')
    },

    getTreeValue (value) {
      if (value == null || value == '') {
        value = []
      } else if (_.isString(value) || _.isNumber(value)) {
        value = value.split(',')
      } else {
        if (_.has(this.item, 'valueKey')) {
          const valueKey = this.item.valueKey
          const vv = []
          _.each(value, (o) => {
            let v = null
            if (_.isObject(o)) {
              v = _.get(o, valueKey)
            } else {
              v = o
            }

            if (this.item.type == 'tree') {
              v = v + ''
            }

            vv.push(v)
          })
          value = vv
        }
      }

      return value
    }

  }
}

</script>

<style scoped>
#components-form-item >>> .anticon-close-circle {
  cursor: pointer;
  color: #ccc;
  transition: color 0.3s;
  font-size: 12px;
}

#components-form-item >>> .anticon-close-circle:hover {
  color: #999;
}

#components-form-item >>> .anticon-close-circle:active {
  color: #666;
}
</style>
