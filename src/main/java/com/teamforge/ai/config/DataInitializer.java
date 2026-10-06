package com.teamforge.ai.config;

import com.teamforge.ai.model.*;
import com.teamforge.ai.repository.*;
import com.teamforge.ai.service.AuthService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final StudentRepository studentRepository;
    private final HackathonHistoryRepository hackathonHistoryRepository;
    private final ProjectHistoryRepository projectHistoryRepository;
    private final CollaborationHistoryRepository collaborationHistoryRepository;
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final EventRepository eventRepository;

    public DataInitializer(UserRepository userRepository,
                           StudentRepository studentRepository,
                           HackathonHistoryRepository hackathonHistoryRepository,
                           ProjectHistoryRepository projectHistoryRepository,
                           CollaborationHistoryRepository collaborationHistoryRepository,
                           TeamRepository teamRepository,
                           TeamMemberRepository teamMemberRepository,
                           EventRepository eventRepository) {
        this.userRepository = userRepository;
        this.studentRepository = studentRepository;
        this.hackathonHistoryRepository = hackathonHistoryRepository;
        this.projectHistoryRepository = projectHistoryRepository;
        this.collaborationHistoryRepository = collaborationHistoryRepository;
        this.teamRepository = teamRepository;
        this.teamMemberRepository = teamMemberRepository;
        this.eventRepository = eventRepository;
    }

    @Override
    public void run(String... args) {
        if (userRepository.count() == 0) {
            initUsers();
        }
        if (studentRepository.count() == 0) {
            initStudentsAndHistories();
            initCollaborations();
            initHistoricalTeams();
        }
        if (eventRepository.count() == 0) {
            initEvents();
        }
    }

    private void initUsers() {
        User faculty = new User(
                "faculty",
                AuthService.hashPassword("faculty123"),
                "Dr. Sarah Jenkins",
                "ROLE_FACULTY",
                "Computer Science & Engineering"
        );
        User admin = new User(
                "admin",
                AuthService.hashPassword("admin123"),
                "Admin Administrator",
                "ROLE_ADMIN",
                "CSE Department"
        );
        userRepository.saveAll(Arrays.asList(faculty, admin));
        System.out.println("[TeamForge DataInit] Seeded default faculty & admin accounts.");
    }

    private void initStudentsAndHistories() {
        List<Student> students = Arrays.asList(
                new Student("Fahran Ali", "CSE2301", "fahran.ali@college.edu", 5, 3, 4, 3, "Developer", "AI/ML, Java, Web Development", 4, 2, 5, 4, 4),
                new Student("Ajay Kumar", "CSE2302", "ajay.kumar@college.edu", 4, 2, 5, 4, "Team Lead", "Cloud, Distributed Systems, Java", 3, 1, 4, 4, 5),
                new Student("Priya Sharma", "CSE2303", "priya.sharma@college.edu", 3, 5, 4, 4, "UI/UX Designer", "UI/UX, Frontend, Figma, Mobile Apps", 2, 1, 3, 3, 4),
                new Student("Arun Karthik", "CSE2304", "arun.karthik@college.edu", 4, 3, 3, 5, "QA & Documentation", "Backend, Testing, DevOps, Python", 3, 1, 4, 3, 3),
                new Student("Ananya Roy", "CSE2305", "ananya.roy@college.edu", 5, 4, 4, 3, "Developer", "AI/ML, Deep Learning, Computer Vision", 5, 3, 6, 5, 5),
                new Student("Rohan Verma", "CSE2306", "rohan.verma@college.edu", 4, 4, 5, 3, "Team Lead", "Full-Stack Web, Microservices, Spring Boot", 4, 2, 5, 4, 4),
                new Student("Sneha Patel", "CSE2307", "sneha.patel@college.edu", 2, 5, 5, 4, "UI/UX Designer", "UI/UX Design, Web Animation, Tailwind CSS", 2, 0, 3, 3, 3),
                new Student("Vikramaditya Rao", "CSE2308", "vikram.rao@college.edu", 5, 2, 3, 4, "Developer", "Data Engineering, Spark, Kafka, SQL", 3, 1, 4, 4, 4),
                new Student("Meera Nair", "CSE2309", "meera.nair@college.edu", 3, 3, 4, 5, "QA & Documentation", "Technical Writing, API Testing, Cybersecurity", 1, 0, 2, 2, 3),
                new Student("Karthik Sundaram", "CSE2310", "karthik.sundaram@college.edu", 4, 3, 4, 3, "Developer", "Mobile Apps, Flutter, Kotlin, Android", 3, 2, 4, 4, 4),
                new Student("Divya Menon", "CSE2311", "divya.menon@college.edu", 3, 4, 4, 4, "UI/UX Designer", "Mobile UX, Human-Computer Interaction, React", 1, 0, 2, 2, 3),
                new Student("Rahul Deshmukh", "CSE2312", "rahul.deshmukh@college.edu", 4, 2, 4, 3, "Developer", "IoT, Embedded C, Raspberry Pi, Robotics", 2, 1, 3, 3, 4),
                new Student("Tanvi Joshi", "CSE2313", "tanvi.joshi@college.edu", 3, 4, 5, 4, "Team Lead", "Agile Management, Product Design, Web Apps", 2, 1, 3, 3, 4),
                new Student("Siddharth Bose", "CSE2314", "siddharth.bose@college.edu", 3, 2, 3, 3, "Developer", "Web Basics, Java, MySQL", 0, 0, 1, 1, 2),
                new Student("Kavya Iyer", "CSE2315", "kavya.iyer@college.edu", 4, 3, 4, 4, "Developer", "Cloud, AWS, Docker, Kubernetes", 0, 0, 2, 2, 3)
        );

        List<Student> savedStudents = studentRepository.saveAll(students);

        hackathonHistoryRepository.saveAll(Arrays.asList(
                new HackathonHistory(savedStudents.get(0).getId(), "Smart India Hackathon 2025", 2025, "1st Place (Winner)", "Team Neural"),
                new HackathonHistory(savedStudents.get(0).getId(), "HackVortex National 2024", 2024, "Finalist", "CodeCrafters"),
                new HackathonHistory(savedStudents.get(0).getId(), "State Level Coding League", 2024, "Winner", "AlphaSquad"),
                new HackathonHistory(savedStudents.get(1).getId(), "AWS Cloud Innovate 2024", 2024, "2nd Runner Up", "CloudChamps"),
                new HackathonHistory(savedStudents.get(2).getId(), "Designathon 2025", 2025, "1st Place (Winner)", "PixelCraft"),
                new HackathonHistory(savedStudents.get(3).getId(), "CyberShield Hackathon", 2024, "Special Mention", "SecureNet"),
                new HackathonHistory(savedStudents.get(4).getId(), "Google Solution Challenge 2025", 2025, "Global Top 50", "VisionAI"),
                new HackathonHistory(savedStudents.get(4).getId(), "AI Odyssey Hackathon", 2024, "Winner", "TensorTeam"),
                new HackathonHistory(savedStudents.get(5).getId(), "HackNova 2025", 2025, "Winner", "DevLords"),
                new HackathonHistory(savedStudents.get(9).getId(), "AppVenture Android Fest", 2024, "Winner", "FlutterFlow")
        ));

        projectHistoryRepository.saveAll(Arrays.asList(
                new ProjectHistory(savedStudents.get(0).getId(), "Autonomous Drone Path Planner", "Python, OpenCV, ROS", "Completed (Grade S)", "Fahran, Ajay, Arun"),
                new ProjectHistory(savedStudents.get(0).getId(), "Hospital Patient Analytics", "Java, Spring Boot, MySQL", "Deployed to Production", "Fahran, Priya"),
                new ProjectHistory(savedStudents.get(1).getId(), "Microservices E-Commerce Portal", "Spring Boot, Docker, Redis", "Completed (Grade A)", "Ajay, Fahran, Arun"),
                new ProjectHistory(savedStudents.get(2).getId(), "Fintech Mobile Design System", "Figma, React Native", "Completed (Grade S)", "Priya, Divya"),
                new ProjectHistory(savedStudents.get(4).getId(), "Multimodal Medical Diagnosis AI", "PyTorch, FastAPI, React", "Completed (Grade S)", "Ananya, Rohan, Sneha"),
                new ProjectHistory(savedStudents.get(5).getId(), "Campus Smart Parking System", "Spring Boot, Vue.js, PostgreSQL", "Completed (Grade A)", "Rohan, Vikram"),
                new ProjectHistory(savedStudents.get(9).getId(), "Blood Donation Bridge App", "Flutter, Firebase, Kotlin", "Completed (Grade S)", "Karthik, Divya, Tanvi")
        ));

        System.out.println("[TeamForge DataInit] Seeded 15 CSE students with hackathons and project records.");
    }

    private void initCollaborations() {
        List<Student> students = studentRepository.findAll();
        if (students.size() >= 4) {
            collaborationHistoryRepository.saveAll(Arrays.asList(
                    new CollaborationHistory(students.get(0).getId(), students.get(1).getId(), students.get(0).getName(), students.get(1).getName(), 2, 2, 92.0, "Microservices E-Commerce"),
                    new CollaborationHistory(students.get(0).getId(), students.get(2).getId(), students.get(0).getName(), students.get(2).getName(), 1, 1, 88.0, "Hospital Patient Analytics"),
                    new CollaborationHistory(students.get(0).getId(), students.get(3).getId(), students.get(0).getName(), students.get(3).getName(), 2, 2, 86.5, "Autonomous Drone Path Planner"),
                    new CollaborationHistory(students.get(1).getId(), students.get(3).getId(), students.get(1).getName(), students.get(3).getName(), 2, 2, 89.0, "Microservices E-Commerce"),
                    new CollaborationHistory(students.get(4).getId(), students.get(5).getId(), students.get(4).getName(), students.get(5).getName(), 3, 3, 95.0, "Multimodal Medical Diagnosis AI"),
                    new CollaborationHistory(students.get(4).getId(), students.get(6).getId(), students.get(4).getName(), students.get(6).getName(), 2, 2, 90.0, "Multimodal Medical Diagnosis AI"),
                    new CollaborationHistory(students.get(9).getId(), students.get(10).getId(), students.get(9).getName(), students.get(10).getName(), 2, 2, 91.0, "Blood Donation Bridge App"),
                    new CollaborationHistory(students.get(9).getId(), students.get(12).getId(), students.get(9).getName(), students.get(12).getName(), 1, 1, 85.0, "Blood Donation Bridge App")
            ));
            System.out.println("[TeamForge DataInit] Seeded student collaboration histories.");
        }
    }

    private void initHistoricalTeams() {
        List<Student> students = studentRepository.findAll();
        if (students.size() >= 12) {
            Team team1 = new Team(
                    "Team Alpha",
                    "AI & Machine Learning",
                    89.4,
                    "High Potential",
                    88.0,
                    92.0,
                    82.0,
                    75.0,
                    88.5,
                    "Strong coding capability across team members | Good role diversity | Proven past collaboration history",
                    "Documentation skill is comparatively low",
                    "2025-2026"
            );
            teamRepository.save(team1);
            teamMemberRepository.saveAll(Arrays.asList(
                    new TeamMember(team1, students.get(0), "Developer"),
                    new TeamMember(team1, students.get(1), "Team Lead"),
                    new TeamMember(team1, students.get(2), "UI/UX Designer"),
                    new TeamMember(team1, students.get(3), "QA & Documentation")
            ));

            Team team2 = new Team(
                    "Team Phoenix",
                    "Full-Stack Web Development",
                    88.1,
                    "High Potential",
                    86.5,
                    90.0,
                    85.0,
                    80.0,
                    87.0,
                    "Excellent technical proficiency | High role diversity covering Developer and Designer roles",
                    "Ensure sprint timelines are clearly tracked",
                    "2025-2026"
            );
            teamRepository.save(team2);
            teamMemberRepository.saveAll(Arrays.asList(
                    new TeamMember(team2, students.get(4), "Developer"),
                    new TeamMember(team2, students.get(5), "Team Lead"),
                    new TeamMember(team2, students.get(6), "UI/UX Designer"),
                    new TeamMember(team2, students.get(7), "Developer")
            ));

            Team team3 = new Team(
                    "Team Nova",
                    "Mobile Computing",
                    83.5,
                    "High Potential",
                    80.0,
                    85.0,
                    78.0,
                    70.0,
                    84.0,
                    "High alignment in mobile project domain interests | Solid communication coverage",
                    "Moderate hackathon competitive exposure",
                    "2025-2026"
            );
            teamRepository.save(team3);
            teamMemberRepository.saveAll(Arrays.asList(
                    new TeamMember(team3, students.get(9), "Developer"),
                    new TeamMember(team3, students.get(10), "UI/UX Designer"),
                    new TeamMember(team3, students.get(12), "Team Lead"),
                    new TeamMember(team3, students.get(8), "QA & Documentation")
            ));

            System.out.println("[TeamForge DataInit] Seeded historical teams for leaderboard and comparisons.");
        }
    }

    private void initEvents() {
        eventRepository.saveAll(Arrays.asList(
                new Event("Smart India Hackathon 2026 (Internal Round)", "National premier open innovation hackathon solving real-world challenges.", "AI & Machine Learning", "Oct 15, 2026", "2025-2026", 6),
                new Event("CSE Annual Mini-Project Exhibition", "Departmental evaluation and live software demonstrations for 2nd year CSE.", "Full-Stack Web Development", "Nov 20, 2026", "2025-2026", 12),
                new Event("National Cloud & DevOps HackFest", "Cloud-native architectural sprint focusing on scalable microservices.", "Cloud & DevOps", "Dec 05, 2026", "2025-2026", 8)
        ));
        System.out.println("[TeamForge DataInit] Seeded college hackathon & project events.");
    }
}
