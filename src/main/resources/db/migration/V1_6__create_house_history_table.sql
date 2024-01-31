drop table if exists house_history cascade;

create table house_history
(
    id   bigserial
            primary key,
    house_id bigint not null
            references houses,
    person_id bigint not null
            references persons,
    update_date   timestamp(6) not null,
    type status not null
);
