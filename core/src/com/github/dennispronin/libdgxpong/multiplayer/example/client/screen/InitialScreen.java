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
import com.esotericsoftware.kryonet.Client;
import com.github.dennispronin.libdgxpong.Network;
import com.github.dennispronin.libdgxpong.multiplayer.example.client.PongGame;
import com.github.dennispronin.libdgxpong.multiplayer.example.server.request.CreateRequest;
import com.github.dennispronin.libdgxpong.multiplayer.example.server.request.JoinRequest;

import static com.github.dennispronin.libdgxpong.Constants.*;

public class InitialScreen implements Screen {

    private final PongGame pongGame;
    private final Stage stage = new Stage();
    private final Client client = new Client();

    private final Skin skin = new Skin(Gdx.files.internal("uiskin.json"));
    private final Table menuLayout = new Table();
    private final TextField host = new TextField(SERVER_HOST, skin);
    private final TextField port = new TextField(String.valueOf(SERVER_PORT), skin);
    private final TextField playerName = new TextField("Enter your name", skin);
    private final TextField sessionPassword = new TextField("Enter session password", skin);
    private final TextButton joinButton = new TextButton("Join session", skin);
    private final TextButton createButton = new TextButton("Create session", skin);

    public InitialScreen(PongGame pongGame) {
        this.pongGame = pongGame;
        this.menuLayout.setBounds(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);

        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false, WINDOW_WIDTH, WINDOW_HEIGHT);

        addConnectButtonListener();
        addCreateButtonListener();
        fillLayout();
    }

    private void addConnectButtonListener() {
        this.joinButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // TODO добавить листенеров а ля client.addListener(new ConnectionStateListener());
                //  Когда от сервера придет ивент StartGame, сменить экран на pongGame.setScreen(new GameScreen(game));
                //        и каким-то макаром вызвать dispose(); этого экрана
                // TODO player controls right paddle in this case
                connectToServer();
                client.sendTCP(new JoinRequest(playerName.getText(), sessionPassword.getText()));
                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }

    private void addCreateButtonListener() {
        this.createButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                // TODO добавить листенеров а ля client.addListener(new ConnectionStateListener());
                //  Когда от сервера придет ивент StartGame, сменить экран на pongGame.setScreen(new GameScreen(game));
                //        и каким-то макаром вызвать dispose(); этого экрана
                // TODO player controls left paddle in this case
                connectToServer();
                client.sendTCP(new CreateRequest(playerName.getText(), sessionPassword.getText()));
                return super.touchDown(event, x, y, pointer, button);
            }
        });
    }

        private void fillLayout() {
        this.menuLayout.clear();
        this.menuLayout.add(this.host).width(250).row();
        this.menuLayout.add(this.port).width(250).padTop(25).row();
        this.menuLayout.add(this.playerName).width(250).padTop(25).row();
        this.menuLayout.add(this.sessionPassword).width(250).padTop(25).row();
        this.menuLayout.add(this.joinButton).size(150, 50).padTop(100).row();
        this.menuLayout.add(this.createButton).size(150, 50).padTop(25).row();

        this.stage.addActor(this.menuLayout);
    }

    private void connectToServer() {
        Network.register(client);
        try {
            client.start();
            client.connect(15000, host.getText(), Integer.parseInt(port.getText()), Integer.parseInt(port.getText()));
        } catch (Exception e) {
            System.exit(1);
        }
        pongGame.setClient(client);
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
        stage.dispose();
        skin.dispose();
    }
}