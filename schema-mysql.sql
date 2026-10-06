-- =============================================================
-- TeamForge AI - MySQL Database Schema and Seed Data Script
-- Platform: MySQL 8.0+
-- Database: teamforge_ai
-- =============================================================

CREATE DATABASE IF NOT EXISTS `teamforge_ai` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE `teamforge_ai`;

-- Drop existing tables in reverse dependency order
SET FOREIGN_KEY_CHECKS = 0;
DROP TABLE IF EXISTS `team_members`;
DROP TABLE IF EXISTS `teams`;
DROP TABLE IF EXISTS `collaboration_history`;
DROP TABLE IF EXISTS `project_history`;
DROP TABLE IF EXISTS `hackathon_history`;
DROP TABLE IF EXISTS `students`;
DROP TABLE IF EXISTS `users`;
SET FOREIGN_KEY_CHECKS = 1;

-- 1. Users Table (Faculty / Admin Authentication)
CREATE TABLE `users` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `username` VARCHAR(60) NOT NULL UNIQUE,
    `password` VARCHAR(255) NOT NULL,
    `full_name` VARCHAR(100) NOT NULL,
    `role` VARCHAR(50) NOT NULL DEFAULT 'ROLE_FACULTY',
    `department` VARCHAR(120) DEFAULT 'Computer Science & Engineering',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- 2. Students Table
CREATE TABLE `students` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `name` VARCHAR(100) NOT NULL,
    `register_number` VARCHAR(30) NOT NULL UNIQUE,
    `email` VARCHAR(100) NOT NULL UNIQUE,
    `coding_skill` INT NOT NULL DEFAULT 3 CHECK (`coding_skill` BETWEEN 1 AND 5),
    `design_skill` INT NOT NULL DEFAULT 3 CHECK (`design_skill` BETWEEN 1 AND 5),
    `communication_skill` INT NOT NULL DEFAULT 3 CHECK (`communication_skill` BETWEEN 1 AND 5),
    `documentation_skill` INT NOT NULL DEFAULT 3 CHECK (`documentation_skill` BETWEEN 1 AND 5),
    `preferred_role` VARCHAR(60) NOT NULL DEFAULT 'Developer',
    `project_interests` VARCHAR(255) DEFAULT 'Web Development, AI/ML',
    `hackathons_participated` INT NOT NULL DEFAULT 0,
    `hackathons_won` INT NOT NULL DEFAULT 0,
    `previous_projects` INT NOT NULL DEFAULT 0,
    `projects_completed` INT NOT NULL DEFAULT 0,
    `team_experience` INT NOT NULL DEFAULT 3 CHECK (`team_experience` BETWEEN 1 AND 5),
    `status` VARCHAR(20) DEFAULT 'AVAILABLE',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- 3. Hackathon History Table
CREATE TABLE `hackathon_history` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `student_id` BIGINT NOT NULL,
    `hackathon_name` VARCHAR(120) NOT NULL,
    `year` INT NOT NULL,
    `result` VARCHAR(60) NOT NULL,
    `team_name` VARCHAR(80),
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT `fk_hackathon_student` FOREIGN KEY (`student_id`) REFERENCES `students` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 4. Project History Table
CREATE TABLE `project_history` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `student_id` BIGINT NOT NULL,
    `project_name` VARCHAR(120) NOT NULL,
    `technology` VARCHAR(150) NOT NULL,
    `result` VARCHAR(60) NOT NULL,
    `team_members` VARCHAR(255),
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT `fk_project_student` FOREIGN KEY (`student_id`) REFERENCES `students` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 5. Teams Table
CREATE TABLE `teams` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `team_name` VARCHAR(100) NOT NULL,
    `project_category` VARCHAR(80) NOT NULL,
    `success_score` DOUBLE NOT NULL,
    `prediction_label` VARCHAR(50) DEFAULT 'High Potential',
    `skill_score` DOUBLE,
    `role_diversity_score` DOUBLE,
    `experience_score` DOUBLE,
    `hackathon_performance` DOUBLE,
    `compatibility_score` DOUBLE,
    `strengths_text` VARCHAR(1000),
    `weaknesses_text` VARCHAR(1000),
    `academic_year` VARCHAR(20) DEFAULT '2025-2026',
    `status` VARCHAR(30) DEFAULT 'SAVED',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- 6. Team Members Association Table
