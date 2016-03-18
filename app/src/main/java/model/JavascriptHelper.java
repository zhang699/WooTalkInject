package model;

import android.util.Log;
import android.webkit.WebView;

import org.jdeferred.Deferred;
import org.jdeferred.impl.DeferredObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.UUID;

/**
 * Created by JimmyJhang on 2016/3/18.
 */
public class JavascriptHelper {
    private WebView mWebView;
    private final HtmlViewerJavascriptInterface mHtmlViewerJavascriptInterface;

    public interface FinishCallback {
        void onFinish(Object result);
    }

    public JavascriptHelper(WebView view){
        mWebView = view;

        mHtmlViewerJavascriptInterface = new HtmlViewerJavascriptInterface();
        mWebView.addJavascriptInterface(mHtmlViewerJavascriptInterface, "HtmlViewer");
    }

    public void changeWebView(WebView view){
        mWebView = view;
        //mWebView.addJavascriptInterface(mHtmlViewerJavascriptInterface, "HtmlViewer");
    }

    public void callAndWaitForSpecifiedSelector(final String name, final String finishedSelector, final FinishCallback callback){
        String executedJavaScript = "javascript:"+name;

        mWebView.loadUrl(executedJavaScript);
        final String id = showView();

        final Deferred deferred = new DeferredObject();

        HtmlViewerJavascriptInterface.Callback result = new HtmlViewerJavascriptInterface.Callback(){
            @Override
            public void onResult(String html) {


                Document doc = Jsoup.parse(html);

                Elements element = doc.select(finishedSelector);
                //Log.d("ElementIs", element.toString());
                boolean isFindSelector = element != null;

                if (finishedSelector.isEmpty() || isFindSelector){
                    //deferred.resolve(name);
                    mWebView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            callback.onFinish(name);
                        }
                    }, 1000);

                    mHtmlViewerJavascriptInterface.removeCallback(id);
                }
            }
        };
        mHtmlViewerJavascriptInterface.addWaitCallback(id, false, result);
        //return deferred.promise();
    }

    public String showView(){
        String randomId =  UUID.randomUUID().toString();

        String showHtml = String.format("javascript:HtmlViewer.showHTML" +
                "('%s', '<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');", randomId);

        Log.d("showView", showHtml);
        mWebView.loadUrl(showHtml);
        return randomId;

    }

}
