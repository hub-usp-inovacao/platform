# frozen_string_literal: true

class SkillUpdate::Confirmation
  include Mongoid::Document

  field :truthful, type: Boolean
  field :allow, type: Boolean
  field :interest, type: String
  
  embedded_in :skill_update_request,
    inverse_of: :confirmation

  validates :interest, presence: true
  validate :truthful_to_be_true

  def truthful_to_be_true
    errors.add(:truthful, 'must be true') unless truthful
  end
end