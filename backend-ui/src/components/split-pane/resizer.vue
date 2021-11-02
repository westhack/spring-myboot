<template>
  <div :class="classes">
    <div class="vertical-drawer-drag-move-trigger" v-if="split == 'vertical'">
      <div class="vertical-drawer-drag-move-trigger-point"><i></i><i></i><i></i></div>
    </div>
    <div class="horizontal-drawer-drag-move-trigger" v-if="split == 'horizontal'">
      <div class="horizontal-drawer-drag-move-trigger-point"><i></i><i></i><i></i></div>
    </div>
  </div>
</template>

<script>
export default {
  props: {
    split: {
      validator (value) {
        return ['vertical', 'horizontal'].indexOf(value) >= 0
      },
      required: true
    },
    className: String
  },
  computed: {
    classes () {
      const classes = ['splitter-pane-resizer', this.split, this.className]
      return classes.join(' ')
    }
  }
}
</script>

<style scoped>
.splitter-pane-resizer {
  -moz-box-sizing: border-box;
	-webkit-box-sizing: border-box;
	box-sizing: border-box;
	background: #bfbfbf;
	position: absolute;
	opacity: .2;
	z-index: 1;
	-moz-background-clip: padding;
	-webkit-background-clip: padding;
	background-clip: padding-box;
}

.splitter-pane-resizer.horizontal {
  height: 11px;
	margin: -5px 0;
	border-top: 5px solid rgba(255, 255, 255, 0);
	border-bottom: 5px solid rgba(255, 255, 255, 0);
	cursor: row-resize;
	width: 100%;
}

.splitter-pane-resizer.vertical {
  width: 11px;
	height: 100%;
	margin-left: -5px;
	border-left: 5px solid rgba(255, 255, 255, 0);
	border-right: 5px solid rgba(255, 255, 255, 0);
	cursor: col-resize;
}

.vertical-drawer-drag-move-trigger {
  width: 8px;
  height: 100px;
  line-height: 100px;
  position: absolute;
  top: 50%;
  background: #f3f3f3;
  -webkit-transform: translate(-50%,-50%);
  -ms-transform: translate(-50%,-50%);
  transform: translate(-50%,-50%);
  border-radius: 4px;
  -webkit-box-shadow:0 0 1px 1px rgba(0,0,0,.2);
  box-shadow:0 0 1px 1px rgba(0,0,0,.2);
  cursor: col-resize;
}

.vertical-drawer-drag-move-trigger-point {
  display: inline-block;
  width: 50%;
  -webkit-transform: translateX(50%);
  -ms-transform: translateX(50%);
  transform: translateX(50%)
}

.vertical-drawer-drag-move-trigger-point i {
  display: block;
  border-bottom: 1px solid #000c17;
  padding-bottom: 2px
}

.horizontal-drawer-drag-move-trigger {
  width: 100px;
  height: 8px;
  position: absolute;
  left: 50%;
  background: #f3f3f3;
  -webkit-transform: translate(-50%,-50%);
  -ms-transform: translate(-50%,-50%);
  transform: translate(-50%,-50%);
  border-radius: 4px;
  -webkit-box-shadow:0 0 1px 1px rgba(0,0,0,.2);
  box-shadow:0 0 1px 1px rgba(0,0,0,.2);
  cursor: row-resize;
}

.horizontal-drawer-drag-move-trigger-point {
  display: flex;
  height: 50%;
  -webkit-transform: translateY(50%);
  transform: translateY(50%);
  flex-direction: row;
  justify-content: center
}

.horizontal-drawer-drag-move-trigger-point i {
  display: block;
  border-left: 1px solid #333;
  padding-left: 3px
}
</style>
