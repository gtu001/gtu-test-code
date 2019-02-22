package com.example.englishtester;

import android.content.Context;

import com.example.englishtester.common.DropboxUtilV2;

import java.util.List;

public class DropboxHtmlService {

    private static final String TAG = DropboxHtmlService.class.getSimpleName();

    final DropboxHtmlDAO dao;
    Context context;

    public DropboxHtmlService(Context context) {
        this.context = context;
        this.dao = new DropboxHtmlDAO(context);
    }

    public void deleteAll() {
        dao.deleteAll();
    }

    public void addOneByDropboxVO(DropboxUtilV2.DropboxUtilV2_DropboxFile f) {
        DropboxHtmlDAO.DropboxHtml vo = new DropboxHtmlDAO.DropboxHtml();
        vo.fileName = f.getName();
        vo.fileSize = f.getSize();
        vo.uploadDate = f.getClientModify();
        vo.fullPath = f.getFullPath();
        dao.insert(vo);
    }

    public List<DropboxHtmlDAO.DropboxHtml> findAll() {
        return dao.queryAll();
    }

    public boolean deleteByFileName(String fileName) {
        return 0 != dao.deleteByPk(fileName);
    }

    public boolean deleteByFullPath(String fullPath) {
        return 0 != dao.deleteByFullPath(fullPath);
    }
}
