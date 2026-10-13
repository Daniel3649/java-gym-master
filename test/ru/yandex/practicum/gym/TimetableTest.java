package ru.yandex.practicum.gym;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TimetableTest {
    @Test
    void testGetTrainingSessionsForDaySingleSession() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник вернулось одно занятие
        //Проверить, что за вторник не вернулось занятий
        Map<TimeOfDay, List<TrainingSession>> trainingsByMonday =
                timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        Map<TimeOfDay, List<TrainingSession>> trainingsByTuesday =
                timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);

        assertIterableEquals(List.of(singleTrainingSession), getTrainingSessionsList(trainingsByMonday));

        assertTrue(trainingsByTuesday.isEmpty());
    }

    @Test
    void testGetTrainingSessionsForDayMultipleSessions() {
        Timetable timetable = new Timetable();

        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");

        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        TrainingSession thursdayAdultTrainingSession = new TrainingSession(groupAdult, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(20, 0));

        timetable.addNewTrainingSession(thursdayAdultTrainingSession);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        TrainingSession mondayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.MONDAY, new TimeOfDay(13, 0));
        TrainingSession thursdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.THURSDAY, new TimeOfDay(13, 0));
        TrainingSession saturdayChildTrainingSession = new TrainingSession(groupChild, coach,
                DayOfWeek.SATURDAY, new TimeOfDay(10, 0));

        timetable.addNewTrainingSession(mondayChildTrainingSession);
        timetable.addNewTrainingSession(thursdayChildTrainingSession);
        timetable.addNewTrainingSession(saturdayChildTrainingSession);

        // Проверить, что за понедельник вернулось одно занятие
        // Проверить, что за четверг вернулось два занятия в правильном порядке: сначала в 13:00, потом в 20:00
        // Проверить, что за вторник не вернулось занятий
        Map<TimeOfDay, List<TrainingSession>> trainingsByMonday =
                timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);
        Map<TimeOfDay, List<TrainingSession>> trainingsByTuesday =
                timetable.getTrainingSessionsForDay(DayOfWeek.TUESDAY);
        Map<TimeOfDay, List<TrainingSession>> trainingsByThursday =
                timetable.getTrainingSessionsForDay(DayOfWeek.THURSDAY);

        assertIterableEquals(List.of(mondayChildTrainingSession), getTrainingSessionsList(trainingsByMonday));

        assertTrue(trainingsByTuesday.isEmpty());

        assertIterableEquals(
                List.of(thursdayChildTrainingSession, thursdayAdultTrainingSession),
                getTrainingSessionsList(trainingsByThursday)
        );
    }

    @Test
    void testGetTrainingSessionsForDayAndTime() {
        Timetable timetable = new Timetable();

        TimeOfDay timeOfDay = new TimeOfDay(13, 0);
        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coach = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession singleTrainingSession = new TrainingSession(group, coach,
                DayOfWeek.MONDAY, timeOfDay);

        timetable.addNewTrainingSession(singleTrainingSession);

        //Проверить, что за понедельник в 13:00 вернулось одно занятие
        //Проверить, что за понедельник в 14:00 не вернулось занятий
        List<TrainingSession> trainingsByMondayAnd1PM = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY,
                timeOfDay
        );
        List<TrainingSession> trainingsByMondayAnd2PM = timetable.getTrainingSessionsForDayAndTime(
                DayOfWeek.MONDAY,
                new TimeOfDay(14, 0)
        );

        assertIterableEquals(List.of(singleTrainingSession), trainingsByMondayAnd1PM);

        assertTrue(trainingsByMondayAnd2PM.isEmpty());
    }

    @ParameterizedTest
    @EnumSource(DayOfWeek.class)
    void testEmptyTimetable(DayOfWeek dayOfWeek) {
        Timetable timetable = new Timetable();

        Map<TimeOfDay, List<TrainingSession>> trainingSessionsByDayOfWeek =
                timetable.getTrainingSessionsForDay(dayOfWeek);

        assertTrue(trainingSessionsByDayOfWeek.isEmpty());
    }

    @Test
    void testMultipleSessionsAtTheSameTime() {
        Timetable timetable = new Timetable();

        TimeOfDay timeOfDay = new TimeOfDay(13, 0);
        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        Coach coachAdult = new Coach("Васильев", "Николай", "Сергеевич");
        Coach coachChild = new Coach("Смолов", "Иван", "Федорович");
        TrainingSession trainingSessionChild = new TrainingSession(
                groupChild,
                coachChild,
                DayOfWeek.MONDAY,
                timeOfDay
        );
        TrainingSession trainingSessionAdult = new TrainingSession(
                groupAdult,
                coachAdult,
                DayOfWeek.MONDAY,
                timeOfDay
        );

        timetable.addNewTrainingSession(trainingSessionChild);
        timetable.addNewTrainingSession(trainingSessionAdult);

        List<TrainingSession> trainingSessionsByMondayAnd1PM =
                timetable.getTrainingSessionsForDayAndTime(DayOfWeek.MONDAY, timeOfDay);

        assertIterableEquals(
                List.of(trainingSessionChild, trainingSessionAdult),
                trainingSessionsByMondayAnd1PM
        );
    }

    @Test
    void testSessionsSortedByMinutes() {
        Timetable timetable = new Timetable();

        TimeOfDay time1PMAnd10Minutes = new TimeOfDay(13, 10);
        TimeOfDay time1PMAnd5Minutes = new TimeOfDay(13, 5);

        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);

        Coach coachAdult = new Coach("Васильев", "Николай", "Сергеевич");
        Coach coachChild = new Coach("Смолов", "Иван", "Федорович");

        TrainingSession trainingSessionAt1PMAnd10Minutes = new TrainingSession(
                groupChild,
                coachChild,
                DayOfWeek.MONDAY,
                time1PMAnd10Minutes
        );
        TrainingSession trainingSessionAdultAt1PMAnd5Minutes = new TrainingSession(
                groupAdult,
                coachAdult,
                DayOfWeek.MONDAY,
                time1PMAnd5Minutes
        );

        timetable.addNewTrainingSession(trainingSessionAt1PMAnd10Minutes);
        timetable.addNewTrainingSession(trainingSessionAdultAt1PMAnd5Minutes);

        Map<TimeOfDay, List<TrainingSession>> trainingSessionsByMonday =
                timetable.getTrainingSessionsForDay(DayOfWeek.MONDAY);

        assertIterableEquals(
                List.of(trainingSessionAdultAt1PMAnd5Minutes, trainingSessionAt1PMAnd10Minutes),
                getTrainingSessionsList(trainingSessionsByMonday)
        );
    }

    @Test
    void testGetCountByCoachesEmptyTimetable() {
        Timetable timetable = new Timetable();

        Map<Coach, Long> countByCoaches = timetable.getCountByCoaches();

        assertTrue(countByCoaches.isEmpty());
    }

    @Test
    void testGetCountByCoachesGroupsEqualCoaches() {
        Timetable timetable = new Timetable();

        TimeOfDay timeOfDay = new TimeOfDay(13, 0);
        Group groupChild = new Group("Акробатика для детей", Age.CHILD, 60);
        Group groupAdult = new Group("Акробатика для взрослых", Age.ADULT, 90);
        Coach coachAdult = new Coach("Васильев", "Николай", "Сергеевич");
        Coach coachChild = new Coach("Васильев", "Николай", "Сергеевич");
        TrainingSession trainingSessionAtMonday = new TrainingSession(
                groupChild,
                coachChild,
                DayOfWeek.MONDAY,
                timeOfDay
        );
        TrainingSession trainingSessionAtTuesday = new TrainingSession(
                groupAdult,
                coachAdult,
                DayOfWeek.TUESDAY,
                timeOfDay
        );

        timetable.addNewTrainingSession(trainingSessionAtMonday);
        timetable.addNewTrainingSession(trainingSessionAtTuesday);

        Map<Coach, Long> countByCoaches = timetable.getCountByCoaches();

        assertEquals(Map.of(coachAdult, 2L), countByCoaches);
    }

    @Test
    void testGetCountByCoachesSortsBySessionCount() {
        Timetable timetable = new Timetable();

        Group group = new Group("Акробатика для детей", Age.CHILD, 60);
        Coach coachWithOneSession = new Coach("Васильев", "Николай", "Сергеевич");
        Coach coachWithTwoSessions = new Coach("Смолов", "Иван", "Федорович");
        Coach coachWithThreeSessions = new Coach("Иванов", "Петр", "Александрович");

        timetable.addNewTrainingSession(new TrainingSession(
                group, coachWithOneSession, DayOfWeek.MONDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(
                group, coachWithTwoSessions, DayOfWeek.MONDAY, new TimeOfDay(11, 0)));
        timetable.addNewTrainingSession(new TrainingSession(
                group, coachWithThreeSessions, DayOfWeek.MONDAY, new TimeOfDay(10, 0)));
        timetable.addNewTrainingSession(new TrainingSession(
                group, coachWithTwoSessions, DayOfWeek.TUESDAY, new TimeOfDay(11, 0)));
        timetable.addNewTrainingSession(new TrainingSession(
                group, coachWithThreeSessions, DayOfWeek.WEDNESDAY, new TimeOfDay(12, 0)));
        timetable.addNewTrainingSession(new TrainingSession(
                group, coachWithThreeSessions, DayOfWeek.FRIDAY, new TimeOfDay(15, 0)));

        Map<Coach, Long> countByCoaches = timetable.getCountByCoaches();

        assertEquals(Map.of(
                coachWithOneSession, 1L,
                coachWithTwoSessions, 2L,
                coachWithThreeSessions, 3L
        ), countByCoaches);

        assertIterableEquals(
                List.of(coachWithThreeSessions, coachWithTwoSessions, coachWithOneSession),
                countByCoaches.keySet()
        );
    }

    private List<TrainingSession> getTrainingSessionsList(Map<TimeOfDay, List<TrainingSession>> trainingSessionsList) {
        return trainingSessionsList.values().stream()
                .flatMap(List::stream)
                .toList();
    }
}
