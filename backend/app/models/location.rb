class Location < ActiveRecord::Base
  geocoded_by :address

  validates :name, presence: true

  after_validation :geocode

  attr_accessor :distance

  def hours=(val)
    write_attribute :hours, val.to_json
  end

  def hours
    JSON.parse read_attribute(:hours)
  end

  def hours_for_today
  end
end
