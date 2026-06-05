db = db.getSiblingDB('plazoleta_trazabilidad');

db.createCollection('trazabilidad');

db.trazabilidad.createIndex({ idPedido: 1, fechaCambio: 1 });
