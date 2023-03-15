
import java.io.File;
import java.util.List;

public class ReadFolder{
    public static void loadFilesForFolder(final File folder, List<String> fileList){
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                loadFilesForFolder(fileEntry, fileList);
            } else {
                fileList.add( fileEntry.getParent() + File.separator + fileEntry.getName() );
            }
        }
    }
}