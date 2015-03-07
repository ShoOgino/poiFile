/*
 *  ====================================================================
 *    Licensed to the Apache Software Foundation (ASF) under one or more
 *    contributor license agreements.  See the NOTICE file distributed with
 *    this work for additional information regarding copyright ownership.
 *    The ASF licenses this file to You under the Apache License, Version 2.0
 *    (the "License"); you may not use this file except in compliance with
 *    the License.  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 * ====================================================================
 */

package org.apache.poi.xslf.usermodel;

import java.awt.Color;
import java.awt.geom.Rectangle2D;

import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

import org.apache.poi.sl.draw.DrawPaint;
import org.apache.poi.sl.draw.geom.*;
import org.apache.poi.sl.usermodel.*;
import org.apache.poi.sl.usermodel.LineDecoration.DecorationShape;
import org.apache.poi.sl.usermodel.LineDecoration.DecorationSize;
import org.apache.poi.sl.usermodel.PaintStyle.SolidPaint;
import org.apache.poi.sl.usermodel.StrokeStyle.LineCap;
import org.apache.poi.sl.usermodel.StrokeStyle.LineDash;
import org.apache.poi.util.Beta;
import org.apache.poi.util.Units;
import org.apache.poi.xslf.model.PropertyFetcher;
import org.apache.xmlbeans.XmlObject;
import org.openxmlformats.schemas.drawingml.x2006.main.*;
import org.openxmlformats.schemas.presentationml.x2006.main.CTPlaceholder;

/**
 * Represents a single (non-group) shape in a .pptx slide show
 *
 * @author Yegor Kozlov
 */
@Beta
public abstract class XSLFSimpleShape extends XSLFShape implements SimpleShape {
    private static CTOuterShadowEffect NO_SHADOW = CTOuterShadowEffect.Factory.newInstance();

    /* package */XSLFSimpleShape(XmlObject shape, XSLFSheet sheet) {
        super(shape,sheet);
    }

    /**
     *
     * @param type
     */
    public void setShapeType(ShapeType type){
        STShapeType.Enum geom = STShapeType.Enum.forInt(type.ooxmlId);
        getSpPr().getPrstGeom().setPrst(geom);
    }

    public ShapeType getShapeType(){
        STShapeType.Enum geom = getSpPr().getPrstGeom().getPrst();
        return ShapeType.forId(geom.intValue(), true);
    }
    
    protected CTTransform2D getSafeXfrm() {
        CTTransform2D xfrm = getXfrm();
        return (xfrm == null ? getSpPr().addNewXfrm() : xfrm);
    }
    
    protected CTTransform2D getXfrm() {
        PropertyFetcher<CTTransform2D> fetcher = new PropertyFetcher<CTTransform2D>() {
            public boolean fetch(XSLFShape shape) {
                CTShapeProperties pr = getSpPr();
                if (pr.isSetXfrm()) {
                    setValue(pr.getXfrm());
                    return true;
                }
                return false;
            }
        };
        fetchShapeProperty(fetcher);
        return fetcher.getValue();
    }

    @Override
    public Rectangle2D getAnchor() {

        CTTransform2D xfrm = getXfrm();

        CTPoint2D off = xfrm.getOff();
        long x = off.getX();
        long y = off.getY();
        CTPositiveSize2D ext = xfrm.getExt();
        long cx = ext.getCx();
        long cy = ext.getCy();
        return new Rectangle2D.Double(
                Units.toPoints(x), Units.toPoints(y),
                Units.toPoints(cx), Units.toPoints(cy));
    }

