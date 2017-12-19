package ru.project.wtf.system.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;
import ru.project.wtf.system.SystemApplication;
import ru.project.wtf.system.model.StageHolder;
import ru.project.wtf.system.modeling.ParametersProperties;
import ru.project.wtf.system.pdf.PdfHolder;
import ru.project.wtf.system.pdf.Reference;
import ru.project.wtf.system.pdf.Theory;
import ru.project.wtf.system.properties.Properties;
import ru.project.wtf.system.testloader.Question;
import ru.project.wtf.system.testloader.Test;
import ru.project.wtf.system.testloader.TestHolder;
import ru.project.wtf.system.timer.MinuteProperties;
import ru.project.wtf.system.timer.SecondProperties;
import ru.project.wtf.system.user.SecurityContext;
import ru.project.wtf.system.utils.FileUtils;

public class MainController extends BaseController {

	private static final String SEP = "@";

	@Autowired
	private Properties props;

	@Resource(name = "theoryHolder")
	private PdfHolder<Theory> theoryHolder;

	@Resource(name = "referenceHolder")
	private PdfHolder<Reference> referenceHolder;

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
	private Button loadButton;

	@FXML
	private Button zoomPlusButton;

	@FXML
	private Button zoomMinusButton;

	@FXML
	private Button uploadTestButton;

	@FXML
	private Button uploadPicturesButton;

	@FXML
	private Button downloadTestButton;

	@FXML
	private Button startFirstTestButton;

	@FXML
	private Button finishFirstTestButton;

	@FXML
	private Button referenceFirstTestButton;

	@FXML
	private Label timerLabelFirst;

	private static final Integer STARTMINUTESFIRST = 7;
	private static final Integer STARTSECONDSFIRST = 0;
	private static final MinuteProperties minutesPropertiesFirst = new MinuteProperties();
	private static final SecondProperties secondsPropertiesFirst = new SecondProperties();

	@FXML
	private Button startSecondTestButton;

	@FXML
	private Button finishSecondTestButton;

	@FXML
	private Button referenceSecondTestButton;

	@FXML
	private Label timerLabelSecond;

	private static final Integer STARTMINUTESSECOND = 20;
	private static final Integer STARTSECONDSSECOND = 0;
	private static final MinuteProperties minutesPropertiesSecond = new MinuteProperties();
	private static final SecondProperties secondsPropertiesSecond = new SecondProperties();

	private boolean isFirstTestSuccess;

	@FXML
	private VBox firstTestVBox;

	@FXML
	private VBox secondTestVBox;

	@Autowired
	private TestHolder testHolder;

	@Autowired
	private SecurityContext securutyContext;

	@FXML
	private Button modelingXeButton;

	@FXML
	private TextField phi0Xe;

	@FXML
	private TextField alfa1Xe;

	@FXML
	private TextField alfa2Xe;

	@FXML
	private TextField alfa3Xe;

	@FXML
	private TextField time1Xe;

	@FXML
	private TextField time2Xe;

	@FXML
	private TextField time3Xe;

	@FXML
	private TextField time4Xe;

	@FXML
	private Label errorModelingXe;

	@FXML
	private LineChart<Number, Number> chartDensityNTimeXe;

	@FXML
	private LineChart<Number, Number> chartConcetrationITime;

	@FXML
	private LineChart<Number, Number> chartConcetrationXeTime;

	private boolean labelIsVisibleXe;
	private ParametersProperties parametersXE = new ParametersProperties();
	private TextField[] arrayOfTextFieldXE = new TextField[8];
	@SuppressWarnings("rawtypes")
	private XYChart.Series seriesChartXe;

	private XYChart.Series seriesXe;

	private XYChart.Series seriesI;
	@FXML
	private Button modelingSmButton;

	@FXML
	private TextField phi0Sm;

	@FXML
	private TextField alfa1Sm;

	@FXML
	private TextField alfa2Sm;

	@FXML
	private TextField alfa3Sm;

	@FXML
	private TextField time1Sm;

	@FXML
	private TextField time2Sm;

	@FXML
	private TextField time3Sm;

	@FXML
	private TextField time4Sm;

