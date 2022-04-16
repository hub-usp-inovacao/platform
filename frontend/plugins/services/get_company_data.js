async function getData(token) {
  const backendURL = process.env.BACKEND_URL;

  try {
    const url = `${backendURL}/company`;

    const body = JSON.stringify({
      security: { token },
    });

    return await fetch(url, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body,
    });
  } catch (error) {
    console.log("error occuried while updating...");
    console.log(error);
    return undefined;
  }
}

export default (_, inject) => {
  inject("getCompanyData", async (token) => {
    const response = await getData(token);

    if (!response || response.status === 400) {
      return {
        status: "failure",
        message:
          "Não foi encontrada nenhuma empresa com este CNPJ. Tente novamente.",
      };
    }

    if (response.status === 500) {
      return {
        status: "failure",
        message: "Ocorreu um erro de servidor. Tente novamente mais tarde",
      };
    }

    const data = await response.json();
    return { status: "ok", message: data };
  });
};
