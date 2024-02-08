-- Volcando estructura de base de datos para bdvisor
CREATE DATABASE IF NOT EXISTS `bdvisor` /*!40100 DEFAULT CHARACTER SET utf8mb3 COLLATE utf8mb3_general_ci */;
USE `bdvisor`;

-- Volcando estructura para tabla bdvisor.cuenta
CREATE TABLE IF NOT EXISTS `cuenta` (
  `numCuenta` int(11) NOT NULL,
  `titular` varchar(50) CHARACTER SET latin1 COLLATE latin1_spanish_ci DEFAULT NULL,
  `nacionalidad` varchar(50) CHARACTER SET latin1 COLLATE latin1_spanish_ci DEFAULT NULL,
  `fechaApertura` date DEFAULT NULL,
  `saldo` double DEFAULT NULL,
  PRIMARY KEY (`numCuenta`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;

-- Volcando datos para la tabla bdvisor.cuenta: ~2 rows (aproximadamente)
DELETE FROM `cuenta`;
/*!40000 ALTER TABLE `cuenta` DISABLE KEYS */;
INSERT INTO `cuenta` (`numCuenta`, `titular`, `nacionalidad`, `fechaApertura`, `saldo`) VALUES
	(1, 'Giuliana', 'Español', '2023-01-23', 564),
	(2, 'Juan', 'Español', '2023-01-23', 5479);
