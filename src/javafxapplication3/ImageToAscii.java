package javafxapplication3;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.QuadCurve2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 *
 * @author Karol
 */
public class ImageToAscii {
    private static boolean wasup=true;
   
    public static BufferedImage CharToIMG4(BufferedImage img, boolean color, int x, int y, int fsize) {
        int p, r, g, b;
        float luminance;
        int dziel = x * y;
        BufferedImage bi = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics graphics = bi.getGraphics();
        graphics.setColor(java.awt.Color.lightGray);
        
        if (color) {
            graphics.setColor(java.awt.Color.DARK_GRAY);
        }
        
        graphics.fillRect(0, 0, x * img.getWidth(), y * img.getHeight());
        graphics.setColor(java.awt.Color.BLACK);
        graphics.setFont(new Font("Arial Black", Font.PLAIN, fsize)); //12
        
        for (int i = 0; i < img.getHeight(); i += y) {
            for (int j = 0; j < img.getWidth(); j += x) {
                
                p = r = g = b = 0;
                
                for (int ix = 0; ix <= x; ix++) {
                    for (int iy = 0; iy <= y; iy++) {
                        p = img.getRGB(j, i);
                        r += (p >> 16) & 0xff;
                        g += (p >> 8) & 0xff;
                        b += p & 0xff;
                    }
                }
                
                r /= dziel;
                g /= dziel;
                b /= dziel;
                if (r > 255) {
                    r = 255;
                }
                if (g > 255) {
                    g = 255;
                }
                if (b > 255) {
                    b = 255;
                }
                if (color) {
                    graphics.setColor(new java.awt.Color(r, g, b));
                }
                luminance = (r * 0.3f + g * 0.59f + b * 0.11f);
                graphics.drawString("" + druk4(luminance, color), j, i);                     //moze odwrotnie
            }
        }
        return bi;
    }
    
    public static char druk4(float f, boolean color) {
        
        String original = "$@B%8&WM#*oahkbdpqwmZO0QLCJUYXzcvunxrjft/\\|()1{}[]?-_+~<>i!lI;:,\"^`'. ";
        String s = "$@B%8&WM#*oahkbdpqwmZO0QLCJUYXzcvunxrjft/\\|()1{}[]?-_+~<>i!l"; //70 teraz 60 na 255 to 3,642857142857143‬
        
        if (color) {
            s = "$@B%8&WM#*oahkbdpqwmZO0QLCJUYXzc";
        }
        String sr = "";
        
        for (int i = s.length() - 1; 0 <= i; i--) {
            sr += s.charAt(i);
        }
        
        double l = s.length();
        
        for (;;) {
            
            if (f < 0) {
                return s.charAt(0);
            } else if (f >= 255) {
                return s.charAt((int) l - 1);
            } else {
                return s.charAt((int) ((f * l / 255.0f)));
            }
        }
    }
    
    public static BufferedImage convertToType(BufferedImage sourceImage, int targetType) {
        BufferedImage image;
        if (sourceImage.getType() == targetType) {
            image = sourceImage;
        } else {
            image = new BufferedImage(sourceImage.getWidth(), sourceImage.getHeight(), targetType);
            image.getGraphics().drawImage(sourceImage, 0, 0, null);
        }
        return image;
    }
    
