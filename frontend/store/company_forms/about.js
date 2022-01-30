const state = () => ({
  description: "",
  services: [],
  technologies: [],
  site: '',
  odss: [],
  socialMedias: []
});

const getters = {
  description: (s) => s.description,
  descriptionLong: (s) => s.description,
  services: (s) => s.services,
  technologies: (s) => s.technologies,
  site: (s) => s.site,
  odss: (s) => s.odss,
  socialMedias: (s) => s.socialMedias
};

const mutations = {
  setFormField: (s, { key, value }) => (s[key] = value),
  setErrors: (s, errors) => (s.errors = errors),
};

const actions = {
  setDescriptionLong: ({ commit }, value) =>
    commit("setFormField", { key: "description", value }),
  setServices: ({ commit }, value) =>
    commit("setFormField", { key: "services", value }),
  setTechnologies: ({ commit }, value) =>
    commit("setFormField", { key: "technologies", value }),
  setSite: ({ commit }, value) =>
    commit("setFormField", { key: "site", value }),
  setOdss: ({ commit }, value) =>
    commit("setFormField", { key: "odss", value }),
  setSocialMedias: ({ commit }, value) =>
    commit("setFormField", { key: "socialMedias", value })
};

const prepareSection = (obj) => ({
  about: {
    description: obj.descriptionLong,
    services: obj.services,
    technologies: obj.technologies,
    site: obj.site,
    odss: obj.odss,
    social_medias: obj.socialMedias
  }
});



export default {
  state,
  getters,
  mutations,
  actions,
  prepareSection
}