    @Override
    public void setAnchor(Rectangle2D anchor) {
        CTTransform2D xfrm = getSafeXfrm();
        CTPoint2D off = xfrm.isSetOff() ? xfrm.getOff() : xfrm.addNewOff();
        long x = Units.toEMU(anchor.getX());
        long y = Units.toEMU(anchor.getY());
        off.setX(x);
        off.setY(y);
        CTPositiveSize2D ext = xfrm.isSetExt() ? xfrm.getExt() : xfrm
                .addNewExt();
        long cx = Units.toEMU(anchor.getWidth());
        long cy = Units.toEMU(anchor.getHeight());
        ext.setCx(cx);
        ext.setCy(cy);
    }
    
    @Override
    public void setRotation(double theta) {
        getSafeXfrm().setRot((int) (theta * 60000));
    }

    @Override
    public double getRotation() {
        CTTransform2D xfrm = getXfrm();
        return (xfrm == null || !xfrm.isSetRot()) ? 0 : (xfrm.getRot() / 60000.d);
    }

    @Override
    public void setFlipHorizontal(boolean flip) {
        getSafeXfrm().setFlipH(flip);
    }

    @Override
    public void setFlipVertical(boolean flip) {
        getSafeXfrm().setFlipV(flip);
    }

    @Override
    public boolean getFlipHorizontal() {
        CTTransform2D xfrm = getXfrm();
        return (xfrm == null || !xfrm.isSetFlipH()) ? false : getXfrm().getFlipH();
    }

    @Override
    public boolean getFlipVertical() {
        CTTransform2D xfrm = getXfrm();
        return (xfrm == null || !xfrm.isSetFlipV()) ? false : getXfrm().getFlipV();
    }

    
    /**
     * Get default line properties defined in the theme (if any).
     * Used internally to resolve shape properties.
     *
     * @return line propeties from the theme of null
     */
    CTLineProperties getDefaultLineProperties() {
        CTLineProperties ln = null;
        CTShapeStyle style = getSpStyle();
        if (style != null) {
            // 1-based index of a line style within the style matrix
            int idx = (int) style.getLnRef().getIdx();
            CTStyleMatrix styleMatrix = getSheet().getTheme().getXmlObject().getThemeElements().getFmtScheme();
            ln = styleMatrix.getLnStyleLst().getLnArray(idx - 1);
        }
        return ln;
    }

    /**
    * @param color  the color to paint the shape outline.
     * A <code>null</code> value turns off the shape outline.
     */
    public void setLineColor(Color color) {
        CTShapeProperties spPr = getSpPr();
        if (color == null) {
            if (spPr.isSetLn() && spPr.getLn().isSetSolidFill())
                spPr.getLn().unsetSolidFill();
        } else {
            CTLineProperties ln = spPr.isSetLn() ? spPr.getLn() : spPr
                    .addNewLn();

            CTSRgbColor rgb = CTSRgbColor.Factory.newInstance();
            rgb.setVal(new byte[]{(byte) color.getRed(),
                    (byte) color.getGreen(), (byte) color.getBlue()});

            CTSolidColorFillProperties fill = ln.isSetSolidFill() ? ln
                    .getSolidFill() : ln.addNewSolidFill();
            fill.setSrgbClr(rgb);
            if(fill.isSetHslClr()) fill.unsetHslClr();
            if(fill.isSetPrstClr()) fill.unsetPrstClr();
            if(fill.isSetSchemeClr()) fill.unsetSchemeClr();
            if(fill.isSetScrgbClr()) fill.unsetScrgbClr();
            if(fill.isSetSysClr()) fill.unsetSysClr();
        }
    }

    /**
     *
     * @return the color of the shape outline or <code>null</code>
     * if outline is turned off
     */
    public Color getLineColor() {
        PaintStyle ps = getLinePaint();
        if (ps == null || ps == TRANSPARENT_PAINT) return null;
        if (ps instanceof SolidPaint) {
            Color col = ((SolidPaint)ps).getSolidColor().getColor();
            return (col == DrawPaint.NO_PAINT) ? null : col;
        }
        return null;
    }

