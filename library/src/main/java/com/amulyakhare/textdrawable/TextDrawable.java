package com.amulyakhare.textdrawable;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;
import android.graphics.drawable.shapes.RoundRectShape;

/**
 * @author amulya
 * @datetime 14 Oct 2014, 3:53 PM
 */
public class TextDrawable extends ShapeDrawable {

    /**
     * Text paint.
     */
    private final Paint textPaint;
    /**
     * Border paint.
     */
    private final Paint borderPaint;
    /**
     * The constant SHADE_FACTOR.
     */
    private static final float SHADE_FACTOR = 0.9f;
    /**
     * Text.
     */
    private final String text;
    /**
     * Color.
     */
    private final int color;
    /**
     * Shape.
     */
    private final RectShape shape;
    /**
     * Height.
     */
    private final int height;
    /**
     * Width.
     */
    private final int width;
    /**
     * Font size.
     */
    private final int fontSize;
    /**
     * Radius.
     */
    private final float radius;
    /**
     * Border thickness.
     */
    private final int borderThickness;
    /**
     * String color.
     */
    private String stringColor;

    /**
     * Instantiates a new Text drawable.
     *
     * @param builder
     *         the builder
     */
    private TextDrawable(Builder builder) {
        super(builder.shape);

        // shape properties
        shape = builder.shape;
        height = builder.height;
        width = builder.width;
        radius = builder.radius;

        // text and color
        text = builder.toUpperCase ? builder.text.toUpperCase() : builder.text;
        color = builder.color;
        stringColor = builder.stringColor;

        // text paint settings
        fontSize = builder.fontSize;
        textPaint = new Paint();
        textPaint.setColor(builder.textColor);
        textPaint.setAntiAlias(true);
        textPaint.setFakeBoldText(builder.isBold);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTypeface(builder.font);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setStrokeWidth(builder.borderThickness);

        // border paint settings
        borderThickness = builder.borderThickness;
        borderPaint = new Paint();
        borderPaint.setColor(getDarkerShade(color));
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setStrokeWidth(borderThickness);

        // drawable paint color
        Paint paint = getPaint();
        if (stringColor != null) {
            paint.setColor(Color.parseColor(stringColor));
        } else {
            paint.setColor(color);
        }
    }

    /**
     * Gets darker shade.
     *
     * @param color
     *         color
     * @return darker shade
     */
    private int getDarkerShade(int color) {
        return Color.rgb((int) (SHADE_FACTOR * Color.red(color)),
                (int) (SHADE_FACTOR * Color.green(color)),
                (int) (SHADE_FACTOR * Color.blue(color)));
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        Rect r = getBounds();


        // draw border
        if (borderThickness > 0) {
            drawBorder(canvas);
        }

        int count = canvas.save();
        canvas.translate(r.left, r.top);

        // draw text
        int width = this.width < 0 ? r.width() : this.width;
        int height = this.height < 0 ? r.height() : this.height;
        int fontSize = this.fontSize < 0 ? (Math.min(width, height) / 2) : this.fontSize;
        textPaint.setTextSize(fontSize);
        canvas.drawText(text, width / 2, height / 2 - ((textPaint.descent() + textPaint.ascent()) / 2), textPaint);

        canvas.restoreToCount(count);

    }

    private void drawBorder(Canvas canvas) {
        RectF rect = new RectF(getBounds());
        rect.inset(borderThickness / 2, borderThickness / 2);

        if (shape instanceof OvalShape) {
            canvas.drawOval(rect, borderPaint);
        } else if (shape instanceof RoundRectShape) {
            canvas.drawRoundRect(rect, radius, radius, borderPaint);
        } else {
            canvas.drawRect(rect, borderPaint);
        }
    }

