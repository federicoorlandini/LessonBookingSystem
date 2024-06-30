package io.rest;

public class CreateLessonRequest {
    private final String dayAndTime;
    private final int maxNumberAttenders;

    public CreateLessonRequest(String dayAndTime, int maxNumberAttenders) {
        this.dayAndTime = dayAndTime;
        this.maxNumberAttenders = maxNumberAttenders;
    }
}
