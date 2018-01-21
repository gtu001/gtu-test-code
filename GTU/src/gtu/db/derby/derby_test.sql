connect 'jdbc:derby://localhost:1527/myDB;create=true;user=user;password=1234';

create table restaurants(id integer, name varchar(20), city varchar(50));
insert into restaurants values (1, 'Irifunes', 'San Mateo');
insert into restaurants values (2, 'Estradas', 'Daly City');
insert into restaurants values (3, 'Prime Rib House', 'San Francisco');

