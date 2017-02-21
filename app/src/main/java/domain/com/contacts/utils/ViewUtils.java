package domain.com.contacts.utils;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

/**
 * Created by sumandas on 05/02/2017.
 */

public class ViewUtils {

    public static void setTint(Context context, ImageView imageView, int notSetColor, int setColor, boolean isSet) {
        imageView.setColorFilter(ContextCompat.getColor(context, isSet ? setColor : notSetColor));

    }
}
