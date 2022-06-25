function DisciplineAdapter(axios) {
  const baseURL = "http://localhost:8080"

  return {
    requestData: async () => {
      const url = baseURL + "/disciplines"

      const { disciplines } = await axios.$get(url)

      return []
    },

    filterData: async (params) => {
      const url = baseURL + "/disciplines"

      const stringParams = Object
        .keys(params)
        .filter((key) => params[key])
        .map((key) => {
          const value = params[key]
          return `${key}=${value}`
        })
        .join("&")

      const { disciplines } = await axios.$get(`${url}?${stringParams}`)

      return []
    }
  }
}

export default (context, inject) => {
  const adapter = DisciplineAdapter(context.$axios)
  inject("DisciplineAdapter", adapter)
}