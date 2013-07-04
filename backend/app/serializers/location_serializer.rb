class LocationSerializer < ActiveModel::Serializer
  #cached
  #delegate :cache_key, to: :object

  attributes :id, :name
  attributes :address
  attributes :latitude, :longitude
  attributes :open_at
  attributes :closed_at
  attributes :distance
  attributes :open_now

  def open_at
    object.hours_for_today.first.try(:utc).to_i
  end

  def closed_at
    object.hours_for_today.last.try(:utc).to_i
  end

  def distance
    object.distance
  end

  def open_now
    object.open_now?
  end
end
