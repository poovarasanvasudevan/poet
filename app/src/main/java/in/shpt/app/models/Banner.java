package in.shpt.app.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by iampo on 10/8/2016.
 */

public class Banner implements Parcelable {


    String title;
    String productId;
    String image;
    String description;
    String author;
    String language;
    String price;


    public Banner(String title, String productId, String image, String description, String author, String language, String price) {
        this.title = title;
        this.productId = productId;
        this.image = image;
        this.description = description;
        this.author = author;
        this.language = language;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.productId);
        dest.writeString(this.image);
        dest.writeString(this.description);
        dest.writeString(this.author);
        dest.writeString(this.language);
        dest.writeString(this.price);
    }

    protected Banner(Parcel in) {
        this.title = in.readString();
        this.productId = in.readString();
        this.image = in.readString();
        this.description = in.readString();
        this.author = in.readString();
        this.language = in.readString();
        this.price = in.readString();
    }

    public static final Parcelable.Creator<Banner> CREATOR = new Parcelable.Creator<Banner>() {
        @Override
        public Banner createFromParcel(Parcel source) {
            return new Banner(source);
        }

        @Override
        public Banner[] newArray(int size) {
            return new Banner[size];
        }
    };
}
