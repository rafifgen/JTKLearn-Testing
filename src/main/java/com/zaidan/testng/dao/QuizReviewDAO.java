package com.zaidan.testng.dao;

import com.zaidan.testng.model.QuizReview;
import com.zaidan.testng.model.QuizQuestion;
import com.zaidan.testng.utils.DatabaseUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class QuizReviewDAO {

    // Get quiz review data for a specific student and quiz
    public QuizReview getQuizReviewForStudent(String studentEmail, int quizId) {
        QuizReview quizReview = null;
        String sql = "SELECT " +
                "    q.id_quiz, " +
                "    q.nama_quiz, " +
                "    q.deskripsi_quiz, " +
                "    p.id_pelajar, " +
                "    hq.nilai " +
                "FROM quiz q " +
                "JOIN \"historyQuiz\" hq ON q.id_quiz = hq.id_quiz " +
                "JOIN pelajar p ON hq.id_pelajar = p.id_pelajar " +
                "JOIN users u ON p.id_user = u.id_user " +
                "WHERE u.email = ? AND q.id_quiz = ? " +
                "ORDER BY hq.waktu_selesai DESC " +
                "LIMIT 1";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, studentEmail);
            pstmt.setInt(2, quizId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int dbQuizId = rs.getInt("id_quiz");
                    String quizName = rs.getString("nama_quiz");
                    String quizDescription = rs.getString("deskripsi_quiz");
                    int studentId = rs.getInt("id_pelajar");
                    double score = rs.getDouble("nilai");

                    // Get questions for this quiz
                    List<QuizQuestion> questions = getQuizQuestionsForStudent(studentEmail, quizId);

                    String reviewUrl = "https://polban-space.cloudias79.com/jtk-learn/learn-course/" + quizId + "?mode=review";

                    quizReview = new QuizReview(
                        dbQuizId,
                        quizName,
                        quizDescription,
                        studentId,
                        score,
                        questions,
                        reviewUrl
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving quiz review for student: " + e.getMessage());
            e.printStackTrace();
        }

        return quizReview;
    }

    // Get quiz with questions and answers using the new database schema
    public QuizReview getQuizWithQuestionsAndAnswers(int quizId) {
        QuizReview quizReview = null;
        String sql = "SELECT " +
                "    q.\"id_quiz\", " +
                "    q.\"nama_quiz\", " +
                "    q.\"deskripsi_quiz\", " +
                "    q.\"durasi\", " +
                "    p.\"id_pertanyaan\", " +
                "    p.\"konten_pertanyaan\", " +
                "    p.\"jenis_pertanyaan\", " +
                "    j.\"id_jawaban\", " +
                "    j.\"nama_jawaban\", " +
                "    j.\"konten_jawaban\", " +
                "    j.\"status_jawaban\" " +
                "FROM " +
                "    \"quiz\" q " +
                "JOIN " +
                "    \"pertanyaan\" p ON p.\"id_quiz\" = q.\"id_quiz\" " +
                "LEFT JOIN " +
                "    \"jawaban\" j ON j.\"id_pertanyaan\" = p.\"id_pertanyaan\" " +
                "WHERE " +
                "    q.\"id_quiz\" = ? " +
                "ORDER BY " +
                "    p.\"id_pertanyaan\", j.\"id_jawaban\"";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, quizId);

            try (ResultSet rs = pstmt.executeQuery()) {
                List<QuizQuestion> questions = new ArrayList<>();
                String currentQuizName = null;
                String currentQuizDescription = null;
                int currentQuestionId = -1;
                QuizQuestion currentQuestion = null;
                List<String> currentOptions = null;
                String correctAnswer = null;

                while (rs.next()) {
                    // Quiz information
                    if (currentQuizName == null) {
                        currentQuizName = rs.getString("nama_quiz");
                        currentQuizDescription = rs.getString("deskripsi_quiz");
                    }

                    int questionId = rs.getInt("id_pertanyaan");
                    
                    // Check if we're starting a new question
                    if (questionId != currentQuestionId) {
                        // Save previous question if exists
                        if (currentQuestion != null) {
                            currentQuestion.setOptions(currentOptions);
                            currentQuestion.setCorrectAnswer(correctAnswer);
                            questions.add(currentQuestion);
                        }

                        // Start new question
                        currentQuestionId = questionId;
                        currentQuestion = new QuizQuestion();
                        currentQuestion.setQuestionId(questionId);
                        currentQuestion.setQuestionText(rs.getString("konten_pertanyaan"));
                        currentQuestion.setQuestionType(rs.getString("jenis_pertanyaan"));
                        currentOptions = new ArrayList<>();
                        correctAnswer = null;
                    }

                    // Add answer options
                    String answerContent = rs.getString("konten_jawaban");
                    String answerStatus = rs.getString("status_jawaban");
                    
                    if (answerContent != null) {
                        currentOptions.add(answerContent);
                        
                        // Check if this is the correct answer
                        if ("benar".equals(answerStatus)) {
                            correctAnswer = answerContent;
                        }
                    }
                }

                // Don't forget the last question
                if (currentQuestion != null) {
                    currentQuestion.setOptions(currentOptions);
                    currentQuestion.setCorrectAnswer(correctAnswer);
                    questions.add(currentQuestion);
                }

                // Create QuizReview object
                if (currentQuizName != null) {
                    String reviewUrl = "https://polban-space.cloudias79.com/jtk-learn/learn-course/" + quizId + "?mode=review";
                    quizReview = new QuizReview(
                        quizId,
                        currentQuizName,
                        currentQuizDescription,
                        0, // studentId not available from this query
                        0.0, // score not available from this query
                        questions,
                        reviewUrl
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving quiz with questions and answers: " + e.getMessage());
            e.printStackTrace();
        }

        return quizReview;
    }

    // Get all quiz questions and answers for a specific student and quiz
    public List<QuizQuestion> getQuizQuestionsForStudent(String studentEmail, int quizId) {
        List<QuizQuestion> questions = new ArrayList<>();
        String sql = "SELECT " +
                "    qq.id_question, " +
                "    qq.pertanyaan, " +
                "    qq.jenis_question, " +
                "    qq.pilihan_a, " +
                "    qq.pilihan_b, " +
                "    qq.pilihan_c, " +
                "    qq.pilihan_d, " +
                "    qq.kunci_jawaban, " +
                "    hq.jawaban_pelajar, " +
                "    CASE " +
                "        WHEN qq.kunci_jawaban = hq.jawaban_pelajar THEN true " +
                "        ELSE false " +
                "    END as is_correct " +
                "FROM \"questionQuiz\" qq " +
                "JOIN \"historyQuiz\" hq ON qq.id_quiz = hq.id_quiz " +
                "JOIN pelajar p ON hq.id_pelajar = p.id_pelajar " +
                "JOIN users u ON p.id_user = u.id_user " +
                "WHERE u.email = ? AND qq.id_quiz = ? " +
                "ORDER BY qq.id_question";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, studentEmail);
            pstmt.setInt(2, quizId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int questionId = rs.getInt("id_question");
                    String questionText = rs.getString("pertanyaan");
                    String questionType = rs.getString("jenis_question");
                    String correctAnswer = rs.getString("kunci_jawaban");
                    String studentAnswer = rs.getString("jawaban_pelajar");
                    boolean isCorrect = rs.getBoolean("is_correct");

                    // Create options list for multiple choice questions
                    List<String> options = new ArrayList<>();
                    options.add(rs.getString("pilihan_a"));
                    options.add(rs.getString("pilihan_b"));
                    options.add(rs.getString("pilihan_c"));
                    options.add(rs.getString("pilihan_d"));

                    // Set border and status classes based on correctness
                    String borderClass = isCorrect ? "border-success" : "border-danger";
                    String statusClass = isCorrect ? "text-success" : "text-danger";

                    QuizQuestion question = new QuizQuestion(
                        questionId,
                        questionText,
                        questionType,
                        options,
                        correctAnswer,
                        studentAnswer,
                        isCorrect,
                        borderClass,
                        statusClass
                    );

                    questions.add(question);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving quiz questions for student: " + e.getMessage());
            e.printStackTrace();
        }

        return questions;
    }

    // Get student ID by email
    public int getStudentIdByEmail(String email) {
        int studentId = 0;
        String sql = "SELECT p.id_pelajar " +
                "FROM pelajar p " +
                "JOIN users u ON p.id_user = u.id_user " +
                "WHERE u.email = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    studentId = rs.getInt("id_pelajar");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving student ID: " + e.getMessage());
            e.printStackTrace();
        }

        return studentId;
    }

    // Get quiz details by ID
    public QuizReview getQuizDetailsById(int quizId) {
        QuizReview quizReview = null;
        String sql = "SELECT " +
                "    id_quiz, " +
                "    nama_quiz, " +
                "    deskripsi_quiz " +
                "FROM quiz " +
                "WHERE id_quiz = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, quizId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String quizName = rs.getString("nama_quiz");
                    String quizDescription = rs.getString("deskripsi_quiz");
                    String reviewUrl = "https://polban-space.cloudias79.com/jtk-learn/learn-course/" + quizId + "?mode=review";

                    quizReview = new QuizReview(
                        quizId,
                        quizName,
                        quizDescription,
                        0, // studentId not available from this query
                        0.0, // score not available from this query
                        new ArrayList<>(), // questions not retrieved in this method
                        reviewUrl
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving quiz details: " + e.getMessage());
            e.printStackTrace();
        }

        return quizReview;
    }

    // Get all completed quizzes for a student
    public List<QuizReview> getCompletedQuizzesForStudent(String studentEmail) {
        List<QuizReview> completedQuizzes = new ArrayList<>();
        String sql = "SELECT DISTINCT " +
                "    q.id_quiz, " +
                "    q.nama_quiz, " +
                "    q.deskripsi_quiz, " +
                "    p.id_pelajar, " +
                "    MAX(hq.nilai) as highest_score " +
                "FROM quiz q " +
                "JOIN \"historyQuiz\" hq ON q.id_quiz = hq.id_quiz " +
                "JOIN pelajar p ON hq.id_pelajar = p.id_pelajar " +
                "JOIN users u ON p.id_user = u.id_user " +
                "WHERE u.email = ? " +
                "GROUP BY q.id_quiz, q.nama_quiz, q.deskripsi_quiz, p.id_pelajar " +
                "ORDER BY q.id_quiz";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, studentEmail);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    int quizId = rs.getInt("id_quiz");
                    String quizName = rs.getString("nama_quiz");
                    String quizDescription = rs.getString("deskripsi_quiz");
                    int studentId = rs.getInt("id_pelajar");
                    double score = rs.getDouble("highest_score");
                    String reviewUrl = "https://polban-space.cloudias79.com/jtk-learn/learn-course/" + quizId + "?mode=review";

                    QuizReview quizReview = new QuizReview(
                        quizId,
                        quizName,
                        quizDescription,
                        studentId,
                        score,
                        new ArrayList<>(), // questions not retrieved in this method
                        reviewUrl
                    );

                    completedQuizzes.add(quizReview);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving completed quizzes: " + e.getMessage());
            e.printStackTrace();
        }

        return completedQuizzes;
    }

    // Validate quiz review data specifically for database consistency
    public boolean validateQuizReviewData(QuizReview uiQuizReview, QuizReview dbQuizReview) {
        if (uiQuizReview == null || dbQuizReview == null) {
            return false;
        }

        // Check quiz basic information
        if (!uiQuizReview.getQuizName().equals(dbQuizReview.getQuizName())) {
            System.err.println("Quiz name mismatch: UI=" + uiQuizReview.getQuizName() + ", DB=" + dbQuizReview.getQuizName());
            return false;
        }

        // Check questions count
        if (uiQuizReview.getQuestions().size() != dbQuizReview.getQuestions().size()) {
            System.err.println("Questions count mismatch: UI=" + uiQuizReview.getQuestions().size() + ", DB=" + dbQuizReview.getQuestions().size());
            return false;
        }

        // Check each question
        for (int i = 0; i < uiQuizReview.getQuestions().size(); i++) {
            QuizQuestion uiQuestion = uiQuizReview.getQuestions().get(i);
            QuizQuestion dbQuestion = dbQuizReview.getQuestions().get(i);

            if (!validateQuestionData(uiQuestion, dbQuestion)) {
                return false;
            }
        }

        return true;
    }

    // Validate individual question data
    private boolean validateQuestionData(QuizQuestion uiQuestion, QuizQuestion dbQuestion) {
        if (!uiQuestion.getQuestionText().equals(dbQuestion.getQuestionText())) {
            System.err.println("Question text mismatch for question " + uiQuestion.getQuestionId());
            return false;
        }

        if (!uiQuestion.getStudentAnswer().equals(dbQuestion.getStudentAnswer())) {
            System.err.println("Student answer mismatch for question " + uiQuestion.getQuestionId());
            return false;
        }

        if (!uiQuestion.getCorrectAnswer().equals(dbQuestion.getCorrectAnswer())) {
            System.err.println("Correct answer mismatch for question " + uiQuestion.getQuestionId());
            return false;
        }

        if (uiQuestion.isCorrect() != dbQuestion.isCorrect()) {
            System.err.println("Answer correctness mismatch for question " + uiQuestion.getQuestionId());
            return false;
        }

        return true;
    }
}
