DELETE FROM QUALIFICATION WHERE ID_INTERNAL='965';
DELETE FROM QUALIFICATION WHERE ID_INTERNAL='966';


ALTER TABLE QUALIFICATION DROP INDEX U1;

ALTER TABLE QUALIFICATION ADD BRANCH varchar(100);
ALTER TABLE QUALIFICATION ADD SPECIALIZATION_AREA varchar(200);
ALTER TABLE QUALIFICATION ADD DEGREE_RECOGNITION varchar(200);
ALTER TABLE QUALIFICATION ADD EQUIVALENCE_DATE date;
ALTER TABLE QUALIFICATION ADD EQUIVALENCE_SCHOOL varchar(200);
ALTER TABLE QUALIFICATION ADD KEY_COUNTRY integer(11);
ALTER TABLE QUALIFICATION ADD DATE date not null;



