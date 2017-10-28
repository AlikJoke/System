package ru.project.wtf.system.pdf;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import ru.project.wtf.system.properties.Properties;

@Component
public class ImageHolderImpl implements ImageHolder {

	private final List<File> images = new LinkedList<>();

	@Autowired
	private Properties props;

	@Override
	public List<File> getImages() {
		return images;
	}

	@SuppressWarnings("unchecked")
	@PostConstruct
	private void loadPdf() throws IOException {
		final String path;
		if (StringUtils.isEmpty(path = props.getProperty("theory.file.path"))) {
			throw new RuntimeException();
		}

		final InputStream is = this.getClass().getClassLoader().getResourceAsStream(path);
		if (is == null) {
			throw new RuntimeException();
		}

		final PDDocument pdf = PDDocument.load(is);
		final List<File> files = new LinkedList<>();
		final List<PDPage> pages = pdf.getDocumentCatalog().getAllPages();
		pages.forEach(page -> {
			try {
				final File file = new File(UUID.randomUUID().toString());
				final BufferedImage image = page.convertToImage();
				ImageIO.write(image, "png", file);
				files.add(file);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}

		});

		images.addAll(files);

	}

	@PreDestroy
	public void destroy() {
		images.forEach(file -> file.delete());
	}

	@Override
	public int size() {
		return getImages().size();
	}
}
