isql $1<<!
DROP TABLE rsdfz221y;
CREATE TABLE rsdfz221y (       
        statistic_yyy CHAR(3) NOT NULL,        
        region CHAR(8) NOT NULL,
        admin_office_code CHAR(8) NOT NULL,
        site_id CHAR(8) NOT NULL,
        village VARCHAR(32) NOT NULL,
        gender CHAR(1) NOT NULL,
        education CHAR(2) NOT NULL,
        living CHAR(1) NOT NULL,
        age CHAR(3) NOT NULL,
        mrg_status CHAR(1) NOT NULL,
        cnt INTEGER NOT NULL
)
in $2 extent size $3 next size $4
lock mode row;
create unique index rsdfz221y_p_key on rsdfz221y(statistic_yyy,region,admin_office_code,site_id,village,gender,education,living,age,mrg_status);
create index rsdfz221y_s1_key on rsdfz221y(statistic_yyy,region,education);
create index rsdfz221y_s2_key on rsdfz221y(statistic_yyy,region,age);
create index rsdfz221y_s3_key on rsdfz221y(statistic_yyy,region,mrg_status);
!
