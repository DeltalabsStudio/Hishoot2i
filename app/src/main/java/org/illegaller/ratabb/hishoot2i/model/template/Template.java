package org.illegaller.ratabb.hishoot2i.model.template;

import org.illegaller.ratabb.hishoot2i.model.template.builder.BaseBuilder;

import android.graphics.Point;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

public class Template implements Parcelable {

    public static final Parcelable.Creator<Template> CREATOR = new Parcelable.Creator<Template>() {
        @Override public Template createFromParcel(Parcel source) {
            return new Template(source);
        }

        @Override public Template[] newArray(int size) {
            return new Template[size];
        }
    };
    public String id;
    public String name;
    public String author;
    public String frameFile;
    public String previewFile;
    @Nullable public String glareFile;
    @Nullable public String shadowFile;
    @Nullable public Point overlayOffset;
    public Point templatePoint;
    public Point leftTop;
    public Point rightTop;
    public Point leftBottom;
    public Point rightBottom;
    public TemplateType type;

    private Template() {
    }

    protected Template(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.author = in.readString();
        this.frameFile = in.readString();
        this.previewFile = in.readString();
        this.glareFile = in.readString();
        this.shadowFile = in.readString();
        this.overlayOffset = in.readParcelable(Point.class.getClassLoader());
        this.templatePoint = in.readParcelable(Point.class.getClassLoader());
        this.leftTop = in.readParcelable(Point.class.getClassLoader());
        this.rightTop = in.readParcelable(Point.class.getClassLoader());
        this.leftBottom = in.readParcelable(Point.class.getClassLoader());
        this.rightBottom = in.readParcelable(Point.class.getClassLoader());
        int tmpType = in.readInt();
        this.type = tmpType == -1 ? null : TemplateType.values()[tmpType];
    }

    public static Template build(BaseBuilder builder) {
        Template template = new Template();
        template.id = builder.id;
        template.name = builder.name;
        template.author = builder.author;
        template.templatePoint = builder.templatePoint;
        template.type = builder.type;
        template.previewFile = builder.previewFile;
        template.frameFile = builder.frameFile;
        template.glareFile = builder.glareFile;
        template.shadowFile = builder.shadowFile;
        template.overlayOffset = builder.overlayOffset;
        template.leftTop = builder.leftTop;
        template.rightTop = builder.rightTop;
        template.leftBottom = builder.leftBottom;
        template.rightBottom = builder.rightBottom;
        return template;
    }

    @Override public String toString() {
        return "Template{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", frameFile='" + frameFile + '\'' +
                ", previewFile='" + previewFile + '\'' +
                ", glareFile='" + glareFile + '\'' +
                ", shadowFile='" + shadowFile + '\'' +
                ", overlayOffset=" + overlayOffset +
                ", templatePoint=" + templatePoint +
                ", leftTop=" + leftTop +
                ", rightTop=" + rightTop +
                ", leftBottom=" + leftBottom +
                ", rightBottom=" + rightBottom +
                ", type=" + type +
                '}';
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.author);
        dest.writeString(this.frameFile);
        dest.writeString(this.previewFile);
        dest.writeString(this.glareFile);
        dest.writeString(this.shadowFile);
        dest.writeParcelable(this.overlayOffset, flags);
        dest.writeParcelable(this.templatePoint, flags);
        dest.writeParcelable(this.leftTop, flags);
        dest.writeParcelable(this.rightTop, flags);
        dest.writeParcelable(this.leftBottom, flags);
        dest.writeParcelable(this.rightBottom, flags);
        dest.writeInt(this.type == null ? -1 : this.type.ordinal());
    }
}
