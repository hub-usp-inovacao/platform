# frozen_string_literal: true

class CompanyUpdateRequest
  include Mongoid::Document

  embeds_one :dna_usp_stamp
  embeds_one :company_data
end
