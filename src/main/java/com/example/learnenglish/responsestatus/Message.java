package com.example.learnenglish.responsestatus;

public enum Message {
    SUCCESSADDBASE("Success","Операція пройшла успішно"),
    ERRORBASE("Error","Щось трапилося з базою данних і операція збереження не може бути виконаною.\n" +
            " Напишіть будь ласка про цю помилку адміністратору застосунка"),
    ERRORADDBASE("Error","Щось пішло не так! Можливо Ви дупистили десь помилку в знаках пунктуації.");

    private String status;
    private String message;

    Message(String status, String message) {
        this.status = status;
        this.message = message;
    }

    @Override
    public String toString() {
        return "status" + status +","+ message;
    }

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }
}
