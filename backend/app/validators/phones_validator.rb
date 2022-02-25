# frozen_string_literal: true

class PhonesValidator < ActiveModel::EachValidator
  def validate_each(record, attribute, value)
    return if list_of_phones? value

    error_message = 'devem ser números válidos. Ex: (dd) ddddd-dddd'

    record.errors[attribute] << (options[:message] || error_message)
  end

  def list_of_phones?(value)
    if value.is_a?(Array)
      value.all? do |val|
        phone_valid? val
      end
    else
      phone_valid? value
    end
  end

  def phone_valid?(phone)
    return true if phone.blank?

    raw = phone.gsub(/\D/, '')
    raw.match?(/^\d{8,13}$/)
  end
end
