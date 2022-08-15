function CompanyAdapter(axios) {
  const baseURL = process.env.BACKEND_URL + "/catalog";

  async function requestData() {
    const url = baseURL + "/companies";

    try {
      const { companies } = await axios.$get(url);
      return companies;
    } catch (error) {
      return [];
    }
  }

  async function filterData(prms) {
    const url = baseURL + "/companies";

    const params = Object.assign({}, prms);

    if (params.areaMajors?.length > 0) {
      params.areaMajors = params.areaMajors.join(",");
    }

    if (params.areaMinors?.length > 0) {
      params.areaMinors = params.areaMinors.join(",");
    }

    try {
      const { companies } = await axios.$get(url, { params });
      return companies;
    } catch (error) {
      return [];
    }
  }

  async function getEcosystems() {
    try {
      const { ecosystems } = await axios.$get(baseURL + "/ecosystems");
      return ecosystems;
    } catch (error) {
      return [];
    }
  }

  return {
    requestData,
    filterData,
    getEcosystems,
  };
}

export default (context, inject) => {
  const adapter = CompanyAdapter(context.$axios);
  inject("CompanyAdapter", adapter);
};
