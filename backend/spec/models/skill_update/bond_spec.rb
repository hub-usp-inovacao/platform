# frozen_string_literal: true

require 'rails_helper'

RSpec.describe SkillUpdate::Bond, type: :model do
  let :codelab do
    [{
      name: 'USPCodeLab',
      initials: 'UCL',
      bond: 'Coordenador(a)',
      category: 'Outro',
      url: 'https://codelab.ime.usp.br'
    }]
  end

  let :bond do
    {
      unities: ['Instituto de Matemática e Estatística - IME'],
      campus: 'Butantã',
      groups: codelab
    }
  end

  it 'is invalid when there are more than 3 groups' do
    args = bond
    args[:groups] = [1, 2, 3, 4]
    inst = described_class.new args

    expect(inst).to be_invalid
  end

  it 'is invalid when unities are empty' do
    args = bond
    args[:unities] = []
    inst = described_class.new args
    expect(inst).to be_invalid
  end

  it 'is invalid when there are non existent unities' do
    args = bond
    args[:unities] = ['random']
    inst = described_class.new args
    expect(inst).to be_invalid
  end

  it 'is invalid with non existent campus' do
    args = bond
    args[:campus] = 'random'
    inst = described_class.new args
    expect(inst).to be_invalid
  end
end
