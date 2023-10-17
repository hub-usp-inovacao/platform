# frozen_string_literal: true

class AboutCompany
  include Mongoid::Document

  field :description, type: String
  field :logo, type: String
  field :services, type: Array
  field :technologies, type: Array
  field :site, type: String
  field :odss, type: Array
  field :social_medias, type: Array

  validates :site, url: true

  validate :all_known_odss?

  embedded_in :company_update_request, inverse_of: :about_company

  def all_known_odss?
    known = [
      '1 - Erradicação da Pobreza',
      '2 - Fome Zero',
      '3 - Saúde e Bem Estar',
      '4 - Educação de Qualidade',
      '5 - Igualdade de Gênero',
      '6 - Água Potável e Saneamento',
      '7 - Energia Limpa e Acessível',
      '8 - Trabalho Decente e Crescimento Econômico',
      '9 - Indústria, Inovação e Infraestrutura',
      '10 - Redução das Desigualdades',
      '11 - Cidades e Comunidades Sustentáveis',
      '12 - Consumo e Produção Responsáveis',
      '13 - Ação Contra a Mudança Global do Clima',
      '14 - Vida na Água',
      '15 - Vida Terrestre',
      '16 - Paz, Justiça e Instituições Eficazes',
      '17 - Parcerias e Meios de Implementação'
    ]

    is_valid = !odss.nil? && odss.all? { |ods| known.include?(ods) }

    errors.add(:odss) unless is_valid
  end

  def format_logo_path(base_url)
    image_name = logo

    return if image_name.blank?

    self.logo = logo_path(image_name, base_url)
  end

  def self.csv_headers
    # rubocop:disable Layout/LineLength
    row_offset + [
      'Insira uma breve descrição da empresa',
      'Liste os principais produtos e/ou serviços oferecidos, separando-os por ; (ponto e vírgula)',
      'Caso a sua empresa atue no desenvolvimento de tecnologias, indique as 5 principais tecnologias desenvolvidas pela mesma separadas por ; (ponto e vírgula)',
      nil,
      'Site da empresa',
      nil,
      nil,
      nil,
      'Logotipo',
      'ODS',
      'Redes Sociais'
    ]
    # rubocop:enable Layout/LineLength
  end

  def prepare_to_csv
    AboutCompany.row_offset + [
      description_to_csv,
      services_to_csv,
      technologies_to_csv,
      nil,
      site,
      nil,
      nil,
      nil,
      logo,
      odss_to_csv,
      social_medias_to_csv
    ]
  end

  private

  def social_medias_to_csv
    nd_or_semi_comma(social_medias)
  end

  def odss_to_csv
    nd_or_semi_comma(odss)
  end

  def technologies_to_csv
    nd_or_semi_comma(technologies)
  end

  def services_to_csv
    nd_or_semi_comma(services)
  end

  def nd_or_semi_comma(value)
    value.empty? ? 'N/D' : value.join(';')
  end

  def description_to_csv
    description.size.positive? ? description : 'N/D'
  end

  # rubocop:disable Lint/IneffectiveAccessModifier
  def self.row_offset
    [nil] * 13
  end
  # rubocop:enable Lint/IneffectiveAccessModifier

  def logo_path(logo, base_url)
    return logo if logo.start_with?('http')

    "#{base_url}/api/uploads/logos/#{logo}"
  end
end
