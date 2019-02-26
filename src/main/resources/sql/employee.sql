/*
SQLyog Enterprise Trial - MySQL GUI
MySQL - 5.7.20-log 
*********************************************************************
*/
/*!40101 SET NAMES utf8 */;

create table `employees` (
	 `id` int primary key auto_increment,
	`emp_no` double ,
	`birth_date` date ,
	`first_name` varchar (42),
	`last_name` varchar (48),
	`gender` varchar (3),
	`hire_date` date 
); 
insert into `employees` (`emp_no`, `birth_date`, `first_name`, `last_name`, `gender`, `hire_date`) values('10001','1953-09-02','Georgi','Facello','M','1986-06-26');
insert into `employees` (`emp_no`, `birth_date`, `first_name`, `last_name`, `gender`, `hire_date`) values('10002','1964-06-02','Bezalel','Simmel','F','1985-11-21');
insert into `employees` (`emp_no`, `birth_date`, `first_name`, `last_name`, `gender`, `hire_date`) values('10003','1959-12-03','Parto','Bamford','M','1986-08-28');
insert into `employees` (`emp_no`, `birth_date`, `first_name`, `last_name`, `gender`, `hire_date`) values('10004','1954-05-01','Chirstian','Koblick','M','1986-12-01');
insert into `employees` (`emp_no`, `birth_date`, `first_name`, `last_name`, `gender`, `hire_date`) values('10005','1955-01-21','Kyoichi','Maliniak','M','1989-09-12');
insert into `employees` (`emp_no`, `birth_date`, `first_name`, `last_name`, `gender`, `hire_date`) values('10006','1953-04-20','Anneke','Preusig','F','1989-06-02');
insert into `employees` (`emp_no`, `birth_date`, `first_name`, `last_name`, `gender`, `hire_date`) values('10007','1957-05-23','Tzvetan','Zielinski','F','1989-02-10');
insert into `employees` (`emp_no`, `birth_date`, `first_name`, `last_name`, `gender`, `hire_date`) values('10008','1958-02-19','Saniya','Kalloufi','M','1994-09-15');
insert into `employees` (`emp_no`, `birth_date`, `first_name`, `last_name`, `gender`, `hire_date`) values('10009','1952-04-19','Sumant','Peac','F','1985-02-18');
insert into `employees` (`emp_no`, `birth_date`, `first_name`, `last_name`, `gender`, `hire_date`) values('10010','1963-06-01','Duangkaew','Piveteau','F','1989-08-24');
insert into `employees` (`emp_no`, `birth_date`, `first_name`, `last_name`, `gender`, `hire_date`) values('10011','1953-11-07','Mary','Sluis','F','1990-01-22');
insert into `employees` (`emp_no`, `birth_date`, `first_name`, `last_name`, `gender`, `hire_date`) values('10012','1960-10-04','Patricio','Bridgland','M','1992-12-18');
insert into `employees` (`emp_no`, `birth_date`, `first_name`, `last_name`, `gender`, `hire_date`) values('10013','1963-06-07','Eberhardt','Terkki','M','1985-10-20');
insert into `employees` (`emp_no`, `birth_date`, `first_name`, `last_name`, `gender`, `hire_date`) values('10014','1956-02-12','Berni','Genin','M','1987-03-11');
insert into `employees` (`emp_no`, `birth_date`, `first_name`, `last_name`, `gender`, `hire_date`) values('10015','1959-08-19','Guoxiang','Nooteboom','M','1987-07-02');
insert into `employees` (`emp_no`, `birth_date`, `first_name`, `last_name`, `gender`, `hire_date`) values('10016','1961-05-02','Kazuhito','Cappelletti','M','1995-01-27');
insert into `employees` (`emp_no`, `birth_date`, `first_name`, `last_name`, `gender`, `hire_date`) values('10017','1958-07-06','Cristinel','Bouloucos','F','1993-08-03');
insert into `employees` (`emp_no`, `birth_date`, `first_name`, `last_name`, `gender`, `hire_date`) values('10018','1954-06-19','Kazuhide','Peha','F','1987-04-03');
insert into `employees` (`emp_no`, `birth_date`, `first_name`, `last_name`, `gender`, `hire_date`) values('10019','1953-01-23','Lillian','Haddadi','M','1999-04-30');
insert into `employees` (`emp_no`, `birth_date`, `first_name`, `last_name`, `gender`, `hire_date`) values('10020','1952-12-24','Mayuko','Warwick','M','1991-01-26');
insert into `employees` (`emp_no`, `birth_date`, `first_name`, `last_name`, `gender`, `hire_date`) values('10021','1960-02-20','Ramzi','Erde','M','1988-02-10');
insert into `employees` (`emp_no`, `birth_date`, `first_name`, `last_name`, `gender`, `hire_date`) values('10022','1952-07-08','Shahaf','Famili','M','1995-08-22');
insert into `employees` (`emp_no`, `birth_date`, `first_name`, `last_name`, `gender`, `hire_date`) values('10023','1953-09-29','Bojan','Montemayor','F','1989-12-17');
insert into `employees` (`emp_no`, `birth_date`, `first_name`, `last_name`, `gender`, `hire_date`) values('10024','1958-09-05','Suzette','Pettey','F','1997-05-19');
insert into `employees` (`emp_no`, `birth_date`, `first_name`, `last_name`, `gender`, `hire_date`) values('10025','1958-10-31','Prasadram','Heyers','M','1987-08-17');
insert into `employees` (`emp_no`, `birth_date`, `first_name`, `last_name`, `gender`, `hire_date`) values('10026','1953-04-03','Yongqiao','Berztiss','M','1995-03-20');
insert into `employees` (`emp_no`, `birth_date`, `first_name`, `last_name`, `gender`, `hire_date`) values('10027','1962-07-10','Divier','Reistad','F','1989-07-07');
insert into `employees` (`emp_no`, `birth_date`, `first_name`, `last_name`, `gender`, `hire_date`) values('10028','1963-11-26','Domenick','Tempesti','M','1991-10-22');
insert into `employees` (`emp_no`, `birth_date`, `first_name`, `last_name`, `gender`, `hire_date`) values('10029','1956-12-13','Otmar','Herbst','M','1985-11-20');
insert into `employees` (`emp_no`, `birth_date`, `first_name`, `last_name`, `gender`, `hire_date`) values('10030','1958-07-14','Elvis','Demeyer','M','1994-02-17');
