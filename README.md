# ProjetBDD

First create and account on Oracle and download oracle XE 11 from the following link :
https://www.oracle.com/database/technologies/xe-prior-releases.html

Once you have downloaded it, go in the folder bin of the repository where you installed oracle XE
(ex : D:\oraclexe\app\oracle\product\11.2.0\server\bin)

You can now run sqlplus.exe
Here connect and put as the username "sys as sysdba" enter and don't put any password just enter.
Wait and when you are connected SQL> must appear.
Here copy and paste the SQL script CREATE TABLESPACES AND USER
You should now have created the user psy.

Run again sqlplus.exe and now connect as "psy" with "admin" as password.
You should now be connected as psy (SQL>)
Here copy and paste the SQL script CREATE TABLES

Congratulations, the database should now be created.
