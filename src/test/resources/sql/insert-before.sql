truncate table house_history cascade;

truncate table house_owner cascade;

truncate table persons cascade;
select setval('persons_id_seq', 1, false);

truncate table houses cascade;
select setval('houses_id_seq', 1, false);

insert into houses (uuid, area, country, city, street, number, create_date)
values ('8e7daff2-df9a-4130-94af-1b4bc6ed3beb'::uuid, 'minsk region', 'belarus', 'minsk', 'first', 1, '2024-01-28T11:17:00');

insert into persons (uuid, name, surname, gender, passport_series, passport_number, create_date, update_date, house_id)
values ('d7f21e40-6a65-4060-946a-600798656f68'::uuid, 'john', 'gold', 0, 'mt', '12fh65ig1', '2024-01-28T11:15:00', '2024-01-28T11:16:00',
        currval(pg_get_serial_sequence('houses', 'id')));

insert into house_owner (house_id, owner_id)
values (currval(pg_get_serial_sequence('houses', 'id')), currval(pg_get_serial_sequence('persons', 'id')));
