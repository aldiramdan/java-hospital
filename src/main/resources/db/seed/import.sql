-- MySQL dump 10.13  Distrib 8.0.34, for Linux (x86_64)
--
-- Host: localhost    Database: db_hospital
-- ------------------------------------------------------
-- Server version	8.0.34

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
-- Dumping data for table `patient`
--

LOCK TABLES `patient` WRITE;
/*!40000 ALTER TABLE `patient` DISABLE KEYS */;
INSERT INTO `patient` (id, address, age, created_at, is_deleted, name, updated_at) VALUES ('6b3bf6cb-e060-4062-a9ed-f39eb22761f7','987 Pine Rd',26,'2023-07-28 03:21:31.000000',_binary '\0','Emily','2023-07-28 03:21:31.000000'),('cca7927e-1bb5-403a-ac6f-6a1f4a767f14','123 Main St',21,'2023-07-28 03:21:33.000000',_binary '\0','James','2023-07-28 03:21:33.000000'),('d493a289-bb6e-4cc7-acf3-5a1f0390455b','789 Oak Ave',37,'2023-07-28 03:21:34.000000',_binary '\0','Emma','2023-07-28 03:21:34.000000'),('da602506-69c0-4101-9191-7644eda0a296','321 Maple Ave',36,'2023-07-28 03:21:32.000000',_binary '\0','John','2023-07-28 03:21:32.000000');
/*!40000 ALTER TABLE `patient` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `doctor`
--

LOCK TABLES `doctor` WRITE;
/*!40000 ALTER TABLE `doctor` DISABLE KEYS */;
INSERT INTO `doctor` (id, consultation_fee, created_at, is_deleted, name, specialization, updated_at) VALUES ('1e96cdba-5eb8-4df3-b2b4-54ffb3b165ac',71922,'2023-07-28 03:20:02.000000',_binary '\0','Dr. Michael Johnson','Dermatologist','2023-07-28 03:20:02.000000'),('a6c398d4-9f8c-4c50-909a-b91050f11976',74179,'2023-07-28 03:20:07.000000',_binary '\0','Dr. Emily Brown','Orthopedic Surgeon','2023-07-28 03:20:07.000000'),('c49491e3-06e4-439b-8191-e3d80e31ec00',75549,'2023-07-28 03:20:05.000000',_binary '\0','Dr. John Doe','Pediatrician','2023-07-28 03:20:05.000000'),('f6632238-6617-4a7f-9154-c0904f760ecd',83672,'2023-07-28 03:20:04.000000',_binary '\0','Dr. Jane Smith','General Physician','2023-07-28 03:20:04.000000');
/*!40000 ALTER TABLE `doctor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `medicine`
--

LOCK TABLES `medicine` WRITE;
/*!40000 ALTER TABLE `medicine` DISABLE KEYS */;
INSERT INTO `medicine` (id, created_at, is_deleted, name, price, updated_at) VALUES ('150799a6-464b-4079-878f-ded155eb12d4','2023-07-28 03:22:50.000000',_binary '\0','Metoprolol',3598,'2023-07-28 03:22:50.000000'),('6a8b5759-2066-4c39-9044-c34bc9140c4f','2023-07-28 03:22:49.000000',_binary '\0','Ibuprofen',3892,'2023-07-28 03:22:49.000000'),('6b1d55a9-5216-48c1-972a-d0a1ccac7cfa','2023-07-28 03:22:51.000000',_binary '\0','Amoxicillin',3339,'2023-07-28 03:22:51.000000'),('8ebc4436-f564-45a9-b27b-00a2114742be','2023-07-28 03:22:47.000000',_binary '\0','Omeprazole',4180,'2023-07-28 03:22:47.000000'),('9adf0c70-6773-4fab-8e41-523abe1eb5cb','2023-07-28 03:22:46.000000',_binary '\0','Lisinopril',4209,'2023-07-28 03:22:46.000000'),('c974a096-5f9e-463f-973c-140ce67bfdc3','2023-07-28 03:22:52.000000',_binary '\0','Paracetamol',3199,'2023-07-28 03:22:52.000000'),('d58b9fd1-a819-42b7-a67b-b9538bf616dd','2023-07-28 03:22:45.000000',_binary '\0','Aspirin',4243,'2023-07-28 03:22:45.000000');
/*!40000 ALTER TABLE `medicine` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `disease`
--

LOCK TABLES `disease` WRITE;
/*!40000 ALTER TABLE `disease` DISABLE KEYS */;
INSERT INTO `disease` (id, created_at, is_deleted, name, updated_at, medicine_id) VALUES ('0c2417cc-4aa1-4229-88d2-898da41a8630','2023-07-28 03:29:35.000000',_binary '\0','Sore Throat','2023-07-28 03:29:35.000000','150799a6-464b-4079-878f-ded155eb12d4'),('183bab79-c225-46e6-972f-62ca00603f65','2023-07-28 03:30:09.000000',_binary '\0','Allergies','2023-07-28 03:30:09.000000','6b1d55a9-5216-48c1-972a-d0a1ccac7cfa'),('50b49fe8-4560-4982-9616-c5277fc91442','2023-07-28 03:30:33.000000',_binary '\0','Cold','2023-07-28 03:30:33.000000','d58b9fd1-a819-42b7-a67b-b9538bf616dd'),('c4303521-032a-4f60-a047-f2774deea8eb','2023-07-28 03:30:19.000000',_binary '\0','Arthritis','2023-07-28 03:30:19.000000','8ebc4436-f564-45a9-b27b-00a2114742be'),('e55232d2-3542-4183-8689-c566110a17c4','2023-07-28 03:29:59.000000',_binary '\0','Fever','2023-07-28 03:29:59.000000','c974a096-5f9e-463f-973c-140ce67bfdc3');
/*!40000 ALTER TABLE `disease` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `treatment`
--

LOCK TABLES `treatment` WRITE;
/*!40000 ALTER TABLE `treatment` DISABLE KEYS */;
/*!40000 ALTER TABLE `treatment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping data for table `treatment_disease`
--

LOCK TABLES `treatment_disease` WRITE;
/*!40000 ALTER TABLE `treatment_disease` DISABLE KEYS */;
/*!40000 ALTER TABLE `treatment_disease` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-08-26  3:46:10
