<template>
  <div class='background'>
    <keep-alive>
      <transition name='slide' mode='in-out'>
  <router-view></router-view>
      </transition>
    </keep-alive>
    <event></event>
    <div v-show="show_login" id='login_zhezhao' class="zhezhao" @click='close_login'></div>
    <div v-show="show_userinfo" class="zhezhao" @click='close_userinfo'></div>
    <transition name='move'>
    <login  v-if='show_login'></login>
    </transition>
    <transition name='slide'>
    <userinfo v-if="show_userinfo"></userinfo>
    </transition>
    <div id='loading'>
    <img v-show="show_loading" 
    src='../../static/img/gears.svg' 
    ondragstart="return false;" 
    title="加载中,请稍等">
    </div>
  </div>
</template>

<script>
import event from '@/components/event'
import login from '@/components/login'
import userinfo from '@/components/userinfo'
export default {
  name: 'background',
  data () {
    return {
    }
  },
  components: {
    event,
    login,
    userinfo
  },
  computed: {
    show_currentevent: function () {
      return this.$store.state.show_currentevent
    },
    show_loading: function () {
      return this.$store.state.show_loading
    },
    show_login: function () {
      return this.$store.state.show_login
    },
    show_userinfo: function () {
      return this.$store.state.show_userinfo
    }
  },
  methods: {
    close_login () {
      this.$store.state.show_login = false
    },
    close_userinfo () {
      this.$store.state.show_userinfo = false
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.slide-enter-active {
    animation: slide-in .5s ease;
  }
  .slide-leave-active {
    animation: slide-out .5s ease;
  }
  @keyframes slide-in {
    0% {
      transform: translateX(-100%);
    }
    100% {
      transform: translateX(0);
    }
  }
  @keyframes slide-out {
    0% {
      transform: translateX(0);
    }
    100% {
      transform: translateX(-100%);
    }
  }

.move-enter-active {
  animation: move-in .5s linear;
}

.move-leave-active {
  animation: move-out .5s linear;
}

@keyframes move-in {
  0% {
    transform: scale(0) rotate(360deg);
  }
  100% {
  }
}

@keyframes move-out {
  0% {}
  100% {
    transform: scale(0) rotate(360deg);
  }
}

#login_zhezhao {
  z-index: 1044;
}

.background {
  position: absolute;
  width: 100%;
  top: 0px;
  bottom: 0px;
  background: #eee
}

#loading {
  position: fixed;
  left: 45%;
  top: 45%;
  z-index: 10000
}
</style>
