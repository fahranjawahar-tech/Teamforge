# 🚀 TeamForge AI – Local Setup & Execution Guide

**Project Title:** Smart Team Formation Optimiser for College Students  
**Team:** Fahran Ali J (2104251040235) & Ajay V (2104251040051)  
**Institution:** Chennai Institute of Technology (Autonomous)  

---

## 📋 Prerequisites

Ensure you have the following installed on your machine:
1. **Java Development Kit (JDK) 17 or 21** (`java -version`)
2. **Python 3.10+** (`python --version`)
3. **Maven** (A standalone Maven 3.9.9 is included in this zip, or use your system Maven)

---

## ⚡ Quick 1-Click Startup (Windows)

Simply double-click the included batch script:
👉 **`start_all.bat`**

This will automatically launch:
1. The **Python ML Microservice** on `http://localhost:5000`
2. The **Java Spring Boot Backend** on `http://localhost:8080`
3. Opens your default web browser to `http://localhost:8080`

---

## 🛠️ Manual Step-by-Step Startup

### Step 1: Start Python AI/ML Microservice (Port 5000)

Open a terminal / command prompt in the project root folder:

```bash
# 1. Navigate to ML service directory
cd ml_service

# 2. Install Python dependencies
pip install -r requirements.txt

# 3. Start the Flask ML microservice
python app.py
```
> You will see: `Starting TeamForge AI ML microservice on port 5000...`  
> Health check: [http://localhost:5000/health](http://localhost:5000/health)

---

### Step 2: Start Java Spring Boot Backend (Port 8080)

Open a **second** terminal / command prompt in the project root folder:

```bash
# Option A: Using the included standalone Maven (Windows)
.\apache-maven-3.9.9\bin\mvn spring-boot:run

# Option B: Using your system installed Maven (Windows / Linux / macOS)
mvn spring-boot:run
```
> You will see:  
> `===============================================================`  
> ` TEAMFORGE AI - Intelligent Team Formation Platform Running!  `  
> ` URL: http://localhost:8080                                   `  
> ` Default Login: faculty / faculty123  or  admin / admin123    `  
> `===============================================================`  

---

## 🌐 Accessing the Application

1. Open your browser and go to: **[http://localhost:8080](http://localhost:8080)**
2. **Sign In Credentials:**
   * **Faculty:** `faculty` / `faculty123` (or click **"Quick Demo Login as Faculty"**)
   * **Admin:** `admin` / `admin123`
3. **Database Console:** [http://localhost:8080/h2-console](http://localhost:8080/h2-console)  
   *(JDBC URL: `jdbc:h2:file:./data/teamforgedb`, Username: `sa`, Password: blank)*

---

## 📁 Included Academic Deliverables

* **Project Report:** `SMART_TEAM_FORMATION_OPTIMISER_PBL_REPORT.docx` (Word Report)
* **Project Poster (PNG):** `SMART_TEAM_FORMATION_OPTIMISER_POSTER.png` (300 DPI A3 Landscape)
* **Project Poster (PDF):** `SMART_TEAM_FORMATION_OPTIMISER_POSTER.pdf` (Print-ready Vector PDF)
