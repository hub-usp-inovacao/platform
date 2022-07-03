const state = () => ({
  name: '',
  nusp: '',
  bond: '',
  limitDate: '',
  phone: '',
  personalPhone: '',
  area: {
    major: '',
    minors: [],
  },
  keywords: []
})

const getters = {
  name: (s) => s.name,
  nusp: (s) => s.nusp,
  bond: (s) => s.bond,
  limitDate: (s) => s.limitDate,
  phone: (s) => s.phone,
  personalPhone: (s) => s.personalPhone,
  areaMajor: (s) => s.area.major,
  areaMinors: (s) => s.area.minors,
  keywords: (s) => s.keywords,
}

const mutations = {
  setName: (s, newValue) => s.name = newValue,
  setNusp: (s, newValue) => s.nusp = newValue,
  setBond: (s, newValue) => s.bond = newValue,
  setLimitDate: (s, newValue) => s.limitDate = newValue,
  setPhone: (s, newValue) => s.phone = newValue,
  setPersonalPhone: (s, newValue) => s.personalPhone = newValue,
  setAreaMajor: (s, newValue) => s.area.major = newValue,
  setAreaMinors: (s, newList) => s.area.minors = newList,
  setKeywords: (s, newValue) => s.keywords = newValue,
}

const actions = {
  setName: ({ commit }, newValue) => commit("setName", newValue),
  setNusp: ({ commit }, newValue) => commit("setNusp", newValue),
  setBond: ({ commit }, newValue) => commit("setBond", newValue),
  setLimitDate: ({ commit }, newValue) => commit("setLimitDate", newValue),
  setPhone: ({ commit }, newValue) => commit("setPhone", newValue),
  setPersonalPhone: ({ commit }, newValue) => commit("setPersonalPhone", newValue),
  setAreaMajor: ({ commit }, newValue) => commit("setAreaMajor", newValue),
  setAreaMinors: ({ commit }, newList) => commit("setAreaMinors", newList),
  setKeywords: ({ commit }, newValue) => commit("setKeywords", newValue),

  loadInitialPersonal: ({ commit }, { bond, limit_date, name, nusp, phones, area, keywords }) => {
    commit("setName", name)
    commit("setNusp", nusp || "")
    commit("setBond", bond)
    commit("setLimitDate", limit_date)
    commit("setPhone", phones[0])
    commit("setAreaMajor", area.major[0])
    commit("setAreaMinors", area.minors)
    commit("setKeywords", keywords)
  }
}

const prepareSection = (obj) => ({
  personal: {
    name: obj.name,
    nusp: obj.nusp,
    bond: obj.bond,
    limitDate: obj.limitDate,
    phone: obj.phone,
    personalPhone: obj.personalPhone,
    area: {
      major: obj.areaMajor,
      minors: obj.areaMinors,
    },
    keywords: obj.keywords
  }
})

export default { state, getters, mutations, actions, prepareSection }
