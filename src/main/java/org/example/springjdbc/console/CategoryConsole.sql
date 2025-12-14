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
values ('Смартфоны'),
       ('Ноутбуки'),
       ('Наушники');

create table tags
(
    id   bigserial primary key,
    name varchar(100) not null unique
);

create table products_tags
(
    product_id bigint references products (id),
    tag_id     bigint references tags (id),
    primary key (product_id, tag_id)
);


insert into tags (name)
values ('Специальная распродажа'),
       ('В топе'),
       ('Последняя партия');

select *, categories.name as category_name, tags.name as tags_name
from products
         join categories on products.category_id = categories.id
         join products_tags on products_tags.product_id = products.id
         join tags on products_tags.tag_id = tags.id;



select
    p.id          as product_id,
    p.name        as product_name,
    p.price       as product_price,
    c.id          as category_id,
    c.name        as category_name,
    t.id          as tag_id,
    t.name        as tag_name
from products p
         join categories c on p.category_id = c.id
         join products_tags pt on pt.product_id = p.id
         join tags t on pt.tag_id = t.id;
