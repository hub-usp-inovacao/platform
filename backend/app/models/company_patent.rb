# frozen_string_literal: true

class CompanyPatent
  include Mongoid::Document

  field :name, type: String
  field :code, type: String

  embedded_in :company_update_request
end
