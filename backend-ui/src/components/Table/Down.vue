<template>
  <div>
    <a-checkbox-group @change="filterDropdownChange" v-model="value">
      <a-menu>
        <a-menu-item
          style="margin-left: 10px;"
          v-for="(option, index) in options"
          :key="index"
        >
          <a-checkbox :value="option.dataIndex">{{ option.title }}</a-checkbox>
        </a-menu-item>
        <a-menu-divider />
      </a-menu>
    </a-checkbox-group>
    <a-menu>
      <a-menu-item>
        <a-checkbox value="all" name="all" v-model="all" @change="allChange">全部</a-checkbox>
      </a-menu-item>
    </a-menu>

  </div>
</template>

<script>
export default {
  name: 'Down',
  props: {
    options: {
      type: Array,
      required: true,
      default: () => {
        return []
      }
    }
  },
  data: () => {
    return {
      value: [],
      all: true
    }
  },
  created () {
    this.options.map((o) => {
      this.value.push(o.dataIndex)
    })
  },
  watch: {
    value: function (val) {
      if (this.value.length == this.options.length) {
        this.all = true
      } else {
        this.all = false
      }
    }
  },
  methods: {
    filterDropdownChange (val) {
      this.$emit('change', val)
      this.$emit('input', val)
    },
    allChange (val) {
      if (this.all) {
        this.value = []
        this.options.map((o) => {
          this.value.push(o.dataIndex)
        })
        this.$emit('change', this.value)
        this.$emit('input', this.value)
      }
    }
  }
}
</script>

<style scoped>

</style>
