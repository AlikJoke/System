package ru.project.wtf.system.pdf;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import ru.project.wtf.system.properties.Properties;

@Component
public class PdfHolderImpl implements PdfHolder {

	private Theory pdf;

	@Autowired
	private Properties props;

	@Autowired
	private PdfLoader loader;

	@Override
	public Theory getPdf() {
		return pdf;
	}

	@PostConstruct
	private void init() throws IOException {
		reload();
	}

	@PreDestroy
	private void destroy() {
		pdf.getObjects().forEach(file -> file.delete());
	}

	@Override
	public int size() {
		return pdf.getObjects().size();
	}

	@Override
	public void reload() throws IOException {
		pdf = loader.load(null, props.getProperty("theory.file.name"));
	}
}
