# frozen_string_literal: true

require 'rails_helper'

RSpec.describe SkillUpdate::Confirmation, type: :model do
  let :conf do
    {
      truthful: true,
      allow: false,
      interest: 'Não tenho interesse'
    }
  end

  it 'is invalid when truthful is false' do
    args = conf
    args[:truthful] = false
    inst = described_class.new args
    expect(inst).to be_invalid
  end

  it 'is invalid with empty interest' do
    args = conf
    args[:interest] = ''
    inst = described_class.new args
    expect(inst).to be_invalid
  end

  it 'is invalid with null interest' do
    args = conf
    args[:interest] = nil
    inst = described_class.new args
    expect(inst).to be_invalid
  end
end