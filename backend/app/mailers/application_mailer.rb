# frozen_string_literal: true

class ApplicationMailer < ActionMailer::Base
  default from: ENV['email_username'],
          to: ENV['email_username']

  def subject(text)
    "Hub USP Inovação - #{text}"
  end

  def skill_update_data(sheet)
    attachments["updated-skills-#{Time.zone.today}.csv"] = { mime_type: 'text/csv', content: sheet }

    mail(subject: subject('Atualização de Competências'),
         cc: [ENV['email_devs_username'], ENV['email_copy_username']])
  end

  def warnings
    @warnings = params[:warnings]
    @entity = params[:entity]
    @sheet_url = "https://docs.google.com/spreadsheets/d/#{params[:sheet_id]}"
    mail(subject: subject("Aviso semanal de #{@entity}"),
         cc: [ENV['email_devs_username'], ENV['email_copy_username']])
  end

  def update_companies
    attachments["updated-companies-#{Time.zone.today}.csv"] =
      { mime_type: 'text/csv', content: CompanyUpdateRequest.to_csv }
    mail(subject: subject('Novas empresas solicitaram atualização dos dados'),
         cc: [ENV['email_devs_username'], ENV['email_copy_username']])
  end

  def update_skills
    attachments["updated-skills-#{Time.zone.today}.csv"] =
      { mime_type: 'text/csv', content: SkillUpdateRequest.to_csv }
    mail(subject: subject('Novas competências solicitaram atualização dos dados'),
         cc: [ENV['email_devs_username'], ENV['email_copy_username']])
  end

  def conexao
    @entities = params[:entities]

    @personal_labels = ['Nome', 'Email', 'Representa uma']
    @org_labels = ['Nome', 'CNPJ', 'Tamanho da empresa',
                   'Telefone', 'Endereço', 'Cidade', 'Site']
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

  def conexao_confirmation(author_email)
    mail(to: author_email,
         subject: subject('Confirmação - ConexãoUSP'))
  end

  def company_update_token(company_email, token)
    @token = token
    @base_url = ENV['JWT_AUDIENCE']

    mail(to: company_email,
         subject: subject('Token de segurança para atualização - Hub USPInovação'))
  end

  def confirmation_company_update(company_update_request)
    @update_data = company_update_request

    email = @update_data.company_data.emails.first
    name = @update_data.company_data.public_name
    email_subject = subject("Confirmação de Atualização de Empresa - #{name}")

    mail(to: email, subject: email_subject)
  end

  def skill_update_token(skill_email, token)
    @token = token
    @base_url = ENV['JWT_AUDIENCE']

    mail(to: skill_email,
         subject: subject('Token de segurança para atualização - Hub USPInovação'))
  end
end
