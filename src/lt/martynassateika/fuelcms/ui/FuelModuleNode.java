package lt.martynassateika.fuelcms.ui;

import com.intellij.ide.projectView.PresentationData;
import com.intellij.ide.projectView.ViewSettings;
import com.intellij.ide.projectView.impl.nodes.PsiDirectoryNode;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiDirectory;
import com.intellij.ui.SimpleTextAttributes;
import icons.Icons;
import lt.martynassateika.fuelcms.util.FuelModuleUtils;
import org.jetbrains.annotations.NotNull;

/**
 * PSI node representing a FUEL CMS module.
 *
 * @author martynas.sateika
 * @since 0.4.0
 */
public class FuelModuleNode extends PsiDirectoryNode {

  private final PsiDirectory directory;

  FuelModuleNode(@NotNull Project project, PsiDirectory directory,
      ViewSettings viewSettings) {
    super(project, directory, viewSettings);
    this.directory = directory;
  }

  @Override
  protected void updateImpl(@NotNull PresentationData data) {
    data.clearText();
    data.setIcon(Icons.FUEL_CMS_ICON);
    data.addText(directory.getName(), SimpleTextAttributes.REGULAR_ATTRIBUTES);

    // Add version number, if present
    FuelModuleUtils
        .getAdvancedModuleVersion(myProject, directory.getName())
        .map(version -> " " + version)
        .ifPresent(version -> data.addText(version, SimpleTextAttributes.GRAYED_SMALL_ATTRIBUTES));
  }

}