    protected PaintStyle getLinePaint() {
        PropertyFetcher<PaintStyle> fetcher = new PropertyFetcher<PaintStyle>() {
            public boolean fetch(XSLFShape shape) {
                CTLineProperties spPr = shape.getSpPr().getLn();
                if (spPr != null) {
                    if (spPr.isSetNoFill()) {
                        setValue(TRANSPARENT_PAINT); // use it as 'nofill' value
                        return true;
                    }
                    PaintStyle paint = getPaint(spPr, null);
                    if (paint != null) {
                        setValue(paint);
                        return true;
                    }
                }
                return false;

            }
        };
        fetchShapeProperty(fetcher);

        PaintStyle paint = fetcher.getValue();
        if (paint != null) return paint;
        
        // line color was not found, check if it is defined in the theme
        CTShapeStyle style = getSpStyle();
        if (style == null) return TRANSPARENT_PAINT;
        
        // get a reference to a line style within the style matrix.
        CTStyleMatrixReference lnRef = style.getLnRef();
        int idx = (int)lnRef.getIdx();
        CTSchemeColor phClr = lnRef.getSchemeClr();
        if(idx > 0){
            XSLFTheme theme = getSheet().getTheme();
            XmlObject lnProps = theme.getXmlObject().
                    getThemeElements().getFmtScheme().getLnStyleLst().selectPath("*")[idx - 1];
            paint = getPaint(lnProps, phClr);
        }

        return paint == null ? TRANSPARENT_PAINT : paint;
    }
    
    /**
     *
     * @param width line width in points. <code>0</code> means no line
     */
    public void setLineWidth(double width) {
        CTShapeProperties spPr = getSpPr();
        if (width == 0.) {
            if (spPr.isSetLn() && spPr.getLn().isSetW())
                spPr.getLn().unsetW();
        } else {
            CTLineProperties ln = spPr.isSetLn() ? spPr.getLn() : spPr
                    .addNewLn();
            ln.setW(Units.toEMU(width));
        }
    }

    /**
     *
     * @return line width in points. <code>0</code> means no line.
     */
    public double getLineWidth() {
        PropertyFetcher<Double> fetcher = new PropertyFetcher<Double>() {
            public boolean fetch(XSLFShape shape) {
                CTShapeProperties spPr = shape.getSpPr();
                CTLineProperties ln = spPr.getLn();
                if (ln != null) {
                    if (ln.isSetNoFill()) {
                        setValue(0.);
                        return true;
                    }

                    if (ln.isSetW()) {
                        setValue(Units.toPoints(ln.getW()));
                        return true;
                    }
                }
                return false;
            }
        };
        fetchShapeProperty(fetcher);

        double lineWidth = 0;
        if (fetcher.getValue() == null) {
            CTLineProperties defaultLn = getDefaultLineProperties();
            if (defaultLn != null) {
                if (defaultLn.isSetW()) lineWidth = Units.toPoints(defaultLn.getW());
            }
        } else {
            lineWidth = fetcher.getValue();
        }

        return lineWidth;
    }

    /**
     *
     * @param dash a preset line dashing scheme to stroke thr shape outline
     */
    public void setLineDash(LineDash dash) {
        CTShapeProperties spPr = getSpPr();
        if (dash == null) {
            if (spPr.isSetLn() &&  spPr.getLn().isSetPrstDash())
                spPr.getLn().unsetPrstDash();
        } else {
            CTPresetLineDashProperties val = CTPresetLineDashProperties.Factory
                    .newInstance();
            val.setVal(STPresetLineDashVal.Enum.forInt(dash.ordinal() + 1));
            CTLineProperties ln = spPr.isSetLn() ? spPr.getLn() : spPr
                    .addNewLn();
            ln.setPrstDash(val);
        }
    }

