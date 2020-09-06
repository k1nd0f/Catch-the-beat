package com.kindof.catchthebeat.beatmaps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.XmlReader;
import com.kindof.catchthebeat.resources.Settings;
import com.kindof.catchthebeat.gameobjects.catcher.Catcher;
import com.kindof.catchthebeat.gameobjects.fruit.Fruit;
import com.kindof.catchthebeat.gameobjects.fruit.Overlap;
import com.kindof.catchthebeat.resources.Globals;
import com.kindof.catchthebeat.tools.Time;

import java.util.Iterator;
import java.util.LinkedList;

public class Beatmap implements Disposable {
    private XmlReader.Element element;
    private MetaData metaData;
    private Parameters parameters;
    private int numberOfCurrentFruit;
    private float offset, songPos, fruitSpawnTime; // in seconds
    private Fruit fruit;
    private String samples;
    private LinkedList<Fruit> fruitSet;
    private Background background;
    private Music audio;
    private Long id;

    public Beatmap(Long id) {
        this.id = id;
        initialization();
        setMusicVolume(Settings.MUSIC_VOLUME);
    }

    private void initialization() {
        XmlReader reader = new XmlReader();
        element = reader.parse(Gdx.files.local(Globals.LOCAL_PATH_TO_SONGS_DIRECTORY + id + "/config.xml").reader("UTF-8"));

        // Parameters
        element = element.getChildByName("Parameters");
        parameters = new Parameters(element.getFloat("ScrollSpeed") * Globals.RESOLUTION_HEIGHT_SCALE, element.getFloat("Difficulty"), element.getFloat("HealthRate"), element.getFloat("BPM"), element.getInt("FruitCount"));

        // MetaData
        element = element.getParent().getChildByName("MetaData");
        metaData = new MetaData(element.get("Title"), element.get("Artist"), element.get("Creator"), element.get("Tags", ""));

        // General
        element = element.getParent().getChildByName("General");
        background = new Background(id, element.get("BackgroundFileName", null), Settings.BEATMAP_BG_BRIGHTNESS);
        offset = Time.millisToSeconds(element.getFloat("Offset"));
        samples = element.get("Samples", "");

        audio = Gdx.audio.newMusic(Gdx.files.local(Globals.LOCAL_PATH_TO_SONGS_DIRECTORY + id + "/" + element.get("AudioFileName")));
        numberOfCurrentFruit = 0;
        fruitSet = new LinkedList<>();
        element = reader.parse(Gdx.files.local(Globals.LOCAL_PATH_TO_SONGS_DIRECTORY + id + "/" + element.get("FruitSetFileName"))).getChild(numberOfCurrentFruit);
    }

    public void draw(SpriteBatch batch) {
        background.draw(batch);
        for (Fruit fruit: fruitSet) {
            fruit.draw(batch);
        }
    }

    public void update(float delta, Catcher catcher) {
        updateFruitsPosition(delta, catcher);
        updateSongPosition(delta);

        if (songPos >= fruitSpawnTime && numberOfCurrentFruit <= parameters.fruitCount) {
            float missedTime = songPos - fruitSpawnTime;
            fruit.setY(fruit.getY() - missedTime * parameters.scrollSpeed);
            fruitSet.add(fruit);
            if (numberOfCurrentFruit < parameters.fruitCount) {
                nextFruit();
            } else if (numberOfCurrentFruit == parameters.fruitCount) {
                numberOfCurrentFruit++;
            }
        }
    }

    private void updateFruitsPosition(float delta, Catcher catcher) {
        Iterator<Fruit> iterator = fruitSet.iterator();
        while (iterator.hasNext()){
            Fruit fruit = iterator.next();
            Overlap overlap = fruit.boundsOverlap(catcher.getBounds());
            if (overlap.getType() != Overlap.Type.unknown) {
                iterator.remove();
                catcher.update(parameters.fruitCount, parameters.healthRate, overlap);
            }

            fruit.setY(fruit.getY() - parameters.scrollSpeed * delta);
        }
    }

    private void updateSongPosition(float delta) {
        songPos += delta;
        if (!audio.isPlaying() && songPos >= 0) {
            audio.setPosition(songPos);
            audio.play();
        }
    }

    private void nextFruit() {
        element = element.getParent().getChild(numberOfCurrentFruit);
        fruit = new Fruit(element.getAttribute("type"), element.getFloatAttribute("x") * (Globals.WIDTH - Globals.FRUIT_DIAMETER), Globals.HEIGHT + Globals.FRUIT_DIAMETER, Globals.FRUIT_DIAMETER);
        fruitSpawnTime = element.getFloatAttribute("spawn_time");
        numberOfCurrentFruit++;
    }

    public void start() {
        songPos = element.getFloatAttribute("spawn_time");
        nextFruit();
    }

    public void pause() {
        audio.pause();
    }

    public void stop() {
        audio.stop();
    }

    public void resume() {
        audio.play();
    }

    private void setMusicVolume(float volume) {
        audio.setVolume(volume);
    }

    public Long getId() {
        return id;
    }

    public int getFruitCount() {
        return parameters.fruitCount;
    }

    public float getDifficulty() {
        return parameters.difficulty;
    }

    public float getBPM() {
        return parameters.bpm;
    }

    public float getHealthRate() {
        return parameters.healthRate;
    }

    public Background getBackground() {
        return background;
    }

    public String getTitle() {
        return metaData.title;
    }

    public String getArtist() {
        return metaData.artist;
    }

    @Override
    public String toString() {
        return "";
    }

    @Override
    public void dispose() {
        audio.dispose();
        background.dispose();
    }

    public class Background implements Disposable {
        private Texture texture;
        private float alpha;

        private Background(long id, String name, float alpha) {
            if (name != null) {
                texture = new Texture(Gdx.files.local(Globals.LOCAL_PATH_TO_SONGS_DIRECTORY + id + "/" + name));
            }
            setAlpha(alpha);
        }

        private Background(long id, String name) {
            if (name != null) {
                texture = new Texture(Gdx.files.local(Globals.LOCAL_PATH_TO_SONGS_DIRECTORY + id + "/" + name));
            }
            setAlpha(1);
        }

        private void draw(SpriteBatch batch) {
            if (texture != null) {
                Color c = batch.getColor();
                float r = c.r, g = c.g, b = c.b, a = c.a;

                batch.setColor(r, g, b, alpha);
                batch.draw(texture, 0, 0, Globals.WIDTH, Globals.HEIGHT);
                batch.setColor(r, g, b, a);
            }
        }

        public Texture getTexture() {
            return texture;
        }

        private void setAlpha(float alpha) {
            this.alpha = alpha;
        }

        @Override
        public void dispose() {
            texture.dispose();
        }
    }

    private class MetaData {
        private String title, artist, creator, tags;

        private MetaData(String title, String artist, String creator, String tags) {
            this.title = title;
            this.artist = artist;
            this.creator = creator;
            this.tags = tags;
        }
    }

    private class Parameters {
        private float scrollSpeed, difficulty, healthRate, bpm;
        private int fruitCount;

        private Parameters(float scrollSpeed, float difficulty, float healthRate, float bpm, int fruitCount) {
            this.scrollSpeed = scrollSpeed;
            this.difficulty = difficulty;
            this.healthRate = healthRate;
            this.bpm = bpm;
            this.fruitCount = fruitCount;
        }
    }
}
