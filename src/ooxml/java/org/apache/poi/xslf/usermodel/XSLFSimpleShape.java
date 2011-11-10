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

import org.apache.poi.util.Beta;
import org.apache.poi.util.Units;
import org.apache.poi.xslf.model.PropertyFetcher;
import org.apache.poi.xslf.model.geom.Context;
import org.apache.poi.xslf.model.geom.CustomGeometry;
import org.apache.poi.xslf.model.geom.Guide;
import org.apache.poi.xslf.model.geom.IAdjustableShape;
import org.apache.poi.xslf.model.geom.Outline;
import org.apache.poi.xslf.model.geom.Path;
import org.apache.poi.xslf.model.geom.PresetGeometries;
import org.apache.poi.openxml4j.opc.PackageRelationship;
import org.apache.poi.openxml4j.opc.PackagePart;
import org.apache.poi.openxml4j.opc.TargetMode;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.POIXMLException;
import org.apache.xmlbeans.XmlObject;
import org.openxmlformats.schemas.drawingml.x2006.main.CTEffectStyleItem;
import org.openxmlformats.schemas.drawingml.x2006.main.CTGeomGuide;
import org.openxmlformats.schemas.drawingml.x2006.main.CTLineProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.CTNonVisualDrawingProps;
import org.openxmlformats.schemas.drawingml.x2006.main.CTOuterShadowEffect;
import org.openxmlformats.schemas.drawingml.x2006.main.CTPoint2D;
import org.openxmlformats.schemas.drawingml.x2006.main.CTPositiveSize2D;
import org.openxmlformats.schemas.drawingml.x2006.main.CTPresetGeometry2D;
import org.openxmlformats.schemas.drawingml.x2006.main.CTPresetLineDashProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.CTSRgbColor;
import org.openxmlformats.schemas.drawingml.x2006.main.CTShapeProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.CTShapeStyle;
import org.openxmlformats.schemas.drawingml.x2006.main.CTSolidColorFillProperties;
import org.openxmlformats.schemas.drawingml.x2006.main.CTStyleMatrix;
import org.openxmlformats.schemas.drawingml.x2006.main.CTTransform2D;
import org.openxmlformats.schemas.drawingml.x2006.main.STLineCap;
import org.openxmlformats.schemas.drawingml.x2006.main.STPresetLineDashVal;
import org.openxmlformats.schemas.drawingml.x2006.main.STShapeType;
import org.openxmlformats.schemas.drawingml.x2006.main.CTBlip;
import org.openxmlformats.schemas.presentationml.x2006.main.CTPlaceholder;
import org.openxmlformats.schemas.presentationml.x2006.main.STPlaceholderType;
import org.openxmlformats.schemas.presentationml.x2006.main.CTPicture;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Represents a single (non-group) shape in a .pptx slide show
 *
 * @author Yegor Kozlov
 */
@Beta
public abstract class XSLFSimpleShape extends XSLFShape {
    private static CTOuterShadowEffect NO_SHADOW = CTOuterShadowEffect.Factory.newInstance();

    private final XmlObject _shape;
    private final XSLFSheet _sheet;
    private CTShapeProperties _spPr;
    private CTShapeStyle _spStyle;
    private CTNonVisualDrawingProps _nvPr;
    private CTPlaceholder _ph;

    /* package */XSLFSimpleShape(XmlObject shape, XSLFSheet sheet) {
        _shape = shape;
        _sheet = sheet;
    }

    @Override
    public XmlObject getXmlObject() {
        return _shape;
    }

    /**
     *
     * @return the sheet this shape belongs to
     */
    public XSLFSheet getSheet() {
        return _sheet;
    }

    /**
     * TODO match STShapeType with
     * {@link org.apache.poi.sl.usermodel.ShapeTypes}
     */
    public int getShapeType() {
        CTPresetGeometry2D prst = getSpPr().getPrstGeom();
        STShapeType.Enum stEnum = prst == null ? null : prst.getPrst();
        return stEnum == null ? 0 : stEnum.intValue();
    }

