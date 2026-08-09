import jwt from "./jwt.js";
import dictionary from "./dictionary.json";

function translateKey(text) {
  Object.entries(dictionary).forEach(
    ([english, portuguese]) => (text = text.replace(english, portuguese)),
  );

  return text;
}

const handlers = {
  form: (response) => {
    switch (response.status) {
      case 201: // Created
        return {};
      case 422: // Unprocessable Content
        return response.json().then((response) => ({
          errors: Object.fromEntries(
            Object.entries(response?.errors ?? {}).map(([key, value]) => [
              key,
              value.map(translateKey),
            ]),
          ),
        }));
      default:
        throw new Error(`Unexpected server response: ${response.status}`);
    }
  },

  error: (err) => ({
    errors: {
      server:
        "Falha de conexão com o servidor.\n" +
        "Por favor, entre em contato com a AUSPIN.\n" +
        err,
    },
  }),
};

/**
 * Send POST request to register Company
 */
const post = async (data, logo) => {
  const form = new FormData();

  form.append("company", JSON.stringify(data.company));

  if (logo) {
    form.append("logo", logo);
  }

  return fetch(`${process.env.BACKEND_URL}/v2/company`, {
    method: "POST",
    body: form,
  })
    .then(handlers.form)
    .catch(handlers.error);
};

/**
 * Send PATCH request to update Company with jwt auth
 */
const patch = async (jwt, data, logo) => {
  const form = new FormData();

  form.append("company", JSON.stringify(data.company));

  if (logo) {
    form.append("logo", logo);
  }

  return fetch(`${process.env.BACKEND_URL}/v2/company`, {
    method: "PATCH",
    body: form,
    headers: {
      Authorization: `Bearer ${jwt}`,
    },
  })
    .then(handlers.form)
    .catch(handlers.error);
};

/**
 * Send GET request to get Company with jwt auth
 */
const get = async (jwt) => {
  return fetch(`${process.env.BACKEND_URL}/v2/company`, {
    method: "GET",
    headers: {
      Authorization: `Bearer ${jwt}`,
    },
  })
    .then(async (response) => {
      switch (response.status) {
        case 200:
          return {
            success: true,
            payload: await response.json(),
          };
        default:
          throw new Error(`Unexpected server response: ${response.status}`);
      }
    })
    .catch(handlers.error);
};

export default { post, patch, get, jwt };
