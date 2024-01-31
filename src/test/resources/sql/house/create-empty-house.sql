truncate table house_history cascade;

truncate table house_owner cascade;

truncate table persons cascade;
select setval('persons_id_seq', 1, false);

truncate table houses cascade;
select setval('houses_id_seq', 1, false);

insert into houses (uuid, area, country, city, street, number, create_date)
values ('8e7daff2-df9a-4130-94af-1b4bc6ed3beb'::uuid, 'minsk region', 'belarus', 'minsk', 'first', 1, '2024-01-28T11:17:00');
