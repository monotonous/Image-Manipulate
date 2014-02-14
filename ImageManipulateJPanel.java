/*
 * Author:  Joshua Parker
 *
 * The program can vertically and horizontally flip the given image
 * Turn the given image to gray scale
 * Swap the red and blue values of the image
 * Turns the image all red, green or blue
 * The user can save and restore the image to its saved state or the original state
 * Custom buttonAB makes image appear negative
 * Custom buttonBB turns the red values of the image to a random value
*/

import java.awt.image.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ImageManipulateJPanel extends JPanel implements ActionListener {
	private static final int RED = 0;
	private static final int GREEN = 1;
	private static final int BLUE = 2;

	private ImageManipulateJFrame mainJFrame; 	//Reference to main JFrame object
												//Use this object to set the size of the JFrame
												//once the size of the image is known.
	private JPanel imageJPanel;
	private BufferedImage currentBI = null; 	//The image buffer.
	private int numberOfRows, numberOfCols;

	//Arrays for storing the RGB colour of each pixel
	private PixelColour[][] originalsPixels2D = null;
	private PixelColour[][] lastSavedPixels2D = null;
	private PixelColour[][] currentPixels2D = null;
	private PixelColour[][] userChangedPixels2D = null;

	private Color panelsBGroundColour = Color.MAGENTA;
	private JTextField commentT;
	private JPanel topJPanel;
	private JPanel bottomJPanel;
	private JButton flipHorizontallyB, flipVerticallyB, swapBlueToRedB;
	private JButton redB, greenB, blueB, changeToGreyB;
	private JButton revertToLastSavedB, saveB, revertToOriginalB;
	private JButton buttonAB, buttonBB;

//---------------------------------------------------------------------
// Constructor
//---------------------------------------------------------------------
	public ImageManipulateJPanel(ImageManipulateJFrame frame, String fileName) {
		int imageWidth, imageHeight;
		Dimension dimImage, dimTopJPanel, dimBottomJPanel;
		mainJFrame = frame;

		setLayout(new BorderLayout());

		// Create top and bottom panels
		topJPanel = createTopJPanel();
		add(topJPanel, BorderLayout.NORTH);

		bottomJPanel = createBottomJPanel();
		add(bottomJPanel, BorderLayout.SOUTH);

		// Read the bmp image file and create the imageJPanel
		currentBI = ManageBImage.getBufferedImageFromFile(fileName);
		imageJPanel = new ImageJPanel(currentBI);
		add(imageJPanel, BorderLayout.CENTER);

		// Get the numberOfRows and numberOfCols from the image file
		// and set up the sizes of the components and the JFrame
		dimImage = ManageBImage.getBufferedImageDimensions(currentBI);

		dimTopJPanel = topJPanel.getPreferredSize();
		dimBottomJPanel = bottomJPanel.getPreferredSize();
		setUpComponentSizes(dimTopJPanel, dimBottomJPanel, dimImage, imageJPanel);

		// get the RGB values from the BufferedImage object and store then into a two-dimensional array
		originalsPixels2D = ManageBImage.get2DArrayFromBufferedImage(currentBI);

		// Set up the two independant copies of the 2D array of PixelColour objects
		currentPixels2D = makeAFullCopy(originalsPixels2D);
		lastSavedPixels2D = makeAFullCopy(originalsPixels2D);
	}
//---------------------------------------------------------------------
// Handles button presses by the user
//---------------------------------------------------------------------
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == flipVerticallyB) {
			commentT.setText("Vertical flip");
			currentPixels2D = flipVertically(currentPixels2D);
		} else if (e.getSource() == flipHorizontallyB) {
			commentT.setText("Horizontal flip");
			currentPixels2D = flipHorizontally(currentPixels2D);
		} else if (e.getSource() == swapBlueToRedB) {
			commentT.setText("Swap the blues and reds");
			currentPixels2D = swapBlueRedInTwoDimArray(currentPixels2D);
		} else if (e.getSource() == revertToOriginalB) {
			commentT.setText("Revert to original image");
			currentPixels2D = makeAFullCopy(originalsPixels2D);
		} else if (e.getSource() == saveB) {
			commentT.setText("Save the current image");
			lastSavedPixels2D = makeAFullCopy(currentPixels2D);
		} else if (e.getSource() == revertToLastSavedB) {
			commentT.setText("Revert to last saved image");
			currentPixels2D = makeAFullCopy(lastSavedPixels2D);
		} else if (e.getSource() == changeToGreyB) {
			commentT.setText("Change r, g, b to average of the three colours");
			currentPixels2D = changeColourToGrey(currentPixels2D);
		} else if (e.getSource() == buttonAB) {
			commentT.setText("Gives the image an inverted look");
			currentPixels2D = doButtonAStuff(currentPixels2D);
		} else if (e.getSource() == buttonBB) {
			commentT.setText("Sets red values in the image to a random value");
			currentPixels2D = doButtonBStuff(currentPixels2D);
		} else if (e.getSource() == redB) {
			commentT.setText("Show just red colour");
			currentPixels2D = showOneColourOnly(currentPixels2D, RED);
		} else if (e.getSource() == greenB) {
			commentT.setText("Show just green colour");
			currentPixels2D = showOneColourOnly(currentPixels2D, GREEN);
		} else if (e.getSource() == blueB) {
			commentT.setText("Show just blue colour");
			currentPixels2D = showOneColourOnly(currentPixels2D, BLUE);
		}

		// get the BufferedImage which corresponds to the 2D array
		// and display it in the imageJPanel
		currentBI = ManageBImage.getBufferedImageFrom2DArray(currentPixels2D);
		((ImageJPanel)imageJPanel).setImage(currentBI);

		repaint();
	}

