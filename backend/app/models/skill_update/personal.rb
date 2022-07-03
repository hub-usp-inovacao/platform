# frozen_string_literal: true

class SkillUpdate::Personal
  include Mongoid::Document

  field :name, type: String
  field :nusp, type: String
  field :bond, type: String
  field :limitDate, type: Date
  field :phone, type: String
  field :personalPhone, type: String
  field :area, type: Hash
  field :keywords, type: Array

  embedded_in :skill_update_request,
    inverse_of: :personal
end