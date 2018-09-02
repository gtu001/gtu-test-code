--UTF8 文件
--1.1--------------------sequnce script
create sequence S_NOCLAIM_DISCNT_ALLOCATE__LIS start with 1;
--1.3--------------------create new table:T_NOCLAIM_DISCNT_ALLOCATE
create table T_NOCLAIM_DISCNT_ALLOCATE(
LIST_ID NUMBER(19)  NOT NULL,
POLICY_ID NUMBER(19)  NOT NULL,
ITEM_ID NUMBER(19)  NOT NULL,
DISCOUNT_AMOUNT NUMBER(18,2)  NULL,
DISCOUNT_RATE NUMBER(4,2)  NULL,
DISCOUNT_METHOD VARCHAR2(2)  NOT NULL,
ALLOCATE_DATE DATE  NOT NULL,
VALIDATE_INDI CHAR(1)  NOT NULL,
CREATE_DATE DATE  NOT NULL,
INSERTED_BY NUMBER(19)  NOT NULL,
UPDATED_BY NUMBER(19)  NOT NULL,
INSERT_TIME DATE  NOT NULL,
UPDATE_TIME DATE  NOT NULL,
INSERT_TIMESTAMP DATE default  sysdate NOT NULL,
UPDATE_TIMESTAMP DATE default  sysdate NOT NULL);

alter table T_NOCLAIM_DISCNT_ALLOCATE add constraint PK_T_NOCLAIM_DISCNT_ALLOCATE primary key (LIST_ID);
alter table T_NOCLAIM_DISCNT_ALLOCATE add constraint FK_NOCLAIM_DISCNT_ALLOCATE__PO foreign key (POLICY_ID) references T_CONTRACT_MASTER (POLICY_ID);
alter table T_NOCLAIM_DISCNT_ALLOCATE add constraint FK_NOCLAIM_DISCNT_ALLOCATE__IT foreign key (ITEM_ID) references T_CONTRACT_PRODUCT (ITEM_ID);
alter table T_NOCLAIM_DISCNT_ALLOCATE add constraint FK_NOCLAIM_DISCNT_ALLOCATE__DI foreign key (DISCOUNT_METHOD) references T_NOCLM_DISCNT_METHOD (CODE);
alter table T_NOCLAIM_DISCNT_ALLOCATE add constraint FK_NOCLAIM_DISCNT_ALLOCATE__VA foreign key (VALIDATE_INDI) references T_YES_NO (YES_NO);
alter table T_NOCLAIM_DISCNT_ALLOCATE add constraint FK_NOCLAIM_DISCNT_ALLOCATE__IN foreign key (INSERTED_BY) references T_USER (USER_ID);
alter table T_NOCLAIM_DISCNT_ALLOCATE add constraint FK_NOCLAIM_DISCNT_ALLOCATE__UP foreign key (UPDATED_BY) references T_USER (USER_ID);


--1.5--------------------commensts of tables and columns
comment on table T_NOCLAIM_DISCNT_ALLOCATE is 'T_NOCLAIM_DISCNT_ALLOCATE';
comment on column T_NOCLAIM_DISCNT_ALLOCATE.LIST_ID is 'Sequence id';
comment on column T_NOCLAIM_DISCNT_ALLOCATE.POLICY_ID is '保單id';
comment on column T_NOCLAIM_DISCNT_ALLOCATE.ITEM_ID is '保項id';
comment on column T_NOCLAIM_DISCNT_ALLOCATE.DISCOUNT_AMOUNT is '一年的無理賠優惠金額(給付抵扣)';
comment on column T_NOCLAIM_DISCNT_ALLOCATE.DISCOUNT_RATE is '當年的無理賠優惠折扣率(保費折讓)';
comment on column T_NOCLAIM_DISCNT_ALLOCATE.DISCOUNT_METHOD is '無理賠優惠發放方式';
comment on column T_NOCLAIM_DISCNT_ALLOCATE.ALLOCATE_DATE is '發放某個年度的無理賠優惠，如果有復效日久是復效周年日，如果沒有復效就是保單周年日';
comment on column T_NOCLAIM_DISCNT_ALLOCATE.VALIDATE_INDI is '是否有效(作理賠後有可能取消)';
comment on column T_NOCLAIM_DISCNT_ALLOCATE.CREATE_DATE is '無理賠優惠派發動作執行的日期';
comment on column T_NOCLAIM_DISCNT_ALLOCATE.INSERTED_BY is 'Who was the record inserted by';
comment on column T_NOCLAIM_DISCNT_ALLOCATE.UPDATED_BY is 'Who was the record modified by';
comment on column T_NOCLAIM_DISCNT_ALLOCATE.INSERT_TIME is 'When was the record inserted';
comment on column T_NOCLAIM_DISCNT_ALLOCATE.UPDATE_TIME is 'When was the record last updated';
comment on column T_NOCLAIM_DISCNT_ALLOCATE.INSERT_TIMESTAMP is 'When was the record inserted at system time';
comment on column T_NOCLAIM_DISCNT_ALLOCATE.UPDATE_TIMESTAMP is 'When was the record last updated at system time';

--2----------------------update script of table structue
--3----------------------update script of other object