	@FXML
	private Label errorModelingSm;

	@FXML
	private LineChart<Number, Number> chartDensityNTimeSm;

	private boolean labelIsVisibleSm;
	private ParametersProperties parametersSM = new ParametersProperties();
	private TextField[] arrayOfTextFieldSM = new TextField[8];
	@SuppressWarnings("rawtypes")
	private XYChart.Series seriesChartSm;

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
		loadPdf(counter);

		final boolean isStudent = securutyContext.getAuthUser().isStudent();
		loadButton.setVisible(!isStudent);
		uploadTestButton.setVisible(!isStudent);
		downloadTestButton.setVisible(!isStudent);
		uploadPicturesButton.setVisible(!isStudent);
		startFirstTestButton.setVisible(isStudent);
		finishFirstTestButton.setVisible(false);
		startSecondTestButton.setVisible(false);
		finishSecondTestButton.setVisible(false);
		isFirstTestSuccess = false;
		if (isStudent) {
			firstTestVBox.setVisible(false);
			secondTestVBox.setVisible(false);
			timerLabelFirst.setVisible(true);
			timerLabelSecond.setVisible(true);

			timerLabelFirst.setText(
					minutesPropertiesFirst.minute.toString() + " : 0" + secondsPropertiesFirst.second.toString());
			timerLabelSecond.setText(
					minutesPropertiesSecond.minute.toString() + " : 0" + secondsPropertiesSecond.second.toString());
		} else {
			timerLabelFirst.setVisible(false);
			timerLabelSecond.setVisible(false);

			loadFirstTestToPane();
			loadSecondTestToPane();
		}
	}

	private void loadFirstTestToPane() {
		firstTestVBox.getChildren().clear();
		final Test test = testHolder.get();
		Collections.shuffle(test.getObjects());
		test.getObjects().stream().filter(question -> question.getComplexity() == 1)
				.limit(test.getFirstTestQuestionsCount()).forEach(question -> loadQuestion(question, firstTestVBox));
	}

	private void loadSecondTestToPane() {
		secondTestVBox.getChildren().clear();
		final Test test = testHolder.get();
		Collections.shuffle(test.getObjects());
		test.getObjects().stream().filter(question -> question.getComplexity() == 1).limit(5)
				.forEach(question -> loadQuestion(question, secondTestVBox));
		test.getObjects().stream().filter(question -> question.getComplexity() == 2).limit(3)
				.forEach(question -> loadQuestion(question, secondTestVBox));
		test.getObjects().stream().filter(question -> question.getComplexity() == 3).limit(2)
				.forEach(question -> loadQuestion(question, secondTestVBox));
	}

	private void loadQuestion(Question question, VBox box) {
		final List<Node> nodes = new ArrayList<>();

		final Label questionLabel = new Label(question.getQuestionTitle());
		questionLabel.setId(question.getId().toString());
		nodes.add(questionLabel);

		nodes.addAll(question.getImages().stream().map(image -> createImageView(image)).collect(Collectors.toList()));

		nodes.addAll(question.getVariantsMap().entrySet().stream().map(entry -> {
			final RadioButton rb = new RadioButton(entry.getValue().getText());
			rb.setId(question.getId() + SEP + entry.getKey());
			return rb;
		}).collect(Collectors.toList()));

		if (question.getVariantsMap().isEmpty()) {
			final Label label = new Label("Ответ: ");
			final TextField tf = new TextField();
			tf.setId(question.getId() + SEP);
			nodes.add(label);
			nodes.add(tf);
		}

		final boolean isStudent = securutyContext.getAuthUser().isStudent();
		final Label answer = new Label("Правильный ответ: " +
				StringUtils.collectionToCommaDelimitedString(question.getAnswer()));
		answer.setVisible(!isStudent);

		nodes.add(answer);

		if (!isStudent) {
			nodes.add(new Label(null));
		}

		box.getChildren().addAll(nodes);
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

		chartDensityNTimeXe.setAnimated(true);
		chartConcetrationITime.setAnimated(true);
		chartConcetrationXeTime.setAnimated(true);
		chartDensityNTimeSm.setAnimated(true);
		minutesPropertiesFirst.minute = STARTMINUTESFIRST;
		secondsPropertiesFirst.second = STARTSECONDSFIRST;
		minutesPropertiesSecond.minute = STARTMINUTESSECOND;
		secondsPropertiesSecond.second = STARTSECONDSSECOND;
		timerLabelFirst.setStyle("-fx-background-color:\n" + "            #090a0c,\n"
				+ "            linear-gradient(#38424b 0%, #1f2429 20%, #191d22 100%),\n"
				+ "            linear-gradient(#20262b, #191d22),\n"
				+ "            radial-gradient(center 50% 0%, radius 100%, rgba(114,131,148,0.9), rgba(255,255,255,0));");
		timerLabelSecond.setStyle("-fx-background-color:\n" + "            #090a0c,\n"
				+ "            linear-gradient(#38424b 0%, #1f2429 20%, #191d22 100%),\n"
				+ "            linear-gradient(#20262b, #191d22),\n"
				+ "            radial-gradient(center 50% 0%, radius 100%, rgba(114,131,148,0.9), rgba(255,255,255,0));");
		timerLabelFirst.setStyle("-fx-font-size: 25px; -fx-font-weight: bold");
		timerLabelSecond.setStyle("-fx-font-size: 25px; -fx-font-weight: bold");
	}

	@FXML
	public void loadTheoryAction(ActionEvent event) throws IOException {
		final FileChooser fileChooser = new FileChooser();
		final FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
		fileChooser.getExtensionFilters().add(extFilter);

		final File selectedFile = fileChooser.showOpenDialog(null);
		if (selectedFile != null && selectedFile.exists() && selectedFile.canRead()
				&& FileUtils.PDF_EXTENSION.equalsIgnoreCase(FilenameUtils.getExtension(selectedFile.getName()))) {
			FileUtils.convertStreamToFile(new FileInputStream(selectedFile), props.getProperty("theory.file.name"));
			theoryHolder.reload();
			refresh();
		}
	}

	private int referenceCounter;

	private void clearRefCounter() {
		referenceCounter = 0;
	}

	@SuppressWarnings("deprecation")
	@FXML
	public void openHelp(ActionEvent event) {
		final Stage myDialog = new Stage();
		myDialog.initModality(Modality.WINDOW_MODAL);

		Scene myDialogScene = new Scene(VBoxBuilder.create().children(createPopupContent()).alignment(Pos.CENTER)
				.padding(new Insets(10)).build());

		myDialog.setScene(myDialogScene);
		myDialog.setTitle("Справка");
		myDialog.setOnCloseRequest(new EventHandler<WindowEvent>() {

			@Override
			public void handle(WindowEvent t) {
				clearRefCounter();
			}
		});

		myDialog.setResizable(false);
		myDialog.show();
	}

	private HBox createPopupContent() {
		final ImageView left = createImageView(FileUtils.convertStreamToFile("img", "arrow-left.png"));
		left.setFitWidth(25.0);
		left.setFitHeight(25.0);
		final ImageView right = createImageView(FileUtils.convertStreamToFile("img", "arrow-right.png"));
		right.setFitWidth(25.0);
		right.setFitHeight(25.0);

		final HBox box = new HBox(5);
		box.setAlignment(Pos.CENTER);
		box.getChildren().setAll(left, getReferencePage(referenceCounter), right);
		left.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent t) {
				box.getChildren().clear();
				box.getChildren().setAll(left, getReferencePage(--referenceCounter < 0 ? 0 : referenceCounter), right);
			}
		});

		right.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent t) {
				box.getChildren().clear();
				box.getChildren().setAll(left,
						getReferencePage(
								referenceCounter == referenceHolder.size() - 1 ? referenceCounter : ++referenceCounter),
						right);
			}
		});

		return box;
	}

	private void fillArrayOfTextFieldXE() {
		arrayOfTextFieldXE[0] = phi0Xe;
		arrayOfTextFieldXE[1] = alfa1Xe;
		arrayOfTextFieldXE[2] = alfa2Xe;
		arrayOfTextFieldXE[3] = alfa3Xe;
		arrayOfTextFieldXE[4] = time1Xe;
		arrayOfTextFieldXE[5] = time2Xe;
		arrayOfTextFieldXE[6] = time3Xe;
		arrayOfTextFieldXE[7] = time4Xe;
	}

	private void fillArrayOfTextFieldSM() {
		arrayOfTextFieldSM[0] = phi0Sm;
		arrayOfTextFieldSM[1] = alfa1Sm;
		arrayOfTextFieldSM[2] = alfa2Sm;
		arrayOfTextFieldSM[3] = alfa3Sm;
		arrayOfTextFieldSM[4] = time1Sm;
		arrayOfTextFieldSM[5] = time2Sm;
		arrayOfTextFieldSM[6] = time3Sm;
		arrayOfTextFieldSM[7] = time4Sm;
	}

	public void zoomPlusButtonPressed(final ActionEvent actionEvent) {
		zoomProperty.set(zoomProperty.get() * 1.1);
	}

	public void zoomMinusButtonPressed(final ActionEvent actionEvent) {
		zoomProperty.set(zoomProperty.get() / 1.1);
	}

	@FXML
	public void uploadTestAction() throws IOException {
		final FileChooser fileChooser = new FileChooser();
		final FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
		fileChooser.getExtensionFilters().add(extFilter);

		final File selectedFile = fileChooser.showOpenDialog(null);
		if (selectedFile != null && selectedFile.exists() && selectedFile.canRead()
				&& FileUtils.XML_EXTENSION.equalsIgnoreCase(FilenameUtils.getExtension(selectedFile.getName()))) {
			FileUtils.convertStreamToFile(new FileInputStream(selectedFile), props.getProperty("test.file.name"));
			testHolder.initReload();
			refresh();
		}
	}

	public void uploadPicturesAction() throws FileNotFoundException {
		final FileChooser fileChooser = new FileChooser();
		final FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PNG, JPG, JPEG files", "*.png",
				"*.jpeg", "*.jpg");
		fileChooser.getExtensionFilters().add(extFilter);

		final File selectedFile = fileChooser.showOpenDialog(null);
		if (selectedFile != null && selectedFile.exists() && selectedFile.canRead() && FileUtils.PICTURES_EXTENTSIONS
				.contains(FilenameUtils.getExtension(selectedFile.getName()).toLowerCase())) {
			FileUtils.convertStreamToFile(new FileInputStream(selectedFile), selectedFile.getName());
			testHolder.initReload();
			refresh();
		}
	}

	@FXML
	public void downloadTestAction() {
		final FileChooser fileChooser = new FileChooser();
		final FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
		fileChooser.getExtensionFilters().add(extFilter);

		final File file = fileChooser.showSaveDialog(null);
		if (file != null) {
			FileUtils.copyFile(testHolder.get().getSourceFile(), file);
			refresh();
		}
	}

	public void completeTestPressed(final VBox box, boolean isFirst) {
		final Test test = testHolder.get();
		final List<Integer> right = new ArrayList<>();
		final Set<Integer> questionIds = box.getChildrenUnmodifiable().stream()
				.filter(node -> StringUtils.hasLength(node.getId()))
				.map(node -> Integer.parseInt(node.getId().split(SEP)[0])).distinct().collect(Collectors.toSet());
		test.getObjects().forEach(item -> {
			final List<Integer> selectedIds = box.getChildrenUnmodifiable().stream()
					.filter(node -> StringUtils.hasLength(node.getId()) && node.getId().startsWith(item.getId() + SEP)
							&& node instanceof RadioButton && ((RadioButton) node).isSelected())
					.map(rb -> Integer.valueOf(rb.getId().substring(rb.getId().indexOf(SEP) + 1))).distinct()
					.collect(Collectors.toList());
			if (!item.getRightVariants().isEmpty() && item.getRightVariants().size() == selectedIds.size()
					&& selectedIds.containsAll(item.getRightVariants())) {
				right.add(item.getId());
			}

			box.getChildrenUnmodifiable().stream()
					.filter(node -> StringUtils.hasLength(node.getId()) && node.getId().equals(item.getId() + SEP))
					.forEach(node -> {
						if (Objects.equals(((TextField) node).getText().toLowerCase(),
								item.getAnswer().get(0).toLowerCase())) {
							right.add(item.getId());
						}
					});
		});

		box.getChildren().clear();

		final List<String> failedQuestions = test.getObjects().stream()
				.filter(item -> !right.contains(item.getId()) && questionIds.contains(item.getId()))
				.map(item -> item.getQuestion()).collect(Collectors.toList());

		final int success = Math.round(right.size() * 100 / questionIds.size());
		final int mark = isFirst ? right.size() : calculateMark(success);

		isFirstTestSuccess = !isFirst ? isFirstTestSuccess : isFirst && mark > 2;

		final StringBuilder sb = new StringBuilder("Результаты: ");
		sb.append("\n");
		sb.append("Правильных ответов: ").append(right.size());
		sb.append("\n");
		sb.append("Неправильных ответов: ").append(questionIds.size() - right.size());
		sb.append("\n");
		sb.append("Вопросы, на которые были даны неверные ответы: ");
		sb.append("\n");
		failedQuestions.forEach(quest -> sb.append(quest).append("\n"));
		sb.append("Процент выполнения теста: ").append(success).append("%");
		sb.append("\n");
		sb.append("Ваша оценка: ").append(mark);
		sb.append("\n");

		box.getChildren().add(new Label(sb.toString()));
	}

	private int calculateMark(final int success) {
		final int result;
		if (success < 60) {
			result = 2;
		} else if (success < 70) {
			result = 3;
		} else if (success < 90) {
			result = 4;
		} else {
			result = 5;
		}

		return result;
	}

	public void arrowRightClicked(MouseEvent mouseEvent) {
		if (theoryHolder.getPdf().getObjects().size() > counter + 1) {
			loadPdf(++counter);
		}
	}

	public void arrowLeftClicked(MouseEvent mouseEvent) {
		if (counter > 0) {
			loadPdf(--counter);
		}
	}

	@Autowired
	private StageHolder stageHolder;

	@FXML
	public void actionButtonPressed() {
		securutyContext.logout();
		SystemApplication.getInstance().gotoScene("authView");
	}

	@NotNull
	private ImageView loadPdf(final int pageNum) {
		if (pageNum < 0 || theoryHolder.getPdf().getObjects().size() < pageNum) {
			throw new IllegalArgumentException();
		}

		try {
			imageView.setImage(new Image(new FileInputStream(theoryHolder.getPdf().getObjects().get(pageNum))));
			imageView.setFitWidth(500);
			imageView.setFitHeight(800);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		return imageView;
	}

	@NotNull
	private ImageView getReferencePage(final int pageNum) {
		if (pageNum < 0 || referenceHolder.getPdf().getObjects().size() <= pageNum) {
			throw new IllegalArgumentException();
		}

		final ImageView result = createImageView(referenceHolder.getPdf().getObjects().get(pageNum));
		result.setFitHeight(650);
		result.setFitWidth(600);
		return result;
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

	public void startFirstTestAction(ActionEvent actionEvent) {
		loadFirstTestToPane();
		startFirstTestButton.setVisible(false);
		finishFirstTestButton.setVisible(true);
		startTimer(timerLabelFirst, minutesPropertiesFirst, secondsPropertiesFirst);
	}

	public void finishFirstTestAction(ActionEvent actionEvent) {
		completeTestPressed(firstTestVBox, true);
		endOfTimer(timerLabelFirst, minutesPropertiesFirst, secondsPropertiesFirst, STARTMINUTESFIRST,
				STARTSECONDSFIRST);

		startFirstTestButton.setVisible(true);
		finishFirstTestButton.setVisible(false);
		if (isFirstTestSuccess) {
			startSecondTestButton.setVisible(true);
			finishSecondTestButton.setVisible(false);
		}
	}

	public void startSecondTestAction(ActionEvent actionEvent) {
		loadSecondTestToPane();
		finishFirstTestButton.setVisible(false);
		startSecondTestButton.setVisible(false);
		finishSecondTestButton.setVisible(true);
		startTimer(timerLabelSecond, minutesPropertiesSecond, secondsPropertiesSecond);
	}

	public void finishSecondTestAction(ActionEvent actionEvent) {
		completeTestPressed(secondTestVBox, false);
		finishSecondTestButton.setVisible(false);
		startSecondTestButton.setVisible(true);
		endOfTimer(timerLabelSecond, minutesPropertiesSecond, secondsPropertiesSecond, STARTMINUTESSECOND,
				STARTSECONDSSECOND);
	}

	@SuppressWarnings("rawtypes")
	public void modelingXeAction(ActionEvent actionEvent) {
		Object source = actionEvent.getSource();
		if (!(source instanceof Button))
			return;
		errorModelingXe.setVisible(false);
		labelIsVisibleXe = false;
		chartDensityNTimeXe.getData().clear();
		chartConcetrationITime.getData().clear();
		chartConcetrationXeTime.getData().clear();
		seriesChartXe = new XYChart.Series();
		seriesI = new XYChart.Series();
		seriesXe = new XYChart.Series();
		seriesChartXe.setName("Мощность Ядерного Реактора");
		seriesI.setName("Концентрация I(t)");
		seriesXe.setName("Концентрация Xe(t)");
		fillArrayOfTextFieldXE();
		if (!parametersXE.stringToDouble(arrayOfTextFieldXE)) {
			errorModelingXe.setVisible(true);
			labelIsVisibleXe = true;
		}
		if (!labelIsVisibleXe)
			parametersXE.drawChartOfPoints(chartDensityNTimeXe, seriesChartXe);
			parametersXE.drawXIEModel(chartConcetrationITime, chartConcetrationXeTime, seriesI, seriesXe);
	}

	@SuppressWarnings("rawtypes")
	public void modelingSmAction(ActionEvent actionEvent) {
		Object source = actionEvent.getSource();
		if (!(source instanceof Button))
			return;
		errorModelingSm.setVisible(false);
		labelIsVisibleSm = false;
		chartDensityNTimeSm.getData().clear();
		seriesChartSm = new XYChart.Series();
		seriesChartSm.setName("Мощность Ядерного Реактора");
		fillArrayOfTextFieldSM();
		if (!parametersSM.stringToDouble(arrayOfTextFieldSM)) {
			errorModelingSm.setVisible(true);
			labelIsVisibleSm = true;
		}
		if (!labelIsVisibleSm)
			parametersSM.drawChartOfPoints(chartDensityNTimeSm, seriesChartSm);
	}

	private void startTimer(Label label, MinuteProperties min, SecondProperties sec) {
		label.setText(min.minute.toString() + " : " + sec.second.toString() + '0');
		min.timeline = new Timeline();
		sec.timeLine = new Timeline();
		min.timeline.setCycleCount(Timeline.INDEFINITE);
		sec.timeLine.setCycleCount(Timeline.INDEFINITE);
		min.reduceMinute();
		min.timeline.getKeyFrames().add(new KeyFrame(Duration.minutes(1), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				min.reduceMinute();
				;
				if (min.minute < 10)
					label.setText('0' + min.minute.toString() + " : " + sec.second.toString());
				else
					label.setText(min.minute.toString() + " : " + sec.second.toString());
				if (min.minute <= 0)
					min.timeline.stop();
			}
		}));
		min.timeline.playFromStart();
		sec.timeLine.getKeyFrames().add(new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				sec.numericFormatSecond();
				if (sec.second < 10)
					label.setText(min.minute.toString() + " : 0" + sec.second.toString());
				else
					label.setText(min.minute.toString() + " : " + sec.second.toString());
				if (min.minute <= 0 && sec.second <= 0)
					sec.timeLine.stop();
			}
		}));
		sec.timeLine.playFromStart();
	}

	private void endOfTimer(Label label, MinuteProperties min, SecondProperties sec, Integer startMinute,
			Integer startSecond) {
		if (min.timeline != null && sec.timeLine != null) {
			min.timeline.stop();
			sec.timeLine.stop();
		}
		sec.second = startSecond;
		min.minute = startMinute;
		label.setText(min.minute.toString() + " : 0" + sec.second.toString());
	}
}
