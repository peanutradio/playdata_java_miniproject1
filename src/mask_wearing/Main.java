package mask_wearing;

import org.opencv.core.Mat;
import org.opencv.videoio.VideoCapture;

public class Main {
	
	static {
		// java opencv 객체 로드
		System.loadLibrary("opencv_java470");
	}

	public static void main(String[] args) {
		//MyFrame 객체 생성
		MyFrame frame = new MyFrame();
		// new VideoCapture(0) : 카메라에서 화면을 캡쳐할 객체 생성.
		VideoCapture cap = new VideoCapture(0);
		
		// cap.isOpened() : 카메라를 사용 불가능한 경우 false가 리턴됨
		if (!cap.isOpened()) {
			//자바 프로그램 실행 종료
			System.exit(-1);
			
			// 카메라가 캡쳐한 이미지를 저장할 객체
			Mat image = new Mat();
			
			
			//Main loop
			while(true) {
				
				// 카메라가 캡쳐한 이미지를 image 변수에 저장.
				cap.read(image);
				// image.empty() : 카메라 정상동작하지 않아서 이미지 변수에 데이터가 없으면 true 리턴
				if (!image.empty()) {
					//System.out.println("카메라 준비 완료");
					//System.out.println("image= "+image);
					//MyFrame render 함수 호출해서 카메라 캡쳐 이미지 전송
					frame.render(image);
				} else {
					System.out.println("카메라 에러");
					break;
				} //end else
			} //end while
		} //end while
	} //end main
} //end class
