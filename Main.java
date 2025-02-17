import javax.sound.sampled.*;
import javax.swing.JButton;
import javax.swing.JFrame;
import java.io.File;

public class Main {
    private static Clip clip;
    static boolean isPlaying = false;
        
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(450, 650);
        frame.setVisible(true);
        frame.setLayout(null);

        
        JButton playButton = new JButton("PLAY");
        playButton.setBounds(100, 100, 100, 20);
        frame.add(playButton);

        JButton pauseButton = new JButton("PAUSE");
        pauseButton.setBounds(150, 150, 100, 20);
        frame.add(pauseButton);

        try {

            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("music.wav"));
            clip = AudioSystem.getClip();
            
            clip.open(audioInputStream);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        playButton.addActionListener(e -> {
           if(clip!=null && !isPlaying) {
               clip.start();
               isPlaying = true;
           }
        });



        pauseButton.addActionListener(e -> {
            if(clip!=null && isPlaying) {
                clip.stop();
                isPlaying = false;
            }
        });


    }
}