//---------------------------------------------------------------------
// Flip 2D array vertically
//---------------------------------------------------------------------
	private PixelColour[][] flipVertically(PixelColour[][] currentPixels2D) {
		PixelColour[] temp;
		for(int row = 0; row < currentPixels2D.length / 2; row++){
			temp = currentPixels2D[currentPixels2D.length - row - 1];
			currentPixels2D[currentPixels2D.length - row - 1] = currentPixels2D[row];
			currentPixels2D[row] = temp;
		}
		return currentPixels2D;
	}
	
//---------------------------------------------------------------------
// Flip 2D array horizontally
//---------------------------------------------------------------------
	private PixelColour[][] flipHorizontally(PixelColour[][] currentPixels2D) {
		PixelColour temp;
		for(int row = 0; row < currentPixels2D.length; row++)
			for(int col = 0; col < currentPixels2D[row].length / 2; col++){
				temp = currentPixels2D[row][col];
				currentPixels2D[row][col] = currentPixels2D[row][currentPixels2D[row].length - col - 1];
				currentPixels2D[row][currentPixels2D[row].length - col - 1] = temp;
			}

		return currentPixels2D;
	}
	
//-------------------------------------------------------------------------
// Change blueness to redness of the PixelColour objects in the 2D array
//-------------------------------------------------------------------------
	private PixelColour[][] swapBlueRedInTwoDimArray(PixelColour[][] currentPixels2D) {
		numberOfRows = currentPixels2D.length;
		numberOfCols = currentPixels2D[0].length;

		for(int rows = 0; rows < numberOfRows; rows++)
			for(int cols = 0; cols < numberOfCols; cols++){
				int blue = currentPixels2D[rows][cols].getBlue();
				int red = currentPixels2D[rows][cols].getRed();
				currentPixels2D[rows][cols].setBlue(red);
				currentPixels2D[rows][cols].setRed(blue);
			}

		return currentPixels2D;
	}
	
//---------------------------------------------------------------------
// Make a full copy of a 2D array of PixelColour objects
//---------------------------------------------------------------------
	private PixelColour[][] makeAFullCopy(PixelColour[][] currentPixels2D) {
		PixelColour[][] copy = new PixelColour[currentPixels2D.length][];

		for(int rows = 0; rows < currentPixels2D.length; rows++){
			copy[rows] = new PixelColour[currentPixels2D[rows].length];
			for(int cols = 0; cols < currentPixels2D[rows].length; cols++){
				copy[rows][cols] = currentPixels2D[rows][cols];
				int red = currentPixels2D[rows][cols].getRed();
				int green = currentPixels2D[rows][cols].getGreen();
				int blue = currentPixels2D[rows][cols].getBlue();
				copy[rows][cols] = new PixelColour(red, green, blue);
			}
		}

		return copy;
	}
	
//-----------------------------------------------------------------------------
// Change r, g, b of each element to the average of the three colours
//-----------------------------------------------------------------------------
	private PixelColour[][] changeColourToGrey(PixelColour[][] currentPixels2D) {
		int numberOfRows = currentPixels2D.length;
		int numberOfCols = currentPixels2D[0].length;
		int temp, red, green, blue;
		for(int rows = 0; rows < numberOfRows; rows++)
			for(int cols = 0; cols < numberOfCols; cols++){
				blue = currentPixels2D[rows][cols].getBlue();
				red = currentPixels2D[rows][cols].getRed();
				green = currentPixels2D[rows][cols].getGreen();
				temp = (red + blue + green) / 3;
				currentPixels2D[rows][cols].setGreen(temp);
				currentPixels2D[rows][cols].setBlue(temp);
				currentPixels2D[rows][cols].setRed(temp);
			}

		return currentPixels2D;
	}
//-------------------------------------------------------------------------
// Complete the helper method which gives the functionality for button A
//-------------------------------------------------------------------------
	private PixelColour[][] doButtonAStuff(PixelColour[][] currentPixels2D) {
		int numberOfRows = currentPixels2D.length;
		int numberOfCols = currentPixels2D[0].length;
		int temp, red, green, blue;
		for(int rows = 0; rows < numberOfRows; rows++)
			for(int cols = 0; cols < numberOfCols; cols++){
				blue = currentPixels2D[rows][cols].getBlue();
				red = currentPixels2D[rows][cols].getRed();
				green = currentPixels2D[rows][cols].getGreen();
				currentPixels2D[rows][cols].setGreen(red);
				currentPixels2D[rows][cols].setBlue(red);
				currentPixels2D[rows][cols].setRed(green);
			}
			
		return currentPixels2D;
	}
	
