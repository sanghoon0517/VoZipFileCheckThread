package kr.co.daemon.test.jsh;

import java.awt.Toolkit;
import java.awt.TrayIcon.MessageType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import org.zeroturnaround.zip.ZipUtil;

/**
 * @author 전상훈
 * 
 * 데몬 스레드를 이용하여 특정 폴더에 특정확장자를 가진 파일이 생기면(zip 파일) 작업을 수행하고 해당 파일을 지우는 작업을 해보자.
 * 
 * 해당 프로그램을 데몬스레드로 만든 이유는?
 * 
 * 데몬 스레드는 일반 스레드와는 다르게 우선 순위가 가장 낮기 때문에 메인 스레드가 종료되면 데몬 스레드도 같이 종료되어 버린다.
 * 즉 해당 프로그램의 목적은 프로그램이 종료되었을 시 스레드 작업도 같이 종료시켜버리기 위함이다.
 * 일반 스레드는 메인 스레드가 종료된다고 해서 종료되는 것이 아니라 작업을 끝마치고 종료된다.
 *
 */
public class VoZipFileCheckThread extends Thread{
	
//	private static String path = "C:/dev/tempVOFile/";
	private String path = "C:/DGBC/iStudio/workspace/DAON/src-biz/com/dgbc/daon/cc/ex/core/";
	
	private static void threadSleep(long millsec) {
		try {
			Thread.sleep(millsec);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static void unzipAndDelete(File zip, File dir) {
		System.out.println("unzip 실행");
		threadSleep(600);
		ZipUtil.unpack(zip, dir);
		zip.delete();
		System.out.println("zip 삭제");
		threadSleep(600);
	}
	
	private static void mkrdirsIfNotExists(File dir) {
		if(!dir.exists()) {
			dir.mkdirs();
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		File dir = new File(path);
		ZipFileFilter zipFilter = new ZipFileFilter();
		
		mkrdirsIfNotExists(dir);
		
		if(Thread.currentThread().isDaemon()) {
			System.out.println("VoZipFileChecker가 데몬스레드로 실행 중입니다.");
			while(true) {
				File[] zipFileList = dir.listFiles(zipFilter);
				int zipCnt = zipFileList.length; 
				threadSleep(800);
				while(zipCnt > 0) {
					try {
						threadSleep(800);
						for(File zip : dir.listFiles(new ZipFileFilter())) {
							//Zip파일 압축풀기
							unzipAndDelete(zip, dir);
						}
					} catch(Exception e) {
						System.err.println("예외 발생으로 나가기 처리");
						threadSleep(800);
						return;
					}
					threadSleep(800);
				}
			}
		}
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		TrayIconHandler.registerTrayIcon(
				Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("blue_icon.png")),
				"VoZipFileChecker 실행 중입니다",
				new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						
					}
				}
			);
		
		TrayIconHandler.addItem("Exit", new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("프로그램을 종료합니다.");
				threadSleep(1000);
				System.out.println("메인 스레드 종료");
				System.exit(0);
			}
		});
		
		TrayIconHandler.displayMessage("VoZipFileChecker", "the program is runnung!", MessageType.INFO);
		
		VoZipFileCheckThread daemon = new VoZipFileCheckThread();
		daemon.setDaemon(true);
		daemon.start();
		
		while(true) {threadSleep(800);}

	}

}
