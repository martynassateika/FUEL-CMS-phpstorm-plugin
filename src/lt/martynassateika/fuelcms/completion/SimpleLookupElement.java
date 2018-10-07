package lt.martynassateika.fuelcms.completion;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementPresentation;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.vfs.VirtualFile;
import com.jetbrains.php.refactoring.PhpNameUtil;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.NotNull;

class SimpleLookupElement extends LookupElement {

  private final VirtualFile file;

  private final String relativePath;

  private final String advancedModuleName;

  SimpleLookupElement(VirtualFile blocksFolder, VirtualFile file, String advancedModuleName) {
    this.file = file;
    this.relativePath = getRelativePathToBlockFile(blocksFolder);
    this.advancedModuleName = advancedModuleName;
  }

  private String getRelativePathToBlockFile(VirtualFile blocksFolder) {
    Path filePath = Paths.get(PhpNameUtil.getNameWithoutExtension(file.getPath()));
    Path blocksFolderPath = Paths.get(blocksFolder.getPath());
    Path relativePath = blocksFolderPath.relativize(filePath);
    return FilenameUtils.separatorsToUnix(relativePath.toString());
  }

  @NotNull
  @Override
  public String getLookupString() {
    return relativePath;
  }

  @Override
  public void renderElement(LookupElementPresentation presentation) {
    super.renderElement(presentation);
    presentation.setIcon(IconLoader.findIcon("/icons/php-icon.png"));
    presentation.setTypeText(advancedModuleName);
    presentation.setTypeGrayed(true);
  }

}