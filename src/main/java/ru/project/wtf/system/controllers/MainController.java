package ru.project.wtf.system.controllers;

import java.io.FileInputStream;
import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;

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
	@PostConstruct
	public void init() {
		loadPdf(0);
	}

	@Override
	public void initialize() {

	}

	@FXML
	public void actionButtonPressed() {
		securutyContext.logout();
		SystemApplication.getInstance().gotoScene("authView");
	}

	@NotNull
	private ImageView loadPdf(final int pageNum) {
		if (pageNum < 0 || imageHolder.size() < pageNum) {
			throw new IllegalArgumentException();
		}

		try {
			imageView.setImage(new Image(new FileInputStream(imageHolder.getImages().get(pageNum))));
			imageView.setFitWidth(600.0);
			imageView.setFitHeight(800.0);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return imageView;
	}
}
