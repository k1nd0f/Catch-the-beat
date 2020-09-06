package com.kindof.catchthebeat.resources;

public enum FileType {
    userIcon(1),
    friendIcon(1),
    beatmap(4),
    beatmapEditorMusic(1),
    beatmapEditorBackground(1),
    oszFile(1),
    unknown(0);

    private int fileCount;

    FileType(int fileCount) {
        this.fileCount = fileCount;
    }

    public int fileCount() {
        return fileCount;
    }
}
