--UTF8 文件
--1.1--------------------sequnce script
create sequence S_PA_CONFIRM_ERROR_CASH__LIST_ start with 1;
--1.3--------------------create new table:T_PA_CONFIRM_ERROR_CASH
create table T_PA_CONFIRM_ERROR_CASH(
LIST_ID NUMBER(19)  NULL,
REL_LIST_ID NUMBER(19)  NULL,
FEE_ID NUMBER(19)  NULL,
INSERT_TIME DATE  NULL,
INSERT_TIMESTAMP DATE  NULL);

alter table T_PA_CONFIRM_ERROR_CASH add constraint FK_PA_CONFIRM_ERROR_CASH__REL_ foreign key (REL_LIST_ID) references T_PA_CONFIRM_ERROR (LIST_ID);
alter table T_PA_CONFIRM_ERROR_CASH add constraint PK_T_PA_CONFIRM_ERROR_CASH primary key (LIST_ID);
alter table T_PA_CONFIRM_ERROR_CASH add constraint FK_PA_CONFIRM_ERROR_CASH__FEE_ foreign key (FEE_ID) references T_CASH (FEE_ID);


--1.5--------------------commensts of tables and columns
comment on table T_PA_CONFIRM_ERROR_CASH is '紀錄未轉保收原因對應懸帳';
comment on column T_PA_CONFIRM_ERROR_CASH.LIST_ID is '主鍵';
comment on column T_PA_CONFIRM_ERROR_CASH.REL_LIST_ID is '關聯T_PA_CONFIRM_ERROR';
comment on column T_PA_CONFIRM_ERROR_CASH.FEE_ID is '對應T_CASH';
comment on column T_PA_CONFIRM_ERROR_CASH.INSERT_TIME is '系統日';
comment on column T_PA_CONFIRM_ERROR_CASH.INSERT_TIMESTAMP is '機器時間';

--2----------------------update script of table structue
--3----------------------update script of other object
