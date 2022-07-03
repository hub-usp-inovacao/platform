# frozen_string_literal: true

class SkillUpdate::Bond
  include Mongoid::Document

  field :unities, type: Array
  field :campus, type: String

  embedded_in :skill_update_request,
    inverse_of: :bond
end