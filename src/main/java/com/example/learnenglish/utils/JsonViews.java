package com.example.learnenglish.utils;

public final class JsonViews {
    public interface ViewFieldId{}
    public interface ViewFieldName{}
    public interface ViewFieldUserLogin{}
    public interface ViewFieldUserEmail{}
    public interface ViewFieldUserDateOfCreated{}
    public interface ViewFieldUserDateLastVisit{}

    public interface ViewIdAndName extends ViewFieldId, ViewFieldName{}
}
