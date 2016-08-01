package common.remote;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;

import common.entity.FTPResult;
import common.entity.ParsingResult;
import common.util.Constants;
import common.util.Logger;
import common.xml.OnlineParser;
import common.xml.PeriodicParser;
import common.xml.StaticParser;

public class FTPHandler {

	public FTPHandler(FTPClient ftp) {
		this.ftp = ftp;
	}

	public FTPResult sendRequest() {
		FTPResult ftpResult = new FTPResult();

		FTPFile onlineFTPFile = getLastFTPFileByName(Constants.ONLINE_MARKET_DATA);
		File onlineXMLFile = createTempFile(onlineFTPFile, Constants.ONLINE_MARKET_DATA);
		if (onlineXMLFile != null) {
			OnlineParser onlineParser = new OnlineParser(onlineXMLFile);
			ParsingResult onlineResult = onlineParser.read();
			ftpResult.setOnlineResult(onlineResult);
		}

		FTPFile periodicFTPFile = getLastFTPFileByName(Constants.PERIODIC_MARKET_DATA);
		File periodicXMLFile = createTempFile(periodicFTPFile, Constants.PERIODIC_MARKET_DATA);
		if (periodicXMLFile != null) {
			PeriodicParser periodicParser = new PeriodicParser(periodicXMLFile);
			ParsingResult periodicResult = periodicParser.read();
			ftpResult.setPeriodicResult(periodicResult);
		}

		return ftpResult;
	}
	
	public FTPResult sendStaticRequest() {
		FTPResult ftpResult = new FTPResult();

		FTPFile staticFTPFile = getLastFTPFileByName(Constants.STATIC_MARKET_DATA);
		File staticXMLFile = createTempFile(staticFTPFile, Constants.STATIC_MARKET_DATA);
		if (staticXMLFile != null) {
			StaticParser staticParser = new StaticParser(staticXMLFile);
			ParsingResult staticResult = staticParser.read();
			ftpResult.setStaticResult(staticResult);
		}

		return ftpResult;
	}

	private File createTempFile(FTPFile ftpFile, String fileNamePrefix) {
		if (ftpFile != null) {
			try {
				File tempFile = new File(fileNamePrefix + ".xml");
				OutputStream out = (new FileOutputStream(tempFile));

				Logger.info("FTP. Downloading file: " + ftpFile.getName() + " ...");
				boolean isSuccess = ftp.retrieveFile(ftpFile.getName(), out);

				if (isSuccess) {
					Logger.info("FTP->IO. File has been successfully recieved! [" + tempFile.getAbsolutePath() + "]");
					return tempFile;
				} else {
					Logger.error("FTP->IO. Error retrieving file! [" + ftpFile.getName() + "]");
					return null;
				}
			} catch (IOException e) {
				Logger.error("FTP->IO. Error retrieving file! [" + ftpFile.getName() + "]", e);
				return null;
			}
		} else {
			return null;
		}
	}

	private FTPFile getLastFTPFileByName(String filename) {
		FTPFile lastFile = null;
		try {
			for (FTPFile file : ftp.listFiles()) {
				if (file.getName().contains(filename)) {
					lastFile = file;
				}
			}
			if (lastFile != null) {
				Logger.info("FTP. Last file name successfully extracted! [" + lastFile.getName() + "] ");
			} else {
				Logger.error("FTP. Catalog <" + ftp.printWorkingDirectory() + "> not contains " + filename);
			}
			return lastFile;
		} catch (IOException e) {
			Logger.error("FTP. Extraction last FTPFile failed", e);
			return null;
		}
	}

	private final FTPClient ftp;
}
