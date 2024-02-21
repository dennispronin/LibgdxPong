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

public class LibgdxPong extends ApplicationAdapter {

    private final float windowHeight = 480f;
    private final float windowWidth = 800f;

    private final float rectangleWidth = 24f;
    private final float rectangleHeight = 90f;

    private static final float INITIAL_BALL_SPEED = 4f;
    private static final float BALL_SPEED_LIMIT = 10f;
    private float ballSpeed = INITIAL_BALL_SPEED;
    private float ballNextX;
    private float ballNextY;
    private final Random random = new Random();

    private static final double RECTANGLE_SPEED = 8f;

    private OrthographicCamera camera;
    private SpriteBatch spriteBatch;

    private Texture ballImage;
    private Texture rectangleImage;
    private Sound ballSound;

    private Circle ball;
    private Rectangle leftRectangle;
    private Rectangle rightRectangle;
    private Rectangle centerLine;

    private int player1Score = 0;
    private int player2Score = 0;

    @Override
    public void create() {
        ballImage = new Texture(Gdx.files.internal("ball.png"));
        rectangleImage = new Texture(Gdx.files.internal("rectangle.png"));
        ballSound = Gdx.audio.newSound(Gdx.files.internal("ball.wav"));
        camera = new OrthographicCamera();
        camera.setToOrtho(false, windowWidth, windowHeight);
        spriteBatch = new SpriteBatch();

        leftRectangle = new Rectangle();
        leftRectangle.x = 0;
        leftRectangle.y = windowHeight / 2 - rectangleHeight / 2;
        leftRectangle.width = rectangleWidth;
        leftRectangle.height = rectangleHeight;

        rightRectangle = new Rectangle();
        rightRectangle.y = windowHeight / 2 - rectangleHeight / 2;
        rightRectangle.x = windowWidth - rectangleWidth;
        rightRectangle.width = rectangleWidth;
        rightRectangle.height = rectangleHeight;

        centerLine = new Rectangle();
        centerLine.y = 0;
        centerLine.x = windowWidth / 2 - 3;
        centerLine.width = 3;
        centerLine.height = windowHeight;

        ball = new Circle();
        ball.radius = 40f;
        ball.y = windowHeight / 2 - ball.radius / 2;
        ball.x = windowWidth / 2 - ball.radius / 2;
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
        spriteBatch.draw(rectangleImage, leftRectangle.x, leftRectangle.y, rectangleWidth, rectangleHeight);
        spriteBatch.draw(rectangleImage, rightRectangle.x, rightRectangle.y, rectangleWidth, rectangleHeight);
        spriteBatch.draw(rectangleImage, centerLine.x, centerLine.y, 3, windowHeight);
        spriteBatch.draw(ballImage, ball.x, ball.y, ball.radius, ball.radius);
        spriteBatch.end();
    }

    private void handleKeyPressed() {
        if (Gdx.input.isKeyPressed(Input.Keys.S)) leftRectangle.y -= RECTANGLE_SPEED;
        if (Gdx.input.isKeyPressed(Input.Keys.W)) leftRectangle.y += RECTANGLE_SPEED;
        if (leftRectangle.y < 0) leftRectangle.y = 0;
        if (leftRectangle.y > windowHeight - rectangleHeight) leftRectangle.y = windowHeight - rectangleHeight;

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) rightRectangle.y -= RECTANGLE_SPEED;
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) rightRectangle.y += RECTANGLE_SPEED;
        if (rightRectangle.y < 0) rightRectangle.y = 0;
        if (rightRectangle.y > windowHeight - rectangleHeight) rightRectangle.y = windowHeight - rectangleHeight;
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
        return ball.x - ball.radius <= 0 || ball.x + ball.radius >= windowWidth;
    }

    public void scoreHit() {
        if (ball.x - ball.radius == 0) {
            player2Score++;
        } else if (ball.x + ball.radius == windowWidth) {
            player1Score++;
        }
    }

    private void moveBallForward(double vectorX, double vectorY) {
        if (vectorX > 0) {
            if (ball.x + ballSpeed + ball.radius < windowWidth) {
                ballNextX += ballSpeed;
            } else {
                ballNextX += windowWidth - ball.x - ball.radius;
            }
        }
        else if (vectorX < 0) {
            if (ball.x - ballSpeed - ball.radius > 0) {
                ballNextX -= ballSpeed;
            } else {
                ballNextX -= ball.x - ball.radius;
            }
        }
        if (vectorY > 0) {
            if (ball.y + ballSpeed + ball.radius > windowHeight) {
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
        ball.x = windowWidth / 2 - ball.radius / 2;
        ball.y = windowHeight / 2 - ball.radius / 2;
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