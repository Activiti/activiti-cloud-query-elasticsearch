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

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import org.activiti.cloud.services.query.model.elastic.Variable;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.ArgumentMatchers.eq;

public class ProcessVariableUpdateEventHandlerTest {

	@InjectMocks
	private ProcessVariableUpdateEventHandler handler;

	@Mock
	private VariableUpdater variableUpdater;

	@Before
	public void setUp() {
		initMocks(this);
	}

	@Test
	public void handleShouldUpdateVariable() {
		// given
		Variable variableEntity = new Variable();
		variableEntity.setName("var");
		variableEntity.setValue("v1");
		variableEntity.setProcessInstanceId("10");

		// when
		handler.handle(variableEntity);

		// then
		verify(variableUpdater).updateVariable(eq(variableEntity));
	}

}