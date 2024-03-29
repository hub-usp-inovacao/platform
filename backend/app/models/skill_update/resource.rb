# frozen_string_literal: true

module SkillUpdate
  class Resource
    include Mongoid::Document

    field :skills, type: Array
    field :services, type: Array
    field :equipments, type: Array

    embedded_in :skill_update_request,
                inverse_of: :resource
  end
end
