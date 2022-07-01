const state = () => {
    return {patents: []}
}

const getters = {
    patents: (s) => {
        console.log("dentro do getter de patents")
        return s.patents
    }
}

const mutations = {
    setPatents: (s, newList) => {s.patents = newList}
}

const actions = {
    setPatents: (context, newList) => {
        context.commit("setPatents", newList)
    }
}

function prepareSection (obj) {
    return {patents: obj.patents}
}

export default {
    state,
    getters,
    mutations,
    actions,
    prepareSection
} 