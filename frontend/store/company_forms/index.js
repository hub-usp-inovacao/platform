import dnaUspStamp from "./dna_usp_stamp";
import companyData from "./company_data";
import about from "./about";
import investment from "./investment";
import revenue from "./revenue";
import incubation from "./incubation";
import staff from "./staff";
import partners from "./partners";

export const state = () => ({
  numberOfCLTEmployees: "",
  numberOfPJColaborators: "",
  numberOfInterns: "",
  errors: {},

  ...dnaUspStamp.state(),
  ...companyData.state(),
  ...about.state(),
  ...investment.state(),
  ...revenue.state(),
  ...incubation.state(),
  ...staff.state(),
  ...partners.state(),
});

export const getters = {
  errors: (s) => s.errors,

  ...dnaUspStamp.getters,
  ...companyData.getters,
  ...about.getters,
  ...investment.getters,
  ...revenue.getters,
  ...incubation.getters,
  ...staff.getters,
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
  ...staff.mutations,
  ...partners.mutations,
};

export const actions = {
  ...dnaUspStamp.actions,
  ...companyData.actions,
  ...about.actions,
  ...investment.actions,
  ...revenue.actions,
  ...incubation.actions,
  ...staff.actions,
  ...partners.actions,

  getCompanyData: async function ({ commit, getters }) {
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
    }
  },

  updateCompanyForm: async function ({ commit, getters }) {
    if (getters.partners.length === 0) {
      commit("setErrors", {
        partners: ["É necessário informar pelo menos um sócio da empresa"],
      });
      return false;
    }

    if (!getters.cnpj || !getters.name) {
      commit("setErrors", {
        company_data: ["É necessário informar o nome e o CNPJ da empresa"],
      });
      return false;
    }

    if (!getters.truthfulInformations) {
      commit("setErrors", {
        dna_usp_stamp:
          ["É necessário declarar que as informações fornecidas são verdadeiras e que a empresa atende aos critérios estabelecidos"],
      });
      return false;
    }

    const logo = getters.logoFile;
    const company = prepareCompanyObject(getters);
    const { errors } = await this.$updateCompanyData(company, logo);

    if (errors !== undefined) {
      commit("setErrors", errors);
      return false;
    }

    commit("setErrors", {});
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
    ...staff.prepareSection(obj),
    ...partners.prepareSection(obj),
  },
});
