package com.ag.controls.common.util;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class WebviewUtils {

    public static String ReplaceHtml(String rootUrl, String html) {

        String data = html.
                replace("&#34;", "\"").
                replace("src=\"/", "src=\"" + rootUrl);

        System.out.println("html=" + data);

        if (html.contains("</html>")) {
            Log.i("replaceTag", "含有html");
            return SetImageCss(data);
        }
        return data = "<html><body><p style=\"word-break:break-all;\">" + SetImageCss(data) + "</P></body></html>";
    }

    public static String SetImageCss(String htmlContent) {
        Document doc_Dis = Jsoup.parse(htmlContent);
        Elements ele_Img = doc_Dis.getElementsByTag("img");
        if (ele_Img.size() != 0) {
            for (Element e_Img : ele_Img) {
                e_Img.attr("style", "width:100%");
            }
        }
        return doc_Dis.toString();
    }

}
