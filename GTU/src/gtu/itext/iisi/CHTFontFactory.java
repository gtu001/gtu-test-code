package gtu.itext.iisi;

import gtu.collection.AeMapUtils;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import tw.gov.moi.common.SystemConfig;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.FontSelector;

/**
 * 統一處理中文字型使用的 FontFactory.
 * 
 * 因為以檔名為路徑的 BaseFont 在此版 itext 對大小寫的 CACHE 有問題.
 */
public class CHTFontFactory {

    public enum RISFont {
        SUNG(1, "戶役政宋體") {
            @Override
            protected void init(File folder) {
                CHTFontFactory.Builder builder = new Builder();
                builder.addPlantFont(0x00, new File(folder, "RIS-Sung-Plane0.ttf"));
                builder.addPlantFont(0x02, new File(folder, "RIS-Sung-Plane2.ttf"));
                builder.addPlantFont(0x0f, new File(folder, "RIS-Sung-PlaneF.ttf"));
                this.factory = builder.build();
            }
        }, //
        KAI(2, "戶役政楷體") {
            @Override
            protected void init(File folder) {
                CHTFontFactory.Builder builder = new Builder();
                builder.addPlantFont(0x00, new File(folder, "TW-Kai-98_1.ttf"));
                builder.addPlantFont(0x02, new File(folder, "TW-Kai-Ext-B-98_1.ttf"));
                builder.addPlantFont(0x0f, new File(folder, "TW-Kai-Plus-98_1.ttf"));
                this.factory = builder.build();
            }
        }, //
        ;

        final private int code;

        final private String name;

        protected CHTFontFactory factory;

        private RISFont(int code, String name) {
            this.code = code;
            this.name = name;
        }

        /**
         * @return the factory
         */
        public synchronized CHTFontFactory getFactory(SystemConfig systemConfig) {
            if (this.factory == null) {
                String dir = systemConfig.getGlobleSharePath() + File.separator + "font";
                LOGGER.debug("system font path : {}", dir);
                File folder = new File(dir);
                init(folder);
            }
            return this.factory;
        }

        /**
         * @return the factory
         */
        public synchronized void reset() {
            if (this.factory != null) {
            }
            this.factory = null;
        }

        /**
         * @return the name
         */
        public String getName() {
            return this.name;
        }

        /**
         * @return the code
         */
        public int getCode() {
            return this.code;
        }

        protected abstract void init(File folder);

        /**
         * @param fontid
         * @return
         */
        public static RISFont lookup(int fontid) {
            if (SUNG.code == fontid) {
                return SUNG;
            }
            if (KAI.code == fontid) {
                return KAI;
            }
            return SUNG;
        }
    }

    //================================================
    //== [static variables] Block Start
    //====

    /** Logger Object. */
    private static final org.slf4j.Logger LOGGER = org.slf4j.LoggerFactory.getLogger(CHTFontFactory.class);

    //####################################################################
    //## [static variables] sub-block : 預設字型
    //####################################################################

    /** 標楷體. */
    public static final CHTFontFactory KAIU = new CHTFontFactory(//
            new File("c:\\windows\\fonts\\kaiu.ttf")//
            , new File("c:\\winnt\\fonts\\kaiu.ttf")//
            , new File("d:\\windows\\fonts\\kaiu.ttf")//
            , new File("d:\\winnt\\fonts\\kaiu.ttf")//
        );

    /** 新細明體. */
    public static final CHTFontFactory MINGLIU = new CHTFontFactory(//
            new File("c:\\windows\\fonts\\MINGLIU.TTC")//
            , new File("c:\\winnt\\fonts\\MINGLIU.TTC")//
            , new File("d:\\windows\\fonts\\MINGLIU.TTC")//
            , new File("d:\\winnt\\fonts\\MINGLIU.TTC")//
        );

    /** 預設字型. */
    public static final CHTFontFactory Default = MINGLIU; //

    //####################################################################
    //## [static variables] sub-block : FontSelector Cache
    //####################################################################

    private static final CacheLoader<FontInfo, Font> FontLoader = new CacheLoader<FontInfo, Font>() {
        @Override
        public Font load(FontInfo key) throws Exception {
            LOGGER.debug("create Font For {}", key);
            BaseFont bf = key.fontFactory.getBaseFont(0);
            return new Font(bf, key.size, key.style, key.color);
        }
    };

