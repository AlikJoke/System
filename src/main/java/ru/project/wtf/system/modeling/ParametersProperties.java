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

	public static double d1 = 0.06;

	public static double b1 = 0.229;

	public static double c1 = 0.000028;

	public static double c2 = 0.000027;

	public static double n1 = 0.000000000000000003;

	// Количество шагов
	public static final int STEPS = 2000;

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

	// Производная  dI/dT
	public double deriv(double I, double Phi)
	{
		//Уравнение для интегрирования 1
		return d1*b1*Phi-c1*I;
	}
	//Производная  dX/dT
	public double deriv2(double X, double I, double Phi)
	{
		//Уравнение для интегрирования 2
		return c1*I-c2*X+n1*X*Phi;
	}

	public void drawXIEModel(LineChart<Number, Number> chart1, LineChart<Number, Number> chart2, XYChart.Series series1, XYChart.Series series2) {
		// Задаём шаг
		double h = t4/STEPS;
		double k1, k2, k3, k4, m1, m2, m3, m4;
		double T, X, I;
		int i;
		double Phi;
		/////////////////////////////////////////////////////////////////*Math.pow(10, 13)
		I = d1*b1*phi0/c1;
		series1.getData().add(new XYChart.Data(0, I));

		X=d1*b1*phi0/(c2+n1*phi0);
		series2.getData().add(new XYChart.Data(0, X));
		for (i=1; i<STEPS; i++)
		{
			T = i * h;
			if (T<t1){
				Phi = phi0;
			} else if (T>t1 && T<t2){
				Phi = a1 * phi0;
			} else if (T>t2 && T<t3){
				Phi = a2 * phi0;
			} else {
				Phi = a3 * phi0;
			}
			k1 = deriv(I, Phi);
			k2 = deriv(I + (k1*h/2), Phi);
			k3 = deriv(I + (k2*h/2), Phi);
			k4 = deriv(I + k3*h, Phi);

			m1 = deriv2(X, I, Phi);
			m2 = deriv2( X + (m1*h/2), I + (h*m1/2), Phi);
			m3 = deriv2(X + m2/2, I + (h*m2/2), Phi);
			m4 = deriv2(X + m3, I + h*m3, Phi);

			I += h*(k1/6 + k2/3+ k3/3 + k4/6);
			X += h*(m1/6 + m2/3+ m3/3 + m4/6);

			series1.getData().add(new XYChart.Data(T, I));
			series2.getData().add(new XYChart.Data(T, X));
		}
		chart1.getData().add(series1);
		ObservableList<XYChart.Data> dataList1 = ((XYChart.Series) chart1.getData().get(0)).getData();
		dataList1.forEach(data -> {
			Node node = data.getNode();
			Tooltip tooltip = new Tooltip(
					"(Time = " + data.getXValue().toString() + " ; I(t) = " + data.getYValue().toString() + ')');
			Tooltip.install(node, tooltip);
		});
		chart2.getData().add(series2);
		ObservableList<XYChart.Data> dataList2 = ((XYChart.Series) chart1.getData().get(0)).getData();
		dataList2.forEach(data -> {
			Node node = data.getNode();
			Tooltip tooltip = new Tooltip(
					"(Time = " + data.getXValue().toString() + " ; X(t) = " + data.getYValue().toString() + ')');
			Tooltip.install(node, tooltip);
		});
	}
}
