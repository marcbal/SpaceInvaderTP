package fr.univ_artois.iut_lens.spaceinvader.client;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import fr.univ_artois.iut_lens.spaceinvader.util.Logger;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;


/**
 * A class to handle keyboard input from the user. The class
 * handles both dynamic input during game play, i.e. left/right 
 * and shoot, and more static type input (i.e. press any key to
 * continue)
 * 
 * @author Kevin Glass
 */
public class KeyInputHandler {
	
	
	private Map<String, KeySetting> keyState = new ConcurrentHashMap<>();
	private Map<String, KeySetting> keyToggle = new ConcurrentHashMap<>();
	private Map<String, KeySetting> keyWaitPress = new ConcurrentHashMap<>();
	
	
	
	
	
	public KeyInputHandler() {
		// la configuration des touche se fait ici
		keyState.put("shipLeft", new KeySetting(KeyCode.LEFT));
		keyState.put("shipRight", new KeySetting(KeyCode.RIGHT));
		keyState.put("shipFire", new KeySetting(KeyCode.SPACE));
		
		
		keyToggle.put("pause", new KeySetting(KeyCode.ESCAPE));
		keyToggle.put("infos", new KeySetting(KeyCode.F3));
		
		
		keyWaitPress.put("start", new KeySetting(KeyCode.ENTER));
	
	}
	
	/**
	 * Notification from AWT that a key has been pressed. Note that
	 * a key being pressed is equal to being pushed down but *NOT*
	 * released. Thats where keyTyped() comes in.
	 *
	 * @param e The details of the key that was pressed 
	 */
	public void keyPressed(KeyEvent e) {
		/*
		 * Parcours la liste des configuration de touche et met à true les valeurs correspondante à la touche pressée
		 */
		for (KeySetting k : keyState.values()){
			if (e.getCode() == k.keyCode)
				k.state = 1;
		}
		/*
		 * Parcours la liste des configuration de touche et met change la valeur correspondante à la touche pressée
		 * c'est à dire : true->false ou false->true
		 */
		for (KeySetting k : keyToggle.values()){
			if (e.getCode() == k.keyCode)
				k.state = (k.state==1)?0:1;
		}
		/*
		 * Parcours la liste des configuration de touche et incrémente la valeur correspondante à la touche pressée
		 */
		for (KeySetting k : keyWaitPress.values()){
			if (e.getCode() == k.keyCode)
				k.state++;
		}
	} 
	
	/**
	 * Notification from AWT that a key has been released.
	 *
	 * @param e The details of the key that was released 
	 */
	public void keyReleased(KeyEvent e) {
		/*
		 * Parcours la liste des configuration de touche et met à false les valeurs correspondante à la touche relachée
		 */
		for (KeySetting k : keyState.values()){
			if (e.getCode() == k.keyCode)
				k.state = 0;
		}
	}
	
	
	
	
	
	
	
	
	public boolean isKeyPressed(String action) {
		if (keyState.containsKey(action))
			return keyState.get(action).state==1;
		Logger.severe("La configuration de touche \""+action+"\" n'existe pas dans KeyInputHandler.keyState");
		return false;
	}
	public boolean isKeyToggled(String action) {
		if (keyToggle.containsKey(action))
			return keyToggle.get(action).state==1;
		Logger.severe("La configuration de touche \""+action+"\" n'existe pas dans KeyInputHandler.keyToggle");
		return false;
	}
	public boolean isKeyWaitPress(String action) {
		if (keyWaitPress.containsKey(action)) {
			KeySetting kS = keyWaitPress.get(action);
			if (kS.state > 0) {
				kS.state--;
				return true;
			}
			return false;
		}
		Logger.severe("La configuration de touche \""+action+"\" n'existe pas dans KeyInputHandler.keyWaitPress");
		return false;
	}
	
	
	public void manualToggle(String action, boolean v)
	{
		if (keyToggle.containsKey(action))
			keyToggle.get(action).state= v ? 1 : 0;
	}
	public void manualToggle(String action) {
		if (keyToggle.containsKey(action))
			keyToggle.get(action).state= (keyToggle.get(action).state==1) ? 0 : 1;
	}
	
	
	
	
	private class KeySetting {
		public KeyCode keyCode;
		public int state = 0;
		public KeySetting(KeyCode kCode) {
			keyCode = kCode;
		}
	}
	
	
	
}	
