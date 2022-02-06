# frozen_string_literal: true

class CompanyUpdatesController < ApplicationController
  def create
    prms = create_params
    @comp_update = CompanyUpdateRequest.new
    @comp_update.timestamp = Time.zone.now
    @comp_update.dna_usp_stamp = DnaUspStamp.new(prms[:dna_usp_stamp])
    @comp_update.company_data = CompanyDatum.new(prms[:data])
    @comp_update.about_company = AboutCompany.new(prms[:about])
    @comp_update.investment = Investment.new(prms[:investment])
    @comp_update.revenue = Revenue.new(prms[:revenue])
    @comp_update.incubation = Incubation.new(prms[:incubation])

    partners = []
    prms[:partners].each_with_index do |raw_partner, index|
      partner = Partner.new(raw_partner.merge(index: index + 1))
      partners << partner
    end

    @comp_update.partners = partners

    if @comp_update.valid?
      @comp_update.save
      render json: { company_update: @comp_update }
    else
      render json: { errors: @comp_update.errors.full_messages }, status: :bad_request
    end
  end

  private

  def create_params
    params.require(:company).permit(
      partners: %i[
        name
        email
        phone
        nusp
        bond
        unity
      ],
      dna_usp_stamp: {},
      data: {},
      about: {},
      investment: {},
      revenue: {},
      incubation: {}
    )
  end
end
