const state = () => ({
  patents: [],
});

const getters = {
  patents: (s) => s.patents,
};

const mutations = {
  setPatents: (s, newList) => {
    s.patents = newList;
  },
};

const actions = {
  setPatents: (context, newList) => {
    context.commit("setPatents", newList);
  },
};

function prepareSection(obj) {
  return {
    patents: obj.patents.map((patent) => ({
      name: patent.Nome,
      code: patent["Código"],
    })),
  };
}

export default {
  state,
  getters,
  mutations,
  actions,
  prepareSection,
};
