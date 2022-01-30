# frozen_string_literal: true

class CompanyUpdatesController < ApplicationController
  def create
    ActionController::Parameters.permit_all_parameters = true
    params = create_params
    @comp_update = CompanyUpdateRequest.new
    @comp_update.dna_usp_stamp = DnaUspStamp.new(params[:dna_usp_stamp])
    @comp_update.company_data = CompanyDatum.new(params[:data])

    if @comp_update.valid?
      @comp_update.save
      render json: { company_update: @comp_update }
    else
      render json: { errors: @comp_update.errors.full_messages }, status: :bad_request
    end
  end

  private

  def create_params
    params.require(:company)
  end
end
