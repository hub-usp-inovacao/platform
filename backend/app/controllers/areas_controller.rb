# frozen_string_literal: true

class AreasController < ApplicationController

  def index
    render json: { areas: knowledge_areas }
  end

end