# frozen_string_literal: true

class Revenue
  include Mongoid::Document

  field :last_year, type: String

  embedded_in :company_update_request, inverse_of: :revenue

  validate :last_year_money?

  def last_year_money?
    rgx = /\A(R\$ )?[\d.,]+\Z/
    is_valid = !last_year.nil? && last_year.match?(rgx)

    errors.add(:last_year) unless is_valid
  end

  def self.csv_headers
    row_offset + [
      "Faturamento em #{1.year.ago.year}"
    ]
  end

  def prepare_to_csv
    Revenue.row_offset + [
      last_year
    ]
  end

  def self.row_offset
    [nil] * 70
  end
end
