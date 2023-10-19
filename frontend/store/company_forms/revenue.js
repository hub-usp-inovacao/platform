const state = () => ({
  lastYear: "R$ 0,00",
});

const getters = {
  lastYear: (s) => s.lastYear,
};

const mutations = {
  setFormField: (s, { key, value }) => (s[key] = value),
  setErrors: (s, errors) => (s.errors = errors),
}

const actions = {
  setLastYear: ({ commit }, value) =>
    commit("setFormField", { key: "lastYear", value }),
};

const prepareSection = (obj) => ({
  revenue: {
    last_year: obj.lastYear,
  },
});


export default {
  state,
  getters,
  mutations,
  actions,
  prepareSection
}
