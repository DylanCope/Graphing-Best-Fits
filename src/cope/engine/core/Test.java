package cope.engine.core;

import java.awt.*;  
import java.awt.image.*;  
import javax.swing.*;  
  
public class Test  
{  
    public static void test() throws Exception  
    {  
        Font font = new Font("sanserif", Font.PLAIN, 12);
        FontMetrics metrics = new JLabel().getFontMetrics(font);  
        int width = metrics.stringWidth("H");  
        int height = metrics.getMaxAscent();  
        // use ARGB or the background will be black as well  
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);  
        // create a Graphics2D object from the BufferedImage  
        Graphics2D g2d = bi.createGraphics();  
        g2d.setFont(font);  
        g2d.setColor(Color.black);  
        g2d.drawString("H", 0, height);  
        g2d.dispose();  
        for(int j = 0; j < height; j++){    //Scroll through rows  
            for(int i = 0; i < width; i++){ //Scroll through columns  
                // check for != 0 as black is -(2^24) and that's negative  
                System.out.print(bi.getRGB(i,j)!=0?"1":"0");  //If pixel rgb value is zero, output '0',  
                                                             //else '1'(want monochome image)  
            }  
            System.out.print("\n"); //next line  
        }  
    }  
}  