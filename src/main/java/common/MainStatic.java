package common;

import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.net.ftp.FTPClient;

import common.entity.FTPResult;
import common.remote.FTPConnector;
import common.remote.FTPHandler;
import common.remote.IDServerSender;
import common.util.Constants;
import common.util.Logger;

public class MainStatic {
	
	public static void main(String[] args) {
		FTPConnector ftpConnector = FTPConnector.getInstance();
		FTPClient ftp = ftpConnector.connect();

		System.out.println();

		FTPHandler fptHelper = new FTPHandler(ftp);
		FTPResult ftpResult = fptHelper.sendStaticRequest();
		ftpConnector.disconnect();

		System.out.println();

		if (ftpResult.hasStaticResult()) {
			sendData(ftpResult);

			System.out.println();

			saveInstrumentSet(ftpResult);
		} else {
			Logger.info("Nothing to send!");
		}

		System.out.println();
		Main.sleep();
		Logger.info("Exit.");
	}

	private static void sendData(FTPResult ftpResult) {
		IDServerSender sender = new IDServerSender(ftpResult);
		sender.sendStatic();
	}
	
	private static void saveInstrumentSet(FTPResult ftpResult) {
		Logger.info("IO. Saving <" + Constants.STATIC_INSTRUMENT_SET + "> ...");

		Set<String> instrumentSet = new TreeSet<>();
		instrumentSet.addAll(ftpResult.getStaticResult().getInstrumentSet());
		
		Main.ioSave(instrumentSet, Constants.STATIC_INSTRUMENT_SET);
	}
}
