const state = () => ({
  unities: [],
  campus: "",
  groups: [],
});

const getters = {
  unities: (s) => s.unities,
  campus: (s) => s.campus,
  groups: (s) => s.groups,
};

const mutations = {
  setUnities: (s, newList) => (s.unities = newList),
  setCampus: (s, newValue) => (s.campus = newValue),
  setGroups: (s, newList) => (s.groups = newList),
};

const actions = {
  setUnities: ({ commit }, newList) => commit("setUnities", newList),
  setCampus: ({ commit }, newValue) => commit("setCampus", newValue),
  setGroups: ({ commit }, newList) => commit("setGroups", newList),

  loadInitialBond: ({ commit }, { unities, campus }) => {
    commit("setUnities", unities);
    commit("setCampus", campus);
  },
};

const prepareSection = (obj) => ({
  bond: {
    unities: obj.unities,
    campus: obj.campus,
    groups: obj.groups,
  },
});

export default { state, getters, mutations, actions, prepareSection };
