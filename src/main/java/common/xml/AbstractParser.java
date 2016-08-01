package common.xml;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import common.entity.ParsingResult;
import common.util.Constants;
import common.util.Logger;

public abstract class AbstractParser {

	public AbstractParser(File xmlFile) {
		this.xmlFile = xmlFile;
		init();
	}

	/**
	 * Инициализация всех объектов необходимых, для дальнейшей работы с XML.
	 */
	public void init() {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			XPathFactory xpfactory = XPathFactory.newInstance();
			xPath = xpfactory.newXPath();
			doc = builder.parse(xmlFile);
			result = new ParsingResult();
		} catch (SAXException | IOException | ParserConfigurationException e) {
			Logger.error("AbstractParser.init()", e);
		}
	}

	/**
	 * Чтение всех item-ов XML-документа.
	 * Для обработки каждого item-а используется
	 * метод: iterateItemTags(Element item)
	 * 
	 * @param expression
	 *            - название root-тега
	 */
	protected ParsingResult read(String expression) {
		try {
			Element root = (Element) xPath.evaluate(expression, doc, XPathConstants.NODE);
			NodeList itemList = root.getElementsByTagName(Constants.ITEM);

			for (int i = 0; i < itemList.getLength(); i++) {
				if (itemList.item(i) instanceof Element) {
					Element item = (Element) itemList.item(i);
					iterateItemContent(item);
				}
			}
			return result;
		} catch (XPathExpressionException e) {
			Logger.error("Error while reading XML-document: " + xmlFile.getName(), e);
			result.clear();
			return result;
		}
	}

	/**
	 * Обработка содержимого item-ов
	 */
	abstract protected void iterateItemContent(Element item);

	/**
	 * Получение имени item-а из тега "instrumentCode"
	 */
	protected String extractItemName(Element item) {
		String name = nullName;

		NodeList nameList = item.getElementsByTagName(Constants.INSTRUMENT_CODE);
		if (nameList.getLength() > 0) {
			name = nameList.item(0).getTextContent();
		}
		
		if (!name.equals(nullName)) {
			result.addInstrument(name);
		}
		
		return name;
	}

	protected XPath xPath;
	protected Document doc;
	protected File xmlFile;

	protected ParsingResult result;
	protected static final String nullName = "NULL_NAME";
}
