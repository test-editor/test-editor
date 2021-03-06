/*******************************************************************************
 * Copyright (c) 2012 - 2015 Signal Iduna Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Signal Iduna Corporation - initial API and implementation
 * akquinet AG
 *******************************************************************************/
package org.testeditor.core.jobs;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.junit.Test;
import org.osgi.service.event.EventHandler;
import org.testeditor.core.exceptions.SystemException;
import org.testeditor.core.model.teststructure.TestProject;
import org.testeditor.core.model.teststructure.TestProjectConfig;
import org.testeditor.core.services.dispatcher.TeamShareServiceDispatcher;
import org.testeditor.core.services.impl.FileWatchServiceImpl;
import org.testeditor.core.services.impl.TestProjectServiceImpl;
import org.testeditor.core.services.interfaces.TeamShareService;
import org.testeditor.core.services.interfaces.TestProjectService;
import org.testeditor.core.util.TestStateProtocolService;

/**
 * Modul tests for TeamModificationCheckJob.
 *
 */
public class TeamModificationCheckJobTest {

	/**
	 * Tests the team check on team shared testprojects.
	 * 
	 * @throws Exception
	 *             on test failure.
	 */
	@Test
	public void testProjectSelectionWithTeamOnly() throws Exception {
		IEclipseContext context = EclipseContextFactory.create();
		final Set<TestProject> monitor = new HashSet<TestProject>();
		TestProjectServiceImpl projectServiceImpl = new TestProjectServiceImpl() {
			@Override
			public List<TestProject> getProjects() {
				List<TestProject> result = new ArrayList<TestProject>();
				TestProject tp = new TestProject();
				tp.setTestProjectConfig(new TestProjectConfig());
				result.add(tp);
				tp = new TestProject();
				tp.setTestProjectConfig(new TestProjectConfig() {
					@Override
					public boolean isTeamSharedProject() {
						return true;
					}
				});
				result.add(tp);
				return result;
			}
		};
		projectServiceImpl.bind(new FileWatchServiceImpl());
		context.set(TestProjectService.class, projectServiceImpl);
		context.set(TeamShareService.class, new TeamShareServiceDispatcher() {
			@Override
			public int availableUpdatesCount(TestProject testProject) throws SystemException {
				monitor.add(testProject);
				return 0;
			}
		});
		context.set(TestStateProtocolService.class, new TestStateProtocolService());
		context.set(IEventBroker.class, getEventBroker());
		TeamModificationCheckJob checkJob = ContextInjectionFactory.make(TeamModificationCheckJob.class, context);
		checkJob.checkForModifications();
		assertEquals(1, monitor.size());
	}

	/**
	 * Tests the team check on team shared testprojects.
	 * 
	 * @throws Exception
	 *             on test failure.
	 */
	@Test
	public void testModificationInfoSetToTestStateProtocol() throws Exception {
		IEclipseContext context = EclipseContextFactory.create();
		final TestProject tp = new TestProject();
		tp.setTestProjectConfig(new TestProjectConfig() {
			@Override
			public boolean isTeamSharedProject() {
				return true;
			}
		});
		TestProjectServiceImpl projectServiceImpl = new TestProjectServiceImpl() {
			@Override
			public List<TestProject> getProjects() {
				List<TestProject> result = new ArrayList<TestProject>();
				result.add(tp);
				return result;
			}
		};
		projectServiceImpl.bind(new FileWatchServiceImpl());
		context.set(TestProjectService.class, projectServiceImpl);
		context.set(TeamShareService.class, new TeamShareServiceDispatcher() {
			@Override
			public int availableUpdatesCount(TestProject testProject) throws SystemException {
				return 10;
			}
		});
		TestStateProtocolService protocolService = new TestStateProtocolService();
		context.set(TestStateProtocolService.class, protocolService);
		context.set(IEventBroker.class, getEventBroker());
		TeamModificationCheckJob checkJob = ContextInjectionFactory.make(TeamModificationCheckJob.class, context);
		checkJob.checkForModifications();
		assertEquals(10, protocolService.getAvailableUpdatesFor(tp));
	}

	/**
	 * 
	 * @return eventborker mock.
	 */
	private IEventBroker getEventBroker() {
		return new IEventBroker() {

			@Override
			public boolean unsubscribe(EventHandler eventHandler) {
				return false;
			}

			@Override
			public boolean subscribe(String topic, String filter, EventHandler eventHandler, boolean headless) {
				return false;
			}

			@Override
			public boolean subscribe(String topic, EventHandler eventHandler) {
				return false;
			}

			@Override
			public boolean send(String topic, Object data) {
				return false;
			}

			@Override
			public boolean post(String topic, Object data) {
				return false;
			}
		};
	}

}
