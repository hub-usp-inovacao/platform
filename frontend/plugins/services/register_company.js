async function postData(data, _logo) {
  const backendURL = `${process.env.BACKEND_URL}/v2`;

  try {
    const url = `${backendURL}/companies/company`;

    // TODO: Handle this in the kotlin backend (or remove if unnecessary)
    // if (logo) {
    //   const body = new FormData();
    //   body.append("company[logo]", logo);
    //   body.append("company[cnpj]", data.company.company_data.cnpj);
    //
    //   await fetch(url + "/update_request/logo", {
    //     method: "POST",
    //     body,
    //   });
    // }

    const headers = new Headers();
    headers.append("Content-Type", "application/json");

    const resp = await fetch(url, {
      method: "POST",
      headers,
      body: JSON.stringify(data),
    });

    return resp;
  } catch (error) {
    console.log("error occuried while updating...");
    console.log(error);
    return undefined;
  }
}

export default (_, inject) => {
  inject("registerCompanyData", async (data, logo) => {
    console.log("reached:", data)
    const response = await postData(data, logo);

    if (!response) {
      return {
        errors: {
          server:
            "Falha de conexão com o servidor. Por favor, entre em contato com a AUSPIN.",
        },
      };
    }

    if (response.status >= 200 && response.status < 300) {
      return {};
    }

    if (response.status >= 400 && response.status < 500) {
      const { errors } = await response.json();

      const returnValue = { errors: translate(errors) };

      console.log(returnValue);

      return returnValue;
    }

    return {
      errors: {
        server:
          "Erro do servidor ao processar os dados. Por favor, entre em contato com a AUSPIN.",
      },
    };
  });
};
