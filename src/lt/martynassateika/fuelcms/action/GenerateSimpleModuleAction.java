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

package lt.martynassateika.fuelcms.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.ui.DialogWrapper;
import lt.martynassateika.fuelcms.ui.GenerateModelDialogWrapper;
import lt.martynassateika.fuelcms.ui.GenerateSimpleModuleDialogWrapper;
import lt.martynassateika.fuelcms.util.FuelCmsProjectUtils;

/**
 * Opens up a modal window for simple module creation.
 */
public class GenerateSimpleModuleAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        DialogWrapper dialog = new GenerateSimpleModuleDialogWrapper(anActionEvent.getProject());
        dialog.show();
    }

    @Override
    public void update(AnActionEvent e) {
        boolean isFuelCmsProject = FuelCmsProjectUtils.isFuelCmsProject(e.getProject());
        e.getPresentation().setEnabled(isFuelCmsProject);
    }

}
