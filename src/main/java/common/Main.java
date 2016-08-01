package common;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.net.ftp.FTPClient;

import common.entity.FTPResult;
import common.remote.FTPConnector;
import common.remote.FTPHandler;
import common.remote.IDServerSender;
import common.util.Constants;
import common.util.Logger;

public class Main {
	public static void main(String[] args) {
		FTPConnector ftpConnector = FTPConnector.getInstance();
		FTPClient ftp = ftpConnector.connect();

		System.out.println();

		FTPHandler fptHelper = new FTPHandler(ftp);
		FTPResult ftpResult = fptHelper.sendRequest();
		ftpConnector.disconnect();

		System.out.println();

		if (ftpResult.hasResult()) {
			sendData(ftpResult);

			System.out.println();

			saveInstrumentSet(ftpResult);
		} else {
			Logger.info("Nothing to send!");
		}

		System.out.println();
		sleep();
		Logger.info("Exit.");
	}

	public static void sleep() {
		Logger.info("Thread.sleep(10000) ...");
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			Logger.error("Thread.sleep error.", e);
		}
	}

	private static void sendData(FTPResult ftpResult) {
		IDServerSender sender = new IDServerSender(ftpResult);
		sender.send();
	}

	private static void saveInstrumentSet(FTPResult ftpResult) {
		Logger.info("IO. Saving <" + Constants.INSTRUMENT_SET + "> ...");

		Set<String> instrumentSet = new TreeSet<>();
		instrumentSet.addAll(ftpResult.getOnlineResult().getInstrumentSet());
		instrumentSet.addAll(ftpResult.getPeriodicResult().getInstrumentSet());
		
		ioSave(instrumentSet, Constants.INSTRUMENT_SET);
	}

	public static void ioSave(Set<String> instrumentSet, String fileName) {
		try (FileWriter out = new FileWriter(new File(fileName))) {
			for (String str : instrumentSet) {
				out.write(str + "\r\n");
			}
			Logger.info("IO. File <" + fileName + "> saved successfully");
		} catch (IOException e) {
			Logger.error("IO. Error when saving <" + fileName + ">", e);
		}
	}
}
