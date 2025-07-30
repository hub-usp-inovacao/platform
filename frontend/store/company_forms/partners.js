const state = () => ({
  partners: [],
});

const getters = {
  partners: (s) => Array.isArray(s.partners) ? s.partners.filter((partner) => partner?.name) : [],
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
