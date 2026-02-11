from ultralytics import YOLO

# Load model once (important for performance)
model = YOLO("yolov8n.pt")

# Calibration constants
KNOWN_HEIGHT = 170  # cm (average human height)
FOCAL_LENGTH = 400
ALERT_DISTANCE = 150  # cm threshold


def detect_objects(frame):
    results = model(frame, verbose=False)
    result = results[0]

    alerts = []
    predictions = []

    for box in result.boxes:
        cls_id = int(box.cls)
        conf = float(box.conf)
        label = result.names[cls_id]

        x1, y1, x2, y2 = map(int, box.xyxy[0])

        # Distance estimation
        h_pixel = y2 - y1
        distance = (KNOWN_HEIGHT * FOCAL_LENGTH) / h_pixel if h_pixel > 0 else 0

        # Store full prediction
        predictions.append({
            "label": label,
            "confidence": round(conf, 2),
            "distance_cm": round(distance, 2),
            "bbox": [x1, y1, x2, y2]
        })

        # Alert condition
        if distance < ALERT_DISTANCE:
            alerts.append({
                "object": label,
                "distance_cm": round(distance, 2)
            })

    return alerts, predictions
