# TEAMFORGE AI
### Intelligent Team Formation & Performance Prediction Platform

> **Academic Context:** College Java Project for Computer Science & Engineering (CSE) 2nd Year.  
> **Core Backend Technology:** Java & Spring Boot (Primary Engine)  
> **Supporting Intelligent Component:** Python with Scikit-learn, Pandas & NumPy (ML Prediction Microservice)  
> **Database:** MySQL / Spring Data JPA & Hibernate  
> **Frontend:** Responsive HTML5, CSS3 Glassmorphism, Modern Vanilla JavaScript, Chart.js  

---

## 1. Project Overview

**TeamForge AI** is a Java-based intelligent team formation platform that analyzes student skills, preferred roles, project experience, hackathon performance and collaboration history to recommend balanced teams and predict their potential success using machine learning.

In academic environments and hackathons, student teams are often formed randomly or purely based on casual friendships. This results in severe skill imbalances—teams with all coders and no UI/UX designers, teams lacking leadership/communication, or teams where high-performing students are clustered together while others struggle.

**TeamForge AI solves this by evaluating:**
1. **Technical Coding Skills** (1–5 scale)
2. **UI/UX & Design Skills** (1–5 scale)
3. **Communication & Pitching Skills** (1–5 scale)
4. **Documentation & QA Skills** (1–5 scale)
5. **Preferred Role** (Developer, UI/UX Designer, Team Lead, QA & Documentation)
6. **Project Domain Interests** (AI/ML, Web, Mobile, Cloud, IoT, Blockchain)
7. **Previous Project History & Completion Rate**
8. **Hackathon Track Record & Historical Win Rate** ($\frac{\text{Hackathons Won}}{\text{Hackathons Participated}} \times 100\%$)
9. **Team Experience** (1–5 scale)
10. **Pairwise Historical Collaboration Synergy**

---

## 2. End-to-End Architecture

```
                                  +-------------------------------------------------------+
                                  |                    Browser Frontend                   |
                                  |    (HTML5 + CSS3 Glassmorphism + JavaScript + Chart.js)|
                                  +-------------------------------------------------------+
                                                              |  REST API Calls (JSON)
                                                              v
+-----------------------------------------------------------------------------------------------------------------------+
|                                              JAVA SPRING BOOT BACKEND (Port 8080)                                      |
|                                                                                                                       |
|  [AuthController]      [StudentController]      [TeamController]      [AnalyticsController]     [HistoryController]   |
|          |                      |                      |                       |                         |            |
|    (AuthService)        (StudentService)     (TeamFormationService)    (AnalyticsService)         (HistoryService)    |
|                                                        |                                                              |
|                                            (MlPredictionClientService)                                                |
|                                                        |                                                              |
|                                         Spring Data JPA / Repositories                                                |
+-----------------------------------------------------------------------------------------------------------------------+
                     |                                                                        |
                     |  JDBC / JPA                                                            | HTTP POST /predict-team
                     v                                                                        v
+----------------------------------------------------+               +--------------------------------------------------+
|                   MySQL Database                   |               |           PYTHON ML MICROSERVICE (Port 5000)     |
|   (Students, Teams, TeamMembers, Histories, Collab)|               |   (Flask + Scikit-Learn RandomForestRegressor)   |
+----------------------------------------------------+               +--------------------------------------------------+
```

---

## 3. Key Modules & Features

### 🔐 1. Faculty / Admin Authentication
- Secure login portal with role-based access (`ROLE_FACULTY`, `ROLE_ADMIN`).
- Comprehensive form validation for empty credentials and invalid logins.
- Default credentials: `faculty / faculty123` or `admin / admin123`.

