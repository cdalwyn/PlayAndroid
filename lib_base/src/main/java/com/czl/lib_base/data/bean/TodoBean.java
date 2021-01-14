package com.czl.lib_base.data.bean;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.czl.lib_base.BR;

import java.util.List;
import java.util.Objects;

/**
 * @author Alwyn
 * @Date 2021/1/14
 * @Description
 */
public class TodoBean implements Parcelable {

    private int curPage;
    private int offset;
    public boolean over;
    private int pageCount;
    private int size;
    private int total;
    private List<Data> datas;

    protected TodoBean(Parcel in) {
        curPage = in.readInt();
        offset = in.readInt();
        over = in.readByte() != 0;
        pageCount = in.readInt();
        size = in.readInt();
        total = in.readInt();
    }

    public static final Creator<TodoBean> CREATOR = new Creator<TodoBean>() {
        @Override
        public TodoBean createFromParcel(Parcel in) {
            return new TodoBean(in);
        }

        @Override
        public TodoBean[] newArray(int size) {
            return new TodoBean[size];
        }
    };

    public int getCurPage() {
        return curPage;
    }

    public void setCurPage(int curPage) {
        this.curPage = curPage;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public boolean isOver() {
        return over;
    }

    public void setOver(boolean over) {
        this.over = over;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Data> getDatas() {
        return datas;
    }

    public void setDatas(List<Data> datas) {
        this.datas = datas;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TodoBean todoBean = (TodoBean) o;
        return curPage == todoBean.curPage &&
                offset == todoBean.offset &&
                over == todoBean.over &&
                pageCount == todoBean.pageCount &&
                size == todoBean.size &&
                total == todoBean.total &&
                Objects.equals(datas, todoBean.datas);
    }

    @Override
    public int hashCode() {
        return Objects.hash(curPage, offset, over, pageCount, size, total, datas);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(curPage);
        dest.writeInt(offset);
        dest.writeByte((byte) (over ? 1 : 0));
        dest.writeInt(pageCount);
        dest.writeInt(size);
        dest.writeInt(total);
    }

    public static class Data extends BaseObservable implements Parcelable{

        private String completeDate;
        private String completeDateStr;
        private String content;
        private long date;
        private String dateStr;
        private int id;
        private int priority;
        private int status;
        private String title;
        private int type;
        private int userId;
        public boolean dateVisible;
        public boolean dateExpired;


        protected Data(Parcel in) {
            completeDate = in.readString();
            completeDateStr = in.readString();
            content = in.readString();
            date = in.readLong();
            dateStr = in.readString();
            id = in.readInt();
            priority = in.readInt();
            status = in.readInt();
            title = in.readString();
            type = in.readInt();
            userId = in.readInt();
            dateVisible = in.readByte() != 0;
            dateExpired = in.readByte() != 0;
        }

        public static final Creator<Data> CREATOR = new Creator<Data>() {
            @Override
            public Data createFromParcel(Parcel in) {
                return new Data(in);
            }

            @Override
            public Data[] newArray(int size) {
                return new Data[size];
            }
        };

        @Bindable
        public boolean isDateVisible() {
            return dateVisible;
        }

        public void setDateVisible(boolean dateVisible) {
            this.dateVisible = dateVisible;
            notifyPropertyChanged(BR.dateVisible);
        }

        @Bindable
        public boolean isDateExpired() {
            return dateExpired;
        }

        public void setDateExpired(boolean dateExpired) {
            this.dateExpired = dateExpired;
            notifyPropertyChanged(BR.dateExpired);
        }

        public String getCompleteDate() {
            return completeDate;
        }

        public void setCompleteDate(String completeDate) {
            this.completeDate = completeDate;
        }

        public String getCompleteDateStr() {
            return completeDateStr;
        }

        public void setCompleteDateStr(String completeDateStr) {
            this.completeDateStr = completeDateStr;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public long getDate() {
            return date;
        }

        public void setDate(long date) {
            this.date = date;
        }

        public String getDateStr() {
            return dateStr;
        }

        public void setDateStr(String dateStr) {
            this.dateStr = dateStr;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPriority() {
            return priority;
        }

        public void setPriority(int priority) {
            this.priority = priority;
        }

        @Bindable
        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
            notifyPropertyChanged(BR.status);
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Data data = (Data) o;
            return date == data.date &&
                    id == data.id &&
                    priority == data.priority &&
                    status == data.status &&
                    type == data.type &&
                    userId == data.userId &&
                    dateVisible == data.dateVisible &&
                    dateExpired == data.dateExpired &&
                    Objects.equals(completeDate, data.completeDate) &&
                    Objects.equals(completeDateStr, data.completeDateStr) &&
                    Objects.equals(content, data.content) &&
                    Objects.equals(dateStr, data.dateStr) &&
                    Objects.equals(title, data.title);
        }

        @Override
        public int hashCode() {
            return Objects.hash(completeDate, completeDateStr, content, date, dateStr, id, priority, status, title, type, userId, dateVisible, dateExpired);
        }

        @Override
        public String toString() {
            return "Data{" +
                    "completeDate='" + completeDate + '\'' +
                    ", completeDateStr='" + completeDateStr + '\'' +
                    ", content='" + content + '\'' +
                    ", date=" + date +
                    ", dateStr='" + dateStr + '\'' +
                    ", id=" + id +
                    ", priority=" + priority +
                    ", status=" + status +
                    ", title='" + title + '\'' +
                    ", type=" + type +
                    ", userId=" + userId +
                    ", dateVisible=" + dateVisible +
                    ", dateExpired=" + dateExpired +
                    '}';
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(completeDate);
            dest.writeString(completeDateStr);
            dest.writeString(content);
            dest.writeLong(date);
            dest.writeString(dateStr);
            dest.writeInt(id);
            dest.writeInt(priority);
            dest.writeInt(status);
            dest.writeString(title);
            dest.writeInt(type);
            dest.writeInt(userId);
            dest.writeByte((byte) (dateVisible ? 1 : 0));
            dest.writeByte((byte) (dateExpired ? 1 : 0));
        }
    }
}
