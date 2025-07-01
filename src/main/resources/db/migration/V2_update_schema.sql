-- Migration V2 : Ajout d'une colonne "status" à la table "documents"
ALTER TABLE documents ADD COLUMN status VARCHAR(50) DEFAULT 'active';SELECT * FROM flyway_schema_history;