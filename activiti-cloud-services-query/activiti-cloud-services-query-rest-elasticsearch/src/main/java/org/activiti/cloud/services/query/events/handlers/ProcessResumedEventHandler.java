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

import org.activiti.api.process.model.events.ProcessRuntimeEvent;
import org.activiti.cloud.api.model.shared.events.CloudRuntimeEvent;
import org.activiti.cloud.api.process.model.events.CloudProcessResumedEvent;
import org.activiti.cloud.services.query.app.repository.elastic.ProcessInstanceRepository;
import org.activiti.cloud.services.query.model.elastic.ProcessInstance;
import org.activiti.cloud.services.query.model.elastic.QueryException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProcessResumedEventHandler implements QueryEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessResumedEventHandler.class);

    private ProcessInstanceRepository processInstanceRepository;

    @Autowired
    public ProcessResumedEventHandler(ProcessInstanceRepository processInstanceRepository) {
        this.processInstanceRepository = processInstanceRepository;
    }

    @Override
    public void handle(CloudRuntimeEvent<?, ?> event) {
        CloudProcessResumedEvent processResumedEvent = (CloudProcessResumedEvent) event;
        String processInstanceId = processResumedEvent.getEntity().getId();
        LOGGER.debug("Handling resumed process Instance " + processInstanceId);
        Optional<ProcessInstance> findResult = processInstanceRepository.findById(processInstanceId);
        ProcessInstance processInstance = findResult.orElseThrow(
                () -> new QueryException("Unable to find process instance with the given id: " + processInstanceId));
        processInstance.setStatus(ProcessInstance.ProcessInstanceStatus.RUNNING);
        processInstance.setLastModified(new Date(processResumedEvent.getTimestamp()));
        processInstance.setProcessDefinitionKey(processResumedEvent.getEntity().getProcessDefinitionKey());
        processInstance.setInitiator(processResumedEvent.getEntity().getInitiator());
        processInstance.setStartDate(processResumedEvent.getEntity().getStartDate());
        processInstance.setBusinessKey(processResumedEvent.getEntity().getBusinessKey());
        processInstanceRepository.save(processInstance);
    }

    @Override
    public String getHandledEvent() {
        return ProcessRuntimeEvent.ProcessEvents.PROCESS_RESUMED.name();
    }
}
