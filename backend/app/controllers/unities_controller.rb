# frozen_string_literal: true

class UnitiesController < ApplicationController

  def unities
    render json: { unities: all_unities }
  end

end