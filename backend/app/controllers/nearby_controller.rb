class NearbyController < ApplicationController
  respond_to :json

  def index
    data = location_params
    latitude  = data[:latitude].to_f
    longitude = data[:longitude].to_f
    distance  = (data[:distance] || 2).to_i

    @locations = Location.near([latitude, longitude], distance)

    @locations = @locations.map { |location|
      location.distance = location.distance_to([latitude, longitude])
      location
    }.sort_by { |s| s.distance }

    respond_with @locations
  end

  private
  def location_params
    params.permit(:latitude, :longitude, :distance)
  end
end
