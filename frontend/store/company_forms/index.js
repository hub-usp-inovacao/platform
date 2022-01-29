import dnaUspStamp from './dna_usp_stamp'
import companyData from './company_data'

export const state = () => ({
  partners: [],
  description: {
    long: "",
  },
  technologies: [],
  services: [],
  ods: [],
  socialMedias: [],
  url: "",
  logo: undefined,
  collaboratorsLastUpdatedAt: new Date(),
  investmentsLastUpdatedAt: new Date(),
  revenuesLastUpdatedAt: new Date(),
  numberOfCTLEmployees: "",
  numberOfPJColaborators: "",
  numberOfInterns: "",
  incubated: "",
  ecosystems: [],
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
  financeValue: "R$ 0,00",
  errors: [],

  ...dnaUspStamp.state(),
  ...companyData.state(),
});

export const getters = {
  partners: (s) => s.partners,
  description: (s) => s.description,
  descriptionLong: (s) => s.description.long,
  technologies: (s) => s.technologies,
  services: (s) => s.services,
  ods: (s) => s.ods,
  socialMedias: (s) => s.socialMedias,
  url: (s) => s.url,
  logo: (s) => s.logo,
  collaboratorsLastUpdatedAt: (s) => s.collaboratorsLastUpdatedAt,
  investmentsLastUpdatedAt: (s) => s.investmentsLastUpdatedAt,
  revenuesLastUpdatedAt: (s) => s.revenuesLastUpdatedAt,
  numberOfCTLEmployees: (s) => s.numberOfCTLEmployees,
  numberOfPJColaborators: (s) => s.numberOfPJColaborators,
  numberOfInterns: (s) => s.numberOfInterns,
  incubated: (s) => s.incubated,
  ecosystems: (s) => s.ecosystems,
  receivedInvestments: (s) => s.receivedInvestments,
  investments: (s) => s.investments,
  investmentsValues: (s) => s.investmentsValues,
  financeValue: (s) => s.financeValue,
  errors: (s) => s.errors,

  ...dnaUspStamp.getters,
  ...companyData.getters,
};

export const mutations = {
  setFormField: (s, { key, value }) => (s[key] = value),
  setErrors: (s, errors) => (s.errors = errors),

  ...dnaUspStamp.mutations,
  ...companyData.mutations,
};

export const actions = {
  ...dnaUspStamp.actions,
  ...companyData.actions,

  setPartners: ({ commit }, value) =>
    commit("setFormField", { key: "partners", value }),
  setDescriptionLong: ({ commit }, value) =>
    commit("setFormField", { key: "description", value: { long: value } }),
  setTechnologies: ({ commit }, value) =>
    commit("setFormField", { key: "technologies", value }),
  setServices: ({ commit }, value) =>
    commit("setFormField", { key: "services", value }),
  setOds: ({ commit }, value) => commit("setFormField", { key: "ods", value }),
  setSocialMedias: ({ commit }, value) =>
    commit("setFormField", { key: "socialMedias", value }),
  setUrl: ({ commit }, value) => commit("setFormField", { key: "url", value }),
  setLogo: ({ commit }, value) =>
    commit("setFormField", { key: "logo", value }),
  setNumberOfCTLEmployees: ({ commit }, value) =>
    commit("setFormField", { key: "numberOfCTLEmployees", value }),
  setNumberOfPJColaborators: ({ commit }, value) =>
    commit("setFormField", { key: "numberOfPJColaborators", value }),
  setNumberOfInterns: ({ commit }, value) =>
    commit("setFormField", { key: "numberOfInterns", value }),
  setIncubated: ({ commit }, value) =>
    commit("setFormField", { key: "incubated", value }),
  setEcosystems: ({ commit }, value) =>
    commit("setFormField", { key: "ecosystems", value }),
  setReceivedInvestments: ({ commit }, value) =>
    commit("setFormField", { key: "receivedInvestments", value }),
  setInvestments: ({ commit }, value) =>
    commit("setFormField", { key: "investments", value }),
  setInvestmentsValues: ({ commit }, value) =>
    commit("setFormField", { key: "investmentsValues", value }),
  setFinanceValue: ({ commit }, value) =>
    commit("setFormField", { key: "financeValue", value }),

  getCompanyData: async function({ commit, getters }) {
    const cnpj = getters.cnpj;
    const { status, message } = await this.$getCompanyData(cnpj);

    if (status !== "ok") {
      commit("setErrors", [message]);
    } else {
      commit("setErrors", []);

      Object.keys(message).forEach((key) => {
        const k = snakeToCamelCase(key);

        // eslint-disable-next-line no-prototype-builtins
        if (getters.hasOwnProperty(k)) {
          commit("setFormField", { key: k, value: message[key] });
        }
      });

      commit("setFormField", {
        key: "collaboratorsLastUpdatedAt",
        value: new Date(message["collaborators_last_updated_at"]),
      });
      commit("setFormField", {
        key: "investmentsLastUpdatedAt",
        value: new Date(message["investments_last_updated_at"]),
      });
      commit("setFormField", {
        key: "revenuesLastUpdatedAt",
        value: new Date(message["revenues_last_updated_at"]),
      });
      commit("setFormField", { key: "logo", value: undefined });
    }
  },

  updateCompanyForm: async function({ commit, getters }) {
    if (!getters.cnpj || !getters.name || getters.partners.length === 0) {
      commit("setErrors", [
        "É necessário informar o nome, CNPJ e pelo menos um sócio da empresa para atualizar os dados",
      ]);
      return false;
    }

    if (!getters.truthfulInformations) {
      commit("setErrors", [
        "É necessário declarar que as informações fornecidas são verdadeiras e que a empresa atende aos critérios estabelecidos",
      ]);
      return false;
    }

    const company = prepareCompanyObject(getters);
    const { errors } = await this.$updateCompanyData(company, getters.logo);

    if (errors) {
      commit("setErrors", errors);
      return false;
    }

    commit("setErrors", []);
    return true;
  },

};

const snakeToCamelCase = (key) => {
  return key
    .split("_")
    .map((value, index) => {
      if (index > 0) {
        let first = value[0];
        first = first.toUpperCase();
        value = first + value.slice(1);
      }

      return value;
    })
    .join("");
};

const prepareCompanyObject = (obj) => ({
  company: {
    ...companyData.prepareSection(obj),
    ...dnaUspStamp.prepareSection(obj),
  },
});

const updateCollaboratorsDate = (obj) => {
  const keys = [
    "numberOfCTLEmployees",
    "numberOfPJColaborators",
    "numberOfInterns",
  ];
  const wasUpdated = keys.some((key) => obj[key]);

  return wasUpdated ? new Date() : obj.collaboratorsLastUpdatedAt;
};

const updateRevenuesDate = (obj) => {
  const wasUpdated = obj.financeValue !== "R$ 0,00";
  return wasUpdated ? new Date() : obj.revenuesLastUpdatedAt;
};

const updateInvestmentsDate = (obj) => {
  const wasUpdated = obj.receivedInvestments;
  return wasUpdated ? new Date() : obj.investmentsLastUpdatedAt;
};