    /**
     * @return  a preset line dashing scheme to stroke thr shape outline
     */
    public LineDash getLineDash() {

        PropertyFetcher<LineDash> fetcher = new PropertyFetcher<LineDash>() {
            public boolean fetch(XSLFShape shape) {
                CTShapeProperties spPr = shape.getSpPr();
                CTLineProperties ln = spPr.getLn();
                if (ln != null) {
                    CTPresetLineDashProperties ctDash = ln.getPrstDash();
                    if (ctDash != null) {
                        setValue(LineDash.values()[ctDash.getVal().intValue() - 1]);
                        return true;
                    }
                }
                return false;
            }
        };
        fetchShapeProperty(fetcher);

        LineDash dash = fetcher.getValue();
        if (dash == null) {
            CTLineProperties defaultLn = getDefaultLineProperties();
            if (defaultLn != null) {
                CTPresetLineDashProperties ctDash = defaultLn.getPrstDash();
                if (ctDash != null) {
                    dash = LineDash.values()[ctDash.getVal().intValue() - 1];
                }
            }
        }
        return dash;
    }

    /**
     *
     * @param cap the line end cap style
     */
    public void setLineCap(LineCap cap) {
        CTShapeProperties spPr = getSpPr();
        if (cap == null) {
            if (spPr.isSetLn() && spPr.getLn().isSetCap())
                spPr.getLn().unsetCap();
        } else {
            CTLineProperties ln = spPr.isSetLn() ? spPr.getLn() : spPr
                    .addNewLn();
            ln.setCap(STLineCap.Enum.forInt(cap.ordinal() + 1));
        }
    }

    /**
     *
     * @return the line end cap style
     */
    public LineCap getLineCap() {
        PropertyFetcher<LineCap> fetcher = new PropertyFetcher<LineCap>() {
            public boolean fetch(XSLFShape shape) {
                CTShapeProperties spPr = shape.getSpPr();
                CTLineProperties ln = spPr.getLn();
                if (ln != null) {
                    STLineCap.Enum stCap = ln.getCap();
                    if (stCap != null) {
                        setValue(LineCap.values()[stCap.intValue() - 1]);
                        return true;
                    }
                }
                return false;
            }
        };
        fetchShapeProperty(fetcher);

        LineCap cap = fetcher.getValue();
        if (cap == null) {
            CTLineProperties defaultLn = getDefaultLineProperties();
            if (defaultLn != null) {
                STLineCap.Enum stCap = defaultLn.getCap();
                if (stCap != null) {
                    cap = LineCap.values()[stCap.intValue() - 1];
                }
            }
        }
        return cap;
    }

    /**
     * Specifies a solid color fill. The shape is filled entirely with the
     * specified color.
     *
     * @param color the solid color fill. The value of <code>null</code> unsets
     *              the solidFIll attribute from the underlying xml
     */
    public void setFillColor(Color color) {
        CTShapeProperties spPr = getSpPr();
        if (color == null) {
            if (spPr.isSetSolidFill()) spPr.unsetSolidFill();

            if (!spPr.isSetNoFill()) spPr.addNewNoFill();
        } else {
            if (spPr.isSetNoFill()) spPr.unsetNoFill();

            CTSolidColorFillProperties fill = spPr.isSetSolidFill() ? spPr
                    .getSolidFill() : spPr.addNewSolidFill();

            CTSRgbColor rgb = CTSRgbColor.Factory.newInstance();
            rgb.setVal(new byte[]{(byte) color.getRed(),
                    (byte) color.getGreen(), (byte) color.getBlue()});

            fill.setSrgbClr(rgb);
            if(fill.isSetHslClr()) fill.unsetHslClr();
            if(fill.isSetPrstClr()) fill.unsetPrstClr();
            if(fill.isSetSchemeClr()) fill.unsetSchemeClr();
            if(fill.isSetScrgbClr()) fill.unsetScrgbClr();
            if(fill.isSetSysClr()) fill.unsetSysClr();
        }
    }

