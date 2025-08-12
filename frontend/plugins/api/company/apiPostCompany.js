import { parseValidationErrors } from "./parse.js";

export default (data) =>
  fetch(`${process.env.BACKEND_URL}/v2/company`, {
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
