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

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import ru.project.wtf.system.SystemApplication;
import ru.project.wtf.system.pdf.PdfHolder;
import ru.project.wtf.system.properties.Properties;
import ru.project.wtf.system.testloader.Test;
import ru.project.wtf.system.testloader.TestHolder;
import ru.project.wtf.system.user.SecurityContext;
import ru.project.wtf.system.utils.FileUtils;

public class MainController extends BaseController {

	private static final String SEP = "$";

	@Autowired
	private Properties props;

	@Autowired
	private PdfHolder pdfHolder;

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
	private Button uploadTestButton;

	@FXML
	private Button downloadTestButton;

	@FXML
	private Button reloadTestButton;

	@FXML
	private Button completeTestButton;

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

	@FXML
	private Button applyButtonXe;

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

	private double fi0Xe;
	private double a1Xe;
	private double a2Xe;
	private double a3Xe;
	private double t1Xe;
	private double t2Xe;
	private double t3Xe;
	private double t4Xe;
	private boolean labelIsVisibleXe;
	@SuppressWarnings("rawtypes")
	private XYChart.Series seriesChartXe;

	@FXML
	private Button applyButtonSm;

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

	private double fi0Sm;
	private double a1Sm;
	private double a2Sm;
	private double a3Sm;
	private double t1Sm;
	private double t2Sm;
	private double t3Sm;
	private double t4Sm;
	private boolean labelIsVisibleSm;
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
		completeTestButton.setVisible(isStudent);
		reloadTestButton.setVisible(false);

