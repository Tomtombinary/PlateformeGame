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
import javax.imageio.ImageIO;

/**
 *
 * @author thomas
 */
public class Plateforme extends Rectangle{
    Color color = Color.BLACK;
    Image texture;
    int textureWidth;
    int textureHeight;
    int map = 0;
    
    public static final int DESSUS=1,DESSOUS=2,DROITE=3,GAUCHE=4;
    public static final int HAUTEUR=0,LARGEUR=1;
    
    public Plateforme(int x,int y,int w,int h){
        super(x,y,w,h);
        try{
            texture = ImageIO.read(this.getClass().getResource("Images/textures/brique.png"));
            textureWidth = texture.getWidth(null);
            textureHeight= texture.getHeight(null);
        }catch(Exception e){}
    }
    
    public void drawPlateforme(Graphics2D g){
        for(int i=0;i<(width/textureWidth);i++){
            for(int j=0;j<(height/textureHeight);j++){
                g.drawImage(texture,x+i*textureWidth,y+j*textureHeight,null);
            }
        }
        g.setColor(color);
        g.drawRect(x, y, width, height);
    }
    
    public void translateX(int dx){
        this.x+=dx;
    }
    
    public int PositionRelative(Player p){
        if(p.boundingBox.y+p.boundingBox.height<this.y+p.boundingBox.height/2 && p.boundingBox.y<this.y)
                return DESSUS;
            else if(p.boundingBox.y>this.y+this.height-p.boundingBox.height/2 && p.boundingBox.y+p.boundingBox.height>this.y+this.height)
                return DESSOUS;
            else if(p.boundingBox.x+p.boundingBox.width<this.x+this.width/2)
                return GAUCHE;
            else if(p.boundingBox.x>this.x+this.width/2)
                return DROITE;
        return 0;
    }
    
    public void agrandir(int size,int flag){
        if(flag==LARGEUR)
            this.width+=size;
        if(flag==HAUTEUR)
            this.height+=size;
    }
}
