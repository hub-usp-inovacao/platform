# frozen_string_literal: true

class SkillsUpdate
  include Mongoid::Document

  field :name, type: String
end
