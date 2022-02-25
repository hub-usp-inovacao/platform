const state = () => ({
  partners: [],
  collaboratorsLastUpdatedAt: new Date(),
  numberOfCLTEmployees: "",
  numberOfPJColaborators: "",
  numberOfInterns: "",
});

const getters = {
  collaboratorsLastUpdatedAt: (s) => s.collaboratorsLastUpdatedAt,
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
      last_update: updateCollaboratorsDate(obj)
    }
});

const updateCollaboratorsDate = (obj) => {
  const keys = [
    "numberOfCLTEmployees",
    "numberOfPJColaborators",
    "numberOfInterns",
  ];
  const wasUpdated = keys.some((key) => obj[key]);

  return wasUpdated ? new Date() : obj.collaboratorsLastUpdatedAt;
};

export default {
  state,
  getters,
  mutations,
  actions,
  prepareSection
}
