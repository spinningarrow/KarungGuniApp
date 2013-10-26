package com.onemore.karungguniapp.PhotoService;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: gemengqin
 * Date: 10/27/13
 * Time: 2:44 AM
 * To change this template use File | Settings | File Templates.
 */

 public   abstract class AlbumStorageDirFactory {
        public abstract File getAlbumStorageDir(String albumName);
    }

