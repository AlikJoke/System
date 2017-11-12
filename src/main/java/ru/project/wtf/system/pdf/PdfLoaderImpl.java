package ru.project.wtf.system.pdf;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import ru.project.wtf.system.external.HasExternalSourceAbstract;
import ru.project.wtf.system.utils.FileUtils;
import ru.project.wtf.system.utils.NotNullOrEmpty;

@Service
public class PdfLoaderImpl extends HasExternalSourceAbstract implements PdfLoader {

	@Override
	public Theory load(final String directory, @NotNullOrEmpty final String fileName) {
		final String path;
		if (StringUtils.isEmpty(path = props.getProperty("theory.file.path"))) {
			throw new RuntimeException();
		}

		try {
			final InputStream is;
			File newFile = new File(props.getProperty("theory.file.name"));
			if (newFile.exists() && newFile.canRead()) {
				is = new FileInputStream(newFile);
			} else {
				if (newFile != null) {
					newFile.delete();
				}
				is = this.getClass().getClassLoader().getResourceAsStream(path);
			}

			if (is == null) {
				throw new RuntimeException();
			}

			final PDDocument pdf = PDDocument.load(is);
			PDFRenderer renderer = new PDFRenderer(pdf);
			final List<File> files = new LinkedList<>();
			final int count = pdf.getDocumentCatalog().getPages().getCount();

			for (int i = 0; i < count; i++) {
				final File file = new File(
						UUID.randomUUID().toString() + FileUtils.getExtensionWithDot(FileUtils.PNG_EXTENSION));
				BufferedImage image = renderer.renderImage(i);
				ImageIO.write(image, FileUtils.PNG_EXTENSION, file);
				files.add(file);
				file.deleteOnExit();
			}

			return new Theory(null, files);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void upload(Theory object) {
		upload(object.getSourceFile());
	}

	@Override
	public String getSourceKey() {
		return "external.source.theory";
	}

}