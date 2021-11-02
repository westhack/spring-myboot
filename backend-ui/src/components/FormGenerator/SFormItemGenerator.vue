<template>
  <div class="components-form-item" v-show="!hidden">

    <div v-if="item.type === 'password'">
      <a-input
        v-if="isPassword"
        v-model="inputValue"
        :size="size"
        :style="{width: width}"
        type="password"
        :disabled="item.disabled"
        :placeholder="item.placeholder"
        @change=" () => onChange(inputValue)"
      >
        <a-icon
          style="color: #cccccc"
          slot="suffix"
          type="eye"
          v-show="inputValue"
          @click="() => {this.isPassword = !this.isPassword} "/>
      </a-input>
      <a-input
        v-else
        v-model="inputValue"
        :size="size"
        :style="{width: width}"
        type="text"
        :disabled="item.disabled"
        :placeholder="item.placeholder"
        @change=" () => onChange(inputValue)"
      >
        <a-icon
          style="color: #cccccc"
          slot="suffix"
          type="eye-invisible"
          v-show="inputValue"
          @click="() => {this.isPassword = !this.isPassword} "/>
      </a-input>
    </div>

    <div v-else-if="item.type === 'label'">
      <div>
        <a-input type="text" v-model="inputValue" readonly disabled/>
      </div>
    </div>

    <div v-else-if="item.type === 'none'">

    </div>

    <a-input-number
      v-else-if="item.type === 'input-number'"
      v-model="inputValue"
      :size="size"
      :style="{width: width}"
      :max="dataSource.max"
      :min="dataSource.min"
      :precision="dataSource.precision"
      :step="dataSource.step"
      :disabled="item.disabled"
      :placeholder="item.placeholder"
      @change="onChange"
    />

    <div v-else-if="item.type === 'switch' || item.type === 'boolean'">
      <a-switch
        v-if="isSearch === false"
        v-model="inputValue"
        :size="size"
        :disabled="item.disabled"
        :checkedChildren="dataSource.checked"
        :unCheckedChildren="dataSource.unchecked"
        @change="onChange"
      />

      <a-select
        v-else
        v-model="inputValue"
        :size="size"
        :allowClear="item.allowClear || isSearch"
        :style="{width: width}"
        :defaultValue="inputValue"
        @change="onChange"
        @search="onSearch"
        @select="onSelect"
      >
        <a-select-option value="">全部</a-select-option>
        <a-select-option :value="`1`">{{ dataSource.checked }}</a-select-option>
        <a-select-option :value="`0`">{{ dataSource.unchecked }}</a-select-option>
      </a-select>
    </div>

    <a-select
      v-else-if="item.type === 'select'"
      v-model="inputValue"
      :defaultValue="inputValue"
      :allowClear="item.allowClear || isSearch"
      :size="size"
      :style="{width: width}"
      :maxTagCount="item.maxTagCount ? item.maxTagCount : 1"
      :mode="item.multiple === true ? 'multiple' : item.mode"
      :showSearch="item.showSearch"
      :filterOption="filterOption"
      :labelInValue="item.labelInValue != undefined ? item.labelInValue : false"
      @change="onChange"
      @search="onSearch"
      @select="onSelect"
      :notFoundContent="fetching ? undefined : null"
    >
      <a-spin v-if="fetching" slot="notFoundContent" size="small"/>
      <a-select-option :key="index2" v-for="(option, index2) in dataSource" :value="option.value">{{
        option.label
      }}
      </a-select-option>
    </a-select>

    <a-select
      v-else-if="item.type === 'multiple-select'"
      v-model="inputValue"
      :defaultValue="inputValue"
      :allowClear="item.allowClear"
      :size="size"
      :style="{width: width}"
      :maxTagCount="item.maxTagCount ? item.maxTagCount : 1"
      mode="multiple"
      :showSearch="item.showSearch"
      :filterOption="filterOption"
      :labelInValue="item.labelInValue != undefined ? item.labelInValue : false"
      @change="onChange"
      @search="onSearch"
      @select="onSelect"
      :notFoundContent="fetching ? undefined : null"
    >
      <a-spin v-if="fetching" slot="notFoundContent" size="small"/>
      <a-select-option :key="index2" v-for="(option, index2) in dataSource" :value="option.value">{{
        option.label
      }}
      </a-select-option>
    </a-select>

    <a-select
      v-else-if="item.type === 'tags'"
      v-model="inputValue"
      :allowClear="item.allowClear"
      :size="size"
      :style="{width: width}"
      mode="tags"
      :showSearch="item.showSearch"
      :filterOption="filterOption"
      :labelInValue="item.labelInValue != undefined ? item.labelInValue : false"
      @change="onChange"
      @search="onSearch"
      @select="onSelect"
      :notFoundContent="fetching ? undefined : null"
    >
      <a-spin v-if="fetching" slot="notFoundContent" size="small"/>
      <a-select-option :key="index2" v-for="(option, index2) in dataSource" :value="option.value">{{
        option.label
      }}
      </a-select-option>
    </a-select>

    <a-radio-group
      v-else-if="item.type === 'radio'"
      v-model="inputValue"
      :defaultValue="inputValue"
      :size="size"
      :disabled="item.disabled"
      :style="{width: width}"
      buttonStyle="solid"
      @change="onChange"
    >
      <a-radio
        style="margin-left: 10px;"
        :key="index"
        v-for="(option, index) in dataSource"
        :value="option.value"
        :disabled="option.disabled">{{ option.label }}
      </a-radio>
    </a-radio-group>

    <a-checkbox-group
      v-else-if="item.type === 'checkbox'"
      v-model="inputValue"
      :defaultValue="inputValue"
      :size="size"
      :disabled="item.disabled"
      :style="{width: width}"
      buttonStyle="solid"
      @change="onChange"
    >
      <a-row>
        <a-checkbox style="margin-left: 10px;" :key="index" v-for="(option, index) in dataSource" :value="option.value">
          {{ option.label }}
        </a-checkbox>
      </a-row>
    </a-checkbox-group>

    <a-rate
      v-else-if="item.type === 'rate'"
      v-model="inputValue"
      :size="size"
      :disabled="item.disabled"
      style="margin-left: 10px"
      allow-half
    />

    <a-cascader
      v-else-if="item.type === 'cascader'"
      v-model="inputValue"
      :defaultValue="inputValue"
      :style="{width: width}"
      :size="size"
      :allowClear="item.allowClear"
      :disabled="item.disabled"
      :options="dataSource"
      :loadData="item.loadData"
      :showSearch="item.showSearch"
      @change="onChange"
      @search="onSearch"
      @select="onSelect"
    />

    <a-auto-complete
      v-else-if="item.type === 'auto-complete'"
      v-model="inputValue"
      :style="{width: width}"
      :size="size"
      :disabled="item.disabled"
      :placeholder="item.placeholder"
      :dataSource="dataSource"
      @change="onChange"
      @search="onSearch"
      @select="onSelect"
    />

    <a-date-picker
      v-else-if="item.type === 'date-picker'"
      v-model="inputValue"
      :style="{width: width}"
      :size="size"
      :allowClear="item.allowClear"
      :disabled="item.disabled"
      :placeholder="item.placeholder"
      :format="item.format"
      :show-time="item.showTime ? { format: 'HH:mm' } : false"
      :disabledDate="item.disabledDate"
      :disabledTime="item.disabledTime"
      @change="onChange"
      @openChange="onOpenChange"
    ></a-date-picker>

    <a-month-picker
      v-else-if="item.type === 'month-picker'"
      v-model="inputValue"
      :style="{width: width}"
      :size="size"
      :allowClear="item.allowClear"
      :disabled="item.disabled"
      :placeholder="item.placeholder"
      :format="item.format"
      :disabledDate="item.disabledDate"
      :disabledTime="item.disabledTime"
      @change="onChange"
      @openChange="onOpenChange"
    ></a-month-picker>

    <a-range-picker
      v-else-if="item.type === 'range-picker'"
      v-model="inputValue"
      :style="{width: width}"
      :size="size"
      :allowClear="item.allowClear"
      :disabled="item.disabled"
      :format="item.format"
      :disabledDate="item.disabledDate"
      :disabledTime="item.disabledTime"
      @change="onChange"
      @openChange="onOpenChange"
    ></a-range-picker>

    <a-week-picker
      v-else-if="item.type === 'week-picker'"
      v-model="inputValue"
      :style="{width: width}"
      :size="size"
      :allowClear="item.allowClear"
      :disabled="item.disabled"
      :placeholder="item.placeholder"
      :format="item.format"
      :disabledDate="item.disabledDate"
      :disabledTime="item.disabledTime"
      @change="onChange"
      @openChange="onOpenChange"
    ></a-week-picker>

    <a-time-picker
      v-else-if="item.type === 'time-picker'"
      v-model="inputValue"
      :style="{width: width}"
      :size="size"
      :allowClear="item.allowClear"
      :disabled="item.disabled"
      :placeholder="item.placeholder"
      :use12Hours="item.use12Hours"
      :format="item.format"
      @change="onChange"
      @openChange="onOpenChange"
    />

    <a-tree-select
      v-else-if="item.type === 'tree-select'"
      v-model="inputValue"
      :style="{width: width}"
      :size="size"
      :disabled="item.disabled"
      :showSearch="item.showSearch"
      :treeData="dataSource"
      :loadData="item.loadData"
      :treeCheckable="item.treeCheckable"
      :showCheckedStrategy="item.showParent === true ? 'SHOW_PARENT' : null"
      :searchPlaceholder="item.searchPlaceholder"
      :dropdownStyle="item.dropdownStyle"
      :placeholder="item.placeholder"
      :allowClear="item.allowClear"
      :multiple="item.multiple"
      :treeDefaultExpandAll="item.treeDefaultExpandAll"
      @change="onChange"
      @search="onSearch"
      @select="onSelect"
    />

    <Tinymce
      v-else-if="item.type === 'editor'"
      :menubar="item.menubar"
      v-model="inputValue"
      @change="onChange"
    />

    <div v-else-if="item.type === 'input-image'">
      <InputImageUpload
        v-model="inputValue"
        :fileSize="item.fileSize"
        :limit="item.limit"
        @change="onChange"
      />
    </div>

    <div v-else-if="item.type === 'image'">
      <MultipleImageUpload
        v-if="item.multiple === true"
        v-model="inputValue"
        :fileSize="item.fileSize"
        :limit="item.limit"
        :width="item.width"
        :height="item.height || item.width"
        @change="onChange"
      />

      <SingleImageUpload
        v-else
        v-model="inputValue"
        :fileSize="item.fileSize"
        :width="item.width"
        :height="item.height || item.width"
        @change="onChange"
      />

    </div>

    <MultipleImageUpload
      v-else-if="item.type === 'multiple-image'"
      v-model="inputValue"
      :fileSize="item.fileSize"
      :limit="item.limit"
      @change="onChange"
    />

    <FileUpload
      v-else-if="item.type === 'file'"
      v-model="inputValue"
      @change="onChange"
    />

    <a-transfer
      v-else-if="item.type === 'transfer'"
      v-model="inputValue"
      :placeholder="item.placeholder"
      :dataSource="dataSource"
      :titles="item.titles"
      :showSearch="item.showSearch"
      :targetKeys="item.targetKeys"
      :selectedKeys="item.selectedKeys"
      @change="onChange"
      @selectChange="onSelectChange"
      @scroll="scroll"
      :render="item.render"
      :disabled="item.disabled"
    />

    <a-slider
      v-else-if="item.type === 'slider'"
      v-model="inputValue"
      :max="dataSource.max"
      :min="dataSource.min"
      :marks="dataSource.marks"
      :range="item.multiple"
      @change="onChange"
      @afterChange="onAfterChange"
    />

    <a-tree
      v-else-if="item.type === 'tree'"
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

    <a-textarea
      v-else-if="item.type === 'textarea'"
      v-model="inputValue"
      :placeholder="item.placeholder"
      :rows="4"
      @change="onChange"
    ></a-textarea>

    <div v-else-if="item.type === 'minimum'">
      <a-input-group compact style="display: flex">
        <a-input
          v-model="inputValue[0]"
          @change="onChange"
          style=" flex: 1; text-align: center"
          :placeholder="isArray(item.placeholder) ? item.placeholder[0] : item.placeholder"/>
        <a-input
          style=" width: 30px; border-left: 0; pointer-events: none; backgroundColor: #fff"
          placeholder="~"
          disabled/>
        <a-input
          v-model="inputValue[1]"
          @change="onChange"
          style=" flex: 1; text-align: center;  border-left: 0"
          :placeholder="isArray(item.placeholder) ? item.placeholder[1] : item.placeholder"/>
      </a-input-group>
    </div>

    <div v-else-if="item.type === 'color'">
      <ColorInput v-model="inputValue"></ColorInput>
    </div>

    <div v-else-if="item.type === 'dict'">
      <DictInput v-model="inputValue" @change="() => onChange(inputValue)"></DictInput>
    </div>

    <div v-else-if="item.type === 'area'">
      <AreaCalendar
        v-model="inputValue"
        :showSearch="true"
        :data-type="item.dataType || 'all'"
        @change="onChange"></AreaCalendar>
    </div>

    <div v-else-if="item.type === 'table-select'">
      <SelectTable
        ref="selectTable"
        :rowKey="item.rowKey || 'id'"
        :search-form-data="item.searchFormData"
        :is-search="item.isSearch"
        :columns="item.columns"
        :viewColumns="item.viewColumns"
        :list-api-url="item.apiUrl || item.api"
        :multiple="item.multiple ? true : false"
        :valueType="item.valueType ? true : false"
        :scroll="item.tableScroll || {x: 1200}"
        v-model="inputValue"
        :width="item.width || 720"
        @change="onChange"
      ></SelectTable>
    </div>

    <div v-else-if="item.type === 'banner'">
      <BannerInput
        v-model="inputValue"
        :limit="item.limit"
        @change="() => onChange(inputValue)"
        @formChange="formChange"></BannerInput>
    </div>

    <div v-else-if="item.type === 'dynamic-input'">
      <DynamicInput
        :fields="item.fields"
        :limit="item.limit"
        v-model="inputValue"
        :layout="item.layout"
        @change="() => onChange(inputValue)"></DynamicInput>
    </div>
    <div v-else-if="item.type === 'single-dynamic-input'">
      <SingleDynamicInput
        :fields="item.fields"
        v-model="inputValue"
        :layout="item.layout"
        @change="() => onChange(inputValue)"></SingleDynamicInput>
    </div>

    <div v-else-if="item.type === 'sku'">
      <SkuInput v-model="inputValue" @change="() => onChange(inputValue)"></SkuInput>
    </div>

    <div v-else-if="item.type === 'select-image'">
      <SelectImageUpload
        :multiple="item.multiple"
        :limit="item.limit"
        :fileSize="item.fileSize"
        v-model="inputValue"
        @change="() => onChange(inputValue)"></SelectImageUpload>
    </div>

    <div v-else-if="item.type === 'single-select-image'">
      <SingleSelectImageUpload
        :fileSize="item.fileSize"
        v-model="inputValue"
        @change="() => onChange(inputValue)"></SingleSelectImageUpload>
    </div>

    <a-input
      v-else
      v-model="inputValue"
      :style="{width: width}"
      type="input"
      :size="size"
      :disabled="item.disabled"
      :placeholder="item.placeholder"
      @change="() => onChange(inputValue)"
    />

  </div>
