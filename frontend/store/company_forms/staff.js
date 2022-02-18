const state = () => ({
  partners: [],
  collaboratorsLastUpdatedAt: new Date(),
  numberOfCTLEmployees: "",
  numberOfPJColaborators: "",
  numberOfInterns: "",
});

const getters = {
  collaboratorsLastUpdatedAt: (s) => s.collaboratorsLastUpdatedAt,
  numberOfCTLEmployees: (s) => s.numberOfCTLEmployees,
  numberOfPJColaborators: (s) => s.numberOfPJColaborators,
  numberOfInterns: (s) => s.numberOfInterns,
};

export const mutations = {
  setFormField: (s, { key, value }) => (s[key] = value),
  setErrors: (s, errors) => (s.errors = errors),
};

export const actions = {
  setNumberOfCTLEmployees: ({ commit }, value) =>
    commit("setFormField", { key: "numberOfCTLEmployees", value }),
  setNumberOfPJColaborators: ({ commit }, value) =>
    commit("setFormField", { key: "numberOfPJColaborators", value }),
  setNumberOfInterns: ({ commit }, value) =>
    commit("setFormField", { key: "numberOfInterns", value }),
};

const prepareSection = (obj) => ({
    staff: {
      numberOfCTLEmployees: obj.numberOfCTLEmployees,
      numberOfPJColaborators: obj.numberOfPJColaborators,
      numberOfInterns: obj.numberOfInterns,
    }
});

export default {
  state,
  getters,
  mutations,
  actions,
  prepareSection
}
