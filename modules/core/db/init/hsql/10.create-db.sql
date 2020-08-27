-- begin MENU_MENU
create table MENU_MENU (
    ID varchar(36) not null,
    VERSION integer not null,
    CREATE_TS timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS timestamp,
    DELETED_BY varchar(50),
    --
    NAME varchar(255) not null,
    CODE varchar(255),
    PRIORITY integer,
    DESCRIPTION varchar(2000),
    CONFIG longvarchar,
    ROLE_ID varchar(36),
    --
    primary key (ID)
)^
-- end MENU_MENU
