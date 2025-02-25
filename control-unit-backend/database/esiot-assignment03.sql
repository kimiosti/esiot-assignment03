-- *********************************************
-- * Standard SQL generation                   
-- *--------------------------------------------
-- * DB-MAIN version: 11.0.2              
-- * Generator date: Sep 14 2021              
-- * Generation date: Tue Feb 25 17:06:28 2025 
-- * LUN file:  
-- * Schema: SmartTemperatureMonitor/SQL 
-- ********************************************* 


-- Database Section
-- ________________ 

create database SmartTemperatureMonitor;


-- DBSpace Section
-- _______________


-- Tables Section
-- _____________ 

create table MEASUREMENTS (
     measureID int auto_increment,
     temperature float not null,
     measureDate date not null,
     measureTime time not null,
     state char(7) not null,
     openingLevel int not null,
     constraint ID_MEASUREMENTS_ID primary key (measureID));


-- Constraints Section
-- ___________________ 


-- Index Section
-- _____________ 

create unique index ID_MEASUREMENTS_IND
     on MEASUREMENTS (measureID);

