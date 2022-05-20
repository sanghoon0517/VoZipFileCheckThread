package kr.co.daemon.test.jsh;

import java.io.File;

import org.zeroturnaround.zip.ZipUtil;

public class VoZipFileCheckThread extends Thread{
	
	private String path = "C:/dev/tempVOFile/";
	
	public void createFileListener() {
		File dir = new File(path);
		
		if(!dir.exists()) {
			dir.mkdirs();
		}
		
		File[] files = dir.listFiles();
		
		for(File zip : dir.listFiles(new ZipFileFilter())) {
			//Zip파일 압축풀기
			//경로에 압축풀면서 덮어씌우고 ZIP파일 삭제
			ZipUtil.unpack(zip, dir);
		}
	}
	
	public void createFile() {
		System.out.println("파일을 생성했습니다.");
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		
		//무한루프를 형성한다.
		//특정폴더 밑에 파일을 하나 만든다.
		//5초 뒤에 파일을 하나 더 만든다.
		//무한루프를 돌다가 10번 만들면 그만둔다.
		int cnt = 0;
		if(Thread.currentThread().isDaemon()) {
			while(true) {
				
				cnt++;
				createFile();
				System.out.println(cnt+"번");
				
				if(cnt == 10) {
					break;
				}
			}
		}
		
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//데몬스레드 실행
		VoZipFileCheckThread daemon = new VoZipFileCheckThread();
		
		daemon.setDaemon(true);
		
		daemon.start();
		
		try {
			Thread.sleep(10000);
			System.out.println("메인 스레드 종료");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
