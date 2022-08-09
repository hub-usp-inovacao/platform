# frozen_string_literal: true

class UnitiesController < ApplicationController
  def unities
    render json: { unities: all_unities }
  end

  def campi
    render json: { campi: campi_names }
  end
end
