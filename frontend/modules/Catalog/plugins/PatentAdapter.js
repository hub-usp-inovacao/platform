function PatentAdapter(axios) {
  const baseURL = process.env.BACKEND_URL + "/catalog";

  async function requestData() {
    const url = baseURL + "/patents";

    try {
      const { patents } = await axios.$get(url);
      return patents;
    } catch (error) {
      return [];
    }
  }

  async function filterData(prms) {
    const url = baseURL + "/patents";

    const params = Object.assign({}, prms);

    if (params.areaMajors?.length > 0) {
      params.areaMajors = params.areaMajors.join(",");
    }

    if (params.areaMinors?.length > 0) {
      params.areaMinors = params.areaMinors.join(",");
    }

    try {
      const { patents } = await axios.$get(url, { params });
      return patents;
    } catch (error) {
      return [];
    }
  }

  async function getClassifications() {
    try {
      let { classifications } = await axios.$get(
        baseURL + "/patent_classifications"
      );
      classifications.forEach(({ secondaries }) =>
        secondaries.sort((a, b) => a.localeCompare(b))
      );
      classifications.sort((a, b) => a.primary.localeCompare(b.primary));
      return classifications;
    } catch (error) {
      return [];
    }
  }

  return { requestData, filterData, getClassifications };
}

export default (context, inject) => {
  const adapter = PatentAdapter(context.$axios);
  inject("PatentAdapter", adapter);
};
