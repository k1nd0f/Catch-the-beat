package com.kindof.catchthebeat.storage;

import com.kindof.catchthebeat.users.User;

import java.io.File;

public interface IStorage {
    void putFile(String path, File file);

    void putFile(Object uri);

    void getFile(String path, File file);

    void getFile(String path, File file, User friend, float itemWidth, float itemHeight, float itemPadLeftRight, float itemPadTopBottom);

    void resetGetFileCounter();

    void resetPutFileCounter();
}
