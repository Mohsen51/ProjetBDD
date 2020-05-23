CREATE TABLE "Patient" (
  "Age" NUMBER NOT NULL,
  "ID" NUMBER NOT NULL PRIMARY KEY,
  "Prenom" VARCHAR2(255) NOT NULL,
  "Nom" VARCHAR2(255) NOT NULL,
  "ConnaissancePsy" VARCHAR2(255) NOT NULL
);


CREATE TABLE "Consultation" (
  "IDConsultation" NUMBER NOT NULL PRIMARY KEY,
  "DateRDV" DATE NOT NULL,
  "Prix" NUMBER NOT NULL,
  "Reglement" VARCHAR2(255) NOT NULL,
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

COMMIT;