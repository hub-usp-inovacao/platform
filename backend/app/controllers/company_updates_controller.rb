# frozen_string_literal: true

class CompanyUpdatesController < ApplicationController
  def create
    prms = create_params
    @comp_update = CompanyUpdateRequest.new
    @comp_update.timestamp = Time.now
    @comp_update.dna_usp_stamp = DnaUspStamp.new(prms[:dna_usp_stamp])
    @comp_update.company_data = CompanyDatum.new(prms[:data])
    @comp_update.about_company = AboutCompany.new(prms[:about])
    @comp_update.investment = Investment.new(prms[:investment])
    @comp_update.revenue = Revenue.new(prms[:revenue])
    @comp_update.incubation = Incubation.new(prms[:incubation])
    @comp_update.staff = Staff.new(prms[:staff])

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
      dna_usp_stamp: {},
      data: {},
      about: {},
      investment: {},
      revenue: {},
      incubation: {},
      staff: {}
    )
  end
end
