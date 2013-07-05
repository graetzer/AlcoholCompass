class Guestbook < ActiveRecord::Base
  validates :user, presence: true
  validates :location_id, presence: true
  validate :location_exists

  belongs_to :location

  private
  def location_exists
    begin
      Location.find(self.location_id)
    rescue ActiveRecord::RecordNotFound
      errors.add(:location_id, "location_id foreign key must exist")
      false
    end
  end
end
