package com.kindof.catchthebeat;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.kindof.catchthebeat.database.IDatabase;
import com.kindof.catchthebeat.storage.IStorage;
import com.kindof.catchthebeat.authentication.IAuthentication;
import com.kindof.catchthebeat.resources.Res;
import com.kindof.catchthebeat.screens.LoadingScreen;

public class CatchTheBeatGame extends Game {
	private IIntentHandler intentHandler;
	private IAuthentication auth;
	private IDatabase database;
	private IStorage storage;
	private IToastMaker toastMaker;

	public CatchTheBeatGame(IToastMaker toastMaker, IAuthentication auth, IDatabase database, IStorage storage, IIntentHandler intentHandler) {
		this.toastMaker = toastMaker;
		this.intentHandler = intentHandler;
		this.auth = auth;
		this.database = database;
		this.storage = storage;
	}

	@Override
	public void create () {
		Gdx.input.setCatchKey(Input.Keys.BACK, true);
		Res.GAME = this;
		setScreen(new LoadingScreen());
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

	@Override
	public void dispose() {
		Res.dispose();
	}
}
