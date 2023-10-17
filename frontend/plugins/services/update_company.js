async function updateData(data, logo) {
  const backendURL = process.env.BACKEND_URL;

  try {
    const url = backendURL + "/companies";

    if (logo) {
      const body = new FormData();
      body.append("company[logo]", logo);
      body.append("company[cnpj]", data.company.company_data.cnpj);

      await fetch(url + '/update_request/logo', {
        method: "POST",
        body,
      });
    }

    const headers = new Headers();
    headers.append("Content-Type", "application/json");

    const resp = await fetch(url, {
      method: "PATCH",
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

function translateErrorMessage(englishError) {
  if (englishError === "Wants inválido")
    return "Pedido de DNA USP inválido - necessário informar nome e email";

  if (englishError === "Was incubated não pode estar em branco")
    return "O primeiro campo de incubação não pode ser vazio";

  if (englishError === "Was incubated inválido")
    return "Empresas que foram ou estão incubadas devem informar a incubadora";

  if (englishError === "Ecosystem inválido")
    return "Empresas que foram ou estão incubadas devem informar a incubadora";

  if (englishError === "Site deve ser um link válido(HTTP(s))")
    return "Site deve ser um link com HTTP ou HTTPS";

  if (englishError === "Public name inválido")
    return "Nome público não pode ser vazio";

  if (englishError === "Corporate name inválido")
    return "Razão social não pode ser vazia";

  if (englishError === "Year inválido") return "Ano de criação inválido";

  if (englishError === "Company nature inválido") return "Natureza jurídica da empresa inválida";

  if (englishError === "Bond inválido. Tente atualizar o seu vínculo.")
	return "Vínculo com a USP inválido. Verifique os vínculos dos sócios.";

  return englishError;
}

function translate(errors) {
  return Object.keys(errors).reduce((acc, curr) => {
    acc[curr] = errors[curr].map(translateErrorMessage);
    return acc;
  }, {});
}

export default (_, inject) => {
  inject("updateCompanyData", async (data, logo) => {
    const response = await updateData(data, logo);

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
