CREATE TABLE "Patient" (
  "ID" NUMBER NOT NULL PRIMARY KEY,
  "Prenom" VARCHAR2(255) NOT NULL,
  "Nom" VARCHAR2(255) NOT NULL,
  "DOB" DATE NOT NULL,
  "ConnaissancePsy" VARCHAR2(255) NOT NULL,
  "Adresse" VARCHAR2(255) NOT NULL,
  "Sexe" VARCHAR2(1) NOT NULL,
  "Email" VARCHAR2(255) NOT NULL;
);

CREATE TABLE "Consultation" (
  "IDConsultation" NUMBER NOT NULL PRIMARY KEY,
  "DateRDV" DATE NOT NULL,
  "Prix" NUMBER,
  "Reglement" VARCHAR2(255),
  "Anxiete" NUMBER,
  "Note" VARCHAR2(255),
	"Couple" NUMBER(1)
);

CREATE TABLE "PatientConsultant" (
  "IDConsultation" NUMBER NOT NULL,
  "IDPatient" NUMBER NOT NULL,
	CONSTRAINT "PK_PatientConsultant" PRIMARY KEY ("IDConsultation","IDPatient"),
  CONSTRAINT "FK_IDPatient" FOREIGN KEY ("IDPatient")
    REFERENCES "Patient"("ID"),
  CONSTRAINT "FK_IDConsultation" FOREIGN KEY ("IDConsultation")
    REFERENCES "Consultation"("IDConsultation")
);

CREATE TABLE "Profession" (
  "IDProfession" NUMBER NOT NULL PRIMARY KEY,
  "IDPatient" NUMBER NOT NULL,
  "Profession" VARCHAR2(255) NOT NULL,
  CONSTRAINT "FK_Prof_IDPatient" FOREIGN KEY ("IDPatient")
    REFERENCES "Patient"("ID")
);

CREATE TRIGGER "PSY"."Pat" BEFORE INSERT ON "PSY"."Patient" REFERENCING OLD AS "OLD" NEW AS "NEW" FOR EACH ROW 
BEGIN
  SELECT pat_seq.NEXTVAL
  INTO   :new.id
  FROM   dual;
END;
/

CREATE TRIGGER "PSY"."Cons" BEFORE INSERT ON "PSY"."Consultation" REFERENCING OLD AS "OLD" NEW AS "NEW" FOR EACH ROW 
BEGIN
  SELECT cons_seq.NEXTVAL
  INTO   :new."IDConsultation"
  FROM   dual;
END;
/

CREATE TRIGGER "PSY"."Prof" BEFORE INSERT ON "PSY"."Profession" REFERENCING OLD AS "OLD" NEW AS "NEW" FOR EACH ROW 
BEGIN
  SELECT prof_seq.NEXTVAL
  INTO   :new."IDPatient"
  FROM   dual;
END;
/

CREATE SEQUENCE pat_seq START WITH 1;
CREATE SEQUENCE prof_seq START WITH 1;
CREATE SEQUENCE cons_seq START WITH 1;


COMMIT;