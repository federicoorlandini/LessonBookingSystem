# LESSON BOOKING SYSTEM

# Build pipeline status
![main branch](https://github.com/federicoorlandini/LessonBookingSystem/actions/workflows/gradle.yml/badge.svg)

## Why did I start this project?
I started this project to dirt my hands with these technologies:

* Event modelling (see https://eventmodeling.org/)
* Event sourcing
* Hexagonal architecture
* Java SpringBoot with Gradle
* EventStoreDB (https://www.eventstore.com/eventstoredb)
* React.js for the front-end
* The web app must be hosted in Azure
* GitHub CI pipeline

## Objective
The objective of this project is to build a simple software that can help in managing presences for lessons. For example, boxing lessons, gym lessons, etc.

## Stories
This system must allow the users to:

* As administrator, 
    I want to be able to define a list of available lessons, including the date of the lesson, the start time and the end time and the max allowed participants
* As administrator,
    I want to be able to see the list of the partecipants to a lesson
* As member,
    I want to be able to book a spot in a lesson
