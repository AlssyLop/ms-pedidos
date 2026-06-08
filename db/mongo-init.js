db = db.getSiblingDB('plazoleta');

db.createCollection('trazabilidad');

db.trazabilidad.createIndex({ idPedido: 1, fechaCambio: 1 });
db.trazabilidad.createIndex({ idRestaurante: 1, fechaCambio: 1 });
db.trazabilidad.createIndex({ idCliente: 1, idPedido: 1 });
