package plateformgame;



/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author thomas
 */
public class GameThread extends Thread {
    boolean arret=false;
    long tps_actuel,tps_precedent,tps_precedent_fps;
    int image_count = 0;
    PlateformGame frame;
    
    public GameThread(PlateformGame frame){
        this.frame = frame;
    }
    
    public void run(){
        tps_precedent = System.currentTimeMillis();
        while(!arret){
            tps_actuel = System.currentTimeMillis();
            if(tps_actuel-tps_precedent>16){
                image_count++;
                frame.render();
                frame.updateEvt();
                tps_precedent = tps_actuel;
            }
            
            if(tps_actuel-tps_precedent_fps>1000){
                frame.updateFps(image_count);
                image_count=0;
                tps_precedent_fps = tps_actuel; 
            }
        }
    }
}
