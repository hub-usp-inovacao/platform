const state = () => ({
  partners: [],
  numberOfCLTEmployees: "",
  numberOfPJColaborators: "",
  numberOfInterns: "",
});

const getters = {
  numberOfCLTEmployees: (s) => s.numberOfCLTEmployees,
  numberOfPJColaborators: (s) => s.numberOfPJColaborators,
  numberOfInterns: (s) => s.numberOfInterns,
};

export const mutations = {
  setFormField: (s, { key, value }) => (s[key] = value),
  setErrors: (s, errors) => (s.errors = errors),
};

export const actions = {
  setNumberOfCLTEmployees: ({ commit }, value) =>
    commit("setFormField", { key: "numberOfCLTEmployees", value }),
  setNumberOfPJColaborators: ({ commit }, value) =>
    commit("setFormField", { key: "numberOfPJColaborators", value }),
  setNumberOfInterns: ({ commit }, value) =>
    commit("setFormField", { key: "numberOfInterns", value }),
};

const prepareSection = (obj) => ({
    staff: {
      number_of_CLT_employees: obj.numberOfCLTEmployees,
      number_of_PJ_colaborators: obj.numberOfPJColaborators,
      number_of_interns: obj.numberOfInterns,
    }
});

export default {
  state,
  getters,
  mutations,
  actions,
  prepareSection
}