    /**
     * @return solid fill color of null if not set or fill color
     * is not solid (pattern or gradient)
     */
    public Color getFillColor() {
        PaintStyle ps = getFillPaint();
        if (ps == null || ps == TRANSPARENT_PAINT) return null;
        if (ps instanceof SolidPaint) {
            Color col = ((SolidPaint)ps).getSolidColor().getColor();
            return (col == DrawPaint.NO_PAINT) ? null : col;
        }
        return null;
    }

    /**
     * @return shadow of this shape or null if shadow is disabled
     */
    public XSLFShadow getShadow() {
        PropertyFetcher<CTOuterShadowEffect> fetcher = new PropertyFetcher<CTOuterShadowEffect>() {
            public boolean fetch(XSLFShape shape) {
                CTShapeProperties spPr = shape.getSpPr();
                if (spPr.isSetEffectLst()) {
                    CTOuterShadowEffect obj = spPr.getEffectLst().getOuterShdw();
                    setValue(obj == null ? NO_SHADOW : obj);
                    return true;
                }
                return false;
            }
        };
        fetchShapeProperty(fetcher);

        CTOuterShadowEffect obj = fetcher.getValue();
        if (obj == null) {
            // fill color was not found, check if it is defined in the theme
            CTShapeStyle style = getSpStyle();
            if (style != null) {
                // 1-based index of a shadow style within the style matrix
                int idx = (int) style.getEffectRef().getIdx();
                if(idx != 0) {
                    CTStyleMatrix styleMatrix = getSheet().getTheme().getXmlObject().getThemeElements().getFmtScheme();
                    CTEffectStyleItem ef = styleMatrix.getEffectStyleLst().getEffectStyleArray(idx - 1);
                    obj = ef.getEffectLst().getOuterShdw();
                }
            }
        }
        return (obj == null || obj == NO_SHADOW) ? null : new XSLFShadow(obj, this);
    }

    /**
     *
     * @return definition of the shape geometry
     */
    public CustomGeometry getGeometry(){
        CTShapeProperties spPr = getSpPr();
        CustomGeometry geom;
        PresetGeometries dict = PresetGeometries.getInstance();
        if(spPr.isSetPrstGeom()){
            String name = spPr.getPrstGeom().getPrst().toString();
            geom = dict.get(name);
            if(geom == null) {
                throw new IllegalStateException("Unknown shape geometry: " + name);
            }
        } else if (spPr.isSetCustGeom()){
            XMLStreamReader staxReader = spPr.getCustGeom().newXMLStreamReader();
            geom = PresetGeometries.convertCustomGeometry(staxReader);
            try { staxReader.close(); }
            catch (XMLStreamException e) {}
        } else {
            geom = dict.get("rect");
        }
        return geom;
    }
    
    @Override
    void copy(XSLFShape sh){
        super.copy(sh);

        XSLFSimpleShape s = (XSLFSimpleShape)sh;

        Color srsSolidFill = s.getFillColor();
        Color tgtSoliFill = getFillColor();
        if(srsSolidFill != null && !srsSolidFill.equals(tgtSoliFill)){
            setFillColor(srsSolidFill);
        }

        if(getSpPr().isSetBlipFill()){
            CTBlip blip = getSpPr().getBlipFill().getBlip();
            String blipId = blip.getEmbed();

            String relId = getSheet().importBlip(blipId, s.getSheet().getPackagePart());
            blip.setEmbed(relId);
        }
        
        Color srcLineColor = s.getLineColor();
        Color tgtLineColor = getLineColor();
        if(srcLineColor != null && !srcLineColor.equals(tgtLineColor)) {
            setLineColor(srcLineColor);
        }

        double srcLineWidth = s.getLineWidth();
        double tgtLineWidth = getLineWidth();
        if(srcLineWidth != tgtLineWidth) {
            setLineWidth(srcLineWidth);
        }

        LineDash srcLineDash = s.getLineDash();
        LineDash tgtLineDash = getLineDash();
        if(srcLineDash != null && srcLineDash != tgtLineDash) {
            setLineDash(srcLineDash);
        }

        LineCap srcLineCap = s.getLineCap();
        LineCap tgtLineCap = getLineCap();
        if(srcLineCap != null && srcLineCap != tgtLineCap) {
            setLineCap(srcLineCap);
        }

    }

