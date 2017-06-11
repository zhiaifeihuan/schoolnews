<template>
    <div class="editor">
      <div class="row">
      <button class="btn btn-info col-xs-6" @click='upload_event'>点此提交 </button>
      <button class="btn btn-danger  col-xs-6" @click='stopedit'>停止编辑 </button>
      </div>
      <mavon-editor id='editor' @change='sync'></mavon-editor>
    </div>
</template>

<script>
  // Local Registration
  import {
    mavonEditor
  } from 'mavon-editor'
  import 'mavon-editor/dist/css/index.css'
  export default {
    name: 'editor',
    data () {
      return {
        value: '',
        event_location: {},
        event_title: ''
      }
    },
    mounted: function () {
      if (!this.$store.state.editing) {
        this.$swal({
          type: 'error',
          title: '错误',
          text: '尚未进入编辑状态，请通过点击发布按钮来编辑'
        }).then(() => {
          this.$router.push('/')
          this.$destroy() // 清除自身的一切数据 此处会弹出控制台警告  不过没影响
        }, () => {
          this.$router.push('/')
          this.$destroy() // 清除自身的一切数据 此处会弹出控制台警告  不过没影响
        })
      }
      this.event_location = this.$route.params.location
      this.event_title = this.$route.params.title
    },
    computed: {
      ajax_data () {
        const type = this.$store.state.opt.params.type
        if (type === 'EVENT_UPLOAD') {
          return {
            title: this.event_title,
            event: this.value,
            happentime: '2012-12-03 03:23:13',
            longitude: this.event_location.lng,
            latitude: this.event_location.lat
          }
        } else if (type === 'EVENT_GETMESSAGE') {
          return {}
        } else {
          return {}
        }
      }
    },
    components: {
      mavonEditor
      // or 'mavon-editor': mavonEditor
    },
    methods: {
      sync (src, html) {
        this.value = html
      },
      upload_event () {
        const that = this
        if (this.location === {}) {
          this.$swal('事件的地点还没选择')
          this.$router.push('/')
        } else {
          this.$swal({
            title: '上传事件',
            type: 'question',
            text: '你确定要上传吗？要不再看看？',
            showCancelButton: 'true',
            confirmButtonText: '就要上传',
            cancelButtonText: '再看看吧',
            showLoaderOnConfirm: true,
            preConfirm: () => {
              return new Promise((resolve, reject) => {
                that.$store.commit('set_ajax', {
                  t: 'EVENT_UPLOAD',
                  s: (data) => {
                  },
                  f: (data) => {
                    console.log(data)
                  }
                })
                this.$store.commit('ajax_data', this.ajax_data)
                this.$store.dispatch('ajax_start').then((data) => {
                  setTimeout(() => { resolve(data) }, 500)
                }, (data) => {
                  reject(data)
                })
              })
            }
          }).then((data) => {
            this.$swal({
              title: '上传成功',
              type: 'success',
              text: '快去主页看看吧'
            }).then(() => {
              that.$store.commit('stopedit')
              that.$router.push('/')
              that.$destroy() // 清除自身的一切数据
            })
          }, (noop) => {
            console.log(noop)
          })
        }
      },
      stopedit () {
        this.$swal({
          title: '危险',
          text: '你确定要停止编辑吗？你的一切改动将不会被保存',
          type: 'error',
          allowOutsideClick: false,
          allowEscapeKey: false,
          showCancelButton: true,
          confirmButtonText: '真的不想写了！',
          cancelButtonText: '好吧继续编辑',
          reverseButtons: true,
          focusCancel: true
        }).then(() => {
          this.$store.commit('stopedit')
          this.$router.push('/')
          this.$destroy() // 清除自身的一切数据
        }).catch(this.$swal.noop)
      }
    }
  }
</script>

<style>
  .editor {
    position: absolute;
    top: 66px;
    left: 0px;
    right: 0px;
    bottom: 0px;
    /*box-shadow: 0 4px 5px 0 rgba(0, 0, 0, .14), 0 1px 10px 0 rgba(0, 0, 0, .12), 0 2px 4px -1px rgba(0, 0, 0, .2);
    -webkit-box-shadow: 0 4px 5px 0 rgba(0, 0, 0, .14), 0 1px 10px 0 rgba(0, 0, 0, .12), 0 2px 4px -1px rgba(0, 0, 0, .2);*/
    overflow: hidden;
    z-index: 0
  }

  #editor {
    position: absolute;
    top: 34px;
    left: 0px;
    right: 0px;
    bottom: 0px;
  }

  @media only screen and (max-width: 767px) {
    .editor {
      top: 137px;
    }
  }

</style>
