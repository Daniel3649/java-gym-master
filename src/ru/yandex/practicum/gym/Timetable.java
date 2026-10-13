package ru.yandex.practicum.gym;

import java.util.*;

public class Timetable {
    private final Map<DayOfWeek, Map<TimeOfDay, List<TrainingSession>>> timetable;

    public Timetable() {
        timetable = new EnumMap<>(DayOfWeek.class);

        for (DayOfWeek day : DayOfWeek.values()) {
            timetable.put(day, new TreeMap<>());
        }
    }

    public void addNewTrainingSession(TrainingSession trainingSession) {
        DayOfWeek dayOfWeekAddedTraining = trainingSession.getDayOfWeek();
        TimeOfDay timeOfDayAddedTraining = trainingSession.getTimeOfDay();

        Map<TimeOfDay, List<TrainingSession>> trainingsByDayOfWeek = timetable.get(dayOfWeekAddedTraining);
        trainingsByDayOfWeek
                .computeIfAbsent(timeOfDayAddedTraining, trainings -> new ArrayList<>())
                .add(trainingSession);
    }

    public Map<TimeOfDay, List<TrainingSession>> getTrainingSessionsForDay(DayOfWeek dayOfWeek) {
        return timetable.get(dayOfWeek);
    }

    public List<TrainingSession> getTrainingSessionsForDayAndTime(DayOfWeek dayOfWeek, TimeOfDay timeOfDay) {
        return timetable.get(dayOfWeek).getOrDefault(timeOfDay, new ArrayList<>());
    }
}
