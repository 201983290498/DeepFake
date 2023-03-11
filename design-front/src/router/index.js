import Vue from 'vue'
import VueRouter from 'vue-router'
import DeepFakeDetector from '@/viewPages/DeepFakeDetector.vue'
import DetectorPage from '@/viewPages/indexPage.vue'
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
        component: () => import('@/viewPages/NormalDetector.vue')
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
    component: () => import('@/viewPages/SignIn.vue')
  },
  {
    path: '/signUp',
    name: 'SignUp',
    component: () => import('@/viewPages/SignUp.vue'),
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
    component: () => import('@/viewPages/ShowBoard.vue'),
    children: [
      {
        path: 'forgetPwd',
        name: 'PersonalForgetPwd',
        component: () => import('@/components/dashboard/personal/PersonalForgetPwd.vue')
      },
      {
        path: 'personalInfo',
        name: 'PersonalInfo',
        component: () => import('@/components/dashboard/personal/PersonalInfo.vue')
      },
      {
        path: '',
        redirect: 'historyRecords'
      },
      {
        path: 'historyRecords',
        name: 'HistoryRecords',
        component: () => import('@/components/dashboard/history/HistoryRecords.vue')
      },
      {
        path: 'historyProjects',
        name: 'HistoryProjects',
        component: () => import('@/components/dashboard/history/HistoryProjects.vue')
      },
      {
        path: 'picBoard',
        name: 'PicBoard',
        component: () => import('@/components/dashboard/common/PicBoard.vue')
      },
      {
        path: 'addProject',
        name: 'AddProject',
        component: () => import('@/components/dashboard/common/AddProject.vue')
      }
    ]
  }
]
const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes: routes
})
// 添加一个路由守卫
router.beforeEach((to, from, next) => {
  const pattern = /^\/showBoard.*$/
  if (to.path === '/signIn') {
    next()
  } else if (pattern.test(to.path)) {
    const token = localStorage.getItem('Authorization')
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
