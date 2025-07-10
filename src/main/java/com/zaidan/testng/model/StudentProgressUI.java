// In a new file: model/StudentProgressUI.java
package com.zaidan.testng.model;

import com.zaidan.testng.enums.TaskStatus;
import java.util.Map;

public class StudentProgressUI {
    private String studentName;
    private double progressPercentage;
    private Map<String, TaskStatus> taskStatuses; // e.g., <"M4", TaskStatus.IN_PROGRESS>

    // Constructor, Getters, and Setters
    public StudentProgressUI(String studentName, double progressPercentage, Map<String, TaskStatus> taskStatuses) {
        this.studentName = studentName;
        this.progressPercentage = progressPercentage;
        this.taskStatuses = taskStatuses;
    }

    public String getStudentName() { return studentName; }
    public double getProgressPercentage() { return progressPercentage; }
    public TaskStatus getStatusForTask(String taskName) {
        return taskStatuses.get(taskName);
    }
    public Map<String, TaskStatus> getTaskStatuses() {
        return this.taskStatuses;
    }
}
