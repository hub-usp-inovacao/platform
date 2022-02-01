# frozen_string_literal: true

class PastDateValidator < ActiveModel::EachValidator
  def validate_each(record, attribute, value)
    return if past_date?(value)

    record.errors[attribute] << (options[:message] || 'deve ser uma data no passado')
  end

  def past_date?(date)
    date.nil? || date <= Time.zone.now
  end
end
