package gtu.constant;

public interface PatternCollection {
    public static final String VIDEO_PATTERN = "(mp4|avi|flv|rm|rmvb|3gp|mp3|wmv)";
    public static final String PICTURE_PATTERN = "(jpg|jpeg|gif|tif|png|bmp)";
    public static final String VIDEO_AND_PICTURE_PATTERN = "(mp4|avi|flv|rm|rmvb|3gp|mp3|wmv|jpg|jpeg|gif|tif|png|bmp)";
    public static final String HYPER_LINK_PATTERN = "https?\\:\\/\\/[\\w+\\.\\/\\~\\-\\&\\?\\+\\.\\=\\:]+";
}