		loadTestToPane();
	}

	private void loadTestToPane() {
		testVBox.getChildren().clear();
		final Test test = testHolder.get();
		test.getObjects().forEach(question -> {
			final List<Node> nodes = new ArrayList<>();

			final Label questionLabel = new Label(question.getQuestionTitle());
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

	@FXML
	public void loadTheoryAction(ActionEvent event) throws IOException {
		final FileChooser fileChooser = new FileChooser();
		final FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PDF files (*.pdf)", "*.pdf");
		fileChooser.getExtensionFilters().add(extFilter);

		final File selectedFile = fileChooser.showOpenDialog(null);
		if (selectedFile != null && selectedFile.exists() && selectedFile.canRead()
				&& FileUtils.PDF_EXTENSION.equalsIgnoreCase(FilenameUtils.getExtension(selectedFile.getName()))) {
			FileUtils.convertStreamToFile(new FileInputStream(selectedFile), props.getProperty("theory.file.name"));
			pdfHolder.reload();
			refresh();
		}
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

	public void uploadPictureAction() throws FileNotFoundException {
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

	public void reloadTestPressed(final ActionEvent event) {
		loadTestToPane();
		completeTestButton.setVisible(true);
		reloadTestButton.setVisible(false);
	}

	public void completeTestPressed(final ActionEvent event) {
		final Test test = testHolder.get();
		final List<Integer> right = new ArrayList<>();
		test.getObjects().forEach(item -> {
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

		final List<String> failedQuestions = test.getObjects().stream().filter(item -> !right.contains(item.getId()))
				.map(item -> item.getQuestion()).collect(Collectors.toList());

		final StringBuilder sb = new StringBuilder("Результаты: ");
		sb.append("\n");
		sb.append("Правильных ответов: ").append(right.size());
		sb.append("\n");
		sb.append("Неправильных ответов: ").append(test.getObjects().size() - right.size());
		sb.append("\n");
		sb.append("Вопросы, на которые были даны неверные ответы: ");
		sb.append("\n");
		failedQuestions.forEach(quest -> sb.append(quest).append("\n"));
		sb.append("Ваша оценка: ?");
		sb.append("\n");

		testVBox.getChildren().add(new Label(sb.toString()));
		reloadTestButton.setVisible(true);
		completeTestButton.setVisible(false);
	}

	public void arrowRightClicked(MouseEvent mouseEvent) {
		if (pdfHolder.getPdf().getObjects().size() > counter + 1) {
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
		if (pageNum < 0 || pdfHolder.getPdf().getObjects().size() < pageNum) {
			throw new IllegalArgumentException();
		}

		try {
			imageView.setImage(new Image(new FileInputStream(pdfHolder.getPdf().getObjects().get(pageNum))));
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

	@SuppressWarnings({ "rawtypes" })
	public void applyButtonXePressed(final ActionEvent actionEvent) {
		final Object source = actionEvent.getSource();
		if (!(source instanceof Button)) {
			return;
		}

		errorModelingXe.setVisible(false);
		labelIsVisibleXe = false;
		chartDensityNTimeXe.getData().clear();
		seriesChartXe = new XYChart.Series();
		seriesChartXe.setName("Мощность Ядерного Реактора");

		try {
			fi0Xe = Double.parseDouble(phi0Xe.getText().trim());
			a1Xe = Double.parseDouble(alfa1Xe.getText().trim());
			a2Xe = Double.parseDouble(alfa2Xe.getText().trim());
			a3Xe = Double.parseDouble(alfa3Xe.getText().trim());
			t1Xe = Double.parseDouble(time1Xe.getText().trim());
			t2Xe = Double.parseDouble(time2Xe.getText().trim());
			t3Xe = Double.parseDouble(time3Xe.getText().trim());
			t4Xe = Double.parseDouble(time4Xe.getText().trim());
		} catch (Exception e) {
			errorModelingXe.setVisible(true);
			labelIsVisibleXe = true;
		}

		if (!labelIsVisibleXe) {
			drawChartXe(chartDensityNTimeXe, seriesChartXe);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void drawChartXe(final LineChart<Number, Number> chart, final XYChart.Series series) {
		series.getData().add(new XYChart.Data(0, fi0Xe));
		series.getData().add(new XYChart.Data(t1Xe, fi0Xe));
		series.getData().add(new XYChart.Data(t1Xe, a1Xe * fi0Xe));
		series.getData().add(new XYChart.Data(t2Xe, a1Xe * fi0Xe));
		series.getData().add(new XYChart.Data(t2Xe, a2Xe * fi0Xe));
		series.getData().add(new XYChart.Data(t3Xe, a2Xe * fi0Xe));
		series.getData().add(new XYChart.Data(t3Xe, a3Xe * fi0Xe));
		series.getData().add(new XYChart.Data(t4Xe, a3Xe * fi0Xe));
		chart.getData().add(series);
		final ObservableList<XYChart.Data> dataList = ((XYChart.Series) chart.getData().get(0)).getData();
		dataList.forEach(data -> {
			final Node node = data.getNode();
			final Tooltip tooltip = new Tooltip(
					"(Time = " + data.getXValue().toString() + " ; φ(t) = " + data.getYValue().toString() + ')');
			Tooltip.install(node, tooltip);
		});
	}

	@SuppressWarnings({ "rawtypes" })
	public void applyButtonSmPressed(final ActionEvent actionEvent) {
		final Object source = actionEvent.getSource();
		if (!(source instanceof Button)) {
			return;
		}

		errorModelingSm.setVisible(false);
		labelIsVisibleSm = false;
		chartDensityNTimeSm.getData().clear();
		seriesChartSm = new XYChart.Series();
		seriesChartSm.setName("Мощность Ядерного Реактора");

		try {
			fi0Sm = Double.parseDouble(phi0Sm.getText().trim());
			a1Sm = Double.parseDouble(alfa1Sm.getText().trim());
			a2Sm = Double.parseDouble(alfa2Sm.getText().trim());
			a3Sm = Double.parseDouble(alfa3Sm.getText().trim());
			t1Sm = Double.parseDouble(time1Sm.getText().trim());
			t2Sm = Double.parseDouble(time2Sm.getText().trim());
			t3Sm = Double.parseDouble(time3Sm.getText().trim());
			t4Sm = Double.parseDouble(time4Sm.getText().trim());
		} catch (Exception e) {
			errorModelingSm.setVisible(true);
			labelIsVisibleSm = true;
		}
		if (!labelIsVisibleSm) {
			drawChartSm(chartDensityNTimeSm, seriesChartSm);
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void drawChartSm(final LineChart<Number, Number> chart, final XYChart.Series series) {
		series.getData().add(new XYChart.Data(0, fi0Sm));
		series.getData().add(new XYChart.Data(t1Sm, fi0Sm));
		series.getData().add(new XYChart.Data(t1Sm, a1Sm * fi0Sm));
		series.getData().add(new XYChart.Data(t2Sm, a1Sm * fi0Sm));
		series.getData().add(new XYChart.Data(t2Sm, a2Sm * fi0Sm));
		series.getData().add(new XYChart.Data(t3Sm, a2Sm * fi0Sm));
		series.getData().add(new XYChart.Data(t3Sm, a3Sm * fi0Sm));
		series.getData().add(new XYChart.Data(t4Sm, a3Sm * fi0Sm));
		chart.getData().add(series);
		final ObservableList<XYChart.Data> dataList = ((XYChart.Series) chart.getData().get(0)).getData();
		dataList.forEach(data -> {
			final Node node = data.getNode();
			final Tooltip tooltip = new Tooltip(
					"(Time = " + data.getXValue().toString() + " ; φ(t) = " + data.getYValue().toString() + ')');
			Tooltip.install(node, tooltip);
		});
	}
}
