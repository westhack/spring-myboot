<template>
  <div>

    <a-modal :visible="previewVisible" :footer="null" @cancel="handleCancel">
      <img style="width: 100%" :src="preview" />
    </a-modal>

    <DescriptionList
      v-if="item.type !== 'hidden'"
      :title="item.label"
    >
      <div v-if="item.type === 'image' || item.type === 'input-image'">
        <div v-if="isArray(item.value)">

          <div :key="src + index2" v-for="(src, index2) in item.value" class="ant-upload-list ant-upload-list-picture-card" :style="{width: item.width || 30, height: item.width || 30}">
            <div class="ant-upload-list-item ant-upload-list-item-done" :style="{width: item.width || 30, height: item.width || 30, padding: 0}">
              <div class="ant-upload-list-item-info">
                <span>
                  <a :href="src" target="_blank" rel="noopener noreferrer" class="ant-upload-list-item-thumbnail">
                    <img :src="src" :alt="src">
                  </a>
                </span>
              </div>
              <span class="ant-upload-list-item-actions">
                <a-icon type="eye-o" @click="handlePreview(src)"/>
              </span>
            </div>
          </div>

        </div>
        <div v-else>

          <div class="ant-upload-list ant-upload-list-picture-card" :style="{width: item.width || 30, height: item.width || 30}">
            <div class="ant-upload-list-item ant-upload-list-item-done" :style="{width: item.width|| 30 , height: item.width || 30, padding: 0}">
              <div class="ant-upload-list-item-info">
                <span>
                  <a :href="item.value" target="_blank" rel="noopener noreferrer" class="ant-upload-list-item-thumbnail">
                    <img :src="item.value" :alt="item.value" :style="{width: item.width || 30, height: item.width || 30}">
                  </a>
                </span>
              </div>
              <span class="ant-upload-list-item-actions">
                <a-icon type="eye-o" @click="handlePreview(item.value)"/>
              </span>
            </div>
          </div>

        </div>
      </div>

      <div v-else-if="item.type === 'file'">
        <div v-if="isArray(item.value)">

          <div :key="src + index2" v-for="(src, index2) in item.value" class="ant-upload-list ant-upload-list-text">
            <div class="ant-upload-list-item ant-upload-list-item-done">
              <div class="ant-upload-list-item-info">
                <span>
                  <i class="anticon anticon-paper-clip" style="top:2px"><a-icon type="paper-clip" /></i>
                  <a target="_blank" rel="noopener noreferrer" class="ant-upload-list-item-name">
                    {{ src }}
                  </a>
                </span>
              </div>
            </div>
          </div>

        </div>
        <div v-else>

          <div class="ant-upload-list ant-upload-list-text">
            <div class="ant-upload-list-item ant-upload-list-item-done">
              <div class="ant-upload-list-item-info">
                <span>
                  <i class="anticon anticon-paper-clip" style="top:2px"><a-icon type="paper-clip" /></i>
                  <a target="_blank" rel="noopener noreferrer" class="ant-upload-list-item-name">
                    {{ item.value }}
                  </a>
                </span>
              </div>
            </div>
          </div>

        </div>
      </div>

      <div v-else-if="item.type === 'switch'">
        <a-switch defaultChecked :disabled="true" v-model="item.value" style="margin-bottom:5px"/>
      </div>

      <div v-else-if="item.type === 'password'">
        ******
      </div>

      <div v-else-if="item.type === 'tags'">
        <Select v-model="item.value" :options="item.options" :show-popover="showPopover && item.hiddenPopover != true"></Select>
      </div>

      <div v-else-if="item.type === 'select'">
        <div v-if="item.multiple != true">
          <Select v-model="item.value" :options="item.options" :show-popover="showPopover && item.hiddenPopover != true"></Select>
        </div>
        <div v-else>
          <MultipleSelect v-model="item.value" :options="item.options" :show-popover="showPopover && item.hiddenPopover != true"></MultipleSelect>
        </div>
      </div>

      <div v-else-if="item.type === 'multiple-select'">
        <MultipleSelect v-model="item.value" :options="item.options" :show-popover="showPopover && item.hiddenPopover != true"></MultipleSelect>
      </div>

      <div v-else-if="item.type === 'tags'">
        <MultipleSelect v-model="item.value" :options="item.options" :show-popover="showPopover && item.hiddenPopover != true"></MultipleSelect>
      </div>

      <div v-else-if="item.type === 'input'">
        <InputTextPermission v-if="item.permission" v-model="item.value" :show-popover="showPopover && item.hiddenPopover != true"></InputTextPermission>
        <InputText v-else v-model="item.value" :show-popover="showPopover && item.hiddenPopover != true"></InputText>
      </div>

      <div v-else-if="item.type === 'editor'">
        <Editor v-model="item.value" :show-popover="showPopover && item.hiddenPopover != true"></Editor>
      </div>

      <div v-else>
        <InputText v-model="item.value" :show-popover="showPopover && item.hiddenPopover != true"></InputText>
      </div>
    </DescriptionList>

  </div>
</template>

<script>

import DescriptionList from '@/components/DescriptionList/DescriptionList'
import Select from './Select'
import InputText from './InputText'
import MultipleSelect from './MultipleSelect'
import Editor from './Editor'
import InputTextPermission from './InputTextPermission'
import moment from 'moment'
import _ from 'lodash'

export default {
  name: 'DataViews',
  components: { DescriptionList, Select, InputText, Editor, InputTextPermission, MultipleSelect },
  props: {
    detail: {
      type: [String, Array, Object, Boolean, Number],
      required: true
    },
    value: {
      type: [String, Array, Object, Boolean, Number],
      required: false
    },
    showPopover: {
      type: Boolean,
      required: false,
      default: false
    },
    popoverWidth: {
      type: [Number, String],
      required: false,
      default: '300px'
    }
  },
  beforeCreate () {
    this.form = this.$form.createForm(this)
  },
  data () {
    const vm = this
    const item = vm.detail
    item.value = vm.value
    return {
      previewVisible: false,
      preview: '',
      item: _.cloneDeep(item),
      popoverCss: { maxWidth: vm.popoverWidth, wordWrap: 'break-word', wordBreak: 'normal' }
    }
  },
  watch: {
    'value': function (val) {
      this.item.value = val
    },
    'detail': function (val) {
      this.item = val
    }
  },
  computed: {
    isObj () {
      return (_.isArray(this.item) || _.isObject(this.item))
    }
  },
  mounted: function () {
    // this.item.value = this.value
  },
  methods: {
    isArray (v) {
      return _.isArray(v)
    },
    isEmpty (v) {
      return _.isEmpty(v)
    },
    handlePreview (url) {
      this.preview = url
      this.previewVisible = true
    },
    handleCancel () {
      this.previewVisible = false
    },
    copy (text) {
      this.$copyText(text).then(message => {
        console.log('copy', message)
        this.$message.success('复制完毕')
      }).catch(err => {
        console.log('copy.err', err)
        this.$message.error('复制失败')
      })
    }
  }
}

</script>

<style scoped>

.text {
  overflow: hidden; white-space: nowrap;text-overflow: ellipsis;word-break: break-all
}

</style>
