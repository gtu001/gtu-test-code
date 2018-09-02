package com.example.englishtester.common.epub.base;

import java.io.Reader;

/**
 * Created by wistronits on 2018/8/3.
 */

public interface Parser {

    public void parse(Reader contentReader, ParserCallback parserCallback, boolean b);
}


