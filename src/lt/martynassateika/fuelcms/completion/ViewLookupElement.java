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

/**
 * Lookup element for FUEL view files.
 *
 * @author martynas.sateika
 */
class ViewLookupElement extends LookupElement {

  private final String relativePath;

  private final String advancedModuleName;

  /**
   * @param viewsFolder a folder with view files
   * @param file a file inside {@code viewsFolder}
   * @param advancedModuleName advanced module name the view is in (can be 'application')
   */
  ViewLookupElement(VirtualFile viewsFolder, VirtualFile file, String advancedModuleName) {
    this.relativePath = getRelativePathToViewFile(viewsFolder, file);
    this.advancedModuleName = advancedModuleName;
  }

  /**
   * @param viewsFolder a folder with view files
   * @param file a file inside {@code viewsFolder}
   * @return relative path between {@code viewsFolder} and {@code file}
   */
  private static String getRelativePathToViewFile(VirtualFile viewsFolder, VirtualFile file) {
    Path filePath = Paths.get(PhpNameUtil.getNameWithoutExtension(file.getPath()));
    Path blocksFolderPath = Paths.get(viewsFolder.getPath());
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
