-- MySQL dump 10.13  Distrib 8.0.27, for Win64 (x86_64)
--
-- Host: localhost    Database: bank
-- ------------------------------------------------------
-- Server version	8.0.27

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `account`
--

DROP TABLE IF EXISTS `account`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `account` (
  `Ainstrumentscode` char(3) NOT NULL,
  `Accountnumber` char(13) NOT NULL,
  `Password` char(4) NOT NULL,
  `Acssn` char(5) NOT NULL,
  `Value` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`Ainstrumentscode`,`Accountnumber`),
  UNIQUE KEY `Accountnumber_UNIQUE` (`Accountnumber`),
  KEY `ac2fi_ainstrumentscode_idx` (`Ainstrumentscode`),
  KEY `ac2cu_acssn_idx` (`Acssn`),
  CONSTRAINT `ac2cu_acssn` FOREIGN KEY (`Acssn`) REFERENCES `customer` (`Cssn`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `ac2fi_ainstrumentscode` FOREIGN KEY (`Ainstrumentscode`) REFERENCES `financialinstruments` (`Instrumentscode`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account`
--

LOCK TABLES `account` WRITE;
/*!40000 ALTER TABLE `account` DISABLE KEYS */;
INSERT INTO `account` VALUES ('563','3513515353444','1234','14911',4000),('563','4096804268266','1234','51098',3599),('563','4756295724755','1234','62039',1822),('563','4937201947271','1234','58723',6162),('563','4968240680924','1234','13802',2463),('563','5310589038559','1234','58723',6484),('563','8467926748296','1234','20591',3522),('687','0194021941294','1234','51098',-132),('687','1788468468888','1234','62039',15),('687','6408649268026','1234','58723',-511),('687','7849734718171','1234','13802',20),('864','1670829682282','1234','69038',6642),('864','5636537482828','1234','92332',100),('864','6702769427629','1234','69038',7675),('864','7489147294477','1234','69038',6622);
/*!40000 ALTER TABLE `account` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `customer`
--

DROP TABLE IF EXISTS `customer`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `customer` (
  `Cssn` char(5) NOT NULL,
  `Id` char(13) NOT NULL,
  `Cname` varchar(10) NOT NULL,
  `Cphonenum` varchar(11) NOT NULL,
  `Credit` int NOT NULL DEFAULT '500',
  `Cmssn` char(5) NOT NULL,
  PRIMARY KEY (`Cssn`),
  UNIQUE KEY `Cssn_UNIQUE` (`Cssn`),
  UNIQUE KEY `Id_UNIQUE` (`Id`),
  KEY `Cmssn_idx` (`Cmssn`),
  CONSTRAINT `cu2ma_cmssn` FOREIGN KEY (`Cmssn`) REFERENCES `manager` (`Mssn`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `customer`
--

LOCK TABLES `customer` WRITE;
/*!40000 ALTER TABLE `customer` DISABLE KEYS */;
INSERT INTO `customer` VALUES ('13802','4612309281662','Ning','23957975831',555,'57922'),('14911','3411303825195','Wendy','39081084101',899,'23456'),('20591','5507138372922','Chen','48920481481',512,'23456'),('51098','5601147582912','Winter','01029394857',899,'57922'),('58723','5502133481922','Carina','43466267323',905,'12345'),('62039','9703223512795','Kai','60260249622',788,'57922'),('69038','8711303825195','Zizel','39571957915',450,'23456'),('92332','8303223512795','Jin','25235255266',114,'23456');
/*!40000 ALTER TABLE `customer` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `deadinstruments`
--

DROP TABLE IF EXISTS `deadinstruments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `deadinstruments` (
  `Dinstrumentscode` char(3) NOT NULL,
  PRIMARY KEY (`Dinstrumentscode`),
  CONSTRAINT `de2fi_dinstru` FOREIGN KEY (`Dinstrumentscode`) REFERENCES `financialinstruments` (`Instrumentscode`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `deadinstruments`
--

LOCK TABLES `deadinstruments` WRITE;
/*!40000 ALTER TABLE `deadinstruments` DISABLE KEYS */;
INSERT INTO `deadinstruments` VALUES ('563');
/*!40000 ALTER TABLE `deadinstruments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `financialinstruments`
--

DROP TABLE IF EXISTS `financialinstruments`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `financialinstruments` (
  `Instrumentscode` char(3) NOT NULL,
  `Category` int NOT NULL,
  `Standard` int NOT NULL,
  `Interest` decimal(4,3) NOT NULL,
  PRIMARY KEY (`Instrumentscode`),
  UNIQUE KEY `Goodscode_UNIQUE` (`Instrumentscode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `financialinstruments`
--

LOCK TABLES `financialinstruments` WRITE;
/*!40000 ALTER TABLE `financialinstruments` DISABLE KEYS */;
INSERT INTO `financialinstruments` VALUES ('123',0,999,0.099),('366',1,999,0.005),('386',1,0,0.563),('563',0,500,0.055),('687',1,500,0.067),('864',0,0,0.021);
/*!40000 ALTER TABLE `financialinstruments` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `history`
--

DROP TABLE IF EXISTS `history`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `history` (
  `Haccountnumber` char(13) NOT NULL,
  `Hindex` int NOT NULL,
  `Hvaluechange` int NOT NULL,
  PRIMARY KEY (`Haccountnumber`,`Hindex`),
  CONSTRAINT `hi2ac_haccountnumber` FOREIGN KEY (`Haccountnumber`) REFERENCES `account` (`Accountnumber`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `history`
--

LOCK TABLES `history` WRITE;
/*!40000 ALTER TABLE `history` DISABLE KEYS */;
INSERT INTO `history` VALUES ('4937201947271',6,-2311),('7489147294477',2,2321),('7849734718171',1,50);
/*!40000 ALTER TABLE `history` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `manager`
--

DROP TABLE IF EXISTS `manager`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `manager` (
  `Mssn` char(5) NOT NULL,
  `Mname` varchar(10) NOT NULL,
  `Position` int NOT NULL DEFAULT '0',
  `Password` varchar(4) DEFAULT NULL,
  PRIMARY KEY (`Mssn`),
  UNIQUE KEY `Mssn_UNIQUE` (`Mssn`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `manager`
--

LOCK TABLES `manager` WRITE;
/*!40000 ALTER TABLE `manager` DISABLE KEYS */;
INSERT INTO `manager` VALUES ('12345','Boss',0,'1234'),('14088','Pepe',1,'1234'),('23456','Rupi',1,'1234'),('42099','Sangdi',1,'1234'),('57922','Sancho',1,'1234'),('58475','Mina',1,'1234');
/*!40000 ALTER TABLE `manager` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-12-03 19:04:47
