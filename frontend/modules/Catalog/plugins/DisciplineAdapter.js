function DisciplineAdapter(axios) {
  const baseURL = process.env.BACKEND_URL + "/catalog";

  async function requestData() {
    const url = baseURL + "/disciplines";

    try {
      const { disciplines } = await axios.$get(url);
      return disciplines;
    } catch (error) {
      return [];
    }
  }

  async function filterData(prms) {
    const url = baseURL + "/disciplines";

    const params = Object.assign({}, prms);

    if (params.categories?.length > 0) {
      params.categories = params.categories.join(",");
    }

    try {
      const { disciplines } = await axios.$get(url, { params });
      return disciplines;
    } catch (error) {
      return [];
    }
  }

  return {
    requestData,
    filterData,
  };
}

export default (context, inject) => {
  const adapter = DisciplineAdapter(context.$axios);
  inject("DisciplineAdapter", adapter);
};
