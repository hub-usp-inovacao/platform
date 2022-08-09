# frozen_string_literal: true

require 'rails_helper'

RSpec.describe SkillUpdate::Personal, type: :model do
  let :params do
    {
      name: 'Fulano',
      nusp: '7777777',
      bond: 'Professor Sênior',
      limitDate: '',
      phone: '(11) 3655-4228',
      personalPhone: '(11) 99655-4228',
      area: {
        major: 'Ciências Exatas e da Natureza',
        minors: ['Ciência da Computação']
      },
      keywords: %w[webdev programação]
    }
  end

  it 'is invalid without nusp' do
    args = params
    args[:nusp] = nil
    inst = described_class.new args
    expect(inst).to be_invalid
  end

  it 'is invalid with empty nusp' do
    args = params
    args[:nusp] = ''
    inst = described_class.new args
    expect(inst).to be_invalid
  end
end
