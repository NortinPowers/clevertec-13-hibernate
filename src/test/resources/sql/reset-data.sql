truncate table house_history cascade;

truncate table house_owner cascade;

truncate table persons cascade;
select setval('persons_id_seq', 1, false);

truncate table houses cascade;
select setval('houses_id_seq', 1, false);
