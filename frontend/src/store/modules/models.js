import { listAll } from '@/api/model'


const state = {
  models: []
}

const mutations = {
  SET_MODELS(state, models) {
    state.models = models;
  }
}

const actions = {
  fetchModels({ commit }) {
    return new Promise(async (resolve, reject) => {
     await listAll(true).then(response => {
        commit('SET_MODELS', response.data)
        resolve(response.data);
      }).catch(error => {
        reject(error)
      })
    })
  }
}


const getters = {
  models: state => state.models
}

export default {
  namespaced: true,
  state,
  mutations,
  actions,
  getters
};
