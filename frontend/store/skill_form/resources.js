const state = () => ({
  skills: [],
  equipments: [],
  services: [],
})

const getters = {
  skills: (s) => s.skills,
  equipments: (s) => s.equipments,
  services: (s) => s.services,
}

const mutations = {
  setSkills: (s, newList) => s.skills = newList,
  setEquipments: (s, newList) => s.equipments = newList,
  setServices: (s, newList) => s.services = newList,
}

const actions = {
  setSkills: ({ commit }, newList) =>
    commit("setSkills", newList),

  setEquipments: ({ commit }, newList) =>
    commit("setEquipments", newList),

  setServices: ({ commit }, newList) =>
    commit("setServices", newList),

  loadInitialResources: ({ commit }, { skills, equipments, services }) => {
    commit("setSkills", skills)
    commit("setEquipments", equipments)
    commit("setServices", services)
  }
}

const prepareSection = (obj) => ({
  resource: {
    skills: obj.skills,
    services: obj.services,
    equipments: obj.equipments
  }
})

export default { state, getters, mutations, actions, prepareSection }
