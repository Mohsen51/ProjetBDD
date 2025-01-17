CREATE TABLESPACE Projet_bdd
   DATAFILE 'Projet_bdd.dbf' 
   SIZE  10m;
/

CREATE USER psy
  IDENTIFIED BY admin
  DEFAULT TABLESPACE Projet_bdd
	QUOTA 20M on Projet_bdd;
	
GRANT create session TO psy;
GRANT create table TO psy;
GRANT create view TO psy;
GRANT create any trigger TO psy;
GRANT create any procedure TO psy;
GRANT create sequence TO psy;
GRANT create synonym TO psy;
GRANT CREATE USER TO psy;
GRANT ALL PRIVILEGES to psy;

COMMIT;