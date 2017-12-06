// Generated code from Butter Knife. Do not modify!
package com.google.firebase.example.fireeats;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.EditText;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class RatingDialogFragment_ViewBinding implements Unbinder {
  private RatingDialogFragment target;

  private View view2131296430;

  private View view2131296431;

  @UiThread
  public RatingDialogFragment_ViewBinding(final RatingDialogFragment target, View source) {
    this.target = target;

    View view;
    target.mRatingBar = Utils.findRequiredViewAsType(source, R.id.restaurant_form_rating, "field 'mRatingBar'", MaterialRatingBar.class);
    target.mRatingText = Utils.findRequiredViewAsType(source, R.id.restaurant_form_text, "field 'mRatingText'", EditText.class);
    view = Utils.findRequiredView(source, R.id.restaurant_form_button, "method 'onSubmitClicked'");
    view2131296430 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onSubmitClicked(p0);
      }
    });
    view = Utils.findRequiredView(source, R.id.restaurant_form_cancel, "method 'onCancelClicked'");
    view2131296431 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onCancelClicked(p0);
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    RatingDialogFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mRatingBar = null;
    target.mRatingText = null;

    view2131296430.setOnClickListener(null);
    view2131296430 = null;
    view2131296431.setOnClickListener(null);
    view2131296431 = null;
  }
}
