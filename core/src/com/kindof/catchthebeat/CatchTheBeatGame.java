package com.kindof.catchthebeat;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.kindof.catchthebeat.database.IDatabase;
import com.kindof.catchthebeat.screens.BaseScreen;
import com.kindof.catchthebeat.screens.Transition;
import com.kindof.catchthebeat.storage.IStorage;
import com.kindof.catchthebeat.authentication.IAuthentication;
import com.kindof.catchthebeat.resources.Globals;
import com.kindof.catchthebeat.screens.LoadingScreen;
import com.kindof.catchthebeat.ui.UI;
import com.kindof.catchthebeat.ui.actors.dialog.IDialogWindow;
import com.kindof.catchthebeat.ui.intenthandler.IIntentHandler;
import com.kindof.catchthebeat.ui.toastmaker.IToastMaker;
import com.kindof.catchthebeat.tools.networkconnection.INetworkConnection;

public class CatchTheBeatGame extends Game {
	private INetworkConnection networkConnection;
	private IIntentHandler intentHandler;
	private IAuthentication auth;
	private IDatabase database;
	private IStorage storage;
	private IToastMaker toastMaker;
	private IDialogWindow dialogWindow;

	public CatchTheBeatGame(
			INetworkConnection networkConnection,
			IIntentHandler intentHandler,
			IToastMaker toastMaker,
			IDialogWindow dialogWindow,
			IAuthentication auth,
			IDatabase database,
			IStorage storage
	) {
		this.networkConnection = networkConnection;
		this.intentHandler = intentHandler;
		this.toastMaker = toastMaker;
		this.dialogWindow = dialogWindow;
		this.auth = auth;
		this.database = database;
		this.storage = storage;
	}

	@Override
	public void create () {
		Gdx.input.setCatchKey(Input.Keys.BACK, true);
		Globals.GAME = this;
		setScreen(new LoadingScreen());
	}

	public void exit() {
		networkConnection.networkAction(() -> database.setUser(Globals.USER));
		Gdx.app.exit();
	}

	public void setScreenWithTransition(BaseScreen nextScreen) {
		setScreenWithTransition(Transition.TransitionType.fade, nextScreen, Transition.DURATION);
	}

	public void setScreenWithTransition(BaseScreen nextScreen, float duration) {
		setScreenWithTransition(Transition.TransitionType.fade, nextScreen, duration);
	}

	public void setScreenWithTransition(Transition.TransitionType transitionType, BaseScreen nextScreen, float duration) {
		UI.TRANSITION.set((BaseScreen) screen, nextScreen);
		UI.TRANSITION.transition(transitionType, duration);
	}

	public IStorage getStorage() {
		return storage;
	}

	public IAuthentication getAuth() {
		return auth;
	}

	public IDatabase getDatabase() {
		return database;
	}

	public IIntentHandler getIntentHandler() {
		return intentHandler;
	}

	public IToastMaker getToastMaker() {
		return toastMaker;
	}

	public IDialogWindow getDialogWindow() {
		return dialogWindow;
	}

	public INetworkConnection getNetworkConnection() {
		return networkConnection;
	}

	@Override
	public void dispose() {
		Globals.dispose();
	}
}
