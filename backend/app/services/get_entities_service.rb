# frozen_string_literal: true

require 'rest-client'
require 'json'

class GetEntitiesService
  def self.run(model, with_report: true)
    @@model = model

    is_valid = request && cleanup && parse

    report if is_valid && with_report && @@warnings.length.positive?
  end

  def self.request
    model_name = @@model.name.upcase
    sheets_api_key = ENV['google_sheets_API_key']
    @@sheet_id = ENV["#{model_name}_SHEET_ID"]

    url = "#{base_url}/#{@@sheet_id}/values/'#{base_name}'?key=#{sheets_api_key}"
    response = RestClient.get url
    @@data = JSON.parse(response.body)['values']
  rescue RestClient::ExceptionWithResponse => e
    services_logger.debug "[Get#{@@model.name}Service::request] #{e}"
    @@data = nil
  end

  def self.parse
    @@warnings = []
    raw_entities = @@data.slice(1, @@data.size - 1)
    raw_entities.each_with_index do |row, index|
      @@model.create_from(row)
    rescue StandardError => e
      e = e.message.gsub(/"|\[|\]/, '')
      warning = "Linha: #{index + 2} - #{e}"
      services_logger.debug "[Get#{@@model.name}Service::parse] - #{warning}"
      @@warnings << warning
    end
  end

  def self.cleanup
    @@model.delete_all
  end

  def self.report
    Report.create({
                    entity: @@model.model_name.human.pluralize,
                    sheet_id: @@sheet_id,
                    warnings: @@warnings
                  })
  end

  def self.base_url
    'https://sheets.googleapis.com/v4/spreadsheets'
  end

  def self.base_name
    'Upload'
  end
end
