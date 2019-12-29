package com.xgzq.yu.reader.model.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.xgzq.yu.reader.constant.Files;
import com.xgzq.yu.reader.model.bean.Book;
import com.xgzq.yu.reader.model.enums.DisplayMode;
import com.xgzq.yu.reader.model.enums.ReadMode;
import com.xgzq.yu.reader.model.interfaces.IListModel;
import com.xgzq.yu.reader.utils.ThreadUtil;
import com.xgzq.yu.reader.widget.ReadView;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.schedulers.Schedulers;

public class BookModel implements IListModel<Book> {


    public interface Callback {
        void onBookInitialized(BookModelData bookModelData);
    }

    public class BookModelData {
        private List<Book> books;
        private ReadConfig readConfig;

        private BookModelData() {
            books = new ArrayList<>(15);
            readConfig = new ReadConfig();
        }

        private BookModelData(List<Book> books, ReadConfig readConfig) {
            this.books = books;
            this.readConfig = readConfig;
        }

        public List<Book> getBooks() {
            return books;
        }

        public ReadConfig getReadConfig() {
            return readConfig;
        }
    }

    public static class ReadConfig implements Parcelable {

        /**
         * 书架展示模式
         */
        private DisplayMode displayMode;

        /**
         * 阅读模式
         */
        private ReadMode readMode;

        /**
         * 背景色
         */
        private int backgroundColor;

        /**
         * 文字颜色
         */
        private int fontColor;

        /**
         * 行间距
         */
        private int lineSpacing;

        /**
         * 字体大小
         */
        private int fontSize;

        /**
         * 当前阅读章节，下标从0开始
         */
        private int chapterIndex;

        /**
         * 亮度
         */
        private int brightness;

        /**
         * 从章节目录到当前阅读点的偏移量
         */
        private long offset;


        private ReadConfig() {
            displayMode = DisplayMode.LIST;
            readMode = ReadMode.TURN;
            backgroundColor = ReadView.COLOR_BACKGROUND;
            fontColor = ReadView.COLOR_TEXT;
            lineSpacing = ReadView.LINE_SPACING;
            fontSize = ReadView.FONT_SIZE;
            chapterIndex = 0;
            brightness = 0;
            offset = 0;
        }

        private ReadConfig(Parcel in) {
            displayMode = in.readParcelable(DisplayMode.class.getClassLoader());
            readMode = in.readParcelable(ReadMode.class.getClassLoader());
            backgroundColor = in.readInt();
            fontColor = in.readInt();
            lineSpacing = in.readInt();
            fontSize = in.readInt();
            chapterIndex = in.readInt();
            brightness = in.readInt();
            offset = in.readLong();
        }


        public DisplayMode getDisplayMode() {
            return displayMode;
        }

        private void setDisplayMode(DisplayMode displayMode) {
            this.displayMode = displayMode;
        }

        public ReadMode getReadMode() {
            return readMode;
        }

        public void setReadMode(ReadMode readMode) {
            this.readMode = readMode;
        }

        public int getBackgroundColor() {
            return backgroundColor;
        }

        public void setBackgroundColor(int backgroundColor) {
            this.backgroundColor = backgroundColor;
        }

        public int getFontColor() {
            return fontColor;
        }

        public void setFontColor(int fontColor) {
            this.fontColor = fontColor;
        }

        public int getLineSpacing() {
            return lineSpacing;
        }

        public void setLineSpacing(int lineSpacing) {
            this.lineSpacing = lineSpacing;
        }

        public int getFontSize() {
            return fontSize;
        }

        public void setFontSize(int fontSize) {
            this.fontSize = fontSize;
        }

        public int getChapterIndex() {
            return chapterIndex;
        }

        public void setChapterIndex(int chapterIndex) {
            this.chapterIndex = chapterIndex;
        }

        public int getBrightness() {
            return brightness;
        }

        public void setBrightness(int brightness) {
            this.brightness = brightness;
        }

        public long getOffset() {
            return offset;
        }

        public void setOffset(long offset) {
            this.offset = offset;
        }

        @Override
        public String toString() {
            return "ReadConfig{" +
                    "displayMode=" + displayMode +
                    ", readMode=" + readMode +
                    ", backgroundColor=" + backgroundColor +
                    ", fontColor=" + fontColor +
                    ", lineSpacing=" + lineSpacing +
                    ", fontSize=" + fontSize +
                    ", chapterIndex=" + chapterIndex +
                    ", brightness=" + brightness +
                    ", offset=" + offset +
                    '}';
        }

