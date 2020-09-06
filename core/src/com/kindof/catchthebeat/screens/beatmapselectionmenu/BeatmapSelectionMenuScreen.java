package com.kindof.catchthebeat.screens.beatmapselectionmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.AlphaAction;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;
import com.kindof.catchthebeat.beatmaps.Beatmap;
import com.kindof.catchthebeat.resources.Res;
import com.kindof.catchthebeat.screens.BaseScreen;
import com.kindof.catchthebeat.screens.MainMenuScreen;
import com.kindof.catchthebeat.ui.animation.AlphaAnimation;
import com.kindof.catchthebeat.screens.beatmapselectionmenu.actors.beatmapscrollpanes.BeatmapList;
import com.kindof.catchthebeat.screens.beatmapselectionmenu.actors.beatmapscrollpanes.BeatmapScrollPaneItem;
import com.kindof.catchthebeat.ui.actors.buttons.Button;
import com.kindof.catchthebeat.ui.actors.buttons.TouchUpEventListener;
import com.kindof.catchthebeat.ui.actors.scrollpanes.ScrollPane;
import com.kindof.catchthebeat.screens.beatmapselectionmenu.actors.sidebars.currentuser.downloadbeatmap.ScrollPaneItem;
import com.kindof.catchthebeat.screens.beatmapselectionmenu.actors.sidebars.SideBar;
import com.kindof.catchthebeat.tools.Font;
import com.kindof.catchthebeat.screens.game.GameScreen;
import com.kindof.catchthebeat.tools.Time;

import java.util.Iterator;

public class BeatmapSelectionMenuScreen extends BaseScreen {
    private BitmapFont titleFont;
    private BitmapFont artistFont;
    private BeatmapList scrollPaneItems;
    private Beatmap[] beatmaps;
    private ScrollPane<BeatmapScrollPaneItem> scrollPane;
    private Table beatmapScrollPaneTable;
    private SideBar sideBar;
    private Button statisticsButton;
    private float padLeftRight, tablePadTopBottom, tablePadLeftRight, scrollPaneItemWidth, scrollPaneItemHeight;
    private int index;

    public BeatmapSelectionMenuScreen() {
        super();
    }

