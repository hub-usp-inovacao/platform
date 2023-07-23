const state = () => ({
  receivedInvestments: false,
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
  receivedInvestments: (s) => s.receivedInvestments,
  investments: (s) => s.investments,
  investmentsValues: (s) => s.investmentsValues,
};

const mutations = {
  setFormField: (s, { key, value }) => (s[key] = value),
  setErrors: (s, errors) => (s.errors = errors),
};

const actions = {
  setReceivedInvestments: ({ commit }, value) =>
    commit("setFormField", { key: "receivedInvestments", value }),
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
      received_investment: obj.receivedInvestments,
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
