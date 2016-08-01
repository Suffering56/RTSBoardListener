package common.xml;

import java.io.File;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import common.entity.ParsingResult;
import common.util.Constants;

public class StaticParser extends AbstractParser {

	public StaticParser(File xmlFile) {
		super(xmlFile);
	}

	/**
	 * Чтение всех item-ов XML-документа.
	 */
	public ParsingResult read() {
		return super.read(Constants.STATIC_MARKET_DATA);
	}

	/**
	 * Обработка содержимого item-тегов
	 */
	protected void iterateItemContent(Element item) {
		NodeList securityList = item.getElementsByTagName(Constants.SECURITY);
		for (int i = 0; i < securityList.getLength(); i++) {
			if (securityList.item(i) instanceof Element) {
				Element security = (Element) securityList.item(i);
				interateSecurityContent(security);
			}
		}
	}

	/**
	 * Обработка содержимого security-тегов
	 */
	private void interateSecurityContent(Element security) {
		String name = extractItemName(security);

		NodeList identifiersList = security.getElementsByTagName(Constants.IDENTIFIERS);
		for (int i = 0; i < identifiersList.getLength(); i++) {
			if (identifiersList.item(i) instanceof Element) {
				Element identifier = (Element) identifiersList.item(i);
				interateIdentifiersContent(identifier, name);
			}
		}

		NodeList descriptionList = security.getElementsByTagName(Constants.DESCRIPTION);
		for (int i = 0; i < descriptionList.getLength(); i++) {
			if (descriptionList.item(i) instanceof Element) {
				Element description = (Element) descriptionList.item(i);
				result.add(name, Constants.FIELD_DESCRIPTION, description.getTextContent());
			}
		}
	}

	/**
	 * Обработка содержимого identifiers-тегов
	 */
	private void interateIdentifiersContent(Element identifier, String name) {
		NodeList isinList = identifier.getElementsByTagName(Constants.ISIN);
		for (int i = 0; i < isinList.getLength(); i++) {
			if (isinList.item(i) instanceof Element) {
				Element isin = (Element) isinList.item(i);
				result.add(name, Constants.FIELD_ISIN, isin.getTextContent());
			}
		}
	}
}
