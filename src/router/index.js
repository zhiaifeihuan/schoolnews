import Vue from 'vue'
import Router from 'vue-router'
import background from '@/components/background'
import nav from '@/components/nav'
import map from '@/components/map'
import editor from '@/components/editor'

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'background',
      components: {
        default: background,
        nav: nav
      },
      children: [{
        path: '',
        components: {
          default: map
        }
      },
      {
        path: 'edit',
        name: 'edit',
        components: {
          default: editor
        }
      }]
    }
  ]
})
