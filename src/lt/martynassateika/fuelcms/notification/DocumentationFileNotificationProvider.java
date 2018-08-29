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

package lt.martynassateika.fuelcms.notification;

import com.intellij.openapi.fileEditor.FileEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.ui.EditorNotificationPanel;
import com.intellij.ui.EditorNotifications;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.util.Optional;
import lt.martynassateika.fuelcms.util.FuelCmsProjectUtils;
import lt.martynassateika.fuelcms.util.FuelWebsiteUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Provides notifications when viewing documentation files.
 *
 * @author martynas.sateika
 * @since 0.3.0
 */
public class DocumentationFileNotificationProvider extends
    EditorNotifications.Provider<EditorNotificationPanel> {

  @NotNull
  private static final Key<EditorNotificationPanel> KEY = Key
      .create("DocumentationFileNotificationProvider");

  private final Project project;

  /**
   * @param project current project
   */
  public DocumentationFileNotificationProvider(@NotNull Project project) {
    this.project = project;
  }

  @NotNull
  @Override
  public Key<EditorNotificationPanel> getKey() {
    return KEY;
  }

  @Nullable
  @Override
  public EditorNotificationPanel createNotificationPanel(@NotNull VirtualFile virtualFile,
      @NotNull FileEditor fileEditor) {
    Optional<URI> documentationPath = FuelWebsiteUtils.getDocumentationUrl(project, virtualFile);
    if (!documentationPath.isPresent()) {
      return null;
    }

    final EditorNotificationPanel panel = new EditorNotificationPanel();

    panel.setText("You are viewing a FUEL CMS documentation file");
    panel.createActionLabel("View online", () -> {
      Desktop desktop = Desktop.getDesktop();
      try {
        URI uri = documentationPath.get();
        desktop.browse(uri);
      } catch (IOException e) {
        e.printStackTrace();
      }
    });
    return panel;
  }

}
