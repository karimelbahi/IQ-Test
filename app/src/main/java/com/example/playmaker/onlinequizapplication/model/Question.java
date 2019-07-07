package com.example.playmaker.onlinequizapplication.model;

public class Question {
    private String Question, AnswerA, AnswerB, AnswerC, AnswerD,AnswerE , CorrectAnswer, CategoryId, IsImageQuestion;

    public Question() {
    }

    public Question(String question, String answerA, String answerB, String answerC, String answerD, String answerE, String correctAnswer, String categoryId, String isImageQuestion) {
        Question = question;
        AnswerA = answerA;
        AnswerB = answerB;
        AnswerC = answerC;
        AnswerD = answerD;
        AnswerE = answerE;
        CorrectAnswer = correctAnswer;
        CategoryId = categoryId;
        IsImageQuestion = isImageQuestion;
    }

    public String getQuestion() {
        return Question;
    }

    public String getAnswerA() {
        return AnswerA;
    }

    public String getAnswerB() {
        return AnswerB;
    }

    public String getAnswerC() {
        return AnswerC;
    }

    public String getAnswerD() {
        return AnswerD;
    }

    public String getAnswerE() {
        return AnswerE;
    }

    public String getCorrectAnswer() {
        return CorrectAnswer;
    }

    public String getCategoryId() {
        return CategoryId;
    }

    public String getIsImageQuestion() {
        return IsImageQuestion;
    }
}
