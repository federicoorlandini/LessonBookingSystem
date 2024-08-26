package com.federico.LessonBookingSystem.adapters.in.rest;

import model.Lesson;

import java.util.List;

public record GetLessonsProjectionResponse(List<Lesson> lessons) {
}
