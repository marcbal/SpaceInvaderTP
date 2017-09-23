package fr.univ_artois.iut_lens.spaceinvader.launcher;

import java.net.InetAddress;
import java.util.Random;
import java.util.stream.Collectors;

import fr.univ_artois.iut_lens.spaceinvader.MegaSpaceInvader;
import fr.univ_artois.iut_lens.spaceinvader.server.Server;
import javafx.geometry.HPos;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TabPane.TabClosingPolicy;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class LauncherDialog extends Scene {
	private CheckBox chckbxRunClient;
	private TextField textFieldServerAddr;
	private TextField textFieldPlayerName;
	private CheckBox chckbxRunServer;
	private CheckBox chckbxServerScoring;
	private Spinner<Integer> spinnerServerPort;
	
	private Stage stage;


	/**
	 * Create the dialog.
	 */
	public LauncherDialog(Stage primaryStage) {
		super(new BorderPane());
		stage = primaryStage;
		stage.setScene(this);
		stage.setTitle("Launcher - Mega Space Invader");

		stage.setWidth(350);
		stage.setHeight(286);
		stage.centerOnScreen();
		
		stage.setOnCloseRequest(event -> {
			if (event.getEventType() == WindowEvent.WINDOW_CLOSE_REQUEST) {
				System.exit(0);
			}
		});
		
		
		// CLIENT TAB
		
		// tab elements
		chckbxRunClient = new CheckBox("Lancer le client");
		chckbxRunClient.setSelected(true);
		chckbxRunClient.selectedProperty().addListener((ob, oldV, newV) -> {
			if (newV)
				enableInternalClient();
			else
				disableInternalClient();
		});
		Label lblServerAddr = new Label("Adresse du serveur");
		textFieldServerAddr = new TextField("localhost:"+MegaSpaceInvader.SERVER_DEFAULT_PORT);
		textFieldServerAddr.setDisable(true); // par défaut, c'est le serveur local. Il faut le désactiver pour configurer un serveur distant.
		textFieldServerAddr.setTooltip(new Tooltip("Ce champ n'est accessible que si le serveur interne est désactivé."));
		Label lblPlayerName = new Label("Nom du joueur");
		textFieldPlayerName = new TextField(randomString(3, LETTERS)+randomString(2, NUMBERS));
		
		// tab content structure
		GridPane tabClientContainer = new GridPane();
		tabClientContainer.setBorder(new Border(new BorderStroke(Color.TRANSPARENT, null, null, new BorderWidths(5))));
		tabClientContainer.getColumnConstraints().add(new ColumnConstraints(120, 120, 120, Priority.NEVER, HPos.LEFT, true));
		tabClientContainer.getColumnConstraints().add(new ColumnConstraints(200, 250, 250, Priority.ALWAYS, HPos.LEFT, true));
		tabClientContainer.add(chckbxRunClient, 0, 0, 2, 1);
		tabClientContainer.add(lblServerAddr, 0, 1);
		tabClientContainer.add(textFieldServerAddr, 1, 1);
		tabClientContainer.add(lblPlayerName, 0, 2);
		tabClientContainer.add(textFieldPlayerName, 1, 2);
		Tab tabClient = new Tab("Client", tabClientContainer);
		// end CLIENT TAB
		
		
		
		
		
		
		// SERVER TAB

		// tab elements
		chckbxRunServer = new CheckBox("Lancer le serveur local");
		chckbxRunServer.setSelected(true);
		chckbxRunServer.selectedProperty().addListener((ob, oldV, newV) -> {
			if (newV)
				enableInternalServer();
			else
				disableInternalServer();
		});
		Label lblServerPort = new Label("Post d'écoute");
		spinnerServerPort = new Spinner<>(1, 65535, MegaSpaceInvader.SERVER_DEFAULT_PORT);
		spinnerServerPort.valueProperty().addListener((obj, oldV, newV) -> {
			changeServerPortValue(newV);
		});
		chckbxServerScoring = new CheckBox("Activer le scoring");
		chckbxServerScoring.setSelected(false);
		Label lblAddress = new Label("Liste des adresses IP de cet ordinateur :");
		ScrollPane scrollAddress = new ScrollPane(new Label(String.join("\n",
				Server.getAllNetworkInterfacesAddress().stream()
						.map(InetAddress::toString)
						.collect(Collectors.toList())
				)));

		// tab content structure
		GridPane tabServerContainer = new GridPane();
		tabServerContainer.setBorder(new Border(new BorderStroke(Color.TRANSPARENT, null, null, new BorderWidths(5))));
		tabServerContainer.getColumnConstraints().add(new ColumnConstraints(120, 120, 120, Priority.NEVER, HPos.LEFT, true));
		tabServerContainer.getColumnConstraints().add(new ColumnConstraints(200, 250, 250, Priority.ALWAYS, HPos.LEFT, true));
		tabServerContainer.add(chckbxRunServer, 0, 0, 2, 1);
		tabServerContainer.add(lblServerPort, 0, 1);
		tabServerContainer.add(spinnerServerPort, 1, 1);
		tabServerContainer.add(chckbxServerScoring, 0, 2, 2, 1);
		tabServerContainer.add(lblAddress, 0, 3, 2, 1);
		tabServerContainer.add(scrollAddress, 0, 4, 2, 1);
		Tab tabServer = new Tab("Serveur", tabServerContainer);
		// end SERVER TAB
		
		
		
		
		// global tab structure
		TabPane tabbedPane = new TabPane(tabClient, tabServer);
		tabbedPane.setTabClosingPolicy(TabClosingPolicy.UNAVAILABLE);
		
		
		
		// START/CLOSE BUTTONS
		
		Button okButton = new Button("Lancer");
		okButton.setOnAction(event -> {
			stage.hide();
			MegaSpaceInvader.afterLauncherUI(getLaunchConfig());
		});
		okButton.setDefaultButton(true);
		
		
		Button closeButton = new Button("Fermer");
		closeButton.setOnAction(event -> {
			System.exit(0);
		});
		closeButton.setCancelButton(true);
		
		FlowPane buttonPane = new FlowPane(Orientation.HORIZONTAL, 5, 0, okButton, closeButton);
		buttonPane.setAlignment(Pos.CENTER_RIGHT);
		buttonPane.setBorder(new Border(new BorderStroke(Color.TRANSPARENT, null, null, new BorderWidths(5))));
		
		// end START/CLOSE BUTTONS
		
		
		
		
		BorderPane rootPane = (BorderPane) getRoot();
		rootPane.setCenter(tabbedPane);
		rootPane.setBottom(buttonPane);
		
		
		
		stage.show();
		
	}
	
	
	
	
	private void disableInternalServer() {
		if (chckbxRunClient.isSelected())
			textFieldServerAddr.setDisable(false);
		spinnerServerPort.setDisable(true);
		chckbxServerScoring.setDisable(true);
	}
	
	private void enableInternalServer() {
		textFieldServerAddr.setDisable(true);
		spinnerServerPort.setDisable(false);
		textFieldServerAddr.setText("localhost:"+spinnerServerPort.getValue());
		chckbxServerScoring.setDisable(false);
	}
	
	private void changeServerPortValue(int newValue) {
		textFieldServerAddr.setText("localhost:"+newValue);
	}
	
	private void disableInternalClient() {
		textFieldServerAddr.setDisable(true);
		textFieldPlayerName.setDisable(true);
	}
	
	private void enableInternalClient() {
		if (!chckbxRunServer.isSelected())
			textFieldServerAddr.setDisable(false);
		textFieldPlayerName.setDisable(false);
	}
	
	
	
	
	
	
	private LaunchingConfiguration getLaunchConfig() {
		LaunchingConfiguration config = new LaunchingConfiguration();
		config.clientEnabled = chckbxRunClient.isSelected();
		if (config.clientEnabled) {
			config.clientConnectionAddress = textFieldServerAddr.getText();
			config.clientPlayerName = textFieldPlayerName.getText().substring(0, Math.min(50, textFieldPlayerName.getText().length()));
		}
		config.serverEnabled = chckbxRunServer.isSelected();
		if (config.serverEnabled) {
			config.serverPort = spinnerServerPort.getValue();
			config.serverScoring = chckbxServerScoring.isSelected();
		}
		
		
		return config;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	/*
	 * Source : http://stackoverflow.com/a/157202
	 */
	static final String LETTERS = "azertyuiopmlkjhgfdsqwxcvbn";
	static final String NUMBERS = "0123456789";
	private static Random rnd = MegaSpaceInvader.RANDOM;

	public static String randomString( int len , String chars){
	   StringBuilder sb = new StringBuilder( len );
	   for( int i = 0; i < len; i++ ) 
	      sb.append( chars.charAt( rnd.nextInt(chars.length()) ) );
	   return sb.toString();
	}
	// -----------------------------------




	public void applyConfiguration(LaunchingConfiguration savedConfig) {
		
		if (chckbxRunClient.isSelected() != savedConfig.clientEnabled)
			chckbxRunClient.setSelected(savedConfig.clientEnabled);
		if (savedConfig.clientEnabled) {
			textFieldPlayerName.setText(savedConfig.clientPlayerName);
			if (!savedConfig.serverEnabled)
				textFieldServerAddr.setText(savedConfig.clientConnectionAddress);
		}
		
		if (chckbxRunServer.isSelected() != savedConfig.serverEnabled)
			chckbxRunServer.setSelected(savedConfig.serverEnabled);
		if (savedConfig.serverEnabled) {
			chckbxServerScoring.setSelected(savedConfig.serverScoring);
			spinnerServerPort.getValueFactory().setValue(savedConfig.serverPort);
		}
	}

}
