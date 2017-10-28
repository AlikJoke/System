package ru.project.wtf.system.controllers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;
import javax.validation.constraints.NotNull;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.springframework.beans.factory.annotation.Autowired;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import ru.project.wtf.system.SystemApplication;
import ru.project.wtf.system.pdf.ImageHolder;
import ru.project.wtf.system.user.SecurityContext;

public class MainController extends BaseController {

	@Autowired
	private ImageHolder imageHolder;

	@FXML
	private ImageView imageView;

	@FXML
	private Button exitButton;

	@FXML
	private AnchorPane theoryPanel;

	@Autowired
	private SecurityContext securutyContext;

	@Override
	public void init() {

	}

	@Override
	public void initialize() {
		loadPdf(0);
	}

	@FXML
	public void actionButtonPressed() {
		securutyContext.logout();
		SystemApplication.getInstance().gotoScene("authView");
	}

	@NotNull
	private ImageView loadPdf(final int pageNum) {
		if (pageNum < 0) {
			throw new IllegalArgumentException();
		}

		final InputStream is = this.getClass().getClassLoader().getResourceAsStream("importdata/theory.pdf");
		if (is == null) {
			throw new RuntimeException();
		}

		try {
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

			imageView.setImage(new Image(new FileInputStream(files.get(pageNum))));
			imageView.setFitWidth(600.0);
			imageView.setFitHeight(800.0);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return imageView;
	}
}
