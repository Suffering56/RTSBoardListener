package common.remote;

import common.entity.FTPResult;
import common.entity.SenderEntity;
import common.util.Logger;
import p.ds.outputers.DServerOutput;

public class IDServerSender {

	public IDServerSender(FTPResult ftpResult) {
		this.ftpResult = ftpResult;
		session = new DServerOutput();
		Logger.info("IDServer successfully connected");
	}

	private void sendOnlineData() {
		Logger.info("Sending onlineMarketData...");

		for (SenderEntity entity : ftpResult.getOnlineResult().getResultList()) {
			sendData(entity.getField(), entity.getName(), entity.getValue());
		}

		Logger.info("onlineMarketData is successfully sent!");
	}

	private void sendPeriodicData() {
		Logger.info("Sending periodicMarketData...");

		for (SenderEntity entity : ftpResult.getPeriodicResult().getResultList()) {
			sendData(entity.getField(), entity.getName(), entity.getValue());
		}

		Logger.info("periodicMarketData is successfully sent!");
	}
	
	private void sendStaticData() {
		Logger.info("Sending staticMarketData...");

		for (SenderEntity entity : ftpResult.getStaticResult().getResultList()) {
			sendData(entity.getField(), entity.getName(), entity.getValue());
		}

		Logger.info("staticMarketData is successfully sent!");
	}

	private void sendData(String field, String name, String value) {
		session.sendData(field, name, value);
	}

	public void send() {
		if (ftpResult.hasOnlineResult()) {
			sendOnlineData();
		} else {
			Logger.info("EMPTY onlineMarketData!");
		}
		if (ftpResult.hasPeriodicResult()) {
			sendPeriodicData();
		} else {
			Logger.info("EMPTY periodicMarketData!");
		}
	}

	public void sendStatic() {
		if (ftpResult.hasStaticResult()) {
			sendStaticData();
		} else {
			Logger.info("EMPTY staticMarketData!");
		}
	}

	private final FTPResult ftpResult;
	private final DServerOutput session;
}
