package com.example.gtu001.qrcodemaker.services;

import android.content.Context;

import com.example.gtu001.qrcodemaker.dao.AppInfoDAO;
import com.example.gtu001.qrcodemaker.dao.AppInfoDAO.AppInfo;

import java.util.List;

public class AppInfoService {
    private AppInfoDAO dao;

    public AppInfoService(Context context) {
        this.dao = new AppInfoDAO(context);
    }

    public List<AppInfoDAO.AppInfo> queryAll() {
        return this.dao.queryAll();
    }

    public boolean insertData(AppInfoDAO.AppInfo vo) {
        return this.dao.insert(vo) > 0;
    }

    public AppInfoDAO.AppInfo findByPk(String videoId) {
        return this.dao.findByPk(videoId);
    }

    public boolean deleteId(String installedPackage) {
        return this.dao.deleteByPk(installedPackage) > 0;
    }

    public boolean update(AppInfoDAO.AppInfo vo) {
        return this.dao.updateByVO(vo) > 0;
    }

    public int countAll() {
        return this.dao.countAll();
    }
}
