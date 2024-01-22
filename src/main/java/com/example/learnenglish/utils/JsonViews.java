package com.example.learnenglish.utils;

public final class JsonViews {
    public interface ViewFieldId{}
    public interface ViewWordId{}
    public interface ViewFieldName{}
    public interface ViewFieldUserLogin{}
    public interface ViewFieldUserEmail{}
    public interface ViewFieldUserDateOfCreated{}
    public interface ViewFieldUserDateLastVisit{}
    public interface ViewFieldOther{}
    public interface ViewFieldImage{}
    public interface ViewFieldWord{}
    public interface ViewFieldAudio{}

    public interface ViewIdAndName extends ViewWordId, ViewFieldId, ViewFieldName{}
    public interface ViewAllCategories extends ViewFieldId, ViewFieldName, ViewFieldOther{}
    public interface ViewWordForWordLesson extends ViewFieldId, ViewFieldName, ViewFieldOther, ViewFieldWord, ViewFieldImage, ViewFieldAudio{}
}
