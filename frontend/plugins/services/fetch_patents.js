async function fetchData() {
  const backendURL = process.env.BACKEND_URL;

  try {
    const url = backendURL + "/patents";

    const resp = await fetch(url);

    return await resp.json();
  } catch (error) {
    console.log("error occuried while fetching...");
    console.log(error);
    return undefined;
  }
}

export default (_, inject) => {
  inject("fetchPatents", async () => {
    const values = await fetchData();
    if (values == undefined) return { patents: [] };

    const patents = values.sort((a, b) => a.name.localeCompare(b.name));

    return { patents };
  });
};
