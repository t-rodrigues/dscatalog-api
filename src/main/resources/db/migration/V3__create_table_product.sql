create table tb_product(
    id          bigint auto_increment,
    name        varchar(150)   not null,
    description text           not null,
    price       decimal(10, 2) not null,
    image_url   varchar(255),
    date        timestamp without time zone,

    constraint product_pkey primary key (id)
);

create table tb_product_category(
    product_id  bigint,
    category_id bigint,

    primary key (product_id, category_id),
    foreign key (product_id) references tb_product (id),
    foreign key (category_id) references tb_category (id)
);
