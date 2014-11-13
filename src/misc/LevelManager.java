package misc;

import java.util.ArrayList;
import java.util.List;

import base.Game;

public class LevelManager {
	
	
	private List<Level> levels = new ArrayList<Level>();
	
	private Game game;
	
	private int currentLevel = 0;
	
	
	
	public LevelManager(Game g) {
		game = g;
		
		
		initLevels();
		
	}
	
	
	private void initLevels()
	{

		// génération des niveaux prédéfinis
		levels.add(new LevelAlien(game));
		levels.add(new LevelMarc(game));
		levels.add(new LevelMaxime(game));
		
	}
	
	
	public Level getCurrentLevel()
	{
		return levels.get(currentLevel);
	}
	/*
	 * Passe au niveau suivant
	 * Boucler (réinitialise) le numéro de level si celui-ci dépasse le nombre de level disponible
	 */
	public void goToNextLevel()
	{
		currentLevel++;
		if (currentLevel >= levels.size()) // on a dépassé le nombre de niveau
			goToFirstLevel();
	}
	
	public void goToFirstLevel()
	{
		currentLevel = 0;
		initLevels();
	}
}
