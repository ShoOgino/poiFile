package org.apache.poi.hssf.contrib.view;

import java.awt.Graphics;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Color;
import java.awt.Component;

import javax.swing.border.AbstractBorder;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;

/**
 * This is an attempt to implement Excel style borders for the SuckyViewer
 *
 */
public class SVBorder extends AbstractBorder {
   Color northColor = null;
   Color eastColor = null;
   Color southColor = null; 
   Color westColor = null;
   int northBorderType;
   int eastBorderType;
   int southBorderType;
   int westBorderType;
   boolean northBorder=false;
   boolean eastBorder=false;
   boolean southBorder=false;
   boolean westBorder=false;

   public SVBorder(Color northColor, Color eastColor, 
                   Color southColor, Color westColor,
                   int northBorderType, int eastBorderType,
                   int southBorderType, int westBorderType, 
                   boolean northBorder, boolean eastBorder,
                   boolean southBorder, boolean westBorder) {
     this.northColor = northColor;
     this.eastColor = eastColor;
     this.southColor = southColor;
     this.westColor = westColor;
     this.northBorderType = northBorderType;
     this.eastBorderType = eastBorderType;
     this.southBorderType = southBorderType;
     this.westBorderType = westBorderType; 
     this.northBorder=northBorder;
     this.eastBorder=eastBorder;
     this.southBorder=southBorder;
     this.westBorder=westBorder;
   }

   public void paintBorder(Component c, Graphics g, int x, int y, int width,
                           int height) {
      Color oldColor = g.getColor();
      int i;

     paintNormalBorders(g, x, y, width, height); 
     paintDottedBorders(g, x, y, width, height); 
     paintDashedBorders(g, x, y, width, height);
     paintDoubleBorders(g, x, y, width, height);
    


     g.setColor(oldColor);    
   }

   private void paintNormalBorders(Graphics g, int x, int y, int width, 
                                  int height) {
       
      if (northBorder && 
             ((northBorderType == HSSFCellStyle.BORDER_THIN) ||
              (northBorderType == HSSFCellStyle.BORDER_MEDIUM) ||
              (northBorderType == HSSFCellStyle.BORDER_THICK) 
             )
         ) {

        int thickness = getThickness(northBorderType);

      	g.setColor(northColor); 

        for (int k=0; k < thickness; k++) {
           g.drawLine(x,y+k,width,y+k);
        }
      }

      if (eastBorder && 
             ((eastBorderType == HSSFCellStyle.BORDER_THIN) ||
              (eastBorderType == HSSFCellStyle.BORDER_MEDIUM) ||
              (eastBorderType == HSSFCellStyle.BORDER_THICK) 
             )
         ) {

        int thickness = getThickness(eastBorderType);

      	g.setColor(eastColor); 

        for (int k=0; k < thickness; k++) {
           g.drawLine(width-k,y,width-k,height);
        }
      }

      if (southBorder && 
              ((southBorderType == HSSFCellStyle.BORDER_THIN) ||
               (southBorderType == HSSFCellStyle.BORDER_MEDIUM) ||
               (southBorderType == HSSFCellStyle.BORDER_THICK)
              )
         ) {

        int thickness = getThickness(southBorderType);

      	g.setColor(southColor); 
        for (int k=0; k < thickness; k++) {
           g.drawLine(x,height - k,width,height - k);
        }
      }

      if (westBorder && 
             ((westBorderType == HSSFCellStyle.BORDER_THIN) ||
              (westBorderType == HSSFCellStyle.BORDER_MEDIUM) ||
              (westBorderType == HSSFCellStyle.BORDER_THICK) 
             )
         ) {

        int thickness = getThickness(westBorderType);

      	g.setColor(westColor); 

        for (int k=0; k < thickness; k++) {
           g.drawLine(x+k,y,x+k,height);
        }
      }
   }

   private void paintDottedBorders(Graphics g, int x, int y, int width, 
                                  int height) {
      if (northBorder && 
             northBorderType == HSSFCellStyle.BORDER_DOTTED) {
        int thickness = getThickness(northBorderType);

      	g.setColor(northColor); 

        for (int k=0; k < thickness; k++) {
           for (int xc = x; xc < width; xc=xc+2) {
             g.drawLine(xc,y+k,xc,y+k);
           }
        }
      }

      if (eastBorder && 
              eastBorderType == HSSFCellStyle.BORDER_DOTTED
         ) {

        int thickness = getThickness(eastBorderType);
        thickness++; //need for dotted borders to show up east

      	g.setColor(eastColor); 

        for (int k=0; k < thickness; k++) {
           for (int yc=y;yc < height; yc=yc+2) {
                g.drawLine(width-k,yc,width-k,yc);
           }
        }
      }

      if (southBorder && 
              southBorderType == HSSFCellStyle.BORDER_DOTTED
         ) {

        int thickness = getThickness(southBorderType);
        thickness++;
      	g.setColor(southColor); 
        for (int k=0; k < thickness; k++) {
           for (int xc = x; xc < width; xc=xc+2) {
             g.drawLine(xc,height-k,xc,height-k);
           }
        }
      }

      if (westBorder && 
            westBorderType == HSSFCellStyle.BORDER_DOTTED
         ) {

        int thickness = getThickness(westBorderType);
//        thickness++;

      	g.setColor(westColor); 

        for (int k=0; k < thickness; k++) {
           for (int yc=y;yc < height; yc=yc+2) {
                g.drawLine(x+k,yc,x+k,yc);
           }
        }
      }
   }


