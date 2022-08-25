async function updateData(data, logo) {
  const backendURL = process.env.BACKEND_URL;

  try {
    const url = backendURL + "/companies";

    const headers = new Headers();
    headers.append("Content-Type", "application/json");

    const resp = await fetch(url, {
      method: "PATCH",
      headers,
      body: JSON.stringify(data),
    });

    if (logo) {
      const body = new FormData();
      body.append("company_image", logo);

      await fetch(url, {
        method: "PATCH",
        body,
      });
    }

    return resp;
  } catch (error) {
    console.log("error occuried while updating...");
    console.log(error);
    return undefined;
  }
}

export default (_, inject) => {
  inject("updateCompanyData", async (data, logo) => {
    const response = await updateData(data, logo);

    if (!response) {
      return {
        errors: {
          server:
            "Falha de conexão com o servidor. Tente novamente mais tarde.",
        },
      };
    }

    if (response.status >= 200 && response.status < 300) {
      return {};
    } else if (response.status >= 400 && response.status < 500) {
      const { errors } = await response.json();

      return { errors };
    } else {
      return {
        errors: {
          server:
            "Erro do servidor ao processar os dados. Tente novamente mais tarde.",
        },
      };
    }
  });
};
