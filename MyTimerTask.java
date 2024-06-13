/**
 * Purpose: Main Game Loop
 * 
 * Owen Colley
 * 5/4/24
 * 
 * 
 * adjust step to only move as high as needed
 * slide down walls slower
 * don't make it so u float then when on walls
 * add platforms
 * make sure u not sided or grounded early
 * make jumps consistent hieights no matter fps
 * fix trystep so it starts where u left off, don't recalc try move, also so u don't glitch thru ceiling
 * make it so u cant go up diagonally backward slope, it sees cx is changing and works but shouldnt
 * 
 * 
 * machine learning?
 * use sprite sheet on next project?
 * next game procedurally generated dungeon game
 * make rpg? inside dungeon game maybe
 */

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.*;
import javax.swing.*;
import javax.swing.JLayeredPane;
import java.util.*;
import java.awt.geom.AffineTransform;

public class MyTimerTask extends TimerTask {
    private DisplayImage displayImage;
    private MyKeyListener listener;
    private Animation animator;
    private int startX, startY;
    private long time;
    private long changeTime1 = 0, changeTime2 = 0;
    private int velocityX = 0, velocityY = 0;
    private double groundedTime = 0;
    private boolean hovering = false;
    private int jumps = 0;
    private int bgWidth, bgHeight;
    private int myX, myY;
    private int maxV;
    private double cx, cy;
    private boolean checkOne;
    private boolean checkTwo;
    private boolean tryStep = false;
    private boolean didStep = false;
    private int temp1, temp4;
    private double temp2, temp3;
    private boolean temp5;
    private final int DELAY = 100; //adjust for milliseconds between frames
    private final int GROUND_COLOR = new Color(204, 236, 255).getRGB(); //adjust for ground hitbox
    private final int HURT_COLOR = new Color(201, 59, 49).getRGB(); //adjust for enemy hitbox
    private final int MAX_JUMPS = 2; //adjust for number of jumps
    private final int JUMP_BUFFERING = 200; //adjust for milliseconds jump buffering
    private final int COYOTE_TIME = 200; //adjust for milliseconds coyote time
    private final int JUMP_POWER = 27; //adjust for jump height
    private final int HOVER_POWER = 4; //adjust for hover strength
    private final int GRAVITY = -6; //adjust for gravity
    private final int SPEED = 20; //adjust for movement speed
    private final int RESPAWN_X = 50; //adjust for respawn x
    private final int RESPAWN_Y = 50; //adjust for respawn y
    private final int STEP_HEIGHT = 6; //adjust for slope climbing
    private final int SPRITE_SPEED = 20; //adjust for animation update speed
    
    public MyTimerTask(DisplayImage displayImage, MyKeyListener listener, Animation animator) {
        this.displayImage = displayImage;
        this.listener = listener;
        this.animator = animator;
        bgWidth = displayImage.getBackground().getWidth();
        bgHeight = displayImage.getBackground().getHeight();
        startX = displayImage.getPlayerLocX();
        startY = -displayImage.getPlayerLocY();
    }
    
    @Override
    public void run() {
        //jump
        time = System.currentTimeMillis();
        if(time - groundedTime > COYOTE_TIME) //coyote time
            jumps = Math.min(jumps, MAX_JUMPS - 1);
        if(time - listener.getJumpTime() > JUMP_BUFFERING) //jump buffering
            listener.setJumpTime0();
        if(jumps > 0 && listener.getJumpTime() > 0) { //jump
            if(jumps == MAX_JUMPS) //only hover if ground jump
                hovering = true;
            else
                hovering = false;
            listener.setJumpTime0();
            jumps--;
            velocityY = JUMP_POWER;
        }
        if(listener.getHoverStopTime() > 0) { //stop hovering if let go
            hovering = false;
            listener.setHoverStopTime0();
        }
        
        //set velocity based on input
        velocityX = 0;
        if(listener.getRight() && !listener.getLeft()) { //if going right only
            velocityX = SPEED * DELAY / 100;
            animator.setFlipped(false);
        } else if(listener.getLeft() && !listener.getRight()) { //if going left only
            velocityX = -SPEED * DELAY / 100;
            animator.setFlipped(true);
        }
        if(hovering) //change velocity if still hovering
            velocityY += HOVER_POWER * DELAY / 100; //adjust velocity for gravity
        velocityY += GRAVITY * DELAY / 100;
        
        testMove();
        startX += (int)cx;
        startY += (int)cy;
        
        /*startY += velocityY;
        if(startY <= 0) {
            velocityY = 0;
            startY = 0;
            jumps = MAX_JUMPS;
            hovering = false;
            groundedTime = System.currentTimeMillis();
        }
        startX += velocityX;*/
        
        displayImage.moveBackground(-startX, startY);
        updateAnimation();
    }
    
