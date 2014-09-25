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
