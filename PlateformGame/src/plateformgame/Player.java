/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package plateformgame;

import java.awt.*;
import java.awt.geom.AffineTransform;
import javax.imageio.ImageIO;

/**
 *
 * @author thomas
 */
public class Player {
    public static int SAUT=1,ENSAUT=2,AUSOL=3;
    double x,y;
    double vx,vy,vr;
    double ax,ay,ar;
    int spriteW,spriteH;
    
    double angle;
    int flag = ENSAUT;
    
    Image sprite;
    
    Rectangle boundingBox;
    
    public Player(int x,int y,int angle){
       this.x = x;
       this.y = y;
       this.vx = 0;
       this.vy = 0;
       this.vr = 0.1;
       this.ax = 0;
       this.ar = 0;
       this.ay = 0.25;
       this.angle = angle;
       try{
            this.sprite = ImageIO.read(this.getClass().getResource("Images/player/spriteredim.png"));
            spriteW = sprite.getWidth(null)/2;
            spriteH = sprite.getHeight(null)/2;
       }catch(Exception e){}
       
       this.boundingBox = new Rectangle(x,y,this.sprite.getWidth(null),this.sprite.getHeight(null));
    }
    
    public void update(){
        if((vx+ax>0 && vx<0) || (vx+ax<0 && vx>0)){
            ax = 0;
            vx = 0;
        }
        vr+=ar;
        vx+=ax;
        vy+=ay;
        x+=vx;
        y+=vy;
        
        if(vx>=0)
            rotate(vr);
        else
            rotate(-vr);
        
        boundingBox.setLocation((int)x-spriteW,(int)y-spriteH);
    }
    
    public void setDeplacement(int flag){
        if(this.flag==AUSOL && flag==SAUT){
            this.flag = ENSAUT;
            vy = -8;
            vr = 0.40;
            ar = -0.005;
        }
    }
    
    public void finSaut(int y){  
            flag=AUSOL;
            this.y=y;
            this.boundingBox.setLocation((int)x-spriteW,(int)y-spriteH);
            vy = 0;
            vr = 0.1;
            ar = 0;
    }
    
    public void renvoieSaut(int y){
         this.y=y;
         this.boundingBox.setLocation((int)x-spriteW,(int)y-spriteH);
         if(vy<0)
            vy = -vy;
         ay = 0.25; 
    }
    
    public void renvoie(int position,double vx,double ax){
        this.x = position;
        this.boundingBox.setLocation((int)x-spriteW,(int)y-spriteH);
        this.vx = vx;
        this.ax = ax;
        this.x+=vx;
    }
    
    public void drawPlayer(Graphics2D g){
        g.setColor(Color.BLUE);
        AffineTransform last = g.getTransform();
        AffineTransform rotateTranslate = new AffineTransform();
        rotateTranslate.translate((int)x,(int)y);
        rotateTranslate.rotate(angle);
        g.setTransform(rotateTranslate);
        g.drawImage(sprite,-spriteW,-spriteH, null);
        g.setTransform(last);
    }
    
    public void move(int x,int y){
        this.x = x;
        this.y = y;
    }
    
    public void translate(int dx,int dy){
        this.x+=dx;
        this.y+=dy;
    }
    
    public void rotate(double angle){
        this.angle+=angle;
    }
}
