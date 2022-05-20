class SkillUpdateRequest
  include Mongoid::Document

  field :delivered, type: Boolean, default: false

  embeds_one :email

end

