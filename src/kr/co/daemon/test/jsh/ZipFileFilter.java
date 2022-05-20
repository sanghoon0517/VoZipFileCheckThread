package kr.co.daemon.test.jsh;

import java.io.File;
import java.io.FilenameFilter;

public class ZipFileFilter implements FilenameFilter{
	@Override
	public boolean accept(File dir, String name) {
		// TODO Auto-generated method stub
		boolean isZip = false;
		
		if(name.toLowerCase().endsWith(".zip") == true) {
			isZip = true;
		} else {
			isZip = false;
		}
		
		return isZip;
	}
}
