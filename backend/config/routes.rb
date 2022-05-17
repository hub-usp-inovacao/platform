# frozen_string_literal: true

Rails.application.routes.draw do
  get '/disciplines', to: 'discipline#index'
  get '/companies', to: 'company#index'
  get '/patents', to: 'patent#index'
  get '/skills', to: 'skill#index'
  get '/iniciatives', to: 'iniciative#index'
  patch '/companies', to: 'company_updates#create'
  post '/companies/update_request', to: 'company_updates#request_update'
  post '/conexao', to: 'conexoes#create'
  post '/conexao/image', to: 'conexoes#create_image'
  post '/skills/update_request', to 'skill_updates#request_update'
  post '/company', to: 'company#protected_read_one'
end
