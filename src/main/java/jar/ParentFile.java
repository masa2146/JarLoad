package jar;

import java.io.File;
import java.net.URISyntaxException;

/**
 * @author fatih
 */
public class ParentFile {
    /**
     * Get parent file of the jar
     */
    public static String getParenFilePath() {
        File jar = null;
        boolean isJar = ParentFile.class.getResource("ParentFile.class").toString().split(":")[0].equals("jar");

        try {
            if (isJar)
                jar = new File(ParentFile.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile();
            else
                jar = new File(ParentFile.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile().getParentFile().getParentFile();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        assert jar != null;
        System.out.println("CONFIG PATH: " + jar.getPath());
        return jar.getPath();
    }
}