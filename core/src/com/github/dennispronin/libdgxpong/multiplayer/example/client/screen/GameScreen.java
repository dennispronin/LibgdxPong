package com.github.dennispronin.libdgxpong.multiplayer.example.client.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.ScreenUtils;
import com.esotericsoftware.kryonet.Connection;
import com.github.dennispronin.libdgxpong.multiplayer.example.client.PlayerSide;
import com.github.dennispronin.libdgxpong.multiplayer.example.server.request.MoveRectangleEvent;
import com.github.dennispronin.libdgxpong.multiplayer.example.server.request.ScoreEvent;

import static com.github.dennispronin.libdgxpong.Constants.*;

public class GameScreen implements Screen {

    private final Rectangle ball;
    private float ballSpeed = INITIAL_BALL_SPEED;
    private float ballNextX;
    private float ballNextY;
    private final Sound ballSound;
    private float ballInitialX;
    private float ballInitialY;

    private final Rectangle leftRectangle;
    private final Rectangle rightRectangle;
    private int leftPlayerScore;
    private int rightPlayerScore;

    private final OrthographicCamera camera;
    private final SpriteBatch spriteBatch;

    private final BitmapFont font;
    private final Texture ballImage;
    private final Texture rectangleImage;
    private final Rectangle centerLine;

    private final PlayerSide playerSide;
    private final Connection connection;
    private final String sessionId;

    public GameScreen(int leftPlayerScore, int rightPlayerScore, float ballInitialX, float ballInitialY, PlayerSide playerSide, Connection connection, String sessionId) {
        this.leftPlayerScore = leftPlayerScore;
        this.rightPlayerScore = rightPlayerScore;
        this.ballInitialX = ballInitialX;
        this.ballInitialY = ballInitialY;
        this.playerSide = playerSide;
        this.connection = connection;
        this.sessionId = sessionId;
        ballImage = new Texture(Gdx.files.internal("ball.png"));
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

        ball = new Rectangle();
        ball.width = 40f;
        ball.height = 40f;
        ball.y = WINDOW_HEIGHT / 2 - ball.width / 2;
        ball.x = WINDOW_WIDTH / 2 - ball.width / 2;
        ballSound = Gdx.audio.newSound(Gdx.files.internal("ball.wav"));
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        camera.update();
        drawObjects();
        handleBall();
        handleKeyPressed();
    }

    private void drawObjects() {
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        spriteBatch.draw(ballImage, ball.x, ball.y, ball.width, ball.width);

        font.draw(spriteBatch, String.valueOf(leftPlayerScore), WINDOW_WIDTH / 2 - 20, WINDOW_HEIGHT - 20);
        font.draw(spriteBatch, String.valueOf(rightPlayerScore), WINDOW_WIDTH / 2 + 8, WINDOW_HEIGHT - 20);

        spriteBatch.draw(rectangleImage, leftRectangle.x, leftRectangle.y, RECTANGLE_WIDTH, RECTANGLE_HEIGHT);
        spriteBatch.draw(rectangleImage, rightRectangle.x, rightRectangle.y, RECTANGLE_WIDTH, RECTANGLE_HEIGHT);
        spriteBatch.draw(rectangleImage, centerLine.x, centerLine.y, 3, WINDOW_HEIGHT);
        spriteBatch.end();
    }

    public void handleBall() {
        double vectorX = ballNextX - ball.x;
        double vectorY = ballNextY - ball.y;
        ball.x = ballNextX;
        ball.y = ballNextY;
        if (isRectangleHit()) {
            ballSound.play();
            deflectBallOfRectangle();
            increaseBallSpeed();
        } else if (isScoreHit()) {
            sendScoreEvent();
        } else {
            moveBallForward(vectorX, vectorY);
        }
    }

    public void moveOtherPlayer(float y) {
        if (playerSide == PlayerSide.LEFT) {
            rightRectangle.y = y;
        } else {
            leftRectangle.y = y;
        }
    }

    /**
     * Player sends event to server about hitting other player
     * Player does not send event about getting hit by other player
     * I've made this logic to avoid duplicate score increments
     */
    private boolean isScoreHit() {
        return (ball.x <= 0 && playerSide == PlayerSide.RIGHT)
                || (ball.x + ball.width >= WINDOW_WIDTH && playerSide == PlayerSide.LEFT);
    }

    private void sendScoreEvent() {
        connection.sendTCP(new ScoreEvent(playerSide, sessionId));
    }

