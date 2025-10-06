/**
 * Send POST request to register Company
 */
export default async (data, logo) => {
  let promises = [];

  console.log(data, logo);

  // TODO: Upload logo to the kotlin backend
  if (logo) {
    const body = new FormData();

    body.append("company[logo]", logo);
    body.append("company[cnpj]", data.company.company_data.cnpj);

    promises.push(
      fetch(`${process.env.BACKEND_URL}/update_request/logo`, {
        method: "POST",
        body,
      })
        .then(() => ({
          errors: {},
        }))
        .catch((e) => ({
          errors: { logo: `Falha ao fazer upload da logo: ${e}` },
        })),
    );
  }

  promises.push(
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
            return response.json();
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
      })),
  );

  return Promise.allSettled(promises).then((responses) =>
    responses
      .map((response) => response?.value ?? {})
      .reduce(
        (acc, val) => ({
          ...acc,
          ...val,
          errors: Object.assign(acc?.errors ?? {}, val?.errors ?? {}),
        }),
        {},
      ),
  );
};
