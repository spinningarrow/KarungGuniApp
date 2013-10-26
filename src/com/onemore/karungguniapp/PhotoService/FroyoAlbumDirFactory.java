package com.onemore.karungguniapp.PhotoService;

/**
 * Created with IntelliJ IDEA.
 * User: gemengqin
 * Date: 10/27/13
 * Time: 3:23 AM
 * To change this template use File | Settings | File Templates.
 */
import android.os.Environment;

import java.io.File;

public final class FroyoAlbumDirFactory extends AlbumStorageDirFactory {

    @Override
    public File getAlbumStorageDir(String albumName) {
        // TODO Auto-generated method stub
        return new File(
                Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES
                ),
                albumName
        );
    }
}
