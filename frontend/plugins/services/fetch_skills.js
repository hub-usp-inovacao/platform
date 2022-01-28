async function fetchData() {
  const url = process.env.BACKEND_URL + "/skills";
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
  inject("fetchSkills", async () => {
    const values = await fetchData();
    if (values == undefined) return { skills: [] };

    const skills = values
      .filter((s) => s !== null && (!s.limitDate || s.limitDate > new Date()))
      .sort((a, b) => a.name.localeCompare(b.name));

    return { skills };
  });
};
