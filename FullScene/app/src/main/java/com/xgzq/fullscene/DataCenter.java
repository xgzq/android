package com.xgzq.fullscene;

import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.List;

public class DataCenter {

    public static void getAlbumData(final ICallback<List<Album>> callback) {
        new AsyncTask<Void, Void, List<Album>>() {
            @Override
            protected List<Album> doInBackground(Void... voids) {
                return genAlbumData();
            }

            @Override
            protected void onPostExecute(List<Album> albums) {
                callback.callback(albums);
            }
        }.execute();
    }

    public static List<Album> genAlbumData() {
        List<Album> albums = new ArrayList<>();
        albums.add(new Album("第一张专辑", "第一个艺术家", "1.5万", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1607105589905&di=bb6311650cc5eb7facd75085ece94017&imgtype=0&src=http%3A%2F%2Fn.sinaimg.cn%2Fsinacn20%2F560%2Fw1080h1080%2F20180710%2F98c7-hfefkqp7625250.jpg"));
        albums.add(new Album("第二张专辑", "第二艺术家", "22万", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1607021621876&di=5bcc70d502f57c6c2f869cb2aeacb6ee&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201901%2F18%2F20190118190003_kopap.jpeg"));
        albums.add(new Album("第三张专辑", "第三个艺术家", "156万", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1607021621874&di=9f712c358f0c2817a29ce0dfa49d8cea&imgtype=0&src=http%3A%2F%2Fp4.itc.cn%2Fimages01%2F20201110%2F687316f736ca4418b989a1362ee02306.jpeg"));
        albums.add(new Album("第四张专辑", "第四个艺术家", "5641万", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1607105589904&di=81b3443a64744344ce7045400026c9d9&imgtype=0&src=http%3A%2F%2Fimg1.doubanio.com%2Fview%2Fgroup_topic%2Fl%2Fpublic%2Fp101503309.jpg"));
        albums.add(new Album("第五张专辑", "第五个艺术家", "0.1万", "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1607105589904&di=c4cce973f66886e85c0729305a4edad7&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201804%2F10%2F20180410161008_mpwam.jpeg"));
        return albums;
    }
}
