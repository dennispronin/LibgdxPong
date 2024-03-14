package com.github.dennispronin.libdgxpong.multiplayer.client.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.esotericsoftware.kryonet.Client;
import com.github.dennispronin.libdgxpong.multiplayer.server.Network;
import com.github.dennispronin.libdgxpong.multiplayer.client.ClientEventListener;
import com.github.dennispronin.libdgxpong.multiplayer.server.event.CreateSessionClientEvent;
import com.github.dennispronin.libdgxpong.multiplayer.server.event.JoinSessionClientEvent;

import java.io.IOException;

import static com.github.dennispronin.libdgxpong.Constants.*;
import static com.github.dennispronin.libdgxpong.multiplayer.server.Network.SERVER_HOST;
import static com.github.dennispronin.libdgxpong.multiplayer.server.Network.SERVER_PORT;

public class InitialScreen implements Screen {

    private final Stage stage = new Stage();
    private final Client client = new Client();

    private final Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
    private final Table menuLayout = new Table();
    private final TextField host = new TextField(SERVER_HOST, skin);
    private final TextField port = new TextField(String.valueOf(SERVER_PORT), skin);
    private final TextField sessionId = new TextField("Enter session Id (if connecting)", skin);
    private final TextField sessionPassword = new TextField("Enter session password", skin);
    private final TextButton joinButton = new TextButton("Join session", skin);
    private final TextButton createButton = new TextButton("Create session", skin);
    private final Label informationMessage = new Label(null, skin);

    public InitialScreen() {
        this.menuLayout.setBounds(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false, WINDOW_WIDTH, WINDOW_HEIGHT);

        client.addListener(new ClientEventListener());

        addConnectButtonListener();
        addCreateButtonListener();
        fillLayout();
    }

    private void addConnectButtonListener() {
        this.joinButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                try {
                    connectToServer();
                } catch (Exception e) {
                    e.printStackTrace();
                    informationMessage.setText("Server connection failed. Try again.");
                    return super.touchDown(event, x, y, pointer, button);
                }
                client.sendTCP(new JoinSessionClientEvent(sessionPassword.getText(), sessionId.getText()));
                informationMessage.setText("Joining game session");
                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }

    private void addCreateButtonListener() {
        this.createButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                try {
                    connectToServer();
                } catch (Exception e) {
                    e.printStackTrace();
                    informationMessage.setText("Server connection failed. Try again.");
                    return super.touchDown(event, x, y, pointer, button);
                }
                client.sendTCP(new CreateSessionClientEvent(sessionPassword.getText()));
                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }

    public void showMessage(String message) {
        this.informationMessage.setText(message);
    }

    public void setSessionId(String sessionId) {
        this.sessionId.setText(sessionId);
    }

    private void fillLayout() {
        this.menuLayout.clear();
        this.menuLayout.add(this.host).width(250).row();
        this.menuLayout.add(this.port).width(250).padTop(25).row();
        this.menuLayout.add(this.sessionId).width(250).padTop(25).row();
        this.menuLayout.add(this.sessionPassword).width(250).padTop(25).row();
        this.menuLayout.add(this.informationMessage).width(250).padTop(25).row();
        this.menuLayout.add(this.joinButton).size(150, 50).padTop(100).row();
        this.menuLayout.add(this.createButton).size(150, 50).padTop(25).row();
        this.stage.addActor(this.menuLayout);
    }

    private void connectToServer() throws IOException {
        Network.register(client);
        client.start();
        client.connect(15000, host.getText(), Integer.parseInt(port.getText()), Integer.parseInt(port.getText()));
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(this.stage);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
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
        stage.dispose();
        skin.dispose();
    }
}