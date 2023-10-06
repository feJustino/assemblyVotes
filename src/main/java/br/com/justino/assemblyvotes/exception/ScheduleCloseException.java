package br.com.justino.assemblyvotes.exception;

public class ScheduleCloseException extends RuntimeException {
    public ScheduleCloseException(String messageError) {
        super(messageError);
    }
}