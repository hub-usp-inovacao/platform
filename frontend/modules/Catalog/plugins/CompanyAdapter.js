function CompanyAdapter(axios) {
  const baseURL = process.env.BACKEND_URL + "/catalog";

  async function requestData() {
    const url = baseURL + "/companies";

    try {
      const { companies } = await axios.$get(url);
      return companies;
    } catch (error) {
      return [];
    }
  }

  async function filterData(prms) {
    const url = baseURL + "/companies";

    const params = Object.assign({}, prms);

    if (params.areaMajors?.length > 0) {
      params.areaMajors = params.areaMajors.join(",");
    }

    if (params.areaMinors?.length > 0) {
      params.areaMinors = params.areaMinors.join(",");
    }

    try {
      const { companies } = await axios.$get(url, { params });
      return companies;
    } catch (error) {
      return [];
    }
  }

  async function getEcosystems() {
    try {
      const { ecosystems } = await axios.$get(baseURL + "/ecosystems");
      return ecosystems;
    } catch (error) {
      return [];
    }
  }

  async function getCities() {
    try {
      const { cities } = await axios.$get(baseURL + "/company_cities");
      return cities;
    } catch (error) {
      return [];
    }
  }

  async function getUnities() {
    return [
            'Faculdade de Odontologia de Bauru - FOB',
            'Hospital de Reabilitação de Anomalias Craniofaciais - HRAC',
            'Escola de Comunicações e Artes - ECA',
            'Escola de Educação Física e Esporte - EEFE',
            'Escola Politécnica - EP',
            'Faculdade de Arquitetura e Urbanismo - FAU',
            'Faculdade de Ciências Farmacêuticas - FCF',
            'Faculdade de Economia, Administração e Contabilidade - FEA',
            'Faculdade de Educação - FE',
            'Faculdade de Filosofia, Letras e Ciências Humanas - FFLCH',
            'Faculdade de Medicina Veterinária e Zootecnia - FMVZ',
            'Faculdade de Odontologia - FO',
            'Hospital Universitário - HU',
            'Instituto de Astronomia, Geofísica e Ciências Atmosféricas - IAG',
            'Instituto de Biociências - IB',
            'Instituto de Ciências Biomédicas - ICB',
            'Instituto de Energia e Ambiente - IEE',
            'Instituto de Estudos Avançados - IEA',
            'Instituto de Estudos Brasileiros - IEB',
            'Instituto de Física - IF',
            'Instituto de Geociências - IGc',
            'Instituto de Matemática e Estatística - IME',
            'Instituto de Psicologia - IP',
            'Instituto de Química - IQ',
            'Instituto de Relações Internacionais - IRI',
            'Instituto Oceanográfico - IO',
            'Instituto de Pesquisas Energéticas e Nucleares - IPEN',
            'Centro de Biologia Marinha - CEBIMar',
            'Faculdade de Direito - FD',
            'Escola de Engenharia de Lorena - EEL',
            'Museu de Arqueologia e Etnologia - MAE',
            'Museu de Arte Contemporânea - MAC',
            'Museu de Zoologia - MZ',
            'Museu Paulista - MP',
            'Centro de Energia Nuclear na Agricultura - CENA',
            'Escola Superior de Agricultura "Luiz de Queiroz" - ESALQ',
            'Faculdade de Saúde Pública - FSP',
            'Faculdade de Medicina - FM',
            'Escola de Enfermagem - EE',
            'Instituto de Medicina Tropical de São Paulo - IMT',
            'Faculdade de Zootecnia e Engenharia de Alimentos - FZEA', 
            'Escola de Educação Física e Esporte de Ribeirão Preto - EEFERP',
            'Escola de Enfermagem de Ribeirão Preto - EERP',
            'Faculdade de Ciências Farmacêuticas de Ribeirão Preto - FCFRP',
            'Faculdade de Direito de Ribeirão Preto - FDRP',
            'Faculdade de Economia, Administração e Contabilidade de Ribeirão Preto - FEARP',
            'Faculdade de Filosofia, Ciências e Letras de Ribeirão Preto - FFCLRP',
            'Faculdade de Medicina de Ribeirão Preto - FMRP',
            'Faculdade de Odontologia de Ribeirão Preto - FORP',
            'Escola de Engenharia de São Carlos - EESC',
            'Instituto de Arquitetura e Urbanismo de São Carlos - IAU',
            'Instituto de Ciências Matemáticas e de Computação - ICMC',
            'Instituto de Física de São Carlos - IFSC',
            'Instituto de Química de São Carlos - IQSC',
            'Escola de Artes, Ciências e Humanidades - EACH'
          ].sort()
  }

  return {
    requestData,
    filterData,
    getEcosystems,
    getCities,
    getUnities
  };
}

export default (context, inject) => {
  const adapter = CompanyAdapter(context.$axios);
  inject("CompanyAdapter", adapter);
};
