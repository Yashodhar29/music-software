// import javax.management.relation.RoleNotFoundException;
import javax.sound.sampled.*;
import javax.swing.*;


import java.awt.*;
import java.io.File;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
// import java.util.concurrent.ExecutorCompletionService;
// import java.util.concurrent.Flow;
import java.awt.Dimension;

class RoundedButton extends JButton {
    public RoundedButton(ImageIcon icon) {
        // super(text);
        super();
        
        Image scaledImage = icon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(scaledImage);
        setPreferredSize(new Dimension(40, 40)); // Set smaller size

        setOpaque(false); 
        setIcon(resizedIcon);
        setFocusPainted(false);
        setContentAreaFilled(false);
        setBorderPainted(false);
        setHorizontalTextPosition(SwingConstants.CENTER);
        setVerticalTextPosition(SwingConstants.BOTTOM);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(Color.white);
        g2.fillRoundRect(30/2, 30/2, getWidth()-30, getHeight()-30, 30, 30); 
        

        g2.dispose();
        super.paintComponent(g); 
    }
}

class ImagePanel extends JPanel {
    private Image backgroundImage;
    private Connection connection; 


    public ImagePanel(String imagePath) {
        super();
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydb", "root", "Yashodhar#123");
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM pathtesting where id = 1;");

            if(rs.next()) {
                imagePath = rs.getString("image_path");
            }
            else {
                System.out.println("Data not fetcehd");
            }
            
            rs.close();
            connection.close();


        }catch(Exception e) {
            System.out.println("Some error occured");
        }

        backgroundImage = new ImageIcon(imagePath).getImage();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this); // Draw image to fill panel
    }
}


public class Main {
    private static Clip clip;
    static boolean isPlaying = false;

    public static void main(String[] args) {
        // Create JFrame



        JFrame frame = new JFrame("Music Player");
        frame.setSize(480, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout()); // Ensure proper layout
        frame.setResizable(false);

        // Buttons
        RoundedButton playButton = new RoundedButton(new ImageIcon("./assets/play.png"));
        playButton.setPreferredSize(new Dimension(100, 100));
        RoundedButton nextButton = new RoundedButton(new ImageIcon("./assets/next.png"));
        nextButton.setPreferredSize(new Dimension(100, 100));
        RoundedButton previousButton = new RoundedButton(new ImageIcon("./assets/previous.png"));
        previousButton.setPreferredSize(new Dimension(100, 100));

        // Options Panel (Bottom Panel)
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10)); 
        optionsPanel.setBackground(new Color(178, 108, 108));
        optionsPanel.setPreferredSize(new Dimension(frame.getWidth(), 110)); 
        optionsPanel.add(previousButton);
        optionsPanel.add(playButton);
        optionsPanel.add(nextButton);

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT)); 
        topPanel.setPreferredSize(new Dimension(frame.getWidth(), 50));
        RoundedButton menuButton = new RoundedButton(new ImageIcon("./assets/menu.png"));
        menuButton.setBackground(Color.WHITE);
        // menuButton.setPreferredSize(new Dimension(80, 100));
        menuButton.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2, true));

        JPanel centerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 50));
        // centerPanel.setPreferredSize(new Dimension(40, 40));
        centerPanel.setBackground(Color.RED);

        ImagePanel imagePanel = new ImagePanel("./assets/image.png");
        imagePanel.setPreferredSize(new Dimension(300, 300));
        imagePanel.setBackground(Color.BLACK);

        centerPanel.add(imagePanel);



        topPanel.add(menuButton); 
        frame.add(topPanel, BorderLayout.NORTH); 
        frame.add(centerPanel, BorderLayout.CENTER); 
        frame.add(optionsPanel, BorderLayout.SOUTH);
        
        frame.setVisible(true); 
        
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("music.wav"));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Define icons as instance variables so they can be referenced properly
        ImageIcon playIcon = new ImageIcon(new ImageIcon("./assets/play.png").getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));
        ImageIcon pauseIcon = new ImageIcon(new ImageIcon("./assets/pause.png").getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH));

        playButton.setIcon(playIcon); // Set initial icon
        // long currentPosition = 0;
        
        playButton.addActionListener(e -> {

            if (clip != null) {
                if (!isPlaying) {
                    // clip.setMicrosecondPosition(currentPosition); // Resume from last position
                    clip.start();
                    isPlaying = true;
                    playButton.setIcon(pauseIcon); // Change icon to pause
                } else {
                    // currentPosition = clip.getMicrosecondPosition(); // Store position before pausing
                    clip.stop();
                    isPlaying = false;
                    playButton.setIcon(playIcon); // Change icon back to play
                }
            }
        });

    }
}
