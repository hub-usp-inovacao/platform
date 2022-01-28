import { formatURL, removeAccent } from "../format";

export class PDI {
  static ID = 1;
  static keys = [
    "inspect.name",
    "inspect.descriptionShort",
    "inspect.descriptionLong",
    "inspect.coordinator",
    "inspect.knowledge",
    "inspect.keywords",
  ];

  constructor(
    name,
    category,
    campus,
    unity,
    description,
    coordinator,
    id = null
  ) {
    this.inspect = {};

    this.id = PDI.ID++;

    this.central_id = id;

    this.name = name;
    this.inspect.name = removeAccent(this.name);

    this.category = category;
    this.campus = campus;
    this.unity = unity;
    this.description = description;
    this.inspect.descriptionShort = removeAccent(this.description.short || "");
    this.inspect.descriptionLong = removeAccent(this.description.long || "");
    this.coordinator = coordinator;
    this.inspect.coordinator = removeAccent(this.coordinator || "");

    this._keywords = [];
    this._knowledge = [];
    this._url = "";
    this._email = "";
    this._phone = "";
    this.services = [];
  }

  set keywords(rawColumn) {
    if (rawColumn != undefined && rawColumn != "") {
      this._keywords = rawColumn.split(/[,;]/);

      this.inspect.keywords = this._keywords.map(removeAccent);
    }
  }

  get keywords() {
    return this._keywords;
  }

  set url(rawColumn) {
    if (rawColumn != undefined && rawColumn != "") {
      this._url = formatURL(rawColumn);
    }
  }

  get url() {
    return this._url;
  }

  set knowledge(rawColumn) {
    if (rawColumn != undefined && rawColumn.length > 0) {
      this._knowledge = rawColumn.split(/[,;]/);

      this.inspect.knowledge = this._knowledge.map(removeAccent);
    }
  }

  get knowledge() {
    return this._knowledge;
  }

  set email(rawColumn) {
    if (rawColumn != undefined && rawColumn != "") {
      this._email = rawColumn;
    }
  }

  get email() {
    return this._email;
  }

  set phone(rawColumn) {
    if (rawColumn != undefined && rawColumn != "") {
      this._phone = rawColumn;
    }
  }

  get phone() {
    return this._phone;
  }

  addService(serviceName) {
    this.services.push(serviceName);
  }

  matchesFilter({ primary, terciary }) {
    let primaryMatch = true;
    let terciaryMatch = true;

    if (primary.length > 0) {
      primaryMatch = primary.includes(this.category);
    }

    const [campus, unidade] = terciary;

    if (campus) {
      terciaryMatch = this.campus === campus;
    }
    if (unidade) {
      terciaryMatch = this.unity === unidade;
    }

    return primaryMatch && terciaryMatch;
  }
}

export class PDIGenerator {
  static runForCentrais(args) {
    const { nome, sigla, endereco, unidade, sobre, observacoes } = args;
    const name = `${sigla} - ${nome}`;

    let campus = endereco.cidade;
    if (campus === "São Paulo") {
      campus = endereco.bairro === "Butantã" ? "Butantã" : "USP Leste";
    }

    const long = `${sobre}${observacoes}`
      .replace(/^\n+/, "")
      .replace(/\n/, " ");

    const description = {
      short: "",
      long,
    };

    const base = new PDI(
      name,
      "Centrais Multiusuário",
      campus,
      unidade.nome,
      description
    );

    base.phone = args.telefone1;
    base.email = args.email;
    if (args.site) base.url = formatURL(args.site);

    return base;
  }
}
