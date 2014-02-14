/*
 * Author:  Joshua Parker
 *
 * PixelColour class
 * Gets and sets each colour
 * and outputs a string with the given values
 *
*/

public class PixelColour {
	private static final int LARGEST_COLOUR_VALUE = 256;
	private int red, blue, green;

//---------------------------------------------------------------------
// Constructors
//---------------------------------------------------------------------
	public PixelColour(){
		this.red = red;
		this.green = green;
		this.blue = blue;
	}
//---------------------------------------------------------------------
// Get and set methods
//---------------------------------------------------------------------
	public PixelColour(int r, int g, int b){
		setRed(r);
		setGreen(g);
		setBlue(b);
	}

	public int getRed(){
		return red;
	}

	public int getGreen(){
		return green;
	}

	public int getBlue(){
		return blue;
	}

	public void setRed(int r){
		red = r;
		if(red < 0){
			red = 0;
		}else if(red > 256){
			red -= 256;
		}
	}

	public void setGreen(int g){
		green = g;
		if(green < 0){
			green = 0;
		}else if(green > 256){
			green -= 256;
		}
	}

	public void setBlue(int b){
		blue = b;
		if(blue < 0){
			blue = 0;
		}else if(blue > 256){
			blue -= 256;
		}
	}
//---------------------------------------------------------------------
// toString() method
//---------------------------------------------------------------------
	public String toString(){
		return "RGB: (" + red + "," + green + "," + blue + ")";
	}
}