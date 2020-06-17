package id.fathonyfath.quran.lite.views.common;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import id.fathonyfath.quran.lite.themes.BaseTheme;
import id.fathonyfath.quran.lite.utils.ThemeContext;
import id.fathonyfath.quran.lite.utils.UnitConverter;

public class HamburgerView extends View {

    private final Paint paint;

    private int workingHeight = 0;

    public HamburgerView(Context context) {
        super(context);

        initConfiguration();

        this.paint = new Paint();

        setPaintColor();
    }

    private void setPaintColor() {
        int colorToApply = Color.parseColor("#FF000000");

        BaseTheme theme = ThemeContext.saveUnwrapTheme(getContext());

        if (theme != null) {
            colorToApply = theme.contrastColor();
        }

        this.paint.setColor(colorToApply);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        updatePadding();

        this.workingHeight = h - getPaddingTop() - getPaddingBottom();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(getPaddingLeft(), getTopAt(1), getWidth() - getPaddingRight(), getTopAt(2), paint);
        canvas.drawRect(getPaddingLeft(), getTopAt(3), getWidth() - getPaddingRight(), getTopAt(4), paint);
        canvas.drawRect(getPaddingLeft(), getTopAt(5), getWidth() - getPaddingRight(), getTopAt(6), paint);
    }

    private void initConfiguration() {
        setLayoutParams(new ViewGroup.LayoutParams(
                (int) UnitConverter.fromDpToPx(getContext(), 48f),
                (int) UnitConverter.fromDpToPx(getContext(), 48f)
        ));

        updatePadding();

        TypedValue outValue = new TypedValue();
        getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
        setBackgroundResource(outValue.resourceId);
        setClickable(true);
    }

    private void updatePadding() {
        setPadding(
                (int) UnitConverter.fromDpToPx(getContext(), 12f),
                (int) UnitConverter.fromDpToPx(getContext(), 8f),
                (int) UnitConverter.fromDpToPx(getContext(), 12f),
                (int) UnitConverter.fromDpToPx(getContext(), 8f)
        );
    }

    private int getTopAt(Integer position) {
        return position * (this.workingHeight / 7) + getPaddingTop();
    }
}
