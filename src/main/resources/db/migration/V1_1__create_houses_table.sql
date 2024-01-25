drop table if exists houses cascade;

create table houses
(
    id          bigserial
        primary key,
    uuid        uuid         not null
        unique,
    area        varchar(255),
    country     varchar(255),
    city        varchar(255),
    street      varchar(255),
    number      varchar(255),
    create_date timestamp(6) not null
);
