/**
 * Purpose: Watch arrow keys for input
 * 
 * Owen Colley
 * 5/4/24
 * 
 */

import java.awt.event.*;
import java.util.Timer;
import java.util.TimerTask;

//Detect arrow keys
public class MyKeyListener implements KeyListener {
    private boolean up = false;
    private boolean down = false;
    private boolean left = false;
    private boolean right = false;
    private double jumpTime = 0;
    private double hoverStopTime = 0;
    
    @Override
    public void keyPressed(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_UP:
                up = true;
                jumpTime = System.currentTimeMillis();
                return;
            case KeyEvent.VK_DOWN:
                down = true;
                return;
            case KeyEvent.VK_LEFT:
                left = true;
                return;
            case KeyEvent.VK_RIGHT:
                right = true;
                return;
        }
    }
    
    @Override
    public void keyReleased(KeyEvent e) {
        int keyCode = e.getKeyCode();
        switch (keyCode) {
            case KeyEvent.VK_UP:
                up = false;
                hoverStopTime = System.currentTimeMillis();
                return;
            case KeyEvent.VK_DOWN:
                down = false;
                return;
            case KeyEvent.VK_LEFT:
                left = false;
                return;
            case KeyEvent.VK_RIGHT:
                right = false;
                return;
        }
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
    }
    
    public boolean getUp() {
        return up;
    }
    
    public boolean getDown() {
        return down;
    }
    
    public boolean getLeft() {
        return left;
    }
    
    public boolean getRight() {
        return right;
    }
    
    public double getJumpTime() {
        return jumpTime;
    }
    
    public void setJumpTime0() {
        jumpTime = 0;
    }
    
    public double getHoverStopTime() {
        return hoverStopTime;
    }
    
    public void setHoverStopTime0() {
        hoverStopTime = 0;
    }
    
}