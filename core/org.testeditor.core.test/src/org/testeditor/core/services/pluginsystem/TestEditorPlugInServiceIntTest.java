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
package org.testeditor.core.services.pluginsystem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.eclipse.e4.core.services.translation.TranslationService;
import org.junit.BeforeClass;
import org.junit.Test;
import org.osgi.framework.FrameworkUtil;
import org.testeditor.core.model.action.ProjectLibraryConfig;
import org.testeditor.core.model.teststructure.LibraryLoadingStatus;
import org.testeditor.core.services.interfaces.FieldMappingExtension;
import org.testeditor.core.services.interfaces.ServiceLookUpForTest;
import org.testeditor.core.services.plugins.LibraryConfigurationServicePlugIn;
import org.testeditor.core.services.plugins.TestEditorPlugInService;

/**
 * 
 * Integrationtests for the TestEditorPlugInServiceImpl.
 * 
 */
public class TestEditorPlugInServiceIntTest {

	/**
	 * Registers a MockService.
	 */
	@BeforeClass
	public static void prepareTestsystem() {
		FrameworkUtil.getBundle(TestEditorPlugInServiceIntTest.class).getBundleContext()
				.registerService(LibraryConfigurationServicePlugIn.class, getLibConfigMock(), null);
	}

	/**
	 * Test the correct lookup for a service to extract properties from a
	 * config.
	 * 
	 * @throws Exception
	 *             for test.
	 */
	@Test
	public void testGetAsProperties() throws Exception {
		TestEditorPlugInService service = ServiceLookUpForTest.getService(TestEditorPlugInService.class);
		ProjectLibraryConfig config = getProjectLibraryConfigMock();
		LibraryConfigurationServicePlugIn cfgService = service.getLibraryConfigurationServiceFor(config.getId());
		Map<String, String> properties = cfgService.getAsProperties(config);
		assertEquals(properties.get(TestEditorPlugInService.LIBRARY_ID), config.getId());
	}

	/**
	 * Test Lookup for LibraryConfigurationService.
	 * 
	 * @throws Exception
	 *             for test
	 */
	@Test
	public void testGetLibraryConfigurationServiceFor() throws Exception {
		TestEditorPlugInService service = ServiceLookUpForTest.getService(TestEditorPlugInService.class);
		ProjectLibraryConfig config = getProjectLibraryConfigMock();
		LibraryConfigurationServicePlugIn libraryConfigurationService = service
				.getLibraryConfigurationServiceFor(config.getId());
		assertNotNull("Plug-In System has a LibraryConfiguration", libraryConfigurationService);
		assertEquals("Has the same ID", config.getId(),
				((LibraryConfigurationServicePlugIn) libraryConfigurationService).getId());
	}

	/**
	 * 
	 * @return Mock of the ProjectConfig
	 */
	private static ProjectLibraryConfig getProjectLibraryConfigMock() {
		return new ProjectLibraryConfig() {

			@Override
			public String getId() {
				return "testMock";
			}

			@Override
			public LibraryLoadingStatus getLibraryLoadingStatus() {
				return null;
			}

			@Override
			public void setLibraryLoadingStatus(LibraryLoadingStatus libraryLoadingStatus) {
			}

			@Override
			public ProjectLibraryConfig copyConfigurationToDestination(String nameOfTamplateProject, String destination) {
				return null;
			}

			@Override
			public void renameConfigurationToDestination(String oldName, String newName) {
			}
		};

	}

	/**
	 * 
	 * @return Mock for Test
	 */
	private static LibraryConfigurationServicePlugIn getLibConfigMock() {
		return new LibraryConfigurationServicePlugIn() {

			@Override
			public String getId() {
				return "testMock";
			}

			@Override
			public Map<String, String> getAsProperties(ProjectLibraryConfig projectLibraryConfig) {
				HashMap<String, String> result = new HashMap<String, String>();
				result.put(TestEditorPlugInService.LIBRARY_ID, getId());
				return result;
			}

			@Override
			public ProjectLibraryConfig createProjectLibraryConfigFrom(Properties properties) {
				return getProjectLibraryConfigMock();
			}

			@Override
			public String getTranslatedHumanReadableLibraryPlugInName(TranslationService service) {
				return null;
			}

			@Override
			public List<FieldMappingExtension> getConfigUIExtensions() {
				return null;
			}

			@Override
			public ProjectLibraryConfig createEmptyProjectLibraryConfig() {
				return null;
			}

			@Override
			public String getTemplateForConfiguration() {
				return null;
			}
		};
	}
}
