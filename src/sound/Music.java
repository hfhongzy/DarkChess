package sound;
import buttonComponent.GameButton;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.File;

public class Music {
    public static Clip clip;
    public static GameButton musicButton;
    public static void flipMusic() {
      if(musicButton.getName().equals("关闭音乐")) {
        stop();
        musicButton.setName("打开音乐");
      } else {
        start();
        musicButton.setName("关闭音乐");
      }
    }
    public static void start() {
      System.out.println(System.getProperty("user.dir"));
      try {
        File musicPath = new File("DarkChess/src/bgm.wav");
        if(musicPath.exists()) {
          AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
          clip = AudioSystem.getClip();
          clip.open(audioInput);
          FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
          gainControl.setValue(-30.0f);
          clip.start();
          clip.loop(Clip.LOOP_CONTINUOUSLY);
        } else {
          System.out.println("Music Error!");
        }
      } catch(Exception e) {
        e.printStackTrace();
      }
    }
    public static void stop() {
      clip.stop();
    }
}
