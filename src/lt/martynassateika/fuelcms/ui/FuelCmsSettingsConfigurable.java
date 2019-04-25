package lt.martynassateika.fuelcms.ui;

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.util.Comparing;
import com.intellij.openapi.util.io.FileUtil;
import javax.swing.JComponent;
import javax.swing.JPanel;
import lt.martynassateika.fuelcms.FuelCmsBundle;
import lt.martynassateika.fuelcms.FuelCmsProjectSettings;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nls.Capitalization;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Holds components of the project settings window.
 *
 * @author martynas.sateika
 * @since 0.4.0
 */
public class FuelCmsSettingsConfigurable implements SearchableConfigurable {

  @NotNull
  private final Project project;

  @NotNull
  private FuelCmsProjectSettings settings;

  private FuelCmsConfigurablePanel panel;

  public FuelCmsSettingsConfigurable(@NotNull Project project,
      @NotNull FuelCmsProjectSettings settings) {
    this.project = project;
    this.settings = settings;
  }

  @Nls(capitalization = Capitalization.Title)
  @Override
  public String getDisplayName() {
    return "FUEL CMS";
  }

  @NotNull
  @Override
  public String getHelpTopic() {
    return "preferences.FuelCms";
  }

  @Nullable
  @Override
  public JComponent createComponent() {
    panel = new FuelCmsConfigurablePanel(project);
    return panel.myWholePanel;
  }

  @Override
  public boolean isModified() {
    return panel.isModified(settings);
  }

  @Override
  public void apply() {
    panel.apply(settings);
  }

  @Override
  public void reset() {
    panel.reset(settings);
  }

  @Override
  public void disposeUIResources() {
    panel = null;
  }

  @NotNull
  @Override
  public String getId() {
    return getHelpTopic();
  }

  @Nullable
  @Override
  public Runnable enableSearch(String s) {
    return null; // default method overridden for 2016.1 support
  }

  private static FileChooserDescriptor createSceneBuilderDescriptor() {
    return FileChooserDescriptorFactory
        .createSingleFolderDescriptor()
        .withTitle(FuelCmsBundle.message("settings.title"))
        .withDescription(FuelCmsBundle.message("settings.fuelFolderPathField.description"));
  }

  public static class FuelCmsConfigurablePanel {

    private TextFieldWithBrowseButton fuelFolderPathField;

    private JPanel myWholePanel;

    public FuelCmsConfigurablePanel(Project project) {
      final FileChooserDescriptor descriptor = createSceneBuilderDescriptor();
      fuelFolderPathField
          .addBrowseFolderListener(descriptor.getTitle(), descriptor.getDescription(), project,
              descriptor);
    }

    private void reset(FuelCmsProjectSettings settings) {
      final String fuelFolderPath = settings.getFuelFolderPath();
      if (fuelFolderPath != null) {
        fuelFolderPathField.setText(FileUtil.toSystemDependentName(fuelFolderPath));
      }
    }

    private void apply(FuelCmsProjectSettings settings) {
      settings
          .setFuelFolderPath(
              FileUtil.toSystemIndependentName(fuelFolderPathField.getText().trim()));
    }

    private boolean isModified(FuelCmsProjectSettings settings) {
      final String fuelFolderPath = settings.getFuelFolderPath();
      return !Comparing
          .strEqual(FileUtil.toSystemIndependentName(fuelFolderPathField.getText().trim()),
              fuelFolderPath != null ? fuelFolderPath.trim() : null);
    }
  }

}
