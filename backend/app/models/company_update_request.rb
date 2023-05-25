# frozen_string_literal: true

require 'csv'

class CompanyUpdateRequest
  include Mongoid::Document

  field :timestamp, type: Time
  field :delivered, type: Boolean, default: false

  embeds_one :company_data ## [nil] * 1 + 12 + [nil] * 61 + 4
  embeds_one :about_company ## [nil] * 13 + 11
  embeds_one :incubation ## [nil] * 24 + 2
  embeds_one :dna_usp_stamp ## [nil] * 26 + 6
  embeds_many :partners ## [nil] * 32 + 5 + 5 + 20
  embeds_one :investment ##   [nil] * 62 + 8
  embeds_one :revenue ## [nil] * 70 + 1
  embeds_one :staff ## [nil] * 71 + 3

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
    headers
  end

  def self.to_csv
    CSV.generate(headers: true) do |csv|
      csv << csv_headers
      all.each do |cur|
        partner_data = cur.partners.map(&:prepare_to_csv).flatten
        csv << merge([
                       [cur.timestamp],
                       cur.dna_usp_stamp.prepare_to_csv,
                       cur.company_data.prepare_to_csv,
                       cur.about_company.prepare_to_csv,
                       cur.investment.prepare_to_csv,
                       cur.revenue.prepare_to_csv,
                       cur.incubation.prepare_to_csv,
                       cur.staff.prepare_to_csv,
                       partner_data,
                     ])
      end
    end
  end

  def self.merge(sections)
    base = [nil] * 78
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
