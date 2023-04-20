# frozen_string_literal: true

class CompanyDatum
  include Mongoid::Document

  field :cnpj, type: String
  field :public_name, type: String
  field :corporate_name, type: String
  field :year, type: Integer
  field :cnae, type: String
  field :registry_status, type: String
  field :phones, type: Array
  field :emails, type: Array
  field :street, type: String
  field :neighborhood, type: String
  field :city, type: Array
  field :state, type: String
  field :zipcode, type: String
  field :company_nature, type: String
  field :size, type: String

  validates :cnpj, cnpj: true, presence: true
  validates :cnae, cnae: true
  validates :phones, phones: true
  validates :emails, emails: true

  validate :not_empty_public_name?, :not_empty_corporate_name?, :current_or_past_year?,
           :valid_zipcode?, :array_of_cities?, :valid_registry_status?, :invalid_size_name?,
           :valid_company_nature?

  embedded_in :company_update_request, inverse_of: :company_data

  def valid_company_nature?
    return if company_nature.nil?

    is_valid = company_nature.match?(/\A\d{3}-\d{1}\s-\s([A-zÀ-ú0-9_ ()-]*)\Z/)

    errors.add(:company_nature) unless is_valid
  end

  def valid_registry_status?
    return if registry_status.nil?

    registry_options = [
      'Ativa',
      'Ativa Não Regular',
      'Baixada',
      'Inapta',
      'Nula',
      'Suspensa'
    ]

    errors.add(:registry_status) unless registry_options.include?(registry_status)
  end

  def invalid_size_name?
    return if size.nil?

    size_options = %w[MEI ME EPP DEMAIS]
    errors.add(:size) unless size_options.include?(size)
  end

  def array_of_cities?
    errors.add(:city) unless city.is_a?(Array)
  end

  def valid_zipcode?
    return if zipcode.nil?

    is_valid = zipcode.match?(/\A\d{5}-\d{3}\Z/)

    errors.add(:zipcode) unless is_valid
  end

  def current_or_past_year?
    current_or_past = !year.nil? && year <= Time.zone.today.year

    errors.add(:year) unless current_or_past
  end

  def not_empty_public_name?
    errors.add(:public_name) unless not_empty?(public_name)
  end

  def not_empty_corporate_name?
    errors.add(:corporate_name) unless not_empty?(corporate_name)
  end

  def self.csv_headers
    row_offset + [
      'CNPJ',
      'Nome Fantasia',
      'Razão Social',
      'Ano de fundação',
      'Porte',
      'CNAE',
      'Situação cadastral',
      'Telefones',
      'Emails',
      'Endereço',
      'Bairro',
      'Cidade',
      'Estado',
      'CEP',
      'Natureza Jurídica'
    ]
  end

  def prepare_to_csv
    CompanyDatum.row_offset + [
      cnpj,
      public_name,
      corporate_name,
      year,
      size,
      cnae,
      registry_status,
      phones_to_csv,
      emails_to_csv,
      street,
      neighborhood,
      city,
      state,
      zipcode,
      company_nature
    ]
  end

  private

  # rubocop:disable Lint/IneffectiveAccessModifier
  def self.row_offset
    [nil]
  end
  # rubocop:enable Lint/IneffectiveAccessModifier

  def phones_to_csv
    phones.join ';'
  end

  def emails_to_csv
    emails.join ';'
  end

  def not_empty?(value)
    !value.nil? && value.size.positive? && !value.eql?('N/D')
  end
end
