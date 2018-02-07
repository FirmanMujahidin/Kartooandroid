package co.kartoo.app.category;

public class ImageItem {
    private int image;
    private String title;
    private String caption;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ImageItem(String title, String caption, int image, String id) {
        super();
        this.image = image;
        this.title = title;
        this.caption = caption;
        this.id = id;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getCaption() {
        return caption;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
