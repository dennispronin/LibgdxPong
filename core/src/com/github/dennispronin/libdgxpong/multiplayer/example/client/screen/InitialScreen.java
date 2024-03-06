package com.github.dennispronin.libdgxpong.multiplayer.example.client.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.github.dennispronin.libdgxpong.multiplayer.example.client.PongGame;

import static com.github.dennispronin.libdgxpong.Constants.*;

public class InitialScreen implements Screen {

    private final PongGame pongGame;

    private final Stage stage = new Stage();

    final Skin skin = new Skin(Gdx.files.internal("uiskin.json"));

    private final OrthographicCamera camera = new OrthographicCamera();
    private final Table menuLayout = new Table();
    private final TextField host = new TextField(SERVER_HOST, skin);
    private final TextField port = new TextField(String.valueOf(SERVER_PORT), skin);
    private final TextField username = new TextField("Enter your name", skin);
    private final TextField sessionPassword = new TextField("Enter session password", skin);

    private final TextButton connectButton = new TextButton("Connect to session", skin);;
    private final TextButton createButton = new TextButton("Create session", skin);

    public InitialScreen(PongGame pongGame) {
        this.pongGame = pongGame;
        this.camera.setToOrtho(false, WINDOW_WIDTH, WINDOW_HEIGHT);
        this.menuLayout.setBounds(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

        this.connectButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // TODO player controls right paddle in this case
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        this.createButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // TODO player controls left paddle in this case
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        fillLayout();
    }

    private void fillLayout() {
        this.menuLayout.clear();
        this.menuLayout.add(this.host).width(250).row();
        this.menuLayout.add(this.port).width(250).padTop(25).row();
        this.menuLayout.add(this.username).width(250).padTop(25).row();
        this.menuLayout.add(this.sessionPassword).width(250).padTop(25).row();
        this.menuLayout.add(this.connectButton).size(150, 50).padTop(100).row();
        this.menuLayout.add(this.createButton).size(150, 50).padTop(25).row();

        this.stage.addActor(this.menuLayout);
    }

    private void connectToServer() {

    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this.stage);
    }

    @Override
    public void render(float delta) {
        this.stage.draw();
        this.stage.act(delta);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}