CREATE TABLE `team_members` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `team_id` BIGINT NOT NULL,
    `student_id` BIGINT NOT NULL,
    `assigned_role` VARCHAR(60),
    CONSTRAINT `fk_member_team` FOREIGN KEY (`team_id`) REFERENCES `teams` (`id`) ON DELETE CASCADE,
    CONSTRAINT `fk_member_student` FOREIGN KEY (`student_id`) REFERENCES `students` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB;

-- 7. Collaboration History Table
CREATE TABLE `collaboration_history` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `student1_id` BIGINT NOT NULL,
    `student2_id` BIGINT NOT NULL,
    `student1_name` VARCHAR(100),
    `student2_name` VARCHAR(100),
    `projects_together` INT NOT NULL DEFAULT 1,
    `completed_projects` INT NOT NULL DEFAULT 1,
    `collaboration_score` DOUBLE NOT NULL DEFAULT 85.0,
    `last_project_name` VARCHAR(150),
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- =============================================================
-- SEED DATA
-- =============================================================

-- Seed Users (Passwords hashed with SHA-256 for 'faculty123' and 'admin123')
INSERT INTO `users` (`username`, `password`, `full_name`, `role`, `department`) VALUES
('faculty', '85350c33a921d7b328a6f95c4cf8923a1a9e7019685db5dbfa7b25299496a75a', 'Dr. Sarah Jenkins', 'ROLE_FACULTY', 'Computer Science & Engineering'),
('admin', '0192023a7bbd73250516f069df18b500', 'Admin Administrator', 'ROLE_ADMIN', 'CSE Department');

