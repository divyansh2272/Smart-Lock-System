import tkinter as tk
from tkinter import messagebox, simpledialog
from PIL import Image, ImageTk
import cv2
import smtplib
import ssl
from email.message import EmailMessage
import os
import pygame
from datetime import datetime
import threading
import time

# ========== CONFIG ==========
SENDER_EMAIL = "smartlocksystem7@gmail.com"
APP_PASSWORD = "boetutabfctkwxsy"
RECEIVER_EMAIL = "riyanshpatel20@gmail.com"
ALARM_FILE = "alarm.mp3"
PASSWORD = "1234"
SELFIE_FOLDER = "selfies"  # folder to save selfies
# ============================

attempts = 0
intruder_image = None
lockout_active = False
lockout_timer = None
log_file = "access_log.txt"

# ======== Photo Capture Function ========
def capture_intruder():
    if not os.path.exists(SELFIE_FOLDER):
        os.makedirs(SELFIE_FOLDER)

    cam = cv2.VideoCapture(0)
    if not cam.isOpened():
        print(" Camera not accessible!")
        return None

    ret, frame = cam.read()
    if ret:
        timestamp = datetime.now().strftime("%Y%m%d_%H%M%S")
        filename = os.path.join(SELFIE_FOLDER, f"intruder_{timestamp}.jpg")
        cv2.imwrite(filename, frame)
        print(f" Intruder image saved at: {filename}")
        cam.release()
        return filename
    else:
        print(" Failed to capture image!")
        cam.release()
        return None

# ======== Email Function ========
def send_email_with_photo(image_path):
    try:
        msg = EmailMessage()
        msg["Subject"] = " SmartLock Alert: Multiple Wrong Attempts"
        msg["From"] = SENDER_EMAIL
        msg["To"] = RECEIVER_EMAIL
        msg.set_content(
            "Warning! Someone is trying to access your system.\nSee the attached photo."
        )

        with open(image_path, "rb") as f:
            file_data = f.read()
            file_name = os.path.basename(image_path)

        msg.add_attachment(
            file_data,
            maintype="image",
            subtype="jpeg",
            filename=file_name
        )

        context = ssl.create_default_context()
        with smtplib.SMTP_SSL("smtp.gmail.com", 465, context=context) as server:
            server.login(SENDER_EMAIL, APP_PASSWORD)
            server.send_message(msg)
        print(" Email sent successfully!")

    except Exception as e:
        print(" Email send failed:", e)

# ======== Logging Function ========
def log_attempt(success, entered_password):
    """
    Ab sirf FAILED attempts ko log kar rahe hain.
    SUCCESS attempts ke liye kuch bhi log nahi hoga.
    """
    if success:
        return  # success toh skip kar diya

    timestamp = datetime.now().strftime("%Y-%m-%d %H:%M:%S")
    with open(log_file, "a") as f:
        # Yaha hum exact wrong password store nahi kar rahe (security ke liye)
        f.write(f"{timestamp} - FAILED ATTEMPT (wrong password entered)\n")

# ======== Lockout Timer Function ========
def lockout_timer_func():
    global lockout_active, attempts
    time.sleep(30)  # 30 seconds lockout
    lockout_active = False
    attempts = 0  # reset attempts after lockout
    unlock_button.config(state="normal")
    messagebox.showinfo("SmartLock", "Lockout period ended. You can try again.")

# ======== Change Password Function ========
def change_password():
    global PASSWORD
    current = simpledialog.askstring(
        "Change Password",
        "Enter current password:",
        show="*"
    )
    if current != PASSWORD:
        messagebox.showerror("SmartLock", "Incorrect current password!")
        return

    new_pass = simpledialog.askstring(
        "Change Password",
        "Enter new password:",
        show="*"
    )
    confirm = simpledialog.askstring(
        "Change Password",
        "Confirm new password:",
        show="*"
    )

    if new_pass == confirm and new_pass:
        PASSWORD = new_pass
        messagebox.showinfo("SmartLock", "Password changed successfully!")
    else:
        messagebox.showerror("SmartLock", "Passwords do not match or empty!")

# ======== View Logs Function ========
def view_logs():
    if os.path.exists(log_file):
        with open(log_file, "r") as f:
            logs = f.read()
        log_window = tk.Toplevel(root)
        log_window.title("Access Logs")
        log_window.geometry("500x400")
        text = tk.Text(log_window, wrap=tk.WORD)
        text.insert(tk.END, logs)
        text.pack(expand=True, fill=tk.BOTH)
        scrollbar = tk.Scrollbar(log_window, command=text.yview)
        text.config(yscrollcommand=scrollbar.set)
        scrollbar.pack(side=tk.RIGHT, fill=tk.Y)
    else:
        messagebox.showinfo("SmartLock", "No logs available.")

