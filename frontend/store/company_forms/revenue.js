const state = () => ({
  financeValue: "R$ 0,00",
});

const getters = {
  financeValue: (s) => s.financeValue,
};

const mutations = {
  setFormField: (s, { key, value }) => (s[key] = value),
  setErrors: (s, errors) => (s.errors = errors),
}

const actions = {
  setFinanceValue: ({ commit }, value) =>
    commit("setFormField", { key: "financeValue", value }),
};

const prepareSection = (obj) => ({
  revenue: {
    last_year: obj.financeValue,
  },
});


export default {
  state,
  getters,
  mutations,
  actions,
  prepareSection
}