   private void paintDashedBorders(Graphics g, int x, int y, int width, 
                                  int height) {
      if (northBorder && 
             ((northBorderType == HSSFCellStyle.BORDER_DASHED) ||
              (northBorderType == HSSFCellStyle.BORDER_HAIR))
         ) {
        int thickness = getThickness(northBorderType);

        int dashlength = 1;
       
        if (northBorderType == HSSFCellStyle.BORDER_DASHED)
           dashlength = 2; 

      	g.setColor(northColor); 

        for (int k=0; k < thickness; k++) {
           for (int xc = x; xc < width; xc=xc+5) {
             g.drawLine(xc,y+k,xc+dashlength,y+k);
           }
        }
      }

      if (eastBorder && 
              ((eastBorderType == HSSFCellStyle.BORDER_DASHED) ||
               (eastBorderType == HSSFCellStyle.BORDER_HAIR)) 
         ) {

        int thickness = getThickness(eastBorderType);
        thickness++; //need for dotted borders to show up east


        int dashlength = 1;
       
        if (eastBorderType == HSSFCellStyle.BORDER_DASHED)
           dashlength = 2; 

      	g.setColor(eastColor); 

        for (int k=0; k < thickness; k++) {
           for (int yc=y;yc < height; yc=yc+5) {
                g.drawLine(width-k,yc,width-k,yc+dashlength);
           }
        }
      }

      if (southBorder && 
              ((southBorderType == HSSFCellStyle.BORDER_DASHED) ||
               (southBorderType == HSSFCellStyle.BORDER_HAIR))
         ) {

        int thickness = getThickness(southBorderType);
        thickness++;

        int dashlength = 1;
       
        if (southBorderType == HSSFCellStyle.BORDER_DASHED)
           dashlength = 2; 

      	g.setColor(southColor); 
        for (int k=0; k < thickness; k++) {
           for (int xc = x; xc < width; xc=xc+5) {
             g.drawLine(xc,height-k,xc+dashlength,height-k);
           }
        }
      }

      if (westBorder && 
            ((westBorderType == HSSFCellStyle.BORDER_DASHED) ||
             (westBorderType == HSSFCellStyle.BORDER_HAIR))
         ) {

        int thickness = getThickness(westBorderType);
//        thickness++;

        int dashlength = 1;
       
        if (westBorderType == HSSFCellStyle.BORDER_DASHED)
           dashlength = 2; 

      	g.setColor(westColor); 

        for (int k=0; k < thickness; k++) {
           for (int yc=y;yc < height; yc=yc+5) {
                g.drawLine(x+k,yc,x+k,yc+dashlength);
           }
        }
      }
   }


   private void paintDoubleBorders(Graphics g, int x, int y, int width, 
                                  int height) {
      if (northBorder && 
             northBorderType == HSSFCellStyle.BORDER_DOUBLE) {

      	g.setColor(northColor); 

           g.drawLine(x,y,width,y);
           g.drawLine(x+3,y+2,width-3,y+2);
      }

      if (eastBorder && 
              eastBorderType == HSSFCellStyle.BORDER_DOUBLE
         ) {

        int thickness = getThickness(eastBorderType);
        thickness++; //need for dotted borders to show up east

      	g.setColor(eastColor); 

        g.drawLine(width-1,y,width-1,height);
        g.drawLine(width-3,y+3,width-3,height-3);
      }

      if (southBorder && 
              southBorderType == HSSFCellStyle.BORDER_DOUBLE
         ) {

      	g.setColor(southColor); 


        g.drawLine(x,height - 1,width,height - 1);
        g.drawLine(x+3,height - 3,width-3,height - 3);
      }

      if (westBorder && 
            westBorderType == HSSFCellStyle.BORDER_DOUBLE
         ) {

        int thickness = getThickness(westBorderType);
//        thickness++;

      	g.setColor(westColor); 

           g.drawLine(x,y,x,height);
           g.drawLine(x+2,y+2,x+2,height-3);
      }
   }

   private int getThickness(int thickness) {
       int retval=1;
       switch (thickness) {
           case HSSFCellStyle.BORDER_THIN:
             retval=2;
             break;
           case HSSFCellStyle.BORDER_MEDIUM:
             retval=3;
             break;
           case HSSFCellStyle.BORDER_THICK:
             retval=4;
             break;
           case HSSFCellStyle.BORDER_DASHED:
             retval=1;
             break;
           case HSSFCellStyle.BORDER_HAIR:
             retval=1;
             break;
           default:
             retval=1;
       }
       return retval; 
   }


}
