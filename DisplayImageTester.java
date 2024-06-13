/**
 * Purpose: Create objects and call timer
 * 
 * Owen Colley
 * 5/4/24
 * 
 */

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.io.File;
import java.util.Timer;
import java.util.Date;
import java.util.ArrayList;

public class DisplayImageTester {
    public static void main(String[] args) throws Exception {
        
        BufferedImage background = ImageIO.read(new File("images/TESTER GOATED.png")); // background
        BufferedImage player = ImageIO.read(new File("images/bee.png")); // player default
        
        BufferedImage falling = ImageIO.read(new File("images/beeWing.png")); // wings out
        
        ArrayList<BufferedImage> jumping = new ArrayList<BufferedImage>(); // jump cycle
        jumping.add(ImageIO.read(new File("images/beeJump.png")));
        jumping.add(ImageIO.read(new File("images/beeJump2.png")));
        
        ArrayList<BufferedImage> walking = new ArrayList<BufferedImage>(); // walk cycle
        walking.add(ImageIO.read(new File("images/beeLegs.png")));
        walking.add(ImageIO.read(new File("images/beeLegs2.png")));
        
        MyKeyListener listener = new MyKeyListener();
        DisplayImage displayImage = new DisplayImage(background, player, listener);
        Animation animator = new Animation(falling, jumping, walking, player, displayImage);

        //call main game loop
        MyTimerTask task = new MyTimerTask(displayImage, listener, animator);
        java.util.Timer timer = new java.util.Timer();
        timer.scheduleAtFixedRate(task, new Date(), task.getDelay());
    }
}