-- Seed Students
INSERT INTO `students` (`id`, `name`, `register_number`, `email`, `coding_skill`, `design_skill`, `communication_skill`, `documentation_skill`, `preferred_role`, `project_interests`, `hackathons_participated`, `hackathons_won`, `previous_projects`, `projects_completed`, `team_experience`, `status`) VALUES
(1, 'Fahran Ali', 'CSE2301', 'fahran.ali@college.edu', 5, 3, 4, 3, 'Developer', 'AI/ML, Java, Web Development', 4, 2, 5, 4, 4, 'ASSIGNED'),
(2, 'Ajay Kumar', 'CSE2302', 'ajay.kumar@college.edu', 4, 2, 5, 4, 'Team Lead', 'Cloud, Distributed Systems, Java', 3, 1, 4, 4, 5, 'ASSIGNED'),
(3, 'Priya Sharma', 'CSE2303', 'priya.sharma@college.edu', 3, 5, 4, 4, 'UI/UX Designer', 'UI/UX, Frontend, Figma, Mobile Apps', 2, 1, 3, 3, 4, 'ASSIGNED'),
(4, 'Arun Karthik', 'CSE2304', 'arun.karthik@college.edu', 4, 3, 3, 5, 'QA & Documentation', 'Backend, Testing, DevOps, Python', 3, 1, 4, 3, 3, 'ASSIGNED'),
(5, 'Ananya Roy', 'CSE2305', 'ananya.roy@college.edu', 5, 4, 4, 3, 'Developer', 'AI/ML, Deep Learning, Computer Vision', 5, 3, 6, 5, 5, 'AVAILABLE'),
(6, 'Rohan Verma', 'CSE2306', 'rohan.verma@college.edu', 4, 4, 5, 3, 'Team Lead', 'Full-Stack Web, Microservices, Spring Boot', 4, 2, 5, 4, 4, 'AVAILABLE'),
(7, 'Sneha Patel', 'CSE2307', 'sneha.patel@college.edu', 2, 5, 5, 4, 'UI/UX Designer', 'UI/UX Design, Web Animation, Tailwind CSS', 2, 0, 3, 3, 3, 'AVAILABLE'),
(8, 'Vikramaditya Rao', 'CSE2308', 'vikram.rao@college.edu', 5, 2, 3, 4, 'Developer', 'Data Engineering, Spark, Kafka, SQL', 3, 1, 4, 4, 4, 'AVAILABLE'),
(9, 'Meera Nair', 'CSE2309', 'meera.nair@college.edu', 3, 3, 4, 5, 'QA & Documentation', 'Technical Writing, API Testing, Cybersecurity', 1, 0, 2, 2, 3, 'AVAILABLE'),
(10, 'Karthik Sundaram', 'CSE2310', 'karthik.sundaram@college.edu', 4, 3, 4, 3, 'Developer', 'Mobile Apps, Flutter, Kotlin, Android', 3, 2, 4, 4, 4, 'AVAILABLE'),
(11, 'Divya Menon', 'CSE2311', 'divya.menon@college.edu', 3, 4, 4, 4, 'UI/UX Designer', 'Mobile UX, Human-Computer Interaction, React', 1, 0, 2, 2, 3, 'AVAILABLE'),
(12, 'Rahul Deshmukh', 'CSE2312', 'rahul.deshmukh@college.edu', 4, 2, 4, 3, 'Developer', 'IoT, Embedded C, Raspberry Pi, Robotics', 2, 1, 3, 3, 4, 'AVAILABLE'),
(13, 'Tanvi Joshi', 'CSE2313', 'tanvi.joshi@college.edu', 3, 4, 5, 4, 'Team Lead', 'Agile Management, Product Design, Web Apps', 2, 1, 3, 3, 4, 'AVAILABLE'),
(14, 'Siddharth Bose', 'CSE2314', 'siddharth.bose@college.edu', 3, 2, 3, 3, 'Developer', 'Web Basics, Java, MySQL', 0, 0, 1, 1, 2, 'AVAILABLE'),
(15, 'Kavya Iyer', 'CSE2315', 'kavya.iyer@college.edu', 4, 3, 4, 4, 'Developer', 'Cloud, AWS, Docker, Kubernetes', 0, 0, 2, 2, 3, 'AVAILABLE');

-- Seed Hackathons
INSERT INTO `hackathon_history` (`student_id`, `hackathon_name`, `year`, `result`, `team_name`) VALUES
(1, 'Smart India Hackathon 2025', 2025, '1st Place (Winner)', 'Team Neural'),
(1, 'HackVortex National 2024', 2024, 'Finalist', 'CodeCrafters'),
(1, 'State Level Coding League', 2024, 'Winner', 'AlphaSquad'),
(2, 'AWS Cloud Innovate 2024', 2024, '2nd Runner Up', 'CloudChamps'),
(3, 'Designathon 2025', 2025, '1st Place (Winner)', 'PixelCraft'),
(4, 'CyberShield Hackathon', 2024, 'Special Mention', 'SecureNet'),
(5, 'Google Solution Challenge 2025', 2025, 'Global Top 50', 'VisionAI'),
(5, 'AI Odyssey Hackathon', 2024, 'Winner', 'TensorTeam'),
(6, 'HackNova 2025', 2025, 'Winner', 'DevLords'),
(10, 'AppVenture Android Fest', 2024, 'Winner', 'FlutterFlow');

