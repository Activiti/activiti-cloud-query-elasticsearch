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
import java.util.Optional;

//import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.model.events.ProcessRuntimeEvent;
import org.activiti.cloud.api.model.shared.events.CloudRuntimeEvent;
import org.activiti.cloud.api.process.model.events.CloudProcessCompletedEvent;
import org.activiti.cloud.services.query.app.repository.elastic.ProcessInstanceRepository;
import org.activiti.cloud.services.query.model.elastic.ProcessInstance;
import org.activiti.cloud.services.query.model.elastic.QueryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProcessCompletedEventHandler implements QueryEventHandler {

    private static Logger LOGGER = LoggerFactory.getLogger(ProcessCompletedEventHandler.class);

    private ProcessInstanceRepository processInstanceRepository;

    @Autowired
    public ProcessCompletedEventHandler(ProcessInstanceRepository processInstanceRepository) {
        this.processInstanceRepository = processInstanceRepository;
    }

    @Override
    public void handle(CloudRuntimeEvent<?, ?> event) {
        CloudProcessCompletedEvent completedEvent = (CloudProcessCompletedEvent) event;
        String processInstanceId = completedEvent.getEntity().getId();
        LOGGER.debug("Handling completed process Instance " + processInstanceId);
        Optional<ProcessInstance> findResult = processInstanceRepository.findById(processInstanceId);
        if (findResult.isPresent()) {
            ProcessInstance processInstance = findResult.get();
            processInstance.setStatus(ProcessInstance.ProcessInstanceStatus.COMPLETED);
            processInstance.setLastModified(new Date(completedEvent.getTimestamp()));
            processInstanceRepository.save(processInstance);
        } else {
            throw new QueryException("Unable to find process instance with the given id: " + processInstanceId);
        }
    }

    @Override
    public String getHandledEvent() {
        return ProcessRuntimeEvent.ProcessEvents.PROCESS_COMPLETED.name();
    }
}