    @Override
    public String getShapeName() {
        return getNvPr().getName();
    }

    @Override
    public int getShapeId() {
        return (int) getNvPr().getId();
    }

    protected CTNonVisualDrawingProps getNvPr() {
        if (_nvPr == null) {
            XmlObject[] rs = _shape
                    .selectPath("declare namespace p='http://schemas.openxmlformats.org/presentationml/2006/main' .//*/p:cNvPr");
            if (rs.length != 0) {
                _nvPr = (CTNonVisualDrawingProps) rs[0];
            }
        }
        return _nvPr;
    }

    protected CTShapeProperties getSpPr() {
        if (_spPr == null) {
            for (XmlObject obj : _shape.selectPath("*")) {
                if (obj instanceof CTShapeProperties) {
                    _spPr = (CTShapeProperties) obj;
                }
            }
        }
        if (_spPr == null) {
            throw new IllegalStateException("CTShapeProperties was not found.");
        }
        return _spPr;
    }

    protected CTShapeStyle getSpStyle() {
        if (_spStyle == null) {
            for (XmlObject obj : _shape.selectPath("*")) {
                if (obj instanceof CTShapeStyle) {
                    _spStyle = (CTShapeStyle) obj;
                }
            }
        }
        return _spStyle;
    }

    protected CTPlaceholder getCTPlaceholder() {
        if (_ph == null) {
            XmlObject[] obj = _shape.selectPath(
                    "declare namespace p='http://schemas.openxmlformats.org/presentationml/2006/main' .//*/p:nvPr/p:ph");
            if (obj.length == 1) {
                _ph = (CTPlaceholder) obj[0];
            }
        }
        return _ph;
    }

