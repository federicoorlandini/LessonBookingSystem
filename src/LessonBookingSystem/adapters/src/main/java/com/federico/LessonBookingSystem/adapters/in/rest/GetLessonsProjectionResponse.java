package com.federico.LessonBookingSystem.adapters.in.rest;

import Lesson.Lesson;

import java.util.List;

public record GetLessonsProjectionResponse(List<Lesson> lessons) {
}
