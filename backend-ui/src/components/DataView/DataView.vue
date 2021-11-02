<template>
  <div>

    <a-modal :visible="previewVisible" :footer="null" @cancel="handleCancel">
      <img style="width: 100%" :src="preview" />
    </a-modal>

    <a-card :title="title">
      <div :key="index" v-for="(item, index) in dataSource">
        <DescriptionList
          v-if="item.type !== 'hidden'"
          :title="item.label"
        >
          <div v-if="item.type === 'image'">
            <div v-if="isArray(item.value)">

              <div :key="src + index2" v-for="(src, index2) in item.value" class="ant-upload-list ant-upload-list-picture-card">
                <div class="ant-upload-list-item ant-upload-list-item-done">
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

              <div class="ant-upload-list ant-upload-list-picture-card">
                <div class="ant-upload-list-item ant-upload-list-item-done">
                  <div class="ant-upload-list-item-info">
                    <span>
                      <a :href="item.value" target="_blank" rel="noopener noreferrer" class="ant-upload-list-item-thumbnail">
                        <img :src="item.value" :alt="item.value">
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

          <div v-else-if="item.type === 'editor'">
            <div v-html="item.value"></div>
          </div>

          <div v-else>
            {{ item.value }}
          </div>
          <DataView v-if="!isEmpty(item.children)" :title="item.label" :dataSource="item.children"></DataView>
          <a-divider />
        </DescriptionList>

      </div>

      <slot name="item"></slot>
    </a-card>

    <slot name="footer"></slot>
  </div>
</template>

<script>

import DescriptionList from '@/components/DescriptionList/DescriptionList'
import moment from 'moment'
import _ from 'lodash'

export default {
  name: 'DataView',
  components: { DescriptionList, 'DataView': this },
  props: {
    dataSource: {
      type: [Array, Object],
      required: true
    },
    title: {
      type: String,
      default: 'title'
    },
    value: {
      type: [String, Array, Object],
      required: false
    }
  },
  beforeCreate () {
    this.form = this.$form.createForm(this)
  },
  data () {
    return {
      previewVisible: false,
      preview: ''
    }
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
    }
  }
}

</script>
