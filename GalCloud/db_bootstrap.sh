DATABASES=("galEmailDb" "galGCSSentryDB" "galCertAuthDb" "galContinuousDb" "galHomesDb" "galHomesDataWarehouseDb" "galUsersDb" "galResourcesDb" "galTestDb")
MYSQL_USERNAME="galsie"
DEFAULT_USER_PWD="galsie123"

sudo mysql -e "CREATE USER IF NOT EXISTS '$MYSQL_USERNAME'@'localhost' IDENTIFIED BY '$DEFAULT_USER_PWD';"

for db in "${DATABASES[@]}"
do
  echo "Checking DB $db..."
	sudo mysql -e "CREATE DATABASE IF NOT EXISTS $db;"
	echo "\tCreated if not exists"
	sudo mysql -e "GRANT ALL PRIVILEGES ON $db.* TO '$MYSQL_USERNAME'@'localhost';"
	echo "\tGranted privileges"
	echo "\tDone!"
done
sudo mysql -e "FLUSH PRIVILEGES;"