DROP TABLE IF EXISTS stock;

CREATE TABLE stock (
    id BIGSERIAL PRIMARY KEY,
    produto_id BIGINT NOT NULL,
    quantidade INTEGER NOT NULL,
    atualizado TIMESTAMPTZ DEFAULT current_timestamp NOT NULL,
    criado TIMESTAMPTZ DEFAULT current_timestamp NOT NULL
);