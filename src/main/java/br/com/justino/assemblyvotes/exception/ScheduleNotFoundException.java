package br.com.justino.assemblyvotes.exception;

public class ScheduleNotFoundException extends RuntimeException {
    public ScheduleNotFoundException(String messageError) {
        super(messageError);
    }
}