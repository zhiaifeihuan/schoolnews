<template>
  <div>
    <transition name='show-zhezhao'>
      <div id='event-zhezhao' class='zhezhao' v-show='show_currentevent' @click='close'>
      </div>
    </transition>
    <transition name='slide-left'>
      <div class='event-main' v-show='show_currentevent' rel='event'>
        <div id='app' @touchmove.prevent>
          <ttt @toclose='close'>
            <template slot='header'>
                <div @click='close'>
                <h1>{{title}}</h1>
                <p>作者: {{author}}</p>
                <p>发布时间: {{happentime}}</p>
                <p>浏览量: {{viewd}}</p>
                </div>
</template>
<template slot='content'>
  <div v-html='content' ondragstart='return false;'>
  </div>
  <button class='btn btn-info' @click='close'>看完啦</button>
  <button class='btn btn-info' v-show="!show_changyan" @click='changyan'>我要说两句！</button>
  <div id="SOHUCS" v-show="show_changyan" :sid='eventid'></div>
</template>
          </ttt>
        </div>
      </div>
    </transition>
  </div>
</template>

<script>
  import ttt from '@/components/header'
  import moment from 'moment'
  export default {
    components: {
      ttt
    },
    data: () => {
      return {
        show_changyan: false
      }
    },
    computed: {
      show_currentevent: function () {
        return this.$store.state.show_currentevent
      },
      title: function () {
        return this.$store.state.current_event.title
      },
      content: function () {
        return this.$store.state.current_event.event
      },
      author: function () {
        return this.$store.state.current_event.nickname
      },
      happentime: function () {
        moment.locale('zh-cn')
        return moment(this.$store.state.current_event.happentime).fromNow()
      },
      viewd: function () {
        return this.$store.state.current_event.viewd
      },
      eventid: function () {
        return this.$store.state.current_event.eventid
      }
    },
    methods: {
      close () {
        this.$store.state.show_currentevent = false
        this.show_changyan = false
      },
      changyan () {
        this.show_changyan = true
        var appid = 'cyt3mHjK0'
        var conf = '0600c66d1760241687be2adb0583e310'
        var doc = document
        var s = doc.createElement('script')
        var h = doc.getElementsByTagName('head')[0] || doc.head || doc.documentElement
        s.type = 'text/javascript'
        s.charset = 'utf-8'
        s.src = 'http://assets.changyan.sohu.com/upload/changyan.js?conf=' + conf + '&appid=' + appid
        h.insertBefore(s, h.firstChild)
      }
    }
  }
</script>

<style>
  .zhezhao {
    position: fixed;
    height: 100%;
    width: 100%;
    background: #bbb;
    z-index: 1040;
    background: rgba(255, 255, 255, .3);
  }
  #event-zhezhao {
    z-index: 1048
  }
  .event-main {
    position: fixed;
    height: 100%;
    left: 20%;
    right: 20%;
    background: #fff;
    z-index: 1050;
    opacity: 1;
  }
  #app img {
    max-width: 100%;
    height: auto;
    display: block;
  }
  @media only screen and (max-width: 992px) {
    .event-main {
      left: 10%;
      right: 10%;
    }
  }
  @media only screen and (max-width: 768px) {
    .event-main {
      left: 0%;
      right: 0%;
    }
  }
  .slide-left-enter-active {
    animation: slide-left-in .5s ease
  }
  .slide-left-leave-active {
    animation: slide-left-out .5s ease
  }
  @keyframes slide-left-in {
    0% {
      transform: scale(0)
    }
    100% {
      transform: scale(1);
    }
  }
  @keyframes slide-left-out {
    0% {
      transform: scale(1);
    }
    100% {
      transform: scale(0);
    }
  }
  .show-zhezhao-leave-active {
    animation: show-zhezhao-out .4s ease
  }
  @keyframes show-zhezhao-out {
    0% {}
    100% {}
  }
</style>
