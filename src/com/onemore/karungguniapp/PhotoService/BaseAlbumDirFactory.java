package com.onemore.karungguniapp.PhotoService;

/**
 * Created with IntelliJ IDEA.
 * User: gemengqin
 * Date: 10/27/13
 * Time: 3:25 AM
 * To change this template use File | Settings | File Templates.
 */
import android.os.Environment;

import java.io.File;

public final class BaseAlbumDirFactory extends AlbumStorageDirFactory {

    // Standard storage location for digital camera files
    private static final String CAMERA_DIR = "/dcim/";

    @Override
    public File getAlbumStorageDir(String albumName) {
        return new File (
                Environment.getExternalStorageDirectory()
                        + CAMERA_DIR
                        + albumName
        );
    }
}

