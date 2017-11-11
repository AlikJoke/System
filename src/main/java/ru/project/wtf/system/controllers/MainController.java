package ru.project.wtf.system.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import ru.project.wtf.system.SystemApplication;
import ru.project.wtf.system.pdf.ImageHolder;
import ru.project.wtf.system.testloader.Test;
import ru.project.wtf.system.testloader.TestHolder;
import ru.project.wtf.system.user.SecurityContext;

public class MainController extends BaseController {

	private static final String SEP = "$";

	@Autowired
	private ImageHolder imageHolder;

	@FXML
	private ImageView imageView;

	@FXML
	private ImageView arrowLeft;

	@FXML
	private ImageView arrowRight;

	@FXML
	private Button exitButton;

	@FXML
	private Button referenceTheoryButton;

	@FXML
	private Button referenceTestButton;

	@FXML
	private Button loadButton;

	@FXML
	private Button cancelButton;

	@FXML
	private Button editButton;
	
	@FXML
	private Button reloadTestButton;

	@FXML
	private Button saveChangesButton;

	@FXML
	private Button completeButton;

	@FXML
	private Button zoomPlusButton;

	@FXML
	private Button zoomMinusButton;

	@FXML
	private VBox testVBox;

	@Autowired
	private TestHolder testHolder;

	@Autowired
	private SecurityContext securutyContext;

	private int counter;

	final DoubleProperty zoomProperty = new SimpleDoubleProperty(300);

	@Override
	@PostConstruct
	public void init() {
		loadPdf(counter);
	}

	@Override
	public void refresh() {
		counter = 0;
		loadPdf(0);

		final boolean isStudent = securutyContext.getAuthUser().isStudent();
		loadButton.setVisible(!isStudent);
		cancelButton.setVisible(!isStudent);
		editButton.setVisible(!isStudent);
		saveChangesButton.setVisible(!isStudent);
		completeButton.setVisible(isStudent);
		reloadTestButton.setVisible(false);

		loadTestToPane();
	}

	private void loadTestToPane() {
		testVBox.getChildren().clear();
		final Test test = testHolder.get();
		test.getQuestions().forEach(question -> {
			final List<Node> nodes = new ArrayList<>();

			final Label questionLabel = new Label(question.getId() + ". " + question.getQuestion());
			questionLabel.setId(question.getId().toString());
			nodes.add(questionLabel);

			nodes.addAll(
					question.getImages().stream().map(image -> createImageView(image)).collect(Collectors.toList()));

			nodes.addAll(question.getVariantsMap().entrySet().stream().map(entry -> {
				final RadioButton rb = new RadioButton(entry.getValue().getText());
				rb.setId(question.getId() + SEP + entry.getKey());
				return rb;
			}).collect(Collectors.toList()));

			final Label answer = new Label(null);
			answer.setVisible(false);

			nodes.add(answer);

			testVBox.getChildren().addAll(nodes);
		});
	}

	@Override
	public void initialize() {
		zoomProperty.addListener(new InvalidationListener() {
			@Override
			public void invalidated(Observable observable) {
				imageView.setFitWidth(zoomProperty.get() * 4);
				imageView.setFitHeight(zoomProperty.get() * 3);
			}
		});
	}

	public void zoomPlusButtonPressed(ActionEvent actionEvent) {
		zoomProperty.set(zoomProperty.get() * 1.1);
	}

	public void zoomMinusButtonPressed(ActionEvent actionEvent) {
		zoomProperty.set(zoomProperty.get() / 1.1);
	}
	
	public void reloadTestPressed(final ActionEvent event) {
		loadTestToPane();
		completeButton.setVisible(true);
		reloadTestButton.setVisible(false);
	}

	public void completeTestPressed(final ActionEvent event) {
		final Test test = testHolder.get();
		final List<Integer> right = new ArrayList<>();
		test.getQuestions().forEach(item -> {
			final List<Integer> selectedIds = testVBox.getChildrenUnmodifiable().stream()
					.filter(node -> StringUtils.hasLength(node.getId()) && node.getId().startsWith(item.getId() + SEP)
							&& ((RadioButton) node).isSelected())
					.map(rb -> Integer.valueOf(rb.getId().substring(rb.getId().indexOf(SEP) + 1))).distinct()
					.collect(Collectors.toList());
			if (item.getRightVariants().size() == selectedIds.size()
					&& selectedIds.containsAll(item.getRightVariants())) {
				right.add(item.getId());
			}
		});

		testVBox.getChildren().clear();

		final List<String> failedQuestions = test.getQuestions().stream().filter(item -> !right.contains(item.getId()))
				.map(item -> item.getQuestion()).collect(Collectors.toList());

		final StringBuilder sb = new StringBuilder("Результаты: ");
		sb.append("\n");
		sb.append("Правильных ответов: ").append(right.size());
		sb.append("\n");
		sb.append("Неправильных ответов: ").append(test.getQuestions().size() - right.size());
		sb.append("\n");
		sb.append("Вопросы, на которые были даны неверные ответы: ");
		sb.append("\n");
		failedQuestions.forEach(quest -> sb.append(quest).append("\n"));
		sb.append("Ваша оценка: ?");
		sb.append("\n");

		testVBox.getChildren().add(new Label(sb.toString()));
		reloadTestButton.setVisible(true);
		completeButton.setVisible(false);
	}

	public void arrowRightClicked(MouseEvent mouseEvent) {
		if (imageHolder.size() > counter + 1) {
			loadPdf(++counter);
		}
	}

	public void arrowLeftClicked(MouseEvent mouseEvent) {
		if (counter > 0) {
			loadPdf(--counter);
		}
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
			imageView.setFitWidth(500);
			imageView.setFitHeight(800);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return imageView;
	}

	@NotNull
	private ImageView createImageView(@NotNull final File file) {
		final ImageView imageView = new ImageView();
		try {
			imageView.setFitHeight(350);
			imageView.setFitWidth(450);
			imageView.setImage(new Image(new FileInputStream(file)));
		} catch (FileNotFoundException e) {
			throw new RuntimeException(e);
		}
		return imageView;
	}
}
