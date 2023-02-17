import Vue from 'vue'
import VueRouter from 'vue-router'
import DeepFakeDetector from '@/views/DeepFakeDetector.vue'
import DetectorPage from '@/views/DetectorPage.vue'
import Vuex from 'vuex'

Vue.use(VueRouter)
Vue.use(Vuex)

const routes = [
  {
    path: '',
    redirect: '/detectorPage'
  },
  {
    path: '/detectorPage',
    name: 'DetectorPage',
    component: DetectorPage,
    children: [
      {
        path: 'deepfakeDetector',
        name: 'DeepFakeDetector',
        component: DeepFakeDetector
      },
      {
        path: 'normalDetector',
        name: 'NormalDetector',
        // route level code-splitting
        // this generates a separate chunk (about.[hash].js) for this route
        // which is lazy-loaded when the route is visited.
        component: () => import('@/views/NormalDetector.vue')
      },
      {
        path: '',
        redirect: 'deepfakeDetector'
      }
    ]
  },
  {
    path: '/signIn',
    name: 'SignIn',
    component: () => import('@/views/SignIn.vue')
  },
  {
    path: '/signUp',
    name: 'SignUp',
    component: () => import('@/views/SignUp.vue'),
    children: [
      {
        path: 'register',
        name: 'RegisterCard',
        component: () => import('@/components/account/RegisterCard.vue')
      },
      {
        path: 'forgetPwd',
        name: 'ForgetPwd',
        component: () => import('@/components/account/ForgetPwd.vue')
      },
      {
        path: '',
        redirect: 'register'
      }
    ]
  },
  {
    path: '/signUp',
    redirect: '/signUp/register'
  },
  {
    path: '/showBoard',
    name: 'ShowBoard',
    component: () => import('@/views/ShowBoard.vue'),
    children: [{
      path: 'forgetPwd',
      name: 'PersonalForgetPwd',
      component: () => import('@/components/dashboard/PersonalForgetPwd.vue')
    }]
  }
]
const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes: routes
})
// 添加一个路由守卫
router.beforeEach((to, from, next) => {
  const pattern = /^\/showBoard\/.*$/
  if (to.path === '/signIn') {
    next()
  } else if (pattern.test(to.path)) {
    const token = localStorage.getItem('Authorization')
    console.log(token)
    if (token === 'null' || token === '' || token === null) {
      next('/')
    } else {
      next()
    }
  } else {
    next()
  }
})
export default router
