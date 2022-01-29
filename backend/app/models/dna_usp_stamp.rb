# frozen_string_literal: true

class DnaUspStamp
  include Mongoid::Document

  field :wants, type: Boolean
  field :truthful_informations, type: Boolean
  field :permissions, type: Array
  field :email, type: String
  field :name, type: String

  embedded_in :company_update_request, inverse_of: :dna_usp_stamp

  validate :known_permissions?, :consistent_request_of_stamp?

  def consistent_request_of_stamp?
    is_consistent = !wants || (name.size.positive? && email.size.positive?)

    errors.add(:wants) unless is_consistent
  end

  def known_permissions?
    known = [
      # rubocop:disable Layout/LineLength
      'Permito o envio de e-mails para ser avisado sobre eventos e oportunidades relevantes à empresa',
      'Permito a divulgação das informações públicas na plataforma Hub USPInovação',
      'Permito a divulgação das informações públicas na plataforma Hub USPInovação e também para unidades da USP'
      # rubocop:enable Layout/LineLength
    ]

    all_known = permissions.all? do |perm|
      known.include? perm
    end

    errors.add(:permissions) unless all_known
  end

  def prepare_to_csv
    [nil] * 23 + [
      wants,
      email_csv_value,
      name_csv_value,
      nil,
      truthful_informations,
      permissions_csv_value
    ]
  end

  private

  def permissions_csv_value
    permissions.join ';'
  end

  def email_csv_value
    csv_value_conditioned_wants(email)
  end

  def name_csv_value
    csv_value_conditioned_wants(name)
  end

  def csv_value_conditioned_wants(value)
    wants ? value : ''
  end
end
