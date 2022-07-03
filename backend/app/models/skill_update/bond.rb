# frozen_string_literal: true

class SkillUpdate::Bond
  include Mongoid::Document

  field :unities, type: Array
  field :campus, type: String
  field :groups, type: Array

  embedded_in :skill_update_request,
    inverse_of: :bond

  validates :campus, inclusion: { within: campi_names }
  validate :max_three_groups, :min_one_unity, :only_existing_unities

  def max_three_groups
    errors.add(:groups, 'must be at most 3 groups') unless groups.size <= 3
  end

  def min_one_unity
    errors.add(:unities, 'must have at least 1 unity') unless unities.size >= 1
  end

  def only_existing_unities
    errors.add(:unities, 'must be all known') unless unities.all? { |uni| all_unities.include? uni }
  end
end