db = db.getSiblingDB('invocationdb');

var data = JSON.parse(
    _readFile('/docker-entrypoint-initdb.d/monstres-with-loot-rate.json')
);

db.monster_templates.insertMany(data);