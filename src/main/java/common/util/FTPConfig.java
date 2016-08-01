package common.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class FTPConfig {

	private FTPConfig() {
		loadProperties();
	}

	private void loadProperties() {
		try {
			Properties property = new Properties();
			property.load(new FileInputStream(Constants.PROPERTIES));
			host = property.getProperty("ftp.host");
			port = Integer.valueOf(property.getProperty("ftp.port"));
			login = property.getProperty("ftp.login");
			password = property.getProperty("ftp.password");
			
			Logger.info("IO. File <" + Constants.PROPERTIES + "> successfully loaded");
		} catch (IOException e) {
			Logger.error("IO. File <" + Constants.PROPERTIES + "> is not found", e);
		}
	}

	public String getHost() {
		return host;
	}

	public int getPort() {
		return port;
	}

	public String getLogin() {
		return login;
	}

	public String getPassword() {
		return password;
	}

	public static FTPConfig getInstance() {
		if (instance == null) {
			instance = new FTPConfig();
		}
		return instance;
	}

	private String host;
	private int port;
	private String login;
	private String password;
	private static FTPConfig instance = null;
}
