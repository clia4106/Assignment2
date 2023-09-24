package invaders.status;

import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public interface BunkerStatus {

    public Image getImage();


    default InputStream createBunker(int width, int height,int state){

        try {
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics graphics = bufferedImage.getGraphics();
            Color color =Color.black;
            switch (state){
                case 0 :
                    color = Color.green;
                    break;
                case 1 :
                    color = Color.yellow;
                    break;
                case 2 :
                    color = Color.red;
                    break;
            }
            graphics.setColor(color);
            graphics.fillRect(0,0,width,height/2);
            graphics.fillRect(0,height/2,width/4,height/2);
            graphics.fillRect(width-width/4,height/2,width/4,height/2);

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage,"png",bos);
            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            return bis;
        }catch (Exception e){
            e.printStackTrace();
        }
        return  new ByteArrayInputStream(new byte[0]);
    }
}
