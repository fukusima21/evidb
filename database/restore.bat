@ECHO OFF
REM
REM ÉTÉìÉvÉãÇcÇaçÏê¨
REM

chcp 65001
set PGCLIENTENCODING=UTF-8

SET PSQL="C:\Program Files\PostgreSQL\9.6\bin\psql.exe"
SET RESTORE="C:\Program Files\PostgreSQL\9.6\bin\pg_restore.exe"

%PSQL% -f ./createdb.sql -U postgres
%RESTORE% -U postgres -d dvdrental ./dvdrental.tar

%PSQL% -f ./comment.sql -U postgres -d dvdrental

pause

chcp 932

