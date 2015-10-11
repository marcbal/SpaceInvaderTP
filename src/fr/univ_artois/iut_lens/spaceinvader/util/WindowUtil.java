package fr.univ_artois.iut_lens.spaceinvader.util;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.Window;

public class WindowUtil {
	
	public static void centerWindow (Window fenetre) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();
		
		
		fenetre.setLocation((int)(width/2 - fenetre.getSize().getWidth()/2),
							(int)(height/2 - fenetre.getSize().getHeight()/2));
	}
	
	
	
	
}
