# frozen_string_literal: true

def log(task_name, message)
  p "[#{task_name}|#{Time.zone.now}] #{message}"
end

desc 'Sends mails with all the skills updates'
task skill_update_report: :environment do
  log(:skill_update_report, 'started')

  reqs = SkillUpdate::Request.where({ delivered: false })
  csv = SkillUpdate::CsvParser.new

  sheet = csv.parse_records(reqs)

  ApplicationMailer.skill_update_data(sheet).deliver_now

  reqs.each do |req|
    req.update(delivered: true)
  end
  log(:skill_update_report, 'ended')
end

desc 'Sends mails with all the errors not yet reported'
task mail_reports: :environment do
  log('mail_reports', 'mailing reports!')
  Report.where(delivered: false).find_each do |report|
    ApplicationMailer.with(warnings: report.warnings, sheet_id: report.sheet_id,
                           entity: report.entity).warnings.deliver_now
    report.update(delivered: true)
  end
  log('mail_reports', 'reports mailed!')
end

desc 'Reports all updated companies'
task mail_updates: :environment do
  log('mail_updates', 'mailing updated companies!')
  new_updates = CompanyUpdateRequest.where(delivered: false)

  if new_updates.length.positive?
    ApplicationMailer.update_companies.deliver_now

    new_updates.each do |company|
      company.update(delivered: true)
    end
  end
  log('mail_updates', 'updated companies mailed!')
end

desc 'Reports all new entries in the Conexão usp forms'
task mail_conexao: :environment do
  log('mail_conexao', 'mailing new Conexão USP entries!')
  new_entries = Conexao.where(delivered: false)

  if new_entries.length.positive?
    ApplicationMailer.with(entities: new_entries).conexao.deliver_now

    new_entries.each do |entry|
      entry.update(delivered: true)
    end
  end
  log('mail_conexao', 'new Conexão USP entries mailed!')
end
