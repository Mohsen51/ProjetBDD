SELECT * FROM "Patient" p ;
DELETE FROM "Patient" p ;


ALTER TABLE "Patient" 
ADD "Sexe" VARCHAR2(1) NOT NULL;

ALTER TABLE "Patient" 
ADD "Email" VARCHAR2(255) NOT NULL;

ALTER TABLE "Patient" 
ADD "Password" VARCHAR2(255) NOT NULL;



CREATE TABLE "Admin" (
  "UserName" VARCHAR2(255) NOT NULL PRIMARY KEY, /*Have to be link with Email for the checking*/
  "Prenom" VARCHAR2(255) NOT NULL,
  "Nom" VARCHAR2(255) NOT NULL,
  "Password" VARCHAR2(255) NOT NULL
);

SELECT * FROM "Admin" ;

SELECT * FROM "Profession" p ;

INSERT INTO "Admin" ("UserName","Prenom","Nom","Password") VALUES ('psy', 'Nicolas', 'Batardot','admin');
INSERT INTO "Profession" ("IDProfession","IDPatient","Profession" ) VALUES (prof_seq.NEXTVAL , 10, 'Cultivatrice');



/* Test Patient user */
CREATE USER test
  IDENTIFIED BY patient
  DEFAULT TABLESPACE Projet_bdd
	QUOTA 20M on Projet_bdd;

GRANT create session TO test;
GRANT create VIEW TO test;

GRANT create USER TO psy;

CREATE USER "marie.jeanne@bio.com" IDENTIFIED BY legalize DEFAULT TABLESPACE Projet_bdd QUOTA 20M on Projet_bdd;
GRANT create session TO "marie.jeanne@bio.com";


COMMIT;
