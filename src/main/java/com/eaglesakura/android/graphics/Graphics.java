package com.eaglesakura.android.graphics;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.graphics.RectF;

import java.util.ArrayList;
import java.util.List;

/**
 * Canvas描画のサポート
 */
public class Graphics {
    Canvas canvas = null;
    Paint paint = new Paint();

    /**
     * 幅
     */
    int width = 0;

    /**
     * 高さ
     */
    int height = 0;


    /**
     * Canvasを指定して初期化
     */
    public Graphics(Canvas canvas) {
        setFontSize(24);
        clearMatrix();
        setCanvas(canvas);
    }

    /**
     * 描画用のキャンバスを指定する。
     */
    public void setCanvas(Canvas canvas) {
        this.canvas = canvas;
        if (canvas != null) {
            setWidth(canvas.getWidth());
            setHeight(canvas.getHeight());
        }
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * 描画時のスケーリングを指定する。
     */
    public void setDrawScalling(float x, float y) {
        Matrix m = new Matrix();
        m.setScale(x, y);
        canvas.setMatrix(m);
    }

    /**
     * 背景を特定色でクリアする。
     */
    public void clearRGBA(int r, int g, int b, int a) {
        canvas.drawColor(toColorARGB(a, r, g, b), Mode.CLEAR);
    }

    /**
     * 幅を取得する。
     */
    public int getWidth() {
        return width;
    }

    /**
     * 高さを取得する。
     */
    public int getHeight() {
        return height;
    }

    /**
     * ロックされたキャンバスを取得する。
     */
    public Canvas getCanvas() {
        return canvas;
    }

    /**
     * ペイントクラスを取得する。
     */
    public Paint getPaint() {
        return paint;
    }

    /**
     * 描画色を指定する。
     */
    public void setColorRGBA(int r, int g, int b, int a) {
        paint.setColor(toColorARGB(a, r, g, b));
    }

    /**
     * 描画色を直接指定する
     *
     * @param argb ARGBマッピングされた色情報
     */
    public void setColorARGB(int argb) {
        paint.setColor(argb);
    }

    /**
     * アンチエイリアスの有効・無効の指定を行う。<BR>
     * デフォルトは無効である。
     */
    public void setAntiAlias(boolean set) {
        paint.setAntiAlias(set);
        paint.setFilterBitmap(true);
    }

    /**
     * 描画する太さを指定する。
     */
    public void setStrokeSize(int size) {
        paint.setStrokeWidth(size);
    }

    /**
     * イメージの描画を行う。
     */
    public void drawBitmap(Bitmap bmp, Rect src, Rect dst) {
        canvas.drawBitmap(bmp, src, dst, paint);
    }

    /**
     * イメージ描画を行う。
     */
    public void drawBitmap(Bitmap bmp, int x, int y) {
        canvas.drawBitmap(bmp, (float) x, (float) y, paint);
    }

    /**
     * イメージ描画を行う。
     */
    public void drawBitmap(Bitmap bmp, int x, int y, int w, int h) {
        srcRect.set(0, 0, bmp.getWidth(), bmp.getHeight());
        dstRect.set(x, y, x + w, y + h);
        drawBitmap(bmp, srcRect, dstRect);
    }

    /**
     * @param x
     * @param y
     * @param r
     */
    public void drawCircle(int x, int y, float r) {
        paint.setStyle(Style.STROKE);
        canvas.drawCircle((float) x, (float) y, r, paint);
    }

    /**
     * @param x
     * @param y
     * @param r
     */
    public void fillCircle(int x, int y, float r) {
        paint.setStyle(Style.FILL);
        canvas.drawCircle((float) x, (float) y, r, paint);
    }

    /**
     * 四角形の塗りつぶしを行う。
     */
    public void fillRect(int x, int y, int w, int h) {
        paint.setStyle(Style.FILL);
        canvas.drawRect((float) x, (float) y, (float) (x + w), (float) (y + h), paint);
    }

    /**
     * 丸角四角形の塗りつぶしを行う
     */
    public void fillRoundRect(int x, int y, int w, int h, float radius) {
        paint.setStyle(Style.FILL);
        dstRectF.set(x, y, (x + w), (y + h));
        canvas.drawRoundRect(dstRectF, radius, radius, paint);
    }

    /**
     * 丸角四角形の塗りつぶしを行う
     */
    public void drawRoundRect(int x, int y, int w, int h, float radius) {
        paint.setStyle(Style.STROKE);
        dstRectF.set(x, y, (x + w), (y + h));
        canvas.drawRoundRect(dstRectF, radius, radius, paint);
    }

    /**
     * 四角形の外枠を描画する。
     */
    public void drawRect(int x, int y, int w, int h) {
        paint.setStyle(Style.STROKE);
        canvas.drawRect((float) x, (float) y, (float) (x + w), (float) (y + h), paint);
    }

    /**
     * テキストを左揃えにする。
     */
    public static final int STRING_LEFT = 0;

    /**
     * テキストを上揃えにする。
     */
    public static final int STRING_TOP = 0;

    /**
     * テキストを下揃えにする。
     */
    public static final int STRING_BOTTOM = 1 << 0;
    /**
     * テキストを右揃えにする。
     */
    public static final int STRING_RIGHT = 1 << 1;

    /**
     * 中央揃えにする。
     */
    public static final int STRING_CENTER_X = 1 << 2;

    /**
     * 中央揃えにする。
     */
    public static final int STRING_CENTER_Y = 1 << 3;

    /**
     * 文字列を描画する。
     */
    public void drawString(String str, int x, int y, int start, int end, int flags) {
        paint.setStyle(Style.FILL);

        if (start < 0 || end < 0) {
            start = 0;
            end = str.length();
        }

        Rect area = new Rect();
        paint.getTextBounds(str, start, end, area);
        int width = area.width();
        int height = area.height();

        //! left / topに位置を合わせる
        x -= area.left;
        y -= area.top;

        if ((flags & STRING_CENTER_X) != 0) {
            x -= (width >> 1);
        } else if ((flags & STRING_RIGHT) != 0) {
            x -= width;
        }

        if ((flags & STRING_CENTER_Y) != 0) {
            y -= (height >> 1);
        } else if ((flags & STRING_BOTTOM) != 0) {
            y -= height;
        }

        canvas.drawText(str, start, end, (float) x, (float) y, paint);
    }

    /**
     * 文字列の高さを取得する。
     */
    public int getStringHeight(String str) {
        Rect r = srcRect;
        paint.getTextBounds(str, 0, str.length(), r);
        return r.height();
    }

    /**
     * 文字列の幅を取得する。
     */
    public int getStringWidth(String str, int start, int end) {
        Rect r = srcRect;
        paint.getTextBounds(str, start, end, r);
        return r.width();
    }

    /**
     * 文字列の幅を取得する。
     */
    public int getStringWidth(String str) {
        Rect r = srcRect;
        paint.getTextBounds(str, 0, str.length(), r);
        return r.width();
    }

    /**
     * 線を描画する。
     */
    public void drawLine(int startX, int startY, int endX, int endY) {
        paint.setStyle(Style.STROKE);
        canvas.drawLine((float) startX, (float) startY, (float) endX, (float) endY, paint);
    }

    /**
     * 描画テキストのサイズを指定する。
     */
    public void setFontSize(int size) {
        paint.setTextSize((float) size);
    }

    /**
     * ARGB色に変換する。
     */
    public static int toColorARGB(int a, int r, int g, int b) {
        return Color.argb(a, r, g, b);
    }

    /**
     * 描画行列スタック。
     */
    List<Matrix> matrixStack = new ArrayList<Matrix>();

    /**
     * 上に積まれた描画行列を全て消去する。
     */
    public void clearMatrix() {
        while (matrixStack.size() > 1) {
            matrixStack.remove(0);
        }
        if (matrixStack.size() == 0) {
            matrixStack.add(new Matrix());
        }
        if (canvas != null) {
            canvas.setMatrix(matrixStack.get(0));
        }
    }

    /**
     * 現在の描画行列×matrixをCanvasに指定する。
     */
    public void pushMatrix(Matrix matrix) {
        Matrix tempMatrix = new Matrix(matrixStack.get(0));
        tempMatrix.postConcat(matrix);
        matrixStack.add(0, tempMatrix);
        canvas.setMatrix(tempMatrix);
    }

    /**
     * 指定した行列を最上位に設定する。
     */
    public void loadMatrix(Matrix matrix) {
        matrixStack.remove(0);
        matrixStack.add(new Matrix(matrix));
        canvas.setMatrix(matrixStack.get(0));
    }

    /**
     * 最上位の行列を取り除き、下の行列をloadする。
     */
    public void popMatrix() {
        matrixStack.remove(0);
        canvas.setMatrix(matrixStack.get(0));
    }

    /**
     * テンポラリ領域。
     */
    private Rect srcRect = new Rect();

    private Rect dstRect = new Rect();

    private RectF dstRectF = new RectF();

    /**
     * 正方形画像へ変形する。
     * <br>
     * 強制的に変形を行うため、srcは破棄して問題ない。
     *
     * @param src  元画像
     * @param size 出力サイズ
     */
    public static Bitmap toSquareImage(Bitmap src, int size) {
        if (src.getWidth() == src.getHeight()) {
            if (src.getWidth() == size) {
                // 既に同じサイズであるため、コピーのみでOK
                return Bitmap.createBitmap(src);
            } else {
                // 既に正方形である場合、縮小のみで行える
                return Bitmap.createScaledBitmap(src, size, size, true);
            }
        } else {
            double scale;
            // 短編に合わせてスケーリングし、長辺ははみ出す
            if (src.getWidth() < src.getHeight()) {
                scale = (double) size / (double) src.getWidth();
            } else {
                scale = (double) size / (double) src.getHeight();
            }

            int srcWidth = (int) (src.getWidth() * scale + 0.9);
            int srcHeight = (int) (src.getHeight() * scale + 0.9);

            Bitmap dst = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(dst);
            Graphics graphics = new Graphics(canvas);
            graphics.setAntiAlias(true);
            graphics.drawBitmap(src, (size - srcWidth) / 2, (size - srcHeight) / 2, srcWidth, srcHeight);

            return dst;
        }
    }
}
