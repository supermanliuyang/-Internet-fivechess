package util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;


public class GameUtil {

	private GameUtil(){}
	public static Image getImage(String path) {
		
		BufferedImage img = null;
		try {
			URL u = GameUtil.class.getClassLoader().getResource(path);
			img = ImageIO.read(u);
		} catch (IOException e) {
			e.printStackTrace();
		}	
		return img;
	}
}
 