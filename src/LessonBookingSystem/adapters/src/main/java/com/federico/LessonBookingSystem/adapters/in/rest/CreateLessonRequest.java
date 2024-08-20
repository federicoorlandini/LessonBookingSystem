package com.federico.LessonBookingSystem.adapters.in.rest;

public record CreateLessonRequest(String date, String startTime, String endTime, int maxNumberAttenders) {
    // Nothing here
}
