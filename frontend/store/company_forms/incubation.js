const state = () => ({
  incubated: "",
  ecosystem: "",
});

const getters = {
  incubated: (s) => s.incubated,
  ecosystems: (s) => s.ecosystem,
};

const mutations = {
  setFormField: (s, { key, value }) => (s[key] = value),
  setErrors: (s, errors) => (s.errors = errors),
};

const actions = {
  setIncubated: ({ commit }, value) =>
    commit("setFormField", { key: "incubated", value }),
  setEcosystems: ({ commit }, value) =>
    commit("setFormField", { key: "ecosystem", value }),
};

const prepareSection = (obj) => ({
  incubation: {
    was_incubated: obj.incubated,
    ecosystem: obj.ecosystems,
  },
});

export default {
  state,
  getters,
  mutations,
  actions,
  prepareSection,
};