    @Override
    public void initialize() {
        stage = new Stage() {
            @Override
            public boolean keyDown(int keyCode) {
                if (keyCode == Input.Keys.BACK) {
                    Res.GAME.setScreen(Res.MAIN_MENU_SCREEN);
                }

                return super.keyDown(keyCode);
            }
        };

        float statisticsButtonSize = Res.HEIGHT / 6.0f;
        statisticsButton = new Button(new TouchUpEventListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                Res.GAME.setScreen(Res.STATISTIC_SCREEN);
            }
        },0, Res.HEIGHT - statisticsButtonSize, statisticsButtonSize, statisticsButtonSize, Res.UI_STATISTICS_BUTTON_UP, Res.UI_STATISTICS_BUTTON_PRESS);

        rootTable = new Table();
        rootTable.setTouchable(Touchable.childrenOnly);
        rootTable.setFillParent(true);
        rootTable.setVisible(false);
        stage.addActor(rootTable);

        scrollPaneItems = new BeatmapList();
        FileHandle[] fileHandles = Gdx.files.local(Res.LOCAL_PATH_TO_SONGS_DIRECTORY).list();
        beatmaps = new Beatmap[fileHandles.length];
        for (int i = 0; i < beatmaps.length; i++) {
            beatmaps[i] = new Beatmap(Long.parseLong(fileHandles[i].name()));
        }
        ScrollPaneItem.initSongsMap(fileHandles);

        sideBar = new SideBar(Res.WIDTH, 0, Res.WIDTH / 2f, Res.HEIGHT, 40 * Res.RESOLUTION_WIDTH_SCALE);
        sideBar.setTouchable(Touchable.enabled);
        stage.addActor(sideBar);

        beatmapScrollPaneTable = new Table();
        beatmapScrollPaneTable.setTouchable(Touchable.enabled);
        beatmapScrollPaneTable.align(Align.center);

        scrollPane = new ScrollPane<>(beatmapScrollPaneTable);
        scrollPane.setMAX_ITEMS_ON_SCREEN(BeatmapList.MAX_ITEMS_ON_SCREEN);

        rootTable.add(scrollPane).width(Res.WIDTH);
        rootTable.addActor(statisticsButton);
        rootTable.pack();

        initBeatmapScrollPane(beatmaps);
    }

    @Override
    public void show() {
        super.show();
        statisticsButton.setUpFrame();
    }

    private void initBeatmapScrollPane(Beatmap[] beatmaps) {
        padLeftRight = 5 * Res.RESOLUTION_WIDTH_SCALE;
        tablePadTopBottom = 15 * Res.RESOLUTION_HEIGHT_SCALE;
        tablePadLeftRight = 50 * Res.RESOLUTION_WIDTH_SCALE;
        scrollPaneItemWidth = Res.WIDTH - 2 * (tablePadLeftRight - sideBar.getPad() / 2.0f);
        scrollPaneItemHeight = (Res.HEIGHT - (BeatmapList.MAX_ITEMS_ON_SCREEN - 1) * tablePadTopBottom * 2) / (BeatmapList.MAX_ITEMS_ON_SCREEN - 1);
        scrollPane.setVisibleItemHeight(scrollPaneItemHeight + 2 * tablePadTopBottom);
        titleFont = Font.getBitmapFont(Res.FONT_NAME, (int) (25 * Res.RESOLUTION_HEIGHT_SCALE));
        artistFont = Font.getBitmapFont(Res.FONT_NAME, (int) (15 * Res.RESOLUTION_HEIGHT_SCALE));

        index = 0;
        for (Beatmap beatmap : beatmaps) {
            addScrollPaneItem(beatmap);
        }

        scrollPane.setItems(scrollPaneItems);
    }

    public void addScrollPaneItem(Beatmap beatmap) {
        TextureRegion beatmapBackgroundTexture = new TextureRegion(beatmap.getBackground().getTexture());
        // kY = coefficient-y | rY = real-size-y | fY = final-y | fHeight = final-height | bg = background
        float
                bgTextureHeight = beatmapBackgroundTexture.getRegionHeight(),
                bgTextureWidth = beatmapBackgroundTexture.getRegionWidth(),
                kY = Res.HEIGHT / bgTextureHeight,
                rY = (Res.HEIGHT - Math.min(Res.HEIGHT, bgTextureHeight)) / 2.0f,
                fY = rY / kY,
                fHeight = bgTextureHeight - fY * 2.0f;

        TextureRegion beatmapBackgroundRegion;
        if (bgTextureHeight >= Res.HEIGHT) {
            beatmapBackgroundRegion = new TextureRegion(beatmapBackgroundTexture, 0, (int) ((bgTextureHeight - scrollPaneItemHeight) / 2.0), (int) bgTextureWidth, (int) scrollPaneItemHeight);
        } else {
            beatmapBackgroundRegion = new TextureRegion(beatmapBackgroundTexture, 0, (int) fY, (int) bgTextureWidth, (int) fHeight);
        }

        final Image beatmapBackgroundImage = new Image(beatmapBackgroundRegion);
        beatmapBackgroundImage.setBounds(0, 0, scrollPaneItemWidth, scrollPaneItemHeight);
        beatmapBackgroundImage.setTouchable(Touchable.disabled);

        Label.LabelStyle labelStyleTitle = new Label.LabelStyle();
        labelStyleTitle.font = titleFont;

        final Label.LabelStyle labelStyleArtist = new Label.LabelStyle();
        labelStyleArtist.font = artistFont;

        final Label labelTitle = new Label(beatmap.getTitle(), labelStyleTitle);
        labelTitle.setBounds(padLeftRight, 0, scrollPaneItemWidth - padLeftRight, scrollPaneItemHeight);
        labelTitle.setAlignment(Align.topLeft);
        labelTitle.setTouchable(Touchable.disabled);

        final Label labelArtist = new Label(beatmap.getArtist(), labelStyleArtist);
        labelArtist.setBounds(padLeftRight, 0, scrollPaneItemWidth - 2 * padLeftRight, scrollPaneItemHeight);
        labelArtist.setAlignment(Align.left);
        labelArtist.setTouchable(Touchable.disabled);

        final Image backgroundImage = new Image(Res.SKIN_ATLAS.findRegion(Res.UI_BEATMAP_SELECTION_ITEM_BACKGROUND));
        backgroundImage.setBounds(0, 0, scrollPaneItemWidth, scrollPaneItemHeight);
        backgroundImage.setTouchable(Touchable.disabled);

        final BeatmapScrollPaneItem beatmapScrollPaneItem = new BeatmapScrollPaneItem(beatmap, beatmapBackgroundImage, backgroundImage, labelTitle, labelArtist);
        if (index > BeatmapList.MAX_ITEMS_ON_SCREEN)
            beatmapScrollPaneItem.setVisible(false);
        beatmapScrollPaneItem.addActor(beatmapBackgroundImage);
        beatmapScrollPaneItem.addActor(backgroundImage);
        beatmapScrollPaneItem.addActor(labelTitle);
        beatmapScrollPaneItem.addActor(labelArtist);

        final AlphaAnimation alphaAnimationLabelTitle = new AlphaAnimation(0.55f, 1, 0);
        final AlphaAnimation alphaAnimationLabelArtist = new AlphaAnimation(0.55f, 1, 0);
        final AlphaAnimation alphaAnimationBackgroundImage = new AlphaAnimation(0.55f, 0.95f, 0);

        beatmapScrollPaneItem.setTouchable(Touchable.enabled);
        beatmapScrollPaneItem.addListener(new InputListener() {
            private float animationStartTime = 0;

            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                scrollPane.setHasScrolled(false);
                return true;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                if (!scrollPane.hasScrolled()) {
                    boolean hasSelectedAgain = scrollPaneItems.markItemAsSelected(beatmapScrollPaneItem);
                    if (hasSelectedAgain) {
                        scrollPaneItems.resetCurrentPreviousSelectedItem();
                        Res.GAME_SCREEN = new GameScreen(new Beatmap(beatmapScrollPaneItem.getBeatmap().getId()));
                        Res.GAME.setScreen(Res.GAME_SCREEN);
                        Res.GAME_SCREEN.start();
                    } else if (TimeUtils.nanoTime() - animationStartTime >= 0) {
                        AlphaAction labelTitleAlphaAction = alphaAnimationLabelTitle.nextAlphaAction();
                        AlphaAction labelArtistAlphaAction = alphaAnimationLabelArtist.nextAlphaAction();
                        AlphaAction backgroundImageAlphaAction = alphaAnimationBackgroundImage.nextAlphaAction();

                        DelayAction labelTitleDelayAlphaAction = new DelayAction();
                        DelayAction labelArtistDelayAlphaAction = new DelayAction();
                        DelayAction backgroundImageDelayAlphaAction = new DelayAction();

                        labelTitleDelayAlphaAction.setAction(alphaAnimationLabelTitle.nextAlphaAction());
                        labelTitleDelayAlphaAction.setDuration(alphaAnimationLabelTitle.getDuration());
                        labelArtistDelayAlphaAction.setAction(alphaAnimationLabelArtist.nextAlphaAction());
                        labelArtistDelayAlphaAction.setDuration(alphaAnimationLabelTitle.getDuration());
                        backgroundImageDelayAlphaAction.setAction(alphaAnimationBackgroundImage.nextAlphaAction());
                        backgroundImageDelayAlphaAction.setDuration(alphaAnimationLabelTitle.getDuration());

                        labelTitle.addAction(labelTitleAlphaAction);
                        labelTitle.addAction(labelTitleDelayAlphaAction);
                        labelArtist.addAction(labelArtistAlphaAction);
                        labelArtist.addAction(labelArtistDelayAlphaAction);
                        backgroundImage.addAction(backgroundImageAlphaAction);
                        backgroundImage.addAction(backgroundImageDelayAlphaAction);

                        animationStartTime = TimeUtils.nanoTime() + Time.secondsToNanos(alphaAnimationLabelTitle.getDuration() * 2);
                    }
                }
            }
        });

        beatmapScrollPaneTable.add(beatmapScrollPaneItem).size(scrollPaneItemWidth, scrollPaneItemHeight).pad(tablePadTopBottom, tablePadLeftRight - sideBar.getPad() / 2.0f, tablePadTopBottom, tablePadLeftRight + sideBar.getPad() / 2.0f).row();
        scrollPaneItems.add(beatmapScrollPaneItem);
        index++;
    }

    public void removeBeatmapFromScrollPane(Long id) {
        Iterator<BeatmapScrollPaneItem> iterator = scrollPaneItems.iterator();
        while (iterator.hasNext()) {
            BeatmapScrollPaneItem beatmapScrollPaneItem = iterator.next();
            Long beatmapId = beatmapScrollPaneItem.getBeatmap().getId();
            if (beatmapId.equals(id)) {
                iterator.remove();
                index--;
                beatmapScrollPaneTable.getCells().removeValue(beatmapScrollPaneTable.getCell(beatmapScrollPaneItem), false);
                beatmapScrollPaneItem.clear();
                beatmapScrollPaneItem.remove();
                beatmapScrollPaneItem.getBeatmap().dispose();
                break;
            }
        }
    }

    public SideBar getSideBar() {
        return sideBar;
    }

    @Override
    public void dispose() {
        BeatmapScrollPaneItem selectedItem = scrollPaneItems.getSelectedItem();
        int selectedItemIndex = scrollPaneItems.indexOf(selectedItem);

        for (int i = 0; i < beatmaps.length; i++) {
            if (selectedItem == null || selectedItemIndex != i)
                beatmaps[i].dispose();
        }

        titleFont.dispose();
        artistFont.dispose();
        super.dispose();
    }
}
