const state = () => ({
  wantsDna: false,
  dnaContactName: "",
  dnaContactEmail: "",
  truthfulInformations: false,
  permissions: [],
});

const getters = {
  wantsDna: (s) => s.wantsDna,
  dnaContactName: (s) => s.dnaContactName,
  dnaContactEmail: (s) => s.dnaContactEmail,
  truthfulInformations: (s) => s.truthfulInformations,
  permissions: (s) => s.permissions
};

const mutations = {
  setFormField: (s, { key, value }) => {
    console.log('changing ', key)
    console.log('to ', value)
    s[key] = value
  },
  setErrors: (s, errors) => (s.errors = errors),
};

const actions = {
  setWantsDna: ({ commit }, value) =>
    commit("setFormField", { key: "wantsDna", value }),
  setDnaContactName: ({ commit }, value) =>
    commit("setFormField", { key: "dnaContactName", value }),
  setDnaContactEmail: ({ commit }, value) =>
    commit("setFormField", { key: "dnaContactEmail", value }),
  setTruthfulInformations: ({ commit }, value) =>
    commit("setFormField", { key: "truthfulInformations", value }),
  setPermissions: ({ commit }, value) =>
    commit("setFormField", { key: "permissions", value }),
};

const prepareSection = (obj) => ({
  dna_usp_stamp: {
    wants: obj.wantsDna,
    name: obj.wantsDna ? obj.dnaContactName : "",
    email: obj.wantsDna ? obj.dnaContactEmail : "",
    truthful_informations: obj.truthfulInformations,
    permissions: obj.permissions,
  },
});

export default {
  state,
  getters,
  mutations,
  actions,
  prepareSection
}
