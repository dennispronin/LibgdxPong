package com.github.dennispronin.libdgxpong.multiplayer.example.client.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;
import com.esotericsoftware.kryonet.Connection;
import com.github.dennispronin.libdgxpong.multiplayer.example.client.PlayerSide;
import com.github.dennispronin.libdgxpong.multiplayer.example.client.events.MoveRectangleClientEvent;
import com.github.dennispronin.libdgxpong.multiplayer.example.client.events.ScoreHitClientEvent;
import com.github.dennispronin.libdgxpong.multiplayer.example.client.object.Ball;

import static com.github.dennispronin.libdgxpong.Constants.*;

public class GameScreen implements Screen {

    private final Ball ball;
    private final Texture rectangleImage;
    private final Rectangle leftRectangle;
    private final Rectangle rightRectangle;

    private final BitmapFont font;
    private int leftPlayerScore;
    private int rightPlayerScore;

    private final OrthographicCamera camera;
    private final SpriteBatch spriteBatch;

    private final Rectangle centerLine;

    private final PlayerSide playerSide;
    private final Connection connection;
    private final String sessionId;
    boolean isWaitingForServerResponse = false;

    public GameScreen(int leftPlayerScore, int rightPlayerScore, float ballInitialX, float ballInitialY, PlayerSide playerSide, Connection connection, String sessionId) {
        this.leftPlayerScore = leftPlayerScore;
        this.rightPlayerScore = rightPlayerScore;
        this.playerSide = playerSide;
        this.connection = connection;
        this.sessionId = sessionId;
        rectangleImage = new Texture(Gdx.files.internal("rectangle.png"));
        camera = new OrthographicCamera();
        camera.setToOrtho(false, WINDOW_WIDTH, WINDOW_HEIGHT);
        spriteBatch = new SpriteBatch();

        font = new BitmapFont();

        leftRectangle = new Rectangle();
        leftRectangle.x = 0;
        leftRectangle.y = WINDOW_HEIGHT / 2 - RECTANGLE_HEIGHT / 2;
        leftRectangle.width = RECTANGLE_WIDTH;
        leftRectangle.height = RECTANGLE_HEIGHT;

        rightRectangle = new Rectangle();
        rightRectangle.y = WINDOW_HEIGHT / 2 - RECTANGLE_HEIGHT / 2;
        rightRectangle.x = WINDOW_WIDTH - RECTANGLE_WIDTH;
        rightRectangle.width = RECTANGLE_WIDTH;
        rightRectangle.height = RECTANGLE_HEIGHT;

        centerLine = new Rectangle();
        centerLine.y = 0;
        centerLine.x = WINDOW_WIDTH / 2 - 3;
        centerLine.width = 3;
        centerLine.height = WINDOW_HEIGHT;

        ball = new Ball(ballInitialX, ballInitialY, playerSide);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        camera.update();
        drawObjects();
        ball.moveBall(leftRectangle, rightRectangle);
        if (ball.areScreenSidesHit()) sendScoreEvent();
        handleKeyPressed();
    }

    private void sendScoreEvent() {
        if (!isWaitingForServerResponse) {
            connection.sendTCP(new ScoreHitClientEvent(playerSide, sessionId));
            isWaitingForServerResponse= true;
        }
    }

    private void drawObjects() {
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();

        ball.draw(spriteBatch);

        font.draw(spriteBatch, String.valueOf(leftPlayerScore), WINDOW_WIDTH / 2 - 20, WINDOW_HEIGHT - 20);
        font.draw(spriteBatch, String.valueOf(rightPlayerScore), WINDOW_WIDTH / 2 + 8, WINDOW_HEIGHT - 20);

        spriteBatch.draw(rectangleImage, leftRectangle.x, leftRectangle.y, RECTANGLE_WIDTH, RECTANGLE_HEIGHT);
        spriteBatch.draw(rectangleImage, rightRectangle.x, rightRectangle.y, RECTANGLE_WIDTH, RECTANGLE_HEIGHT);
        spriteBatch.draw(rectangleImage, centerLine.x, centerLine.y, 3, WINDOW_HEIGHT);
        spriteBatch.end();
    }

    public void moveOtherPlayer(float y) {
        if (playerSide == PlayerSide.LEFT) {
            rightRectangle.y = y;
        } else {
            leftRectangle.y = y;
        }
    }

    public void startRound(int leftPlayerScore, int rightPlayerScore, float ballInitialX, float ballInitialY) {
        this.leftPlayerScore = leftPlayerScore;
        this.rightPlayerScore = rightPlayerScore;
        this.isWaitingForServerResponse = false;
        ball.reset(ballInitialX, ballInitialY);
    }

    private void handleKeyPressed() {
        if (playerSide == PlayerSide.LEFT) {
            if (Gdx.input.isKeyPressed(Input.Keys.S)) leftRectangle.y -= RECTANGLE_SPEED;
            if (Gdx.input.isKeyPressed(Input.Keys.W)) leftRectangle.y += RECTANGLE_SPEED;
            if (leftRectangle.y < 0) leftRectangle.y = 0;
            if (leftRectangle.y > WINDOW_HEIGHT - RECTANGLE_HEIGHT) leftRectangle.y = WINDOW_HEIGHT - RECTANGLE_HEIGHT;
            connection.sendTCP(new MoveRectangleClientEvent(sessionId, leftRectangle.y));
        }
        if (playerSide == PlayerSide.RIGHT) {
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) rightRectangle.y -= RECTANGLE_SPEED;
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) rightRectangle.y += RECTANGLE_SPEED;
            if (rightRectangle.y < 0) rightRectangle.y = 0;
            if (rightRectangle.y > WINDOW_HEIGHT - RECTANGLE_HEIGHT)
                rightRectangle.y = WINDOW_HEIGHT - RECTANGLE_HEIGHT;
            connection.sendTCP(new MoveRectangleClientEvent(sessionId, rightRectangle.y));
        }
    }

    @Override
    public void show() {

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
        font.dispose();
        ball.dispose();
        rectangleImage.dispose();
        spriteBatch.dispose();
    }
}