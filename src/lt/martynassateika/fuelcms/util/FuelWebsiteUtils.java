package lt.martynassateika.fuelcms.util;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.jetbrains.php.refactoring.PhpNameUtil;
import java.net.URI;
import java.util.Collections;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;

/**
 * Utility methods related to the Fuel CMS website.
 *
 * @author martynas.sateika
 * @since 0.3.0
 */
public class FuelWebsiteUtils {

  private static final String DOCUMENTATION_BASE_PATH = "http://docs.getfuelcms.com/";

  /**
   * Returns the URL to the online documentation page corresponding to the open file.
   *
   * If {@code file} is not a FUEL documentation file, or if there's an under-score anywhere in the
   * relative path from the {@linkplain FuelCmsVfsUtils#getModuleDocumentationFolder(Project,
   * String) documentation folder} to {@code file}, an empty Optional is returned.
   *
   * @param project current project
   * @param file active file
   * @return the URL to the online documentation page corresponding to the open file
   */
  public static Optional<URI> getDocumentationUrl(Project project, @NotNull VirtualFile file) {
    return FuelCmsVfsUtils
        .getModuleDocumentationFolder(project, "fuel")
        .filter(docsFolder -> VfsUtil.isUnder(file, Collections.singleton(docsFolder)))
        .map(docsFolder -> VfsUtil.findRelativePath(docsFolder, file, '/'))
        .filter(relativePath -> !relativePath.startsWith("_"))
        .filter(relativePath -> !relativePath.contains("/_"))
        .map(PhpNameUtil::getNameWithoutExtension)
        .map(page -> DOCUMENTATION_BASE_PATH + page)
        .map(URI::create);
  }

}
