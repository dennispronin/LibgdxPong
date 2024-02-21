package com.github.dennispronin.libdgxpong;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;

public class LibgdxPong extends ApplicationAdapter {

    private final float windowHeight = 480;
    private final float windowWidth = 800;

    private final float rectangleWidth = 24;
    private final float rectangleHeight = 90;

    private static final double RECTANGLE_SPEED = 8d;

    private OrthographicCamera camera;
    private SpriteBatch spriteBatch;
    private ShapeRenderer shapeRenderer;

    private Texture ballImage;
    private Texture rectangleImage;
    private Sound ballSound;

    private Circle circle;
    private Rectangle leftRectangle;
    private Rectangle rightRectangle;
    private Rectangle centerLine;

    @Override
    public void create() {
        ballImage = new Texture(Gdx.files.internal("ball.png"));
        rectangleImage = new Texture(Gdx.files.internal("rectangle.png"));
        ballSound = Gdx.audio.newSound(Gdx.files.internal("ball.wav"));
        camera = new OrthographicCamera();
        camera.setToOrtho(false, windowWidth, windowHeight);
        spriteBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();

        leftRectangle = new Rectangle();
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
    }

    @Override
    public void render() {
        ScreenUtils.clear(Color.BLACK);
        camera.update();

        drawObjects();
        handleKeyPressed();
    }

    private void drawObjects() {
        spriteBatch.setProjectionMatrix(camera.combined);
        spriteBatch.begin();
        spriteBatch.draw(rectangleImage, leftRectangle.x, leftRectangle.y, rectangleWidth, rectangleHeight);
        spriteBatch.draw(rectangleImage, rightRectangle.x, rightRectangle.y, rectangleWidth, rectangleHeight);
        spriteBatch.draw(rectangleImage, centerLine.x, centerLine.y, 3, windowHeight);
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

    @Override
    public void dispose() {
        ballImage.dispose();
        rectangleImage.dispose();
        ballSound.dispose();
        spriteBatch.dispose();
    }
}