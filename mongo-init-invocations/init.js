db = db.getSiblingDB('invocationdb');

db.monster_templates.insertMany(
  JSON.parse(cat('/docker-entrypoint-initdb.d/monstres-with-loot-rate.json'))
);
