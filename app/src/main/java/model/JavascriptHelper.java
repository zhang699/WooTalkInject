package model;

import android.os.Handler;
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
    private static final int CALLBACK_DELAY = 300;


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

    public void postDelayed(Runnable action, long ms){
        mWebView.postDelayed(action, ms);
    }

    public void callAndWaitForSpecifiedSelector(final String name, final String finishedSelector, final FinishCallback callback){

        String executedJavaScript = "javascript:"+name;

        mWebView.loadUrl(executedJavaScript);
        final String id = showView();

        HtmlViewerJavascriptInterface.Callback result = new HtmlViewerJavascriptInterface.Callback(){
            @Override
            public void onResult(String html) {
                if (finishedSelector.isEmpty()){
                    executeCallback(name);
                    return;
                }

                Document doc = Jsoup.parse(html);

                Elements element = doc.select(finishedSelector);
                //Log.d("ElementIs", element.toString());
                boolean isFindSelector = element != null && element.size() > 0 ;

                if (isFindSelector){
                    //deferred.resolve(name);
                    executeCallback(name);
                }else{
                    executeCallback(null);
                }
            }

            private void executeCallback(final Object result) {
                mWebView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        callback.onFinish(result);
                    }
                }, CALLBACK_DELAY);

                mHtmlViewerJavascriptInterface.removeCallback(id);
            }
        };
        mHtmlViewerJavascriptInterface.addWaitCallback(id, false, result);
        //return deferred.promise();
    }

    public void reload(){
        if (mWebView != null){
            Handler handler = mWebView.getHandler();
            if (handler != null){
                handler.removeCallbacksAndMessages(null);
            }

        }


    }

    public String showView(){
        String randomId =  UUID.randomUUID().toString();

        String showHtml = String.format("javascript:HtmlViewer.showHTML" +
                "('%s', '<html>'+document.getElementsByTagName('html')[0].innerHTML+'</html>');", randomId);

        mWebView.loadUrl(showHtml);
        return randomId;

    }

}
