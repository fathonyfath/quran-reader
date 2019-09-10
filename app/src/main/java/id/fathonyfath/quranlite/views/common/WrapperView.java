package id.fathonyfath.quranlite.views.common;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import id.fathonyfath.quranlite.Res;
import id.fathonyfath.quranlite.themes.BaseTheme;
import id.fathonyfath.quranlite.utils.ThemeContext;
import id.fathonyfath.quranlite.utils.UnitConverter;

public abstract class WrapperView extends RelativeLayout {

    private final ToolbarView toolbarView;
    private final ElevationView elevationView;
    private final FrameLayout contentView;

    public WrapperView(Context context) {
        super(context);

        this.toolbarView = new ToolbarView(context);
        this.elevationView = new ElevationView(context);
        this.contentView = new FrameLayout(context);

        initConfiguration();
        initView();
        applyStyleBasedOnTheme();
    }

    protected void setToolbarTitle(String title) {
        if (this.toolbarView != null) {
            this.toolbarView.setTitle(title);
        }
    }

    protected void setElevationAlpha(float alpha) {
        if (this.elevationView != null) {
            this.elevationView.setAlpha(alpha);
        }
    }

    @Override
    public void addView(View child) {
        this.contentView.addView(child);
    }

    private void initConfiguration() {
        setLayoutParams(new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        ));

        setBackgroundColor(Color.WHITE);
    }

    private void initView() {
        this.toolbarView.setId(Res.Id.toolbar);

        this.elevationView.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                (int) UnitConverter.fromDpToPx(getContext(), 8f)
        ));

        super.addView(this.toolbarView);
        super.addView(this.elevationView);
        super.addView(this.contentView);

        final RelativeLayout.LayoutParams toolbarParams = (LayoutParams) this.toolbarView.getLayoutParams();
        final RelativeLayout.LayoutParams overlayParams = (LayoutParams) this.elevationView.getLayoutParams();
        final RelativeLayout.LayoutParams containerParams = new LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        );

        toolbarParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        this.toolbarView.setLayoutParams(toolbarParams);

        overlayParams.addRule(RelativeLayout.BELOW, toolbarView.getId());
        this.elevationView.setLayoutParams(overlayParams);
        this.elevationView.bringToFront();

        containerParams.addRule(BELOW, this.toolbarView.getId());
        this.contentView.setLayoutParams(containerParams);
    }

    private void applyStyleBasedOnTheme() {
        BaseTheme theme = ThemeContext.saveUnwrapTheme(getContext());
        if (theme != null) {
            this.setBackgroundColor(theme.primary());
        }
    }
}
