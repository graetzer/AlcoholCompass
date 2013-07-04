class LocationSerializer < ActiveModel::Serializer
  #cached
  #delegate :cache_key, to: :object

  attributes :id, :name
  attributes :address
  attributes :latitude, :longitude
  attributes :distance

  def distance
    object.distance
  end
end
