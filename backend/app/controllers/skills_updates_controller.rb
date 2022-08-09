# frozen_string_literal: true

class SkillsUpdatesController < ApplicationController
  def request_update
    params = request_update_params

    email = params[:email]
    @skill = Skill.where({ email: email }).first
    if @skill.nil?
      render json: { error: 'skill not found' }, status: :bad_request
      return
    end

    token = TokenManager.create_token({ email: email })
    ApplicationMailer.skill_update_token(email, token).deliver_now
    render json: { message: 'ok' }, status: :ok
  end

  def update
    request = SkillUpdate::Request.new(update_params)

    request.save

    render json: { request: request }, status: :created
  end

  private

  def request_update_params
    params.require(:update_request).permit(:email)
  end

  def update_params
    params.require(:skill).permit(confirmation: {}, resource: {}, bond: {}, personal: {})
  end
end
