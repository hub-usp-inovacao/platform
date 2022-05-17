class SkillsUpdate
  include Mongoid::Document

  field :name, type: String

  validates :name
end

def valid_name(name)
  !name.nil? && name.is_a?(String) && name.size.positive?
end
