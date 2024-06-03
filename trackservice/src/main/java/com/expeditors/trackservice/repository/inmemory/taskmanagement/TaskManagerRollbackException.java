package com.expeditors.trackservice.repository.inmemory.taskmanagement;

public class TaskManagerRollbackException extends RuntimeException {

    public TaskManagerRollbackException(String message) {
        super(message);
    }
}
