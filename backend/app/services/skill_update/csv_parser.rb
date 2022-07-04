# frozen_string_literal: true

require 'csv'

class SkillUpdate::CsvParser

  def parse_records(requests)
    rows = []
    rows << headers

    requests.each do |req|
      row = parse_record(req)
      rows << row
    end

    CSV.generate(headers: true, col_sep: "\t") do |csv|
      rows.each { |row| csv << row }
    end
  end

  private

  def parse_record(skill_update_request)
    @current_row = [nil] * 38

    parse_bond(skill_update_request.bond)
    parse_confirmation(skill_update_request.confirmation)
    parse_personal(skill_update_request.personal)
    parse_resource(skill_update_request.resource)

    @current_row
  end

  def parse_resource(resource)
    @current_row[23] = resource.skills.join(';')
    @current_row[24] = resource.services.join(';')
    @current_row[25] = resource.equipments.join(';')
  end

  def parse_personal(personal)
    @current_row[1] = personal.bond
    @current_row[2] = personal.name
    @current_row[4] = personal.personalPhone
    @current_row[26] = personal.area[:major]
    @current_row[27] = personal.area[:minors].join(';')
    @current_row[28] = personal.keywords.join(';')
    @current_row[31] = personal.phone
    @current_row[36] = personal.limitDate
    @current_row[37] = personal.nusp
  end

  def parse_confirmation(confirmation)
    @current_row[32] = confirmation.interest
    @current_row[33] = confirmation.allow
    @current_row[35] = confirmation.truthful
  end

  def parse_bond(bond)
    @current_row[5] = bond.unities[0]
    @current_row[6] = bond.campus
    @current_row[7] = bond.unities[1..-1].join(';')

    bond.groups.each_with_index do |group, i|
      @current_row[8 + i] = group["bond"]
      @current_row[9 + i] = group["category"]
      @current_row[10 + i] = group["name"]
      @current_row[11 + i] = group["initials"]
      @current_row[12 + i] = group["url"]
    end
  end

  def headers
    [
      "",
      "Qual o seu vínculo com a USP?",
      "Nome completo",
      "Endereço de e-mail",
      "Telefone fixo pessoal ou celular de contato",
      "Com qual unidade de ensino/pesquisa da USP é o seu vínculo principal (ex.: contratação)?",
      "Em que local está sediada a principal unidade de ensino/pesquisa a qual você está vinculado(a)?",
      "Você possui vínculo secundário com alguma outra unidade de ensino/pesquisa da USP e gostaria de registrar?",
      "Qual é o seu vínculo com este grupo de pesquisa e/ou laboratório?",
      "Em qual categoria este grupo de pesquisa / laboratório se enquadra?",
      "Qual o NOME do 1º grupo de pesquisa e/ou laboratório ao qual você está vinculado(a)?",
      "Qual a SIGLA do 1º grupo de pesquisa e/ou laboratório ao qual você está vinculado(a)?",
      "Indique o website do seu grupo de pesquisa/laboratório ou uma página na web a qual esteja vinculado(a):",
      "Qual é o seu vínculo com este grupo de pesquisa e/ou laboratório?",
      "Em qual categoria este grupo de pesquisa / laboratório se enquadra?",
      "Qual o NOME do 2º grupo de pesquisa e/ou laboratório ao qual você está vinculado(a)?",
      "Qual a SIGLA do 2º grupo de pesquisa e/ou laboratório ao qual você está vinculado(a)?",
      "Indique o website do seu grupo de pesquisa/laboratório ou uma página na web a qual esteja vinculado(a):",
      "Qual é o seu vínculo com este grupo de pesquisa e/ou laboratório?",
      "Em qual categoria este grupo de pesquisa / laboratório se enquadra?",
      "Qual o NOME do 3 grupo de pesquisa e/ou laboratório ao qual você está vinculado(a)?",
      "Qual a SIGLA do 3º grupo de pesquisa e/ou laboratório ao qual você está vinculado(a)?",
      "Indique o website do seu grupo de pesquisa/laboratório ou uma página na web a qual esteja vinculado(a):",
      "COMPETÊNCIAS - VALIDAÇÃO AUSPIN",
      "SERVIÇOS - VALIDAÇÃO AUSPIN",
      "EQUIPAMENTOS - VALIDAÇÃO AUSPIN",
      "Indique qual é a sua principal grande área de conhecimento (CNPq), atualmente",
      "Indique qual é a sua principal área de conhecimento (CNPq) atualmente",
      "PALAVRAS-CHAVE - VALIDAÇÃO AUSPIN",
      "Indique o endereço para acessar seu Currículo Lattes",
      "",
      "Telefone institucional (USP) para ser divulgado no Portal",
      "Indique qual o seu grau de interesse em realizar projetos de P&D&I em parcerias, seja com empresas (públicas ou privadas) ou órgãos de governo",
      "Você permite a divulgação destas informações para a comunidade através do Hub USPInovação",
      "",
      "Declaro que todas as informações listadas acima são verdadeiras",
      "Qual a data de término de seu vínculo com a USP?",
      "Número USP"
    ]
  end

end