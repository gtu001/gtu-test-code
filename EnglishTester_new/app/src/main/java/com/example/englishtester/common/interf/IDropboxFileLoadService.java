package com.example.englishtester.common.interf;

import com.dropbox.core.DbxException;
import com.example.englishtester.common.DropboxUtilV2;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by wistronits on 2018/8/24.
 */

public interface IDropboxFileLoadService {
    File downloadFile(final String path);

    File downloadFile(final String path, final String fileExtension);

    File downloadHtmlReferencePicDir(final String dropboxDirName, final long timeout);

    Map<String, String> listFile();

    List<DropboxUtilV2.DropboxUtilV2_DropboxFile> listFileV2();

    void uploadFile(final File file, final String fileName) throws DbxException, IOException;
}
