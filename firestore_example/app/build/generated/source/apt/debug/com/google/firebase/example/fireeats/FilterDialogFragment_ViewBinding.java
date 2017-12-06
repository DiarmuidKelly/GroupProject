// Generated code from Butter Knife. Do not modify!
package com.google.firebase.example.fireeats;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.view.View;
import android.widget.Spinner;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class FilterDialogFragment_ViewBinding implements Unbinder {
  private FilterDialogFragment target;

  private View view2131296302;

  private View view2131296296;

  @UiThread
  public FilterDialogFragment_ViewBinding(final FilterDialogFragment target, View source) {
    this.target = target;

    View view;
    target.mCategorySpinner = Utils.findRequiredViewAsType(source, R.id.spinner_category, "field 'mCategorySpinner'", Spinner.class);
    target.mCitySpinner = Utils.findRequiredViewAsType(source, R.id.spinner_city, "field 'mCitySpinner'", Spinner.class);
    target.mSortSpinner = Utils.findRequiredViewAsType(source, R.id.spinner_sort, "field 'mSortSpinner'", Spinner.class);
    target.mPriceSpinner = Utils.findRequiredViewAsType(source, R.id.spinner_price, "field 'mPriceSpinner'", Spinner.class);
    view = Utils.findRequiredView(source, R.id.button_search, "method 'onSearchClicked'");
    view2131296302 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onSearchClicked();
      }
    });
    view = Utils.findRequiredView(source, R.id.button_cancel, "method 'onCancelClicked'");
    view2131296296 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onCancelClicked();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    FilterDialogFragment target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mCategorySpinner = null;
    target.mCitySpinner = null;
    target.mSortSpinner = null;
    target.mPriceSpinner = null;

    view2131296302.setOnClickListener(null);
    view2131296302 = null;
    view2131296296.setOnClickListener(null);
    view2131296296 = null;
  }
}
