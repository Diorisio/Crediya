#!/bin/sh
set -e

# Primer argumento: host de MySQL
host="$1"
shift

# Variables de entorno necesarias: MYSQL_USER y MYSQL_PASSWORD
if [ -z "$MYSQL_USER" ] || [ -z "$MYSQL_PASSWORD" ]; then
  echo "Error: debes definir MYSQL_USER y MYSQL_PASSWORD"
  exit 1
fi

echo "Esperando a que MySQL en $host esté disponible..."

# Loop hasta que MySQL acepte conexiones
until mysql -h "$host" -u"$MYSQL_USER" -p"$MYSQL_PASSWORD" -e 'SELECT 1;' > /dev/null 2>&1; do
  echo "MySQL no disponible - esperando 3s..."
  sleep 3
done


echo "MySQL disponible, iniciando aplicación..."

# Ejecutar el comando que sigue (java -jar app.jar)
exec "$@"
