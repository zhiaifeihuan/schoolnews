<template>
  <div class='userinfo-main'>
  <img src='../../static/img/gou.png' 
  class="img-responsive center-block header-img">
  <h2 v-if='haslogin' class="text-center" @click='show_events=!show_events'>你好 {{nickname}}</h2>
  <h2 v-else id='logintips' @click='show_login' class="text-center">请先登录</h2>
  <div v-show="haslogin" class="my-message">
    <a @click='show_all'>
      <p class="p-1">{{count}}</p>
      我的发布
    </a>
    <div class='blank'></div>
    <a @click='show_hot'>
      <p class="p-1">{{count_2}}</p>
      被看最多
    </a>
  </div>
  <div v-if="haslogin && !show_events" class="my-content">
    <div class="info"><span class="spann">昵称</span> <input @focus='change_btn' @blur='change_btn' type="text" v-model="nickname"></div>
    <div class="info"><span class="spann">性别</span> <input @focus='change_btn' @blur='change_btn' type="text" v-model="gender"></div>
    <div class="info"><span class="spann">专业</span> <input @focus='change_btn' @blur='change_btn' type="text" v-model="major"></div>
    <div class="info"><span class="spann">身份</span> <input @focus='change_btn' @blur='change_btn' type="text" v-model="identity"></div>
    <div class="info"><span class="spann">电话</span> <input @focus='change_btn' @blur='change_btn' type="text" v-model="phone"></div>
    <div class="info"><span class="spann">简介</span> <input @focus='change_btn' @blur='change_btn' type="text" v-model="introduce"></div>
    <button v-show="show_changebtn" class="btn btn-primary" @click='set_userinfo'>修改</button>
  </div>
  <div v-else>
    <div class="my-content" id='userinfo-events'>
    <a v-for='item in got_events' class="event-title"
    @mouseout='set_deletebtn_id(close_deletebtn)'
    @mouseover='set_deletebtn_id(item.eventid)'
    ><div>
    <a class="event-title-title" @click='show_event($event,item)'> {{item.title}} </a> <span class="glyphicon glyphicon-eye-open" aria-hidden="true"></span>
     {{item.viewd}} <span style="cursor: default;display: inline" class="glyphicon glyphicon-remove text-danger" aria-hidden="true"
     v-show="item.eventid === delete_btn_id"
     @click='delete_event($event,item)'
     ></span></div>
     </a>
  </div>
  <div class="show-more">
    <button v-if='!has_got_all' class="btn btn-primary loadmore" @click='show_more'>查看更多</button>
    <h3 v-else>没有更多了.....</h3>
  </div>
  </div>
  </div>
</template>

