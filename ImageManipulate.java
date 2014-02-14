/*
 * Author:  Joshua Parker
 *
 * Added ability for user to pass their own image as input
 * or use the original image if no input is given
*/

import javax.swing.*;
import java.io.*;

public class ImageManipulate {
//---------------------------------------------------------------------
// Main method which creates an instance of the class
// The filename of the image should be used as an argument passed to
// JFrame (default is "Im06Cat.bmp" or any bmp file of your choice)
//---------------------------------------------------------------------
	public static void main(String[] args) {
		JFrame frame;
		String defaultImage = "Im06Cat.bmp";
		try{
			System.out.println("Image " + args[0] + " used as input");
			frame = new ImageManipulateJFrame(args[0], "Image Manipulation by Joshua Parker", 100, 120, 500, 500);
		}catch(Exception e){
			File f = new File(defaultImage);
			if(f.exists()){
				System.out.println("No image given or image unreadable using default image");
				frame = new ImageManipulateJFrame(defaultImage, "Image Manipulation by Joshua Parker", 100, 120, 500, 500);
			}else{
				System.err.println("Default image " + defaultImage + " missing please specify another image or replace "
							+ defaultImage + "\nQuitting...");
				System.exit(1);
			}
		}
	}
}