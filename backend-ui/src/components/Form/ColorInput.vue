<template>
  <div>
    <div class="colorMain">

      <a-input v-model="inputValue" @change="valueChange" :style="{width: '100px', marginRight: '5px', marginLeft: '5px', color: inputValue}">

        <div slot="prefix" >
          <input type="color" v-model="inputValue" @change="valueChange" class="colorInput"/>
        </div>

        <a-tooltip slot="suffix" title="清空">
          <a-icon type="close" style="color: rgba(0,0,0,.45)" @click="cleanValue()"/>
        </a-tooltip>

      </a-input>
      <a-button @click="resetValue()">重置</a-button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'ColorInput',
  props: {
    value: {
      type: [String, Number],
      required: false,
      default: function () {
        return null
      }
    },
    size: {
      type: [String],
      required: false,
      default: ''
    }
  },
  data () {
    const vm = this
    return {
      oldValue: vm.value,
      inputValue: vm.value
    }
  },
  watch: {
    'inputValue': function (val) {
    },
    'value': function (val) {
      this.inputValue = this.value
      this.emitValue()
    }
  },
  methods: {
    valueChange () {
      this.emitValue()
    },

    emitValue () {
      this.$emit('input', this.inputValue)
      this.$emit('change', this.inputValue)
    },
    cleanValue () {
      this.inputValue = ''
      this.emitValue()
    },
    resetValue () {
      this.inputValue = this.oldValue
      this.emitValue()
    }
  }
}
</script>

<style scoped>

.colorMain {
  display: flex;flex-wrap: nowrap;
  flex-direction: row;
  width: 100%;
  margin-left: 5px
}

/deep/ .colorMain .ant-input-prefix {
  left: 5px;
}
/deep/ .colorMain .ant-input-suffix {
  right: 5px;
}

.colorInput {
  padding: 0px;
  border: 0px;
  width: 20px;
}

/deep/ .colorMain  .ant-input-affix-wrapper .ant-input:not(:first-child) {
  padding-left: 25px;
}

/deep/ .colorMain  .ant-input-affix-wrapper .ant-input:not(:last-child) {
  padding-right: 19px;
}

</style>
