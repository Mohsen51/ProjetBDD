SELECT * FROM "Patient" p ;
DELETE FROM "Patient" p ;
TRUNCATE  TABLE "Patient"  ;
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

INSERT INTO "Admin" ("UserName","Prenom","Nom","Password") VALUES ('psy', 'Nicolas', 'Batardot','admin');

