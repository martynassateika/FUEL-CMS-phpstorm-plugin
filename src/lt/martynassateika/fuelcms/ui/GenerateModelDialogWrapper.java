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

import com.google.common.base.Joiner;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.openapi.vfs.VirtualFile;
import com.jetbrains.php.config.PhpProjectConfigurationFacade;
import com.jetbrains.php.config.interpreters.PhpInterpreter;
import lt.martynassateika.fuelcms.generate.FuelCmsModel;
import lt.martynassateika.fuelcms.generate.GenerateTarget;
import lt.martynassateika.fuelcms.util.FuelCmsVfsUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.List;
import java.util.stream.Collectors;

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
        setTitle("FUEL CMS: Generate Model");

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
        advancedModule.addItem("none (use application folder)");
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
            return new ValidationInfo("Model name not specified.");
        }
        // Check PHP is installed
        PhpProjectConfigurationFacade facade = PhpProjectConfigurationFacade.getInstance(project);
        PhpInterpreter phpInterpreter = facade.getInterpreter();
        if (phpInterpreter == null) {
            return new ValidationInfo("PHP interpreter is not configured.");
        }
        return null;
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
