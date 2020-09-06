package com.kindof.catchthebeat.screens.beatmapselectionmenu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
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
import com.kindof.catchthebeat.resources.Globals;
import com.kindof.catchthebeat.screens.BaseScreen;
import com.kindof.catchthebeat.screens.beatmapselectionmenu.actors.beatmapscrollpane.BeatmapList;
import com.kindof.catchthebeat.screens.beatmapselectionmenu.actors.beatmapscrollpane.BeatmapScrollPaneItem;
import com.kindof.catchthebeat.ui.Alignment;
import com.kindof.catchthebeat.ui.UI;
import com.kindof.catchthebeat.ui.animation.AlphaAnimation;
import com.kindof.catchthebeat.ui.actors.button.Button;
import com.kindof.catchthebeat.ui.actors.scrollpane.ScrollPane;
import com.kindof.catchthebeat.screens.beatmapselectionmenu.actors.sidebar.SideBar;
import com.kindof.catchthebeat.screens.game.GameScreen;
import com.kindof.catchthebeat.tools.Time;
import com.kindof.catchthebeat.ui.styles.LabelStyle;

import static com.kindof.catchthebeat.screens.beatmapselectionmenu.actors.sidebar.currentuser.downloadbeatmap.BeatmapScrollPaneItem.initSongsMap;

import java.util.Iterator;

