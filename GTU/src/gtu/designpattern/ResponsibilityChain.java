/*
 * Copyright (c) 2010-2020 IISI. All rights reserved.
 * 
 * This software is the confidential and proprietary information of IISI.
 */
package gtu.designpattern;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ResponsibilityChain {

    public static void main(String[] args) {
        //隨機挑選幾個女性
        Random rand = new Random();
        List<IWomen> list = new ArrayList<IWomen>();
        for (int ii = 0; ii < 5; ii++) {
            list.add(new Women(rand.nextInt(4), "我要出去逛街"));
        }
        //定義三個請示物件
        Handler father = new Father();
        Handler husband = new Husband();
        Handler son = new Son();
        //設置請示順序
        father.setNext(husband);
        husband.setNext(son);
        for (IWomen women : list) {
            father.handleMessage(women);
        }

        //讓多個物件都有機會處理請求，從而避免請求的發送者和請求的接受者之間的耦合關係。將這些物件串成一條鍊，並沿著這條鍊傳遞請求，直到有物件處理它為止
        //Avoid coupling the sender of a request to its receiver by giving more than one object a chance to handle the request. Chain the receiving objects and pass the request along the chain until an object handles it.
    }

    interface IWomen {
        //獲得個人狀況
        public int getType();

        //獲得個人請示，你要幹什麼?出去逛街?約會?看電影?
        public String getRequest();
    }

    static class Women implements IWomen {

        /** 1=未出嫁,2=出嫁,3=寡婦 */
        private int type = 0;

        /** 婦女的請示 */
        private String request = "";

        public Women(int type, String request) {
            this.type = type;
            this.request = request;
            switch (this.type) {
            case 1:
                this.request = "女兒的請求是:" + request;
                break;
            case 2:
                this.request = "妻子的請求是:" + request;
                break;
            case 3:
                this.request = "母親的請求是:" + request;
            }
        }

        @Override
        public int getType() {
            return type;
        }

        @Override
        public String getRequest() {
            return request;
        }

    }

    static abstract class Handler {
        public final static int FATHER_LEVEL_REQUEST = 1;
        public final static int HUSBAND_LEVEL_REQUEST = 2;
        public final static int SON_LEVEL_REQUEST = 3;
        //能處理的級別
        private int level = 0;
        //責任傳遞下一個負責人是誰
        private Handler nextHandler;

        //每個類別都要說明一下自己能處理的那些請求
        public Handler(int _level) {
            this.level = _level;
        }

        public final void handleMessage(IWomen women) {
            if (women.getType() == this.level) {
                this.response(women);
            } else {
                if (this.nextHandler != null) {
                    this.nextHandler.handleMessage(women);
                } else {
                    System.out.println("--沒有地方請示了,按不同意處理--:" + women.getType());
                }
            }
        }

        public void setNext(Handler _handler) {
            this.nextHandler = _handler;
        }

        abstract void response(IWomen women);
    }

    static class Father extends Handler {
        //父親只處理女兒的請求
        public Father() {
            super(FATHER_LEVEL_REQUEST);
        }

        @Override
        void response(IWomen women) {
            System.out.println("--------女兒向父親請示-------");
            System.out.println(women.getRequest());
            System.out.println("父親的答覆是:同意\n");
        }
    }

    static class Husband extends Handler {
        public Husband() {
            super(HUSBAND_LEVEL_REQUEST);
        }

        @Override
        void response(IWomen women) {
            System.out.println("--------女兒向丈夫請示-------");
            System.out.println(women.getRequest());
            System.out.println("丈夫的答覆是:同意\n");
        }
    }

    static class Son extends Handler {
        public Son() {
            super(SON_LEVEL_REQUEST);
        }

        @Override
        void response(IWomen women) {
            System.out.println("--------母親向兒子請示-------");
            System.out.println(women.getRequest());
            System.out.println("兒子的答覆是:同意\n");
        }
    }
}
