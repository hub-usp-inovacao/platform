# frozen_string_literal: true

class Report
  include Mongoid::Document

  field :entity, type: String
  field :sheet_id, type: String
  field :warnings, type: Array
  field :delivered, type: Boolean, default: false

  validates :entity, :sheet_id, :warnings, presence: true
end
