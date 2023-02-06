import Vue from 'vue'
import VueRouter from 'vue-router'
import DeepFakeDetector from '@/views/DeepFakeDetector.vue'
import DetectorPage from '@/views/DetectorPage.vue'

Vue.use(VueRouter)

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
    path: '/signUp/:page',
    name: 'SignUp',
    component: () => import('@/views/SignUp.vue')
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes: routes
})
export default router
