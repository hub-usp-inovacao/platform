class Collaborator
  include Mongoid::Document

  field :numberOfCTLEmployees, type: String
  field :numberOfPJColaborators, type: String
  field :numberOfInterns, type: String
  field :last_update, type: Time

  embedded_in :company_update_request, inverse_of: :collaborator

  validates :last_update, past_date: true
  validate :numberOfCTLEmployees_not_a_number?
  validate :numberOfPJColaborators_not_a_number?
  validate :numberOfInterns_not_a_number?

  def is_number? string
    true if Float(string) rescue false
  end

  def numberOfCTLEmployees_not_a_number?
    is_valid = is_number?(numberOfCTLEmployees) or numberOfCTLEmployees.nil?

    errors.add(:numberOfCTLEmployees) unless is_valid
  end

  def numberOfPJColaborators_not_a_number?
    is_valid = is_number?(numberOfPJColaborators) or numberOfPJColaborators.nil?

    errors.add(:numberOfPJColaborators) unless is_valid
  end

  def numberOfInterns_not_a_number?
    is_valid = is_number?(numberOfInterns) or numberOfInterns.nil?

    errors.add(:numberOfInterns) unless is_valid
  end

  def last_update_in_the_past?
    is_valid = last_update.nil? ||
               last_update < Time.zone.now

    errors.add(:last_update) unless is_valid
  end

  def self.csv_headers
    row_offset + [
      'Qual o número de funcionários contratados como CLT?',
      'Qual o número de colaboradores contratados como pessoa jurídica (PJ)?',
      'Qual o número de estagiários/bolsistas contratados?'
    ]
  end

  def prepare_to_csv
    Collaborator.row_offset + [
      numberOfCTLEmployees,
      numberOfPJColaborators,
      numberOfInterns
    ] + Collaborator.middle_offset + [
      last_update
    ]
  end

  def self.row_offset
    [nil] * 58
  end

  def self.middle_offset
    [nil] * 24
  end
end

