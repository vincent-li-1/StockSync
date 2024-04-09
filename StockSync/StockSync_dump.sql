-- MySQL dump 10.13  Distrib 8.0.32, for Linux (x86_64)
--
-- Host: localhost    Database: StockSync
-- ------------------------------------------------------
-- Server version	8.0.32

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Item`
--

DROP TABLE IF EXISTS `Item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Item` (
  `item_id` int NOT NULL AUTO_INCREMENT,
  `item_name` varchar(100) DEFAULT NULL,
  `item_size` double DEFAULT NULL,
  `item_price` double DEFAULT NULL,
  PRIMARY KEY (`item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Item`
--

LOCK TABLES `Item` WRITE;
/*!40000 ALTER TABLE `Item` DISABLE KEYS */;
/*!40000 ALTER TABLE `Item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Shipment`
--

DROP TABLE IF EXISTS `Shipment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Shipment` (
  `shipment_id` int NOT NULL AUTO_INCREMENT,
  `shipment_dst` varchar(255) DEFAULT NULL,
  `shipment_orig` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`shipment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Shipment`
--

LOCK TABLES `Shipment` WRITE;
/*!40000 ALTER TABLE `Shipment` DISABLE KEYS */;
/*!40000 ALTER TABLE `Shipment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ShipmentItems`
--

DROP TABLE IF EXISTS `ShipmentItems`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `ShipmentItems` (
  `ship_items_id` int NOT NULL AUTO_INCREMENT,
  `shipment_id` int NOT NULL,
  `item_id` int NOT NULL,
  `quantity` int NOT NULL,
  PRIMARY KEY (`ship_items_id`),
  KEY `shipment_id` (`shipment_id`),
  KEY `item_id` (`item_id`),
  CONSTRAINT `ShipmentItems_ibfk_1` FOREIGN KEY (`shipment_id`) REFERENCES `Shipment` (`shipment_id`),
  CONSTRAINT `ShipmentItems_ibfk_2` FOREIGN KEY (`item_id`) REFERENCES `Item` (`item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ShipmentItems`
--

LOCK TABLES `ShipmentItems` WRITE;
/*!40000 ALTER TABLE `ShipmentItems` DISABLE KEYS */;
/*!40000 ALTER TABLE `ShipmentItems` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Warehouse`
--

