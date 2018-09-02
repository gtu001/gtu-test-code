--UTF8 文件
--1.4--------------------alter existing table:T_NOCLAIM_DISCNT_PREM
alter table T_NOCLAIM_DISCNT_PREM add DISCOUNT_METHOD VARCHAR2(2)  NULL;


--1.5--------------------commensts of tables and columns
comment on column T_NOCLAIM_DISCNT_PREM.DISCOUNT_METHOD is '折扣方式(人工補發的為空)';

--2----------------------update script of table structue
--2.2--------------------alter old table:T_NOCLAIM_DISCNT_PREM
alter table T_NOCLAIM_DISCNT_PREM add constraint FK_NOCLAIM_DISCNT_PREM__DISCO1 foreign key (DISCOUNT_METHOD) references T_NOCLM_DISCNT_METHOD (CODE);

--3----------------------update script of other object
