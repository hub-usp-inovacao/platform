import { parseValidationErrors } from "./parse.js";

/**
 * Send POST request to register Company
 */
export default async (data, logo) => {
  // TODO: Upload logo to the kotlin backend
  if (logo) {
    const body = new FormData();

    body.append("company[logo]", logo);
    body.append("company[cnpj]", data.company.company_data.cnpj);

    await fetch(`${process.env.BACKEND_URL}/update_request/logo`, {
      method: "POST",
      body,
    });
  }

  return fetch(`${process.env.BACKEND_URL}/v2/company`, {
    method: "POST",
    headers: new Headers({
      "Content-Type": "application/json",
    }),
    body: JSON.stringify(data),
  })
    .then((response) => {
      switch (response.status) {
        case 201: // Created
          return {};
        case 422: // Unprocessable Content
          return response.json().then(({ errors }) => ({
            errors: parseValidationErrors(errors),
          }));
        default:
          throw new Error(`Unexpected server response: ${response.status}`);
      }
    })
    .catch((err) => ({
      errors: {
        server:
          "Falha de conexão com o servidor.\n" +
          "Por favor, entre em contato com a AUSPIN.\n" +
          err,
      },
    }));
};
