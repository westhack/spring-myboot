<template>
  <div>
    <draggable
      :list="inputValue"
      class="dragArea"
      @update="onDragItemEnd"
      :options="{animation: 120, filter: '.drag__nomove' }">
      <div
        v-for="(item, index) in inputValue"
        :key="index"
        style="display: flex; padding: 5px 10px"
      >
        <div style="width: 80%">
          <a-input-group compact>
            <a-input
              @change="valueChange"
              v-model="item['label']"
              placeholder="名称"
              style="width: 30%;"
            />

            <a-input
              @change="valueChange"
              v-model="item['value']"
              placeholder="值"
              style="width: 70%;"
            />
          </a-input-group>
        </div>
        <div style="width: 20%">
          <ColorInput v-model="item['color']"></ColorInput>
        </div>

        <div style="margin-left: 5px">
          <a-icon
            v-if="inputValue.length > 1"
            class="dynamic-delete-button"
            type="minus-circle-o"
            :disabled="inputValue.length === 1"
            @click="() => remove(index)"
          />
        </div>
      </div>
    </draggable>
    <div>
      <a-button type="dashed" style="width: 100%;" @click="add">
        <a-icon type="plus" /> 添加
      </a-button>
    </div>
  </div>
</template>

<script>

import _ from 'lodash'
import ColorInput from './ColorInput'
import draggable from 'vuedraggable'

export default {
  name: 'DictInput',
  components: {
    ColorInput,
    draggable
  },
  props: {
    value: {
      type: [String, Array],
      required: false,
      default: function () {
        return []
      }
    },
    isColor: {
      type: Boolean,
      default: true
    }
  },
  data () {
    const vm = this
    const value = vm.parseValue(vm.value)
    return {
      keys: [],
      inputValue: value
    }
  },
  beforeCreate () {
  },
  watch: {
    'inputValue': function (val) {
    },
    'value': function (val) {
      this.inputValue = this.parseValue(val)
      this.emitValue()
    }
  },
  methods: {
    remove (index) {
      const newInputValue = this.inputValue.slice()
      newInputValue.splice(index, 1)
      this.inputValue = newInputValue

      this.emitValue()
    },

    add () {
      const val = {
        label: '',
        value: '',
        color: ''
      }

      this.inputValue.push(val)
    },

    valueChange () {
      this.emitValue()
    },

    emitValue () {
      this.$emit('input', this.inputValue)
      this.$emit('change', this.inputValue)
    },

    parseValue (val) {
      let value = val
      if (_.isArray(value)) {
        // this.$emit('input', value)
        // this.$emit('change', value)
        return value
      }

      if (_.isObject(value)) {
        value = _.values(value)
        this.$emit('input', value)
        // this.$emit('change', value)
        return value
      }

      if (value == '') {
        this.$emit('input', [])
        return []
      }

      if (_.isString(value)) {
        value = JSON.parse(value)
        this.$emit('input', value)
        return value
      }
      return val
    },
    onDragItemEnd () {

    }
  }
}
</script>
<style scoped>
.dynamic-delete-button {
  cursor: pointer;
  position: relative;
  top: 4px;
  font-size: 24px;
  color: #999;
  transition: all 0.3s;
}
.dynamic-delete-button:hover {
  color: #777;
}
.dynamic-delete-button[disabled] {
  cursor: not-allowed;
  opacity: 0.5;
}
</style>
