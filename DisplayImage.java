/**
 * Purpose: Adjust and display images
 * 
 * Owen Colley
 * 5/4/24
 * 
 */

import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.*;
import javax.swing.*;
import javax.swing.JLayeredPane;
import java.util.*;

public class DisplayImage {
    private BufferedImage background;
    private BufferedImage player;
    private MyKeyListener listener;
    private JLabel label1;
    private JLabel label2;
    private JPanel panel1;
    private JLayeredPane layeredPane = new JLayeredPane();
    private JFrame frame = new JFrame();
    private ImageIcon currentIcon;
    private int playerLocX;
    private int playerLocY;
    public static final int WIDTH = 1000; //adjust for width of screen
    public static final int HEIGHT = 500; //adjust for height of screen
    
    public DisplayImage(BufferedImage background, BufferedImage player, MyKeyListener listener) {
        this.background = background;
        this.player = player;
        this.listener = listener;
        
        playerLocX = WIDTH/2 - player.getWidth()/2; //left of player in middle
        playerLocY = HEIGHT/2 - player.getHeight()/2; //top of player in middle
        
        label1 = new JLabel(new ImageIcon(background));
        label1.setBounds(0, 0, background.getWidth(), background.getHeight());
        
        label2 = new JLabel(new ImageIcon(player));
        label2.setBounds(playerLocX, playerLocY, player.getWidth(), player.getHeight());
        
        panel1 = new JPanel();
        panel1.addKeyListener(listener);
        
        layeredPane = new JLayeredPane();
        layeredPane.add(label1, JLayeredPane.DEFAULT_LAYER); //First layer image
        layeredPane.add(label2, JLayeredPane.PALETTE_LAYER); //Second layer player
        layeredPane.add(panel1, JLayeredPane.MODAL_LAYER); //Third layer listener
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(layeredPane);
        frame.setSize(background.getWidth(), background.getHeight());
        frame.setVisible(true);
        panel1.requestFocusInWindow();
        
        currentIcon = new ImageIcon(player);
    }
    
    public int getPlayerLocX() {
        return playerLocX;
    }
    
    public void setPlayerLocX(int x) {
        playerLocX = x;
    }
    
    public int getPlayerLocY() {
        return playerLocY;
    }
    
    public void setlayerLocY(int y) {
        playerLocY = y;
    }
    
    public BufferedImage getBackground() {
        return background;
    }
    
    public void setBackground(BufferedImage image) {
        label1.setIcon(new ImageIcon(image));
    }
    
    public BufferedImage getPlayer() {
        return player;
    }
    
    public void setPlayer(BufferedImage image) {
        if(currentIcon.getImage() != image) {
            player = image;
            currentIcon = new ImageIcon(image);
        }
        label2.setIcon(currentIcon);
    }
    
    public void moveBackground(int startX, int startY) {
        label1.setBounds(startX, startY, background.getWidth(), background.getHeight());
    }
    
    public void movePlayer(int startX, int startY) {
        label2.setBounds(startX, startY, player.getWidth(), player.getHeight());
    }
    
    /*public int checkPixel(int x, int y) {
        return layeredPane.getComponentAt(x, y).getRGB(x, y);
    }*/
    
}