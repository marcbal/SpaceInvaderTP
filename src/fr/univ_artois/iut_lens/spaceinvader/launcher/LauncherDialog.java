package fr.univ_artois.iut_lens.spaceinvader.launcher;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import fr.univ_artois.iut_lens.spaceinvader.MegaSpaceInvader;
import fr.univ_artois.iut_lens.spaceinvader.server.Server;
import fr.univ_artois.iut_lens.spaceinvader.sprites_manager.SpriteStore;
import fr.univ_artois.iut_lens.spaceinvader.util.WindowUtil;

import javax.swing.JLabel;
import javax.swing.JTextField;

import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.net.InetAddress;
import java.text.ParseException;
import java.util.Random;
import java.awt.event.ActionEvent;
import javax.swing.JTabbedPane;
import javax.swing.JSpinner;
import javax.swing.SwingConstants;
import javax.swing.SpinnerNumberModel;
import javax.swing.JCheckBox;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

@SuppressWarnings("serial")
public class LauncherDialog extends JFrame {
	private JCheckBox chckbxLancerLeClient;
	private JTextField textFieldServerAddr;
	private JTextField textFieldPlayerName;
	private JCheckBox chckbxLancerLeServeur;
	private JCheckBox chckbxServerScoring;
	private JSpinner spinnerServerPort;


