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
  field :last_update, type: Time

  embedded_in :company_update_request, inverse_of: :investment

  validate :types_only_money?, :data_consistent?, :last_update_in_the_past?

  def last_update_in_the_past?
    is_valid = last_update.nil? ||
               last_update < Time.now

    errors.add(:last_update) unless is_valid
  end

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
      nil,
      'Valor do Investimento Próprio',
      'Valor do Investimento Anjo',
      'Valor do Venture Capital',
      'Valor do Private Equity',
      'Valor do PIPE-FAPESP',
      'Valor do Outros'
    ] + middle_offset + [
      'Data da última atualização de Investimentos'
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
    ] + Investment.middle_offset + [
      last_update
    ]
  end

  private

  # rubocop:disable Lint/IneffectiveAccessModifier
  def self.row_offset
    [nil] * 64
  end

  def self.middle_offset
    [nil] * 19
  end
  # rubocop:enable Lint/IneffectiveAccessModifier

  def received_to_csv
    received ? 'Sim' : 'Não'
  end
end
