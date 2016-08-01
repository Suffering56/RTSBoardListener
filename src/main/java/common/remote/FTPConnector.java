package common.remote;

import java.io.IOException;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import common.util.Constants;
import common.util.FTPConfig;
import common.util.Logger;

public class FTPConnector {

	public FTPClient connect() {
		FTPConfig config = FTPConfig.getInstance();
		try {
			ftp = new FTPClient();
			ftp.connect(config.getHost(), config.getPort());
			
			if (ftp.login(config.getLogin(), config.getPassword())) {
				Logger.info("FTP. Successfully connected");
			} else {
				throw new IOException("Failed to login");
			}

			/**
			 * Древняя магия. Помню, что без нее не всегда работает.
			 */
			ftp.enterLocalPassiveMode();

			selectWorkingDirectory(Constants.ROOT_FTP_DIRECTORY);
			String dir = extractLastDirectoryName();
			selectWorkingDirectory(dir);

			return ftp;

		} catch (IOException e) {
			Logger.error("FTP. Failed to connect", e);
			disconnect();
			return null;
		}
	}

	public void disconnect() {
		try {
			if (ftp != null) {
				ftp.disconnect();
			}
			Logger.info("FTP. Successfully disconnected");
		} catch (IOException e) {
			Logger.error("FTP. Failed to disconnect", e);
		} finally {
			ftp = null;
		}
	}

	/**
	 * Установка активной директории на FTP-сервере.
	 */
	public boolean selectWorkingDirectory(String path) throws IOException {
		try {
			boolean exist = ftp.changeWorkingDirectory(path);
			if (exist) {
				Logger.info("FTP. ChangeWorkingDirectory SUCCESS! [Current Path: " + ftp.printWorkingDirectory() + "]");
			} else {
				Logger.error("FTP. ChangeWorkingDirectory ERROR! [Error Path: " + ftp.printWorkingDirectory() + "/" + path + "] [Current Path: " + ftp.printWorkingDirectory() + "]");
			}
			return exist;
		} catch (IOException e) {
			Logger.error("FTP. ChangeWorkingDirectory ERROR! [Error Path: " + ftp.printWorkingDirectory() + "/" + path + "] [Current Path: " + ftp.printWorkingDirectory() + "]", e);
			return false;
		}
	}

	/**
	 * Извлекаем имя рабочей директории из текущей даты.
	 */
	private String extractLastDirectoryName() {
		try {
			FTPFile[] dirs = ftp.listDirectories();
			if (dirs.length > 0) {
				int n = dirs.length - 1;
				FTPFile lastDir = dirs[n];
				return lastDir.getName();
			} else {
				Logger.info("FTP. Catalog <" + ftp.printWorkingDirectory() + "> not contains DIRECTORIES!");
				return "NULL_DIRECTORY";
			}
		} catch (IOException e) {
			Logger.error("FTP. Extraction last directory name failed", e);
			return "NULL_DIRECTORY";
		}
	}

	public static FTPConnector getInstance() {
		if (instance == null) {
			instance = new FTPConnector();
		}
		return instance;
	}

	private FTPConnector() {
		/**
		 * Private constructor.
		 * Need for Singleton.
		 */
	}

	private static FTPConnector instance = null;
	private FTPClient ftp;
}
