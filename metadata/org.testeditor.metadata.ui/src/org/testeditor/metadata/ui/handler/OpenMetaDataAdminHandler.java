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
package org.testeditor.metadata.ui.handler;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.services.IServiceConstants;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.swt.widgets.Shell;
import org.testeditor.core.model.teststructure.TestStructure;
import org.testeditor.core.services.interfaces.TestStructureService;
import org.testeditor.metadata.ui.parts.MetaDataAdminView;
import org.testeditor.ui.constants.TestEditorConstants;
import org.testeditor.ui.handlers.OpenTestLogHandler;
import org.testeditor.ui.parts.testExplorer.TestExplorer;

public class OpenMetaDataAdminHandler {

	@Inject
	private TestStructureService testStructureService;

	private static final Logger LOGGER = Logger.getLogger(OpenTestLogHandler.class);

	/**
	 * Executes the handler to show test log in a dialog.
	 * 
	 * @param partService
	 *            to get the TestCaseExplorer
	 */
	@Execute
	public void execute(EPartService partService) {

		TestExplorer testExplorer = (TestExplorer) partService.findPart(TestEditorConstants.TEST_EXPLORER_VIEW)
				.getObject();

		TestStructure selected = (TestStructure) testExplorer.getSelection().getFirstElement();

		String id = MetaDataAdminView.ID;
		MPart part = partService.findPart(id);
		if (part == null) {
			part = partService.createPart(id);
		}
		partService.showPart(part, PartState.ACTIVATE);
		MetaDataAdminView metaDataAdminView = (MetaDataAdminView) part.getObject();
		metaDataAdminView.setTestStructure(selected);
	}

	/**
	 * Check if this Handler is enabled on the selection. Only one Teststrucutre
	 * is valid as a selection.
	 * 
	 * @param shell
	 *            active shell.
	 * @param partService
	 *            to get the TestCaseExplorer
	 * 
	 * @return true if only one element is selected.
	 */

	@CanExecute
	public boolean canExecute(@Named(IServiceConstants.ACTIVE_SHELL) Shell shell, EPartService partService) {
		// TestExplorer testExplorer = (TestExplorer)
		// partService.findPart(TestEditorConstants.TEST_EXPLORER_VIEW)
		// .getObject();
		// TestStructure selected = (TestStructure)
		// testExplorer.getSelection().getFirstElement();
		// if (!(selected instanceof TestCase)) {
		// return false;
		// }
		// try {
		// return testStructureService.hasLogData(selected);
		// } catch (SystemException e) {
		// LOGGER.error("can't check the execution of openMetaDataAdminHandler",
		// e);
		// MessageDialog.openError(shell, "System-Exception",
		// e.getLocalizedMessage());
		// return false;
		// }
		return true;
	}

}
