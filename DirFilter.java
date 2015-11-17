import java.io.File;
import java.io.FileFilter;

public class DirFilter implements FileFilter{

	@Override
	public boolean accept(File file) {
		if (file.isDirectory() && file.canRead() && file.exists()) {
			return true;
		}
		else return false;
	}
	

}
