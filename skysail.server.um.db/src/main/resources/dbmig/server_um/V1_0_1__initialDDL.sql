CREATE TABLE um_users(
    ID INTEGER NOT NULL ,
    PASSWORD VARCHAR(255) ,
    USERNAME VARCHAR(255) ,
    PRIMARY KEY(ID)
); 

CREATE TABLE um_roles(
    ID INTEGER NOT NULL ,
    NAME VARCHAR(255) ,
    PRIMARY KEY(ID)
); 

CREATE TABLE um_users_um_roles(
    SkysailUser_ID INTEGER NOT NULL ,
    roles_ID INTEGER NOT NULL ,
    PRIMARY KEY(
        SkysailUser_ID ,
        roles_ID
    )
); 

ALTER TABLE um_users_um_roles 
ADD CONSTRAINT FK_um_users_um_roles_roles_ID FOREIGN KEY(roles_ID) REFERENCES um_roles(ID); 

ALTER TABLE um_users_um_roles 
ADD CONSTRAINT FK_um_users_um_roles_SkysailUser_ID FOREIGN KEY(SkysailUser_ID) REFERENCES um_users(ID); 

CREATE TABLE SEQUENCE(
    SEQ_NAME VARCHAR(50) NOT NULL ,
    SEQ_COUNT DECIMAL(38) ,
    PRIMARY KEY(SEQ_NAME)
); 

INSERT INTO SEQUENCE(
    SEQ_NAME ,
    SEQ_COUNT
) values(
    'UM_SKYSAIL_USER_SEQ' ,
    0
); 

INSERT INTO SEQUENCE(
    SEQ_NAME ,
    SEQ_COUNT
) values(
    'UM_SKYSAIL_ROLE_SEQ' ,
    0
); 
