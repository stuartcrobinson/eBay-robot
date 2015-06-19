package helloworld;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
 
public class ImageDownloader
{      
    public static void main(String[] args )
    {
        BufferedImage image =null;
        try{
 
            URL url =new URL("http://i.ebayimg.com/00/s/MTAwMFg3NTA=/z/SZIAAOSwDk5UF46x/$_57.JPG");
            // read the url
           image = ImageIO.read(url);
 
//            //for png
//            ImageIO.write(image, "png",new File("/tmp/have_a_question.png"));
 
            // for jpg
            ImageIO.write(image, "jpg",new File("c:\\temp\\EBAYDELETEME.jpg"));
 
        }catch(IOException e){
            e.printStackTrace();
        }
    }}