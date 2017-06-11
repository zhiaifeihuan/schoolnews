<template>
  <nav class="navbar navbar-default navbar-fixed-top">
    <div class="container">
      <!-- Brand and toggle get grouped for better mobile display -->
      <div class="navbar-header">
        <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1" aria-expanded="false">
                      <span class="sr-only">Toggle navigation</span>
                      <span class="icon-bar"></span>
                      <span class="icon-bar"></span>
                      <span class="icon-bar"></span>
        </button>
        <router-link to='/'>
        <a class="navbar-brand">中南最前线</a>
        </router-link>
      </div>
      <form class="navbar-form navbar-left" ref='search'>
        <div class="form-group">
          <input type="text" v-model="search_src" class="form-control" placeholder="搜索">
        </div>
      </form>
      <search
      :left='searchstyle.left'
      :top='searchstyle.top'
      :events='search_events'
      ></search>
      <!-- Collect the nav links, forms, and other content for toggling -->
      <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
        <ul class="nav navbar-nav navbar-right">
          <li v-if='!haslogin' @click='show("login")'><router-link :to='currenturl'>登录 </router-link></li>
          <li v-if='!haslogin' @click='show("regis")'><router-link :to='currenturl'>注册 </router-link></li>
          <li v-if='haslogin' @click='show_user'><router-link :to='currenturl'>个人信息 </router-link></li>
          <li v-if='haslogin' @click='logout'><router-link :to='currenturl'>退出 </router-link></li>
        </ul>
      </div>
    </div>
    <!-- /.container-fluid -->
  </nav>
</template>

<script>
  import _ from 'lodash'
  import search from '@/components/search'
  export default {
    components: {
      search
    },
    data () {
      return {
        currenturl: '',
        search_src: '',
        searchstyle: {
          left: '',
          top: ''
        },
        search_events: []
      }
    },
    mounted: function () {
      this.searchstyle.left = this.$refs.search.offsetLeft
      // this.searchstyle.width = this.$refs.search.clientWidth
      this.searchstyle.top = this.$refs.search.offsetTop + this.$refs.search.offsetHeight
      window.onresize = _.debounce((event) => {
        this.searchstyle.left = this.$refs.search.offsetLeft
        // this.searchstyle.width = this.$refs.search.clientWidth
        this.searchstyle.top = this.$refs.search.offsetTop + this.$refs.search.offsetHeight
      }, 500)
    },
    computed: { // 绑定更新ajax_data
      haslogin: function () {
        return this.$store.state.haslogin
      },
      ajax_data: function () {
        const type = this.$store.state.opt.params.type
        if (type === 'EVENT_SEARCH') {
          return {
            title: this.search_src
          }
        } else {
          return {}
        }
      }
    },
    methods: {
      show (a) {
        if (a === 'login') {
          this.$store.state.login_or_regis = true
        } else if (a === 'regis') {
          this.$store.state.login_or_regis = false
        }
        this.$store.state.show_login = true
      },
      logout () {
        this.$store.commit('set_ajax', {
          t: 'USER_QUIT',
          s: (data) => {
            this.$store.dispatch('checklogin')
          },
          f: (data) => {
            console.log(data)
            this.$store.dispatch('checklogin')
          }
        })
        this.$store.dispatch('ajax_start')
      },
      show_user () {
        this.$store.state.show_userinfo = !this.$store.state.show_userinfo
      },
      search () {
        const a = this.$store
        a.commit('set_ajax', {
          t: 'EVENT_SEARCH',
          s: (data) => {
            this.search_events = data.data
          },
          f: (data) => {
            console.log(data)
          }
        })
        a.commit('ajax_data', this.ajax_data)
        a.dispatch('ajax_start')
      }
    },
    watch: {
      $route: function () {
        this.currenturl = this.$route.path
      },
      search_src: _.debounce(function () { // 删除字符串为''时返回全部事件！错误 获取事件要返回点击次数 获取全部事件以时间排序
        this.search()
      }, 500)
    }
  }
</script>

<style scoped>
  .modal-dialog {
    top: 30%;
    padding-left: 50px;
    padding-right: 50px
  }
  #myModalLabel,
  #twobtn {
    text-align: center
  }
</style>
