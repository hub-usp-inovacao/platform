const state = () => ({
  description: "",
  services: [],
  technologies: [],
  site: "",
  odss: [],
  socialMedias: [],
  logo: "",
  logoFile: null,
  linkedin: "",
  instagram: "",
  facebook: "",
  youtube: "",
});

const getters = {
  description: (s) => s.description,
  descriptionLong: (s) => s.description,
  services: (s) => s.services,
  technologies: (s) => s.technologies,
  site: (s) => s.site,
  odss: (s) => s.odss,
  socialMedias: (s) => s.socialMedias,
  logo: (s) => s.logo,
  logoFile: (s) => s.logoFile,
  linkedin: (s) => s.linkedin,
  instagram: (s) => s.instagram,
  facebook: (s) => s.facebook,
  youtube: (s) => s.youtube,
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
    commit("setFormField", { key: "socialMedias", value }),
  setLogo: ({ commit }, value) =>
    commit("setFormField", { key: "logo", value }),
  setLogoFile: ({ commit }, value) =>
    commit("setFormField", { key: "logoFile", value }),
  setLinkedin: ({ commit }, value) =>
    commit("setFormField", { key: "linkedin", value }),
  setInstagram: ({ commit }, value) =>
    commit("setFormField", { key: "instagram", value }),
  setFacebook: ({ commit }, value) =>
    commit("setFormField", { key: "facebook", value }),
  setYoutube: ({ commit }, value) =>
    commit("setFormField", { key: "youtube", value }),
};

const prepareSection = (obj) => ({
  about_company: {
  setCompanyType: ({ commit }, value) =>
    commit("setFormField", { key: "companyType", value }),
  setIsUnicorn: ({ commit }, value) =>
    commit("setFormField", { key: "isUnicorn", value }),
    description: obj.descriptionLong,
    services: obj.services,
    technologies: obj.technologies,
    site: obj.site,
    odss: obj.odss,
    social_medias: obj.socialMedias,
    logo: obj.logo,
    linkedin: obj.linkedin,
    instagram: obj.instagram,
    facebook: obj.facebook,
    youtube: obj.youtube,
  },
});

export default {
  state,
  getters,
  mutations,
  actions,
  prepareSection,
};
