import cv2

# Start camera
cam = cv2.VideoCapture(0)  # 0 = default laptop camera
ret, frame = cam.read()

if ret:
    cv2.imshow("Camera Test", frame)  # Show photo
    cv2.imwrite("intruder.jpg", frame)  # Save photo
    print("✅ Photo captured and saved as intruder.jpg")
    cv2.waitKey(2000)  # Show for 2 sec
else:
    print("❌ Camera not working!")

cam.release()
cv2.destroyAllWindows()