### 📊 2. Faculty Analytics Dashboard
- **Metric KPI Cards:** Total Students, Total Teams, Hackathon Records, Completed Projects, Average Team Success Score, Top Performing Team.
- **Cohort Skill Distribution Radar Chart:** Multi-axis visualization of class-wide strengths in Coding, Design, Communication, and Documentation.
- **Team Performance Benchmarking:** Bar chart comparing formed teams against predicted performance.
- **Hackathon Statistics Donut:** Ratio of podium finishes to total participations.
- **Quick Leaderboard:** Live ranking of top teams with member rosters.

### 🎓 3. Student Management & Comprehensive Profiles
- Full CRUD operations (Add, View, Update, Delete) with live search and role/status filtering.
- **Validation Engine:** Skill bounds (1–5), non-negative constraints, logical business rules ($\text{Won} \le \text{Participated}$, $\text{Completed} \le \text{Previous}$).
- **Automatic Hackathon Win Rate Calculation:** Safely handles zero history without division-by-zero errors (displays *"No Hackathon History"* badge).
- **Detailed Student Profile:** Skill rating progress bars, track record summary, chronological Hackathon timeline, Project history, and pairwise Collaboration synergy list.

### 🧠 4. AI Team Generator (Core Engine)
- **Multi-Constraint Optimization in Java:**
  - Snake-draft talent partitioning to prevent skill clustering.
  - Greedy role-swapping algorithm ensuring each team has a Developer, Designer, Lead, and Doc specialist.
  - Domain interest cosine matching with the target project category.
  - Pairwise collaboration synergy evaluation.
- **5 Sub-Dimension Score Breakdowns:**
  1. *Team Skill Score (0–100%)*
  2. *Role Diversity Score (0–100%)*
  3. *Experience Score (0–100%)*
  4. *Hackathon Performance Score (0–100%)*
  5. *Compatibility Score (0–100%)*
- **ML Predicted Success Score (0–100%)** with potential badge (*High Potential*, *Moderate Potential*, *Needs Improvement*).
- **Explainable AI (XAI):** Clear natural language bullet points detailing *Key Strengths* and *Areas to Monitor / Potential Weaknesses*.

### ⚖️ 5. Team Comparison & Radar Benchmarking
- Multi-team selection to evaluate 2 or more teams side-by-side.
- Side-by-side metric comparison matrix table.
- Interactive Radar overlay chart comparing all 6 performance dimensions.
- AI Comparative Synthesis providing natural language guidance to faculty on team trade-offs.

### 🏆 6. Team Rankings & Leaderboard
- Podium visualization for Gold 🥇, Silver 🥈, and Bronze 🥉 teams.
- Global leaderboard with filters by Project Category, Academic Year, and success score.

### 🤝 7. Collaboration History Tracker
- Pairwise tracking of student pairs who previously worked together.
- Records joint projects, completed projects, and rolling collaboration synergy scores.

---

## 4. Technology Stack & Project Structure

