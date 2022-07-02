# frozen_string_literal: true

class SkillUpdate::Confirmation
  include Mongoid::Document

  field :truthful, type: Boolean
  field :allow, type: Boolean
  field :interest, type: String
  
  embedded_in :skill_update_request,
    inverse_of: :confirmation
end