package ru.project.wtf.system.testloader;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import ru.project.wtf.system.utils.FileUtils;

@Service
public class TestLoaderImpl implements TestLoader {

	private static final String IMAGE_MARKER = "image-name";
	private static final String QUESTION_BLOCK_MARKER = "question-block";
	private static final String QUESTION_MARKER = "question";
	private static final String VARIANT_MARKER = "variant";
	private static final String ANSWER_MARKER = "answer";

	@Override
	public Test load(final String directory, final String fileName) {
		final File file = FileUtils.convertStreamToFile(directory, fileName);
		if (!file.exists()) {
			throw new IllegalArgumentException(
					String.format("File not exists with name = %s in directory = %s!", directory, fileName));
		}

		if (!file.canRead()) {
			throw new IllegalArgumentException(
					String.format("File is not readable with name = %s in directory = %s!", directory, fileName));
		}

		final List<Question> questions = new LinkedList<>();
		try {
			final DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			final DocumentBuilder builder = dbFactory.newDocumentBuilder();
			final Document doc = builder.parse(file);

			doc.getDocumentElement().normalize();
			final NodeList nodes = doc.getElementsByTagName(QUESTION_BLOCK_MARKER);
			for (int i = 0; i < nodes.getLength(); i++) {
				final Node node = nodes.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					final Element elem = (Element) node;
					final String question = elem.getElementsByTagName(QUESTION_MARKER).item(0).getTextContent();
					final String answer = elem.getElementsByTagName(ANSWER_MARKER).item(0).getTextContent();
					final List<File> images = new LinkedList<>();
					final List<String> variants = new LinkedList<>();

					final NodeList imageNodes = elem.getElementsByTagName(IMAGE_MARKER);
					for (int j = 0; j < imageNodes.getLength(); j++) {
						final Node imageNode = imageNodes.item(j);
						images.add(FileUtils.convertStreamToFile(directory, imageNode.getTextContent()));
					}

					final NodeList variantNodes = elem.getElementsByTagName(VARIANT_MARKER);
					for (int j = 0; j < variantNodes.getLength(); j++) {
						final Node variantNode = variantNodes.item(j);
						variants.add(variantNode.getTextContent());
					}

					questions.add(new Question(question, answer, variants, images));
				}
			}

			return new Test(file, questions);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void replace(File file) {
		// TODO Auto-generated method stub

	}

}