```
java project/
├── pom.xml                                   # Maven dependencies & build config
├── schema-mysql.sql                          # Standalone MySQL DDL & Seed Data SQL
├── start-all.bat                             # One-click launcher for both Java & Python
├── start-backend.bat                         # Spring Boot backend launcher
├── start-ml.bat                              # Python ML microservice launcher
│
├── src/main/java/com/teamforge/ai/
│   ├── TeamForgeAiApplication.java           # Spring Boot Entry Point
│   ├── config/
│   │   ├── DataInitializer.java              # 15+ Realistic Students & Seed Teams
│   │   └── WebConfig.java                    # CORS & Resource Mapping
│   ├── controller/
│   │   ├── AuthController.java               # Authentication endpoints
│   │   ├── StudentController.java            # Student CRUD & search
│   │   ├── TeamController.java               # AI generation & save teams
│   │   ├── HistoryController.java            # Hackathons, projects, collaborations
│   │   ├── AnalyticsController.java          # Dashboard KPIs & comparison
│   │   └── PredictionController.java         # ML test endpoint
│   ├── dto/                                  # Request/Response data transfer objects
│   ├── exception/                            # Global exception handling
│   ├── model/                                # JPA Entities (Student, Team, etc.)
│   ├── repository/                           # Spring Data JPA interfaces
│   └── service/                              # Business logic & AI algorithms
│       ├── AuthService.java
│       ├── StudentService.java
│       ├── TeamFormationService.java         # AI Team Formation Engine
│       ├── HistoryService.java
│       ├── AnalyticsService.java
│       └── MlPredictionClientService.java    # REST client + Fallback ML engine
│
├── src/main/resources/
│   ├── application.properties                # Spring Boot configuration (H2 / MySQL)
│   ├── application-mysql.properties          # Dedicated MySQL profile
│   └── static/                               # Frontend Single Page Web App
│       ├── index.html                        # Main UI Shell & Modals
│       ├── css/style.css                     # Glassmorphic responsive styling
│       └── js/
│           ├── api.js                        # Centralized REST API client
│           ├── app.js                        # Routing & orchestrator
│           ├── auth.js                       # Session & login handler
│           ├── dashboard.js                  # KPI metrics & Chart.js charts
│           ├── students.js                   # Student management & profiles
│           ├── generator.js                  # AI Team Generator UI
│           ├── comparison.js                 # Multi-team comparison & radar
│           ├── teams.js                      # Formed teams & rosters
│           ├── rankings.js                   # Leaderboard & podium
│           └── history.js                    # Collaboration matrix
│
└── ml_service/                               # Python ML Microservice
    ├── app.py                                # Flask REST API (Port 5000)
    ├── train_model.py                        # Scikit-Learn training script
    ├── team_success_model.joblib             # Trained Random Forest Regressor
    ├── model_metadata.json                   # Model metrics ($R^2$, RMSE)
    └── requirements.txt                      # Python dependencies
```

---

## 5. How to Run Locally

### Prerequisites
- **Java 17 or 21+** (e.g., OpenJDK / Oracle JDK 17, 21, or 25)
- **Python 3.10+** (with pip)
- *(Optional)* **MySQL 8.0+** (The project runs out-of-the-box using embedded persistent H2 database by default, and seamlessly connects to MySQL when configured).

---

### Step 1: Quick 1-Click Startup (Recommended)
Double-click `start-all.bat` or run in terminal:
```cmd
start-all.bat
```
This automatically starts:
1. Python ML Microservice on `http://localhost:5000`
2. Java Spring Boot Backend on `http://localhost:8080`

Open your browser and navigate to:
👉 **`http://localhost:8080`**

**Default Login:**
- Username: `faculty`
- Password: `faculty123`
*(Or click "Quick Demo Login as Faculty" on the login screen)*

---

### Step 2: Manual / Command-Line Startup

#### 1. Start Python ML Microservice:
```bash
python -m pip install -r ml_service/requirements.txt
python ml_service/train_model.py
python ml_service/app.py
```
*The ML service will start on `http://localhost:5000`.*

#### 2. Start Java Spring Boot Backend:
```bash
.\apache-maven-3.9.9\bin\mvn.cmd spring-boot:run
```
*The Java Spring Boot backend will start on `http://localhost:8080`.*

---

### Step 3: Using MySQL Database (Optional)
If you want to use a local MySQL server:
1. Import `schema-mysql.sql` into MySQL:
   ```bash
   mysql -u root -p < schema-mysql.sql
   ```
2. Start Spring Boot with MySQL profile:
   ```bash
   .\apache-maven-3.9.9\bin\mvn.cmd spring-boot:run -Dspring-boot.run.profiles=mysql
   ```

---

## 6. Complete Demonstration Walkthrough

