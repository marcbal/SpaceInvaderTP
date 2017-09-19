package fr.univ_artois.iut_lens.spaceinvader.client;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import fr.univ_artois.iut_lens.spaceinvader.util.Logger;


/**
 * A class to handle keyboard input from the user. The class
 * handles both dynamic input during game play, i.e. left/right 
 * and shoot, and more static type input (i.e. press any key to
 * continue)
 * 
 * This has been implemented as an inner class more through 
 * habbit then anything else. Its perfectly normal to implement
 * this as seperate class if slight less convienient.
 * 
 * @author Kevin Glass
 */
public class KeyInputHandler extends KeyAdapter {
	
	
	private Map<String, KeySetting> keyState = new ConcurrentHashMap<>();
	private Map<String, KeySetting> keyToggle = new ConcurrentHashMap<>();
	private Map<String, KeySetting> keyWaitPress = new ConcurrentHashMap<>();
	
	
	
	
	
	public KeyInputHandler() {
		// la configuration des touche se fait ici
		keyState.put("shipLeft", new KeySetting(KeyEvent.VK_LEFT));
		keyState.put("shipRight", new KeySetting(KeyEvent.VK_RIGHT));
		keyState.put("shipFire", new KeySetting(KeyEvent.VK_SPACE));
		
		
		keyToggle.put("pause", new KeySetting(KeyEvent.VK_ESCAPE));
		keyToggle.put("infos", new KeySetting(KeyEvent.VK_F3));
		
		
		keyWaitPress.put("start", new KeySetting(KeyEvent.VK_ENTER));
	
	}
	
	/**
	 * Notification from AWT that a key has been pressed. Note that
	 * a key being pressed is equal to being pushed down but *NOT*
	 * released. Thats where keyTyped() comes in.
	 *
	 * @param e The details of the key that was pressed 
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		/*
		 * Parcours la liste des configuration de touche et met à true les valeurs correspondante à la touche pressée
		 */
		for (Map.Entry<String, KeySetting> k : keyState.entrySet()){
			if (e.getKeyCode() == k.getValue().keyCode)
				k.getValue().state = 1;
		}
		/*
		 * Parcours la liste des configuration de touche et met change la valeur correspondante à la touche pressée
		 * c'est à dire : true->false ou false->true
		 */
		for (Map.Entry<String, KeySetting> k : keyToggle.entrySet()){
			if (e.getKeyCode() == k.getValue().keyCode)
				k.getValue().state = (k.getValue().state==1)?0:1;
		}
		/*
		 * Parcours la liste des configuration de touche et incrémente la valeur correspondante à la touche pressée
		 */
		for (Map.Entry<String, KeySetting> k : keyWaitPress.entrySet()){
			if (e.getKeyCode() == k.getValue().keyCode)
				k.getValue().state++;
		}
	} 
	
	/**
	 * Notification from AWT that a key has been released.
	 *
	 * @param e The details of the key that was released 
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		/*
		 * Parcours la liste des configuration de touche et met à false les valeurs correspondante à la touche relachée
		 */
		for (Map.Entry<String, KeySetting> k : keyState.entrySet()){
			if (e.getKeyCode() == k.getValue().keyCode)
				k.getValue().state = 0;
		}
	}

	/**
	 * Notification from AWT that a key has been typed. Note that
	 * typing a key means to both press and then release it.
	 *
	 * @param e The details of the key that was typed. 
	 */
	@Override
	public void keyTyped(KeyEvent e) {
		
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
		public int keyCode;
		public int state = 0;
		public KeySetting(int kCode) {
			keyCode = kCode;
		}
	}
	
	
	
}	
