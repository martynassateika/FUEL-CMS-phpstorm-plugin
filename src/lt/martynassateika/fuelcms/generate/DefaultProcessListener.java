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
import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import lt.martynassateika.fuelcms.util.FuelCmsVfsUtils;
import org.jetbrains.annotations.NotNull;

class DefaultProcessListener implements ProcessListener {

    private final Project project;

    DefaultProcessListener(@NotNull Project project) {
        this.project = project;
    }

    @Override
    public void startNotified(ProcessEvent processEvent) {
    }

    @Override
    public void processTerminated(ProcessEvent processEvent) {
        // Notify user
        Notification notification = new Notification("fuelcmsplugin", null, "Module created.", NotificationType.INFORMATION);
        Notifications.Bus.notify(notification, project);

        // Refresh modules view
        FuelCmsVfsUtils.getFuelFolder(project).ifPresent(vf -> vf.refresh(true, true));
    }

    @Override
    public void processWillTerminate(ProcessEvent processEvent, boolean b) {
    }

    @Override
    public void onTextAvailable(ProcessEvent processEvent, Key key) {
    }

}
