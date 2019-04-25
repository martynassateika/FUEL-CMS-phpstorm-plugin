/*
 * Copyright 2018 Martynas Sateika
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
import lt.martynassateika.fuelcms.util.FuelCmsProjectUtils;
import org.jetbrains.annotations.NotNull;

/**
 * Opens up a modal window for model creation.
 */
public class GenerateModelAction extends AnAction {

  @Override
  public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
    DialogWrapper dialog = new GenerateModelDialogWrapper(anActionEvent.getProject());
    dialog.show();
  }

  @Override
  public void update(@NotNull AnActionEvent e) {
    boolean isFuelCmsProject = FuelCmsProjectUtils.isFuelCmsProject(e.getProject());
    e.getPresentation().setEnabled(isFuelCmsProject);
  }

}
