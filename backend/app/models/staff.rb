# frozen_string_literal: true

class Staff
  include Mongoid::Document

  field :number_of_CLT_employees, type: Integer
  field :number_of_PJ_colaborators, type: Integer
  field :number_of_interns, type: Integer

  embedded_in :company_update_request, inverse_of: :staff

  validate :number_of_clt_employees_not_a_number?
  validate :number_of_pj_colaborators_not_a_number?
  validate :number_of_interns_not_a_number?

  def number?(integer)
    true if Float(integer)
  rescue StandardError
    false
  end

  def number_of_clt_employees_not_a_number?
    is_valid = number_of_CLT_employees.nil? || number?(number_of_CLT_employees)

    errors.add(:number_of_CLT_employees) unless is_valid
  end

  def number_of_pj_colaborators_not_a_number?
    is_valid = number_of_PJ_colaborators.nil? || number?(number_of_PJ_colaborators)

    errors.add(:number_of_PJ_colaborators) unless is_valid
  end

  def number_of_interns_not_a_number?
    is_valid = number_of_interns.nil? || number?(number_of_interns)

    errors.add(:number_of_interns) unless is_valid
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
      number_of_CLT_employees,
      number_of_PJ_colaborators,
      number_of_interns
    ]
  end

  def self.row_offset
    [nil] * 62
  end
end
