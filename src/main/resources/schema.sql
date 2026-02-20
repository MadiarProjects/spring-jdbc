drop table if exists products_tags;
drop table if exists tags;
drop table if exists products;
drop table if exists categories;


    create table if not exists categories
(
    id   bigserial primary key,
    name varchar(255) not null unique
);
create table if not exists products
(
    id          bigserial primary key,
    name        varchar(255)                      not null,
    price       double precision                  not null,
    category_id bigint references categories (id) not null
);

create table if not exists tags
(
    id bigserial primary key,
    name varchar(100) not null unique
);

create table if not exists products_tags
(
    product_id bigint references products(id),
    tag_id bigint references tags(id),
    primary key (product_id, tag_id)
);

