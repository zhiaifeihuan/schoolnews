<template>
  <div class="search-main"
  :style='style'
  >
  <h2 v-for="item in events" 
  :class="[item.eventid === item_be_chosen ? 'bg-info' : 'text-primary']"
  @mouseover='choose_item(item.eventid)'
  @click='show_event($event,item)'
  >{{item.title}}</h2>
  </div>
</template>

<script>
export default {
  props: [
    'left',
    'top',
    'events'
  ],
  data: function () {
    return {
      eventid_to_show: '',
      item_be_chosen: ''
    }
  },
  computed: {
    style: function () {
      return {
        left: this.left + 'px',
        top: this.top + 'px',
        borderBottom: this.events.length ? 'thin solid' : '',
        borderLeft: this.events.length ? 'thin solid' : '',
        borderRight: this.events.length ? 'thin solid' : ''
      }
    },
    ajax_data: function () {
      const type = this.$store.state.opt.params.type
      if (type === 'EVENT_GETMESSAGE') {
        return {
          eventid: this.eventid_to_show
        }
      }
    }
  },
  methods: {
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
      a.dispatch('ajax_start').then((data) => {
      }, (error) => {
        console.log(error)
      })
    },
    choose_item (currenteventid) {
      this.item_be_chosen = currenteventid
    }
  }
}
</script>

<style scoped>
.search-main {
  position: fixed;
  height: auto;
  width: auto;
  background: #fff;
  max-height: 300px;
  overflow-y: auto;
  -webkit-overflow-scrolling: touch;
}

h2 {
  margin: 0px;
  padding: 10px;
  cursor: pointer;
}
</style>