    public int getDelay() {
        return DELAY;
    }
    
    public void respawn() { //reset stats if died
        System.out.println("You died!");
        startY = RESPAWN_Y;
        startX = RESPAWN_X;
        velocityX = 0;
        velocityY = 0;
        jumps = 0;
        hovering = false;
    }
    
    public void groundedStats() {
        velocityY = Math.max(0, velocityY);
        jumps = MAX_JUMPS;
        groundedTime = System.currentTimeMillis();
        hovering = false;
    }
    
    public void updateAnimation() {
        if ((int)cy < 0) { // if falling
            animator.falling();
        } else if ((int)cy > 0) { // if jumping
            if(time-changeTime1>SPRITE_SPEED/Math.abs(velocityY)) {
                changeTime1 = time;
                animator.jumping();
            }
        } else if ((int)cx != 0) { // if walking
            if(time-changeTime2>SPRITE_SPEED/Math.abs(velocityX)) {
                changeTime2 = time;
                animator.walking();
            }
        } else // if standing
            animator.standing();
    }
    
    //if step might be possible, adds upward velocity and commits if it works, undo if not
    public void tryStep() {
        temp1 = maxV;
        temp2 = cx;
        temp3 = cy;
        temp4 = velocityY;
        temp5 = checkOne;
        startX += cx;
        startY += cy;
        velocityX -= (int)cx;
        velocityY = STEP_HEIGHT;
        
        tryStep = true;
        testMove();
        tryStep = false;
        
        if(cx != temp2) { //end move if did step
            didStep = true;
            groundedStats();
        } else { //undo otherwise
            maxV = temp1;
            cx = temp2;
            cy = temp3;
            velocityY = temp4;
            checkOne = temp5;
            startX -= cx;
            startY -= cy;
            velocityX += (int)cx;
        }
    }
    
    public boolean checkLeft() {
        myX = startX + displayImage.getPlayerLocX() - 1 + (int)cx;
        if(myX-1 < 0) {
            respawn();
            return true;
        }
        for(int y = -startY + displayImage.getPlayerLocY() + displayImage.getPlayer().getHeight()-1; y >= -startY + displayImage.getPlayerLocY(); y--) {
            if(displayImage.getBackground().getRGB(myX,y) == HURT_COLOR) { //if danger respawn
                respawn();
                return true;
            } if(displayImage.getBackground().getRGB(myX,y) == GROUND_COLOR) { //if ground move out of it
                cx -= velocityX/maxV;
                if( !tryStep )
                    tryStep();
                return true;
            }
        } return false;
    }
    
    public boolean checkRight() {
        myX = startX + displayImage.getPlayerLocX() + displayImage.getPlayer().getWidth() + 1 + (int)cx;
        if(myX+1 > displayImage.getBackground().getWidth()) {
            respawn();
            return true;
        }
        for(int y = -startY + displayImage.getPlayerLocY() + displayImage.getPlayer().getHeight()-1; y >= -startY + displayImage.getPlayerLocY(); y--) {
            if(displayImage.getBackground().getRGB(myX,y) == HURT_COLOR) { //if danger respawn
                respawn();
                return true;
            } if(displayImage.getBackground().getRGB(myX,y) == GROUND_COLOR) { //if ground move out of it
                cx -= velocityX/maxV;
                if( !tryStep )
                    tryStep();
                return true;
            }
        } return false;
    }
    
    public boolean checkTop() {
        myY = -startY + displayImage.getPlayerLocY() - 1 - (int)cy;
        if(myY-1 < 0) {
            respawn();
            return true;
        }
        for(int x = startX + displayImage.getPlayerLocX(); x < startX + displayImage.getPlayerLocX() + displayImage.getPlayer().getWidth(); x++) {
            if(displayImage.getBackground().getRGB(x,myY) == HURT_COLOR) { //if danger respawn
                respawn();
                return true;
            } if(displayImage.getBackground().getRGB(x,myY) == GROUND_COLOR) { //if ground move out of it
                cy -= velocityY/maxV;
                return true;
            }
        } return false;
    }
    
