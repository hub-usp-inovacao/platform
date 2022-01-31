# frozen_string_literal: true

class CompanyUpdateRequest
  include Mongoid::Document

  embeds_one :dna_usp_stamp
  embeds_one :company_data
  embeds_one :about_company
  embeds_one :investment
end
