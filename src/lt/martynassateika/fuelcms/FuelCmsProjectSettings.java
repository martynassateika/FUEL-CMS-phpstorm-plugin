package lt.martynassateika.fuelcms;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.util.xmlb.XmlSerializerUtil;
import java.beans.Transient;
import java.nio.file.Paths;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Holds project-specific Fuel CMS settings.
 *
 * @author martynas.sateika
 * @since 0.4.0
 */
@State(name = "FuelCmsProjectSettings")
public class FuelCmsProjectSettings implements ProjectComponent,
    PersistentStateComponent<FuelCmsProjectSettings> {

  private String fuelFolderPath;

  private VirtualFile virtualFile;

  @Nullable
  @Override
  public FuelCmsProjectSettings getState() {
    return this;
  }

  @Override
  public void loadState(@NotNull FuelCmsProjectSettings state) {
    XmlSerializerUtil.copyBean(state, this);
  }

  @Nullable
  public String getFuelFolderPath() {
    return fuelFolderPath;
  }

  public void setFuelFolderPath(String fuelFolderPath) {
    this.fuelFolderPath = fuelFolderPath;
    updateFuelFolderVirtualFile(fuelFolderPath);
  }

  @Nullable
  public VirtualFile getFuelFolder() {
    return virtualFile;
  }

  @Transient
  private void updateFuelFolderVirtualFile(@Nullable String folderPath) {
    virtualFile = Optional.ofNullable(folderPath)
        .map(Paths::get)
        .map(path -> VfsUtil.findFile(path, true))
        .orElse(null);
  }

}
