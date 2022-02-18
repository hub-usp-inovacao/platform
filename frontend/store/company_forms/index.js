import dnaUspStamp from './dna_usp_stamp'
import companyData from './company_data'
import about from './about'
import investment from './investment'
import revenue from './revenue'
import incubation from './incubation'
import partners from './partners'

export const state = () => ({
  collaboratorsLastUpdatedAt: new Date(),
  numberOfCTLEmployees: "",
  numberOfPJColaborators: "",
  numberOfInterns: "",
  errors: [],

  ...dnaUspStamp.state(),
  ...companyData.state(),
  ...about.state(),
  ...investment.state(),
  ...revenue.state(),
  ...incubation.state(),
  ...partners.state(),
});

export const getters = {
  collaboratorsLastUpdatedAt: (s) => s.collaboratorsLastUpdatedAt,
  numberOfCTLEmployees: (s) => s.numberOfCTLEmployees,
  numberOfPJColaborators: (s) => s.numberOfPJColaborators,
  numberOfInterns: (s) => s.numberOfInterns,
  errors: (s) => s.errors,

  ...dnaUspStamp.getters,
  ...companyData.getters,
  ...about.getters,
  ...investment.getters,
  ...revenue.getters,
  ...incubation.getters,
  ...partners.getters,
};

export const mutations = {
  setFormField: (s, { key, value }) => (s[key] = value),
  setErrors: (s, errors) => (s.errors = errors),

  ...dnaUspStamp.mutations,
  ...companyData.mutations,
  ...about.mutations,
  ...investment.mutations,
  ...revenue.mutations,
  ...incubation.mutations,
  ...partners.mutations,
};

export const actions = {
  ...dnaUspStamp.actions,
  ...companyData.actions,
  ...about.actions,
  ...investment.actions,
  ...revenue.actions,
  ...incubation.actions,
  ...partners.actions,

  setNumberOfCTLEmployees: ({ commit }, value) =>
    commit("setFormField", { key: "numberOfCTLEmployees", value }),
  setNumberOfPJColaborators: ({ commit }, value) =>
    commit("setFormField", { key: "numberOfPJColaborators", value }),
  setNumberOfInterns: ({ commit }, value) =>
    commit("setFormField", { key: "numberOfInterns", value }),

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
    ...about.prepareSection(obj),
    ...investment.prepareSection(obj),
    ...revenue.prepareSection(obj),
    ...incubation.prepareSection(obj),
    ...partners.prepareSection(obj),
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
