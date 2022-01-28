export const state = () => ({
  isLoading: false,
  skills: [],
  errors: undefined,
  keys: [
    "name",
    "labsOrGroupsNames",
    "labsOrGroupsInitials",
    "keywords",
    "descriptionSkills",
    "descriptionEquipments",
    "descriptionServices",
  ],
});

export const getters = {
  dataStatus: (s) => (s.isLoading ? "loading" : "ok"),
  isEmpty: (s) => s.skills.length == 0,
  skills: (s) => s.skills,
  searchKeys: (s) => s.keys,
  errors: (s) => s.errors,
  campi: (s) =>
    s.skills
      .map((s) => s.campus)
      .filter(
        (campus, index, campi) =>
          index === campi.findIndex((other) => other === campus)
      ),
  unities: (s) =>
    s.skills
      .map((s) => s.unity)
      .concat([
        "CeBiMar - Centro de Biologia Marinha",
        "CENA - Centro de Energia Nuclear na Agricultura",
        "IEA - Instituto de Estudos Avançados",
        "IEB - Instituto de Estudos Brasileiros",
        "IEE - Instituto de Energia e Ambiente",
        "IMT - Instituto de Medicina Tropical",
      ])
      .filter(
        (unity, index, unities) =>
          index === unities.findIndex((other) => other == unity)
      ),
  bonds: (s) =>
    s.skills.reduce((acc, skill) => {
      return acc.includes(skill.bond) ? acc : acc.concat(skill.bond);
    }, []),
};

export const mutations = {
  setLoadingStatus: (s) => (s.isLoading = true),
  unsetLoadingStatus: (s) => (s.isLoading = false),
  setSkills: (s, newSkills) => (s.skills = newSkills),
  setErrors: (s, errors) => (s.errors = errors),
};

export const actions = {
  fetchSpreadsheets: async function (ctx, payload) {
    ctx.commit("setLoadingStatus");

    const { skills, errors } = await this.$fetchSkills(payload);
    ctx.commit("setErrors", errors);
    ctx.commit("setSkills", skills);

    ctx.commit("unsetLoadingStatus");
  },
};
