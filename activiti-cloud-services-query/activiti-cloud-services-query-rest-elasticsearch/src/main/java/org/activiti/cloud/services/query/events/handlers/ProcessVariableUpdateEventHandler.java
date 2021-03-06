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

import org.activiti.cloud.services.query.model.elastic.Variable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProcessVariableUpdateEventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProcessVariableUpdateEventHandler.class);

    private VariableUpdater variableUpdater;

    @Autowired
    public ProcessVariableUpdateEventHandler(VariableUpdater variableUpdater) {
        this.variableUpdater = variableUpdater;
    }

    public void handle(Variable updatedVariableEntity) {
        LOGGER.debug("Handling process variable updated event: " + updatedVariableEntity.getName());
        variableUpdater.updateVariable(updatedVariableEntity);
    }

}
