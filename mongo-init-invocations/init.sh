#!/bin/sh

mongoimport \
  --username admin \
  --password passadmin \
  --authenticationDatabase admin \
  --db invocationdb \
  --collection monster_templates \
  --file /docker-entrypoint-initdb.d/monstres-with-loot-rate.json \
--jsonArray