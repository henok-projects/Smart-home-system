#DATABASES=("galEmailDb" "galGCSSentryDB" "galCertAuthDb" "galContinuousDb" "galHomesDb" "galHomesDataWarehouseDb" "galUsersDb" "galResourcesDb" "galTestDb")
DATABASES=("galUsersDb")

MYSQL_USERNAME="galsie"
DEFAULT_USER_PWD="galsie123"

sudo mysql -e "CREATE USER IF NOT EXISTS '$MYSQL_USERNAME'@'localhost' IDENTIFIED BY '$DEFAULT_USER_PWD';"

for db in "${DATABASES[@]}"
do
  echo "Dropping DB $db..."
	sudo mysql -e "DROP DATABASE IF EXISTS $db;"
	echo "\tDropped if exists"
done