</template>

<script>
import { TreeSelect } from 'ant-design-vue'
import SingleImageUpload from '@/components/Upload/SingleImageUpload'
import FileUpload from '@/components/Upload/FileUpload'
import MultipleImageUpload from '@/components/Upload/MultipleImageUpload'
import InputImageUpload from '@/components/Upload/InputImageUpload'
import SelectImageUpload from '@/components/Upload/SelectImageUpload'
import SingleSelectImageUpload from '@/components/Upload/SingleSelectImageUpload'
import Tinymce from '@/components/Tinymce/index'
import AreaCalendar from '@/components/Form/Area'
import DictInput from '@/components/Form/DictInput'
import SkuInput from '@/components/Form/SkuInput/SkuInput'
import BannerInput from '@/components/Form/Banner/BannerInput'
import DynamicInput from '@/components/Form/DynamicInput'
import SingleDynamicInput from '@/components/Form/SingleDynamicInput'
import SelectTable from '@/components/Table/SelectTable'
import { axios } from '@/utils/request'
import _ from 'lodash'
import { listToTree2 } from '@/utils/util'
import moment from 'moment'
import ColorInput from '@/components/Form/ColorInput'

export default {
  name: 'SFormItemGenerator',
  components: {
    ColorInput,
    Tinymce,
    SingleImageUpload,
    MultipleImageUpload,
    InputImageUpload,
    SelectImageUpload,
    SingleSelectImageUpload,
    FileUpload,
    DictInput,
    AreaCalendar,
    SelectTable,
    BannerInput,
    DynamicInput,
    SingleDynamicInput,
    SkuInput
  },
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
      default: ''
    }
  },
  data () {
    const vm = this
    let value = null

    if (vm.value !== undefined && vm.value != null) {
      value = this.parseValue(vm.value)
      if (this.item.type === 'select') {
        value = this.parseSelectItemValue(value)
      }
    } else if (vm.item.value !== undefined && vm.value != null) {
      value = this.parseValue(vm.item.value)
    } else {
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
      let dataSource = this.searchDataSource || this.options || this.item.options
      if (dataSource != null) {
        if (this.item.type === 'select' || this.item.type === 'multiple-select' || this.item.type === 'tags' || this.item.type === 'radio' || this.item.type === 'checkbox') {
          dataSource = dataSource.map((value, index) => {
            const labelName = this.item['labelName'] || 'label'
            const valueName = this.item['valueName'] || 'value'
            if (_.isString(value) || _.isNumber(value)) {
              return {
                'label': value,
                'value': value == null ? null : value // 转字符串
              }
            } else {
              // return value
              return {
                'label': value[labelName],
                'value': value[valueName] == null ? null : value[valueName] // 转字符串
              }
            }
          })
        }
      }

      if (this.item.type === 'auto-complete') {
        dataSource = dataSource.map((value, index) => {
          if (_.isString(value) || _.isNumber(value)) {
            return value
          } else {
            return value['value']
          }
        })
      }

      if (this.item.type === 'input-number' || this.item.type === 'slider') {
        if (dataSource == null) {
          dataSource = {}
        }

        if (dataSource['max'] === undefined) {
          dataSource['max'] = Infinity
        }

        if (dataSource['min'] === undefined) {
          dataSource['min'] = -Infinity
        }

        if (_.has(this.item, 'min')) {
          dataSource['min'] = this.item['min']
        }
        if (_.has(this.item, 'max')) {
          dataSource['max'] = this.item['max']
        }
      }

      if (this.item.type === 'tree' || this.item.type === 'tree-select') {
        return this.parseData(dataSource)
      }

      if (this.item.type === 'switch' || this.item.type === 'boolean') {
        return Object.assign({ 'checked': '是', 'unchecked': '否' }, dataSource)
      }

      return dataSource
    },

    'onChange': function () {
      const callback = this.itemChange || this.item.change
      this.$emit('input', this.inputValue)
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
      if (this.item.type === 'select') {
        this.inputValue = this.parseSelectItemValue(this.inputValue)
      }
    },
    'inputValue': function (val) {
      if (this.item.checkStrictly === true) {
        this.$emit('input', val['checked'])
        this.$emit('change', val['checked'])
      } else {
        if (this.item.type === 'minimum' && (val[0] == '' || val[1] == '')) {
          this.$emit('input', null)
          this.$emit('change', null)
        } else {
          this.$emit('input', val)
          this.$emit('change', val)
        }
      }
      this.$emit('formChange')
    },
    'item.value': function (val) {
      this.inputValue = this.parseValue(val)
      if (this.item.type === 'select') {
        this.inputValue = this.parseSelectItemValue(this.inputValue)
      }
    },
    'item.type': function (val) {
      if (['date-picker', 'week-picker', 'month-picker', 'range-picker', 'time-picker'].indexOf(val) != -1) {
        this.item.value = null
      } else {
        this.inputValue = this.parseValue(this.item.value)
        if (this.item.type === 'select') {
          this.inputValue = this.parseSelectItemValue(this.inputValue)
        }
      }
    },
    'item': function (val) {
      this.inputValue = this.parseValue(val.value)
      if (this.item.type === 'select') {
        this.inputValue = this.parseSelectItemValue(this.inputValue)
      }
    },
    'item.hidden': function (val) {
    }
  },
  methods: {

    cleanValue () {
      this.inputValue = ''
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
            _callback = this.item.callback || this.getCallback()
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
      if (this.item.type === 'select' || this.item.type === 'multiple-select') {
        return this.selectCallback
      } else if (this.item.type === 'tree-select' || this.item.type === 'tree') {
        return this.treeSelectCallback
      }

      return null
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

      value = val

      if (this.item.type === 'input' || this.item.type === 'textarea') {
        value = (_.isArray(value) || _.isObject(value)) ? value.toString() : value
      } else if (this.item.type === 'input-number') {
      } else if (this.item.type === 'minimum') {
        value = value || [null, null]
      } else if (this.item.type === 'switch' || this.item.type === 'boolean') {
        if (this.isSearch) {
          // value = ''
        } else {
          if (parseInt(value) === 1 || value === true) {
            value = true
          } else {
            value = false
          }
        }
      } else if (this.item.type === 'tags') {
        if (this.isSearch) {
          value = []
        } else {
          if (value) {
            if (!_.isArray(value)) {
              value = value.split(',')
            }
          } else {
            value = []
          }
        }
      } else if ((this.item.type === 'tree-select')) {
        if (this.item.treeCheckable != true) {
          if (value != null) {
            // value = value
          } else {
            value = null
          }
        } else {
          value = this.getTreeValue(value)
        }
      } else if ((this.item.type === 'tree')) {
        value = this.getTreeValue(value)
      } else if (this.item.type === 'slider') {
        // value = parseInt(value)
      } else if (this.item.type === 'input-number') {
        // value = parseInt(value)
      } else if (this.item.type === 'select') {
        if (!(this.item.mode === 'multiple' || this.item.multiple === true)) {
          // if (value != null) {
          // } else {
          //   value = []
          // }
        }
      } else if (this.item.type === 'file') {
        if (value) {
          if (!_.isArray(value)) {
            value = value.split(',')
          }
        } else {
          value = []
        }
      } else if (this.item.type === 'date-picker' || this.item.type === 'week-picker' || this.item.type === 'month-picker') {
        if (value != null && _.isString(value)) {
          value = moment(value)
        }
      } else if (this.item.type === 'range-picker') {
        if (value != null) {
          if (_.isString(value)) {
            value = value.split(',')
            value[0] = moment(value[0])
            if (value[1]) {
              value[1] = moment(value[1])
            } else {
              value[1] = null
            }
          } else if (_.isArray(value)) {
            value[0] = moment(value[0])
            if (value[1]) {
              value[1] = moment(value[1])
            } else {
              value[1] = null
            }
          } else {
            value = []
          }
        } else {
          value = []
        }
      } else if (this.item.type === 'dict') {
        if (value != '' && _.isString(value)) {
          value = JSON.parse(value)
        }
      } else if (this.item.type === 'select-image') {
        if (value != '' && _.isString(value)) {
          value = value.split(',')
        }
      } else if (this.item.type === 'multiple-select') {
        // if (value == null || value == '') {
        //   value = []
        // } else {
        //   if (_.isString(value)) {
        //     value = value.split(',')
        //   }
        // }
      } else if (this.item.type === 'cascader') {
        if (value == null || value == '') {
          value = []
        } else {
          if (_.isString(value)) {
            value = value.split(',')
          }
        }
      }

      this.$emit('input', value)

      return value
    },

    parseSelectItemValue (value) {
      if (this.item.type === 'select') {
        const valueKey = this.item['valueKey'] || null
        if (valueKey != null) {
          if (this.item['multiple'] === true) {
            if ((_.isArray(value) || _.isObject(value))) {
              const _v = []
              _.each(value, (val, i) => {
                if (_.isObject(val)) {
                  _v[i] = _.get(val, valueKey)
                } else {
                  _v[i] = val
                }
              })

              value = _v
            } else if (_.isString(value)) {
              value = value.split(',')
            }
          } else {
            if (_.isObject(value)) {
              value = _.get(value, valueKey)
            }
          }
        }
      }

      this.$emit('input', value)
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

    selectCallback (list) {
      const labelName = this.item['labelName'] || 'label'
      const valueName = this.item['valueName'] || 'value'

      list.forEach(res => {
        res['label'] = res[labelName]
        res['value'] = res[valueName]
      })
      this.searchDataSource = list
    },

    treeSelectCallback (list) {
      // const idName = this.item['idName'] || 'id'
      // const parentName = this.item['parentName'] || 'parentId'
      // const parent = _.has(this.item, 'parent') ? this.item['parent'] : null

      if (_.has(this.item, 'parentName')) {
        // const tree = listToTree2(list, idName, parentName, parent)
        // this.searchDataSource = tree
        this.searchDataSource = list
      } else {
        this.searchDataSource = list
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