# ======== Stop Alarm Function ========
def stop_alarm():
    try:
        if pygame.mixer.get_init():
            pygame.mixer.music.stop()
    except Exception as e:
        print(" Error stopping alarm:", e)
    messagebox.showinfo("SmartLock", "Alarm stopped.")

# ======== Password Check Function ========
def check_password():
    global attempts, intruder_image, lockout_active, lockout_timer

    if lockout_active:
        messagebox.showerror("SmartLock", "System is locked out. Please wait.")
        return

    entered_password = password_entry.get()
    password_entry.delete(0, tk.END)  # clear entry

    if entered_password == PASSWORD:
        # SUCCESS attempt ko log nahi kar rahe
        messagebox.showinfo("SmartLock", " Access Granted!")
        root.destroy()
    else:
        attempts += 1
        # sirf failed attempt log hogi
        log_attempt(False, entered_password)
        messagebox.showerror("SmartLock", f" Wrong Password! Attempt {attempts}")

        if attempts == 1:
            intruder_image = capture_intruder()
        elif attempts == 2:
            if intruder_image and os.path.exists(intruder_image):
                send_email_with_photo(intruder_image)
            else:
                print(" No intruder image found to send!")
        elif attempts == 3:
            lockout_active = True
            unlock_button.config(state="disabled")
            lockout_timer = threading.Thread(target=lockout_timer_func)
            lockout_timer.start()
            if os.path.exists(ALARM_FILE):
                pygame.mixer.init()
                pygame.mixer.music.load(ALARM_FILE)
                pygame.mixer.music.play(-1)  # loop continuously
                print(" Alarm triggered!")
            else:
                print(" Alarm file not found!")

# ======== GUI Setup ========
root = tk.Tk()
root.title("Smart Lock")
root.geometry("400x500")
root.resizable(False, False)

# ---- Background Image ----
try:
    bg_img = Image.open("background.jpg")  # optional background image
    bg_img = bg_img.resize((400, 500))
    bg_photo = ImageTk.PhotoImage(bg_img)

    bg_label = tk.Label(root, image=bg_photo)
    bg_label.image = bg_photo
    bg_label.place(x=0, y=0, relwidth=1, relheight=1)
except:
    print(" background.jpg not found, using plain background")
    root.configure(bg="#1e1e1e")

# ---- Heading ----
heading = tk.Label(
    root,
    text=" Smart Lock System",
    font=("Arial", 16, "bold"),
    fg="white",
    bg="#1e1e1e"
)
heading.pack(pady=20)

# ---- Lock Icon ----
try:
    lock_img = Image.open("lock.png")
    lock_img = lock_img.resize((100, 100))
    lock_photo = ImageTk.PhotoImage(lock_img)

    lock_label = tk.Label(root, image=lock_photo, bg="#1e1e1e")
    lock_label.image = lock_photo
    lock_label.pack(pady=10)
except:
    print(" lock.png not found, skipping image")

# ---- Password Label ----
password_label = tk.Label(
    root,
    text="Enter Password:",
    font=("Arial", 12),
    fg="white",
    bg="#1e1e1e"
)
password_label.pack(pady=10)

# ---- Password Entry ----
password_entry = tk.Entry(root, show="*", font=("Arial", 14), justify="center")
password_entry.pack(pady=5)

# ---- Unlock Button ----
unlock_button = tk.Button(
    root,
    text="Unlock",
    font=("Arial", 12, "bold"),
    bg="#4CAF50",
    fg="white",
    width=15,
    command=check_password
)
unlock_button.pack(pady=20)

# ---- Additional Buttons ----
button_frame = tk.Frame(root, bg="#1e1e1e")
button_frame.pack(pady=10)

change_pass_button = tk.Button(
    button_frame,
    text="Change Password",
    font=("Arial", 10),
    bg="#2196F3",
    fg="white",
    width=12,
    command=change_password
)
change_pass_button.pack(side=tk.LEFT, padx=5)

view_logs_button = tk.Button(
    button_frame,
    text="View Logs",
    font=("Arial", 10),
    bg="#FF9800",
    fg="white",
    width=12,
    command=view_logs
)
view_logs_button.pack(side=tk.LEFT, padx=5)

stop_alarm_button = tk.Button(
    button_frame,
    text="Stop Alarm",
    font=("Arial", 10),
    bg="#F44336",
    fg="white",
    width=12,
    command=stop_alarm
)
stop_alarm_button.pack(side=tk.LEFT, padx=5)

# ---- Run GUI ----
root.mainloop()