<script>
export default {
  data: function () {
    return {
      gender: '',
      introduce: '',
      identity: '',
      major: '',
      phone: '',
      nickname: '',
      count: '',
      has_got_all: false,
      get_event_by_index: 0,
      show_changebtn: false,
      got_events: [],
      show_events: false,
      eventid_to_show: '',
      delete_btn_id: false,
      close_deletebtn: false
    }
  },
  computed: {
    ajax_data: function () {
      const type = this.$store.state.opt.params.type
      if (type === 'USER_ALTER') {
        return {
          gender: this.gender,
          introduce: this.introduce,
          identity: this.identity,
          major: this.major,
          phone: this.phone,
          nickname: this.nickname
        }
      } else if (type === 'USER_GETEVENT') {
        return {
          param: this.get_event_by_index
        }
      } else if (type === 'EVENT_GETMESSAGE') {
        return {
          eventid: this.eventid_to_show
        }
      } else if (type === 'EVENT_DELETE') {
        return {
          eventid: this.eventid_to_show
        }
      } else {
        return {}
      }
    },
    haslogin: function () {
      return this.$store.state.haslogin
    },
    count_2: function () {
      if (this.count >= 5) {
        return 5
      } else if (this.count < 5 && this.count >= 0) {
        return this.count
      }
    }
  },
  created: function () {
    if (this.$store.state.haslogin) {
      this.get_userinfo()
    }
  },
  methods: {
    get_userinfo () {
      const a = this.$store
      a.commit('set_ajax', {
        t: 'USER_GETMESSAGE',
        s: (data) => {
          const userinfo = data.data
          this.gender = userinfo.gender
          this.major = userinfo.major
          this.phone = userinfo.phone
          this.nickname = userinfo.nickname
          this.introduce = userinfo.introduce
          this.identity = userinfo.identity
          this.count = userinfo.count
        },
        f: (data) => {
          console.log(data)
        }
      })
      a.dispatch('ajax_start')
    },
    set_userinfo () {
      const a = this.$store
      a.commit('set_ajax', {
        t: 'USER_ALTER',
        s: (data) => {
          this.$swal({
            type: 'success',
            title: '修改成功',
            timer: 2000,
            text: '2S后自动消失'
          }).catch(this.$swal.noop)
        },
        f: (data) => {
          console.log(data)
        }
      })
      a.commit('ajax_data', this.ajax_data)
      a.dispatch('ajax_start').then(() => {
        this.get_userinfo()
      })
    },
    change_btn () {
      setTimeout(() => {
        this.show_changebtn = !this.show_changebtn
      }, 100)
    },
    show_login () {
      this.$store.state.show_login = true
    },
    show_hot () {
      this.get_event_by_index = 0
      this.has_got_all = true
      this.show_events = true
      const a = this.$store
      a.commit('set_ajax', {
        t: 'EVENT_GETHOTMESSAGE',
        s: (data) => {
          this.got_events = data.data
        },
        f: (data) => {
          console.log(data)
        }
      })
      a.commit('ajax_data', this.ajax_data)
      a.dispatch('ajax_start')
    },
    show_all () {
      this.show_events = true
      this.has_got_all = false
      this.get_event_by_index = 0
      this.got_events = []
      this.show_more()
    },
    show_more () {
      const a = this.$store
      const that = this
      a.commit('set_ajax', {
        t: 'USER_GETEVENT',
        s: (data) => {
          const events = data.data
          if (events.length < 5) {
            that.has_got_all = true
          }
          for (var key in events) {
            if (events.hasOwnProperty(key)) {
              var element = events[key]
              that.got_events.push(element)
            }
          }
        },
        f: (data) => {
          console.log(data)
        }
      })
      a.commit('ajax_data', this.ajax_data)
      a.dispatch('ajax_start').then(() => {
        this.get_event_by_index = this.get_event_by_index + 1
      }, (error) => {
        console.log(error)
      })
    },
    show_event (event, item) {
      const a = this.$store
      a.commit('set_ajax', {
        t: 'EVENT_GETMESSAGE',
        s: function (data) {
          a.commit('set_current_event', data.data)
          a.state.show_currentevent = !a.state.show_currentevent
        },
        f: (data) => {
          console.log(data)
        }
      })
      this.eventid_to_show = item.eventid
      a.commit('ajax_data', this.ajax_data)
      a.dispatch('ajax_start')
    },
    delete_event (event, item) {
      const a = this.$store
      a.commit('set_ajax', {
        t: 'EVENT_DELETE',
        s: (data) => {
        },
        f: (data) => {
          console.log(data)
        }
      })
      this.eventid_to_show = item.eventid
      a.commit('ajax_data', this.ajax_data)
      a.dispatch('ajax_start').then(() => {
        this.show_all()
      })
    },
    set_deletebtn_id (item) {
      this.delete_btn_id = item
    }
  }
}
</script>

<style scoped>
.userinfo-main {
  position: absolute;
  top: 0px;
  left: 0px;
  bottom: 0px;
  width: 400px;
  background: #5bc0de;
  z-index: 1043;
  opacity: 1;
  color: #fff;
  opacity: .8;
  overflow: hidden;
}

@media only screen and (max-width: 450px) {
  .userinfo-main {
    width: 300px;
  }
}


.header-img {
  margin-top: 5%;
  margin-bottom: 5%;
}

h2 {
  color: #000;
}

#logintips {
  cursor: pointer;
}

.my-message {
  height: 8.5rem;
  text-align: center;
  padding-top: 5%;
}

a {
  height: 100%;
  display: inline-block;
  cursor: pointer;
  color: #fff;
  font-size: 1.3rem;
}

.blank {
  width: 35px;
  display: inline-block;
}

.p-1 {
  font-size: 2.5rem;
  margin-bottom: 5px;
}

.info {
  margin: 1.5rem;
}

.spann {
  font-size: 2rem;
  margin-right: 20px;
  position: relative;
  top: 1px;
}

input {
  color: #fff;
  font-size: 1.5rem;
  background: #5bc0de;
  border: 0;
}

.my-content {
  padding-top: 5%;
  padding-bottom: 5%;
  text-align: center;
}

#userinfo-events {
  overflow-y: scroll;
  -webkit-overflow-scrolling: touch;
  margin-right: -1.5rem;
  padding-right: 1.5rem;
  max-height: 22rem;
}

.show-more {
  text-align: center;
}

.event-title {
  font-size: 1.9rem;
  position: relative;
  margin-top: 1.5rem;
  display: block;
}

.event-title-title {
  font-size: 1.9rem;
  position: relative;
  margin-top: 1.5rem;
}

.loadmore {
  margin-top: 1rem;
}



@media only screen and (max-height: 600px) {
  .info {
    margin: 1rem;
  }
  .event-title {
    margin-top: 1rem;
  }
  .event-title-title {
    margin-top: 1rem;
  }
  #userinfo-events {
    max-height: 17rem;
  }
}

@media only screen and (max-width: 600px) {
  .event-title {
    font-size: 1.5rem;
  }
  .event-title-title {
    margin-top: 1rem;
  }
}
</style>
