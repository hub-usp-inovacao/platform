function InitiativeAdapter(axios) {
  const baseURL = "http://localhost:8080";

  async function requestData() {
    const url = baseURL + "/initiatives";

    try {
      const { initiatives } = await axios.$get(url);
      return initiatives;
    } catch (e) {
      return [];
    }
  }

  async function filterData(prms) {
    const url = baseURL + "/initiatives";

    const params = Object.assign({}, prms);

    if (params.classifications?.length > 0) {
      params.classifications = params.classifications.join(",");
    }

    try {
      const { initiatives } = await axios.$get(url, { params });
      return initiatives;
    } catch (e) {
      return [];
    }
  }

  return { requestData, filterData };
}

export default (context, inject) => {
  const adapter = InitiativeAdapter(context.$axios);
  inject("InitiativeAdapter", adapter);
};
