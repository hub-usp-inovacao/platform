async function registerCompany(data) {
  try {
    const url = "http://localhost:8080/company/register";

    const headers = new Headers();
    headers.append("Content-Type", "application/json");

    const response = await fetch(url, {
      method: "POST",
      headers,
      body: JSON.stringify(data),
    });

    const result = await response.json();

    if (response.status >= 200 && response.status < 300) {
      return { success: true, data: result };
    } else {
      return { success: false, error: result.mensagem || "Erro no servidor" };
    }
  } catch (error) {
    console.error("Erro ao registrar empresa:", error);
    return {
      success: false,
      error: "Falha de conexão com o servidor. Verifique sua conexão e tente novamente."
    };
  }
}

export default (_, inject) => {
  inject("registerCompany", registerCompany);
};