    private static LoadingCache<FontInfo, Font> CACHE_F = CacheBuilder.newBuilder() //                                        
            .maximumSize(30) // maximum cache size
            .expireAfterWrite(15, TimeUnit.MINUTES)//
            .build(FontLoader);

    //####################################################################
    //## [static variables] sub-block : Font Cache
    //####################################################################

    private static final CacheLoader<FontInfo, RisFontSelector> FontSelectorLoader = new CacheLoader<FontInfo, RisFontSelector>() {
        @Override
        public RisFontSelector load(FontInfo key) throws Exception {
            LOGGER.debug("create FontSelector For {}", key);
            RisFontSelector selector = new RisFontSelector();
            addFont(key, selector, 0x0);
            addFont(key, selector, 0x2);
            addFont(key, selector, 0xf);
            return selector;
        }

        private void addFont(FontInfo key, FontSelector selector, final int plane) {
            BaseFont bf = key.fontFactory.getBaseFont(plane);
            if (bf != null) {
                selector.addFont(new Font(bf, key.size, key.style, key.color));
            }
        }
    };

    private static LoadingCache<FontInfo, RisFontSelector> CACHE_FS = CacheBuilder.newBuilder() //                                        
            .maximumSize(30) // maximum cache size
            .expireAfterWrite(15, TimeUnit.MINUTES)//
            .build(FontSelectorLoader);

    //====
    //== [static variables] Block Stop 
    //================================================
    //== [instance variables] Block Start
    //====

    /** 基本中文字集. */
    private BaseFont[] baseFont = new BaseFont[16];

    private java.awt.Font[] awtFont = new java.awt.Font[16];

    private File[][] fontFilePath = new File[16][];

    //====
    //== [instance variables] Block Stop 
    //================================================
    //== [static Constructor] Block Start
    //====
    //====
    //== [static Constructor] Block Stop 
    //================================================
    //== [Constructors] Block Start (含init method)
    //====

    public static class Builder {
        private Map<Integer, List<File>> fontFiles = AeMapUtils.newLazyMapToList();

        /**
         * 
         */
        public Builder() {
        }

        public Builder addPlantFont(int plane, File... fontFiles) {
            if (fontFiles != null) {
                final List<File> list = this.fontFiles.get(plane);
                Collections.addAll(list, fontFiles);
            }
            return this;
        }

        public CHTFontFactory build() {
            final CHTFontFactory factory = new CHTFontFactory();
            for (Entry<Integer, List<File>> entry : this.fontFiles.entrySet()) {
                Integer key = entry.getKey();
                List<File> value = entry.getValue();
                factory.loadFont(key, value.toArray(new File[value.size()]));
            }
            return factory;
        }
    }

    protected CHTFontFactory(File... fontFiles) {
        loadFont(0, fontFiles);
    }

    private void loadFont(final int plane, File... fontFiles) {
        boolean flag = false;
        for (File fontFile : fontFiles) {
            if (fontFile.isFile()) {
                try {
                    LOGGER.debug("load font file : {}", fontFile);
                    final URL fontResource = fontFile.toURI().toURL();
                    String extrnalFrom = fontResource.toExternalForm();
                    String fontPath = fontFile.getCanonicalPath();
                    if (fontPath.toLowerCase().endsWith(".ttc")) {
                        extrnalFrom = extrnalFrom + ",0";
                    }
                    LOGGER.debug("load font from : {}", extrnalFrom);
                    this.baseFont[plane] = BaseFont.createFont(extrnalFrom, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, false, null, null, false);
                    this.baseFont[plane].setSubset(true);
                    this.awtFont[plane] = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, new FileInputStream(fontFile));
                    flag = true;
                } catch (Exception e) {
                    LOGGER.error("error", e);
                }
            } else {
                LOGGER.debug("font file not exist : {}", fontFile);
            }
        }

