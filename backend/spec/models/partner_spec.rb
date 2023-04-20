# frozen_string_literal: true

require 'rails_helper'

RSpec.describe Partner, type: :model do
  let :attrs do
    {
      index: 1,
      name: 'Pedro de Tal',
      nusp: '7571332',
      bond: 'Aluno ou ex-aluno (graduação)',
      unity: 'Instituto de Matemática e Estatística - IME',
      email: 'pedro@mail.com',
      phone: '(11) 99999-7665',
      role: 'CEO'
    }
  end

  context 'with validation problems' do
    [0, 6].each do |val|
      it "on index out of range [index = #{val}]" do
        attrs[:index] = val
        expect(described_class.new(attrs)).to be_invalid
      end
    end

    it 'on name being too short' do
      attrs[:name] = ''
      expect(described_class.new(attrs)).to be_invalid
    end

    it 'on name being nil' do
      attrs.delete :name
      expect(described_class.new(attrs)).to be_invalid
    end

    it 'on unknown bond' do
      attrs[:bond] = 'foo'
      expect(described_class.new(attrs)).to be_invalid
    end

    it 'on unknown unity' do
      attrs[:unity] = 'foo'
      expect(described_class.new(attrs)).to be_invalid
    end

    it 'on malformed phone' do
      attrs[:phone] = 'foo'
      expect(described_class.new(attrs)).to be_invalid
    end
  end

  context 'with validation passing' do
    it 'accepts empty nusp' do
      attrs[:nusp] = ''
      expect(described_class.new(attrs)).to be_valid
    end

    it 'accepts empty unity' do
      attrs[:unity] = ''
      expect(described_class.new(attrs)).to be_valid
    end

    it 'accepts empty email' do
      attrs[:email] = ''
      expect(described_class.new(attrs)).to be_valid
    end

    it 'accepts empty phone' do
      attrs[:phone] = ''
      expect(described_class.new(attrs)).to be_valid
    end

    it 'accepts empty role' do
      attrs[:role] = ''
      expect(described_class.new(attrs)).to be_valid
    end
  end

  context 'with CSV preparation' do
    let :first_handmade do
      [nil] * 29 + [
        attrs[:name],
        attrs[:nusp],
        attrs[:bond],
        attrs[:unity],
        attrs[:email],
        attrs[:phone],
        attrs[:role]
      ]
    end

    let :fifth_handmade do
      [nil] * 54 + [
        attrs[:name],
        attrs[:nusp],
        attrs[:bond],
        attrs[:unity]
      ]
    end

    it 'prepares the first correctly' do
      attrs[:index] = 1
      partner = described_class.new(attrs)
      expect(partner.prepare_to_csv).to match_array(first_handmade)
    end

    it 'prepares the fifth correctly' do
      attrs[:index] = 5
      partner = described_class.new(attrs)
      expect(partner.prepare_to_csv).to match_array(fifth_handmade)
    end
  end
end
