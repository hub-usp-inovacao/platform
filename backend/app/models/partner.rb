# frozen_string_literal: true

class Partner
  include Mongoid::Document

  field :index, type: Integer
  field :name, type: String
  field :nusp, type: String
  field :bond, type: String
  field :unity, type: String
  field :email, type: String
  field :phone, type: String
  field :role, type: String

  embedded_in :company_update_request

  validates :index, inclusion: 1..5
  validates :name, length: { minimum: 1 }
  validates :unity, inclusion: all_unities + ['']
  validates :phone, phones: true

  validate :known_bonds?

  def known_bonds?
    bonds = [
      'Aluno ou ex-aluno (graduação)',
      'Aluno ou ex-aluno (pós-graduação)',
      'Aluno ou ex-aluno de pós-graduação do IPEN (Instituto de Pesquisas Energéticas e Nucleares)',
      'Docente aposentado / Licenciado',
      'Docente',
      'Pós-doutorando',
      'Pesquisador',
      'Empresa incubada ou graduada em incubadora associada à USP',
      'Nenhum'
    ]

    is_valid = bonds.include?(bond)

    errors.add(:bond, 'inválido. Tente atualizar o seu vínculo.') unless is_valid
  end

  def prepare_to_csv
    base = offset + [
      name,
      nusp,
      bond,
      unity_to_csv
    ]

    return base if index > 1

    base + [
      email,
      phone,
      role
    ]
  end

  def offset
    jump = index > 1 ? [nil] * 10 : []

    skip = index > 2 ? [nil] * 5 * (index - 2) : []

    Partner.row_offset + jump + skip
  end

  def unity_to_csv
    bond.eql?('Nenhum') ? '' : unity
  end

  def self.row_offset
    [nil] * 33
  end

  def self.csv_headers
    base = [
      'Nome',
      'Num. USP',
      'Vínculo com a Universidade',
      'Unidade com a qual tem vínculo',
      nil
    ]

    row_offset + base + [
      'Email',
      'Telefone',
      'Cargo',
      nil,
      nil
    ] + base * 4
  end
end
