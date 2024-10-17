# frozen_string_literal: true

require 'rails_helper'

RSpec.describe SkillUpdate::Request, type: :model do
  let :conf do
    {
      truthful: true,
      allow: false,
      interest: 'Não tenho interesse'
    }
  end

  let :res do
    {
      skills: %w[Cooking Cleaning],
      services: %w[Programming Teaching],
      equipments: []
    }
  end

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

  let :pers do
    {
      name: 'João',
      nusp: '7654321',
      phone: '(11) 3677-8987',
      area: {
        major: 'Ciências Exatas e da Terra',
        minors: ['Ciência da Computação']
      },
      keywords: []
    }
  end

  let :params do
    {
      confirmation: conf,
      resource: res,
      bond:,
      personal: pers
    }
  end

  it 'creates correctly and directly from a hash with all information' do
    inst = described_class.new params
    expect(inst).to be_valid
  end
end
