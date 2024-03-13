package com.github.dennispronin.libdgxpong.multiplayer.example.client.screen;

import com.badlogic.gdx.Gdx;
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
import com.github.dennispronin.libdgxpong.multiplayer.example.client.object.Paddle;

import static com.github.dennispronin.libdgxpong.Constants.WINDOW_HEIGHT;
import static com.github.dennispronin.libdgxpong.Constants.WINDOW_WIDTH;
import static com.github.dennispronin.libdgxpong.multiplayer.example.client.PlayerSide.LEFT;
import static com.github.dennispronin.libdgxpong.multiplayer.example.client.PlayerSide.RIGHT;

public class GameScreen implements Screen {

    private final Ball ball;
    private final Paddle leftPaddle;
    private final Paddle rightPaddle;

    private final BitmapFont font;
    private int leftPlayerScore;
    private int rightPlayerScore;

    private final OrthographicCamera camera;
    private final SpriteBatch spriteBatch;

    private final Rectangle centerLine;
    private final Texture rectangleImage;

    private final PlayerSide playerSide;
    private final Connection connection;
    private final String sessionId;
    boolean isWaitingForServerResponse = false;

    public GameScreen(int leftPlayerScore, int rightPlayerScore, float ballInitialX, float ballInitialY,
                      PlayerSide playerSide, Connection connection, String sessionId) {
        this.leftPlayerScore = leftPlayerScore;
        this.rightPlayerScore = rightPlayerScore;
        this.playerSide = playerSide;
        this.connection = connection;
        this.sessionId = sessionId;
        camera = new OrthographicCamera();
        camera.setToOrtho(false, WINDOW_WIDTH, WINDOW_HEIGHT);
        spriteBatch = new SpriteBatch();

        rectangleImage = new Texture(Gdx.files.internal("rectangle.png"));
        centerLine = new Rectangle();
        centerLine.y = 0;
        centerLine.x = WINDOW_WIDTH / 2 - 3;
        centerLine.width = 3;
        centerLine.height = WINDOW_HEIGHT;

        font = new BitmapFont();
        leftPaddle = new Paddle(LEFT, playerSide == LEFT);
        rightPaddle = new Paddle(RIGHT, playerSide == RIGHT);
        ball = new Ball(ballInitialX, ballInitialY, playerSide);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        camera.update();
        drawObjects();
        ball.moveBall(leftPaddle.getPaddleFrame(), rightPaddle.getPaddleFrame());
        if (ball.areScreenSidesHit()) sendScoreEvent();
        if (leftPaddle.isKeyPressed()) {
            connection.sendTCP(new MoveRectangleClientEvent(sessionId, leftPaddle.getPaddleFrame().y));
        } else if (rightPaddle.isKeyPressed()) {
            connection.sendTCP(new MoveRectangleClientEvent(sessionId, rightPaddle.getPaddleFrame().y));
        }
    }

    private void sendScoreEvent() {
        if (!isWaitingForServerResponse) {
            connection.sendTCP(new ScoreHitClientEvent(playerSide, sessionId));
            isWaitingForServerResponse = true;
        }
    }

    private void drawObjects() {
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();

        ball.draw(spriteBatch);
        leftPaddle.draw(spriteBatch);
        rightPaddle.draw(spriteBatch);

        font.draw(spriteBatch, String.valueOf(leftPlayerScore), WINDOW_WIDTH / 2 - 20, WINDOW_HEIGHT - 20);
        font.draw(spriteBatch, String.valueOf(rightPlayerScore), WINDOW_WIDTH / 2 + 8, WINDOW_HEIGHT - 20);
        spriteBatch.draw(rectangleImage, centerLine.x, centerLine.y, 3, WINDOW_HEIGHT);
        spriteBatch.end();
    }

    public void moveOtherPlayer(float y) {
        if (playerSide == LEFT) {
            rightPaddle.getPaddleFrame().y = y;
        } else {
            leftPaddle.getPaddleFrame().y = y;
        }
    }

    public void startRound(int leftPlayerScore, int rightPlayerScore, float ballInitialX, float ballInitialY) {
        this.leftPlayerScore = leftPlayerScore;
        this.rightPlayerScore = rightPlayerScore;
        this.isWaitingForServerResponse = false;
        ball.reset(ballInitialX, ballInitialY);
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
        leftPaddle.dispose();
        rightPaddle.dispose();
        spriteBatch.dispose();
    }
}