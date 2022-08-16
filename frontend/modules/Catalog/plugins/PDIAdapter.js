function PDIAdapter(axios) {
  const baseURL = process.env.BACKEND_URL + "/catalog";

  async function requestData() {
    const url = baseURL + "/pdis";

    try {
      const { pdis } = await axios.$get(url);
      return pdis;
    } catch (error) {
      return [];
    }
  }

  async function filterData(prms) {
    const url = baseURL + "/pdis";

    const params = Object.assign({}, prms);

    if (params.categories?.length > 0) {
      params.categories = params.categories.join(",");
    }

    try {
      const { pdis } = await axios.$get(url, { params });
      return pdis;
    } catch (error) {
      return [];
    }
  }

  return { requestData, filterData };
}

export default (context, inject) => {
  const adapter = PDIAdapter(context.$axios);
  inject("PDIAdapter", adapter);
};
