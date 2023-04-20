const state = () => ({
  name: "",
  corporateName: "",
  year: "",
  size: "",
  cnpj: "",
  cnae: "",
  registry_status: "",
  phones: [],
  emails: [],
  address: {
    venue: "",
    neighborhood: "",
    city: [],
    state: "",
    cep: "",
  },
  company_nature: "",
});

const getters = {
  name: (s) => s.name,
  corporateName: (s) => s.corporateName,
  year: (s) => s.year,
  size: (s) => s.size,
  cnpj: (s) => s.cnpj,
  cnae: (s) => s.cnae,
  registry_status: (s) => s.registry_status,
  phones: (s) => s.phones,
  emails: (s) => s.emails,
  address: (s) => s.address,
  venue: (s) => s.address.venue,
  neighborhood: (s) => s.address.neighborhood,
  city: (s) => s.address.city,
  state: (s) => s.address.state,
  cep: (s) => s.address.cep,
  company_nature: (s) => s.company_nature,
};

const mutations = {
  setFormField: (s, { key, value }) => (s[key] = value),
  setErrors: (s, errors) => (s.errors = errors),
};

const actions = {
  setName: ({ commit }, value) =>
    commit("setFormField", { key: "name", value }),
  setCorporateName: ({ commit }, value) =>
    commit("setFormField", { key: "corporateName", value }),
  setYear: ({ commit }, value) =>
    commit("setFormField", { key: "year", value }),
  setSize: ({ commit }, value) =>
    commit("setFormField", { key: "size", value }),
  setCnpj: ({ commit }, value) =>
    commit("setFormField", { key: "cnpj", value }),
  setCnae: ({ commit }, value) =>
    commit("setFormField", { key: "cnae", value }),
  setRegistryStatus: ({ commit }, value) =>
    commit("setFormField", { key: "registry_status", value }),
  setPhones: ({ commit }, value) =>
    commit("setFormField", { key: "phones", value }),
  setEmails: ({ commit }, value) =>
    commit("setFormField", { key: "emails", value }),
  setAddress: ({ commit }, value) =>
    commit("setFormField", { key: "address", value }),
  setVenue: ({ commit, getters }, value) =>
    commit("setFormField", {
      key: "address",
      value: { ...getters.address, venue: value },
    }),
  setNeighborhood: ({ commit, getters }, value) =>
    commit("setFormField", {
      key: "address",
      value: { ...getters.address, neighborhood: value },
    }),
  setCity: ({ commit, getters }, value) =>
    commit("setFormField", {
      key: "address",
      value: { ...getters.address, city: value },
    }),
  setState: ({ commit, getters }, value) =>
    commit("setFormField", {
      key: "address",
      value: { ...getters.address, state: value },
    }),
  setCep: ({ commit, getters }, value) =>
    commit("setFormField", {
      key: "address",
      value: { ...getters.address, cep: value },
    }),
  setCompanyNature: ({ commit }, value) =>
    commit("setFormField", { key: "company_nature", value }),
};

const prepareSection = (obj) => ({
  company_data: {
    cnpj: obj.cnpj,
    public_name: obj.name,
    corporate_name: obj.corporateName,
    year: obj.year,
    size: obj.size,
    cnae: obj.cnae,
    registry_status: obj.registry_status,
    phones: obj.phones,
    emails: obj.emails,
    street: obj.address.venue,
    neighborhood: obj.neighborhood,
    city: obj.city,
    state: obj.state,
    zipcode: obj.cep,
    company_nature: obj.company_nature,
  },
});

export default {
  state,
  getters,
  mutations,
  actions,
  prepareSection,
};
