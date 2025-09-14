#!/bin/sh

host="$1"
shift
port="$1"
shift

url="http://$host:$port/actuator/health"

echo "Waiting for $url..."

until curl -s "$url" | grep UP > /dev/null; do
  sleep 2
done

echo "$url is available. Starting service..."
exec "$@"