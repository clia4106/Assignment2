package invaders.entities;

import invaders.physics.Vector2D;
import javafx.scene.image.Image;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


public class TextPrompt extends BaseEntity  {

    private Map<String,Image> imageMap = new HashMap<>();
    private String imageKey ;
    public TextPrompt(Map<String,String> textMap,Vector2D location,int type,double width,double height){
        super(new String[]{}, location,width,height, null);
        for (String key :textMap.keySet()){
            this.imageKey = key;
            String value = textMap.get(key);
            Image image = new Image(createImage((int) width, (int) height, value, type));
            imageMap.put(key,image);
        }
    }

    private static InputStream createImage(int width, int height, String text, int type){
        try {
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics graphics = bufferedImage.getGraphics();
            graphics.setColor(Color.black);
            graphics.fillRect(0,0,(int)width,(int)height);
            graphics.setColor(Color.white);
            Font font = new Font(null, Font.BOLD, 20);
            graphics.setFont(font);
            int x = 30;
            if(type ==1){
                x =  (width-text.length()*font.getSize())/2;
            }else if(type ==2){
                x = width - 30 -text.length()*font.getSize();
            }
            graphics.drawString(text,x,20);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage,"png",bos);
            ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
            return bis;
        }catch (Exception e){
            e.printStackTrace();
        }
        return  new ByteArrayInputStream(new byte[0]);
    }

    @Override
    public Image getImage() {
        return imageMap.get(this.imageKey);
    }

    public void setImageKey(String imageKey) {
        this.imageKey = imageKey;
    }
}
