class AddTextAndImageToGuestbook < ActiveRecord::Migration
  def change
    add_column :guestbooks, :text, :text
    add_column :guestbooks, :url, :string
  end
end
