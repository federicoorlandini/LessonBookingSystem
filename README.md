# LESSON BOOKING SYSTEM

# Build pipeline status
![main branch](https://github.com/federicoorlandini/LessonBookingSystem/actions/workflows/gradle.yml/badge.svg)

## Why did I start this project?
I started this project to dirt my hands with these technologies:

* Event modelling (see https://eventmodeling.org/)
* Event sourcing
* Hexagonal architecture
* Java SpringBoot with Gradle
    * Integration tests using TestContainers (https://testcontainers.com/) for the EventStoreDB
* EventStoreDB (https://www.eventstore.com/eventstoredb)
* React.js for the front-end
* The web app must be hosted in Azure
* GitHub CI pipeline

## Objective
The objective of this project is to build a simple software that can help in managing presences for lessons. For example, boxing lessons, gym lessons, etc.

## User stories
Refer to the event modelling model here (Draw.io diagram): https://github.com/federicoorlandini/LessonBookingSystem/blob/main/diagrams/LessonBookingSystem%20-%20Event%20modelling.drawio

## Resources
* Event modelling:
    * Event modelling - https://eventmodeling.org/
    * Copenhagen DDD Event Modeling with Adam Dymitruk - https://www.youtube.com/watch?v=U_MwAEf8V_A

* Event sourcing:
    * Oskar Dudycz - Pragmatic about programming - https://event-driven.io/en/