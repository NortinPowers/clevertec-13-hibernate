create extension if not exists "uuid-ossp";

--1 дом, с одним владельцем и 1 жильцом
insert into houses (uuid, area, country, city, street, number, create_date)
values (uuid_generate_v4(), 'minsk region', 'belarus', 'minsk', 'first', 0, current_timestamp);

select currval(pg_get_serial_sequence('houses', 'id'));

insert into persons (uuid, name, surname, gender, passport_series, passport_number, create_date, update_date, house_id)
values (uuid_generate_v4(), 'ivan', 'ivanov', 0, 'mp', '12q12a12z', current_timestamp, current_timestamp,
        currval(pg_get_serial_sequence('houses', 'id')));

insert into house_owner (house_id, owner_id)
values (currval(pg_get_serial_sequence('houses', 'id')), currval(pg_get_serial_sequence('persons', 'id')));

--2 дом, с одним владельцем и 3 жильцами
insert into houses (uuid, area, country, city, street, number, create_date)
values (uuid_generate_v4(), 'minsk region', 'belarus', 'minsk', 'second', 1, current_timestamp);

select currval(pg_get_serial_sequence('houses', 'id'));

insert into persons (uuid, name, surname, gender, passport_series, passport_number, create_date, update_date, house_id)
values (uuid_generate_v4(), 'petr', 'petrov', 0, 'mr', '23q23a23z', current_timestamp, current_timestamp,
        currval(pg_get_serial_sequence('houses', 'id')));

insert into house_owner (house_id, owner_id)
values (currval(pg_get_serial_sequence('houses', 'id')), currval(pg_get_serial_sequence('persons', 'id')));

insert into persons (uuid, name, surname, gender, passport_series, passport_number, create_date, update_date, house_id)
values (uuid_generate_v4(), 'janet', 'clark', 1, 'mr', '28g63a23z', current_timestamp, current_timestamp,
        currval(pg_get_serial_sequence('houses', 'id')));

insert into persons (uuid, name, surname, gender, passport_series, passport_number, create_date, update_date, house_id)
values (uuid_generate_v4(), 'mary', 'davis', 1, 'mr', '23q23lo9z', current_timestamp, current_timestamp,
        currval(pg_get_serial_sequence('houses', 'id')));

insert into persons (uuid, name, surname, gender, passport_series, passport_number, create_date, update_date, house_id)
values (uuid_generate_v4(), 'joshua', 'mckenzie', 0, 'mr', 'r5623a23z', current_timestamp, current_timestamp,
        currval(pg_get_serial_sequence('houses', 'id')));

--3 дом, без жильцов
insert into houses (uuid, area, country, city, street, number, create_date)
values (uuid_generate_v4(), 'minsk region', 'belarus', 'brest', 'third', '3', current_timestamp);

--4 дом, с двумя владельцами
insert into houses (uuid, area, country, city, street, number, create_date)
values (uuid_generate_v4(), 'minsk region', 'belarus', 'gomel', 'fourth', '4', current_timestamp);

select currval(pg_get_serial_sequence('houses', 'id'));

insert into persons (uuid, name, surname, gender, passport_series, passport_number, create_date, update_date, house_id)
values (uuid_generate_v4(), 'brian', 'ross', 0, 'mr', '22564a23z', current_timestamp, current_timestamp,
        currval(pg_get_serial_sequence('houses', 'id')));

insert into house_owner (house_id, owner_id)
values (currval(pg_get_serial_sequence('houses', 'id')), currval(pg_get_serial_sequence('persons', 'id')));

insert into persons (uuid, name, surname, gender, passport_series, passport_number, create_date, update_date, house_id)
values (uuid_generate_v4(), 'michael', 'hardy', 0, 'mr', '12f26a23z', current_timestamp, current_timestamp,
        currval(pg_get_serial_sequence('houses', 'id')));

insert into house_owner (house_id, owner_id)
values (currval(pg_get_serial_sequence('houses', 'id')), currval(pg_get_serial_sequence('persons', 'id')));

--5 дом, с двумя владельцем и 4 жильцами
insert into houses (uuid, area, country, city, street, number, create_date)
values (uuid_generate_v4(), 'minsk region', 'belarus', 'minsk', 'fifth', '5', current_timestamp);

select currval(pg_get_serial_sequence('houses', 'id'));

insert into persons (uuid, name, surname, gender, passport_series, passport_number, create_date, update_date, house_id)
values (uuid_generate_v4(), 'dan', 'price', 0, 'mb', '23q25ju3z', current_timestamp, current_timestamp,
        currval(pg_get_serial_sequence('houses', 'id')));

insert into house_owner (house_id, owner_id)
values (currval(pg_get_serial_sequence('houses', 'id')), currval(pg_get_serial_sequence('persons', 'id')));

insert into persons (uuid, name, surname, gender, passport_series, passport_number, create_date, update_date, house_id)
values (uuid_generate_v4(), 'mary', 'james', 1, 'br', '45g63a45z', current_timestamp, current_timestamp,
        currval(pg_get_serial_sequence('houses', 'id')));

insert into house_owner (house_id, owner_id)
values (currval(pg_get_serial_sequence('houses', 'id')), currval(pg_get_serial_sequence('persons', 'id')));

insert into persons (uuid, name, surname, gender, passport_series, passport_number, create_date, update_date, house_id)
values (uuid_generate_v4(), 'danna', 'collins', 1, 'mr', '223223o23', current_timestamp, current_timestamp,
        currval(pg_get_serial_sequence('houses', 'id')));

insert into persons (uuid, name, surname, gender, passport_series, passport_number, create_date, update_date, house_id)
values (uuid_generate_v4(), 'jerry', 'weaver', 0, 'nh', '4562d563z', current_timestamp, current_timestamp,
        currval(pg_get_serial_sequence('houses', 'id')));
