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

package lt.martynassateika.fuelcms.ui;

import com.google.common.base.Joiner;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.openapi.vfs.VirtualFile;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import lt.martynassateika.fuelcms.FuelCmsBundle;
import lt.martynassateika.fuelcms.generate.FuelCmsModel;
import lt.martynassateika.fuelcms.generate.GenerateTarget;
import lt.martynassateika.fuelcms.util.FuelCmsVfsUtils;
import org.jetbrains.annotations.Nullable;

public class GenerateModelDialogWrapper extends GenerateDialogWrapper {

  private JPanel contentPane;

  private JTextField modelName;

  private JComboBox<String> advancedModule;

  private JLabel commandLabel;

  public GenerateModelDialogWrapper(@Nullable Project project) {
    super(project);
    init();

    // Custom setup
    setUpListeners();
    populateAdvancedModules(project);
    setTitle(FuelCmsBundle.message("dialog.generate.model.title"));

    // Defaults
    modelName.setText("example");
  }

  @Override
  GenerateTarget getGenerateTarget() {
    String module = isAdvancedModuleSelected()
        ? advancedModule.getSelectedItem().toString()
        : null;
    return new FuelCmsModel(modelName.getText().trim(), module);
  }

  private boolean isAdvancedModuleSelected() {
    return advancedModule.getSelectedIndex() > 0;
  }

  private void populateAdvancedModules(Project project) {
    advancedModule.addItem(FuelCmsBundle.message(
        "dialog.generate.no.advanced.module.dropdown.item"));
    List<String> advancedModules = FuelCmsVfsUtils.getAdvancedModules(project)
        .stream()
        .map(VirtualFile::getName)
        .collect(Collectors.toList());
    advancedModules.forEach(advancedModule::addItem);
  }

  @Nullable
  @Override
  protected JComponent createCenterPanel() {
    return contentPane;
  }

  @Nullable
  @Override
  protected ValidationInfo doValidate() {
    if (this.modelName.getText().trim().isEmpty()) {
      return new ValidationInfo(FuelCmsBundle.message("validation.model.name.not.specified"));
    }

    return super.doValidate();
  }

  private void setUpListeners() {
    modelName.getDocument().addDocumentListener(
        (DelegatingDocumentListener) e -> uiUpdated());
    advancedModule.addActionListener(e -> uiUpdated());
  }

  private void uiUpdated() {
    String joined = Joiner.on(" ").join(getGenerateTarget().getCommand());
    commandLabel.setText("php " + joined);
  }

}
