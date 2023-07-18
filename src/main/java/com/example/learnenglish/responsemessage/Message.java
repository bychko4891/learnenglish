package com.example.learnenglish.responsemessage;

/**
 * @author: Anatolii Bychko
 * Application Name: Learn English
 * Description: My Description
 *  GitHub source code: https://github.com/bychko4891/learnenglish
 */

public enum Message {
    SUCCESSADDBASE("Success","Операція пройшла успішно"),
    ERRORBASE("Error","Щось трапилося з базою данних і операція збереження не може бути виконаною.\n" +
            " Напишіть будь ласка про цю помилку адміністратору застосунка"),
    ERROR_DUPLICATE_TEXT("Error","Така фраза вже є у Вашій базі"),
    ERRORVALIDATETEXT("Error","Щось пішло не так! Ви не пройшли валідацію текста"),
    ERRORNULLRELOAD("Error","Ви ще не завантажили жодного текста для навчання"),
    ERRORLOGIN("Error", " Ви не авторизувались і не можете завантажувати текст"),
    ERROR_UPDATEPASSWORD("Error", "Не вірний поточний пароль"),
    SUCCESS_UPDATEPASSWORD("Success", "Ваш пароль успішно змінено"),
    SUCCESS_CREATELESSON("Success", "Нове заняття створено"),
    ERROR_CREATELESSON("Success", "Виникла помилка в створенні заняття"),
    SUCCESS_FORGOT_PASSWORD("Success", "Операція успішна! Слідуйте вказівкам в листі, яке надійшло на email."),
    ERROR_FORGOT_PASSWORD("Error", "Такий email не зареєстрований в додатку."),
    ERROR_CAPTCHA("Error", "Не вірно введена сумма двух чисел"),
    SUCCESS_SAVE_TEXT_OF_PAGE("Success", "Текст успішно збережено"),
    ERROR_SAVE_TEXT_OF_PAGE("Error", "На обраній сторінці текст вже існує"),
    SUCCESS_CHECKBOX("Success", "Параметр успішно застосовано"),
    ERROR_SAVE_CATEGORY("Error", "Не можно додати поточну категорію в вибрану"),
    SUCCESS_SAVE_WORD_USER("Success", "Слово успішно збережено до Вашого списку"),
    SUCCESS_REMOVE_USER_PHRASE("Success", "Видалення успішне"),
    ERROR_SERVER("Error", "Помилка на сервері");

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
