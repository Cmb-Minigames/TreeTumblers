-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jul 14, 2025 at 07:08 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `main`
--

-- --------------------------------------------------------

--
-- Table structure for table `players`
--

CREATE TABLE `players` (
  `uuid` varchar(255) NOT NULL COMMENT 'The players unique identifier',
  `name` varchar(255) NOT NULL COMMENT 'A backup name to use if the player hasn''t played before',
  `score` int(11) NOT NULL DEFAULT 0 COMMENT 'The personal score of the player',
  `team` varchar(255) DEFAULT NULL COMMENT 'The team of the player (null = spectator)'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='A table to store all player data for the event';

--
-- Dumping data for table `players`
--

-- Removed.

-- --------------------------------------------------------

--
-- Table structure for table `teams`
--

CREATE TABLE `teams` (
  `id` varchar(255) NOT NULL COMMENT 'The camelcase id of the team for internal management and organizer assignments.',
  `displayName` varchar(255) NOT NULL,
  `score` int(11) NOT NULL DEFAULT 0 COMMENT 'The total score for the entire team (added up individial)',
  `color` varchar(255) NOT NULL COMMENT 'The hex color in string form',
  `icon` varchar(255) NOT NULL COMMENT 'The unicode character for the icon formatted like \\uE000'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci COMMENT='The database with all of the team data';

--
-- Dumping data for table `teams`
--

-- Removed.

--
-- Indexes for dumped tables
--

--
-- Indexes for table `players`
--
ALTER TABLE `players`
  ADD PRIMARY KEY (`uuid`),
  ADD KEY `players_uuid_index` (`uuid`);

--
-- Indexes for table `teams`
--
ALTER TABLE `teams`
  ADD PRIMARY KEY (`id`),
  ADD KEY `teams_id_index` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
