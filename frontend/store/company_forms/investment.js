const state = () => ({
  investmentsLastUpdatedAt: new Date(),
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
  investmentsLastUpdatedAt: (s) => s.investmentsLastUpdatedAt,
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
      received: obj.receivedInvestments,
      own,
      angel,
      venture,
      equity,
      pipe,
      others,
      last_update: updateInvestmentsDate(obj)
    }
  };
};

const updateInvestmentsDate = (obj) => {
  const wasUpdated = obj.receivedInvestments;
  return wasUpdated ? new Date() : obj.investmentsLastUpdatedAt;
};

export default {
  state,
  getters,
  mutations,
  actions,
  prepareSection
}
