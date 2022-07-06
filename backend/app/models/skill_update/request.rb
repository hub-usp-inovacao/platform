# frozen_string_literal: true

class SkillUpdate::Request
  include Mongoid::Document

  field :delivered, type: Boolean, default: false

  embeds_one :confirmation
  embeds_one :resource
  embeds_one :bond
  embeds_one :personal
end