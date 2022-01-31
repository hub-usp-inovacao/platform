# frozen_string_literal: true

class Investment
  include Mongoid::Document

  field :received, type: Boolean
  field :own, type: String
  field :angel, type: String
  field :venture, type: String
  field :equity, type: String
  field :pipe, type: String
  field :others, type: String

  embedded_in :company_update_request, inverse_of: :investment

  validate :types_only_money?, :data_consistent?

  def data_consistent?
    is_valid = !received || (
      !own.nil? &&
      !angel.nil? &&
      !equity.nil? &&
      !venture.nil? &&
      !pipe.nil? &&
      !others.nil?
    )

    errors.add(:received) unless is_valid
  end

  def types_only_money?
    rgx = /\A(R\$)?[\d.,]+\Z/
    %i[own angel venture equity pipe others].each do |type|
      attr = send(type)
      is_valid = attr.nil? || attr.match?(rgx)

      errors.add(type) unless is_valid
    end
  end

  def self.csv_headers
    row_offset + [
      'A empresa recebeu investimento?',
      nil,
      'Valor do Investimento Próprio',
      'Valor do Investimento Anjo',
      'Valor do Venture Capital',
      'Valor do Private Equity',
      'Valor do PIPE-FAPESP',
      'Valor do Outros'
    ]
  end

  def prepare_to_csv
    Investment.row_offset + [
      received_to_csv,
      nil,
      own,
      angel,
      venture,
      equity,
      pipe,
      others
    ]
  end

  private

  def self.row_offset
    [nil] * 64
  end

  def received_to_csv
    received ? 'Sim' : 'Não'
  end
end
