package fr.univ_artois.iut_lens.spaceinvader.launcher;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class ConfigurationSaver {
	
	public static final File configFile = new File("space_invader.json");
	
	public LaunchingConfiguration getConfigFromFile() {
		try {
			return new Gson().fromJson(new FileReader(configFile), LaunchingConfiguration.class);
		} catch (JsonSyntaxException | JsonIOException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// rien
		}
		return null;
	}
	
	
	public void saveConfiguration(LaunchingConfiguration config) {
		try {
			FileWriter fw= new FileWriter(configFile, false);
			fw.append(new Gson().toJson(config));
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
}
