# frozen_string_literal: true

class ApplicationMailer < ActionMailer::Base
  default from: ENV['email_username'],
          to: ENV['email_username'],
          cc: ENV['email_dev_username']

  def subject(text)
    "Hub USP Inovação - #{text}"
  end

  def warnings
    @warnings = params[:warnings]
    @entity = params[:entity]
    @sheet_url = "https://docs.google.com/spreadsheets/d/#{params[:sheet_id]}"
    mail(subject: subject("Aviso semanal de #{@entity}"))
  end

  def update_companies
    attachments["updated-companies-#{Time.zone.today}.csv"] =
      { mime_type: 'text/csv', content: CompanyUpdateRequest.to_csv }
    mail(subject: subject('Novas empresas solicitaram atualização dos dados'))
  end

  def conexao
    @entities = params[:entities]

    @personal_labels = ['Nome', 'Email', 'Representa uma']
    @org_labels = ['Nome', 'CNPJ', 'Os dados são sigilosos?', 'Tamanho da empresa',
                   'Telefone', 'Endereço', 'Cidade','Site']
    @demand_labels = ['Descrição', 'Expectativa', 'Perfil de pesquisador desejado',
                      'Qual é a sua necessidade em relação a esses pesquisadores?']

    @entities.each do |entity|
      entity.images.each_with_index do |image, index|
        attachments["image-#{index + 1}-#{entity.org['name']}.jpeg"] = File.read(image.content.path)
      end
    end

    mail(to: ENV['email_conexao_username'],
         subject: subject('Novas demandas foram solicitadas no Conexão USP'))
  end

  def company_update_token(company_email, token)
    @token = token
    @base_url = ENV['JWT_AUDIENCE']

    mail(to: company_email,
         subject: subject('Token de segurança para atualização - Hub USPInovação'))
  end
end
