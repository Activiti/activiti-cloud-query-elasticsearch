/*
 * Copyright 2017 Alfresco, Inc. and/or its affiliates.
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

package org.activiti.cloud.services.query;

import org.activiti.cloud.services.api.model.Task;

public class CoreTaskBuilder {

    private Task task;

    private CoreTaskBuilder() {
        task = new Task();
    }

    public static CoreTaskBuilder aTask() {
        return new CoreTaskBuilder();
    }

    public CoreTaskBuilder withId(String id) {
        task.setId(id);
        return this;
    }

    public CoreTaskBuilder withName(String name) {
        task.setName(name);
        return this;
    }

    public Task build() {
        return task;
    }


}