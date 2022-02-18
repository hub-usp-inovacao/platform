const state = () => ({
  partners: [],
});

const getters = {
  partners: (s) => s.partners,
};

const mutations = {
  setFormField: (s, { key, value }) => (s[key] = value),
  setErrors: (s, errors) => (s.errors = errors),
};

const actions = {
  setPartners: ({ commit }, value) =>
    commit("setFormField", { key: "partners", value }),
};

const prepareSection = (obj) => ({
  partners: obj.partners,
});

export default {
  state,
  getters,
  mutations,
  actions,
  prepareSection
}
