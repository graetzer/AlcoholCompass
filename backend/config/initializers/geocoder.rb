# encoding: utf-8

Geocoder.configure({
  lookup: :google,
  timeout: 5,
  cache: Redis.new,
  units: :km,
})

ActiveSupport.on_load(:active_model_serializers) do
  # Disable for all serializers (except ArraySerializer)
  ActiveModel::Serializer.root = false

  # Disable for ArraySerializer
  ActiveModel::ArraySerializer.root = false
end