    /**
     * Specifies the line end decoration, such as a triangle or arrowhead.
     */
    public void setLineHeadDecoration(DecorationShape style) {
        CTLineProperties ln = getSpPr().getLn();
        CTLineEndProperties lnEnd = ln.isSetHeadEnd() ? ln.getHeadEnd() : ln.addNewHeadEnd();
        if (style == null) {
            if (lnEnd.isSetType()) lnEnd.unsetType();
        } else {
            lnEnd.setType(STLineEndType.Enum.forInt(style.ordinal() + 1));
        }
    }

    public DecorationShape getLineHeadDecoration() {
        CTLineProperties ln = getSpPr().getLn();
        if (ln == null || !ln.isSetHeadEnd()) return DecorationShape.NONE;

        STLineEndType.Enum end = ln.getHeadEnd().getType();
        return end == null ? DecorationShape.NONE : DecorationShape.values()[end.intValue() - 1];
    }

    /**
     * specifies decorations which can be added to the head of a line.
     */
    public void setLineHeadWidth(DecorationSize style) {
        CTLineProperties ln = getSpPr().getLn();
        CTLineEndProperties lnEnd = ln.isSetHeadEnd() ? ln.getHeadEnd() : ln.addNewHeadEnd();
        if (style == null) {
            if (lnEnd.isSetW()) lnEnd.unsetW();
        } else {
            lnEnd.setW(STLineEndWidth.Enum.forInt(style.ordinal() + 1));
        }
    }

    public DecorationSize getLineHeadWidth() {
        CTLineProperties ln = getSpPr().getLn();
        if (ln == null || !ln.isSetHeadEnd()) return DecorationSize.MEDIUM;

        STLineEndWidth.Enum w = ln.getHeadEnd().getW();
        return w == null ? DecorationSize.MEDIUM : DecorationSize.values()[w.intValue() - 1];
    }

    /**
     * Specifies the line end width in relation to the line width.
     */
    public void setLineHeadLength(DecorationSize style) {
        CTLineProperties ln = getSpPr().getLn();
        CTLineEndProperties lnEnd = ln.isSetHeadEnd() ? ln.getHeadEnd() : ln.addNewHeadEnd();

        if (style == null) {
            if (lnEnd.isSetLen()) lnEnd.unsetLen();
        } else {
            lnEnd.setLen(STLineEndLength.Enum.forInt(style.ordinal() + 1));
        }
    }

    public DecorationSize getLineHeadLength() {
        CTLineProperties ln = getSpPr().getLn();
        if (ln == null || !ln.isSetHeadEnd()) return DecorationSize.MEDIUM;

        STLineEndLength.Enum len = ln.getHeadEnd().getLen();
        return len == null ? DecorationSize.MEDIUM : DecorationSize.values()[len.intValue() - 1];
    }

    /**
     * Specifies the line end decoration, such as a triangle or arrowhead.
     */
    public void setLineTailDecoration(DecorationShape style) {
        CTLineProperties ln = getSpPr().getLn();
        CTLineEndProperties lnEnd = ln.isSetTailEnd() ? ln.getTailEnd() : ln.addNewTailEnd();
        if (style == null) {
            if (lnEnd.isSetType()) lnEnd.unsetType();
        } else {
            lnEnd.setType(STLineEndType.Enum.forInt(style.ordinal() + 1));
        }
    }

    public DecorationShape getLineTailDecoration() {
        CTLineProperties ln = getSpPr().getLn();
        if (ln == null || !ln.isSetTailEnd()) return DecorationShape.NONE;

        STLineEndType.Enum end = ln.getTailEnd().getType();
        return end == null ? DecorationShape.NONE : DecorationShape.values()[end.intValue() - 1];
    }

