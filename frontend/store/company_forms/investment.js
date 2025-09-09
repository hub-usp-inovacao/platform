export const state = () => ({
  received: "",
  investments: [],
  investmentsValues: {
    own: "R$ 0,00",
    angel: "R$ 0,00",
    venture: "R$ 0,00",
    equity: "R$ 0,00",
    pipe: "R$ 0,00",
    crowdfunding: "R$ 0,00",
    bndesFinep: "R$ 0,00",
    others: "R$ 0,00"
  }
});

export const getters = {
  received: (state) => state.received,
  investments: (state) => state.investments,
  investmentsValues: (state) => state.investmentsValues,
};

export const mutations = {
  setFormField: (state, { key, value }) => {
    state[key] = value;
  },
};

export const actions = {
  setReceived: ({ commit }, value) => {
    commit("setFormField", { key: "received", value });
  },
  setInvestments: ({ commit }, value) => {
    commit("setFormField", { key: "investments", value });
  },
  setInvestmentsValues: ({ commit }, value) => {
    commit("setFormField", { key: "investmentsValues", value });
  },
};

export default {
  state,
  getters,
  mutations,
  actions,
};
