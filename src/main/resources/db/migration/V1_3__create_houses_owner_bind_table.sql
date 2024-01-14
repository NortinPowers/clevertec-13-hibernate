drop table if exists house_owner cascade;

create table house_owner
(
    house_id bigint not null
            references houses,
    owner_id bigint not null
            references persons,
    primary key (house_id, owner_id)
);
