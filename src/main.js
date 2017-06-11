// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import BaiduMap from 'vue-baidu-map'
import '../static/js/bootstrap.js'
import Vuex from 'vuex'
import connect from '../static/js/Connector.js'
import VueSweetAlert from 'vue-sweetalert'

Vue.config.productionTip = false
Vue.use(BaiduMap, {
  ak: 'b0yahNnOZyaNowxE2FNPr9FTZwn9sDY1'
})
Vue.use(Vuex)
Vue.use(VueSweetAlert)

const store = new Vuex.Store({
  state: {
    editing: false,
    haslogin: false,
    show_currentevent: false,
    current_event: {},
    show_loading: false,
    show_login: false,
    show_userinfo: false,
    login_or_regis: true,
    opt: {
      url: 'DesktopService',
      params: {
        type: '',
        data: {}
      },
      success: function () {},
      failure: function () {}
    }
  },
  mutations: {
    startedit (state) {
      state.editing = true
    },
    stopedit (state) {
      state.editing = false
    },
    ajax_type (state, type) {
      state.opt.params.type = type
    },
    ajax_data (state, data) {
      state.opt.params.data = data
    },
    ajax_success (state, successfunc) {
      state.opt.success = successfunc
    },
    ajax_failure (state, failurefunc) {
      state.opt.failure = failurefunc
    },
    set_ajax (state, data) {
      state.opt.params.type = data.t
      state.opt.success = data.s
      state.opt.failure = data.f
    },
    login (state) {
      state.haslogin = true
    },
    logout (state) {
      state.haslogin = false
    },
    set_current_event (state, event) {
      state.current_event = event
    }
  },
  actions: {
    ajax_start (context) {
      context.state.show_loading = true
      return new Promise((resolve, reject) => {
        connect(context.state.opt).then((data) => {
          context.state.show_loading = false
          resolve(data)
        }, (data) => {
          context.state.show_loading = false
          reject(data)
        })
      })
    },
    checklogin (context) {
      context.state.opt.params.type = 'USER_CHECK'
      context.state.opt.params.data = {}
      context.state.opt.success = (data) => {
        context.state.haslogin = true
      }
      context.state.opt.failure = (data) => {
        context.state.haslogin = false
      }
      context.dispatch('ajax_start').then(() => {
      }, (data) => console.log(data, '失败'))
    }
  }
})

/* eslint-disable no-new */
new Vue({
  el: '#el',
  router,
  store: store,
  template: '<App/>',
  components: { App }
})