    private CTTransform2D getXfrm() {
        PropertyFetcher<CTTransform2D> fetcher = new PropertyFetcher<CTTransform2D>() {
            public boolean fetch(XSLFSimpleShape shape) {
                CTShapeProperties pr = shape.getSpPr();
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
        CTShapeProperties spPr = getSpPr();
        CTTransform2D xfrm = spPr.isSetXfrm() ? spPr.getXfrm() : spPr.addNewXfrm();
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
        CTShapeProperties spPr = getSpPr();
        CTTransform2D xfrm = spPr.isSetXfrm() ? spPr.getXfrm() : spPr.addNewXfrm();
        xfrm.setRot((int) (theta * 60000));
    }

    @Override
    public double getRotation() {
        CTTransform2D xfrm = getXfrm();
        return (double) xfrm.getRot() / 60000;
    }

    @Override
    public void setFlipHorizontal(boolean flip) {
        CTShapeProperties spPr = getSpPr();
        CTTransform2D xfrm = spPr.isSetXfrm() ? spPr.getXfrm() : spPr.addNewXfrm();
        xfrm.setFlipH(flip);
    }

    @Override
    public void setFlipVertical(boolean flip) {
        CTShapeProperties spPr = getSpPr();
        CTTransform2D xfrm = spPr.isSetXfrm() ? spPr.getXfrm() : spPr.addNewXfrm();
        xfrm.setFlipV(flip);
    }

    @Override
    public boolean getFlipHorizontal() {
        return getXfrm().getFlipH();
    }

    @Override
    public boolean getFlipVertical() {
        return getXfrm().getFlipV();
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
            CTStyleMatrix styleMatrix = _sheet.getTheme().getXmlObject().getThemeElements().getFmtScheme();
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
        RenderableShape rShape = new RenderableShape(this);
        Paint paint = rShape.getLinePaint(null);
        if (paint instanceof Color) {
            return (Color) paint;
        }
        return null;
    }

    /**
     *
     * @param width line width in points. <code>0</code> means no line
     */
    public void setLineWidth(double width) {
        CTShapeProperties spPr = getSpPr();
        if (width == 0.) {
            if (spPr.isSetLn())
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
            public boolean fetch(XSLFSimpleShape shape) {
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
            if (spPr.isSetLn())
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
            public boolean fetch(XSLFSimpleShape shape) {
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
            if (spPr.isSetLn())
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
            public boolean fetch(XSLFSimpleShape shape) {
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
        RenderableShape rShape = new RenderableShape(this);
        Paint paint = rShape.getFillPaint(null);
        if (paint instanceof Color) {
            return (Color) paint;
        }
        return null;
    }

    /**
     * @return shadow of this shape or null if shadow is disabled
     */
    public XSLFShadow getShadow() {
        PropertyFetcher<CTOuterShadowEffect> fetcher = new PropertyFetcher<CTOuterShadowEffect>() {
            public boolean fetch(XSLFSimpleShape shape) {
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
                    CTStyleMatrix styleMatrix = _sheet.getTheme().getXmlObject().getThemeElements().getFmtScheme();
                    CTEffectStyleItem ef = styleMatrix.getEffectStyleLst().getEffectStyleArray(idx - 1);
                    obj = ef.getEffectLst().getOuterShdw();
                }
            }
        }
        return (obj == null || obj == NO_SHADOW) ? null : new XSLFShadow(obj, this);
    }

    @Override
    public void draw(Graphics2D graphics) {
        RenderableShape rShape = new RenderableShape(this);
        rShape.render(graphics);
    }


    /**
     * Walk up the inheritance tree and fetch shape properties.
     *
     * The following order of inheritance is assumed:
     * <p>
     * slide <-- slideLayout <-- slideMaster <-- default styles in the slideMaster
     * </p>
     *
     * @param visitor the object that collects the desired property
     * @return true if the property was fetched
     */
    boolean fetchShapeProperty(PropertyFetcher visitor) {
        boolean ok = visitor.fetch(this);

        XSLFSimpleShape masterShape;
        XSLFSheet layout = getSheet().getMasterSheet();
        if (layout != null) {
            if (!ok) {
                // first try to fetch from the slide layout
                CTPlaceholder ph = getCTPlaceholder();
                if (ph != null) {
                    masterShape = layout.getPlaceholder(ph);
                    if (masterShape != null) {
                        ok = visitor.fetch(masterShape);
                    }
                }
            }

            // try slide master
            if (!ok) {
                int textType;
                CTPlaceholder ph = getCTPlaceholder();
                if (ph == null || !ph.isSetType()) textType = STPlaceholderType.INT_BODY;
                else {
                    switch (ph.getType().intValue()) {
                        case STPlaceholderType.INT_TITLE:
                        case STPlaceholderType.INT_CTR_TITLE:
                            textType = STPlaceholderType.INT_TITLE;
                            break;
                        case STPlaceholderType.INT_FTR:
                        case STPlaceholderType.INT_SLD_NUM:
                        case STPlaceholderType.INT_DT:
                            textType = ph.getType().intValue();
                            break;
                        default:
                            textType = STPlaceholderType.INT_BODY;
                            break;
                    }
                }
                XSLFSheet master = layout.getMasterSheet();
                if (master != null) {
                    masterShape = master.getPlaceholderByType(textType);
                    if (masterShape != null) {
                        ok = visitor.fetch(masterShape);
                    }
                }
            }
        }
        return ok;
    }

    /**
     *
     * @return definition of the shape geometry
     */
    CustomGeometry getGeometry(){
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
            geom = new CustomGeometry(spPr.getCustGeom());
        } else {
            geom = dict.get("rect");
        }
        return geom;
    }


    /**
     * @return any shape-specific geometry that is not included in presetShapeDefinitions.xml
     * (line decorations, etc)
     */
    List<Outline>  getCustomOutlines(){
        return Collections.emptyList();
    }

    /**
     * draw any content within this shape (image, text, etc.).
     *
     * @param graphics the graphics to draw into
     */
    public void drawContent(Graphics2D graphics){

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
}
