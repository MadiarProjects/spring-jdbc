create table categories
(
    id   bigserial primary key,
    name varchar(255) not null unique
);
create table products
(
    id          bigserial primary key,
    name        varchar(255)                      not null,
    price       double precision                  not null,
    category_id bigint references categories (id) not null
);
insert into categories(name)
values ('Смартфоны'),('Ноутбуки'),('Наушники');