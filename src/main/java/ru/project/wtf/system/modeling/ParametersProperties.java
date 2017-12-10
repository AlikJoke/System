package ru.project.wtf.system.modeling;

import org.apache.commons.lang3.StringUtils;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;

public class ParametersProperties {

	public double phi0;

	public double a1;

	public double a2;

	public double a3;

	public double t1;

	public double t2;

	public double t3;

	public double t4;

	public boolean stringToDouble(TextField[] textField) {
		try {
			phi0 = Double.parseDouble(getTrimmedString(textField[0]));
			a1 = Double.parseDouble(getTrimmedString(textField[1]));
			a2 = Double.parseDouble(getTrimmedString(textField[2]));
			a3 = Double.parseDouble(getTrimmedString(textField[3]));
			t1 = Double.parseDouble(getTrimmedString(textField[4]));
			t2 = Double.parseDouble(getTrimmedString(textField[5]));
			t3 = Double.parseDouble(getTrimmedString(textField[6]));
			t4 = Double.parseDouble(getTrimmedString(textField[7]));
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void drawChartOfPoints(LineChart<Number, Number> chart, XYChart.Series series) {
		series.getData().add(new XYChart.Data(0, phi0));
		series.getData().add(new XYChart.Data(t1, phi0));
		series.getData().add(new XYChart.Data(t1, a1 * phi0));
		series.getData().add(new XYChart.Data(t2, a1 * phi0));
		series.getData().add(new XYChart.Data(t2, a2 * phi0));
		series.getData().add(new XYChart.Data(t3, a2 * phi0));
		series.getData().add(new XYChart.Data(t3, a3 * phi0));
		series.getData().add(new XYChart.Data(t4, a3 * phi0));
		chart.getData().add(series);
		ObservableList<XYChart.Data> dataList = ((XYChart.Series) chart.getData().get(0)).getData();
		dataList.forEach(data -> {
			Node node = data.getNode();
			Tooltip tooltip = new Tooltip(
					"(Time = " + data.getXValue().toString() + " ; φ(t) = " + data.getYValue().toString() + ')');
			Tooltip.install(node, tooltip);
		});
	}

	private String getTrimmedString(final TextField tf) {
		return StringUtils.trim(tf.getText());
	}
}
