<template>
    <div class="draggable-header-view"
    @mousedown="startDrag" @touchstart="startDrag"
    @mousemove="onDrag" @touchmove="onDrag"
    @mouseup="stopDrag" @touchend="stopDrag" 
    @mouseleave="stopDrag"
    :style='{width: width+"px"}'
    >
    <svg class="bg" width="100%" height="100%">
      <path :d="headerPath" fill="#5bc0de" @click='close'></path>
    </svg>
    <div class="header">
      <slot name="header"></slot>
    </div>
    <div class="content" :style="contentPosition" id='content'>
      <slot name="content"></slot>
    </div>
  </div>
</template>

<script>
import dynamics from 'dynamics.js'
export default {
  name: 'draggable-header-view',
  data: function () {
    return {
      dragging: false,
      // record drag start point
      c: {   // quadratic bezier control point
        x: 0,
        y: 180
      },
      start: { x: 0, y: 0 },
      width: '',
      current_scroll_top: 0
    }
  },
  created: function () {
    this.width = this.c.x = 800 // 防止初始化的时候弹出不存在headerpath的警告
  },
  watch: {
    show_currentevent: function () {
      this.width = this.$parent.$el.lastChild.offsetWidth // 一顿骚操作，总算得到了rel='event'背景的宽度
      this.c.x = this.width
    }
  },
  computed: {
    contentPosition: function () {
      var dy = this.c.y - 180
      var dampen = dy > 0 ? 2 : 4
      return {
        transform: 'translate3d(0,' + dy / dampen + 'px,0)',
        width: this.width + 'px'
      }
    },
    show_currentevent: function () {
      return this.$store.state.show_currentevent
    },
    headerPath: function () {
      return 'M0,0 L' + this.width + ',0 L' + this.width + ',180 ' +
      'Q' + this.c.x + ',' + this.c.y + ' 0,180'
    }
  },
  methods: {
    startDrag: function (e) {
      e = e.changedTouches ? e.changedTouches[0] : e
      this.dragging = true
      this.start.x = e.pageX
      this.start.y = e.pageY
      this.current_scroll_top = document.getElementById('content').scrollTop
    },
    onDrag: function (e) {
      e = e.changedTouches ? e.changedTouches[0] : e
      if (this.dragging) {
        this.c.x = this.width / 2 + (e.pageX - this.start.x)
        // dampen vertical drag by a factor
        var dy = e.pageY - this.start.y
        var dampen = dy > 0 ? 1.5 : 4
        this.c.y = 180 + dy / dampen
        document.getElementById('content').scrollTop = this.current_scroll_top - dy
      }
    },
    stopDrag: function () {
      if (this.dragging) {
        this.dragging = false
        dynamics.animate(this.c, {
          x: this.width / 2,
          y: 180
        }, {
          type: dynamics.spring,
          duration: 700,
          friction: 280
        })
      }
    },
    close () {
      this.$emit('toclose')
    }
  }
}
</script>

<style scoped>
h1 {
  font-weight: 300;
  font-size: 1.8em;
  margin-top: 0;
}
a {
  color: #fff;
}

@media only screen and (max-width: 992px) {
  h1 {
  font-weight: 300;
  font-size: 1.7em;
  margin-top: 0;
}
}

@media only screen and (max-width: 768px) {
  h1 {
  font-weight: 300;
  font-size: 1.6em;
  margin-top: 0;
}
}

@media only screen and (max-width: 420px) {
  h1 {
  font-weight: 300;
  font-size: 1.5em;
  margin-top: 0;
}
}

.draggable-header-view {
  background-color: #fff;
  box-shadow: 0 4px 16px rgba(0,0,0,.15);
  height: 100%;
  position: fixed;
  font-family: 'Roboto', Helvetica, Arial, sans-serif;
  color: #fff;
  font-size: 14px;
  font-weight: 300;
  -webkit-user-select: none;
  -moz-user-select: none;
  -ms-user-select: none;
  user-select: none;
}

.draggable-header-view .bg {
  position: absolute;
  top: 0;
  left: 0;
  z-index: 0;
}
.draggable-header-view .header, .draggable-header-view .content {
  position: absolute;
  z-index: 1;
  padding: 30px;
  box-sizing: border-box;
}
.draggable-header-view .header {
  height: 180px;
}
.draggable-header-view .content {
  color: #333;
  line-height: 1.5em;
  position: absolute;
  top: 180px;
  bottom: 0px;
  overflow: auto;
  -webkit-overflow-scrolling: touch;
  text-align: left;
}
</style>
