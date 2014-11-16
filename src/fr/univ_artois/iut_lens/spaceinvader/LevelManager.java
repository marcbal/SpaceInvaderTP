package fr.univ_artois.iut_lens.spaceinvader;

import java.util.ArrayList;
import java.util.List;

import fr.univ_artois.iut_lens.spaceinvader.levels.Level;
import fr.univ_artois.iut_lens.spaceinvader.levels.LevelAlien;
import fr.univ_artois.iut_lens.spaceinvader.levels.LevelAlien2;
import fr.univ_artois.iut_lens.spaceinvader.levels.LevelAlien3;
import fr.univ_artois.iut_lens.spaceinvader.levels.LevelBossPremier;
import fr.univ_artois.iut_lens.spaceinvader.levels.LevelMarc;
import fr.univ_artois.iut_lens.spaceinvader.levels.LevelMaxime;

public class LevelManager {
	
	
	private List<Level> levels = new ArrayList<Level>();
	
	private EntitiesManager entitiesManager;
	
	private int currentLevel = 4;
	
	
	
	public LevelManager(EntitiesManager eM) {
		entitiesManager = eM;
		
		
		initLevels();
		
	}
	
	
	private void initLevels()
	{
		levels.clear();
		// génération des niveaux prédéfinis
		levels.add(new LevelAlien(entitiesManager));
		levels.add(new LevelMarc(entitiesManager));
		levels.add(new LevelMaxime(entitiesManager));
		levels.add(new LevelBossPremier(entitiesManager));
		levels.add(new LevelAlien2(entitiesManager));
		levels.add(new LevelAlien3(entitiesManager));
		
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
