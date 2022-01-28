async function fetchData() {
  const backendURL = process.env.BACKEND_URL;

  try {
    const url = backendURL + "/disciplines";

    const resp = await fetch(url);

    const rawData = await resp.json();
    return rawData;
  } catch (error) {
    console.log("error occuried while fetching...");
    console.log(error);
    return undefined;
  }
}

export default (_, inject) => {
  inject("fetchDisciplines", async () => {
    const values = await fetchData();
    if (values == undefined) return { disciplines: [], errors: [] };

    const disciplines = values.sort((a, b) => {
      const aName = a.name.split(" - ")[1];
      const bName = b.name.split(" - ")[1];

      return aName.localeCompare(bName);
    });

    return { disciplines };
  });
};