    @Override
    public void setAlpha(int alpha) {
        textPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        textPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public int getIntrinsicWidth() {
        return width;
    }

    @Override
    public int getIntrinsicHeight() {
        return height;
    }

    public static IShapeBuilder builder() {
        return new Builder();
    }

    public static class Builder implements IConfigBuilder, IShapeBuilder, IBuilder {

        private String text;

        private int color;

        private int borderThickness;

        private int width;

        private int height;

        private Typeface font;

        private RectShape shape;

        public int textColor;

        private int fontSize;

        private boolean isBold;

        private boolean toUpperCase;

        public float radius;

        private String stringColor;

        private Builder() {
            text = "";
            color = Color.GRAY;
            textColor = Color.WHITE;
            borderThickness = 0;
            width = -1;
            height = -1;
            shape = new RectShape();
            font = Typeface.create("sans-serif-light", Typeface.NORMAL);
            fontSize = -1;
            isBold = false;
            toUpperCase = false;
        }


        public IConfigBuilder width(int width) {
            this.width = width;
            return this;
        }

        public IConfigBuilder height(int height) {
            this.height = height;
            return this;
        }

        public IConfigBuilder textColor(int color) {
            this.textColor = color;
            return this;
        }

        public IConfigBuilder withBorder(int thickness) {
            this.borderThickness = thickness;
            return this;
        }

        public IConfigBuilder useFont(Typeface font) {
            this.font = font;
            return this;
        }

        public IConfigBuilder fontSize(int size) {
            this.fontSize = size;
            return this;
        }

        public IConfigBuilder bold() {
            this.isBold = true;
            return this;
        }

        public IConfigBuilder toUpperCase() {
            this.toUpperCase = true;
            return this;
        }

        @Override
        public IConfigBuilder beginConfig() {
            return this;
        }

        @Override
        public IShapeBuilder endConfig() {
            return this;
        }

        @Override
        public IBuilder rect() {
            this.shape = new RectShape();
            return this;
        }

        @Override
        public IBuilder round() {
            this.shape = new OvalShape();
            return this;
        }

        @Override
        public IBuilder roundRect(int radius) {
            this.radius = radius;
            float[] radii = {radius, radius, radius, radius, radius, radius, radius, radius};
            this.shape = new RoundRectShape(radii, null, null);
            return this;
        }

        @Override
        public TextDrawable buildRect(String text, int color) {
            rect();
            return build(text, color);
        }

        @Override
        public TextDrawable buildRoundRect(String text, int color, int radius) {
            roundRect(radius);
            return build(text, color);
        }

        @Override
        public TextDrawable buildRound(String text, int color) {
            round();
            return build(text, color);
        }

        @Override
        public TextDrawable build(String text, int color) {
            this.color = color;
            this.text = text;
            return new TextDrawable(this);
        }

        @Override
        public TextDrawable build(String text, String color) {
            this.stringColor = color;
            this.text = text;
            return new TextDrawable(this);
        }
    }

    public interface IConfigBuilder {

        /**
         * Width i config builder.
         *
         * @param width
         *         width
         * @return the i config builder
         */
        public IConfigBuilder width(int width);

        /**
         * Height i config builder.
         *
         * @param height
         *         height
         * @return the i config builder
         */
        public IConfigBuilder height(int height);

        /**
         * Text color.
         *
         * @param color
         *         color
         * @return the i config builder
         */
        public IConfigBuilder textColor(int color);

        /**
         * With border.
         *
         * @param thickness
         *         thickness
         * @return the i config builder
         */
        public IConfigBuilder withBorder(int thickness);

        /**
         * Use font.
         *
         * @param font
         *         font
         * @return the i config builder
         */
        public IConfigBuilder useFont(Typeface font);

        /**
         * Font size.
         *
         * @param size
         *         size
         * @return the i config builder
         */
        public IConfigBuilder fontSize(int size);

        /**
         * Bold i config builder.
         *
         * @return the i config builder
         */
        public IConfigBuilder bold();

        /**
         * To upper case.
         *
         * @return the i config builder
         */
        public IConfigBuilder toUpperCase();

        /**
         * End config.
         *
         * @return the i shape builder
         */
        public IShapeBuilder endConfig();
    }

    public static interface IBuilder {

        /**
         * Build text drawable.
         *
         * @param text
         *         text
         * @param color
         *         color
         * @return the text drawable
         */
        public TextDrawable build(String text, int color);

        /**
         * Build text drawable.
         *
         * @param text
         *         text
         * @param color
         *         color
         * @return the text drawable
         */
        public TextDrawable build(String text, String color);
    }

    public static interface IShapeBuilder {

        /**
         * Begin config.
         *
         * @return the i config builder
         */
        public IConfigBuilder beginConfig();

        /**
         * Rect i builder.
         *
         * @return the i builder
         */
        public IBuilder rect();

        /**
         * Round i builder.
         *
         * @return the i builder
         */
        public IBuilder round();

        /**
         * Round rect.
         *
         * @param radius
         *         radius
         * @return the i builder
         */
        public IBuilder roundRect(int radius);

        /**
         * Build rect.
         *
         * @param text
         *         text
         * @param color
         *         color
         * @return the text drawable
         */
        public TextDrawable buildRect(String text, int color);

        /**
         * Build round rect.
         *
         * @param text
         *         text
         * @param color
         *         color
         * @param radius
         *         radius
         * @return the text drawable
         */
        public TextDrawable buildRoundRect(String text, int color, int radius);

        /**
         * Build round.
         *
         * @param text
         *         text
         * @param color
         *         color
         * @return the text drawable
         */
        public TextDrawable buildRound(String text, int color);
    }
}