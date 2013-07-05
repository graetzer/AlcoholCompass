json.array!(@guestbooks) do |guestbook|
  json.extract! guestbook, :user
  json.url guestbook_url(guestbook, format: :json)
end
