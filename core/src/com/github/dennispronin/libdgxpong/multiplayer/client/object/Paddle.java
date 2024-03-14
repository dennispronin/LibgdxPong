package com.github.dennispronin.libdgxpong.multiplayer.client.object;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.github.dennispronin.libdgxpong.multiplayer.server.state.PlayerSide;

import static com.github.dennispronin.libdgxpong.Constants.*;

public class Paddle {

    private final Rectangle rectangle;
    private final Texture rectangleImage;
    private final boolean isPlayable;

    public Paddle(PlayerSide playerSide, boolean isPlayable) {
        this.isPlayable = isPlayable;
        rectangleImage = new Texture(Gdx.files.internal("rectangle.png"));

        rectangle = new Rectangle();
        rectangle.y = WINDOW_HEIGHT / 2 - RECTANGLE_HEIGHT / 2;
        if (playerSide == PlayerSide.LEFT) {
            rectangle.x = 0;
        } else {
            rectangle.x = WINDOW_WIDTH - RECTANGLE_WIDTH;
        }
        rectangle.width = RECTANGLE_WIDTH;
        rectangle.height = RECTANGLE_HEIGHT;
    }

    public boolean isKeyPressed() {
        boolean isKeyPressed = false;
        if (isPlayable) {
            if (Gdx.input.isKeyPressed(Input.Keys.S) || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                rectangle.y -= RECTANGLE_SPEED;
                isKeyPressed = true;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.W) || Gdx.input.isKeyPressed(Input.Keys.UP)) {
                rectangle.y += RECTANGLE_SPEED;
                isKeyPressed = true;
            }
            if (rectangle.y < 0) rectangle.y = 0;
            if (rectangle.y > WINDOW_HEIGHT - RECTANGLE_HEIGHT) rectangle.y = WINDOW_HEIGHT - RECTANGLE_HEIGHT;
        }
        return isKeyPressed;
    }

    public Rectangle getPaddleFrame() {
        return rectangle;
    }

    public void draw(SpriteBatch spriteBatch) {
        spriteBatch.draw(rectangleImage, rectangle.x, rectangle.y, RECTANGLE_WIDTH, RECTANGLE_HEIGHT);
    }

    public void dispose() {
        rectangleImage.dispose();
    }
}