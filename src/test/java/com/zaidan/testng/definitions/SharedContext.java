package com.zaidan.testng.definitions;

public class SharedContext {
    private static String currentStudentEmail;
    private static int currentQuizId = 1;
    
    public static void setStudentEmail(String email) {
        currentStudentEmail = email;
        System.out.println("SharedContext: Student email set to: " + email);
    }
    
    public static String getStudentEmail() {
        return currentStudentEmail;
    }
    
    public static void setQuizId(int quizId) {
        currentQuizId = quizId;
    }
    
    public static int getQuizId() {
        return currentQuizId;
    }
    
    public static void clear() {
        currentStudentEmail = null;
        currentQuizId = 1;
    }
} 