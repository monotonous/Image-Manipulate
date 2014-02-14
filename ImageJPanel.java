/*
 * Author:  Joshua Parker
 *
 * This JPanel object
 * is used for displaying
 * the BufferedImage object.
 *
*/

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;

public class ImageJPanel extends JPanel {
	private BufferedImage image; 	//Reference to image being displayed

//---------------------------------------------------------------------
// Set the image for the JPanel along with background colour
//---------------------------------------------------------------------
	public ImageJPanel(BufferedImage image) {
		this.image = image;
		setBackground(Color.LIGHT_GRAY);
	}

//---------------------------------------------------------------------
// Set the BufferedImage which is to be displayed
//---------------------------------------------------------------------
	public void setImage(BufferedImage image) {
		this.image = image;
	}

//---------------------------------------------------------------------
// Draw the BufferedImage in the JPanel
//---------------------------------------------------------------------
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(image, 0, 0, null);
	}

//---------------------------------------------------------------------
// Set ImageJPanel preferred size
//---------------------------------------------------------------------
	public void setPreferredSize(Dimension dim) {
		super.setPreferredSize(dim);
	}
}
