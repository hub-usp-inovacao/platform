import { parseValidationErrors } from "./parse.js";

/**
 * Send POST request to register Company
 *
 * TODO: Deal with logo:
 * - allow local images (and serve them from the kotlin server)
 * - or restrict it to only allow web images
 */
// eslint-disable-next-line no-unused-vars
export default (data, _logo) =>
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
