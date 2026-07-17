package com.agendajava.backend.model.rooms;

public class ICURoom extends Room {
    private List<Calendar> bedsCalendars; // Lista com os calendários de cada leito
    private int bedNumber;                // Quantidade de leitos

    public ICURoom(String name, int bedNumber) {
        super(name);
        this.bedNumber = bedNumber;
        this.bedCalendars = new ArrayList<Calendar>(bedNumber);
        for (int i = 0; i < bedNumber; i++)
            bedsCalendars.get(i) = new Calendar();
    }

    public boolean hasBedsAvailable(LocalDateTime startDateTime, Duration duration) {
        for (int i = 0; i < bedNumber; i++) {
            bedCalendar = bedsCalendars.get(i)
            if (bedIsAvailable(bedCalendar, startDateTime, duration))
                return true;
        }
        return false;
    }

    public void bedSchedule(LocalDateTime startDateTime, Duration duration, Procedure procedure) {
        LocalDate date = startDateTime.toLocalDate();
        LocalTime startTime = startDateTime.toLocalTime();
        Calendar bedCalendar;

        // Obs: esse método só será utilizado depois de hasBedsAvailable(), o que garante que sempre haverá pelo menos 1 leito disponível!
        for (int i = 0; i < bedNumber; i++) {
            bedCalendar = bedsCalendars.get(i)
            if (bedIsAvailable(bedCalendar, startDateTime, duration))
                break; 
        }

        bedCalendar.computeIfAbsent(date, d -> new TreeMap<>());
        TreeMap<LocalTime, Procedure> daymap = bedCalendar.get(date);

        if (!isAvailable(startDateTime, duration)) {
            throw new SchedulingConflict ( 
                "Horário indisponível!"
            );
        } 

        daymap.put(startTime, procedure);
    }

    public void addBed() {
        bedsCalendars.add(new Calendar());
        bedNumber++;
    }

    public void removeBed() {
        bedsCalendars.remove(bedNumber - 1);
        bedNumber--;
    }

    private boolean bedIsAvailable(Calendar bedCalendar, LocalDateTime startDateTime, Duration duration) {
        LocalDate date = startDateTime.toLocalDate();
        LocalTime startTime = startDateTime.toLocalTime();

        bedCalendar.computeIfAbsent(date, d -> new TreeMap<>());
        TreeMap<LocalTime, Procedure> daymap = bedCalendar.get(date);

        if (daymap.isEmpty()) // Não existe nenhum procedimento agendado nesse dia!
            return true;

        LocalTime priorProcedureStartTime = daymap.floorKey(startTime); // Horário de início do procedimento que começa antes do novo
        if (priorProcedureStartTime != null && daymap.get(priorProcedureStartTime).overlapsWith(startDateTime, duration))
            return false;

        LocalTime nextProcedureStartTime = daymap.ceilingKey(startTime); // Horário de início do procedimento que começa depois do novo
        if (nextProcedureStartTime != null && daymap.get(nextProcedureStartTime).overlapsWith(startDateTime, duration))
            return false;

        return true;
    }

    public int getBedNumber() {
        return bedNumber;
    }
}