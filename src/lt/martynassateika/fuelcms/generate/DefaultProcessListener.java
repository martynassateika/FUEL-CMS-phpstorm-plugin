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

package lt.martynassateika.fuelcms.generate;

import com.intellij.execution.process.ProcessEvent;
import com.intellij.execution.process.ProcessListener;
import com.intellij.execution.process.ProcessOutputTypes;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import lt.martynassateika.fuelcms.ui.PluginConstants;
import lt.martynassateika.fuelcms.util.FuelCmsVfsUtils;
import org.jetbrains.annotations.NotNull;

class DefaultProcessListener implements ProcessListener {

    private final Project project;
    private final GenerateTarget target;
    private final ToolWindow toolWindow;
    private final ConsoleView consoleView;

    DefaultProcessListener(@NotNull Project project, @NotNull GenerateTarget target) {
        this.project = project;
        this.target = target;
        this.toolWindow = ToolWindowManager.getInstance(project).getToolWindow("FUEL CMS Log");
        this.consoleView = ServiceManager.getService(project, ConsoleService.class)
                .getConsoleView();
    }

    private static Notification getGenerateNotification(@NotNull String notificationText,
                                                        @NotNull NotificationType notificationType) {
        return new Notification(
                PluginConstants.NOTIFICATION_GROUP,
                "FUEL CMS: Generate",
                notificationText,
                notificationType
        );
    }

    @Override
    public void startNotified(ProcessEvent processEvent) {
        // Clear existing output
        consoleView.clear();

        // Focus to display new output
        toolWindow.activate(() -> {
        }, true, true);
    }

    @Override
    public void processTerminated(ProcessEvent processEvent) {
        // Notify user
        Notification notification;
        if (processEvent.getExitCode() == 0) {
            notification = getGenerateNotification(
                    String.format("\"%s\" successfully created.", target.getName()),
                    NotificationType.INFORMATION
            );
        } else {
            notification = getGenerateNotification(
                    String.format("Could not generate \"%s\".", target.getName()),
                    NotificationType.ERROR
            );
        }
        Notifications.Bus.notify(notification, project);

        // Refresh modules view
        FuelCmsVfsUtils.getFuelFolder(project).ifPresent(vf -> vf.refresh(true, true));
        // TODO Open up new / existing file
    }

    @Override
    public void processWillTerminate(ProcessEvent processEvent, boolean b) {
    }

    @Override
    public void onTextAvailable(ProcessEvent processEvent, Key key) {
        if (ProcessOutputTypes.STDERR.equals(key)) {
            this.consoleView.print(processEvent.getText(), ConsoleViewContentType.ERROR_OUTPUT);
        } else if (ProcessOutputTypes.SYSTEM.equals(key)) {
            this.consoleView.print(processEvent.getText(), ConsoleViewContentType.SYSTEM_OUTPUT);
        } else { // STDOUT
            this.consoleView.print(processEvent.getText(), ConsoleViewContentType.NORMAL_OUTPUT);
        }
    }

}