    public static BufferedImage imageToDots(BufferedImage img,int size,boolean color)
    {
        int p,r,g,b;
        float luminance=0;
        BufferedImage bi =new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics graphics = bi.getGraphics();
        graphics.setColor(java.awt.Color.BLACK);                                                         //tło
        graphics.fillRect(0, 0,img.getWidth(), img.getHeight());
        graphics.setColor(java.awt.Color.WHITE);
        
        int max=size*size;
        int dots;
        Map<Integer, Integer> mapa=new HashMap<Integer, Integer>();
        int xpos;
        int ypos;
        
        for(int i=0; i<img.getHeight(); i+=size){
            for(int j=0; j<img.getWidth(); j+=size){
                for(int y=0;y<size;y++)
                    for(int x=0;x<size;x++){
                        
                        if(img.getHeight()>(y+i)&&img.getWidth()>(x+j)){
                            
                            p=img.getRGB(j+x, i+y);
                           // if(color)graphics.setColor(new Color(p));
                            r = (p>>16) & 0xff;
                            g = (p>>8) & 0xff;
                            b = p & 0xff;
                            luminance+= (r * 0.3f+ g * 0.59f + b * 0.11f) ;
                        }
                    }
                luminance/=max;
                dots=Math.round((luminance/255)*max);
                
                for(int d=0;d<dots;d++){
                    
                    xpos=(int) Math.round((size)*Math.random());
                    ypos=(int) Math.round((size)*Math.random());
                    boolean war=true;
                    while(war){
                        
                        if(mapa.containsKey(xpos)){
                            
                            if(mapa.get(xpos).equals(ypos)){
                                
                                xpos=(int) Math.round((size)*Math.random());
                                ypos=(int) Math.round((size)*Math.random());
                            }else
                                war=false;
                        }else
                            war=false;
                    }
                    mapa.put(xpos, ypos);
                    if(img.getWidth()>(xpos+j)&&img.getHeight()>(ypos+i)){
                        if(color){
                         p=img.getRGB(xpos+j, ypos+i);
                         graphics.setColor(new Color(p));
                        }
                        graphics.fillRect(xpos+j, ypos+i, 1, 1);
                    }
                }
                mapa.clear();
                luminance=0;
            }
            
        }
        
        return bi;
    }
    
