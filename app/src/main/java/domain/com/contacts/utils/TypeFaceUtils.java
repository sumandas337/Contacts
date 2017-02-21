package domain.com.contacts.utils;

import android.content.Context;
import android.graphics.Typeface;

import java.util.HashMap;


/**
 * Created by sumandas on 29/01/2017.
 */

public class TypeFaceUtils {

    private static final HashMap<String, Typeface> mTypeFaceCache = new HashMap<String, Typeface>();
    private static final String FONT_PATH = "fonts/%s";

    public static Typeface get(Context context, String typeFaceName){
        synchronized(mTypeFaceCache){
            if(!mTypeFaceCache.containsKey(typeFaceName)){
                Typeface typeface = Typeface.createFromAsset(
                        context.getAssets(),
                        String.format(FONT_PATH, typeFaceName)
                );
                mTypeFaceCache.put(typeFaceName, typeface);
            }
            return mTypeFaceCache.get(typeFaceName);
        }
    }
}