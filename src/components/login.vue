<template>
<div class='perspective'>
    <transition name='fanzhuan' mode='out-in'>
      <div v-if='login_or_regis' class='loginframe' key='login'>
        <div class="close"><i @click='close'><span class="glyphicon glyphicon-remove" aria-hidden="true"></span></i></div>
        <img src="../../static/img/itachi.png">
        <div class="login">
          <div class="holder">
            <div class="text-muted">使用账号登录</div>
            <div class="mail-login">
              <input type="text" v-model="username" placeholder="输入账号" class="clear-input">
              <input type="password" v-model="password" placeholder="密码" class="clear-input">
              <a href="#" @click='login' class="btn btn18 rbtn">
                <span class="text"> 登录</span>
              </a>
            </div>
            <a class="reset-password red-link">忘记密码»</a>
            <div class="switch-back">还没有帐号？
              <a class="red-link" @click='change_dia'>点击注册»</a>
            </div>
          </div>
        </div>
      </div>
  
      <div v-else class='loginframe' key='regis'>
        <div class="close"><i @click='close'><span class="glyphicon glyphicon-remove" aria-hidden="true"></span></i></div>
        <img src="../../static/img/itachi.png">
        <div class="login">
          <div class="holder">
            <div class="with-line">注册新账号</div>
            <div class="mail-login">
              <input type="text" v-model="nickname" placeholder="输入昵称" class="clear-input">
              <input type="text" v-model="username" placeholder="输入账号" class="clear-input">
              <input type="password" v-model="password" placeholder="密码" class="clear-input">
              <a href="#" @click='regis' class="btn btn18 rbtn">
                <span class="text"> 注册</span>
              </a>
            </div>
            <div class="switch-back">已经有账号?
              <a class="red-link" @click='change_dia'>去登录»</a>
            </div>
          </div>
        </div>
      </div>
    </transition>
  </div>
</template>

<script>
export default {
  data: () => {
    return {
      username: '',
      password: '',
      nickname: ''
    }
  },
  computed: {
    show_login: function () {
      return this.$store.state.show_login
    },
    login_or_regis: function () {
      return this.$store.state.login_or_regis
    },
    ajax_data: function () {
      const type = this.$store.state.opt.params.type
      if (type === 'USER_REGISTE') {
        return {
          username: this.username,
          password: this.password,
          nickname: this.nickname
        }
      } else if (type === 'USER_LOGIN') {
        return {
          username: this.username,
          password: this.password
        }
      } else {
        return {}
      }
    }
  },
  methods: {
    close () {
      this.$store.state.show_login = false
    },
    change_dia () {
      this.$store.state.login_or_regis = !this.$store.state.login_or_regis
    },
    login () {
      this.$store.commit('set_ajax', {
        t: 'USER_LOGIN',
        s: (data) => {
          this.$swal({
            type: 'success',
            title: data.message,
            timer: 1000,
            text: '1S后自动消失'
          }).then((data) => {
            this.$store.state.show_login = false
            this.$store.dispatch('checklogin')
          }, () => {
            this.$store.state.show_login = false
            this.$store.dispatch('checklogin')
          })
        },
        f: (data) => {
          console.log(data)
          this.$swal(data.message)
        }
      })
      this.$store.commit('ajax_data', this.ajax_data)
      this.$store.dispatch('ajax_start')
    },
    regis () {
      this.$store.commit('set_ajax', {
        t: 'USER_REGISTE',
        s: (data) => {
          this.$swal(data.message).then((data) => {
            this.$store.state.show_login = false
            this.$store.dispatch('checklogin')
          }, () => {
            this.$store.state.show_login = false
            this.$store.dispatch('checklogin')
          })
        },
        f: (data) => {
          console.log(data)
          this.$swal(data.message)
        }
      })
      this.$store.commit('ajax_data', this.ajax_data)
      this.$store.dispatch('ajax_start')
    }
  }
}
</script>

<style scoped>
.fanzhuan-enter-active {
  animation: fanzhuan-in .5s linear;
}

.fanzhuan-leave-active {
  animation: fanzhuan-out .5s linear;
}

@keyframes fanzhuan-in {
  0% {
    transform: rotateY(-83deg);
  }
  100% {}
}

@keyframes fanzhuan-out {
  0% {}
  100% {
    transform: rotateY(97deg);
  }
}


.perspective {
  perspective: 1060px;
  -webkit-perspective: 1060px;
  position: absolute;
  top: 50%;
  left: 50%;
  z-index: 1045;
}


.loginframe {
  position: absolute;
  top: 50%;
  left: 50%;
  width: 520px;
  background: #fff;
  margin: -155px 0 0 -260px;
  text-align: center;
  padding: 30px 0 20px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, .3);
  border-radius: 4px;
}

.loginframe .login .holder {
  margin-top: 20px;
}

.loginframe .holder {
  width: 286px;
  margin: 0 auto;
  position: relative;
}

.with-line {
  font-size: 16px;
  color: #999;
  margin: 0 auto;
  position: relative;
  text-align: center;
}


.loginframe .login .mail-login {
  margin-top: 15px;
}

.loginframe .login .reset-password {
  position: absolute;
  left: 0;
  bottom: 0;
}

.loginframe a {
  cursor: pointer;
}

.red-link {
  color: #9A0000;
}

a {
  color: #222;
  text-decoration: none;
  outline: 0;
}

.loginframe .login .switch-back {
  text-align: right;
  margin: 10px 0;
}

.loginframe .login .mail-login .clear-input {
  margin-bottom: 10px;
}

.loginframe .clear-input {
  box-sizing: border-box;
  width: 100%;
}

.clear-input {
  display: inline-block;
  padding: 0 10px;
  height: 36px;
  font-size: 16px;
  line-height: 1;
  color: #777;
  background: #FCFCFC;
  border: 1px solid #CCC;
  border-radius: 3px;
  width: 250px;
  box-shadow: inset 0 1px 2px rgba(0, 0, 0, .05);
  transition: color .2s linear, border-color .3s linear;
  -webkit-transition: color .2s linear, border-color .3s linear;
}

input,
textarea,
select {
  font-family: inherit;
  font-size: inherit;
  font-weight: inherit;
  resize: none;
  outline: 0;
}

.loginframe .login .mail-login .btn {
  display: block;
}

.loginframe a {
  cursor: pointer;
}

.btn18 {
  font-size: 18px;
  padding: 0 15px;
  border-radius: 3px;
  height: 36px;
  line-height: 36px;
}

.rbtn {
  background: #E53E49;
  background: linear-gradient( #E53E49, #D43636);
  box-shadow: inset 0 1px 0 rgba(255, 255, 255, .08), 0 1px 0 rgba(255, 255, 255, .3);
  text-shadow: 0 -1px 0 rgba(0, 0, 0, .1);
  color: #fff;
  border: 1px solid #C90000;
}

.loginframe .close {
    width: 36px;
    height: 36px;
    position: absolute;
    right: 0;
    top: 0;
    cursor: pointer;
}

.loginframe .close i {
    width: 20px;
    height: 20px;
    position: absolute;
    left: 50%;
    top: 50%;
    margin: -10px 0 0 -10px;
}
</style>

