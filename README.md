# BugTrackerPro - Company Workflow Simulator

## Overview
BugTrackerPro is a Java-based simulation of a professional software development environment. It manages the lifecycle of software projects by tracking bugs, assigning tasks to users based on roles, and simulating the day-to-day workflow of a tech company.

## Key Features
- **User Role Management:** Distinct logic for `Testers` and `Programmers`.
- **Project & Ticket Tracking:** Create projects and manage tickets with varying priorities (Low, Medium, High).
- **Dynamic Command System:** Processes complex inputs to simulate actions like adding users, creating tickets, and updating project statuses.
- **Data Persistence:** Efficiently manages internal data structures to maintain the state of the company workflow.

## Technical Implementation
- **Language:** Java
- **Concepts used:** 
    - **Inheritance & Polymorphism:** To differentiate between user types and ticket behaviors.
    - **Collections Framework:** Heavy use of `ArrayList`, `HashMap`, and `Collections.sort()` for data organization.
    - **Encapsulation:** Ensuring data integrity across the simulation.
    - **Design Patterns:** Implementation of patterns (e.g., Singleton or Factory) to manage object creation and system states.

## How to Run
1. Clone the repository:
   ```bash
   git clone https://github.com/AndreiStreche0/BugTrackerPro.git
