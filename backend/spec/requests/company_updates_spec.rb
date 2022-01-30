# frozen_string_literal: true

require 'rails_helper'

RSpec.describe 'CompanyUpdates', type: :request do
  let(:valid_data) do
    {
      data: {
        cnpj: '42.420.420/0001-21',
        public_name: 'test inc.',
        corporate_name: 'benefit of testing rails and other apps',
        year: 2019,
        cnae: '99.21-3-54',
        phones: [
          '(11) 99999-4433'
        ],
        emails: [
          'test@testinc.com'
        ],
        street: 'rua das couves, 37 - apt 51',
        neighborhood: 'vila vegetal',
        city: ['fito'],
        state: 'plantae',
        zipcode: '04331-000'
      },
      dna_usp_stamp: {
        wants: true,
        truthful_informations: true,
        permissions: [
          # rubocop:disable Layout/LineLength
          'Permito o envio de e-mails para ser avisado sobre eventos e oportunidades relevantes à empresa',
          'Permito a divulgação das informações públicas na plataforma Hub USPInovação',
          'Permito a divulgação das informações públicas na plataforma Hub USPInovação e também para unidades da USP'
          # rubocop:enable Layout/LineLength
        ],
        email: 'fulano@mail.com',
        name: 'fulano de tal'
      }
    }
  end

  describe 'PATCH /companies' do
    before do
      CompanyUpdateRequest.delete_all
    end

    %i[data dna_usp_stamp].each do |k|
      it "blocks requests that fails validation (missing #{k})" do
        valid_data.delete k
        patch '/companies', params: { company: valid_data }
        expect(response).to have_http_status(:bad_request)
      end
    end

    it 'returns the just-created object' do
      patch '/companies', params: { company: valid_data }
      body = JSON.parse response.body
      expect(body['company_update']['_id']).not_to be_nil
    end
  end
end
