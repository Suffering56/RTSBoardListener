package common.entity;

public class FTPResult {

	public boolean hasResult() {
		return hasOnlineResult() || hasPeriodicResult();
	}

	public boolean hasOnlineResult() {
		if (onlineResult != null) {
			return onlineResult.hasResult();
		}
		return false;
	}

	public boolean hasPeriodicResult() {
		if (periodicResult != null) {
			return periodicResult.hasResult();
		}
		return false;
	}

	public boolean hasStaticResult() {
		if (staticResult != null) {
			return staticResult.hasResult();
		}
		return false;
	}

	public ParsingResult getOnlineResult() {
		return onlineResult;
	}

	public void setOnlineResult(ParsingResult onlineResult) {
		this.onlineResult = onlineResult;
	}

	public ParsingResult getPeriodicResult() {
		return periodicResult;
	}

	public void setPeriodicResult(ParsingResult periodicResult) {
		this.periodicResult = periodicResult;
	}

	public ParsingResult getStaticResult() {
		return staticResult;
	}

	public void setStaticResult(ParsingResult staticResult) {
		this.staticResult = staticResult;
	}

	private ParsingResult onlineResult;
	private ParsingResult periodicResult;
	private ParsingResult staticResult;
}
