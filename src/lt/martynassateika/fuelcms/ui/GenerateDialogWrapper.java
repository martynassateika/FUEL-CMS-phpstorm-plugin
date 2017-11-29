/*
 * Copyright 2017 Martynas Sateika
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package lt.martynassateika.fuelcms.ui;

import com.intellij.execution.ExecutionException;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import lt.martynassateika.fuelcms.generate.FuelCli;
import lt.martynassateika.fuelcms.generate.GenerateTarget;
import org.jetbrains.annotations.Nullable;

/**
 * Extension of {@link DialogWrapper} with common functionality for all 'generate' dialogs.
 */
public abstract class GenerateDialogWrapper extends DialogWrapper {

    private static final Logger LOGGER = Logger.getInstance(GenerateDialogWrapper.class);

    protected final Project project;

    GenerateDialogWrapper(@Nullable Project project) {
        super(project);
        this.project = project;
    }

    /**
     * Sub-classes have access to UI elements and so
     * can construct instances of {@link GenerateTarget}.
     *
     * @return 'generate' command's target
     */
    abstract GenerateTarget getGenerateTarget();

    /**
     * Prevents the modal window from being closed if an error occurs during generation.
     */
    @Override
    protected void doOKAction() {
        if (project != null) {
            try {
                FuelCli.executeGenerateCommand(project, getGenerateTarget());
                super.doOKAction();
            } catch (ExecutionException e) {
                LOGGER.error("Could not execute 'generate' command", e);
                // TODO Notification
            }
        }

    }

}
