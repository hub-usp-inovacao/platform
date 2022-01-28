import { PDI } from "@/lib/classes/pdi";
import { formatURL } from "@/lib/format";

function runForCentrais(args) {
  const { nome, sigla, endereco, unidade, sobre, observacoes, id } = args;

  const name = `${sigla} - ${nome}`;

  let campus = endereco?.cidade;
  if (campus === "São Paulo") {
    campus = endereco.bairro === "Butantã" ? "Butantã" : "USP Leste";
  }

  const long =
    sobre || observacoes
      ? `${sobre}${observacoes}`.replace(/^\n+/, "").replace(/\n/, " ")
      : "";

  const description = {
    short: "",
    long,
  };

  const base = new PDI(
    name,
    "Centrais Multiusuário",
    campus,
    unidade.nome,
    description,
    null,
    id
  );

  base.phone = args.telefone1;
  base.email = args.email;
  if (args.site) base.url = formatURL(args.site);

  return base;
}

function addServiceToCentral(service, centrals) {
  const central = centrals.find((c) => c.central_id === service.central_id);

  central.addService(service.nome);
}

export async function fetchCentrals(isDev) {
  let objectsFromAPI = [];

  let baseURL = isDev
    ? "http://localhost:3000"
    : "https://uspmulti.prp.usp.br/api/public";

  let centraisURL = baseURL + "/centrais";
  let servicosURL = baseURL + "/servicos";

  let resp, centraisData, servicosData;

  try {
    resp = await fetch(centraisURL);
    centraisData = await resp.json();

    resp = await fetch(servicosURL);
    servicosData = await resp.json();
  } catch (error) {
    console.log("Error fetching from USPMulti...");
    console.log(error);
    console.log("Running fallback");

    baseURL = "http://hubuspinovacao.if.usp.br";

    centraisURL = baseURL + "/centrais";
    servicosURL = baseURL + "/servicos";

    resp = await fetch(centraisURL);
    centraisData = await resp.json();

    resp = await fetch(servicosURL);
    servicosData = await resp.json();
  } finally {
    objectsFromAPI = centraisData.map((d) => runForCentrais(d));

    servicosData.forEach((svc) => addServiceToCentral(svc, objectsFromAPI));
  }

  return objectsFromAPI;
}
