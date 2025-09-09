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

        if (getters.hasOwnProperty(k)) {
          commit("setFormField", { key: k, value: message[key] });
        }
      });
    }
  },

  updateCompanyForm: async function ({ commit, getters }) {
    if (getters.partners && getters.partners.length > 0) {
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

    const company = prepareCompanyObject(getters);
    const result = await this.$registerCompany(company);

    if (!result.success) {
      commit("setErrors", {
        general: [result.error]
      });
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

const prepareCompanyObject = (obj) => {
  return {
    name: obj.name,
    corporateName: obj.corporateName,
    cnpj: obj.cnpj,
    year: obj.year ? obj.year.toString() : "",
    cnae: obj.cnae,
    address: {
      venue: obj.venue || obj.address?.venue || "",
      neighborhood: obj.neighborhood || obj.address?.neighborhood || "",
      city: obj.city || obj.address?.city || "",
      state: obj.state || obj.address?.state || "",
      cep: obj.cep || obj.address?.cep || ""
    },
    phones: Array.isArray(obj.phones) ? obj.phones : [],
    emails: Array.isArray(obj.emails) ? obj.emails : [],
    description: obj.descriptionLong || obj.description || "",
    services: Array.isArray(obj.services) ? obj.services : [],
    technologies: Array.isArray(obj.technologies) ? obj.technologies : [],
    logo: obj.logo || null,
    url: obj.site || null,
    incubated: obj.incubated || "",
    ecosystems: Array.isArray(obj.ecosystems) ? obj.ecosystems : [],
    isUnicorn: Boolean(obj.isUnicorn),
    totalCollaborators: (parseInt(obj.numberOfCLTEmployees) || 0) + (parseInt(obj.numberOfPJColaborators) || 0) + (parseInt(obj.numberOfInterns) || 0),
    odss: Array.isArray(obj.odss) ? obj.odss : [],
    linkedin: obj.linkedin || null,
    instagram: obj.instagram || null,
    youtube: obj.youtube || null,
    facebook: obj.facebook || null,
    dnaUspWanted: Boolean(obj.wantsDna),
    dnaUspContactEmail: obj.dnaContactEmail || null,
    dnaUspContactName: obj.dnaContactName || null,
    dnaUspContract: obj.dnaUspContract || null,
    truthfulInformation: Boolean(obj.truthfulInformations),
    agreementOptions: Array.isArray(obj.permissions) ? obj.permissions : [],
    partnerBonds: Array.isArray(obj.partners) ? obj.partners.map(p => p.bond || "") : [],
    partnerUnities: Array.isArray(obj.partners) ? obj.partners.map(p => p.unity || "") : [],
    partnerPositions: Array.isArray(obj.partners) ? obj.partners.map(p => p.position || "") : [],
    partnerEmails: Array.isArray(obj.partners) ? obj.partners.map(p => p.email || "") : [],
    partnerPhones: Array.isArray(obj.partners) ? obj.partners.map(p => p.phone || "") : [],
    totalPartners: Array.isArray(obj.partners) ? obj.partners.length : 0,
    hasUspPartners: Array.isArray(obj.partners) ? obj.partners.some(p => p.nusp && p.nusp.trim() !== "") : false,
    wantsToAddMorePartners: Boolean(obj.wantsToAddMorePartners),
    cltEmployees: parseInt(obj.numberOfCLTEmployees) || 0,
    pjCollaborators: parseInt(obj.numberOfPJColaborators) || 0,
    hasInvestment: obj.received === "Sim",
    investmentTypes: Array.isArray(obj.investments) ? obj.investments : [],
    ownInvestmentAmount: obj.investmentsValues?.own || null,
    angelInvestmentAmount: obj.investmentsValues?.angel || null,
    ventureCapitalAmount: obj.investmentsValues?.venture || null,
    privateEquityAmount: obj.investmentsValues?.equity || null,
    pipeAmount: obj.investmentsValues?.pipe || null,
    crowdfundingAmount: obj.investmentsValues?.crowdfunding || null,
    bndesFinepAmount: obj.investmentsValues?.bndesFinep || null,
    otherInvestmentsAmount: obj.investmentsValues?.others || null,
    revenue2022: obj.lastYear || null,
    rfbSize: obj.size || null,
    companyType: obj.companyType || null,
    operationalStatus: obj.registry_status || null,
    index: obj.index || null,
    totalSum: (parseInt(obj.numberOfCLTEmployees) || 0) + (parseInt(obj.numberOfPJColaborators) || 0) + (parseInt(obj.numberOfInterns) || 0),
    incubatorBond: obj.incubatorBond || null,
    uspBondConfirmation: obj.uspBondConfirmation || null,
    uspDnaCategory: obj.category || null,
    companyBondConfirmation: obj.companyBondConfirmation || null
  };
};
