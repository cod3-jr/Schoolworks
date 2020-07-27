-- in case table already exists
drop table files;

create table files(id number, parentid number, name varchar2(2000), fsize number, type char(1));
copy files from '/filedata.csv' delimiter ',' csv header;

WITH filepaths (id, name) AS
(
    SELECT id, name
    FROM files
    WHERE parentid IS NULL

    UNION ALL

    SELECT gp.id, gps.name || '/' || gp.name
    FROM files gp
    JOIN filepaths gps ON gps.id = gp.parentid
)

SELECT 
  id, name
FROM
  filepaths
;