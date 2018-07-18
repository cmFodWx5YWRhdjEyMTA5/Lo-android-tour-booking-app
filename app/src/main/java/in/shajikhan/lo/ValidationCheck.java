package in.shajikhan.lo;

import android.content.Context;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;

/**
 * Created by djshaji on 7/11/17.
 */

public class ValidationCheck {
    Context context = null ;

    public ValidationCheck (Context context) {
        this.context = context;
    }

    public ArrayList getErrorInfo () {
        ArrayList info = new ArrayList();
        InputStream rawResource ;
        URL url = null ;
        try {
            url = new URL (null, "http://shajikhan.in/apps/dil/403");
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
            return null;
        }

        try {
            rawResource = url.openStream();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        Properties properties = new Properties();
        try {
            properties.load(rawResource);
        } catch (IOException e) {
            return null;
        }

        String msg = properties.getProperty("msg");
        String icon = properties.getProperty("icon");
        String link = properties.getProperty("url");
        info.add (icon);
        info.add (msg);
        info.add(link);

        return info;
    }

}