    private void moveBallForward(double vectorX, double vectorY) {
        if (vectorX > 0) {
            if (ball.x + ballSpeed + ball.width < WINDOW_WIDTH) {
                ballNextX += ballSpeed;
            } else {
                ballNextX += WINDOW_WIDTH - ball.x - ball.width;
            }
        } else if (vectorX < 0) {
            if (ball.x - ballSpeed > 0) {
                ballNextX -= ballSpeed;
            } else {
                ballNextX -= ball.x;
            }
        }
        if (vectorY > 0) {
            if (ball.y + ballSpeed + ball.width > WINDOW_HEIGHT) {
                ballNextY -= ballSpeed;
            } else {
                ballNextY += ballSpeed;
            }
        } else if (vectorY < 0) {
            if (ball.y - ballSpeed < 0) {
                ballNextY += ballSpeed;
            } else {
                ballNextY -= ballSpeed;
            }
        }
    }

    private void deflectBallOfRectangle() {
        if (Intersector.overlaps(ball, leftRectangle)) {
            ballNextX += ballSpeed;
            calculateRectangleHitYDirection(ball.y, leftRectangle.y);
        } else if (Intersector.overlaps(ball, rightRectangle)) {
            ballNextX -= ballSpeed;
            calculateRectangleHitYDirection(ball.y, rightRectangle.y);
        }
    }

    private void calculateRectangleHitYDirection(float ballY, float rectangleY) {
        if (ballY < rectangleY + 40) {
            // upper part of rectangle
            ballNextY -= ballSpeed;
        } else if (ballY < rectangleY + 50) {
            // middle part of rectangle
            ballNextY = ballY;
        } else {
            // lower part of rectangle
            ballNextY += ballSpeed;
        }
    }

    private boolean isRectangleHit() {
        return Intersector.overlaps(ball, leftRectangle) || Intersector.overlaps(ball, rightRectangle);
    }

    public void resetBall() {
        ball.x = WINDOW_WIDTH / 2 - ball.width / 2;
        ball.y = WINDOW_HEIGHT / 2 - ball.width / 2;
        ballSpeed = INITIAL_BALL_SPEED;
        defineBallInitialDirection();
    }

    private void defineBallInitialDirection() {
        if (ballInitialX == 0) {
            this.ballNextX = ball.x + ballSpeed;
        } else {
            this.ballNextX = ball.x - ballSpeed;
        }
        if (ballInitialY == 0) {
            this.ballNextY = ball.y + ballSpeed;
        } else {
            this.ballNextY = ball.y - ballSpeed;
        }
    }

    private void increaseBallSpeed() {
        if (ballSpeed != BALL_SPEED_LIMIT) {
            ballSpeed += 0.5f;
        }
    }

    private void handleKeyPressed() {
        if (playerSide == PlayerSide.LEFT) {
            if (Gdx.input.isKeyPressed(Input.Keys.S)) leftRectangle.y -= RECTANGLE_SPEED;
            if (Gdx.input.isKeyPressed(Input.Keys.W)) leftRectangle.y += RECTANGLE_SPEED;
            if (leftRectangle.y < 0) leftRectangle.y = 0;
            if (leftRectangle.y > WINDOW_HEIGHT - RECTANGLE_HEIGHT) leftRectangle.y = WINDOW_HEIGHT - RECTANGLE_HEIGHT;
            connection.sendTCP(new MoveRectangleEvent(sessionId, leftRectangle.y));
        }
        if (playerSide == PlayerSide.RIGHT) {
            if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) rightRectangle.y -= RECTANGLE_SPEED;
            if (Gdx.input.isKeyPressed(Input.Keys.UP)) rightRectangle.y += RECTANGLE_SPEED;
            if (rightRectangle.y < 0) rightRectangle.y = 0;
            if (rightRectangle.y > WINDOW_HEIGHT - RECTANGLE_HEIGHT)
                rightRectangle.y = WINDOW_HEIGHT - RECTANGLE_HEIGHT;
            connection.sendTCP(new MoveRectangleEvent(sessionId, rightRectangle.y));
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
        ballImage.dispose();
        rectangleImage.dispose();
        spriteBatch.dispose();
    }

    public void setLeftPlayerScore(int leftPlayerScore) {
        this.leftPlayerScore = leftPlayerScore;
    }

    public void setRightPlayerScore(int rightPlayerScore) {
        this.rightPlayerScore = rightPlayerScore;
    }

    public void setBallInitialX(float ballInitialX) {
        this.ballInitialX = ballInitialX;
    }

    public void setBallInitialY(float ballInitialY) {
        this.ballInitialY = ballInitialY;
    }
}