async function getData(cnpj) {
  const backendURL = process.env.BACKEND_URL;

  try {
    const url = `${backendURL}/companies?cnpj=${cnpj}`;

    return await fetch(url);
  } catch (error) {
    console.log("error occuried while updating...");
    console.log(error);
    return undefined;
  }
}

export default (_, inject) => {
  inject("getCompanyData", async (cnpj) => {
    const response = await getData(cnpj);

    if (!response || response.status === 404) {
      return {
        status: "failure",
        message:
          "Não foi encontrada nenhuma empresa com este CNPJ. Tente novamente.",
      };
    }

    const data = await response.json();
    return { status: "ok", message: data };
  });
};