	/**
	 * Create the dialog.
	 */
	public LauncherDialog() {
		setIconImage(SpriteStore.get().getSprite("sprites/ComplexShot.png").image);
		setResizable(false);
		
		setDefaultCloseOperation(JDialog.EXIT_ON_CLOSE);
		setTitle("Launcher - Mega Space Invader");
		setSize(350, 286);
		WindowUtil.centerWindow(this);
		getContentPane().setLayout(new BorderLayout());
		{
			JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
			getContentPane().add(tabbedPane, BorderLayout.CENTER);
			{
				JPanel clientPanel = new JPanel();
				clientPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
				tabbedPane.addTab("Client", null, clientPanel, null);
				GridBagLayout gbl_clientPanel = new GridBagLayout();
				gbl_clientPanel.columnWidths = new int[] {142, 182, 0};
				gbl_clientPanel.rowHeights = new int[] {25, 30, 30, 0};
				gbl_clientPanel.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
				gbl_clientPanel.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
				clientPanel.setLayout(gbl_clientPanel);
				{
					chckbxLancerLeClient = new JCheckBox("Lancer le client");
					chckbxLancerLeClient.setSelected(true);
					chckbxLancerLeClient.addItemListener((event) -> {
						if (event.getStateChange() == ItemEvent.SELECTED)
							enableInternalClient();
						else
							disableInternalClient();
					});
					GridBagConstraints gbc_chckbxLancerLeClient = new GridBagConstraints();
					gbc_chckbxLancerLeClient.gridwidth = 2;
					gbc_chckbxLancerLeClient.insets = new Insets(0, 0, 5, 0);
					gbc_chckbxLancerLeClient.fill = GridBagConstraints.BOTH;
					gbc_chckbxLancerLeClient.gridx = 0;
					gbc_chckbxLancerLeClient.gridy = 0;
					clientPanel.add(chckbxLancerLeClient, gbc_chckbxLancerLeClient);
				}
				{
					JLabel lblServerAddr = new JLabel("Adresse du serveur");
					lblServerAddr.setHorizontalAlignment(SwingConstants.LEFT);
					GridBagConstraints gbc_lblServerAddr = new GridBagConstraints();
					gbc_lblServerAddr.fill = GridBagConstraints.BOTH;
					gbc_lblServerAddr.anchor = GridBagConstraints.WEST;
					gbc_lblServerAddr.insets = new Insets(0, 0, 5, 5);
					gbc_lblServerAddr.gridx = 0;
					gbc_lblServerAddr.gridy = 1;
					clientPanel.add(lblServerAddr, gbc_lblServerAddr);
				}
				{
					textFieldServerAddr = new JTextField();
					textFieldServerAddr.setText("localhost:"+MegaSpaceInvader.SERVER_DEFAULT_PORT);
					textFieldServerAddr.setEnabled(false); // par défaut, c'est le serveur local. Il faut le désactiver pour configurer un serveur distant.
					textFieldServerAddr.setToolTipText("Ce champ n'est accessible que si le serveur interne est désactivé.");
					GridBagConstraints gbc_textFieldServerAddr = new GridBagConstraints();
					gbc_textFieldServerAddr.insets = new Insets(0, 0, 5, 0);
					gbc_textFieldServerAddr.fill = GridBagConstraints.BOTH;
					gbc_textFieldServerAddr.gridx = 1;
					gbc_textFieldServerAddr.gridy = 1;
					clientPanel.add(textFieldServerAddr, gbc_textFieldServerAddr);
					textFieldServerAddr.setColumns(10);
				}
				{
					JLabel lblNewLabel = new JLabel("Nom du joueur");
					lblNewLabel.setHorizontalAlignment(SwingConstants.LEFT);
					GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
					gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
					gbc_lblNewLabel.fill = GridBagConstraints.BOTH;
					gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
					gbc_lblNewLabel.gridx = 0;
					gbc_lblNewLabel.gridy = 2;
					clientPanel.add(lblNewLabel, gbc_lblNewLabel);
				}
				{
					textFieldPlayerName = new JTextField();
					textFieldPlayerName.setText(randomString(3, LETTERS)+randomString(2, NUMBERS));
					GridBagConstraints gbc_textFieldPlayerName = new GridBagConstraints();
					gbc_textFieldPlayerName.insets = new Insets(0, 0, 5, 0);
					gbc_textFieldPlayerName.fill = GridBagConstraints.BOTH;
					gbc_textFieldPlayerName.gridx = 1;
					gbc_textFieldPlayerName.gridy = 2;
					clientPanel.add(textFieldPlayerName, gbc_textFieldPlayerName);
					textFieldPlayerName.setColumns(10);
				}
			}
			{
				JPanel serverPanel = new JPanel();
				tabbedPane.addTab("Serveur", null, serverPanel, null);
				serverPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
				GridBagLayout gbl_serverPanel = new GridBagLayout();
				gbl_serverPanel.columnWidths = new int[] {142, 182, 0};
				gbl_serverPanel.rowHeights = new int[] {25, 30, 30, 30};
				gbl_serverPanel.columnWeights = new double[]{1.0, 1.0, Double.MIN_VALUE};
				gbl_serverPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 1.0};
				serverPanel.setLayout(gbl_serverPanel);
				{
					chckbxLancerLeServeur = new JCheckBox("Lancer le serveur local");
					chckbxLancerLeServeur.setSelected(true);
					chckbxLancerLeServeur.addItemListener((event) -> {
						if (event.getStateChange() == ItemEvent.SELECTED)
							enableInternalServer();
						else
							disableInternalServer();
					});
					GridBagConstraints gbc_chckbxLancerLeServeur = new GridBagConstraints();
					gbc_chckbxLancerLeServeur.gridwidth = 2;
					gbc_chckbxLancerLeServeur.fill = GridBagConstraints.BOTH;
					gbc_chckbxLancerLeServeur.insets = new Insets(0, 0, 5, 0);
					gbc_chckbxLancerLeServeur.gridx = 0;
					gbc_chckbxLancerLeServeur.gridy = 0;
					serverPanel.add(chckbxLancerLeServeur, gbc_chckbxLancerLeServeur);
				}
				{
					JLabel lblSvPort = new JLabel("Port d'écoute");
					lblSvPort.setHorizontalAlignment(SwingConstants.LEFT);
					GridBagConstraints gbc_lblSvPort = new GridBagConstraints();
					gbc_lblSvPort.fill = GridBagConstraints.BOTH;
					gbc_lblSvPort.insets = new Insets(0, 0, 5, 5);
					gbc_lblSvPort.gridx = 0;
					gbc_lblSvPort.gridy = 1;
					serverPanel.add(lblSvPort, gbc_lblSvPort);
				}
				{
					spinnerServerPort = new JSpinner();
					spinnerServerPort.setModel(new SpinnerNumberModel(MegaSpaceInvader.SERVER_DEFAULT_PORT, 1, 65535, 1));
					spinnerServerPort.addChangeListener((event) -> {
						changeServerPortValue();
					});
					GridBagConstraints gbc_spinnerServerPort = new GridBagConstraints();
					gbc_spinnerServerPort.insets = new Insets(0, 0, 5, 0);
					gbc_spinnerServerPort.fill = GridBagConstraints.BOTH;
					gbc_spinnerServerPort.gridx = 1;
					gbc_spinnerServerPort.gridy = 1;
					serverPanel.add(spinnerServerPort, gbc_spinnerServerPort);
				}
				{
					chckbxServerScoring = new JCheckBox("Activer le scoring");
					chckbxServerScoring.setSelected(false);
					GridBagConstraints gbc_chckbxLancerLeServeur = new GridBagConstraints();
					gbc_chckbxLancerLeServeur.gridwidth = 2;
					gbc_chckbxLancerLeServeur.fill = GridBagConstraints.BOTH;
					gbc_chckbxLancerLeServeur.insets = new Insets(0, 0, 5, 0);
					gbc_chckbxLancerLeServeur.gridx = 0;
					gbc_chckbxLancerLeServeur.gridy = 2;
					serverPanel.add(chckbxServerScoring, gbc_chckbxLancerLeServeur);
				}
				{
					JScrollPane scrollPane = new JScrollPane();
					scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
					GridBagConstraints gbc_scrollPane = new GridBagConstraints();
					gbc_scrollPane.gridwidth = 2;
					gbc_scrollPane.fill = GridBagConstraints.BOTH;
					gbc_scrollPane.gridx = 0;
					gbc_scrollPane.gridy = 3;
					serverPanel.add(scrollPane, gbc_scrollPane);
					{
						
						String IPAdressesList = "<html><b>Liste des adresses IP de cet ordinateur</b><br/>";
						for (InetAddress addr : Server.getAllNetworkInterfacesAddress()) {
							IPAdressesList += addr+"<br/>";
						}
						IPAdressesList += "</html>";
						
						
						JLabel lblNewLabel_1 = new JLabel(IPAdressesList);
						lblNewLabel_1.setVerticalAlignment(SwingConstants.TOP);
						lblNewLabel_1.setHorizontalAlignment(SwingConstants.LEFT);
						scrollPane.setViewportView(lblNewLabel_1);
					}
				}
					
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				JButton okButton = new JButton("Lancer");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						dispose();
					}
				});
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Fermer");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent arg0) {
						System.exit(0);
					}
				});
				buttonPane.add(cancelButton);
			}
		}
		
		setVisible(true);
	}
	
	
	
	
	private void disableInternalServer() {
		if (chckbxLancerLeClient.isSelected())
			textFieldServerAddr.setEnabled(true);
		spinnerServerPort.setEnabled(false);
	}
	
	private void enableInternalServer() {
		textFieldServerAddr.setEnabled(false);
		spinnerServerPort.setEnabled(true);
		textFieldServerAddr.setText("localhost:"+spinnerServerPort.getValue());
	}
	
	private void changeServerPortValue() {
		try {
			spinnerServerPort.commitEdit();
		} catch (ParseException e) {
			spinnerServerPort.setBackground(new Color(255, 240, 240));
			return;
		}
		spinnerServerPort.setBackground(Color.WHITE);
		textFieldServerAddr.setText("localhost:"+spinnerServerPort.getValue());
	}
	
	private void disableInternalClient() {
		textFieldServerAddr.setEnabled(false);
		textFieldPlayerName.setEnabled(false);
	}
	
	private void enableInternalClient() {
		if (!chckbxLancerLeServeur.isSelected())
			textFieldServerAddr.setEnabled(true);
		textFieldPlayerName.setEnabled(true);
	}
	
	
	
	
	
	
	public LaunchingConfiguration generateConfig() {
		LaunchingConfiguration config = new LaunchingConfiguration();
		config.clientEnabled = chckbxLancerLeClient.isSelected();
		if (config.clientEnabled) {
			config.clientConnectionAddress = textFieldServerAddr.getText();
			config.clientPlayerName = textFieldPlayerName.getText().substring(0, Math.min(50, textFieldPlayerName.getText().length()));
		}
		config.serverEnabled = chckbxLancerLeServeur.isSelected();
		if (config.serverEnabled) {
			config.serverPort = (Integer) spinnerServerPort.getValue();
			config.serverScoring = chckbxServerScoring.isSelected();
		}
		
		
		return config;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Override
	public synchronized void dispose() {
		super.dispose();
		notify();
	}
	
	public synchronized void waitForDispose() {
		try {
			wait();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	/*
	 * Source : http://stackoverflow.com/a/157202
	 */
	private static final String LETTERS = "azertyuiopmlkjhgfdsqwxcvbn";
	private static final String NUMBERS = "0123456789";
	private static Random rnd = MegaSpaceInvader.RANDOM;

	public static String randomString( int len , String chars){
	   StringBuilder sb = new StringBuilder( len );
	   for( int i = 0; i < len; i++ ) 
	      sb.append( chars.charAt( rnd.nextInt(chars.length()) ) );
	   return sb.toString();
	}
	// -----------------------------------
	
	
	
	
	public class LaunchingConfiguration {
		public boolean serverEnabled;
		public int serverPort;
		public boolean serverScoring;
		public boolean clientEnabled;
		public String clientConnectionAddress;
		public String clientPlayerName;
	}

}
