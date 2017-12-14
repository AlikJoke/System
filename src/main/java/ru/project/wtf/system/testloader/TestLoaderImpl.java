package ru.project.wtf.system.testloader;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import ru.project.wtf.system.external.HasExternalSourceAbstract;
import ru.project.wtf.system.utils.FileUtils;

@Service
public class TestLoaderImpl extends HasExternalSourceAbstract implements TestLoader {

	private static final String IMAGE_MARKER = "image-name";
	private static final String QUESTION_BLOCK_MARKER = "question-block";
	private static final String QUESTION_MARKER = "question";
	private static final String VARIANT_MARKER = "variant";
	private static final String COMPLEXITY_MARKER = "complexity";
	private static final String FIRST_TEST_TIME_MARKER = "first-test-time";
	private static final String SECOND_TEST_TIME_MARKER = "second-test-time";
	private static final String FIRST_TEST_QUESTIONS_COUNT_MARKER = "first-test-questions-count";
	private static final String SECOND_TEST_QUESTIONS_COUNT_MARKER = "second-test-questions-count";
	private static final String ANSWER_MARKER = "answer";

	private static final String ID_ATTRIBUTE = "id";

	@Override
	public Test load(final String directory, final String fileName) {
		final File file;
		File newFile = new File(fileName);
		if (newFile.exists() && newFile.canRead()) {
			file = newFile;
		} else {
			if (newFile != null) {
				newFile.delete();
			}
			file = FileUtils.convertStreamToFile(directory, fileName);
			file.deleteOnExit();
		}

		if (!file.exists()) {
			throw new IllegalArgumentException(
					String.format("File not exists with name = %s in directory = %s!", directory, fileName));
		}

		if (!file.canRead()) {
			throw new IllegalArgumentException(
					String.format("File is not readable with name = %s in directory = %s!", directory, fileName));
		}

		if (!FileUtils.XML_EXTENSION.equalsIgnoreCase(FilenameUtils.getExtension(fileName))) {
			throw new IllegalArgumentException(
					String.format("File isn't valid xml file. Filename = %s in directory = %s!", directory, fileName));
		}

		final List<Question> questions = new LinkedList<>();
		try {
			final DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			final DocumentBuilder builder = dbFactory.newDocumentBuilder();
			final Document doc = builder.parse(file);

			doc.getDocumentElement().normalize();
			final Integer firstTestTime = Integer
					.parseInt(doc.getElementsByTagName(FIRST_TEST_TIME_MARKER).item(0).getTextContent());
			final Integer secondTestTime = Integer
					.parseInt(doc.getElementsByTagName(SECOND_TEST_TIME_MARKER).item(0).getTextContent());
			final Integer firstTestQuestionsCount = Integer
					.parseInt(doc.getElementsByTagName(FIRST_TEST_QUESTIONS_COUNT_MARKER).item(0).getTextContent());
			final Integer secondTestQuestionsCount = Integer
					.parseInt(doc.getElementsByTagName(SECOND_TEST_QUESTIONS_COUNT_MARKER).item(0).getTextContent());

			final NodeList nodes = doc.getElementsByTagName(QUESTION_BLOCK_MARKER);
			for (int i = 0; i < nodes.getLength(); i++) {
				final Node node = nodes.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					final Element elem = (Element) node;
					final String question = elem.getElementsByTagName(QUESTION_MARKER).item(0).getTextContent();
					final String complexityString = elem.getElementsByTagName(COMPLEXITY_MARKER).item(0)
							.getTextContent();
					final Integer complexity;
					if (StringUtils.hasLength(complexityString)) {
						complexity = Integer.parseInt(complexityString) > 3 ? 3 : Integer.parseInt(complexityString);
					} else {
						complexity = 1;
					}

					final List<File> images = new LinkedList<>();
					final Map<Integer, Variant> variants = new LinkedHashMap<>();
					final List<String> answers = new ArrayList<>();

					final NodeList imageNodes = elem.getElementsByTagName(IMAGE_MARKER);
					for (int j = 0; j < imageNodes.getLength(); j++) {
						final Node imageNode = imageNodes.item(j);
						if (StringUtils.hasLength(imageNode.getTextContent())) {
							final File imageFile = FileUtils.convertStreamToFileIfNotExists(directory,
									imageNode.getTextContent());
							if (imageFile != null) {
								images.add(imageFile);
							}
						}
					}
					final NodeList answerNodes = elem.getElementsByTagName(ANSWER_MARKER);
					for (int j = 0; j < answerNodes.getLength(); j++) {
						final Node anwerNode = answerNodes.item(j);
						if (StringUtils.hasLength(anwerNode.getTextContent())) {
							answers.add(anwerNode.getTextContent());
						}
					}

					final NodeList variantNodes = elem.getElementsByTagName(VARIANT_MARKER);
					for (int j = 0; j < variantNodes.getLength(); j++) {
						final Node variantNode = variantNodes.item(j);
						final Integer variantId = Integer
								.parseInt(variantNode.getAttributes().getNamedItem(ID_ATTRIBUTE).getTextContent());
						final Variant variant = new Variant(variantId, variantNode.getTextContent());
						variants.put(variantId, variant);
					}

					questions.add(new Question(i + 1, question, variants, images, complexity, answers));
				}
			}

			return new Test(file, questions, firstTestQuestionsCount, secondTestQuestionsCount, firstTestTime,
					secondTestTime);
		} catch (ParserConfigurationException | SAXException | IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void upload(@NotNull final Test object) {
		upload(object.getSourceFile());
	}

	@Override
	public String getSourceKey() {
		return "external.source.test";
	}

}
