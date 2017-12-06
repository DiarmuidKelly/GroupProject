// Generated code from Butter Knife. Do not modify!
package com.google.firebase.example.fireeats;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class RestaurantDetailActivity_ViewBinding implements Unbinder {
  private RestaurantDetailActivity target;

  private View view2131296426;

  private View view2131296344;

  @UiThread
  public RestaurantDetailActivity_ViewBinding(RestaurantDetailActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public RestaurantDetailActivity_ViewBinding(final RestaurantDetailActivity target, View source) {
    this.target = target;

    View view;
    target.mImageView = Utils.findRequiredViewAsType(source, R.id.restaurant_image, "field 'mImageView'", ImageView.class);
    target.mNameView = Utils.findRequiredViewAsType(source, R.id.restaurant_name, "field 'mNameView'", TextView.class);
    target.mRatingIndicator = Utils.findRequiredViewAsType(source, R.id.restaurant_rating, "field 'mRatingIndicator'", MaterialRatingBar.class);
    target.mNumRatingsView = Utils.findRequiredViewAsType(source, R.id.restaurant_num_ratings, "field 'mNumRatingsView'", TextView.class);
    target.mCityView = Utils.findRequiredViewAsType(source, R.id.restaurant_city, "field 'mCityView'", TextView.class);
    target.mCategoryView = Utils.findRequiredViewAsType(source, R.id.restaurant_category, "field 'mCategoryView'", TextView.class);
    target.mPriceView = Utils.findRequiredViewAsType(source, R.id.restaurant_price, "field 'mPriceView'", TextView.class);
    target.mEmptyView = Utils.findRequiredViewAsType(source, R.id.view_empty_ratings, "field 'mEmptyView'", ViewGroup.class);
    target.mRatingsRecycler = Utils.findRequiredViewAsType(source, R.id.recycler_ratings, "field 'mRatingsRecycler'", RecyclerView.class);
    view = Utils.findRequiredView(source, R.id.restaurant_button_back, "method 'onBackArrowClicked'");
    view2131296426 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onBackArrowClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.fab_show_rating_dialog, "method 'onAddRatingClicked'");
    view2131296344 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onAddRatingClicked(p0);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    RestaurantDetailActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mImageView = null;
    target.mNameView = null;
    target.mRatingIndicator = null;
    target.mNumRatingsView = null;
    target.mCityView = null;
    target.mCategoryView = null;
    target.mPriceView = null;
    target.mEmptyView = null;
    target.mRatingsRecycler = null;

    view2131296426.setOnClickListener(null);
    view2131296426 = null;
    view2131296344.setOnClickListener(null);
    view2131296344 = null;
  }
}
