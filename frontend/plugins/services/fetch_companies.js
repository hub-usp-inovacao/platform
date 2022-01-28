async function fetchData() {
  const backendURL = process.env.BACKEND_URL;

  try {
    const url = backendURL + "/companies";

    const resp = await fetch(url);

    return await resp.json();
  } catch (error) {
    console.log("error occuried while fetching...");
    console.log(error);
    return undefined;
  }
}

export default (app, inject) => {
  inject("fetchCompanies", async () => {
    const values = await fetchData();
    if (values == undefined) return { companies: [] };

    const companies = values.sort((a, b) => a.name.localeCompare(b.name));

    return { companies };
  });
};
