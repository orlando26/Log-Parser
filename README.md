# Log-Parser
Log Parser java app

## Java
#### Instructions.
the LogParser java tool is a spring boot jar application that connects to a google cloud db.

run the jar file in the following format. the uploadLog parameter specify if we need to upload the log file to the DB.
by default the Log is already uploaded in the DB so if the value of the parameter is false the tool skips to upload the log and search for the
ips in the information already uploaded to the DB. if the value of the parameter is true then the tool first deletes any record in the Log table and uploads the log file to the table.

###### For skip the db upload
java -jar parser-0.0.1-SNAPSHOT.jar --startDate=2017-01-01.13:00:00 --duration=hourly --threshold=100 --uploadLog=false

###### For uploading first the log file to the db.
java -jar parser-0.0.1-SNAPSHOT.jar --startDate=2017-01-01.13:00:00 --duration=hourly --threshold=100 --uploadLog=true

## MySQL

#### Schema & tables
```
CREATE DATABASE `LogParser` 

CREATE TABLE `lp_log` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Log_Date` datetime NOT NULL,
  `Log_Ip` varchar(50) NOT NULL,
  `Log_Request` varchar(50) NOT NULL,
  `Log_Status` int(11) NOT NULL,
  `Log_User_Agent` varchar(500) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE `bloqued_ips` (
  `Id` int(11) NOT NULL AUTO_INCREMENT,
  `Ip` varchar(20) NOT NULL,
  `Comments` varchar(100) NOT NULL,
  PRIMARY KEY (`Id`)
) ENGINE=InnoDB AUTO_INCREMENT=399 DEFAULT CHARSET=latin1;
```

#### Query

```
select Log_Ip, Log_Status, count(*) from lp_log where Log_Date >= "2017-01-01 00:00:00" AND Log_Date <= "2017-01-02 00:00:00" group by Log_Ip having count(Log_Ip) > 50
```
