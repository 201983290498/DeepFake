import Vuex from 'vuex'
import Vue from 'vue'

Vue.use(Vuex)

const store = new Vuex.Store({
  state: {
    Authorization: localStorage.getItem('Authorization') ? localStorage.getItem('Authorization') : '',
    data: {},
    status: false
  },
  mutations: {
    changeLogin (state, token) {
      state.Authorization = token
      localStorage.setItem('Authorization', token)
    },
    saveData (state, data) {
      state.data = data
    },
    changeStatus (state, status) {
      state.status = status
    }
  }
})

export default store
