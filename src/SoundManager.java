import javax.sound.sampled.*;
import java.net.URL;

public class SoundManager {
    private static Clip backgroundClip;

    // 🔊 สำหรับเล่นเสียงสั้นๆ (กดปุ่ม, ทอยเต๋า)
    public static void playSFX(String fileName) {
        try {
            // ✅ แก้ไข: ใช้ชื่อไฟล์ที่ส่งมาตรงๆ (เช่น "S2.wav")
            URL url = SoundManager.class.getResource("/" + fileName);
            if (url != null) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
                Clip clip = AudioSystem.getClip();
                clip.open(audioIn);
                clip.start();
                System.out.println("✅ Playing SFX: " + fileName);
            } else {
                System.out.println("❌ หาไฟล์ SFX ไม่เจอ: " + fileName);
            }
        } catch (Exception e) {
            System.out.println("⚠️ Error playing SFX: " + fileName);
            e.printStackTrace();
        }
    }

    // 🎵 สำหรับเล่นเพลงพื้นหลังวนลูป
    public static void playBGM(String fileName) {
        try {
            if (backgroundClip != null && backgroundClip.isRunning()) return;

            // ✅ แก้ไข: ใช้ชื่อไฟล์ที่ส่งมาตรงๆ (เช่น "S1.wav")
            URL url = SoundManager.class.getResource("/" + fileName);
            if (url != null) {
                AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
                backgroundClip = AudioSystem.getClip();
                backgroundClip.open(audioIn);
                backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);
                backgroundClip.start();
                System.out.println("✅ Playing BGM: " + fileName);
            } else {
                System.out.println("❌ หาไฟล์ BGM ไม่เจอ: " + fileName);
            }
        } catch (Exception e) {
            System.out.println("⚠️ Error playing BGM: " + fileName);
        }
    }

    public static void stopBGM() {
        if (backgroundClip != null) {
            backgroundClip.stop();
            backgroundClip.close();
            backgroundClip = null;
        }
    }
}