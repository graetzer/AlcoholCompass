# encoding: UTF-8
# This file is auto-generated from the current state of the database. Instead
# of editing this file, please use the migrations feature of Active Record to
# incrementally modify your database, and then regenerate this schema definition.
#
# Note that this schema.rb definition is the authoritative source for your
# database schema. If you need to create the application database on another
# system, you should be using db:schema:load, not running all the migrations
# from scratch. The latter is a flawed and unsustainable approach (the more migrations
# you'll amass, the slower it'll run and the greater likelihood for issues).
#
# It's strongly recommended that you check this file into your version control system.

ActiveRecord::Schema.define(version: 20130704230553) do

  create_table "guestbooks", force: true do |t|
    t.string   "user"
    t.datetime "created_at"
    t.datetime "updated_at"
    t.integer  "location_id"
    t.text     "text"
    t.string   "url"
  end

  create_table "locations", force: true do |t|
    t.string   "name"
    t.text     "address"
    t.float    "latitude"
    t.float    "longitude"
    t.datetime "created_at"
    t.datetime "updated_at"
    t.text     "hours"
  end

  add_index "locations", ["latitude", "longitude"], name: "index_locations_on_latitude_and_longitude"

end
