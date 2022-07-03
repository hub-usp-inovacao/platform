const state = () => ({
  unities: []
})

const getters = {
  unities: (s) => s.unities
}

const mutations = {
  setUnities: (s, newList) => s.unities = newList,
}

const actions = {
  setUnities: ({ commit }, newList) => commit("setUnities", newList),

  loadInitialBond: ({ commit }, { unities }) => {
    commit("setUnities", unities)
  }
}

const prepareSection = (obj) => ({
  bond: {
    unities: obj.unities
  }
})

export default { state, getters, mutations, actions, prepareSection }
