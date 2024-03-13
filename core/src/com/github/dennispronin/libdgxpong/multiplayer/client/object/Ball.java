package com.github.dennispronin.libdgxpong.multiplayer.client.object;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.github.dennispronin.libdgxpong.multiplayer.server.state.PlayerSide;

import static com.github.dennispronin.libdgxpong.Constants.*;

public class Ball {

    private final Rectangle rectangle;
    private float ballSpeed = INITIAL_BALL_SPEED;
    private float ballNextX;
    private float ballNextY;
    private float ballInitialX;
    private float ballInitialY;

    private final PlayerSide playerSide;

    private final Sound ballSound;
    private final Texture ballImage;

    public Ball(float ballInitialX, float ballInitialY, PlayerSide playerSide) {
        this.playerSide = playerSide;
        rectangle = new Rectangle();
        rectangle.width = 40f;
        rectangle.height = 40f;
        ballSound = Gdx.audio.newSound(Gdx.files.internal("ball.wav"));
        ballImage = new Texture(Gdx.files.internal("ball.png"));
        reset(ballInitialX, ballInitialY);
    }

    public void reset(float ballInitialX, float ballInitialY) {
        this.ballInitialX = ballInitialX;
        this.ballInitialY = ballInitialY;
        rectangle.x = WINDOW_WIDTH / 2 - rectangle.width / 2;
        rectangle.y = WINDOW_HEIGHT / 2 - rectangle.width / 2;
        ballSpeed = INITIAL_BALL_SPEED;
        defineInitialDirection();
    }

    public void moveBall(Rectangle leftPaddle, Rectangle rightPaddle) {
        double vectorX = ballNextX - rectangle.x;
        double vectorY = ballNextY - rectangle.y;
        rectangle.x = ballNextX;
        rectangle.y = ballNextY;
        if (areScreenSidesHit()) {
            stop();
        } else if (isRectangleHit(leftPaddle, rightPaddle)) {
            ballSound.play();
            deflectBallOfRectangle(leftPaddle, rightPaddle);
            increaseSpeed();
        } else {
            moveBallForward(vectorX, vectorY);
        }
    }

    private boolean isRectangleHit(Rectangle leftPaddle, Rectangle rightPaddle) {
        return Intersector.overlaps(rectangle, leftPaddle) || Intersector.overlaps(rectangle, rightPaddle);
    }

    /**
     * Player sends event to server about hitting other player
     * Player does not send event about getting hit by other player
     * I've made this logic to avoid duplicate score increments
     */
    public boolean areScreenSidesHit() {
        return (rectangle.x <= 0 && playerSide == PlayerSide.RIGHT)
                || (rectangle.x + rectangle.width >= WINDOW_WIDTH && playerSide == PlayerSide.LEFT);
    }

    private void stop() {
        this.ballSpeed = 0;
    }

    private void deflectBallOfRectangle(Rectangle leftRectangle, Rectangle rightRectangle) {
        if (Intersector.overlaps(rectangle, leftRectangle)) {
            ballNextX += ballSpeed;
            calculateRectangleHitYDirection(rectangle.y, leftRectangle.y);
        } else if (Intersector.overlaps(rectangle, rightRectangle)) {
            ballNextX -= ballSpeed;
            calculateRectangleHitYDirection(rectangle.y, rightRectangle.y);
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

    private void moveBallForward(double vectorX, double vectorY) {
        if (vectorX > 0) {
            if (rectangle.x + ballSpeed + rectangle.width < WINDOW_WIDTH) {
                ballNextX += ballSpeed;
            } else {
                ballNextX += WINDOW_WIDTH - rectangle.x - rectangle.width;
            }
        } else if (vectorX < 0) {
            if (rectangle.x - ballSpeed > 0) {
                ballNextX -= ballSpeed;
            } else {
                ballNextX -= rectangle.x;
            }
        }
        if (vectorY > 0) {
            if (rectangle.y + ballSpeed + rectangle.width > WINDOW_HEIGHT) {
                ballNextY -= ballSpeed;
            } else {
                ballNextY += ballSpeed;
            }
        } else if (vectorY < 0) {
            if (rectangle.y - ballSpeed < 0) {
                ballNextY += ballSpeed;
            } else {
                ballNextY -= ballSpeed;
            }
        }
    }


    private void defineInitialDirection() {
        if (ballInitialX == 0) {
            this.ballNextX = rectangle.x + ballSpeed;
        } else {
            this.ballNextX = rectangle.x - ballSpeed;
        }
        if (ballInitialY == 0) {
            this.ballNextY = rectangle.y + ballSpeed;
        } else {
            this.ballNextY = rectangle.y - ballSpeed;
        }
    }

    private void increaseSpeed() {
        if (ballSpeed != BALL_SPEED_LIMIT) {
            ballSpeed += 0.5f;
        }
    }

    public void draw(SpriteBatch spriteBatch) {
        spriteBatch.draw(ballImage, rectangle.x, rectangle.y, rectangle.width, rectangle.width);
    }

    public void dispose() {
        ballImage.dispose();
        ballSound.dispose();
    }
}