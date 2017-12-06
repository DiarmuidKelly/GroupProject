// Generated code from Butter Knife. Do not modify!
package com.google.firebase.example.fireeats.adapter;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.google.firebase.example.fireeats.R;
import java.lang.IllegalStateException;
import java.lang.Override;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class RestaurantAdapter$ViewHolder_ViewBinding implements Unbinder {
  private RestaurantAdapter.ViewHolder target;

  @UiThread
  public RestaurantAdapter$ViewHolder_ViewBinding(RestaurantAdapter.ViewHolder target,
      View source) {
    this.target = target;

    target.imageView = Utils.findRequiredViewAsType(source, R.id.restaurant_item_image, "field 'imageView'", ImageView.class);
    target.nameView = Utils.findRequiredViewAsType(source, R.id.restaurant_item_name, "field 'nameView'", TextView.class);
    target.ratingBar = Utils.findRequiredViewAsType(source, R.id.restaurant_item_rating, "field 'ratingBar'", MaterialRatingBar.class);
    target.numRatingsView = Utils.findRequiredViewAsType(source, R.id.restaurant_item_num_ratings, "field 'numRatingsView'", TextView.class);
    target.priceView = Utils.findRequiredViewAsType(source, R.id.restaurant_item_price, "field 'priceView'", TextView.class);
    target.categoryView = Utils.findRequiredViewAsType(source, R.id.restaurant_item_category, "field 'categoryView'", TextView.class);
    target.cityView = Utils.findRequiredViewAsType(source, R.id.restaurant_item_city, "field 'cityView'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    RestaurantAdapter.ViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.imageView = null;
    target.nameView = null;
    target.ratingBar = null;
    target.numRatingsView = null;
    target.priceView = null;
    target.categoryView = null;
    target.cityView = null;
  }
}
