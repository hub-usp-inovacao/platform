async function fetchData() {
  const url = process.env.BACKEND_URL + "/iniciatives";
  try {
    const resp = await fetch(url);

    return await resp.json();

  } catch (error) {
    console.log("error occuried while fetching...");
    console.log(error);
    return undefined;
  }
}

export default (_, inject) => {
  inject("fetchIniciatives", async () => {
    const values = await fetchData();
    if (values == undefined) return { iniciatives: [] };

    const iniciatives = values
      .filter((i) => i !== null)
      .sort((a, b) => a.name.localeCompare(b.name));

    return { iniciatives };
  });
};
