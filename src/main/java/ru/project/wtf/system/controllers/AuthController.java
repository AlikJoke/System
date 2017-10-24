package ru.project.wtf.system.controllers;

import java.time.LocalDateTime;

import org.controlsfx.control.ToggleSwitch;
import org.controlsfx.control.textfield.CustomPasswordField;
import org.controlsfx.control.textfield.CustomTextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import ru.project.wtf.system.SystemApplication;
import ru.project.wtf.system.user.Professor;
import ru.project.wtf.system.user.SecurityContext;
import ru.project.wtf.system.user.Student;
import ru.project.wtf.system.user.User;

public class AuthController extends BaseController {

	@FXML
	private CustomTextField loginField;

	@FXML
	private CustomPasswordField passwordField;

	@FXML
	private CustomTextField surnameField;

	@FXML
	private CustomTextField nameField;

	@FXML
	private CustomTextField groupField;

	@FXML
	private HBox boxGroup;

	@FXML
	private ImageView loginImg;

	@FXML
	private ImageView passwordImg;

	@FXML
	private ToggleSwitch toggleStudent;

	@FXML
	private ToggleSwitch toggleTeacher;

	@FXML
	private Button loginButton;

	@FXML
	private Label surnameLabel;

	@FXML
	private Label nameLabel;

	@FXML
	private Label errorFirstLabel;

	@FXML
	private Label errorSecondLabel;

	@Autowired
	private SecurityContext securityContext;

	@Override
	public void init() {
		toggleStudent.setSelected(true);
	}

	@FXML
	public void actionButtonPressed() {
		final User user;
		final String id;
		final String password;
		if (toggleStudent.isSelected()) {
			if (!validateStudentInfo()) {
				return;
			}

			id = nameField.getText().concat(surnameField.getText()).concat(groupField.getText());
			password = null;
			final User.StudentInfo studentInfo = new User.StudentInfo(nameField.getText(), surnameField.getText(),
					groupField.getText());
			user = new Student(id, LocalDateTime.now(), studentInfo);
		} else {
			if (!validateAuthForm()) {
				return;
			}

			id = loginField.getText();
			password = passwordField.getText();
			user = new Professor(id, LocalDateTime.now(), password);
		}

		securityContext.auth(user);
		SystemApplication.getInstance().gotoScene("mainView");
	}

	@FXML
	public void toggleStudentClick() {
		if (toggleTeacher.isSelected()) {
			toggleTeacher.setSelected(false);
			clearAuthForm();
		} else {
			toggleStudent.setSelected(true);
		}

		enableOrDisableAuthForm(false);
		enableOrDisableStudentIdentify(true);
	}

	@FXML
	public void toggleTeacherClick() {
		if (toggleStudent.isSelected()) {
			toggleStudent.setSelected(false);
			clearStudentInfo();
		} else {
			toggleTeacher.setSelected(true);
		}

		enableOrDisableAuthForm(true);
		enableOrDisableStudentIdentify(false);
	}

	private void enableOrDisableStudentIdentify(final boolean enable) {
		surnameLabel.setVisible(enable);
		surnameField.setVisible(enable);
		nameLabel.setVisible(enable);
		nameField.setVisible(enable);
		boxGroup.setVisible(enable);
		
		errorFirstLabel.setVisible(false);
	}

	private void enableOrDisableAuthForm(final boolean enable) {
		loginImg.setVisible(enable);
		loginField.setVisible(enable);
		passwordField.setVisible(enable);
		passwordImg.setVisible(enable);
		
		errorSecondLabel.setVisible(false);
	}

	private void clearAuthForm() {
		loginField.clear();
		passwordField.clear();
		groupField.clear();
	}

	private void clearStudentInfo() {
		surnameField.clear();
		nameField.clear();
	}

	private boolean validateAuthForm() {
		final boolean isValid;
		if (StringUtils.isEmpty(loginField.getText()) || StringUtils.isEmpty(passwordField.getText())) {
			errorFirstLabel.setText("Неверный логин/пароль");
			errorFirstLabel.setVisible(true);
			isValid = false;
		} else {
			errorFirstLabel.setVisible(false);
			isValid = true;
		}
		return isValid;
	}

	private boolean validateStudentInfo() {
		boolean withErrors = false;
		if (withErrors |= StringUtils.isEmpty(surnameField.getText())) {
			errorSecondLabel.setText("Укажите Вашу Фамилию");
			errorSecondLabel.setVisible(true);
			surnameField.setStyle("-fx-border-color: #ff0000; -fx-border-width: 1px; -fx-border-radius: 3px");
		}

		if (withErrors |= StringUtils.isEmpty(nameField.getText())) {
			surnameField.setStyle("-fx-border: none");
			errorSecondLabel.setText("Укажите Ваше Имя");
			errorSecondLabel.setVisible(true);
			nameField.setStyle("-fx-border-color: #ff0000; -fx-border-width: 1px; -fx-border-radius: 3px");
		}

		if (withErrors |= StringUtils.isEmpty(groupField.getText())) {
			surnameField.setStyle("-fx-border: none");
			nameField.setStyle("-fx-border: none");
			errorSecondLabel.setText("Укажите Вашу Группу");
			errorSecondLabel.setVisible(true);
			groupField.setStyle("-fx-border-color: #ff0000; -fx-border-width: 1px; -fx-border-radius: 3px");
		}
		if (!withErrors) {
			errorSecondLabel.setVisible(false);
			surnameField.setStyle("-fx-border: none");
			nameField.setStyle("-fx-border: none");
			groupField.setStyle("-fx-border: none");
		}

		return withErrors;
	}
}