        if (flag) {
            this.fontFilePath[plane] = null;
        } else {
            this.fontFilePath[plane] = fontFiles;
        }
    }

    private void checkFont(final int plane) {
        if (this.fontFilePath[plane] != null) {
            synchronized (this) {
                if (this.fontFilePath[plane] != null) {
                    LOGGER.debug("reload font {}", plane);
                    loadFont(plane, this.fontFilePath[plane]);
                }
            }
        }
    }

    //====
    //== [Constructors] Block Stop 
    //================================================
    //== [Static Method] Block Start
    //====
    //
    //    /** 以字型檔取得 FontFactory */
    //    private static Map<String, CHTFontFactory> FACTORIES = new HashMap<String, CHTFontFactory>();
    //    public static final CHTFontFactory lookup(File file) {
    //        String key = file.getAbsolutePath();
    //        CHTFontFactory chtFontFactory = FACTORIES.get(key);
    //        if (chtFontFactory == null) {
    //            try {
    //                LOGGER.debug("Loading font : {} ", key);
    //                chtFontFactory = new CHTFontFactory(file);
    //                LOGGER.debug("Loaded font success!! ");
    //            } catch (Exception e) {
    //                e.printStackTrace();
    //                chtFontFactory = Default;
    //            }
    //            FACTORIES.put(key, chtFontFactory);
    //        }
    //        if (chtFontFactory == null) {
    //            return Default;
    //        }
    //        return chtFontFactory;
    //    }

    //====
    //== [Static Method] Block Stop 
    //================================================
    //== [Accessor] Block Start
    //====
    //====
    //== [Accessor] Block Stop 
    //================================================
    //== [Overrided Method] Block Start (Ex. toString/equals+hashCode)
    //====
    //====
    //== [Overrided Method] Block Stop 
    //================================================
    //== [Method] Block Start
    //====

    //####################################################################
    //## [Method] sub-block : BaseFont
    //####################################################################

    public BaseFont getBaseFont() {
        checkFont(0);
        return this.baseFont[0];
    }

    public BaseFont getBaseFont(int plane) {
        checkFont(plane);
        return this.baseFont[plane];
    }

    public java.awt.Font getAwtFont(int plane) {
        checkFont(plane);
        final java.awt.Font font = this.awtFont[plane];
        return font; // ObjectUtils.defaultIfNull(font, this.awtFont[0]);
    }

    //####################################################################
    //## [Method] sub-block : 標準字型
    //####################################################################

    public FontInfo createFontInfo(final int size) {
        return createFontInfo(size, (FontStyle) null, Color.BLACK);
    }

    public FontInfo createFontInfo(final int size, final Set<FontStyle> style, final Color color) {
        final FontInfo fontInfo = new FontInfo(this, size, FontStyle.toITextCode(style), color);
        return fontInfo;
    }

    public FontInfo createFontInfo(final int size, final Set<FontStyle> style, final Color color, final Color bColor) {
        final FontInfo fontInfo = new FontInfo(this, size, FontStyle.toITextCode(style), color, bColor);
        return fontInfo;
    }

    public FontInfo createFontInfo(final int size, final FontStyle style, final Color color) {
        final FontInfo fontInfo = new FontInfo(this, size, style == null ? 0 : style.getCode(), color);
        return fontInfo;
    }

    public FontInfo createFontInfo(final int size, final FontStyle style, final Color color, final Color bColor) {
        final FontInfo fontInfo = new FontInfo(this, size, style == null ? 0 : style.getCode(), color, bColor);
        return fontInfo;
    }

    //####################################################################
    //## [Method] sub-block : 標準字型
    //####################################################################

    public Font getNormalChinese(final int size) throws DocumentException {
        return getNormalChinese(size, Color.BLACK);
    }

    public Font getNormalChinese(final int size, final Color color) throws DocumentException {
        final FontInfo fontStyle = new FontInfo(this, size, Font.NORMAL, color);
        try {
            return CACHE_F.get(fontStyle);
        } catch (ExecutionException e) {
            throw new DocumentException(e);
        }
    }

    public Font getFont(final int size, final Set<FontStyle> style, final Color color) throws DocumentException {
        final FontInfo fontStyle = createFontInfo(size, style, color);
        try {
            return CACHE_F.get(fontStyle);
        } catch (ExecutionException e) {
            throw new DocumentException(e);
        }

    }

    public RisFontSelector getFontSelector(int size) throws DocumentException {
        return getFontSelector(size, EnumSet.noneOf(FontStyle.class), Color.BLACK);
    }

    public RisFontSelector getFontSelector(final int size, final Set<FontStyle> style, final Color color) throws DocumentException {
        return getFontSelector(createFontInfo(size, style, color));
    }

    public RisFontSelector getFontSelector(FontInfo fontInfo) throws DocumentException {
        try {
            return CACHE_FS.get(fontInfo);
        } catch (ExecutionException e) {
            throw new DocumentException(e);
        }
    }

    //====
    //== [Method] Block Stop 
    //================================================

}