        public static final Creator<ReadConfig> CREATOR = new Creator<ReadConfig>() {
            @Override
            public ReadConfig createFromParcel(Parcel in) {
                return new ReadConfig(in);
            }

            @Override
            public ReadConfig[] newArray(int size) {
                return new ReadConfig[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeParcelable(displayMode, flags);
            dest.writeParcelable(readMode, flags);
            dest.writeInt(backgroundColor);
            dest.writeInt(fontColor);
            dest.writeInt(lineSpacing);
            dest.writeInt(fontSize);
            dest.writeInt(chapterIndex);
            dest.writeInt(brightness);
            dest.writeLong(offset);
        }
    }

    private static final String TAG = "XGZQ:BookModel";

    private BookModelData mBookModelData;
    private Callback mCallback;

    public BookModel(Callback callback) {
        this.mBookModelData = new BookModelData();
        this.mCallback = callback;
        initData();
    }

    /**
     * 初始化数据
     * 数据初始化在computation线程，回调在UI线程
     */
    private void initData() {
        Log.d(TAG, "initBooks");
        Observable<List<Book>> initBookTask = Observable.create(new ObservableOnSubscribe<List<Book>>() {
            @Override
            public void subscribe(ObservableEmitter<List<Book>> emitter) throws Exception {
                ThreadUtil.printThread(TAG + ": initBookTask");
                List<Book> books = listBooks();
                emitter.onNext(books);
            }
        });

        ObservableSource<ReadConfig> initConfigTask = new ObservableSource<ReadConfig>() {
            @Override
            public void subscribe(Observer<? super ReadConfig> observer) {
                ThreadUtil.printThread(TAG + ": initConfigTask");
                // TODO: 从数据库获取配置参数。包括但不限于列表展示方式，文字大小，颜色，间距，排序等
                observer.onNext(new ReadConfig());
            }
        };
        Observable
                .zip(initBookTask, initConfigTask, new BiFunction<List<Book>, ReadConfig, BookModelData>() {
                    @Override
                    public BookModelData apply(List<Book> books, ReadConfig readConfig) throws Exception {
                        ThreadUtil.printThread(TAG + ": zip.apply");
                        return new BookModelData(books, readConfig);
                    }
                })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BookModelData>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        ThreadUtil.printThread(TAG + ": onSubscribe");
                    }

                    @Override
                    public void onNext(BookModelData bookModelData) {
                        ThreadUtil.printThread(TAG + ": onNext");
                        BookModel.this.mBookModelData = bookModelData;
                        if (mCallback != null) {
                            mCallback.onBookInitialized(bookModelData);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        ThreadUtil.printThread(TAG + ": onError: " + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        ThreadUtil.printThread(TAG + ": onComplete");
                    }
                });
    }


    @Override
    public List<Book> getList() {
        return mBookModelData.getBooks();
    }

    @Override
    public Book get(int position) {
        List<Book> books = getList();
        return books != null ? books.get(position) : null;
    }

    public void setDisplayMode(DisplayMode model) {
        this.mBookModelData.getReadConfig().setDisplayMode(model);
        // TODO: 保存到数据库
    }

    public ReadConfig getBookConfig() {
        return mBookModelData.getReadConfig();
    }


    private List<Book> listBooks() {
        final List<Book> books = new ArrayList<>();
        final File booksDir = new File(Files.DIR_BOOKS);
        if (!booksDir.exists() || !booksDir.isDirectory()) {
            return books;
        }
        Log.d(TAG, "path: " + booksDir.getAbsolutePath());
        Log.d(TAG, "files: " + Arrays.toString(booksDir.list()));
        /**
         * TODO:申请权限
         */
        final String[] txtFileNames = booksDir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                Log.d(TAG, dir.getAbsolutePath());
                Log.d(TAG, booksDir.getAbsolutePath());
                return dir.getAbsolutePath() == booksDir.getAbsolutePath() && name.endsWith(".txt");
            }
        });

        final String booksDirPath = booksDir.getAbsolutePath();
        for (String name : txtFileNames) {
            Book book = new Book(booksDirPath + File.separator + name);
            book.setName(name);
            books.add(book);
        }
        for (int i = 0; i < books.size(); i++) {
            Log.d(TAG, "listBooks: " + books.get(i).toString());
        }
        return books;
    }

}
