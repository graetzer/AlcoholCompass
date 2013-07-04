require 'bundler/capistrano'

set :application, "webshop"
set :repository,  "file:///home/jer/git/alcohol_compass.git"
set :user, "jer"

set :scm, :git
set :branch, 'backend'

set :deploy_to, "/var/www/alcohol"
set :deploy_via, :remote_cache

role :web, "31.172.42.83"
role :app, "31.172.42.83"
role :db,  "31.172.42.83", :primary => true

set :subdir, "backend"

after "deploy:update_code", "deploy:checkout_subdir"

namespace :deploy do
  desc "Checkout subdirectory and delete all the other stuff"
  task :checkout_subdir do
    run "mv #{current_release}/#{subdir}/ /tmp && rm -rf #{current_release}/* && mv /tmp/#{subdir}/* #{current_release}"
  end
end

# if you want to clean up old releases on each deploy uncomment this:
# after "deploy:restart", "deploy:cleanup"

# if you're still using the script/reaper helper you will need
# these http://github.com/rails/irs_process_scripts

namespace :deploy do
  task :start do ; end
  task :stop do ; end
  task :restart, :roles => :app, :except => { :no_release => true } do
    run "touch #{File.join(current_path,'tmp','restart.txt')}"
  end
end

# Show password prompts
default_run_options[:pty] = true
