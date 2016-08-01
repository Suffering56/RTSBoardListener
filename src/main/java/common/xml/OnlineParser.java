package common.xml;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import common.entity.ParsingResult;
import common.util.Constants;
import common.util.Logger;

public class OnlineParser extends AbstractParser {

	public OnlineParser(File xmlFile) {
		super(xmlFile);
	}

	/**
	 * Чтение всех item-ов XML-документа.
	 * Для обработки каждого item-а используется
	 * метод: iterateItemTags(Element item)
	 */
	public ParsingResult read() {
		return super.read(Constants.ONLINE_MARKET_DATA);
	}

	/**
	 * Обработка содержимого item-тегов
	 */
	protected void iterateItemContent(Element item) {
		String name = extractItemName(item);
		Set<String> directionSet = new HashSet<String>();

		if (!name.equals(nullName)) {
			NodeList tagsList = item.getElementsByTagName(Constants.QUOTE);
			for (int i = 0; i < tagsList.getLength(); i++) {
				if (tagsList.item(i) instanceof Element) {
					Element quote = (Element) tagsList.item(i);
					
					String direction = iterateQuoteContent(name, quote);
					if (!direction.equals(nullName)) {
						directionSet.add(direction);
					}
				}
			}
		}
		checkDirections(name, directionSet);
	}

	/**
	 * Обработка содержимого quote-тегов
	 */
	private String iterateQuoteContent(String name, Element quote) {
		NodeList quoteContent = quote.getChildNodes();
		String direction = extractDirection(quote);

		if (direction.equals(nullName)) {
			return nullName;
		}

		for (int i = 0; i < quoteContent.getLength(); i++) {
			if (quoteContent.item(i) instanceof Element) {
				Element quoteAttr = (Element) quoteContent.item(i);
				if (quoteAttr.getTagName().equals(Constants.VSIZE)) {
					result.add(name, Constants.V + direction, quoteAttr.getTextContent());
				} else if (quoteAttr.getTagName().equals(Constants.PRICE)) {
					result.add(name, direction, quoteAttr.getTextContent());
				}
			}
		}

		return direction;
	}

	protected String extractDirection(Element qouteAttr) {
		String direction = nullName;
		NodeList dirList = qouteAttr.getElementsByTagName(Constants.DIRECTION);
		if (dirList.getLength() > 0) {
			direction = dirList.item(0).getTextContent();
		}
		return direction;
	}

	private void checkDirections(String name, Set<String> directionSet) {
		switch (directionSet.size()) {
		case 1: {
			if (!directionSet.contains(Constants.ASK)) {
				result.add(name, Constants.ASK, Constants.NULL);
				result.add(name, Constants.V + Constants.ASK, Constants.NULL);
			} else {
				result.add(name, Constants.BID, Constants.NULL);
				result.add(name, Constants.V + Constants.BID, Constants.NULL);
			}
			return;
		}
		case 2: {
			return;
		}
		default:
			Logger.error("Unknown problem with <direction> tag. Count 0 or 3+. InstrumentCode: " + name);
			return;
		}
	}

}
