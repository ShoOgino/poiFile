package org.apache.poi.sl.draw;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

import org.apache.poi.sl.usermodel.PlaceableShape;
import org.apache.poi.sl.usermodel.Shape;


public class DrawShape<T extends Shape> implements Drawable {

    protected final T shape;
    
    public DrawShape(T shape) {
        this.shape = shape;
    }
    
    /**
     * Apply 2-D transforms before drawing this shape. This includes rotation and flipping.
     *
     * @param graphics the graphics whos transform matrix will be modified
     */
    public void applyTransform(Graphics2D graphics) {
        if (!(shape instanceof PlaceableShape)) return;
        
        PlaceableShape ps = (PlaceableShape)shape;
        AffineTransform tx = (AffineTransform)graphics.getRenderingHint(Drawable.GROUP_TRANSFORM);
        final Rectangle2D anchor = (tx != null)
            ? tx.createTransformedShape(ps.getAnchor()).getBounds2D()
            : ps.getAnchor();

        // rotation
        double rotation = ps.getRotation();
        if (rotation != 0.) {
            // PowerPoint rotates shapes relative to the geometric center
            double centerX = anchor.getCenterX();
            double centerY = anchor.getCenterY();

            // normalize rotation
            rotation %= 360.;
            if (rotation < 0) rotation += 360.;
            int quadrant = (((int)rotation+45)/90)%4;
            double scaleX = 1.0, scaleY = 1.0;

            // scale to bounding box (bug #53176)
            if (quadrant == 1 || quadrant == 3) {
                // In quadrant 1 and 3, which is basically a shape in a more or less portrait orientation 
                // (45-135 degrees and 225-315 degrees), we need to first rotate the shape by a multiple 
                // of 90 degrees and then resize the bounding box to its original bbox. After that we can 
                // rotate the shape to the exact rotation amount.
                // It's strange that you'll need to rotate the shape back and forth again, but you can
                // think of it, as if you paint the shape on a canvas. First you rotate the canvas, which might
                // be already (differently) scaled, so you can paint the shape in its default orientation
                // and later on, turn it around again to compare it with its original size ...

                // graphics coordinate space
                AffineTransform txg = new AffineTransform();
                txg.translate(centerX, centerY);
                txg.rotate(Math.toRadians(90));
                txg.translate(-centerX, -centerY);

                boolean oldVariant = true;
                Rectangle2D anchor2;
                
                if (oldVariant) {
                    // shape coordinate space
                    AffineTransform txs = new AffineTransform(tx);
                    txs.translate(centerX, centerY);
                    txs.rotate(Math.toRadians(90));
                    txs.translate(-centerX, -centerY);
                    txg.concatenate(txs);
                    anchor2 = txg.createTransformedShape(ps.getAnchor()).getBounds2D();
                } else {
                    anchor2 = txg.createTransformedShape(anchor).getBounds2D();
                }

                scaleX = anchor.getWidth() == 0. ? 1.0 : anchor.getWidth() / anchor2.getWidth();
                scaleY = anchor.getHeight() == 0. ? 1.0 : anchor.getHeight() / anchor2.getHeight();
                
                graphics.translate(centerX, centerY);
                graphics.rotate(Math.toRadians(rotation-quadrant*90.));
                graphics.scale(scaleX, scaleY);
                graphics.rotate(Math.toRadians(quadrant*90));
                graphics.translate(-centerX, -centerY);
            } else {
                graphics.translate(centerX, centerY);
                graphics.rotate(Math.toRadians(rotation));
                graphics.scale(scaleX, scaleY);
                graphics.translate(-centerX, -centerY);
            }
            // transformation is applied reversed ...
        }

        //flip horizontal
        if (ps.getFlipHorizontal()) {
            graphics.translate(anchor.getX() + anchor.getWidth(), anchor.getY());
            graphics.scale(-1, 1);
            graphics.translate(-anchor.getX(), -anchor.getY());
        }

        //flip vertical
        if (ps.getFlipVertical()) {
            graphics.translate(anchor.getX(), anchor.getY() + anchor.getHeight());
            graphics.scale(1, -1);
            graphics.translate(-anchor.getX(), -anchor.getY());
        }
    }


    public void draw(Graphics2D graphics) {
    }

    public void drawContent(Graphics2D context) {
    }

    public static Rectangle2D getAnchor(Graphics2D graphics, PlaceableShape shape) {
        return getAnchor(graphics, shape.getAnchor());
    }
    
    public static Rectangle2D getAnchor(Graphics2D graphics, Rectangle2D anchor) {
        if(graphics == null)  {
            return anchor;
        }

        AffineTransform tx = (AffineTransform)graphics.getRenderingHint(Drawable.GROUP_TRANSFORM);
        if(tx != null) {
            anchor = tx.createTransformedShape(anchor).getBounds2D();
        }
        return anchor;
    }    
}
