class SkillsUpdatesController < ApplicationController
  def request_update
    params = request_update_params

    email = params[:email]
    @skill = Skill.where({ email: email }).first
    if @skill.nil?
      render json: { error: 'skill not found' }, status: :bad_request
      return
    end

    skill = @skill.email
    token = TokenManager.create_token({ email: email })
    ApplicationMailer.skill_update_token(email, token).deliver_now
    render json: { message: 'ok' }, status: :ok
  end

  def update
    conf = SkillUpdate::Confirmation.new(update_params[:confirmation])
    res = SkillUpdate::Resource.new(update_params[:resources])
    bond = SkillUpdate::Bond.new(update_params[:bond])

    request = SkillUpdate::Request.new()
    request.confirmation = conf
    request.resource = res
    request.bond = bond

    request.save

    render json: { request: request }, status: :created
  end

  private

  def request_update_params
    params.require(:update_request).permit(:email)
  end

  def update_params
    params.require(:skill).permit(confirmation: {}, resources: {}, bond: {})
  end
end
