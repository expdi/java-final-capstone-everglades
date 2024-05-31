package com.expeditors.trackservice.repository.taskmanagement;

import com.expeditors.trackservice.repository.taskmanagement.implementation.TaskManagerImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ConcurrentLinkedQueue;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;

public class TaskManagerImplTest {

    private TaskManagerImpl taskManager;
    private ConcurrentLinkedQueue<Task> taskToPerform;
    private ConcurrentLinkedQueue<Task> completedTasks;

    @BeforeEach
    void init(){

        taskToPerform = new ConcurrentLinkedQueue<>();
        completedTasks = new ConcurrentLinkedQueue<>();

        taskManager = new TaskManagerImpl(
                taskToPerform,
                completedTasks);
    }

    @Test
    void processTasks_ReturnsTrue_WhenTasksAreProcessed(){

        taskManager.addTask(new TaskThatReturnsTrueWhenProcessIsCalled());

        assertTrue(taskManager.processTasks());
        assertThat(taskToPerform, hasSize(0));
        assertThat(completedTasks, hasSize(0));
    }

    @Test
    void processTasks_ReturnsFalse_WhenTasksWereNotProcessed(){

        taskManager.addTask(new TaskThatReturnsTrueWhenProcessIsCalled());
        taskManager.addTask(new TaskThatReturnsFalseWhenProcessIsCalled());

        assertFalse(taskManager.processTasks());
        assertThat(taskToPerform, hasSize(0));
        assertThat(completedTasks, hasSize(0));
    }

    @Test
    void processTasks_ThrowsTaskManagerRollbackException_WhenTasksWereNotProcessedAndRollbackFailedAsWell(){

        taskManager.addTask(new TaskThatThatReturnsFalseWhenRevertIsCalled());
        taskManager.addTask(new TaskThatReturnsFalseWhenProcessIsCalled());

        assertThrows(TaskManagerRollbackException.class,
                () -> taskManager.processTasks());

        assertThat(taskToPerform, hasSize(0));
        assertThat(completedTasks, hasSize(0));
    }
}

class TaskThatReturnsTrueWhenProcessIsCalled implements Task{

    @Override
    public boolean process() {
        return true;
    }

    @Override
    public boolean revert() {
        return true;
    }
}

class TaskThatReturnsFalseWhenProcessIsCalled implements Task{

    @Override
    public boolean process() {
        return false;
    }

    @Override
    public boolean revert() {
        return true;
    }
}

class TaskThatThatReturnsFalseWhenRevertIsCalled implements Task{

    @Override
    public boolean process() {
        return true;
    }

    @Override
    public boolean revert() {
        return false;
    }
}
