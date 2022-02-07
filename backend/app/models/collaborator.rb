class Collaborator
  include Mongoid::Document

  field :number_of_clt, type: String
  field :number_of_pj, type: String
  field :number_of_scholarship, type: String


  def prepare_to_csv
    Collaborator.row_offset + [
      number_of_clt,
      number_of_pj,
      number_of_scholarship
    ]
  end

  def self.row_offset
    [nil] * 58
  end
  
end

