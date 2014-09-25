/*
*  Copyright (C) 2014  Thomas DUBIER
* 
*   This program is free software: you can redistribute it and/or modify
*   it under the terms of the GNU General Public License as published by
*   the Free Software Foundation, either version 3 of the License, or
*   (at your option) any later version.
* 
*   This program is distributed in the hope that it will be useful,
*   but WITHOUT ANY WARRANTY; without even the implied warranty of
*   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*   GNU General Public License for more details.
* 
*    You should have received a copy of the GNU General Public License
*   along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package plateformgame;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.io.*;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author thomas
 */
public class PlateformGame extends Frame implements KeyListener{

    public static final String title = "Plateform Game";
    boolean [] keysPressed = new boolean[256];
    
    double xfond = 0;
    
    Image fond;
    
    
    GameThread gameThread = new GameThread(this);
    
    double a = 0.001;
    double v = 1;
    ArrayList<Plateforme> plateformes = new ArrayList<Plateforme>();
    
    BufferStrategy strategy;
    Graphics buffer;
    
    int fps = 0;
    
    Player p;
    /**
     * Creer la fenetre de jeu
     * @param w
     *  largeur de la fenetre
     * @param h 
     *  hauteur de la fenetre
     */
    public PlateformGame(int w,int h){
           setTitle(title);
           
           setSize(w,h);
           setVisible(true);
          
           setIgnoreRepaint(true);
           
           addWindowListener(new WindowAdapter(){
               public void windowClosing(WindowEvent we){
               System.exit(0);
               }
           });
           
           requestFocus();
           
           addKeyListener(this);
           createBufferStrategy(2);
           try{
            fond = ImageIO.read(this.getClass().getResource("Images/fond/fond.jpg"));
           }catch(Exception e){}
           strategy = getBufferStrategy();
           buffer = strategy.getDrawGraphics();
           p = new Player(getWidth()/2,getHeight()/2,45);
           loadLvl("lv1");
           fusionnerPlateformes();
           gameThread.start();
    }
    
    public void fusionnerPlateformes(){
        boolean fusion = true;
        while(fusion){
            fusion = false;
            for(int i=0;i<plateformes.size();i++){
                for(int j=0;j<plateformes.size();j++){
                    if(i<plateformes.size() && j<plateformes.size()){
                        if(plateformes.get(i).x+plateformes.get(i).width==plateformes.get(j).x && 
                            plateformes.get(i).y==plateformes.get(j).y){
                            plateformes.get(i).agrandir(plateformes.get(j).width,Plateforme.LARGEUR);
                            plateformes.remove(plateformes.get(j));
                            fusion = true;
                        }
                    }
                }
            }
        }
        
        fusion = true;
        while(fusion){
            fusion = false;
            for(int i=0;i<plateformes.size();i++){
                for(int j=0;j<plateformes.size();j++){
                    if(i<plateformes.size() && j<plateformes.size()){
                        if(plateformes.get(i).x==plateformes.get(j).x && 
                            plateformes.get(i).y+plateformes.get(i).height==plateformes.get(j).y &&
                            plateformes.get(i).width==plateformes.get(j).width){
                            plateformes.get(i).agrandir(plateformes.get(j).height,Plateforme.HAUTEUR);
                            plateformes.remove(plateformes.get(j));
                            fusion = true;
                        }
                    }
                }
            }
        }
        System.out.println(plateformes.size());
    }
    
    public void loadLvl(String levelname){
        System.out.println(this.getClass().getResource("Levels/"+levelname+".lvl").getPath());
        BufferedReader reader;
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(this.getClass().getResource("Levels/"+levelname+".lvl").getPath()), "utf-8"));
                String line;
                while((line=reader.readLine())!=null){
                    String result[] = line.split(";");
                    if(result.length==3){
                        int x = Integer.parseInt(result[0]);
                        int y = Integer.parseInt(result[1]);
                        int map = Integer.parseInt(result[2]);
                        Plateforme p = new Plateforme(x+800*map,y,32,32);
                        p.map = map;
                        plateformes.add(p);
                    }
                }
            reader.close();
        }catch (Exception ex) {
            System.err.println("Impossible de charger le niveau");
            System.exit(0);
        }
    }
    /**
     *  Mehtode de rendu de la fenetre
     */
    public void render(){
       buffer.setColor(Color.RED);
       if(xfond+fond.getWidth(null)>0)
            buffer.drawImage(fond,(int)xfond, 0, null);
       buffer.drawImage(fond,(int)xfond+fond.getWidth(null),  0, null);
       if(xfond+fond.getWidth(this)*2<=800)
           xfond=0;
       buffer.setColor(Color.WHITE);
       buffer.drawString("FPS:"+Integer.toString(fps),10,50);
       p.drawPlayer((Graphics2D)buffer);
       for(int i=0;i<plateformes.size();i++){
           if(plateformes.get(i).x+plateformes.get(i).width>0 && plateformes.get(i).x<800)
                plateformes.get(i).drawPlateforme((Graphics2D)buffer);
       }
       strategy.show();
    }
    
    /**
     * Actualiser le nombre d'image par seconde
     * @param imagecount
     *  nombre d'image par seconde
     */
    public void updateFps(int imagecount){
        this.fps = imagecount;
    }
    
    /**
     *  update des positions du player et traitement des evenements claviers
     */
    public void updateEvt(){
       xfond-=1;

       if(keysPressed[KeyEvent.VK_UP])
           p.setDeplacement(Player.SAUT);
       p.update();
       for(int i=0;i<plateformes.size();i++){
               if(p.boundingBox.intersects(plateformes.get(i))){
                   switch(plateformes.get(i).PositionRelative(p)){
                       case Plateforme.DESSUS:
                        p.finSaut(plateformes.get(i).y-p.spriteH);
                       break;
                       case Plateforme.DESSOUS:
                        p.renvoieSaut(plateformes.get(i).y+plateformes.get(i).height+p.spriteH);
                       break;
                       case Plateforme.DROITE:
                        p.renvoie(plateformes.get(i).x+plateformes.get(i).width+p.spriteW,4,-0.05);
                       break;
                       case Plateforme.GAUCHE:
                        p.renvoie(plateformes.get(i).x-p.spriteW,-4,0.05);
                       break;
                    }
               }
            plateformes.get(i).translateX((int)-2);
       }
       if(p.boundingBox.x+p.boundingBox.width<0 || p.boundingBox.y+p.boundingBox.height>600)
        reset();
    }
    
    public void reset(){
        gameThread.arret = true;
        dispose();
        new PlateformGame(800,600);
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        new PlateformGame(800,600);
    }
    
    
    @Override
    public void keyTyped(KeyEvent arg0) {
        
    }

    @Override
    public void keyPressed(KeyEvent arg0) {
       if(!keysPressed[arg0.getKeyCode()])
            keysPressed[arg0.getKeyCode()]=true;
    }

    @Override
    public void keyReleased(KeyEvent arg0) {
       if(keysPressed[arg0.getKeyCode()])
            keysPressed[arg0.getKeyCode()]=false;
    }
}
