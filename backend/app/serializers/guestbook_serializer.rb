class GuestbookSerializer < ActiveModel::Serializer
  attributes :id, :user
  attributes :created_at
  attributes :text
  attributes :url

  def created_at
    object.created_at.try(:utc).to_i
  end
end
