import cv2
from ultralytics import YOLO

#카메라 이미지가 저ㅇㅚㄴ 배ㄹ
file_path = 'frame.jpg'
# 카메라 아미지를 읽어서 img 에 저장.
img = cv2.imread(file_path)
# yolo 모델 생성
model = YOLO('./python_file/best.pt')
#이미지를 RGB 타입으로 변환하고 Detect결과 리턴
results = model(cv2.cvtColor(img, cv2.COLOR_BGR2RGB))
# detect 결과를 저장할 배열
box_result = []
# detect 결과 하나를 r에 저장.
for r in results:
    #yolo 박스 정보 저장.
    boxes = r.boxes
    for box in boxes:
        #box 좌표 저장
        left, top, right, bottom = box,xyxy[0]
        #클래스 타입 저장.
        cls = int(box.cls)
        # 확률 저장.
        conf = box.conf
        # 확률이 50%  이상이면
        if conf> 0.5:
            # yolo detect 결과 저장.
            box_result.append({
                "left": int(left), "top": int(top), "right": int(right), 
                "bottom": int(bottom), "cls": int(cls), "conf": float(conf)})

#yolo 결과 출력
print(box_result)  
