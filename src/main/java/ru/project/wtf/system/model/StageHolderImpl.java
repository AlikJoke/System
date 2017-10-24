package ru.project.wtf.system.model;

import javax.annotation.PreDestroy;

import org.springframework.stereotype.Component;

import javafx.stage.Stage;

@Component
public class StageHolderImpl implements StageHolder {

	private Stage stage;

	@Override
	public Stage getStage() {
		return this.stage;
	}

	@Override
	public void holdStage(final Stage stageToHold) {
		this.stage = stageToHold;
	}

	@PreDestroy
	public void destroy() {
		this.stage.close();
	}
}
