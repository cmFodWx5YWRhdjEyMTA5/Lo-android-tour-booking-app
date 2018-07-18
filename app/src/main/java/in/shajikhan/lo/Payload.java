package in.shajikhan.lo;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by djshaji on 7/4/17.
 */

public class Payload {
    public class Item {
        int id = 0 ;
        String name = null ;
        String type = null ;
        String description = null ;
        int price = 0 ;
        String icon = null ;
        String url = null;

        Item () {
            // for those who only stand and wait
        }

        Item (int id, String name, String type, String description, int price, String icon, String url) {
            this.id = id ;
            this.name = name ;
            this.url = url ;
            this.type = type ;
            this.description = description ;
            this.price = price ;
            this.icon = icon ;
        }
    }

    class ItemGroup extends Item {
        ArrayList addons = null ;

        public  ItemGroup () {
            super();
            this.addons = new ArrayList() ;
        }

        void add (int item) {
            addons.add (item) ;
        }
    }

    class Package extends ItemGroup {

    }

    static ArrayList items = new ArrayList();
    static ArrayList itemgroups = new ArrayList();
    static ArrayList packages = new ArrayList();
    Context context = null ;
    static boolean loaded = false ;

    Payload (Context context) {
        this.context = context ;
        /*try {
            load ("payload");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }*/
    }

    boolean isLoaded () {
        // hah! int to bool!
        // It's tricky to figure out
        // try it :0
        // this is actually fuckin brilliant
//        return packages.size() != 0;
        Log.e ("items", String.valueOf(items.size()));
        if (items.size() == 0) {
            return false;
        }
        else {
            return true;
        }
    }

    boolean load (String filename) throws IOException, XmlPullParserException {
        InputStream rawResource = null ;
        if (! filename.startsWith("http")) {
            try {
                rawResource = context.getResources().openRawResource(context.getResources().getIdentifier(filename, "raw", context.getPackageName()));
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        } else {
            URL url = new URL (null, filename);
            Log.e ("net", filename);
            try {
                rawResource = url.openStream();
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }

        parse(rawResource);
        loaded = true;
        return true;
    }

    void addItem (XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "item");
        int id = Integer.valueOf(parser.getAttributeValue(null, "id"));
        String name = parser.getAttributeValue(null, "name");
        String type = parser.getAttributeValue(null, "type");
        String description = null ;
        String url = null;
        int price = 0 ;
        String icon = null ;

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String tag = parser.getName();
            if (tag.equals("description")) {
                description = get (parser, tag) ;
            } else if (tag.equals("url")) {
                url = get (parser, tag) ;
            } else if (tag.equals("icon")) {
                icon = get (parser, tag) ;
            } else if (tag.equals("price")) {
//                Log.e("price", get (parser, tag));
//                can do this only once! the next time
//                we call get () the tag will be different!
                price = Integer.valueOf(get (parser, tag)) ;
            } else {
                skip(parser);
            }
        }

        Item item = new Item (id, name, type, description, price, icon, url) ;
        items.add (item) ;
    }

    void addItemGroup (XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "itemgroup");
        int id = Integer.valueOf(parser.getAttributeValue(null, "id"));
        String name = parser.getAttributeValue(null, "name");
        String type = parser.getAttributeValue(null, "type");
        String description = null ;
        int price = 0 ;
        String icon = null ;

        ItemGroup group = new ItemGroup();

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            String tag = parser.getName();
            if (tag.equals("description")) {
                description = get (parser, tag) ;
//                Log.e ("descr", description);
            } else if (tag.equals("icon")) {
                icon = get (parser, tag) ;
            } else if (tag.equals("price")) {
                price = Integer.valueOf(get (parser, tag)) ;
            } else if (tag.equals("addon")) {
                group.add (Integer.valueOf(get (parser, tag))) ;
            } else {
                Log.e ("parser", tag);
                skip(parser);
            }
        }

        group.id = id ;
        group.icon = icon ;
        group.price = price ;
        group.description = description ;
        group.name = name ;
        group.type = type ;

        itemgroups.add (group);
    }

    private static final String ns = null;

    public void parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);

            try{
                parser.nextTag();
            }
            catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            finally {
                read(parser);

            }
        } finally {
            in.close();
        }
    }

    private void read(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "payload");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("item")) {
                addItem(parser);
            } else if (name.equals("itemgroup")) {
                addItemGroup(parser);
            } else {
                skip(parser);
            }
        }

    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }

    void debug () {
        Log.e ("item", getItem(10).name);
        ItemGroup g = getItemGroup(2001);
        Log.e ("itemgroup", g.name);
        for (Object e: getItems("default")) {
            Item w = (Item) e ;
            Log.e ("items", w.name);
        }

        for (Object e: getItemgroups("package")) {
            ItemGroup j = (ItemGroup) e;
            Log.e("packages", j.name);
        }
    }


    private String get(XmlPullParser parser, String tag) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, tag);
        String title = getText(parser);
        parser.require(XmlPullParser.END_TAG, ns, tag);
        return title;
    }

    private String getText (XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }

        return result;
    }

    public ArrayList getItems (String type) {
        ArrayList list = new ArrayList();
        if (type == null) {
            list = items;
        } else {
            for (Object o: items) {
                Item i = (Item) o;
                if (i.type.equals(type)) {
                    list.add (i);
                }
            }
        }

        return list;
    }

    public  ArrayList getItemgroups (String type) {
        ArrayList list = new ArrayList();
        if (type == null) {
            list = itemgroups;
        } else {
            for (Object o: itemgroups) {
                ItemGroup i = (ItemGroup) o;
                if (i.type.equals(type)) {
                    list.add (i);
                }
            }
        }

        return list;
    }

    public Item getItem (int id) {
        for (Object o: items) {
            Item i = (Item) o;
            if (i.id == id) {
                return i;
            }
        }

        return null;
    }

    ArrayList getItemsFromString (String s) {
        if (s == null) {
            return new ArrayList() ;
        }
        else if (s.equals("")) {
            return new ArrayList() ;
        }

        String [] p = s.split (";") ;
        ArrayList list = new ArrayList();

        for (String a: p) {
            if (a.equals("")) {
                continue;
            }

            int id = -1 ;
            try {
                id = Integer.valueOf(a);
            } catch (NumberFormatException e) {
                e.printStackTrace();
                continue;
            }

            Item item = getItem(id);
            if (item == null) {
                item = getItemGroup(id);
            }

            if (item != null) {
                list.add(item);
            }
        }

        return list;
    }

    public ItemGroup getItemGroup (int id) {
        for (Object o: itemgroups) {
            ItemGroup i = (ItemGroup) o;
            if (i.id == id) {
                return i;
            }
        }

        return null;
    }

    //Todo: find icon
    public int findIcon (String icon) {
        int id = -1 ;
        try {
            id = context.getResources().getIdentifier(icon, "drawable", context.getPackageName());
        }
        catch (Exception e) {
            e.printStackTrace();
        }

        return id ;
    }

    void loadIcon (final ImageView view, String icon) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        view.setImageDrawable(context.getResources().getDrawable(R.drawable.loading3));
        imageLoader.loadImage(icon, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingComplete(String imageUri, View view1, Bitmap loadedImage) {
                view.setImageBitmap(loadedImage);
                view.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }
        });
    }

}