-- begin MENU_MENU
alter table MENU_MENU add constraint FK_MENU_MENU_ON_ROLE foreign key (ROLE_ID) references SEC_ROLE(ID)^
create unique index IDX_MENU_MENU_UNIQ_NAME on MENU_MENU (NAME)^
create unique index IDX_MENU_MENU_UNIQ_CODE on MENU_MENU (CODE)^
create index IDX_MENU_MENU_ON_ROLE on MENU_MENU (ROLE_ID)^
-- end MENU_MENU