    public boolean checkBottom() {
        myY = -startY + displayImage.getPlayerLocY() + displayImage.getPlayer().getHeight() + 1 - (int)cy;
        
        /*BufferedImage hi = displayImage.getBackground();
        hi.setRGB(startX + displayImage.getPlayerLocX() - 1, myY, Color.BLUE.getRGB());
        displayImage.setBackground(hi);*/
        
        if(myY+1 > displayImage.getBackground().getHeight()) {
            respawn();
            return true;
        }
        for(int x = startX + displayImage.getPlayerLocX(); x < startX + displayImage.getPlayerLocX() + displayImage.getPlayer().getWidth(); x++) {
            if(displayImage.getBackground().getRGB(x,myY) == HURT_COLOR) { //if danger respawn
                respawn();
                return true;
            } if(displayImage.getBackground().getRGB(x,myY) == GROUND_COLOR) { //if ground move out of it
                cy -= velocityY/maxV;
                if( !tryStep )
                    groundedStats();
                
                /*BufferedImage hi = displayImage.getBackground();
                hi.setRGB(startX + displayImage.getPlayerLocX(), myY, Color.BLUE.getRGB());
                displayImage.setBackground(hi);*/
                
                return true;
            }
        } return false;
    }
    
    public void testMove() { //checks every instance between start and end point to see if there's danger or ground
        maxV = Math.max(Math.abs(velocityX), Math.abs(velocityY));
        cx = 0.5;
        cy = 0.5;
        checkOne = false;
        checkTwo = false;
        didStep = false;
        if (velocityX > 0 && velocityY > 0) { //Moving diagonally up-right
            for(int i = 0; i < maxV; i++) {
                if(!checkOne) {
                    cy += velocityY / maxV;
                    checkOne = checkTop();
                } if(!checkTwo) {
                    cx += velocityX / maxV;
                    checkTwo = checkRight();
                } if((checkOne && checkTwo) || didStep)
                    break;
            }
        } else if (velocityX > 0 && velocityY < 0) { //Moving diagonally down-right
            for(int i = 0; i < maxV; i++) {
                if(!checkOne) {
                    cy += velocityY / maxV;
                    checkOne = checkBottom();
                } if(!checkTwo) {
                    cx += velocityX / maxV;
                    checkTwo = checkRight();
                } if((checkOne && checkTwo) || didStep)
                    break;
            }
        } else if (velocityX < 0 && velocityY > 0) { //Moving diagonally up-left
            for(int i = 0; i < maxV; i++) {
                if (!checkOne) {
                    cy += velocityY / maxV;
                    checkOne = checkTop();
                } if (!checkTwo) {
                    cx += velocityX / maxV;
                    checkTwo = checkLeft();
                } if((checkOne && checkTwo) || didStep)
                    break;
            }
        } else if (velocityX < 0 && velocityY < 0) { //Moving diagonally down-left
            for(int i = 0; i < maxV; i++) {
                if(!checkOne) {
                    cy += velocityY / maxV;
                    checkOne = checkBottom();
                } if(!checkTwo) {
                    cx += velocityX / maxV;
                    checkTwo = checkLeft();
                } if((checkOne && checkTwo) || didStep)
                    break;
            }
        } else if (velocityX > 0) { //Moving right
            for(int i = 0; i < maxV; i++) {
                cx += velocityX/maxV;
                if(checkRight() || didStep)
                    break;
            }
        } else if (velocityX < 0) { //Moving left
            for(int i = 0; i < maxV; i++) {
                cx += velocityX/maxV;
                if(checkLeft() || didStep)
                    break;
            }
        } else if (velocityY > 0) { //Moving up
            for(int i = 0; i < maxV; i++) {
                cy += velocityY/maxV;
                if(checkTop())
                    break;
            }
        } else if (velocityY < 0) { //Moving down
            for(int i = 0; i < maxV; i++) {
                cy += velocityY/maxV;
                if(checkBottom())
                    break;
            }
        } else { //Stationary
            
        }
    }
    
}