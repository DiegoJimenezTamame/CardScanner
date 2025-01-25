package local.cardscanner;
import java.io.Serializable;

public class CardResponse implements Serializable {
    private String name; // Card name
    private String text; // Card rules text
    private ImageUris image_uris; // Nested object for card image

    // Getters
    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }

    public String getImageUrl() {
        return image_uris != null ? image_uris.getNormal() : null;
    }

    // Nested class for image URIs
    public static class ImageUris implements Serializable {
        private String normal;

        public String getNormal() {
            return normal;
        }
    }
}
