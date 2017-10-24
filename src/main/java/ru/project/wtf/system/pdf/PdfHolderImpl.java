package ru.project.wtf.system.pdf;

import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.jpedal.PdfDecoder;
import org.jpedal.exception.PdfException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.project.wtf.system.properties.Properties;

@Component
public class PdfHolderImpl implements PdfHolder {

	private final PdfDecoder pdf = new PdfDecoder();

	@Autowired
	private Properties props;

	@Override
	public PdfDecoder getPdf() {
		return pdf;
	}

	@PostConstruct
	public void savePdf() throws PdfException {
		final InputStream is = this.getClass().getClassLoader()
				.getResourceAsStream(props.getProperty("directory") + "theory.pdf");
		if (is == null) {
			throw new RuntimeException();
		}
		
		pdf.openPdfFileFromInputStream(is, false);
	}

}
