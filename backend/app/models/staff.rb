class Staff
  include Mongoid::Document

  field :number_of_CTL_employees, type: String
  field :number_of_PJ_colaborators, type: String
  field :number_of_interns, type: String
  field :last_update, type: Time

  embedded_in :company_update_request, inverse_of: :staff

  validates :last_update, past_date: true
  validate :numberOfCTL_employees_not_a_number?
  validate :number_of_PJ_colaborators_not_a_number?
  validate :number_of_interns_not_a_number?

  def is_number? string
    true if Float(string) rescue false
  end

  def numberOfCTL_employees_not_a_number?
    is_valid = number_of_CTL_employees.nil? or is_number?(number_of_CTL_employees) 

    errors.add(:number_of_CTL_employees) unless is_valid
  end

  def number_of_PJ_colaborators_not_a_number?
    is_valid = number_of_PJ_colaborators.nil? or is_number?(number_of_PJ_colaborators)

    errors.add(:number_of_PJ_colaborators) unless is_valid
  end

  def number_of_interns_not_a_number?
    is_valid = number_of_interns.nil? or is_number?(number_of_interns) 

    errors.add(:number_of_interns) unless is_valid
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
    Staff.row_offset + [
      number_of_CTL_employees,
      number_of_PJ_colaborators,
      number_of_interns
    ] + Staff.middle_offset + [
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

