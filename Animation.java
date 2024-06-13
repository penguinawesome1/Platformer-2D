/**
 * Purpose: Animate player
 * 
 * Owen Colley
 * 5/4/24
 * 
 */

import java.awt.Graphics2D;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import java.awt.image.BufferedImage;
import java.awt.geom.AffineTransform;
import java.awt.*;
import java.util.ArrayList;

public class Animation {
    private BufferedImage falling;
    private ArrayList<BufferedImage> jumping;
    private ArrayList<BufferedImage> walking;
    private BufferedImage standing;
    private DisplayImage displayImage;
    private int currentFrame = 0;
    private boolean flipped;
    private AffineTransform transform = AffineTransform.getScaleInstance(-1, 1);
    private ImageIcon icon;
    private BufferedImage image;
    private int imageWidth;
    private Graphics2D g;
    private BufferedImage flippedImage;
    private int jumpingSize;
    private int walkingSize;
    
    public Animation(BufferedImage falling, ArrayList<BufferedImage> jumping, ArrayList<BufferedImage> walking, BufferedImage standing, DisplayImage displayImage) {
        this.falling = falling;
        this.jumping = jumping;
        this.walking = walking;
        this.standing = standing;
        this.displayImage = displayImage;
        jumpingSize = jumping.size();
        walkingSize = walking.size();
        image = displayImage.getPlayer();
    }
    
    public void setFlipped(boolean isFlipped) {
        if(flipped != isFlipped) {
            flipped = isFlipped;
            if(flipped)
                flipPlayer();
            else
                undoFlip();
        }
    }
    
    public void falling() {
        image = falling;
        if(flipped)
            flipPlayer();
        else
            displayImage.setPlayer(image);
    }
    
    public void jumping() {
        if(currentFrame >= jumpingSize)
            currentFrame = 0;
        image = jumping.get(currentFrame);
        currentFrame++;
        if(flipped)
            flipPlayer();
        else
            displayImage.setPlayer(image);
    }
    
    public void walking() {
        if(currentFrame >= walkingSize)
            currentFrame = 0;
        image = walking.get(currentFrame);
        currentFrame++;
        if(flipped)
            flipPlayer();
        else
            displayImage.setPlayer(image);
    }
    
    public void standing() {
        image = standing;
        if(flipped)
            flipPlayer();
        else
            displayImage.setPlayer(image);
    }
    
    public void flipPlayer() {
        imageWidth = image.getWidth();
        
        transform.translate(-imageWidth, 0);
        
        flippedImage = new BufferedImage(imageWidth, image.getHeight(), BufferedImage.TYPE_INT_ARGB);
        
        g = flippedImage.createGraphics();
        g.drawImage(image, transform, null);
        
        displayImage.setPlayer(flippedImage);
        transform.translate(imageWidth, 0);
    }
    
    public void undoFlip() {
        displayImage.setPlayer(image);
    }
    
}