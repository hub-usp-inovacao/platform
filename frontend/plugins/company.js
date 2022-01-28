const companyConsts = {
  sizes: [
    "Microempresa",
    "Pequena Empresa",
    "Média Empresa",
    "Grande Empresa",
    "Unicórnio",
    "Não Informado",
  ],
};

export default (_, inject) => {
  inject("Company", companyConsts);
};
