package ru.project.wtf.system.controllers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.springframework.beans.factory.annotation.Autowired;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import ru.project.wtf.system.SystemApplication;
import ru.project.wtf.system.model.StageHolder;
import ru.project.wtf.system.pdf.PdfHolder;
import ru.project.wtf.system.user.SecurityContext;

public class MainController extends BaseController {

	@Autowired
	private PdfHolder pdfHolder;

	@Autowired
	private StageHolder holder;

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
		
		getPdf();
		theoryPanel.autosize();
	}

	@FXML
	public void actionButtonPressed() {
		securutyContext.logout();
		SystemApplication.getInstance().gotoScene("authView");
	}

	private void textField() {
		theoryPanel.getChildren().add(new TextField("test"));
	}

	private ImageView getPdf() {
		final InputStream is = this.getClass().getClassLoader().getResourceAsStream("importdata/" + "theory.pdf");
		if (is == null) {
			throw new RuntimeException();
		}

		PDDocument pdf;
		try {
			pdf = PDDocument.load(is);

			// splitting the pages of a PDF document
			List<PDPage> pages = pdf.getDocumentCatalog().getAllPages();
			File file = new File("test");
			BufferedImage image = pages.get(0).convertToImage();
			ImageIO.write(image, "png", file);
			imageView.setImage(new Image(new FileInputStream(file)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		imageView.setFitWidth(100.0);
		imageView.setFitHeight(1000.0);
		theoryPanel.setMaxHeight(2000.0);
		return imageView;
	}
}
