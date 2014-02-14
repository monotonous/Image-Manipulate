/*
 * Author:  Joshua Parker
 *
 * This class contains static
 * methods for managing a
 * BufferedImage object
 *
*/

import java.awt.image.*;
import javax.imageio.*;
import java.io.*;
import java.awt.*;

public class ManageBImage {											
//---------------------------------------------------------------------
// Returns the BufferedImage read from the file
//---------------------------------------------------------------------
	public static BufferedImage getBufferedImageFromFile(String fileName) {	
		BufferedImage bi = null;		
		File file = new File(fileName);

		try {
			bi = javax.imageio.ImageIO.read(file);
		} catch (IOException e) {
			System.out.println(e);
		}

		return bi;
	}
//---------------------------------------------------------------------
// Get 2D array of PixelColour objects from the BufferedImage
//---------------------------------------------------------------------
	public static PixelColour[][] get2DArrayFromBufferedImage(BufferedImage bi) {
		Dimension dimImage = getBufferedImageDimensions(bi);
		int numberOfRows = dimImage.height;
		int numberOfCols = dimImage.width;
				
		int[] imagePixels = get1DArrayFromBufferedImage(bi, numberOfRows, numberOfCols);
		PixelColour[][] imagePixels2D = get2DArrayFrom1DArray(imagePixels, numberOfRows, numberOfCols);
		
		return imagePixels2D;
	}
	
	private static int[] get1DArrayFromBufferedImage(BufferedImage bi, int numberOfRows, int numberOfCols) {		
		int startX = 0;
		int startY = 0;
		int offset = 0;	
		int scansize = numberOfCols;
		int imageWidth = numberOfCols;
		int imageHeight = numberOfRows;
		
		int[] originalImagePixels = new int[numberOfRows * numberOfCols];
			
		originalImagePixels = bi.getRGB(startX, startY, imageWidth, imageHeight, originalImagePixels, offset, scansize);
		
		return originalImagePixels;
	}
	
	private static PixelColour[][] get2DArrayFrom1DArray(int[] pixels, int numberOfRows, int numberOfCols) {
		 	PixelColour[][] currentPixels2D = new PixelColour[numberOfRows][numberOfCols];
			int startX = 0;
			int startY = 0;
			int offset = 0;
			int scansize = numberOfCols;
			int rgBValue; 

			for(int x=0; x<numberOfRows; x++)
				for(int y=0; y<numberOfCols; y++) {
					currentPixels2D[x][y] = new PixelColour();
					rgBValue = pixels[offset + (x - startX) * scansize + (y - startY)];
					currentPixels2D[x][y].setRed((rgBValue >> 16) & 0xFF); 
					currentPixels2D[x][y].setGreen((rgBValue >> 8) & 0xFF); 
					currentPixels2D[x][y].setBlue(rgBValue & 0xFF); 
				}

			return currentPixels2D;
		}
		
//-------------------------------------------------------------------------
// Create a BufferedImage from the colours of the 2D array
//-------------------------------------------------------------------------	
	public static BufferedImage getBufferedImageFrom2DArray(PixelColour[][] currentPixels2D) {
		int rgbValue;
		int red, green, blue;
		int numberOfRows = currentPixels2D.length;
		int numberOfCols = currentPixels2D[0].length;
		
		BufferedImage bi = new BufferedImage(numberOfCols, numberOfRows, BufferedImage.TYPE_INT_RGB);

		for (int row=0; row<numberOfRows; row++)		
			for (int col=0; col<numberOfCols; col++) {
				red = currentPixels2D[row][col].getRed();
				green = currentPixels2D[row][col].getGreen();
				blue = currentPixels2D[row][col].getBlue();
				rgbValue = getRGBInSingleIntValue(red, green, blue);
				bi.setRGB(col, row, rgbValue);
			}

		return bi;
	}
	
//---------------------------------------------------------------------
// Sets the seperate r g b values to a single rgb int value
//---------------------------------------------------------------------
	private static int getRGBInSingleIntValue(int r, int g, int b) {
		int rgbValue = r << 16;
		rgbValue = rgbValue + (g << 8);
		rgbValue = rgbValue + b;
		return rgbValue;		
	}
	
//---------------------------------------------------------------------
// Gets the dimensions of the BufferedImage object
//---------------------------------------------------------------------
	public static Dimension getBufferedImageDimensions(BufferedImage bi) {	
		Dimension dim = new Dimension(bi.getWidth(), bi.getHeight());	
		return dim;
	}
}
