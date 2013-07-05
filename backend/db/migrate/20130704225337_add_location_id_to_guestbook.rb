class AddLocationIdToGuestbook < ActiveRecord::Migration
  def change
    add_column :guestbooks, :location_id, :integer
  end
end
