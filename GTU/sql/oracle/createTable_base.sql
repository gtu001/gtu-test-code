
create table check_apply (
        id varchar2(30) not null,
        year varchar2(4) , 
        month varchar2(2), 
        day varchar2(2), 
        phone varchar2(15),
        create_dt timestamp,
        PRIMARY KEY (id)
);

