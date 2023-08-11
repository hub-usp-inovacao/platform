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

  validate :valid_nusp?
  validate :known_bonds?
 
  def valid_nusp?
    is_valid = nusp.match?(/\A\d+\z/) || nusp.empty?
    errors.add(:nusp, 'deve ser um número válido') unless is_valid
  end


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
    base = [
      name,
      nusp,
      bond,
      unity_to_csv,
      nil
    ]

    return base if index > 1

    Partner.row_offset + base + [
      email,
      phone,
      role,
      nil,
      nil
    ]
  end

  def self.row_offset
    [nil] * 32
  end

  def unity_to_csv
    bond.eql?('Nenhum') ? '' : unity
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
