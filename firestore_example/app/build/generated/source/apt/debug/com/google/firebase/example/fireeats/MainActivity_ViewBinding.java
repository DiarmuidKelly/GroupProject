// Generated code from Butter Knife. Do not modify!
package com.google.firebase.example.fireeats;

import android.support.annotation.CallSuper;
import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Unbinder;
import butterknife.internal.DebouncingOnClickListener;
import butterknife.internal.Utils;
import java.lang.IllegalStateException;
import java.lang.Override;

public class MainActivity_ViewBinding implements Unbinder {
  private MainActivity target;

  private View view2131296349;

  private View view2131296297;

  @UiThread
  public MainActivity_ViewBinding(MainActivity target) {
    this(target, target.getWindow().getDecorView());
  }

  @UiThread
  public MainActivity_ViewBinding(final MainActivity target, View source) {
    this.target = target;

    View view;
    target.mToolbar = Utils.findRequiredViewAsType(source, R.id.toolbar, "field 'mToolbar'", Toolbar.class);
    target.mCurrentSearchView = Utils.findRequiredViewAsType(source, R.id.text_current_search, "field 'mCurrentSearchView'", TextView.class);
    target.mCurrentSortByView = Utils.findRequiredViewAsType(source, R.id.text_current_sort_by, "field 'mCurrentSortByView'", TextView.class);
    target.mRestaurantsRecycler = Utils.findRequiredViewAsType(source, R.id.recycler_restaurants, "field 'mRestaurantsRecycler'", RecyclerView.class);
    target.mEmptyView = Utils.findRequiredViewAsType(source, R.id.view_empty, "field 'mEmptyView'", ViewGroup.class);
    view = Utils.findRequiredView(source, R.id.filter_bar, "method 'onFilterClicked'");
    view2131296349 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onFilterClicked();
      }
    });
    view = Utils.findRequiredView(source, R.id.button_clear_filter, "method 'onClearFilterClicked'");
    view2131296297 = view;
    view.setOnClickListener(new DebouncingOnClickListener() {
      @Override
      public void doClick(View p0) {
        target.onClearFilterClicked();
      }
    });
  }

  @Override
  @CallSuper
  public void unbind() {
    MainActivity target = this.target;
    if (target == null) throw new IllegalStateException("Bindings already cleared.");
    this.target = null;

    target.mToolbar = null;
    target.mCurrentSearchView = null;
    target.mCurrentSortByView = null;
    target.mRestaurantsRecycler = null;
    target.mEmptyView = null;

    view2131296349.setOnClickListener(null);
    view2131296349 = null;
    view2131296297.setOnClickListener(null);
    view2131296297 = null;
  }
}
