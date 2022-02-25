const state = () => ({
  revenuesLastUpdatedAt: new Date(),
  financeValue: "R$ 0,00",
});

const getters = {
  revenuesLastUpdatedAt: (s) => s.revenuesLastUpdatedAt,
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
    last_update: updateRevenuesDate(obj)
  },
});

const updateRevenuesDate = (obj) => {
  const wasUpdated = obj.financeValue !== "R$ 0,00";
  return wasUpdated ? new Date() : obj.revenuesLastUpdatedAt;
};

export default {
  state,
  getters,
  mutations,
  actions,
  prepareSection
}
