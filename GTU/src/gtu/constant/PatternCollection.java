package gtu.constant;

public interface PatternCollection {
    public static final String VIDEO_PATTERN = "(avi|rmvb|rm|mp4|mp3|m4a|flv|3gp|flac|webm|wmv|mkv|m4s)";
    public static final String PICTURE_PATTERN = "(jpg|jpeg|gif|tif|png|bmp)";
    public static final String VIDEO_AND_PICTURE_PATTERN = "(mp4|avi|flv|rm|rmvb|3gp|mp3|wmv|jpg|jpeg|gif|tif|png|bmp)";
    public static final String HYPER_LINK_PATTERN = "https?\\:\\/\\/[\\w+\\.\\/\\~\\-\\&\\?\\+\\.\\=\\:]+";
}
