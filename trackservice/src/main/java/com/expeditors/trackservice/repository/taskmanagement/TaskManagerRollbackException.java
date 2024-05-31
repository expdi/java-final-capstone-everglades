package com.expeditors.trackservice.repository.taskmanagement;

public class TaskManagerRollbackException extends RuntimeException {

    public TaskManagerRollbackException(String message) {
        super(message);
    }
}