    /**
     * specifies decorations which can be added to the tail of a line.
     */
    public void setLineTailWidth(DecorationSize style) {
        CTLineProperties ln = getSpPr().getLn();
        CTLineEndProperties lnEnd = ln.isSetTailEnd() ? ln.getTailEnd() : ln.addNewTailEnd();
        if (style == null) {
            if (lnEnd.isSetW()) lnEnd.unsetW();
        } else {
            lnEnd.setW(STLineEndWidth.Enum.forInt(style.ordinal() + 1));
        }
    }

    public DecorationSize getLineTailWidth() {
        CTLineProperties ln = getSpPr().getLn();
        if (ln == null || !ln.isSetTailEnd()) return DecorationSize.MEDIUM;

        STLineEndWidth.Enum w = ln.getTailEnd().getW();
        return w == null ? DecorationSize.MEDIUM : DecorationSize.values()[w.intValue() - 1];
    }

    /**
     * Specifies the line end width in relation to the line width.
     */
    public void setLineTailLength(DecorationSize style) {
        CTLineProperties ln = getSpPr().getLn();
        CTLineEndProperties lnEnd = ln.isSetTailEnd() ? ln.getTailEnd() : ln.addNewTailEnd();

        if (style == null) {
            if (lnEnd.isSetLen()) lnEnd.unsetLen();
        } else {
            lnEnd.setLen(STLineEndLength.Enum.forInt(style.ordinal() + 1));
        }
    }

    public DecorationSize getLineTailLength() {
        CTLineProperties ln = getSpPr().getLn();
        if (ln == null || !ln.isSetTailEnd()) return DecorationSize.MEDIUM;

        STLineEndLength.Enum len = ln.getTailEnd().getLen();
        return len == null ? DecorationSize.MEDIUM : DecorationSize.values()[len.intValue() - 1];
    }

    public boolean isPlaceholder() {
        CTPlaceholder ph = getCTPlaceholder();
        return ph != null;
    }

    @SuppressWarnings("deprecation")
    public Guide getAdjustValue(String name) {
        CTPresetGeometry2D prst = getSpPr().getPrstGeom();
        if (prst.isSetAvLst()) {
            for (CTGeomGuide g : prst.getAvLst().getGdArray()) {
                if (g.getName().equals(name)) {
                    return new Guide(g.getName(), g.getFmla());
                }
            }
        }

        return null;
    }

    public LineDecoration getLineDecoration() {
        return new LineDecoration() {
            public DecorationShape getHeadShape() {
                return getLineHeadDecoration();
            }

            public DecorationSize getHeadWidth() {
                return getLineHeadWidth();
            }

            public DecorationSize getHeadLength() {
                return getLineHeadLength();
            }

            public DecorationShape getTailShape() {
                return getLineTailDecoration();
            }

            public DecorationSize getTailWidth() {
                return getLineTailWidth();
            }

            public DecorationSize getTailLength() {
                return getLineTailLength();
            }
        };
    }

    /**
     * fetch shape fill as a java.awt.Paint
     *
     * @return either Color or GradientPaint or TexturePaint or null
     */
    public FillStyle getFillStyle() {
        return new FillStyle() {
            public PaintStyle getPaint() {
                return XSLFSimpleShape.this.getFillPaint();
            }
        };
    }

    public StrokeStyle getStrokeStyle() {
        return new StrokeStyle() {
            public PaintStyle getPaint() {
                return XSLFSimpleShape.this.getLinePaint();
            }

            public LineCap getLineCap() {
                return XSLFSimpleShape.this.getLineCap();
            }

            public LineDash getLineDash() {
                return XSLFSimpleShape.this.getLineDash();
            }

            public double getLineWidth() {
                return XSLFSimpleShape.this.getLineWidth();
            }
            
        };
    }
}
