class Location < ActiveRecord::Base
  geocoded_by :address

  validates :name, presence: true

  after_validation :geocode

  attr_accessor :distance

  has_many :guestbooks

  def hours=(val)
    @hours = nil
    write_attribute :hours, val
  end

  def hours
    hours = read_attribute(:hours)
    return nil if hours.nil?

    @hours ||= JSON.parse hours
  end

  WEEK = (1..4) # Montag-Donnerstag
  NIGHT_HOURS = (0..7)

  def hours_for_today
    now = Time.zone.now
    night_hour = false
    if NIGHT_HOURS.include?(now.hour)
      night_hour = true
      now -= (now.hour + 1)
    end
    range = []

    return range if hours.blank?

    if hours['week'].present?
      range = to_range(*hours['week'])
    elsif hours['workdays'].present? && WEEK.include?(now.wday)
      range = to_range(*hours['workdays'])
    elsif hours['friday'].present? && now.wday == 5
      range = to_range(*hours['friday'])
    elsif hours['saturday'].present? && now.wday == 6
      range = to_range(*hours['saturday'])
    elsif hours['sunday'].present? && now.wday == 0
      range = to_range(*hours['sunday'])
    end

    unless range.empty?
      range = [(range[0]-1.day), (range[1]-1.day)]
    end
    range
  end

  def open_now?
    h = hours_for_today
    return false if h.empty?

    Time.now.between?(*hours_for_today)
  end

  def to_range start_time, end_time
    if start_time.empty? && end_time.empty?
      return []
    end

    if end_time.empty?
      return [(Time.zone.parse(start_time)-1.day), (1.year.from_now)]
    end

    if start_time.tr(':','').to_i < end_time.tr(':','').to_i
      return [Time.zone.parse(start_time), Time.zone.parse(end_time)]
    else
      return [Time.zone.parse(start_time), (Time.zone.parse(end_time)+1.day)]
    end
  end
end