    public static BufferedImage curvedLines(BufferedImage srci,int scalex, int scaley) throws IOException{
        QuadCurve2D q = new QuadCurve2D.Float();
        int p,r,g,b;
        float luminance=0;
        BufferedImage img=srci;
        BufferedImage bi=new BufferedImage( img.getWidth(),img.getHeight(),BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d= bi.createGraphics();
        int max=scalex*scaley;
        int l;
        int iter=1;
        
        for(int i=0; i<img.getHeight(); i+=scaley)
        {
            wasup=false;
            for(int j=0; j<img.getWidth(); j+=scalex)
            {
                System.out.println("nr "+i+" "+j);
                for(int y=0;y<scaley;y++)
                    for(int x=0;x<scalex;x++)
                    {
                        if(img.getHeight()>(y+i)&&img.getWidth()>(x+j))
                        {
                            p=img.getRGB(j+x, i+y);
                            r = (p>>16) & 0xff;
                            g = (p>>8) & 0xff;
                            b = p & 0xff;
                            luminance+= (r * 0.3f+ g * 0.59f + b * 0.11f) ;
                        }
                    }
                luminance/=max;
                
                l=Math.round((luminance/255)*6); //12skala wyboru
                
                switch(l){
                    case 6: g2d.draw(krzywa(q,j, i, scalex,scaley,1.5));
                    break;
                    case 5: g2d.draw(krzywa(q,j, i, scalex,scaley,2));
                    break;
                    case 4: g2d.draw(krzywa(q,j, i, scalex,scaley,3));
                    break;
                    case 3: g2d.draw(krzywa(q,j, i, scalex,scaley,4));
                    break;
                    case 2: g2d.draw(krzywa(q,j, i, scalex,scaley,5));
                    break;
                    case 1: g2d.draw(krzywa(q,j, i, scalex,scaley,6));
                    break;
                    case 0: g2d.draw(plaski(q,j, i, scalex,scaley));
                    break;
                    default:
                        break;
                }
            }
            iter=((iter==1)?2:1);
            luminance=0;
        }
        
        return bi;
    }
    public static QuadCurve2D krzywa(QuadCurve2D curve, int x, int y, int scalex,int scaley,double dzielnik){
        
        int i=scaley;
        if(wasup){
            i=-scaley;
            wasup=false;
        }
        else
            wasup=true;
        curve.setCurve(x, y+((scaley)/2), x+((scalex)/2), y+((scaley)/2)+(i/dzielnik), x+scalex, y+((scaley)/2));
        return curve;
    }
    public static QuadCurve2D krzywa(QuadCurve2D curve, int x, int y, int mx,int my,int lx,int ly){
        
        curve.setCurve(x, y, mx, my, lx,ly);
        return curve;
    }
    
    public static QuadCurve2D plaski(QuadCurve2D curve,int x, int y, int scalex,int scaley){
        curve.setCurve(x, y+((scaley)/2), x+((scalex)/2), y+((scaley)/2), x+scalex, y+((scaley)/2));
        return curve;
    }
    public static BufferedImage curvedRound(BufferedImage img,int l,double skala,int odstep){

        QuadCurve2D q = new QuadCurve2D.Float();
        BufferedImage bi=new BufferedImage( img.getWidth(),img.getHeight(),BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d= bi.createGraphics();
        double skalaodstepu=skala;
        int przerwa=odstep;
        System.out.println(img.getWidth()+" "+img.getHeight());
        int cenx=img.getWidth()/2;
        int ceny=img.getHeight()/2;
        int malapolowa=(cenx<ceny)?ceny:cenx;
        double dystans=l;
        int lastx = 0;
        int lasty = 0;
        int pointx;
        int pointmx;
        int pointmy;
        int pointy;
        int xpp,ypp,xpk,ypk;
        int p,r,g,b;
        
        while(dystans<malapolowa){
            
            System.out.println("d---"+dystans);
            double lokregu=((2.0*Math.PI)*dystans);
            odstep=(int)(lokregu/l);
            double stopnie=360/odstep;
            dystans+=(l*skalaodstepu+przerwa);
            for(double i=0;i<=360;i+=stopnie){
                if(stopnie==0){
                    dystans-=(l*skalaodstepu+przerwa);
                    ++l;//+=0.5;                                               //blokuje przy liczbach <1,
                    odstep=(int)(lokregu/l);
                    stopnie=360/odstep;
                    dystans+=(l*skalaodstepu+przerwa);
                    continue;
                }
                double deg=Math.toRadians((i));
                double deg2=Math.toRadians((i-stopnie)+stopnie/2);
                pointx=(int)(dystans*Math.cos(deg))+cenx;
                pointy=(int)(dystans*Math.sin(deg))+ceny;
                pointmx=(int)(dystans*Math.cos(deg2))+cenx;
                pointmy=(int)(dystans*Math.sin(deg2))+ceny;
                
                //pobierz dane pixeli
                xpp=pointmx-l/2;
                ypp=pointmy-l/2;
                xpk=pointmx+l/2;
                ypk=pointmy+l/2;
                int licznik=0;
                double luminance=0;
                if(xpp<0)
                    xpp=0;
                if(ypp<0)
                    ypp=0;
                if(xpk>img.getWidth())
                    xpk=img.getWidth();
                if(ypk>img.getHeight())
                    ypk=img.getHeight();
                for(int x=xpp;x<=xpk;x++)
                    for(int y=ypp;y<=ypk;y++){
                        try{
                            p=img.getRGB(x, y);
                            r = (p>>16) & 0xff;
                            g = (p>>8) & 0xff;
                            b = p & 0xff;
                            luminance+= (r * 0.3f+ g * 0.59f + b * 0.11f) ;
                        }
                        catch(Exception e){
                            System.err.println(e.getMessage());
                        }
                        ++licznik;
                        
                    }
                
                luminance=luminance/licznik;
                luminance=Math.round((luminance/255)*8)+1;
                int dodatek=(int)((luminance/8)*l*skalaodstepu);
                
                if(wasup){
                    dodatek=-dodatek;
                    wasup=false;
                }
                else
                    wasup=true
                            ;
                pointmx=(int)((dystans+dodatek)*Math.cos(deg2))+cenx;
                pointmy=(int)((dystans+dodatek)*Math.sin(deg2))+ceny;
                if(lastx!=0&&lasty!=0)
                    g2d.draw(krzywa(q,lastx,lasty, pointmx,pointmy, pointx,pointy));
                lastx=pointx;
                lasty=pointy;
            }
            lastx=lasty=0;
        }

        return bi;
    }
    public static BufferedImage koloruj(BufferedImage img,BufferedImage imgwcolor){
        int p,r,g,b;
        BufferedImage bi=new BufferedImage( img.getWidth(),img.getHeight(),img.getType());
        for(int x=0;x<img.getWidth();x++)
            for(int y=0;y<img.getHeight();y++)
            {
                p=img.getRGB(x, y);
                r = (p>>16) & 0xff;
                g = (p>>8) & 0xff;
                b = p & 0xff;
                if((r!=0&&g!=0&&b!=0))
                    bi.setRGB(x, y, imgwcolor.getRGB(x, y));   
            }
        return bi;
    }

      public static BufferedImage imageToLine(BufferedImage img,int scale)
    {
        
        boolean color=true;
         int p,r,g,b;
         float luminance=0;
 
    
        
            BufferedImage bi=new BufferedImage( img.getWidth(),img.getHeight(),BufferedImage.TYPE_INT_RGB); 
            Graphics graphics = bi.getGraphics();    
             
            if(color)
            graphics.setColor(java.awt.Color.WHITE);                //tło
            else 
            graphics.setColor(java.awt.Color.BLACK);    
            graphics.fillRect(0, 0,img.getWidth(), img.getHeight());       
            
            if(color)
            graphics.setColor(java.awt.Color.BLACK);    
            else  
            graphics.setColor(java.awt.Color.WHITE);    
            
            int size=scale*2;//8;
            int max=size*size;
            int lines;  
            int pl=scale;//4;
            for(int i=0; i<img.getHeight(); i+=size)
            {
       
                for(int j=0; j<img.getWidth(); j+=size)
                {
                    System.out.println("nr "+i+" "+j);
                    for(int y=0;y<size;y++)
                        for(int x=0;x<size;x++)
                        {
                            if(img.getHeight()>(y+i)&&img.getWidth()>(x+j))
                                {
                                   // try{
                                p=img.getRGB(j+x, i+y);
                                
                                
                                r = (p>>16) & 0xff;
                                g = (p>>8) & 0xff;
                                b = p & 0xff;
                                luminance+= (r * 0.3f+ g * 0.59f + b * 0.11f) ;
                                //    }catch(Exception e){}
                                }
                        }
                    luminance/=max;
                    
                    
                   
                  

                     lines=Math.round((luminance/255)*12);
 
                     switch(lines)
                    {
                        case 0:
                            graphics.drawLine(j+size-1, i+1, j, i+size-2);
                        case 1:
                            graphics.drawLine(j, i-1, j+size-1, i+size-2);
                            //graphics.fillRect(j, i, size, size);break;
                        case 2:
                             graphics.drawLine(j+pl+1, i, j+pl-1, i+size-1);
                        case 3:
                             graphics.drawLine(j, i+pl+1, j+size-1, i+pl-1);
                        case 4:
                             graphics.drawLine(j+pl-1, i, j+pl+1, i+size-1);
                        case 5:
                             graphics.drawLine(j, i+pl-1, j+size-1, i+pl+1);
                        case 6:
                            graphics.drawLine(j+size-2, i, j+1, i+size-1);
                        case 7:
                            graphics.drawLine(j, i+1, j+size-1, i+size-2);
                        case 8:
                            graphics.drawLine(j+pl, i, j+pl, i+size-1);
                        case 9:
                            graphics.drawLine(j, i+pl, j+size-1, i+pl);
                        case 10:
                            graphics.drawLine(j, i, j+size-1, i+size-1);
                        case 11:
                            graphics.drawLine(j+size-1, i, j, i+size-1);
                        case 12:
                            break;
                        default : 
                            System.out.println("nie tak"+lines);break;    
                    }
                    
                    
                    //graphics.drawString(""+druk3(luminance), x*j, y*i);                     //moze odwrotnie             
                }   
                 luminance=0;
            }
            return bi;
  
           
        
    
    }
      

}
