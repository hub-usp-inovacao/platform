# frozen_string_literal: true

require 'csv'

class CompanyUpdateRequest
  include Mongoid::Document

  field :timestamp, type: Time
  field :delivered, type: Boolean, default: false

  embeds_one :dna_usp_stamp
  embeds_one :company_data
  embeds_one :about_company
  embeds_one :investment
  embeds_one :revenue
  embeds_one :incubation
  embeds_one :staff
  embeds_many :partners

  def self.csv_headers
    subsection_classes = [
      DnaUspStamp,
      CompanyDatum,
      AboutCompany,
      Investment,
      Revenue,
      Incubation,
      Staff,
      Partner
    ]

    headers = merge([['Carimbo de data']] + subsection_classes.map { |cls| cls.send :csv_headers })
    Rails.logger.debug headers
    headers
  end

  def self.to_csv
    CSV.generate(headers: true) do |csv|
      csv << csv_headers
      all.each do |cur|
        csv << merge([
                       [cur.timestamp],
                       cur.dna_usp_stamp.prepare_to_csv,
                       cur.company_data.prepare_to_csv,
                       cur.about_company.prepare_to_csv,
                       cur.investment.prepare_to_csv,
                       cur.revenue.prepare_to_csv,
                       cur.incubation.prepare_to_csv,
                       cur.staff.prepare_to_csv,
                       cur.partners.map(&:prepare_to_csv)
                     ])
      end
    end
  end

  def self.merge(sections)
    base = [nil] * 91
    base.each_with_index do |_b, i|
      sections.each do |sec|
        base[i] = base[i] || sec[i]
      end
    end

    base.map do |b|
      b || ''
    end
  end
end
