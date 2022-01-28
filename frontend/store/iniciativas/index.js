export const state = () => ({
  iniciatives: [],
  isLoading: false,
  errors: undefined,
  keys: [
    "name",
    "descriptionLong",
    "descriptionShort",
    "keywords",
    "services",
  ],
});

export const getters = {
  iniciatives: (s) => s.iniciatives,
  isEmpty: (s) => s.iniciatives.length === 0,
  dataStatus: (s) => (s.isLoading ? "loading" : "ok"),
  searchKeys: (s) => s.keys,
  campi: (s) => {
    const campi = s.iniciatives
      .reduce(
        (campi, iniciative) =>
          campi.concat(
            iniciative.local.split(",").map((local) => local.trim())
          ),
        []
      )
      .filter((campi) => campi.trim().length > 0)
      .filter(
        (campus, index, campi) =>
          index === campi.findIndex((other) => other === campus)
      );

    return campi.sort();
  },
  errors: (s) => s.errors,
};

export const mutations = {
  setLoadingStatus: (s) => (s.isLoading = true),
  unsetLoadingStatus: (s) => (s.isLoading = false),
  setIniciatives: (s, newIniciatives) => (s.iniciatives = newIniciatives),
  setErrors: (s, errors) => (s.errors = errors),
};

export const actions = {
  fetchSpreadsheets: async function (ctx, env) {
    ctx.commit("setLoadingStatus");

    const { iniciatives, errors } = await this.$fetchIniciatives(env);
    ctx.commit("setErrors", errors);
    ctx.commit("setIniciatives", iniciatives);

    ctx.commit("unsetLoadingStatus");
  },
};
