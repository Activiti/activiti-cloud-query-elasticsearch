/*
 * Copyright 2018 Alfresco, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.activiti.cloud.services.query.events.handlers;

import java.util.Date;

import org.activiti.api.task.model.events.TaskRuntimeEvent;
import org.activiti.cloud.api.model.shared.events.CloudRuntimeEvent;
import org.activiti.cloud.api.task.model.events.CloudTaskCancelledEvent;
import org.activiti.cloud.services.query.app.repository.elastic.TaskRepository;
import org.activiti.cloud.services.query.model.elastic.QueryException;
import org.activiti.cloud.services.query.model.elastic.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaskCancelledEventHandler implements QueryEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(TaskCancelledEventHandler.class);

    private final TaskRepository taskRepository;

    @Autowired
    public TaskCancelledEventHandler(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public void handle(CloudRuntimeEvent<?, ?> event) {
        CloudTaskCancelledEvent taskCancelledEvent = (CloudTaskCancelledEvent) event;
        org.activiti.api.task.model.Task eventTask = taskCancelledEvent.getEntity();
        LOGGER.debug("Handling cancelled task Instance " + eventTask.getId());
        updateTaskStatus(
                taskRepository.findById(eventTask.getId())
                        .orElseThrow(() -> new QueryException("Unable to find task with id: " + eventTask.getId())),
                taskCancelledEvent.getTimestamp());
    }

    private void updateTaskStatus(Task taskEntity, Long eventTimestamp) {
        taskEntity.setStatus(Task.TaskStatus.CANCELLED);
        taskEntity.setLastModified(new Date(eventTimestamp));
        taskRepository.save(taskEntity);
    }

    @Override
    public String getHandledEvent() {
        return TaskRuntimeEvent.TaskEvents.TASK_CANCELLED.name();
    }
}
