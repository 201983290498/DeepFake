import Vuex from 'vuex'
import Vue from 'vue'

Vue.use(Vuex)
const store = new Vuex.Store({
  state: {
    Authorization: localStorage.getItem('Authorization') ? localStorage.getItem('Authorization') : '',
    data: localStorage.getItem('data') ? localStorage.getItem('data') : '{}',
    status: localStorage.getItem('status') ? localStorage.getItem('status') : 'false'
  },
  mutations: {
    changeLogin (state, token) {
      state.Authorization = token
      localStorage.setItem('Authorization', token)
    },
    saveData (state, data) {
      state.data = data
      localStorage.setItem('data', JSON.stringify(data))
    },
    changeStatus (state, status) {
      state.status = status
      localStorage.setItem('status', JSON.stringify(status))
    }
  }
})

export default store