//---------------------------------------------------------------------
// Complete the helper method which gives the functionality for button B
//---------------------------------------------------------------------
	private PixelColour[][] doButtonBStuff(PixelColour[][] currentPixels2D) {
		int numberOfRows = currentPixels2D.length;
		int numberOfCols = currentPixels2D[0].length;
		
		for(int rows = 0; rows < numberOfRows; rows++)
			for(int cols = 0; cols < numberOfCols; cols++)
				currentPixels2D[rows][cols].setRed((int)(Math.random() * 256));

		return currentPixels2D;
	}
	
//---------------------------------------------------------------------
// Sets two of the colours in each element of the 2D array to zero
//---------------------------------------------------------------------
	private PixelColour[][] showOneColourOnly(PixelColour[][] currentPixels2D, int colour) {
		int numberOfRows = currentPixels2D.length;
		int numberOfCols = currentPixels2D[0].length;

		for(int x=0; x<numberOfRows; x++)
			for(int y=0; y<numberOfCols; y++)
				setTwoColoursToZero(colour, currentPixels2D[x][y]);
		
		return currentPixels2D;
	}
	
	private void setTwoColoursToZero(int colour, PixelColour c) {
		if (colour == RED) {
			c.setGreen(0);
			c.setBlue(0);
		} else 	if (colour == GREEN) {
			c.setRed(0);
			c.setBlue(0);
		} else {
			c.setRed(0);
			c.setGreen(0);
		}
	}
	
//---------------------------------------------------------------------
// Create the top JPanel of components
//---------------------------------------------------------------------
	private JPanel createTopJPanel() {
		JPanel topJPanel = new JPanel();

		commentT = new JTextField(30);
		revertToOriginalB = new JButton("REVERT_TO_ORIGINAL");
		saveB = new JButton("SAVE");
		revertToLastSavedB = new JButton("REVERT_TO_SAVED");

		commentT.addActionListener(this);
		saveB.addActionListener(this);
		revertToOriginalB.addActionListener(this);
		revertToLastSavedB.addActionListener(this);

		topJPanel.add(commentT);
		topJPanel.add(revertToOriginalB);
		topJPanel.add(saveB);
		topJPanel.add(revertToLastSavedB);

		topJPanel.setBackground(panelsBGroundColour);

		return topJPanel;
	}
//---------------------------------------------------------------------
// Create bottom JPanel of components
//---------------------------------------------------------------------
	private JPanel createBottomJPanel() {
		JPanel bottomJPanel = new JPanel();

		flipHorizontallyB = new JButton("FLIP_H");
		flipVerticallyB = new JButton("FLIP_V");
		swapBlueToRedB = new JButton("BLUE<->RED");
		changeToGreyB = new JButton("GREY");
		buttonAB = new JButton("BUTTON A");
		redB = new JButton("RED");
		greenB = new JButton("GREEN");
		blueB = new JButton("BLUE");

		buttonBB = new JButton("BUTTON B");

		flipHorizontallyB.addActionListener(this);
		flipVerticallyB.addActionListener(this);
		buttonAB.addActionListener(this);
		swapBlueToRedB.addActionListener(this);
		redB.addActionListener(this);
		greenB.addActionListener(this);
		blueB.addActionListener(this);
		changeToGreyB.addActionListener(this);
		buttonBB.addActionListener(this);

		bottomJPanel.add(flipVerticallyB);
		bottomJPanel.add(flipHorizontallyB);
		bottomJPanel.add(swapBlueToRedB);
		bottomJPanel.add(changeToGreyB);
		bottomJPanel.add(buttonAB);
		bottomJPanel.add(buttonBB);
		bottomJPanel.add(redB);
		bottomJPanel.add(greenB);
		bottomJPanel.add(blueB);

		bottomJPanel.setBackground(panelsBGroundColour);

		return bottomJPanel;
	}
//---------------------------------------------------------------------
// Set up all the component sizes
//---------------------------------------------------------------------
	private void setUpComponentSizes(Dimension dimUpperJPanel, Dimension dimLowerJPanel, Dimension dimImage, JPanel imageJPanel) {
		final int VERTICAL_TOTAL_GAP = 40;
		final int HORIZONTAL_TOTAL_GAP = 10;

		int maxWidth = Math.max(dimImage.width, Math.max(dimUpperJPanel.width, dimLowerJPanel.width));
		int totalHeight = dimImage.height + dimUpperJPanel.height + dimLowerJPanel.height;
		mainJFrame.setPreferredSize(new Dimension(maxWidth + HORIZONTAL_TOTAL_GAP, totalHeight + VERTICAL_TOTAL_GAP));
		setPreferredSize(new Dimension(maxWidth, totalHeight));
		imageJPanel.setPreferredSize(dimImage);
	}
//---------------------------------------------------------------------
// Set JPanel preferred size
//---------------------------------------------------------------------
	public void setPreferredSize(Dimension dim) {
		super.setPreferredSize(dim);
	}
}