public class BeatmapSelectionMenuScreen extends BaseScreen {
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
                    Globals.GAME.setScreenWithTransition(Globals.MAIN_MENU_SCREEN);
                }

                return super.keyDown(keyCode);
            }
        };

        float statisticsButtonSize = Globals.HEIGHT / 6.0f;
        statisticsButton = new Button((event, x, y, pointer, button) -> Globals.GAME.setScreenWithTransition(Globals.STATISTIC_SCREEN), UI.STATISTICS_BUTTON + UI.UP_POSTFIX, UI.STATISTICS_BUTTON);
        UI.calculateAndSetViewElementBounds(
                statisticsButton,
                Alignment.topLeft, 0,
                0, Globals.HEIGHT - statisticsButtonSize, statisticsButtonSize, statisticsButtonSize
        );

        rootTable = new Table();
        rootTable.setTouchable(Touchable.childrenOnly);
        rootTable.setFillParent(true);
        rootTable.setVisible(false);
        stage.addActor(rootTable);

        scrollPaneItems = new BeatmapList();
        FileHandle[] fileHandles = Gdx.files.local(Globals.LOCAL_PATH_TO_SONGS_DIRECTORY).list();
        beatmaps = new Beatmap[fileHandles.length];
        for (int i = 0; i < beatmaps.length; i++) {
            beatmaps[i] = new Beatmap(Long.parseLong(fileHandles[i].name()));
        }
        initSongsMap(fileHandles);

        sideBar = new SideBar(Globals.WIDTH, 0, Globals.WIDTH / 2f, Globals.HEIGHT, 40 * Globals.RESOLUTION_WIDTH_SCALE);
        sideBar.setTouchable(Touchable.enabled);
        stage.addActor(sideBar);

        beatmapScrollPaneTable = new Table();
        beatmapScrollPaneTable.setTouchable(Touchable.enabled);
        beatmapScrollPaneTable.align(Align.center);

        scrollPane = new ScrollPane<>(beatmapScrollPaneTable);
        scrollPane.setMaxItemsOnScreen(BeatmapList.MAX_ITEMS_ON_SCREEN);

        rootTable.add(scrollPane).width(Globals.WIDTH);
        rootTable.addActor(statisticsButton);

        initBeatmapScrollPane(beatmaps);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void hide() {
        super.hide();
        statisticsButton.finish();
    }

    private void initBeatmapScrollPane(Beatmap[] beatmaps) {
        padLeftRight = 5 * Globals.RESOLUTION_WIDTH_SCALE;
        tablePadTopBottom = 15 * Globals.RESOLUTION_HEIGHT_SCALE;
        tablePadLeftRight = 50 * Globals.RESOLUTION_WIDTH_SCALE;
        scrollPaneItemWidth = Globals.WIDTH - 2 * (tablePadLeftRight - sideBar.getPad() / 2.0f);
        scrollPaneItemHeight = (Globals.HEIGHT - (BeatmapList.MAX_ITEMS_ON_SCREEN - 1) * tablePadTopBottom * 2) / (BeatmapList.MAX_ITEMS_ON_SCREEN - 1);
        scrollPane.setVisibleItemHeight(scrollPaneItemHeight + 2 * tablePadTopBottom);

        index = 0;
        for (Beatmap beatmap : beatmaps) {
            addScrollPaneItem(beatmap);
        }

        scrollPane.setItems(scrollPaneItems);
    }

    public void addScrollPaneItem(Beatmap beatmap) {
        TextureRegion beatmapBackgroundTexture = new TextureRegion(beatmap.getBackground().getTexture());
        /*
            kY = coefficient-y
            rY = real-size-y
            fY = final-y
            fHeight = final-height
            bg = background
        */
        float
                bgTextureHeight = beatmapBackgroundTexture.getRegionHeight(),
                bgTextureWidth = beatmapBackgroundTexture.getRegionWidth(),
                kY = Globals.HEIGHT / bgTextureHeight,
                rY = (Globals.HEIGHT - Math.min(Globals.HEIGHT, bgTextureHeight)) / 2.0f,
                fY = rY / kY,
                fHeight = bgTextureHeight - fY * 2.0f;

        TextureRegion beatmapBackgroundRegion;
        if (bgTextureHeight >= Globals.HEIGHT) {
            beatmapBackgroundRegion = new TextureRegion(beatmapBackgroundTexture, 0, (int) ((bgTextureHeight - scrollPaneItemHeight) / 2.0), (int) bgTextureWidth, (int) scrollPaneItemHeight);
        } else {
            beatmapBackgroundRegion = new TextureRegion(beatmapBackgroundTexture, 0, (int) fY, (int) bgTextureWidth, (int) fHeight);
        }

        final Image beatmapBackgroundImage = new Image(beatmapBackgroundRegion);
        beatmapBackgroundImage.setBounds(0, 0, scrollPaneItemWidth, scrollPaneItemHeight);
        beatmapBackgroundImage.setTouchable(Touchable.disabled);

        LabelStyle labelStyleTitle = new LabelStyle(Color.WHITE, 25);
        LabelStyle labelStyleArtist = new LabelStyle(Color.WHITE, 15);

        final Label labelTitle = new Label(beatmap.getTitle(), labelStyleTitle);
        labelTitle.setBounds(padLeftRight, 0, scrollPaneItemWidth - padLeftRight, scrollPaneItemHeight);
        labelTitle.setAlignment(Align.topLeft);
        labelTitle.setTouchable(Touchable.disabled);

        final Label labelArtist = new Label(beatmap.getArtist(), labelStyleArtist);
        labelArtist.setBounds(padLeftRight, 0, scrollPaneItemWidth - 2 * padLeftRight, scrollPaneItemHeight);
        labelArtist.setAlignment(Align.left);
        labelArtist.setTouchable(Touchable.disabled);

        final Image backgroundImage = new Image(UI.SKIN_ATLAS.findRegion(UI.BEATMAP_BUTTON_FG));
        backgroundImage.setBounds(0, 0, scrollPaneItemWidth, scrollPaneItemHeight);
        backgroundImage.setTouchable(Touchable.disabled);

        final BeatmapScrollPaneItem beatmapScrollPaneItem = new BeatmapScrollPaneItem(beatmap, beatmapBackgroundImage, backgroundImage, labelTitle, labelArtist);
        if (index > BeatmapList.MAX_ITEMS_ON_SCREEN) {
            beatmapScrollPaneItem.setVisible(false);
        }
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
                        Globals.GAME_SCREEN = new GameScreen(new Beatmap(beatmapScrollPaneItem.getBeatmap().getId()));
                        Globals.GAME.setScreen(Globals.GAME_SCREEN);
                        Globals.GAME_SCREEN.start();
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
        super.dispose();
    }
}
