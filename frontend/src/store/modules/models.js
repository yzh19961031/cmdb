import { listAll } from "@/api/model";

const state = {
  models: [],
  modelsFlag: false, //是否已经按照模型添加动态路由，表示判断位
};

const mutations = {
  SET_MODELS(state, models) {
    state.models = models;
  },
  SET_MODELSFLAG(state, modelsFlag) {
    state.modelsFlag = modelsFlag;
  },
};

const actions = {
  fetchModels({ commit }) {
    return new Promise(async (resolve, reject) => {
      await listAll(true)
        .then((response) => {
          commit("SET_MODELS", response.data);
          commit("SET_MODELSFLAG", true);
          resolve(response.data);
        })
        .catch((error) => {
          reject(error);
        });
    });
  },
};

const getters = {
  models: (state) => state.models,
};

export default {
  namespaced: true,
  state,
  mutations,
  actions,
  getters,
};
