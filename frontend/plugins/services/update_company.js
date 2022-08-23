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

function translateError(error) {
  return error
    .replaceAll("Partners", "Sócios")
    .replaceAll("Colaborators", "Colaboradores")
    .replaceAll("Investiment", "Investimento")
    .replaceAll("Dna usp stamp", "Selo DNA USP")
    .replaceAll("Incubation", "Incubação");
}

function translate(errors) {
  return errors.map(translateError);
}

export default (_, inject) => {
  inject("updateCompanyData", async (data, logo) => {
    const response = await updateData(data, logo);

    if (!response) {
      return {
        error: ["Falha de conexão com o servidor. Tente novamente mais tarde."],
      };
    }

    if (response.status >= 200 && response.status < 300) {
      return {};
    } else if (response.status >= 400 && response.status < 500) {
      const { errors } = await response.json();

      return { errors: translate(errors) };
    } else {
      return {
        errors: [
          "Erro do servidor ao processar os dados. Tente novamente mais tarde.",
        ],
      };
    }
  });
};
