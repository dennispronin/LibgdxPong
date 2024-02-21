package com.github.dennispronin.libdgxpong;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.Random;

import static com.github.dennispronin.libdgxpong.Constants.*;

public class LibgdxPong extends ApplicationAdapter {

    private float ballSpeed = INITIAL_BALL_SPEED;
    private float ballNextX;
    private float ballNextY;
    private final Random random = new Random();

    private OrthographicCamera camera;
    private SpriteBatch spriteBatch;

    private Texture ballImage;
    private Texture rectangleImage;
    private Sound ballSound;

    private Rectangle ball;
    private Rectangle leftRectangle;
    private Rectangle rightRectangle;
    private Rectangle centerLine;

    private int player1Score = 0;
    /**
     * For some reason the ball's initial x is 0, even though I set it in the create()
     * It leads to scoreHit() being triggered and player2Score being incremented
     */
    private int player2Score = -1;

    @Override
    public void create() {
        ballImage = new Texture(Gdx.files.internal("ball.png"));
        rectangleImage = new Texture(Gdx.files.internal("rectangle.png"));
        ballSound = Gdx.audio.newSound(Gdx.files.internal("ball.wav"));
        camera = new OrthographicCamera();
        camera.setToOrtho(false, WINDOW_WIDTH, WINDOW_HEIGHT);
        spriteBatch = new SpriteBatch();

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
    }

    @Override
    public void render() {
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

        spriteBatch.draw(rectangleImage, leftRectangle.x, leftRectangle.y, RECTANGLE_WIDTH, RECTANGLE_HEIGHT);
        spriteBatch.draw(rectangleImage, rightRectangle.x, rightRectangle.y, RECTANGLE_WIDTH, RECTANGLE_HEIGHT);
        spriteBatch.draw(rectangleImage, centerLine.x, centerLine.y, 3, WINDOW_HEIGHT);
        spriteBatch.end();
    }

    private void handleKeyPressed() {
        if (Gdx.input.isKeyPressed(Input.Keys.S)) leftRectangle.y -= RECTANGLE_SPEED;
        if (Gdx.input.isKeyPressed(Input.Keys.W)) leftRectangle.y += RECTANGLE_SPEED;
        if (leftRectangle.y < 0) leftRectangle.y = 0;
        if (leftRectangle.y > WINDOW_HEIGHT - RECTANGLE_HEIGHT) leftRectangle.y = WINDOW_HEIGHT - RECTANGLE_HEIGHT;

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) rightRectangle.y -= RECTANGLE_SPEED;
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) rightRectangle.y += RECTANGLE_SPEED;
        if (rightRectangle.y < 0) rightRectangle.y = 0;
        if (rightRectangle.y > WINDOW_HEIGHT - RECTANGLE_HEIGHT) rightRectangle.y = WINDOW_HEIGHT - RECTANGLE_HEIGHT;
    }

    private void handleBall() {
        double vectorX = ballNextX - ball.x;
        double vectorY = ballNextY - ball.y;
        ball.x = ballNextX;
        ball.y = ballNextY;
        if (isRectangleHit()) {
            deflectBallOfRectangle();
            increaseBallSpeed();
        } else if (isScoreHit()) {
            scoreHit();
            resetBall();
        } else {
            moveBallForward(vectorX, vectorY);
        }
    }

    private boolean isScoreHit() {
        return ball.x <= 0 || ball.x + ball.width >= WINDOW_WIDTH;
    }

    public void scoreHit() {
        // fixme change to precise value
        if (ball.x <= 0) {
            player2Score++;
        // fixme change to precise value
        } else if (ball.x + ball.width >= WINDOW_WIDTH) {
            player1Score++;
        }
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
        ballSound.play();
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

    private void resetBall() {
        ball.x = WINDOW_WIDTH / 2 - ball.width / 2;
        ball.y = WINDOW_HEIGHT / 2 - ball.width / 2;
        ballSpeed = INITIAL_BALL_SPEED;
        defineBallInitialDirection();
    }

    private void defineBallInitialDirection() {
        random.nextInt(2);
        if (random.nextInt(2) == 0) {
            this.ballNextX = ball.x + ballSpeed;
        } else {
            this.ballNextX = ball.x - ballSpeed;
        }
        if (random.nextInt(2) == 0) {
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

    @Override
    public void dispose() {
        ballImage.dispose();
        rectangleImage.dispose();
        ballSound.dispose();
        spriteBatch.dispose();
    }
}