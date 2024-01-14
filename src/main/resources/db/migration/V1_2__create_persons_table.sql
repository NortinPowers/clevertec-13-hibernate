drop table if exists persons cascade;

create table persons
(
    id              bigserial
        primary key,
    uuid            uuid         not null,
    name            varchar(255),
    surname         varchar(255),
    gender          int,
    passport_series varchar(255) not null,
    passport_number varchar(255) not null,
    create_date     timestamp(6) not null,
    update_date     timestamp(6),
    house_id        bigint       not null
        references houses,
    unique (passport_series, passport_number)
);