-- Seed Projects
INSERT INTO `project_history` (`student_id`, `project_name`, `technology`, `result`, `team_members`) VALUES
(1, 'Autonomous Drone Path Planner', 'Python, OpenCV, ROS', 'Completed (Grade S)', 'Fahran, Ajay, Arun'),
(1, 'Hospital Patient Analytics', 'Java, Spring Boot, MySQL', 'Deployed to Production', 'Fahran, Priya'),
(2, 'Microservices E-Commerce Portal', 'Spring Boot, Docker, Redis', 'Completed (Grade A)', 'Ajay, Fahran, Arun'),
(3, 'Fintech Mobile Design System', 'Figma, React Native', 'Completed (Grade S)', 'Priya, Divya'),
(5, 'Multimodal Medical Diagnosis AI', 'PyTorch, FastAPI, React', 'Completed (Grade S)', 'Ananya, Rohan, Sneha'),
(6, 'Campus Smart Parking System', 'Spring Boot, Vue.js, PostgreSQL', 'Completed (Grade A)', 'Rohan, Vikram'),
(10, 'Blood Donation Bridge App', 'Flutter, Firebase, Kotlin', 'Completed (Grade S)', 'Karthik, Divya, Tanvi');

-- Seed Collaborations
INSERT INTO `collaboration_history` (`student1_id`, `student2_id`, `student1_name`, `student2_name`, `projects_together`, `completed_projects`, `collaboration_score`, `last_project_name`) VALUES
(1, 2, 'Fahran Ali', 'Ajay Kumar', 2, 2, 92.0, 'Microservices E-Commerce Portal'),
(1, 3, 'Fahran Ali', 'Priya Sharma', 1, 1, 88.0, 'Hospital Patient Analytics'),
(1, 4, 'Fahran Ali', 'Arun Karthik', 2, 2, 86.5, 'Autonomous Drone Path Planner'),
(2, 4, 'Ajay Kumar', 'Arun Karthik', 2, 2, 89.0, 'Microservices E-Commerce Portal'),
(5, 6, 'Ananya Roy', 'Rohan Verma', 3, 3, 95.0, 'Multimodal Medical Diagnosis AI'),
(5, 7, 'Ananya Roy', 'Sneha Patel', 2, 2, 90.0, 'Multimodal Medical Diagnosis AI'),
(10, 11, 'Karthik Sundaram', 'Divya Menon', 2, 2, 91.0, 'Blood Donation Bridge App');

-- Seed Historical Teams
INSERT INTO `teams` (`id`, `team_name`, `project_category`, `success_score`, `prediction_label`, `skill_score`, `role_diversity_score`, `experience_score`, `hackathon_performance`, `compatibility_score`, `strengths_text`, `weaknesses_text`, `academic_year`, `status`) VALUES
(1, 'Team Alpha', 'AI & Machine Learning', 89.4, 'High Potential', 88.0, 92.0, 82.0, 75.0, 88.5, 'Strong coding capability across team members | Good role diversity | Proven past collaboration history', 'Documentation skill is comparatively low', '2025-2026', 'SAVED'),
(2, 'Team Phoenix', 'Full-Stack Web Development', 88.1, 'High Potential', 86.5, 90.0, 85.0, 80.0, 87.0, 'Excellent technical proficiency | High role diversity covering Developer and Designer roles', 'Ensure sprint timelines are clearly tracked', '2025-2026', 'SAVED'),
(3, 'Team Nova', 'Mobile Computing', 83.5, 'High Potential', 80.0, 85.0, 78.0, 70.0, 84.0, 'High alignment in mobile project domain interests | Solid communication coverage', 'Moderate hackathon competitive exposure', '2025-2026', 'SAVED');

INSERT INTO `team_members` (`team_id`, `student_id`, `assigned_role`) VALUES
(1, 1, 'Developer'),
(1, 2, 'Team Lead'),
(1, 3, 'UI/UX Designer'),
(1, 4, 'QA & Documentation'),
(2, 5, 'Developer'),
(2, 6, 'Team Lead'),
(2, 7, 'UI/UX Designer'),
(2, 8, 'Developer'),
(3, 10, 'Developer'),
(3, 11, 'UI/UX Designer'),
(3, 13, 'Team Lead'),
(3, 9, 'QA & Documentation');
