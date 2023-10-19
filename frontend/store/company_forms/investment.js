const state = () => ({
  received: "",
  investments: [],
  investmentsValues: {
    own: "R$ 0,00",
    angel: "R$ 0,00",
    venture: "R$ 0,00",
    equity: "R$ 0,00",
    pipe: "R$ 0,00",
    others: "R$ 0,00",
  },
});

const getters = {
  received: (s) => s.received,
  investments: (s) => s.investments,
  investmentsValues: (s) => s.investmentsValues,
};

const mutations = {
  setFormField: (s, { key, value }) => (s[key] = value),
  setErrors: (s, errors) => (s.errors = errors),
};

const actions = {
  setReceived: ({ commit }, value) =>
    commit("setFormField", { key: "received", value }),
  setInvestments: ({ commit }, value) =>
    commit("setFormField", { key: "investments", value }),
  setInvestmentsValues: ({ commit }, value) =>
    commit("setFormField", { key: "investmentsValues", value }),
};

const prepareSection = (obj) => {
  return {
    investment: {
      received: obj.received,
      investments: obj.investments,
      own: obj.investmentsValues.own,
      angel: obj.investmentsValues.angel,
      venture: obj.investmentsValues.venture,
      equity: obj.investmentsValues.equity,
      pipe: obj.investmentsValues.pipe,
      others: obj.investmentsValues.others,
    }
  };
};

export default {
  state,
  getters,
  mutations,
  actions,
  prepareSection
}
