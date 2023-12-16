const state = () => ({
  incubated: "",
  ecosystems: [],
});

const getters = {
  incubated: (s) => s.incubated,
  ecosystems: (s) => s.ecosystems,
};

const mutations = {
  setFormField: (s, { key, value }) => (s[key] = value),
  setErrors: (s, errors) => (s.errors = errors),
};

const actions = {
  setIncubated: ({ commit }, value) =>
    commit("setFormField", { key: "incubated", value }),
  setEcosystems: ({ commit }, value) => 
    commit("setFormField", { key: "ecosystems", value })
};

const prepareSection = (obj) => ({
  incubation: {
    was_incubated: obj.incubated,
    ecosystems: obj.ecosystems
  },
});

export default {
  state,
  getters,
  mutations,
  actions,
  prepareSection
}