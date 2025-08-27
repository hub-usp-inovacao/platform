// Tabela de Natureza Jurídica 2021 - Estrutura Detalhada
export const naturezaJuridicaTable = {
  // 1 - Administração Pública
  "101-5": "Órgão Público do Poder Executivo Federal",
  "102-3": "Órgão Público do Poder Executivo Estadual ou do Distrito Federal",
  "103-1": "Órgão Público do Poder Executivo Municipal",
  "104-0": "Órgão Público do Poder Legislativo Federal",
  "105-8": "Órgão Público do Poder Legislativo Estadual ou do Distrito Federal",
  "106-6": "Órgão Público do Poder Legislativo Municipal",
  "107-4": "Órgão Público do Poder Judiciário Federal",
  "108-2": "Órgão Público do Poder Judiciário Estadual",
  "110-4": "Autarquia Federal",
  "111-2": "Autarquia Estadual ou do Distrito Federal",
  "112-0": "Autarquia Municipal",
  "113-9": "Fundação Pública de Direito Público Federal",
  "114-7": "Fundação Pública de Direito Público Estadual ou do Distrito Federal",
  "115-5": "Fundação Pública de Direito Público Municipal",
  "116-3": "Órgão Público Autônomo Federal",
  "117-1": "Órgão Público Autônomo Estadual ou do Distrito Federal",
  "118-0": "Órgão Público Autônomo Municipal",
  "119-8": "Comissão Polinacional",
  "121-0": "Consórcio Público de Direito Público (Associação Pública)",
  "122-8": "Consórcio Público de Direito Privado",
  "123-6": "Estado ou Distrito Federal",
  "124-4": "Município",
  "125-2": "Fundação Pública de Direito Privado Federal",
  "126-0": "Fundação Pública de Direito Privado Estadual ou do Distrito Federal",
  "127-9": "Fundação Pública de Direito Privado Municipal",
  "128-7": "Fundo Público da Administração Indireta Federal",
  "129-5": "Fundo Público da Administração Indireta Estadual ou do Distrito Federal",
  "130-9": "Fundo Público da Administração Indireta Municipal",
  "131-7": "Fundo Público da Administração Direta Federal",
  "132-5": "Fundo Público da Administração Direta Estadual ou do Distrito Federal",
  "133-3": "Fundo Público da Administração Direta Municipal",
  "134-1": "União",

  // 2 - Entidades Empresariais
  "201-1": "Empresa Pública",
  "203-8": "Sociedade de Economia Mista",
  "204-6": "Sociedade Anônima Aberta",
  "205-4": "Sociedade Anônima Fechada",
  "206-2": "Sociedade Empresária Limitada",
  "207-0": "Sociedade Empresária em Nome Coletivo",
  "208-9": "Sociedade Empresária em Comandita Simples",
  "209-7": "Sociedade Empresária em Comandita por Ações",
  "212-7": "Sociedade em Conta de Participação",
  "213-5": "Empresário (Individual)",
  "214-3": "Cooperativa",
  "215-1": "Consórcio de Sociedades",
  "216-0": "Grupo de Sociedades",
  "217-8": "Estabelecimento, no Brasil, de Sociedade Estrangeira",
  "219-4": "Estabelecimento, no Brasil, de Empresa Binacional Argentino-Brasileira",
  "221-6": "Empresa Domiciliada no Exterior",
  "222-4": "Clube/Fundo de Investimento",
  "223-2": "Sociedade Simples Pura",
  "224-0": "Sociedade Simples Limitada",
  "225-9": "Sociedade Simples em Nome Coletivo",
  "226-7": "Sociedade Simples em Comandita Simples",
  "227-5": "Empresa Binacional",
  "228-3": "Consórcio de Empregadores",
  "229-1": "Consórcio Simples",
  "230-5": "Empresa Individual de Responsabilidade Limitada (de Natureza Empresária)",
  "231-3": "Empresa Individual de Responsabilidade Limitada (de Natureza Simples)",
  "232-1": "Sociedade Unipessoal de Advogados",
  "233-0": "Cooperativas de Consumo",
  "234-8": "Empresa Simples de Inovação - Inova Simples",
  "235-6": "Investidor Não Residente",

  // 3 - Entidades sem Fins Lucrativos
  "303-4": "Serviço Notarial e Registral (Cartório)",
  "306-9": "Fundação Privada",
  "307-7": "Serviço Social Autônomo",
  "308-5": "Condomínio Edilício",
  "310-7": "Comissão de Conciliação Prévia",
  "311-5": "Entidade de Mediação e Arbitragem",
  "313-1": "Entidade Sindical",
  "320-4": "Estabelecimento, no Brasil, de Fundação ou Associação Estrangeiras",
  "321-2": "Fundação ou Associação Domiciliada no Exterior",
  "322-0": "Organização Religiosa",
  "323-9": "Comunidade Indígena",
  "324-7": "Fundo Privado",
  "325-5": "Órgão de Direção Nacional de Partido Político",
  "326-3": "Órgão de Direção Regional de Partido Político",
  "327-1": "Órgão de Direção Local de Partido Político",
  "328-0": "Comitê Financeiro de Partido Político",
  "329-8": "Frente Plebiscitária ou Referendaria",
  "330-1": "Organização Social (OS)",
  "331-0": "Demais Condomínios",
  "332-8": "Plano de Benefícios de Previdência Complementar Fechada",
  "399-9": "Associação Privada",

  // 4 - Pessoas Físicas
  "401-4": "Empresa Individual Imobiliária",
  "402-2": "Segurado Especial",
  "408-1": "Contribuinte individual",
  "409-0": "Candidato a Cargo Político Eletivo",
  "411-1": "Leiloeiro",
  "412-0": "Produtor Rural (Pessoa Física)",

  // 5 - Organizações Internacionais e Outras Instituições Extraterritoriais
  "501-0": "Organização Internacional",
  "502-9": "Representação Diplomática Estrangeira",
  "503-7": "Outras Instituições Extraterritoriais"
};

// Lista de códigos válidos para validação
export const codigosValidos = Object.keys(naturezaJuridicaTable);

// Função para buscar descrição pelo código
export const getDescricaoNaturezaJuridica = (codigo) => {
  return naturezaJuridicaTable[codigo] || null;
};

// Função para buscar código pela descrição
export const getCodigoNaturezaJuridica = (descricao) => {
  const codigo = Object.keys(naturezaJuridicaTable).find(
    key => naturezaJuridicaTable[key] === descricao
  );
  return codigo || null;
};

// Função para validar código
export const isCodigoValido = (codigo) => {
  return codigosValidos.includes(codigo);
};

// Função para buscar códigos por termo de pesquisa
export const buscarNaturezaJuridica = (termo) => {
  if (!termo) return [];
  
  const termoLower = termo.toLowerCase();
  const resultados = [];
  
  // Busca por código
  if (/^\d/.test(termo)) {
    Object.keys(naturezaJuridicaTable).forEach(codigo => {
      if (codigo.startsWith(termo)) {
        resultados.push({
          codigo,
          descricao: naturezaJuridicaTable[codigo]
        });
      }
    });
  }
  
  // Busca por descrição
  Object.entries(naturezaJuridicaTable).forEach(([codigo, descricao]) => {
    if (descricao.toLowerCase().includes(termoLower)) {
      resultados.push({ codigo, descricao });
    }
  });
  
  return resultados.slice(0, 10); // Limita a 10 resultados
};

export default {
  naturezaJuridicaTable,
  codigosValidos,
  getDescricaoNaturezaJuridica,
  getCodigoNaturezaJuridica,
  isCodigoValido,
  buscarNaturezaJuridica
};