DROP TABLE IF EXISTS `Warehouse`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `Warehouse` (
  `warehouse_id` int NOT NULL AUTO_INCREMENT,
  `warehouse_name` varchar(255) DEFAULT NULL,
  `warehouse_address` varchar(255) DEFAULT NULL,
  `warehouse_long` float DEFAULT NULL,
  `warehouse_lat` float DEFAULT NULL,
  PRIMARY KEY (`warehouse_id`),
  UNIQUE KEY `warehouse_id` (`warehouse_id`)
) ENGINE=InnoDB AUTO_INCREMENT=67 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Warehouse`
--

LOCK TABLES `Warehouse` WRITE;
/*!40000 ALTER TABLE `Warehouse` DISABLE KEYS */;
INSERT INTO `Warehouse` VALUES (1,'test warehouse','123 Main Street',NULL,NULL),(2,'Warehouse 2','2 Main Street, Madison WI',-100,0.8),(3,'Warehouse 3','3 Main Street, Madison WI',25,10),(4,'Warehouse 4','3 Main Street, Madison WI',25,10),(5,'Warehouse 5','3 Main Street, Madison WI',25,10),(6,'Warehouse 6','3 Main Street, Madison WI',25,10),(7,'Warehouse 7','3 Main Street, Madison WI',25,10),(8,'Warehouse 8','3 Main Street, Madison WI',25,10),(9,'Warehouse 9','3 Main Street, Madison WI',25,10),(10,'Warehouse 10','3 Main Street, Madison WI',25,10),(11,'Warehouse 11','3 Main Street, Madison WI',25,10),(12,'Warehouse 12','3 Main Street, Madison WI',25,10),(13,'Warehouse 13','3 Main Street, Madison WI',25,10),(14,'Warehouse 14','3 Main Street, Madison WI',25,10),(15,'Warehouse 15','3 Main Street, Madison WI',25,10),(16,'Warehouse 16','3 Main Street, Madison WI',25,10),(17,'Warehouse 17','3 Main Street, Madison WI',25,10),(18,'Warehouse 18','3 Main Street, Madison WI',25,10),(19,'Warehouse 19','3 Main Street, Madison WI',25,10),(20,'Warehouse 20','3 Main Street, Madison WI',25,10),(21,'Warehouse 21','3 Main Street, Madison WI',25,10),(22,'Warehouse 22','3 Main Street, Madison WI',25,10),(23,'Warehouse 23','3 Main Street, Madison WI',25,10),(24,'Warehouse 24','3 Main Street, Madison WI',25,10),(25,'Warehouse 25','3 Main Street, Madison WI',25,10),(26,'Warehouse 26','3 Main Street, Madison WI',25,10),(27,'Warehouse 27','3 Main Street, Madison WI',25,10),(28,'Warehouse 28','3 Main Street, Madison WI',25,10),(29,'Warehouse 29','3 Main Street, Madison WI',25,10),(30,'Warehouse 30','3 Main Street, Madison WI',25,10),(31,'Warehouse 31','3 Main Street, Madison WI',25,10),(32,'Warehouse 32','3 Main Street, Madison WI',25,10),(33,'Warehouse 33','3 Main Street, Madison WI',25,10),(34,'Warehouse 34','3 Main Street, Madison WI',25,10),(35,'Warehouse 35','3 Main Street, Madison WI',25,10),(36,'Warehouse 36','3 Main Street, Madison WI',25,10),(37,'Warehouse 37','3 Main Street, Madison WI',25,10),(38,'Warehouse 38','3 Main Street, Madison WI',25,10),(39,'Warehouse 39','3 Main Street, Madison WI',25,10),(40,'Warehouse 40','3 Main Street, Madison WI',25,10),(41,'Warehouse 41','3 Main Street, Madison WI',25,10),(42,'Warehouse 42','3 Main Street, Madison WI',25,10),(43,'Warehouse 43','3 Main Street, Madison WI',25,10),(44,'Warehouse 44','3 Main Street, Madison WI',25,10),(45,'Warehouse 45','3 Main Street, Madison WI',25,10),(46,'Warehouse 46','3 Main Street, Madison WI',25,10),(47,'Warehouse 47','3 Main Street, Madison WI',25,10),(48,'Warehouse 48','3 Main Street, Madison WI',25,10),(49,'Warehouse 49','3 Main Street, Madison WI',25,10),(50,'Warehouse 50','3 Main Street, Madison WI',25,10),(51,'Warehouse 51','3 Main Street, Madison WI',25,10),(52,'Warehouse 52','3 Main Street, Madison WI',25,10),(53,'Warehouse 53','3 Main Street, Madison WI',25,10),(54,'Warehouse 54','3 Main Street, Madison WI',25,10),(55,'Warehouse 55','3 Main Street, Madison WI',25,10),(56,'Warehouse 56','3 Main Street, Madison WI',25,10),(57,'Warehouse 57','3 Main Street, Madison WI',25,10),(58,'Warehouse 58','3 Main Street, Madison WI',25,10),(59,'Warehouse 59','3 Main Street, Madison WI',25,10),(60,'Warehouse 60','3 Main Street, Madison WI',25,10),(61,'Warehouse 61','3 Main Street, Madison WI',25,10),(62,'Warehouse 62','3 Main Street, Madison WI',25,10),(63,'Warehouse 63','3 Main Street, Madison WI',25,10),(64,'Vincent\'s Warehouse','123 Vincent Road',10,10),(65,'test','test',1,1),(66,'haha','madison',2923,121);
/*!40000 ALTER TABLE `Warehouse` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `WarehouseItems`
--

DROP TABLE IF EXISTS `WarehouseItems`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `WarehouseItems` (
  `ware_items_id` int NOT NULL AUTO_INCREMENT,
  `item_id` int NOT NULL,
  `warehouse_id` int NOT NULL,
  `quantity` int NOT NULL,
  PRIMARY KEY (`ware_items_id`),
  KEY `item_id` (`item_id`),
  KEY `warehouse_id` (`warehouse_id`),
  CONSTRAINT `WarehouseItems_ibfk_1` FOREIGN KEY (`item_id`) REFERENCES `Item` (`item_id`),
  CONSTRAINT `WarehouseItems_ibfk_2` FOREIGN KEY (`warehouse_id`) REFERENCES `Warehouse` (`warehouse_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `WarehouseItems`
--

LOCK TABLES `WarehouseItems` WRITE;
/*!40000 ALTER TABLE `WarehouseItems` DISABLE KEYS */;
/*!40000 ALTER TABLE `WarehouseItems` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-04-09 20:42:43
