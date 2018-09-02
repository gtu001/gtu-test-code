/*
 * Copyright (c) 2010-2020 IISI.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of IISI.
 */
package gtu.itext.iisi;

import java.awt.Color;
import java.util.List;

import com.lowagie.text.Chunk;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.FontSelector;

/**
 * 
 * @author tsaicf
 */
public class RisFontSelector extends FontSelector {
    @SuppressWarnings("unchecked")
    public Phrase process(String text, Color bgColor) {
        Phrase phrase = super.process(text);
        for (Chunk chunk : (List<Chunk>) phrase.getChunks()) {
            chunk.setBackground(bgColor);
        }
        return phrase;
    }

    @SuppressWarnings("unchecked")
    public Paragraph process(String text, SubPhrase... subPhrases) throws DocumentException {
        final Phrase phrase = super.process(text);
        final Paragraph paragraph = new Paragraph(phrase);
        if (subPhrases != null) {
            for (SubPhrase subPhrase : subPhrases) {
                RisFontSelector selector = subPhrase.fontInfo.getFontSelector();
                if (subPhrase.fontInfo.backgroundColor != null) {
                    Phrase sub = selector.process(subPhrase.text, subPhrase.fontInfo.backgroundColor);
                    paragraph.add(sub);
                } else {
                    Phrase sub = selector.process(subPhrase.text);
                    paragraph.add(sub);
                }
            }
        }
        return paragraph;
    }

    public static Paragraph process(SubPhrase... subPhrases) throws DocumentException {
        final Paragraph paragraph = new Paragraph();
        if (subPhrases != null) {
            for (SubPhrase subPhrase : subPhrases) {
                RisFontSelector selector = subPhrase.fontInfo.getFontSelector();
                if (subPhrase.fontInfo.backgroundColor != null) {
                    Phrase sub = selector.process(subPhrase.text, subPhrase.fontInfo.backgroundColor);
                    paragraph.add(sub);
                } else {
                    Phrase sub = selector.process(subPhrase.text);
                    paragraph.add(sub);
                }
            }
        }
        return paragraph;
    }

}
