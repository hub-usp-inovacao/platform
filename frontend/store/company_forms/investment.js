const state = () => ({
  received: "",
  investments: [],
  investmentsValues: {
    own: "R$ 0,00",
    angel: "R$ 0,00",
    ventureCapital: "R$ 0,00",
    privateEquity: "R$ 0,00",
    pipeFapesp: "R$ 0,00",
    other: "R$ 0,00",
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
  const {
    own,
    angel,
    ventureCapital: venture,
    privateEquity: equity,
    pipeFapesp: pipe,
    other: others
  } = obj.investmentsValues

  return {
    investment: {
      received: obj.received,
      investments: obj.investments,
      own,
      angel,
      venture,
      equity,
      pipe,
      others,
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
