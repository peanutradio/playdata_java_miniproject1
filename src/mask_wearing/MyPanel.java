package mask_wearing;

import java.awt.Graphics;
import javax.swing.JPanel;
import java.awt.Image;


public class MyPanel extends JPanel {
	// 카메라 이미지를 저장할 속성
	private Image cameraImage;
	
	//카메라 이미지를 송석 cameraImage에 저장하는 메서드.
	public void setImage(Image img) {
		// 카페라 이미지가 저장된 매개변수 img를 속성 cameraImage에 저장.
		this.cameraImage = img;
	}
	
	// 화면에 이미지 카메라이미지 텍스트를 출력하는 메서드
	public void paintComponent(Graphics g) {
		// g.drawString(화면에 출력할 텍스트, 텍스트 x 좌표, 텍스트 y 좌표);
		// 화면에 텍스트 출력
		//g.drawString("여기에 카메라 이미지를 출력할 겁니다.", 100, 200);
		
		// 카메라 이미지를 화면에 출력하는 메서드.
		// drawImage(화면에 출력할 이미지, x좌표, y좌표, null)
		g.drawImage(cameraImage, 0, 0, null);
		
	}
}
