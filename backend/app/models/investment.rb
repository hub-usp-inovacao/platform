# frozen_string_literal: true

class Investment
  include Mongoid::Document

  field :received, type: String
  field :investments, type: Array
  field :own, type: String
  field :angel, type: String
  field :venture, type: String
  field :equity, type: String
  field :pipe, type: String
  field :others, type: String

  embedded_in :company_update_request, inverse_of: :investment

  validate :types_only_money?, :data_consistent?

  def data_consistent?
    is_valid = (received == 'Não') || (
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
    rgx = /\A(R\$ )?[\d.,]+\Z/
    %i[own angel venture equity pipe others].each do |type|
      attr = send(type)
      is_valid = attr.nil? || attr.match?(rgx)

      errors.add(type) unless is_valid
    end
  end

  def self.csv_headers
    row_offset + [
      'A empresa recebeu investimento?',
      'Qual(is) investimento(s) a empresa recebeu?',
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
      investments_to_csv,
      own,
      angel,
      venture,
      equity,
      pipe,
      others
    ]
  end

  private

  def investments_to_csv
    nd_or_comma(investments)
  end

  def nd_or_comma(value)
    value.empty? ? 'N/D' : value.join(',')
  end

  # rubocop:disable Lint/IneffectiveAccessModifier
  def self.row_offset
    [nil] * 65
  end

  # rubocop:enable Lint/IneffectiveAccessModifier

  def received_to_csv
    received == 'Sim' ? 'Sim' : 'Não'
  end
end
