# frozen_string_literal: true

class SkillUpdate::Request
  include Mongoid::Document

  embeds_one :confirmation
  embeds_one :resource
  embeds_one :bond
  embeds_one :personal
end