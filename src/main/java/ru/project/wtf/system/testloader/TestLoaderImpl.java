package ru.project.wtf.system.testloader;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import ru.project.wtf.system.external.HasExternalSourceAbstract;
import ru.project.wtf.system.properties.Properties;
import ru.project.wtf.system.utils.FileUtils;

@Service
public class TestLoaderImpl extends HasExternalSourceAbstract implements TestLoader {

	private static final String IMAGE_MARKER = "image-name";
	private static final String QUESTION_BLOCK_MARKER = "question-block";
	private static final String QUESTION_MARKER = "question";
	private static final String VARIANT_MARKER = "variant";

	private static final String ID_ATTRIBUTE = "id";
	private static final String IS_TRUE_ATTRIBUTE = "isTrue";

	@Autowired
	private Properties props;

	@Override
	public Test load(final String directory, final String fileName) {
		final File file;
		File newFile = new File(props.getProperty("test.file.name"));
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
			final NodeList nodes = doc.getElementsByTagName(QUESTION_BLOCK_MARKER);
			for (int i = 0; i < nodes.getLength(); i++) {
				final Node node = nodes.item(i);
				if (node.getNodeType() == Node.ELEMENT_NODE) {
					final Element elem = (Element) node;
					final String question = elem.getElementsByTagName(QUESTION_MARKER).item(0).getTextContent();
					final List<File> images = new LinkedList<>();
					final Map<Integer, Variant> variants = new LinkedHashMap<>();

					final NodeList imageNodes = elem.getElementsByTagName(IMAGE_MARKER);
					for (int j = 0; j < imageNodes.getLength(); j++) {
						final Node imageNode = imageNodes.item(j);
						if (StringUtils.hasLength(imageNode.getTextContent())) {
							images.add(FileUtils.convertStreamToFile(directory, imageNode.getTextContent()));
						}
					}

					final NodeList variantNodes = elem.getElementsByTagName(VARIANT_MARKER);
					for (int j = 0; j < variantNodes.getLength(); j++) {
						final Node variantNode = variantNodes.item(j);
						final Integer variantId = Integer
								.parseInt(variantNode.getAttributes().getNamedItem(ID_ATTRIBUTE).getTextContent());
						final Boolean isTrue = variantNode.getAttributes().getNamedItem(IS_TRUE_ATTRIBUTE) == null
								? false
								: StringUtils.isEmpty(
										variantNode.getAttributes().getNamedItem(IS_TRUE_ATTRIBUTE).getTextContent())
										|| Boolean.parseBoolean(variantNode.getAttributes()
												.getNamedItem(IS_TRUE_ATTRIBUTE).getTextContent());
						final Variant variant = new Variant(variantId, variantNode.getTextContent(), isTrue);
						variants.put(variantId, variant);
					}

					questions.add(new Question(i + 1, question, variants, images));
				}
			}

			return new Test(file, questions);
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
