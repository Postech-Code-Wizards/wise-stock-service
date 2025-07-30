DROP TABLE IF EXISTS stock;

CREATE TABLE product (
    id BIGSERIAL PRIMARY KEY,
    produto_id BIGINT NOT NULL
);

CREATE TABLE stock (
    id BIGSERIAL PRIMARY KEY,
    produto_id BIGINT NOT NULL,
    quantidade INTEGER NOT NULL,
    atualizado TIMESTAMPTZ DEFAULT current_timestamp NOT NULL,
    criado TIMESTAMPTZ DEFAULT current_timestamp NOT NULL,
    constraint fk_product foreign key (produto_id) references product (id)
);