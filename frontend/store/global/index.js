export const state = () => ({
  strictSearchResults: true,
});

export const getters = {
  strictSearchResults: (s) => s.strictSearchResults,
};

export const mutations = {
  setStrictResults: (s) => (s.strictSearchResults = true),
  setFlexibleResults: (s) => (s.strictSearchResults = false),
};

export const actions = {
  setStrictResults: ({ commit }) => commit("setStrictResults"),
  setFlexibleResults: ({ commit }) => commit("setFlexibleResults"),
};
