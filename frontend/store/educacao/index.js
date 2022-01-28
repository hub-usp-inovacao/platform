const indexingKeys = ["name", "description.long", "description.short"];

export const state = () => ({
  disciplines: [],
  isLoading: false,
  keys: indexingKeys.map((k) => `inspect.${k}`),
});

export const getters = {
  dataStatus: (state) => (state.isLoading ? "loading" : "ok"),
  disciplines: (state) => state.disciplines,
  isEmpty: (state) => state.disciplines.length === 0,
  campi: (state) => {
    return Array.from(
      state.disciplines.reduce(
        (campiSet, di) => campiSet.add(di.campus),
        new Set()
      )
    ).sort((a, b) => a.localeCompare(b));
  },
  searchKeys: (s) => s.keys,
};

export const mutations = {
  setLoadingStatus(state) {
    state.isLoading = true;
  },
  unsetLoadingStatus(state) {
    state.isLoading = false;
  },
  setDisciplines(state, disciplines) {
    state.disciplines = disciplines;
  },
};

export const actions = {
  fetchSpreadsheets: async function (ctx, env) {
    ctx.commit("setLoadingStatus");

    const { disciplines } = await this.$fetchDisciplines(env);
    const indexed = this.$indexer("disciplines", disciplines);
    ctx.commit("setDisciplines", indexed);

    ctx.commit("unsetLoadingStatus");
  },
};