```text
1. Login Screen
   └─ Enter `faculty / faculty123` -> Redirect to Faculty Dashboard

2. Faculty Dashboard
   └─ Review KPI cards (Total Students: 15, Formed Teams: 3, Avg Success: 87.0%)
   └─ Inspect Cohort Skill Distribution Radar Chart & Team Performance Bars

3. Student Management
   └─ Click "All Students" -> Filter by role (Developer, Designer, Lead)
   └─ Click "Add Student" -> Enter values, test 1-5 validation & live Win Rate preview
   └─ Click "Profile" on "Fahran Ali" -> View animated skill bars, hackathon history, and project history

4. AI Team Generator
   └─ Navigate to "AI Team Generator"
   └─ Choose Project Domain (e.g. "Full-Stack Web Development")
   └─ Choose Team Size (e.g. 4 Members per Team) and Skill Priority ("Balanced")
   └─ Click "Generate Teams with AI"
   └─ Watch the AI neural optimizer balance skills, roles, and collaboration synergy
   └─ Inspect 5 breakdown metrics, ML Predicted Success Score (e.g. 88.5%), and Explainable AI bullets
   └─ Click "Save Team" to persist to database

5. Team Comparison
   └─ Navigate to "Team Comparison" -> Select "Team Alpha" and "Team Phoenix"
   └─ View side-by-side benchmark table, Radar comparison chart, and AI synthesis text

6. Team Rankings & Leaderboard
   └─ Navigate to "Team Rankings" -> View Champion Podium 🏆 and global rankings
```

---

## 7. REST API Endpoints Catalog

| Module | HTTP Method | Endpoint | Description |
| :--- | :--- | :--- | :--- |
| **Auth** | `POST` | `/api/auth/login` | Authenticate faculty/admin user |
| **Auth** | `POST` | `/api/auth/logout` | Invalidate user session |
| **Students** | `GET` | `/api/students` | Get all students (with search & filters) |
| **Students** | `GET` | `/api/students/{id}` | Get student profile with histories |
| **Students** | `POST` | `/api/students` | Create new student (with validation) |
| **Students** | `PUT` | `/api/students/{id}` | Update student profile |
| **Students** | `DELETE` | `/api/students/{id}` | Delete student |
| **Teams** | `POST` | `/api/teams/generate` | Run AI Team Formation Algorithm |
| **Teams** | `POST` | `/api/teams/save` | Persist generated team to database |
| **Teams** | `GET` | `/api/teams` | Get all formed teams and rosters |
| **Analytics**| `GET` | `/api/analytics/dashboard` | Aggregated dashboard KPI stats & charts |
| **Analytics**| `POST` | `/api/analytics/compare` | Multi-team comparison matrix & radar data |
| **Analytics**| `GET` | `/api/analytics/rankings` | Leaderboard ranked by success score |
| **History** | `GET` | `/api/history/collaborations`| Pairwise student collaboration scores |
| **ML Engine**| `POST` | `http://localhost:5000/predict-team` | Python Scikit-Learn prediction endpoint |

---

## 8. College Project Evaluation Summary

| Requirement | Implementation Detail | Status |
| :--- | :--- | :--- |
| **Main Technology** | Java 17/25 + Spring Boot (Spring Web, Spring Data JPA, REST APIs, Maven) | ✅ Complete |
| **Supporting AI/ML** | Python Flask + Scikit-Learn Random Forest Regressor ($R^2 = 0.873$) | ✅ Complete |
| **Database** | MySQL 8.0+ schema + persistent JPA entities + Zero-config H2 mode | ✅ Complete |
| **Frontend** | Modern, responsive, glassmorphic UI (HTML5, CSS3, JS, Chart.js) | ✅ Complete |
| **Student Validation** | 1–5 Skill bounds, non-negative numbers, win-rate division-by-zero protection | ✅ Complete |
| **Team Formation Logic** | Multi-factor balanced heuristic (skills, role diversity, interests, synergy) | ✅ Complete |
| **Explainable AI (XAI)** | Natural language positive drivers and areas for improvement | ✅ Complete |
| **Sample Data** | 15 realistic CSE students, hackathon histories, project histories, sample teams | ✅ Complete |
| **Zero Mockups** | 100% connected end-to-end with working CRUD, AI generation, and persistence | ✅ Complete |

---
*Developed for College CSE 2nd Year Project Examination.*
