package common.xml;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import common.entity.ParsingResult;
import common.util.Constants;
import common.util.Logger;

public class PeriodicParser extends AbstractParser {

	public PeriodicParser(File xmlFile) {
		super(xmlFile);
	}

	/**
	 * Чтение всех item-ов XML-документа.
	 */
	public ParsingResult read() {
		return super.read(Constants.PERIODIC_MARKET_DATA);
	}

	/**
	 * Обработка содержимого item-ов
	 */
	protected void iterateItemContent(Element item) {
		String name = extractItemName(item);

		if (!name.equals(nullName)) {
			NodeList tagsList = item.getElementsByTagName(Constants.TRADE);
			for (int i = 0; i < tagsList.getLength(); i++) {
				if (tagsList.item(i) instanceof Element) {
					Element trade = (Element) tagsList.item(i);

					extractTradeDateTime(name, trade);
					iterateTradeTags(name, trade);
				}
			}
		}
	}

	private void extractTradeDateTime(String name, Element trade) {
		String dateTime = trade.getAttribute(Constants.DATE_TIME);
		Matcher m = p.matcher(dateTime);
		if (m.matches()) {
			String date = m.group(4) + "/" + m.group(3) + "/" + m.group(2);
			String time = m.group(5) + ":" + m.group(6) + ":" + m.group(7);
			result.add(name, Constants.FIELD_DATE, date);
			result.add(name, Constants.FIELD_TIME, time);
		} else {
			Logger.error("Невозможно конвертировать dateTime. Строка <" + dateTime + "> не соответствует шаблону <yyyy-MM-ddТHH:mm:ss.milliseconds>.");
		}
	}

	private void iterateTradeTags(String name, Element trade) {
		NodeList tradeTags = trade.getChildNodes();

		for (int i = 0; i < tradeTags.getLength(); i++) {
			if (tradeTags.item(i) instanceof Element) {
				Element tradeAttr = (Element) tradeTags.item(i);
				if (tradeAttr.getTagName().equals(Constants.VLAST)) {
					result.add(name, Constants.FIELD_VLAST, tradeAttr.getTextContent());
				} else if (tradeAttr.getTagName().equals(Constants.LAST)) {
					result.add(name, Constants.FIELD_LAST, tradeAttr.getTextContent());
				}
			}
		}
	}

	private static final Pattern p = Pattern.compile("(\\d{2})(\\d{2})-(\\d+)-(\\d+)T(\\d+):(\\d+):(\\d+)\\.(\\d+)");
}
