// Generated code from Butter Knife. Do not modify!
package com.google.firebase.example.fireeats.adapter;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.Utils;
import com.google.firebase.example.fireeats.R;
import java.lang.IllegalStateException;
import java.lang.Override;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class RatingAdapter$ViewHolder_ViewBinding implements Unbinder {
  private RatingAdapter.ViewHolder target;

  @UiThread
  public RatingAdapter$ViewHolder_ViewBinding(RatingAdapter.ViewHolder target, View source) {
    this.target = target;

    target.nameView = Utils.findRequiredViewAsType(source, R.id.rating_item_name, "field 'nameView'", TextView.class);
    target.ratingBar = Utils.findRequiredViewAsType(source, R.id.rating_item_rating, "field 'ratingBar'", MaterialRatingBar.class);
    target.textView = Utils.findRequiredViewAsType(source, R.id.rating_item_text, "field 'textView'", TextView.class);
    target.dateView = Utils.findRequiredViewAsType(source, R.id.rating_item_date, "field 'dateView'", TextView.class);
  }

  @Override
  @CallSuper
  public void unbind() {
    RatingAdapter.ViewHolder target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.nameView = null;
    target.ratingBar = null;
    target.textView = null;
    target.dateView = null;
  }
}
