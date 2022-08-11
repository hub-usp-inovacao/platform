function ResearcherAdapter(axios) {
  const baseURL = process.env.BACKEND_URL + "/api/catalog";

  async function requestData() {
    const url = baseURL + "/skills";

    try {
      const { skills } = await axios.$get(url);
      return skills;
    } catch (error) {
      return [];
    }
  }

  async function filterData(prms) {
    const url = baseURL + "/skills";

    const params = Object.assign({}, prms);

    if (params.areaMajors?.length > 0) {
      params.areaMajors = params.areaMajors.join(",");
    }

    if (params.areaMinors?.length > 0) {
      params.areaMinors = params.areaMinors.join(",");
    }

    try {
      const { skills } = await axios.$get(url, { params });
      return skills;
    } catch (error) {
      return [];
    }
  }

  return { requestData, filterData };
}

export default (context, inject) => {
  const adapter = ResearcherAdapter(context.$axios);
  inject("ResearcherAdapter", adapter);
};
