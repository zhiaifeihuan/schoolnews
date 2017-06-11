<template>
  <div>
    <div class='row maptab'>
      <div class="col-xs-2">
              <button class="btn btn-info tag" @click='enter(pos.xinxiao)'>新校
                <span class="glyphicon glyphicon-map-marker" aria-hidden="true"></span>
                </button>
            </div>
            <div class="col-xs-2">
              <button class="btn btn-info tag" @click='enter(pos.nanxiao)'>南校
                <span class="glyphicon glyphicon-map-marker" aria-hidden="true"></span>
              </button>
            </div>
            <div class="col-xs-2">
              <button class="btn btn-info tag" @click='enter(pos.benbu)'>本部
                <span class="glyphicon glyphicon-map-marker" aria-hidden="true"></span>
              </button>
            </div>
            <div class="col-xs-2">
              <button class="btn btn-info tag" @click='enter(pos.xiangya)'>湘雅
                <span class="glyphicon glyphicon-map-marker" aria-hidden="true"></span>
              </button>
            </div>
            <div class="col-xs-2">
              <button class="btn btn-info tag" @click='enter(pos.tiedao)'>铁道
                <span class="glyphicon glyphicon-map-marker" aria-hidden="true"></span>
              </button>
            </div>
              <button @click='create_new_event' class="btn btn-danger col-xs-2">发布 
                <span class="glyphicon glyphicon-plus" aria-hidden="true"></span>
              </button>
    </div>
    <baidu-map class='bm' 
    :center='position.center' 
    @zoomend='synczoom' 
    @moveend='sync' 
    :scroll-wheel-zoom='scroll' 
    :zoom='position.zoom'
    :min-zoom=11
    ><bm-navigation 
    anchor='BMAP_ANCHOR_BOTTOM_RIGHT'
    :offset='{width: 10, height: 30}'
    >
    </bm-navigation>
    <bm-geolocation anchor="BMAP_ANCHOR_BOTTOM_LEFT" 
    :showAddressBar="true" 
    @locationError='locationError'
    @locationSuccess='locationError'
    ></bm-geolocation>
    <bm-copyright
    anchor="BMAP_ANCHOR_BOTTOM_RIGHT"
    :copyright="[{id: 1, content: '<a title=去Edward的主页 target=_blank href=\'http://blog.edwarddd.cn\' class=text-danger>© 2017 Edward CSU</a>'}]">
    </bm-copyright>
    <bm-marker v-if='centerlabel' 
    :icon="{url: './static/img/arrow.ico', size: {width: 64, height: 64}}" 
    :position="position.center" 
    :dragging='dragging' 
    animation="BMAP_ANIMATION_DROP" 
    @rightclick='get_pos'>
    <bm-info-window :show='tips'>拖动我选择地点，右键点我确认(手机长按)</bm-info-window>
    </bm-marker>
    <template v-for="item in init_event_data">
    <bm-marker 
    :key='item.eventid'
    :position='{lng: item.longitude, lat: item.latitude}' 
    @click='show_event($event,item)' 
    animation='BMAP_ANIMATION_BOUNCE'
    :icon="{url: '../../static/img/location.png', size: {width: 24, height: 24}}"
    >
    </bm-marker>
    <!-- marker和label的测试偏移，暂时不添加 :offset='{width: -23, height: 13}' -->
    <bm-label 
    :content='item.title' 
    :labelStyle="{color: '#111', fontSize : '15px', backgroundColor: 'transparent', borderColor: 'transparent'}"
    :position='{lng: item.longitude, lat: item.latitude}' 
    ></bm-label>
    </template>
    </baidu-map>
  </div>
</template>

