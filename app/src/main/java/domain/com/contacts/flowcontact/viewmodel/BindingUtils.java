package domain.com.contacts.flowcontact.viewmodel;

import android.databinding.BindingAdapter;
import android.widget.ImageView;

import domain.com.contacts.utils.ImageUtils;

/**
 * Created by sumandas on 04/02/2017.
 */

public class BindingUtils {
    @BindingAdapter({"imageUrl"})
    public static void setImageUrl(ImageView view, String url) {
        ImageUtils.convertImageUrlToRoundedView(view.getContext(),url,view);
    }
}
