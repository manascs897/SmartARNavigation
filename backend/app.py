from flask import Flask, request, jsonify
from ultralytics import YOLO
import cv2
import numpy as np
import base64

app = Flask(__name__)
model = YOLO("yolov8n.pt")

KNOWN_HEIGHT = 170
FOCAL_LENGTH = 400
ALERT_DISTANCE = 150  # cm


@app.route("/detect", methods=["POST"])
def detect():
    data = request.json
    image_data = base64.b64decode(data["image"])

    np_arr = np.frombuffer(image_data, np.uint8)
    frame = cv2.imdecode(np_arr, cv2.IMREAD_COLOR)

    results = model(frame)
    result = results[0]

    alerts = []

    for box in result.boxes:
        cls_id = int(box.cls)
        label = result.names[cls_id]
        x1, y1, x2, y2 = map(int, box.xyxy[0])

        h_pixel = y2 - y1
        distance = (KNOWN_HEIGHT * FOCAL_LENGTH) / h_pixel if h_pixel > 0 else 0

        if distance < ALERT_DISTANCE:
            alerts.append({
                "object": label,
                "distance": round(distance, 2)
            })

    return jsonify({
        "alerts": alerts
    })


if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000)