<script>
  export default {
    data () {
      return {
        position: {// 点击定位一次后若鼠标拖动缩放   再点击相同地点  会出现错误
          center: {
            lng: 112.942658,
            lat: 28.164901
          },
          zoom: 15
        },
        dragging: true,
        scroll: true,
        centerlabel: false,
        tips: true,
        event_title: '',
        eventid_to_show: '',
        pos: {
          nanxiao: {
            lng: 112.942658,
            lat: 28.164901
          },
          xinxiao: {
            lng: 112.94644,
            lat: 28.154619
          },
          benbu: {
            lng: 112.935229,
            lat: 28.177018
          },
          xiangya: {
            lng: 112.949436,
            lat: 28.225955
          },
          tiedao: {
            lng: 112.999247,
            lat: 28.144036
          }
        },
        init_event_data: {}
      }
    },
    computed: {
      editing () {
        return this.$store.state.editing
      },
      ajax_data () {
        const type = this.$store.state.opt.params.type
        if (type === 'EVENT_INIT') {
          return {
            happentime1: '2000-01-01',
            happentime2: '2017-07-01'
          }
        } else if (type === 'EVENT_GETMESSAGE') {
          return {
            eventid: this.eventid_to_show
          }
        } else {
          return {}
        }
      }
    },
    mounted: function () {
      const a = this.$store
      a.commit('set_ajax', {
        t: 'EVENT_INIT',
        s: (data) => {
          this.init_event_data = data.data
        },
        f: (data) => {
          console.log(data)
        }
      })
      a.commit('ajax_data', this.ajax_data)
      a.dispatch('ajax_start').then(() => {
        a.dispatch('checklogin')
      }, (error) => {
        console.log(error)
      })
    },
    methods: {
      handler ({
        BMap,
        map
      }) {
        console.log(BMap, map)
        this.lng = 116.404
        this.lat = 39.915
      },
      sync (e) {
        this.position.center = e.target.getCenter()
      },
      synczoom (e) {
        this.position.zoom = e.target.getZoom()
      },
      enter (target) {
        setTimeout(() => {
          this.position.center = target
        }, 100)
        setTimeout(() => {
          this.position.zoom = 17.9
        }, 100)
        setTimeout(() => {
          this.position.zoom = 18
        }, 100)
      },
      create_new_event () {
        if (!this.$store.state.haslogin) {
          this.$swal({
            type: 'warning',
            text: '请先登录再进行操作'
          }).then(() => {
            this.$store.state.show_login = true
          }).catch(this.$swal.noop)
        } else
        if (!this.editing && this.centerlabel === false) {
          this.$swal.queue([{
            progressSteps: ['1', '2'],
            title: '第一步',
            text: '请输入事件标题',
            confirmButtonText: '下一步',
            input: 'text',
            preConfirm: (title) => {
              return new Promise((resolve, reject) => {
                if (title === '') {
                  reject('标题不能为空！')
                } else if (title.length > 15) {
                  reject('标题不能大于超过15个字符')
                } else {
                  resolve() // 默认参数有 input
                }
              })
            }
          }, {
            progressSteps: ['1', '2'],
            title: '第二步',
            text: '请在下面的地图中选取事件发生的地点',
            confirmButtonText: '去选取'
          }]).then((data) => {
            this.centerlabel = true
            this.event_title = data[0]
            setTimeout(() => {
              this.tips = false
            }, 3000)
          }).catch(this.$swal.noop)
        } else if (this.centerlabel === true) { // 当开始选择地点又还没右键确定的时候如果再点击发布
          this.$swal({
            type: 'warning',
            text: '请在下面的地图中选取事件发生的地点',
            confirmButtonText: '确定'
          })
        } else {
          this.$router.push('edit')
        }
      },
      get_pos (event) {
        this.$router.push({
          name: 'edit', // 只能用name来传params，如果用path数据就传不过去  也不知道为什么。
          params: {
            location: event.point,
            title: this.event_title
          }
        })
        this.$store.commit('startedit')
        this.centerlabel = false
        this.tips = true
      },
      show_event (event, item) {
        const a = this.$store
        a.commit('set_ajax', {
          t: 'EVENT_GETMESSAGE',
          s: function (data) {
            // this.$swal({
            //   title: data.data.title,
            //   width: 1500,
            //   confirmButtonText: '看完了',
            //   allowOutsideClick: false,
            //   html: data.data.happentime + data.data.event
            // })
            a.commit('set_current_event', data.data)
            a.state.show_currentevent = !a.state.show_currentevent
          },
          f: (data) => {
            console.log(data)
          }
        })
        this.eventid_to_show = item.eventid
        a.commit('ajax_data', this.ajax_data)
        a.dispatch('ajax_start').then((data) => {
        }, (error) => {
          console.log(error)
        })
      },
      locationError () {
        this.$swal({
          type: 'error',
          title: '定位失败',
          text: '请检查你的定位功能是否开启'
        })
      }
    }
  }
</script>

<style>
  .bm {
    position: absolute;
    top: 100px;
    left: 0px;
    right: 0px;
    bottom: 0px;
    box-shadow: 0 4px 5px 0 rgba(0, 0, 0, .14), 0 1px 10px 0 rgba(0, 0, 0, .12), 0 2px 4px -1px rgba(0, 0, 0, .2);
    -webkit-box-shadow: 0 4px 5px 0 rgba(0, 0, 0, .14), 0 1px 10px 0 rgba(0, 0, 0, .12), 0 2px 4px -1px rgba(0, 0, 0, .2)
  }
  .maptab {
    position: fixed;
    top: 61px;
    left: 0;
    right: 0;
  }
  .col-xs-2 {
    text-align: center
  }

  label { /*修正百度地图标签显示不完整(与bootstrap冲突)*/
    max-width:none
  }

    .tag {
        border-width: 3px;
        border-color: #5bc0de;
        border-bottom-color: #eee;
        border-style: dashed
    }
    @media only screen and (max-width: 767px){
        .tag {
            padding: 3px;
            border-width: 5px;
        }
        .bm {
          top: 171px;
        }
        .maptab {
          top: 132px;
        }

    }
</style>
