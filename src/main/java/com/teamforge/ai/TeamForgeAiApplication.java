package com.teamforge.ai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TeamForgeAiApplication {

    public static void main(String[] args) {
        SpringApplication.run(TeamForgeAiApplication.class, args);
        System.out.println("===============================================================");
        System.out.println("🚀 TEAMFORGE AI - Intelligent Team Formation Platform Running!");
        System.out.println("🌐 URL: http://localhost:8080");
        System.out.println("👥 Default Login: faculty / faculty123  or  admin / admin123");
        System.out.println("===============================================================");
    }
}
