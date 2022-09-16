function JornadaAdapter(axios) {
  async function makeRequest(path, prms) {
    const baseURL = process.env.BACKEND_URL + "/journey";
    const url = baseURL + path;
    const params = Object.assign({}, prms);

    try {
      const { records } = await axios.$get(url, { params });
      return records;
    } catch (error) {
      return [];
    }
  }

  function capitalize(name) {
    return name.charAt(0).toUpperCase() + name.slice(1);
  }

  function generateStepMethods(acc, stepName) {
    const methods = Object.assign({}, acc);
    const stepNameCapitalized = capitalize(stepName);
    const stepURIPath = `/${stepName}`;

    methods[`request${stepNameCapitalized}`] = async function () {
      return makeRequest(stepURIPath, {});
    };

    methods[`update${stepNameCapitalized}`] = async function (prms) {
      return makeRequest(stepURIPath, prms);
    };

    return methods;
  }

  const steps = ["learn", "practice", "create", "improve", "fund"];

  return steps.reduce(generateStepMethods, {});
}

export default (context, inject) => {
  const adapter = JornadaAdapter(context.$axios);
  inject("JornadaAdapter", adapter);
};
