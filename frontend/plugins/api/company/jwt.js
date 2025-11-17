const BACKEND_URL = process.env.BACKEND_URL;

export default {
  post: (cnpj) =>
    fetch(`${BACKEND_URL}/v2/company/jwt`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ cnpj }),
    })
      .then((response) => {
        switch (response.status) {
          case 200: // Created
            return true;
          case 404: // Not Found
            return false;
          default:
            throw new Error(`Unexpected server response: ${response.status}`);
        }
      })
};
