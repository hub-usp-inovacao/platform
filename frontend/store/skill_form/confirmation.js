const state = () => ({
  interest: undefined,
  allow: null,
  truthful: null,
})

const getters = {
  interest: (s) => s.interest,
  allow: (s) => s.allow,
  truthful: (s) => s.truthful,
}

const mutations = {
  setInterest: (s, value) => s.interest = value,
  setAllow: (s, value) => s.allow = value,
  setTruthful: (s, value) => s.truthful = value,
}

const actions = {
  setInterest: ({ commit }, value) => commit("setInterest", value),
  setAllow: ({ commit }, value) => commit("setAllow", value),
  setTruthful: ({ commit }, value) => commit("setTruthful", value),
}

const prepareSection = (obj) => ({
  confirmation: {
    interest: obj.interest,
    allow: obj.allow,
    truthful: obj.truthful
  }
})

export default { state, getters, mutations, actions, prepareSection }
