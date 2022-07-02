# frozen_string_literal: true

class SkillUpdate::Request
  include Mongoid::Document

  embeds_one :confirmation
  embeds_one :resource
end