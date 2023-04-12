package mask_wearing;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;
import java.io.*;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.JSONArray;
import java.util.ArrayList;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.BasicStroke;


// extends JFrame : Window 창을 만들기 위해서 JFrame을 상속 받음.
public class MyFrame extends JFrame {
	//카메라 이미지를 화면에 그리는 Panel 추
	private MyPanel panel;
	// 카메라 이미지를 저장할 변수.
	private Image videoImage = null;
	
	// 파이썬에서 Detect한 좌표 정보를 저장할 배열
	private ArrayList<double[]> detectResult = new ArrayList<double[]>();
	public MyFrame() {
		// 윈도우 종료 닫기 버튼 추가
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// 윈도우 창의 크기 설정
		setSize(480, 640);
		// 윈도우창을 화면에 보이도록 설정
		setVisible(true);
		//윈도우 제목 설정
		setTitle("Mask application");
		//윈도우창 크기 조정 불가
		setResizable(false);
	
	// 카메라 이미지를 윈도우 창에 그릴 객체 생성.
	panel = new MyPanel();
	// 가운데 정렬
	add(panel, BorderLayout.CENTER);
	
	//1초마다 actionPerformed 메서드를 시행하는 함수
	Timer timer = new Timer(1000, new ActionListener() {
		//1초마다 반복해서 이 부분이 실행됨.
		public void actionPerformed(ActionEvent ae) {
	try {
		// 카메라 이미지를 저장할 파일의 경로 및 파일명
		File outputfile = new File("frame.jpg");
		//파일 저장시 메모리를 비워줌
		ImageIO.setUseCache(false);
		//videoImage : 카메라 캡쳐된 이미지가 저장.
		//videoImag를 jpg 형식으로 frame.jpg파일로 저장.
		ImageIO.write((BufferedImage)videoImage, "jpg", outputfile);
		
		// 파이썬 파일의 경로
		String file_path = "/Users/chanhupark/eclipse-workspace/JavaProject01/python_file/yolo_python.py";
		//  가상환경 파이썬의 절대 경로(앞페이지에서 확인한 경로를  입력할 것.
		String python_path = "";
		// 파이썬 파일을 실행해서 결과를 자바로 리턴할 객체.
		ProcessBuilder pb = new ProcessBuilder(python_path, file_path);
		// 파이썬 파일 실행
		Process p = pb.start();
		//실행 결과를 리턴할 객체 생성.
		BufferedReader bfr = new BufferedReader(new InputStreamReader(p.getInputStream()));
		System.out.println("....start  first detect process.....");
		
		// 기존의 detect 좌표 삭제
		detectResult.clear();
		
		//파이썬에서 전송한 데이터에서 일부분을 꺼낼 객체
		JSONParser parser = new JSONParser();
		//파이썬 파일의 실행 결과를 한줄씩 저장할 변수.
		String line = "";
		while((line = bfr.readLine()) !=null) {
			System.out.println("Python Output : " + line);
			//파이썬에서 전송한 detect 정보 line에서 일부 데이터를 꺼낼 준비를 함.
			Object obj = parser.parse(line);
			// 파이썬에서 전송한 데이터를 자바의 JSONArray 객체로 생성.
			JSONArray arr = (JSONArray) obj;
			//파이썬에서 전송한 데이터의 개수만큼 반복
			for (int i=0; i<arr.size(); i++) {
				//파이썬에서 전송한 i번째 객체 리턴
				JSONObject jsonObj = (JSONObject) arr.get(i);
				// 파이썬에서 전송한 left값 리턴
				long left = (Long) jsonObj.get("left");
				System.out.println("left = "+ left);
				// 파이썬에서 전송한 top값 리턴 
				long top = (Long) jsonObj.get("top");
				System.out.println("top = "+ top);
				// 파이썬에서 전송한 right값 리턴 
				long right = (Long) jsonObj.get("right");
				System.out.println("right = "+ right);
				// 파이썬에서 전송한 bottom값 리턴.
				long bottom = (Long) jsonObj.get("bottom");
				System.out.println("bottom = "+ bottom);
				// 파이썬에서 전송한 cls 값 리턴.
				long cls = (Long) jsonObj.get("cls");
				System.out.println("cls = "+ cls);
				// 파이썬에서 전송한 conf값 리턴.
				double conf = (double) jsonObj.get("conf");
				System.out.println("conf = "+ conf);
				
				//파이썬에서 detect 한 정보를 배열에 저장.
				double [] detect = {left, top, right, bottom, cls, conf};
				// 배열을 detectResult에 추가
				detectResult.add(detect);
				System.out.println("detectResult="+detectResult);
				System.out.println("=============================");
			}	
		}
		// 파이썬 프로그램과 자바프로그램의 연결 종료.
		p.destroy();
		System.out.println("....end  first detect process.....");	
	}catch(Exception e) {
		//오류 방샐시 발생된 이유를 출력
		e.printStackTrace();
	}

}

// 카메라 이미지를 읽어 왔을 때 Main.java에서 호출하는 함수.
// 매개변수 image :  카메라 이미지 저장.
public void render(Mat image) {
	// 카메라 이미지(opencv Mat객체)를 화면에 그릴 수 있는 Image 객체로 변환해서 리턴
	videoImage= toBufferedImage(image);
	//videoImage MyPanel에 설정
	panel.setImage(videoImage);
	// videoImage를 화면에 그림 -> MyPanel 클래스의 paintComponent() 실행.
	panel.repaint();
	// 화면에 사각형 글씨등을 그릴 객체 생성
	Graphics2D graphic = ((BufferedImage)videoImage).createGraphics();
	//선색깔 설정.
	graphic.setColor(Color.RED);
	//detectResult.size() : detectResult에 저장된 데이터의 
	for (int i=0; i<detectResult.size(); i++) {
		// detectionResult에 저장된 i번째 데이터 꺼내기
		// double 타입의 배열로 { 사각형 x1,  사각형 x2, 사각형 y2, 클래서, 확률} 순으로 저장되어 있음.
		double [] detect = detectResult.get(i);
		// 화면에 사각형을 그림.
		graphic.drawRect((int)detect[0],(int)detect[1], (int)detect[2], (int)detect[3]);
		// 클래스 리턴
		int cls = (int) detect[4];
		System.out.println("cls=" + cls);
		// 클래스가 0 (mask)
		if (cls== 0) {
			//mask 글씨 출력 
			graphic.drawString("mask", (int)detect[0],(int)detect[1]);
		}else {
			// no mask 글씨 출력
			graphic.drawString("no mask", (int)detect[0],(int)detect[1]);
		}
		//  화면에 사각형 글씨 출력 종료.
		graphic.dispose();		
	}
	
}
//openCV 객체인 mat를 화면에 그릴 수 있는 Image 객체로 변환해서 리턴하는 함수.
//매개변수 mat : 카메라가 캡쳐한 이미지가 저장된 OpenCV 객체.
public Image toBufferedImage(Mat mat) {
	//Mat 객체를 jpg 형식의 이미지로 변환 한 결과를 저장할 객체.
	MatOfByte matOfByte = new MatOfByte();
	//Imgcodecs.imencode( 변환형식, OpenCV객체, 변환한 결과 저장할 객채ㅔ)
	// matf를 jpg 형식의 이미지로 변환해서 matOfByte에 저장.
	Imgcodecs.imencode(".jpg", mat, matOfByte);
	//matOfByte에 저장된 카메라 이미지를 byte배열 byteArray에 저장.
	byte[] byteArray = matOfByte.toArray();
	//byteArray배열에 저장된 이미지의 내용을 읽어서 Image 객체로 변환할 객체
	InputStream in = new ByteArrayInputStream(byteArray);
	BufferedImage bufImage = null;
	try {
		// byteAttay의 내용을 읽어서 Image 객체로 변환.
		bufImage = ImageIO.read(in);
	} catch (Exception e) {
		e.printStackTrace();
	}
  }
	});
	// 1초마다 반복 실행 시작.
	timer.start();
}
	// 카메라 이미지를 읽어 왔을 때, Main.java에서 호출되는 함수.
	// 매개변수 image : 카메라 이미지 저장.
	public void render(Mat image) {
		// 카메라 이미지 (opencv Mat객체)를 화면에 그릴 수 있는 Image 객체로 변환해서 리턴.
		videoImage = toBufferedImage(image);
		//videoImage MyPanel에 설정.

	}
}
