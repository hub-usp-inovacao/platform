class Incubation
  include Mongoid::Document

  field :was_incubated, type: String
  field :ecosystem, type: String

  embedded_in :company_update_request, inverse_of: :incubation

  validates :was_incubated, presence: true
  validates :ecosystem, presence: true

  validate :expected_was_incubated?, :consistent_pair?

  def both_negative?
    was_incubated.eql?('Não') && ecosystem.eql?('Direto para o Mercado')
  end

  def both_positive?
    !was_incubated.eql?('Não') && !ecosystem.eql?('Direto para o Mercado')
  end

  def consistent_pair?
    is_valid = both_negative? || both_positive?

    errors.add(:was_incubated) unless is_valid
    errors.add(:ecosystem) unless is_valid
  end

  def expected_was_incubated?
    expected = expected_incubated_answers

    is_valid = expected.include?(was_incubated)
    
    errors.add(:was_incubated) unless is_valid
  end

  def expected_incubated_answers
    [
      'Não',
      'Sim. A empresa está incubada',
      'Sim. A empresa já está graduada'
    ]
  end

  def self.csv_headers
    row_offset + [
      'A empresa está incubada?',
      'Em qual incubadora ou parque tecnológico?'
    ]
  end

  def prepare_to_csv
    Incubation.row_offset + [
      was_incubated,
      ecosystem
    ]
  end

  def self.row_offset
    [nil] * 18
  end
end
