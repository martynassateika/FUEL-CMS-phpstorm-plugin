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
import com.google.common.base.Splitter;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ValidationInfo;
import com.jetbrains.php.refactoring.PhpNameUtil;
import lt.martynassateika.fuelcms.generate.FuelCmsAdvancedModule;
import lt.martynassateika.fuelcms.generate.GenerateTarget;
import lt.martynassateika.fuelcms.util.FuelCmsVfsUtils;
import lt.martynassateika.fuelcms.util.Messages;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class GenerateAdvancedModuleDialogWrapper extends GenerateDialogWrapper {

    private JPanel contentPane;

    private JTextField advancedModuleName;

    private JLabel commandLabel;

    public GenerateAdvancedModuleDialogWrapper(@Nullable Project project) {
        super(project);
        init();

        // Custom setup
        setUpListeners();
        setTitle("FUEL CMS: Generate Advanced Module");

        // Defaults
        advancedModuleName.setText("example");
    }

    @Override
    GenerateTarget getGenerateTarget() {
        return new FuelCmsAdvancedModule(advancedModuleName.getText().trim());
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        return contentPane;
    }

    @Nullable
    @Override
    protected ValidationInfo doValidate() {
        String trimmedName = this.advancedModuleName.getText().trim();
        if (trimmedName.isEmpty()) {
            return new ValidationInfo("Advanced module name not specified.");
        }

        // The user can supply one or more advanced module names.
        // Check they're all valid PHP class names, and do not already exist.
        Iterable<String> split = Splitter.on(":").split(this.advancedModuleName.getText());
        for (String name : split) {
            if (!PhpNameUtil.isValidClassName(name)) {
                return new ValidationInfo(Messages.invalidClassName(name));
            }
            if (FuelCmsVfsUtils.advancedModuleExists(project, name)) {
                return new ValidationInfo(String.format(
                        "Advanced module '%s' already exists",
                        name
                ));
            }
        }

        return super.doValidate();
    }

    private void setUpListeners() {
        advancedModuleName.getDocument().addDocumentListener(
                (DelegatingDocumentListener) e -> uiUpdated());
    }

    private void uiUpdated() {
        String joined = Joiner.on(" ").join(getGenerateTarget().getCommand());
        commandLabel.setText("php " + joined);
    }

}
