/* ====================================================================
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
==================================================================== */

package org.apache.poi.hwpf.model.types;



import org.apache.poi.util.BitField;
import org.apache.poi.util.LittleEndian;
import org.apache.poi.util.StringUtil;
import org.apache.poi.util.HexDump;
import org.apache.poi.hdf.model.hdftypes.HDFType;
import org.apache.poi.hwpf.usermodel.*;

/**
 * Paragraph Properties.
 * NOTE: This source is automatically generated please do not modify this file.  Either subclass or
 *       remove the record in src/records/definitions.

 * @author S. Ryan Ackley
 */
public abstract class PAPAbstractType
    implements HDFType
{

    protected  int field_1_istd;
    protected  byte field_2_fSideBySide;
    protected  byte field_3_fKeep;
    protected  byte field_4_fKeepFollow;
    protected  byte field_5_fPageBreakBefore;
    protected  byte field_6_brcl;
    protected  byte field_7_brcp;
    protected  byte field_8_ilvl;
    protected  int field_9_ilfo;
    protected  byte field_10_fNoLnn;
    protected  LineSpacingDescriptor field_11_lspd;
    protected  int field_12_dyaBefore;
    protected  int field_13_dyaAfter;
    protected  byte field_14_fInTable;
    protected  byte field_15_finTableW97;
    protected  byte field_16_fTtp;
    protected  int field_17_dxaAbs;
    protected  int field_18_dyaAbs;
    protected  int field_19_dxaWidth;
    protected  byte field_20_fBrLnAbove;
    protected  byte field_21_fBrLnBelow;
    protected  byte field_22_pcVert;
    protected  byte field_23_pcHorz;
    protected  byte field_24_wr;
    protected  byte field_25_fNoAutoHyph;
    protected  int field_26_dyaHeight;
    protected  byte field_27_fMinHeight;
    protected  DropCapSpecifier field_28_dcs;
    protected  int field_29_dyaFromText;
    protected  int field_30_dxaFromText;
    protected  byte field_31_fLocked;
    protected  byte field_32_fWidowControl;
    protected  byte field_33_fKinsoku;
    protected  byte field_34_fWordWrap;
    protected  byte field_35_fOverflowPunct;
    protected  byte field_36_fTopLinePunct;
    protected  byte field_37_fAutoSpaceDE;
    protected  byte field_38_fAutoSpaceDN;
    protected  int field_39_wAlignFont;
    protected  short field_40_fontAlign;
        private static BitField  fVertical = new BitField(0x0001);
        private static BitField  fBackward = new BitField(0x0002);
        private static BitField  fRotateFont = new BitField(0x0004);
    protected  byte field_41_fVertical;
    protected  byte field_42_fBackward;
    protected  byte field_43_fRotateFont;
    protected  byte field_44_lvl;
    protected  byte field_45_fBiDi;
    protected  byte field_46_fNumRMIns;
    protected  byte field_47_fCrLf;
    protected  byte field_48_fUsePgsuSettings;
    protected  byte field_49_fAdjustRight;
    protected  short field_50_dxcRight;
    protected  short field_51_dxcLeft;
    protected  short field_52_dxcLeft1;
    protected  byte field_53_fDyaBeforeAuto;
    protected  byte field_54_fDyaAfterAuto;
    protected  int field_55_dxaRight;
    protected  int field_56_dxaLeft;
    protected  int field_57_dxaLeft1;
    protected  byte field_58_jc;
    protected  byte field_59_fNoAllowOverlap;
    protected  BorderCode field_60_brcTop;
    protected  BorderCode field_61_brcLeft;
    protected  BorderCode field_62_brcBottom;
    protected  BorderCode field_63_brcRight;
    protected  BorderCode field_64_brcBetween;
    protected  BorderCode field_65_brcBar;
    protected  ShadingDescriptor field_66_shd;
    protected  byte[] field_67_anld;
    protected  byte[] field_68_phe;
    protected  int field_69_fPropRMark;
    protected  int field_70_ibstPropRMark;
    protected  DateAndTime field_71_dttmPropRMark;
    protected  int field_72_itbdMac;
    protected  int[] field_73_rgdxaTab;
    protected  byte[] field_74_rgtbd;
    protected  byte[] field_75_numrm;
    protected  byte[] field_76_ptap;
    protected  byte field_77_tableLevel;
    protected  byte field_78_fTtpEmbedded;
    protected  byte field_79_embeddedCellMark;


    public PAPAbstractType()
    {

    }


    public String toString()
    {
        StringBuffer buffer = new StringBuffer();

        buffer.append("[PAP]\n");

        buffer.append("    .istd                 = ");
        buffer.append(" (").append(getIstd()).append(" )\n");

        buffer.append("    .fSideBySide          = ");
        buffer.append(" (").append(getFSideBySide()).append(" )\n");

        buffer.append("    .fKeep                = ");
        buffer.append(" (").append(getFKeep()).append(" )\n");

        buffer.append("    .fKeepFollow          = ");
        buffer.append(" (").append(getFKeepFollow()).append(" )\n");

        buffer.append("    .fPageBreakBefore     = ");
        buffer.append(" (").append(getFPageBreakBefore()).append(" )\n");

        buffer.append("    .brcl                 = ");
        buffer.append(" (").append(getBrcl()).append(" )\n");

        buffer.append("    .brcp                 = ");
        buffer.append(" (").append(getBrcp()).append(" )\n");

        buffer.append("    .ilvl                 = ");
        buffer.append(" (").append(getIlvl()).append(" )\n");

        buffer.append("    .ilfo                 = ");
        buffer.append(" (").append(getIlfo()).append(" )\n");

        buffer.append("    .fNoLnn               = ");
        buffer.append(" (").append(getFNoLnn()).append(" )\n");

        buffer.append("    .lspd                 = ");
        buffer.append(" (").append(getLspd()).append(" )\n");

        buffer.append("    .dyaBefore            = ");
        buffer.append(" (").append(getDyaBefore()).append(" )\n");

        buffer.append("    .dyaAfter             = ");
        buffer.append(" (").append(getDyaAfter()).append(" )\n");

        buffer.append("    .fInTable             = ");
        buffer.append(" (").append(getFInTable()).append(" )\n");

        buffer.append("    .finTableW97          = ");
        buffer.append(" (").append(getFinTableW97()).append(" )\n");

        buffer.append("    .fTtp                 = ");
        buffer.append(" (").append(getFTtp()).append(" )\n");

        buffer.append("    .dxaAbs               = ");
        buffer.append(" (").append(getDxaAbs()).append(" )\n");

        buffer.append("    .dyaAbs               = ");
        buffer.append(" (").append(getDyaAbs()).append(" )\n");

        buffer.append("    .dxaWidth             = ");
        buffer.append(" (").append(getDxaWidth()).append(" )\n");

        buffer.append("    .fBrLnAbove           = ");
        buffer.append(" (").append(getFBrLnAbove()).append(" )\n");

        buffer.append("    .fBrLnBelow           = ");
        buffer.append(" (").append(getFBrLnBelow()).append(" )\n");

        buffer.append("    .pcVert               = ");
        buffer.append(" (").append(getPcVert()).append(" )\n");

        buffer.append("    .pcHorz               = ");
        buffer.append(" (").append(getPcHorz()).append(" )\n");

        buffer.append("    .wr                   = ");
        buffer.append(" (").append(getWr()).append(" )\n");

        buffer.append("    .fNoAutoHyph          = ");
        buffer.append(" (").append(getFNoAutoHyph()).append(" )\n");

        buffer.append("    .dyaHeight            = ");
        buffer.append(" (").append(getDyaHeight()).append(" )\n");

        buffer.append("    .fMinHeight           = ");
        buffer.append(" (").append(getFMinHeight()).append(" )\n");

        buffer.append("    .dcs                  = ");
        buffer.append(" (").append(getDcs()).append(" )\n");

        buffer.append("    .dyaFromText          = ");
        buffer.append(" (").append(getDyaFromText()).append(" )\n");

        buffer.append("    .dxaFromText          = ");
        buffer.append(" (").append(getDxaFromText()).append(" )\n");

        buffer.append("    .fLocked              = ");
        buffer.append(" (").append(getFLocked()).append(" )\n");

        buffer.append("    .fWidowControl        = ");
        buffer.append(" (").append(getFWidowControl()).append(" )\n");

        buffer.append("    .fKinsoku             = ");
        buffer.append(" (").append(getFKinsoku()).append(" )\n");

        buffer.append("    .fWordWrap            = ");
        buffer.append(" (").append(getFWordWrap()).append(" )\n");

        buffer.append("    .fOverflowPunct       = ");
        buffer.append(" (").append(getFOverflowPunct()).append(" )\n");

        buffer.append("    .fTopLinePunct        = ");
        buffer.append(" (").append(getFTopLinePunct()).append(" )\n");

        buffer.append("    .fAutoSpaceDE         = ");
        buffer.append(" (").append(getFAutoSpaceDE()).append(" )\n");

        buffer.append("    .fAutoSpaceDN         = ");
        buffer.append(" (").append(getFAutoSpaceDN()).append(" )\n");

        buffer.append("    .wAlignFont           = ");
        buffer.append(" (").append(getWAlignFont()).append(" )\n");

        buffer.append("    .fontAlign            = ");
        buffer.append(" (").append(getFontAlign()).append(" )\n");
        buffer.append("         .fVertical                = ").append(isFVertical()).append('\n');
        buffer.append("         .fBackward                = ").append(isFBackward()).append('\n');
        buffer.append("         .fRotateFont              = ").append(isFRotateFont()).append('\n');

        buffer.append("    .fVertical            = ");
        buffer.append(" (").append(getFVertical()).append(" )\n");

        buffer.append("    .fBackward            = ");
        buffer.append(" (").append(getFBackward()).append(" )\n");

        buffer.append("    .fRotateFont          = ");
        buffer.append(" (").append(getFRotateFont()).append(" )\n");

        buffer.append("    .lvl                  = ");
        buffer.append(" (").append(getLvl()).append(" )\n");

        buffer.append("    .fBiDi                = ");
        buffer.append(" (").append(getFBiDi()).append(" )\n");

        buffer.append("    .fNumRMIns            = ");
        buffer.append(" (").append(getFNumRMIns()).append(" )\n");

        buffer.append("    .fCrLf                = ");
        buffer.append(" (").append(getFCrLf()).append(" )\n");

        buffer.append("    .fUsePgsuSettings     = ");
        buffer.append(" (").append(getFUsePgsuSettings()).append(" )\n");

        buffer.append("    .fAdjustRight         = ");
        buffer.append(" (").append(getFAdjustRight()).append(" )\n");

        buffer.append("    .dxcRight             = ");
        buffer.append(" (").append(getDxcRight()).append(" )\n");

        buffer.append("    .dxcLeft              = ");
        buffer.append(" (").append(getDxcLeft()).append(" )\n");

        buffer.append("    .dxcLeft1             = ");
        buffer.append(" (").append(getDxcLeft1()).append(" )\n");

        buffer.append("    .fDyaBeforeAuto       = ");
        buffer.append(" (").append(getFDyaBeforeAuto()).append(" )\n");

        buffer.append("    .fDyaAfterAuto        = ");
        buffer.append(" (").append(getFDyaAfterAuto()).append(" )\n");

        buffer.append("    .dxaRight             = ");
        buffer.append(" (").append(getDxaRight()).append(" )\n");

        buffer.append("    .dxaLeft              = ");
        buffer.append(" (").append(getDxaLeft()).append(" )\n");

        buffer.append("    .dxaLeft1             = ");
        buffer.append(" (").append(getDxaLeft1()).append(" )\n");

        buffer.append("    .jc                   = ");
        buffer.append(" (").append(getJc()).append(" )\n");

        buffer.append("    .fNoAllowOverlap      = ");
        buffer.append(" (").append(getFNoAllowOverlap()).append(" )\n");

        buffer.append("    .brcTop               = ");
        buffer.append(" (").append(getBrcTop()).append(" )\n");

        buffer.append("    .brcLeft              = ");
        buffer.append(" (").append(getBrcLeft()).append(" )\n");

        buffer.append("    .brcBottom            = ");
        buffer.append(" (").append(getBrcBottom()).append(" )\n");

        buffer.append("    .brcRight             = ");
        buffer.append(" (").append(getBrcRight()).append(" )\n");

        buffer.append("    .brcBetween           = ");
        buffer.append(" (").append(getBrcBetween()).append(" )\n");

        buffer.append("    .brcBar               = ");
        buffer.append(" (").append(getBrcBar()).append(" )\n");

        buffer.append("    .shd                  = ");
        buffer.append(" (").append(getShd()).append(" )\n");

        buffer.append("    .anld                 = ");
        buffer.append(" (").append(getAnld()).append(" )\n");

        buffer.append("    .phe                  = ");
        buffer.append(" (").append(getPhe()).append(" )\n");

        buffer.append("    .fPropRMark           = ");
        buffer.append(" (").append(getFPropRMark()).append(" )\n");

        buffer.append("    .ibstPropRMark        = ");
        buffer.append(" (").append(getIbstPropRMark()).append(" )\n");

        buffer.append("    .dttmPropRMark        = ");
        buffer.append(" (").append(getDttmPropRMark()).append(" )\n");

        buffer.append("    .itbdMac              = ");
        buffer.append(" (").append(getItbdMac()).append(" )\n");

        buffer.append("    .rgdxaTab             = ");
        buffer.append(" (").append(getRgdxaTab()).append(" )\n");

        buffer.append("    .rgtbd                = ");
        buffer.append(" (").append(getRgtbd()).append(" )\n");

        buffer.append("    .numrm                = ");
        buffer.append(" (").append(getNumrm()).append(" )\n");

        buffer.append("    .ptap                 = ");
        buffer.append(" (").append(getPtap()).append(" )\n");

        buffer.append("    .tableLevel           = ");
        buffer.append(" (").append(getTableLevel()).append(" )\n");

        buffer.append("    .fTtpEmbedded         = ");
        buffer.append(" (").append(getFTtpEmbedded()).append(" )\n");

        buffer.append("    .embeddedCellMark     = ");
        buffer.append(" (").append(getEmbeddedCellMark()).append(" )\n");

        buffer.append("[/PAP]\n");
        return buffer.toString();
    }

    /**
     * Size of record (exluding 4 byte header)
     */
    public int getSize()
    {
        return 4 +  + 2 + 1 + 1 + 1 + 1 + 1 + 1 + 1 + 2 + 1 + 4 + 4 + 4 + 1 + 1 + 1 + 4 + 4 + 4 + 1 + 1 + 1 + 1 + 1 + 1 + 2 + 1 + 2 + 4 + 4 + 1 + 1 + 1 + 1 + 1 + 1 + 1 + 1 + 2 + 2 + 1 + 1 + 1 + 1 + 1 + 1 + 1 + 1 + 1 + 2 + 2 + 2 + 1 + 1 + 4 + 4 + 4 + 1 + 1 + 4 + 4 + 4 + 4 + 4 + 4 + 2 + 84 + 12 + 1 + 2 + 4 + 2 + 128 + 128 + 128 + 4 + 1 + 1 + 1;
    }



    /**
     * Get the istd field for the PAP record.
     */
    public int getIstd()
    {
        return field_1_istd;
    }

    /**
     * Set the istd field for the PAP record.
     */
    public void setIstd(int field_1_istd)
    {
        this.field_1_istd = field_1_istd;
    }

    /**
     * Get the fSideBySide field for the PAP record.
     */
    public byte getFSideBySide()
    {
        return field_2_fSideBySide;
    }

    /**
     * Set the fSideBySide field for the PAP record.
     */
    public void setFSideBySide(byte field_2_fSideBySide)
    {
        this.field_2_fSideBySide = field_2_fSideBySide;
    }

    /**
     * Get the fKeep field for the PAP record.
     */
    public byte getFKeep()
    {
        return field_3_fKeep;
    }

    /**
     * Set the fKeep field for the PAP record.
     */
    public void setFKeep(byte field_3_fKeep)
    {
        this.field_3_fKeep = field_3_fKeep;
    }

    /**
     * Get the fKeepFollow field for the PAP record.
     */
    public byte getFKeepFollow()
    {
        return field_4_fKeepFollow;
    }

    /**
     * Set the fKeepFollow field for the PAP record.
     */
    public void setFKeepFollow(byte field_4_fKeepFollow)
    {
        this.field_4_fKeepFollow = field_4_fKeepFollow;
    }

    /**
     * Get the fPageBreakBefore field for the PAP record.
     */
    public byte getFPageBreakBefore()
    {
        return field_5_fPageBreakBefore;
    }

    /**
     * Set the fPageBreakBefore field for the PAP record.
     */
    public void setFPageBreakBefore(byte field_5_fPageBreakBefore)
    {
        this.field_5_fPageBreakBefore = field_5_fPageBreakBefore;
    }

    /**
     * Get the brcl field for the PAP record.
     */
    public byte getBrcl()
    {
        return field_6_brcl;
    }

    /**
     * Set the brcl field for the PAP record.
     */
    public void setBrcl(byte field_6_brcl)
    {
        this.field_6_brcl = field_6_brcl;
    }

    /**
     * Get the brcp field for the PAP record.
     */
    public byte getBrcp()
    {
        return field_7_brcp;
    }

    /**
     * Set the brcp field for the PAP record.
     */
    public void setBrcp(byte field_7_brcp)
    {
        this.field_7_brcp = field_7_brcp;
    }

    /**
     * Get the ilvl field for the PAP record.
     */
    public byte getIlvl()
    {
        return field_8_ilvl;
    }

    /**
     * Set the ilvl field for the PAP record.
     */
    public void setIlvl(byte field_8_ilvl)
    {
        this.field_8_ilvl = field_8_ilvl;
    }

    /**
     * Get the ilfo field for the PAP record.
     */
    public int getIlfo()
    {
        return field_9_ilfo;
    }

    /**
     * Set the ilfo field for the PAP record.
     */
    public void setIlfo(int field_9_ilfo)
    {
        this.field_9_ilfo = field_9_ilfo;
    }

    /**
     * Get the fNoLnn field for the PAP record.
     */
    public byte getFNoLnn()
    {
        return field_10_fNoLnn;
    }

    /**
     * Set the fNoLnn field for the PAP record.
     */
    public void setFNoLnn(byte field_10_fNoLnn)
    {
        this.field_10_fNoLnn = field_10_fNoLnn;
    }

    /**
     * Get the lspd field for the PAP record.
     */
    public LineSpacingDescriptor getLspd()
    {
        return field_11_lspd;
    }

    /**
     * Set the lspd field for the PAP record.
     */
    public void setLspd(LineSpacingDescriptor field_11_lspd)
    {
        this.field_11_lspd = field_11_lspd;
    }

    /**
     * Get the dyaBefore field for the PAP record.
     */
    public int getDyaBefore()
    {
        return field_12_dyaBefore;
    }

    /**
     * Set the dyaBefore field for the PAP record.
     */
    public void setDyaBefore(int field_12_dyaBefore)
    {
        this.field_12_dyaBefore = field_12_dyaBefore;
    }

    /**
     * Get the dyaAfter field for the PAP record.
     */
    public int getDyaAfter()
    {
        return field_13_dyaAfter;
    }

    /**
     * Set the dyaAfter field for the PAP record.
     */
    public void setDyaAfter(int field_13_dyaAfter)
    {
        this.field_13_dyaAfter = field_13_dyaAfter;
    }

    /**
     * Get the fInTable field for the PAP record.
     */
    public byte getFInTable()
    {
        return field_14_fInTable;
    }

    /**
     * Set the fInTable field for the PAP record.
     */
    public void setFInTable(byte field_14_fInTable)
    {
        this.field_14_fInTable = field_14_fInTable;
    }

    /**
     * Get the finTableW97 field for the PAP record.
     */
    public byte getFinTableW97()
    {
        return field_15_finTableW97;
    }

    /**
     * Set the finTableW97 field for the PAP record.
     */
    public void setFinTableW97(byte field_15_finTableW97)
    {
        this.field_15_finTableW97 = field_15_finTableW97;
    }

    /**
     * Get the fTtp field for the PAP record.
     */
    public byte getFTtp()
    {
        return field_16_fTtp;
    }

    /**
     * Set the fTtp field for the PAP record.
     */
    public void setFTtp(byte field_16_fTtp)
    {
        this.field_16_fTtp = field_16_fTtp;
    }

    /**
     * Get the dxaAbs field for the PAP record.
     */
    public int getDxaAbs()
    {
        return field_17_dxaAbs;
    }

    /**
     * Set the dxaAbs field for the PAP record.
     */
    public void setDxaAbs(int field_17_dxaAbs)
    {
        this.field_17_dxaAbs = field_17_dxaAbs;
    }

    /**
     * Get the dyaAbs field for the PAP record.
     */
    public int getDyaAbs()
    {
        return field_18_dyaAbs;
    }

    /**
     * Set the dyaAbs field for the PAP record.
     */
    public void setDyaAbs(int field_18_dyaAbs)
    {
        this.field_18_dyaAbs = field_18_dyaAbs;
    }

    /**
     * Get the dxaWidth field for the PAP record.
     */
    public int getDxaWidth()
    {
        return field_19_dxaWidth;
    }

    /**
     * Set the dxaWidth field for the PAP record.
     */
    public void setDxaWidth(int field_19_dxaWidth)
    {
        this.field_19_dxaWidth = field_19_dxaWidth;
    }

    /**
     * Get the fBrLnAbove field for the PAP record.
     */
    public byte getFBrLnAbove()
    {
        return field_20_fBrLnAbove;
    }

    /**
     * Set the fBrLnAbove field for the PAP record.
     */
    public void setFBrLnAbove(byte field_20_fBrLnAbove)
    {
        this.field_20_fBrLnAbove = field_20_fBrLnAbove;
    }

    /**
     * Get the fBrLnBelow field for the PAP record.
     */
    public byte getFBrLnBelow()
    {
        return field_21_fBrLnBelow;
    }

    /**
     * Set the fBrLnBelow field for the PAP record.
     */
    public void setFBrLnBelow(byte field_21_fBrLnBelow)
    {
        this.field_21_fBrLnBelow = field_21_fBrLnBelow;
    }

    /**
     * Get the pcVert field for the PAP record.
     */
    public byte getPcVert()
    {
        return field_22_pcVert;
    }

    /**
     * Set the pcVert field for the PAP record.
     */
    public void setPcVert(byte field_22_pcVert)
    {
        this.field_22_pcVert = field_22_pcVert;
    }

    /**
     * Get the pcHorz field for the PAP record.
     */
    public byte getPcHorz()
    {
        return field_23_pcHorz;
    }

    /**
     * Set the pcHorz field for the PAP record.
     */
    public void setPcHorz(byte field_23_pcHorz)
    {
        this.field_23_pcHorz = field_23_pcHorz;
    }

    /**
     * Get the wr field for the PAP record.
     */
    public byte getWr()
    {
        return field_24_wr;
    }

    /**
     * Set the wr field for the PAP record.
     */
    public void setWr(byte field_24_wr)
    {
        this.field_24_wr = field_24_wr;
    }

    /**
     * Get the fNoAutoHyph field for the PAP record.
     */
    public byte getFNoAutoHyph()
    {
        return field_25_fNoAutoHyph;
    }

    /**
     * Set the fNoAutoHyph field for the PAP record.
     */
    public void setFNoAutoHyph(byte field_25_fNoAutoHyph)
    {
        this.field_25_fNoAutoHyph = field_25_fNoAutoHyph;
    }

    /**
     * Get the dyaHeight field for the PAP record.
     */
    public int getDyaHeight()
    {
        return field_26_dyaHeight;
    }

    /**
     * Set the dyaHeight field for the PAP record.
     */
    public void setDyaHeight(int field_26_dyaHeight)
    {
        this.field_26_dyaHeight = field_26_dyaHeight;
    }

    /**
     * Get the fMinHeight field for the PAP record.
     */
    public byte getFMinHeight()
    {
        return field_27_fMinHeight;
    }

    /**
     * Set the fMinHeight field for the PAP record.
     */
    public void setFMinHeight(byte field_27_fMinHeight)
    {
        this.field_27_fMinHeight = field_27_fMinHeight;
    }

    /**
     * Get the dcs field for the PAP record.
     */
    public DropCapSpecifier getDcs()
    {
        return field_28_dcs;
    }

    /**
     * Set the dcs field for the PAP record.
     */
    public void setDcs(DropCapSpecifier field_28_dcs)
    {
        this.field_28_dcs = field_28_dcs;
    }

    /**
     * Get the dyaFromText field for the PAP record.
     */
    public int getDyaFromText()
    {
        return field_29_dyaFromText;
    }

    /**
     * Set the dyaFromText field for the PAP record.
     */
    public void setDyaFromText(int field_29_dyaFromText)
    {
        this.field_29_dyaFromText = field_29_dyaFromText;
    }

    /**
     * Get the dxaFromText field for the PAP record.
     */
    public int getDxaFromText()
    {
        return field_30_dxaFromText;
    }

    /**
     * Set the dxaFromText field for the PAP record.
     */
    public void setDxaFromText(int field_30_dxaFromText)
    {
        this.field_30_dxaFromText = field_30_dxaFromText;
    }

    /**
     * Get the fLocked field for the PAP record.
     */
    public byte getFLocked()
    {
        return field_31_fLocked;
    }

    /**
     * Set the fLocked field for the PAP record.
     */
    public void setFLocked(byte field_31_fLocked)
    {
        this.field_31_fLocked = field_31_fLocked;
    }

    /**
     * Get the fWidowControl field for the PAP record.
     */
    public byte getFWidowControl()
    {
        return field_32_fWidowControl;
    }

    /**
     * Set the fWidowControl field for the PAP record.
     */
    public void setFWidowControl(byte field_32_fWidowControl)
    {
        this.field_32_fWidowControl = field_32_fWidowControl;
    }

    /**
     * Get the fKinsoku field for the PAP record.
     */
    public byte getFKinsoku()
    {
        return field_33_fKinsoku;
    }

    /**
     * Set the fKinsoku field for the PAP record.
     */
    public void setFKinsoku(byte field_33_fKinsoku)
    {
        this.field_33_fKinsoku = field_33_fKinsoku;
    }

    /**
     * Get the fWordWrap field for the PAP record.
     */
    public byte getFWordWrap()
    {
        return field_34_fWordWrap;
    }

    /**
     * Set the fWordWrap field for the PAP record.
     */
    public void setFWordWrap(byte field_34_fWordWrap)
    {
        this.field_34_fWordWrap = field_34_fWordWrap;
    }

    /**
     * Get the fOverflowPunct field for the PAP record.
     */
    public byte getFOverflowPunct()
    {
        return field_35_fOverflowPunct;
    }

    /**
     * Set the fOverflowPunct field for the PAP record.
     */
    public void setFOverflowPunct(byte field_35_fOverflowPunct)
    {
        this.field_35_fOverflowPunct = field_35_fOverflowPunct;
    }

    /**
     * Get the fTopLinePunct field for the PAP record.
     */
    public byte getFTopLinePunct()
    {
        return field_36_fTopLinePunct;
    }

    /**
     * Set the fTopLinePunct field for the PAP record.
     */
    public void setFTopLinePunct(byte field_36_fTopLinePunct)
    {
        this.field_36_fTopLinePunct = field_36_fTopLinePunct;
    }

    /**
     * Get the fAutoSpaceDE field for the PAP record.
     */
    public byte getFAutoSpaceDE()
    {
        return field_37_fAutoSpaceDE;
    }

    /**
     * Set the fAutoSpaceDE field for the PAP record.
     */
    public void setFAutoSpaceDE(byte field_37_fAutoSpaceDE)
    {
        this.field_37_fAutoSpaceDE = field_37_fAutoSpaceDE;
    }

    /**
     * Get the fAutoSpaceDN field for the PAP record.
     */
    public byte getFAutoSpaceDN()
    {
        return field_38_fAutoSpaceDN;
    }

    /**
     * Set the fAutoSpaceDN field for the PAP record.
     */
    public void setFAutoSpaceDN(byte field_38_fAutoSpaceDN)
    {
        this.field_38_fAutoSpaceDN = field_38_fAutoSpaceDN;
    }

    /**
     * Get the wAlignFont field for the PAP record.
     */
    public int getWAlignFont()
    {
        return field_39_wAlignFont;
    }

    /**
     * Set the wAlignFont field for the PAP record.
     */
    public void setWAlignFont(int field_39_wAlignFont)
    {
        this.field_39_wAlignFont = field_39_wAlignFont;
    }

    /**
     * Get the fontAlign field for the PAP record.
     */
    public short getFontAlign()
    {
        return field_40_fontAlign;
    }

    /**
     * Set the fontAlign field for the PAP record.
     */
    public void setFontAlign(short field_40_fontAlign)
    {
        this.field_40_fontAlign = field_40_fontAlign;
    }

    /**
     * Get the fVertical field for the PAP record.
     */
    public byte getFVertical()
    {
        return field_41_fVertical;
    }

    /**
     * Set the fVertical field for the PAP record.
     */
    public void setFVertical(byte field_41_fVertical)
    {
        this.field_41_fVertical = field_41_fVertical;
    }

    /**
     * Get the fBackward field for the PAP record.
     */
    public byte getFBackward()
    {
        return field_42_fBackward;
    }

    /**
     * Set the fBackward field for the PAP record.
     */
    public void setFBackward(byte field_42_fBackward)
    {
        this.field_42_fBackward = field_42_fBackward;
    }

    /**
     * Get the fRotateFont field for the PAP record.
     */
    public byte getFRotateFont()
    {
        return field_43_fRotateFont;
    }

    /**
     * Set the fRotateFont field for the PAP record.
     */
    public void setFRotateFont(byte field_43_fRotateFont)
    {
        this.field_43_fRotateFont = field_43_fRotateFont;
    }

    /**
     * Get the lvl field for the PAP record.
     */
    public byte getLvl()
    {
        return field_44_lvl;
    }

    /**
     * Set the lvl field for the PAP record.
     */
    public void setLvl(byte field_44_lvl)
    {
        this.field_44_lvl = field_44_lvl;
    }

    /**
     * Get the fBiDi field for the PAP record.
     */
    public byte getFBiDi()
    {
        return field_45_fBiDi;
    }

    /**
     * Set the fBiDi field for the PAP record.
     */
    public void setFBiDi(byte field_45_fBiDi)
    {
        this.field_45_fBiDi = field_45_fBiDi;
    }

    /**
     * Get the fNumRMIns field for the PAP record.
     */
    public byte getFNumRMIns()
    {
        return field_46_fNumRMIns;
    }

    /**
     * Set the fNumRMIns field for the PAP record.
     */
    public void setFNumRMIns(byte field_46_fNumRMIns)
    {
        this.field_46_fNumRMIns = field_46_fNumRMIns;
    }

    /**
     * Get the fCrLf field for the PAP record.
     */
    public byte getFCrLf()
    {
        return field_47_fCrLf;
    }

    /**
     * Set the fCrLf field for the PAP record.
     */
    public void setFCrLf(byte field_47_fCrLf)
    {
        this.field_47_fCrLf = field_47_fCrLf;
    }

    /**
     * Get the fUsePgsuSettings field for the PAP record.
     */
    public byte getFUsePgsuSettings()
    {
        return field_48_fUsePgsuSettings;
    }

    /**
     * Set the fUsePgsuSettings field for the PAP record.
     */
    public void setFUsePgsuSettings(byte field_48_fUsePgsuSettings)
    {
        this.field_48_fUsePgsuSettings = field_48_fUsePgsuSettings;
    }

    /**
     * Get the fAdjustRight field for the PAP record.
     */
    public byte getFAdjustRight()
    {
        return field_49_fAdjustRight;
    }

    /**
     * Set the fAdjustRight field for the PAP record.
     */
    public void setFAdjustRight(byte field_49_fAdjustRight)
    {
        this.field_49_fAdjustRight = field_49_fAdjustRight;
    }

    /**
     * Get the dxcRight field for the PAP record.
     */
    public short getDxcRight()
    {
        return field_50_dxcRight;
    }

    /**
     * Set the dxcRight field for the PAP record.
     */
    public void setDxcRight(short field_50_dxcRight)
    {
        this.field_50_dxcRight = field_50_dxcRight;
    }

    /**
     * Get the dxcLeft field for the PAP record.
     */
    public short getDxcLeft()
    {
        return field_51_dxcLeft;
    }

    /**
     * Set the dxcLeft field for the PAP record.
     */
    public void setDxcLeft(short field_51_dxcLeft)
    {
        this.field_51_dxcLeft = field_51_dxcLeft;
    }

    /**
     * Get the dxcLeft1 field for the PAP record.
     */
    public short getDxcLeft1()
    {
        return field_52_dxcLeft1;
    }

    /**
     * Set the dxcLeft1 field for the PAP record.
     */
    public void setDxcLeft1(short field_52_dxcLeft1)
    {
        this.field_52_dxcLeft1 = field_52_dxcLeft1;
    }

    /**
     * Get the fDyaBeforeAuto field for the PAP record.
     */
    public byte getFDyaBeforeAuto()
    {
        return field_53_fDyaBeforeAuto;
    }

    /**
     * Set the fDyaBeforeAuto field for the PAP record.
     */
    public void setFDyaBeforeAuto(byte field_53_fDyaBeforeAuto)
    {
        this.field_53_fDyaBeforeAuto = field_53_fDyaBeforeAuto;
    }

    /**
     * Get the fDyaAfterAuto field for the PAP record.
     */
    public byte getFDyaAfterAuto()
    {
        return field_54_fDyaAfterAuto;
    }

    /**
     * Set the fDyaAfterAuto field for the PAP record.
     */
    public void setFDyaAfterAuto(byte field_54_fDyaAfterAuto)
    {
        this.field_54_fDyaAfterAuto = field_54_fDyaAfterAuto;
    }

    /**
     * Get the dxaRight field for the PAP record.
     */
    public int getDxaRight()
    {
        return field_55_dxaRight;
    }

    /**
     * Set the dxaRight field for the PAP record.
     */
    public void setDxaRight(int field_55_dxaRight)
    {
        this.field_55_dxaRight = field_55_dxaRight;
    }

    /**
     * Get the dxaLeft field for the PAP record.
     */
    public int getDxaLeft()
    {
        return field_56_dxaLeft;
    }

    /**
     * Set the dxaLeft field for the PAP record.
     */
    public void setDxaLeft(int field_56_dxaLeft)
    {
        this.field_56_dxaLeft = field_56_dxaLeft;
    }

    /**
     * Get the dxaLeft1 field for the PAP record.
     */
    public int getDxaLeft1()
    {
        return field_57_dxaLeft1;
    }

    /**
     * Set the dxaLeft1 field for the PAP record.
     */
    public void setDxaLeft1(int field_57_dxaLeft1)
    {
        this.field_57_dxaLeft1 = field_57_dxaLeft1;
    }

    /**
     * Get the jc field for the PAP record.
     */
    public byte getJc()
    {
        return field_58_jc;
    }

    /**
     * Set the jc field for the PAP record.
     */
    public void setJc(byte field_58_jc)
    {
        this.field_58_jc = field_58_jc;
    }

    /**
     * Get the fNoAllowOverlap field for the PAP record.
     */
    public byte getFNoAllowOverlap()
    {
        return field_59_fNoAllowOverlap;
    }

    /**
     * Set the fNoAllowOverlap field for the PAP record.
     */
    public void setFNoAllowOverlap(byte field_59_fNoAllowOverlap)
    {
        this.field_59_fNoAllowOverlap = field_59_fNoAllowOverlap;
    }

    /**
     * Get the brcTop field for the PAP record.
     */
    public BorderCode getBrcTop()
    {
        return field_60_brcTop;
    }

    /**
     * Set the brcTop field for the PAP record.
     */
    public void setBrcTop(BorderCode field_60_brcTop)
    {
        this.field_60_brcTop = field_60_brcTop;
    }

    /**
     * Get the brcLeft field for the PAP record.
     */
    public BorderCode getBrcLeft()
    {
        return field_61_brcLeft;
    }

    /**
     * Set the brcLeft field for the PAP record.
     */
    public void setBrcLeft(BorderCode field_61_brcLeft)
    {
        this.field_61_brcLeft = field_61_brcLeft;
    }

    /**
     * Get the brcBottom field for the PAP record.
     */
    public BorderCode getBrcBottom()
    {
        return field_62_brcBottom;
    }

    /**
     * Set the brcBottom field for the PAP record.
     */
    public void setBrcBottom(BorderCode field_62_brcBottom)
    {
        this.field_62_brcBottom = field_62_brcBottom;
    }

    /**
     * Get the brcRight field for the PAP record.
     */
    public BorderCode getBrcRight()
    {
        return field_63_brcRight;
    }

    /**
     * Set the brcRight field for the PAP record.
     */
    public void setBrcRight(BorderCode field_63_brcRight)
    {
        this.field_63_brcRight = field_63_brcRight;
    }

    /**
     * Get the brcBetween field for the PAP record.
     */
    public BorderCode getBrcBetween()
    {
        return field_64_brcBetween;
    }

    /**
     * Set the brcBetween field for the PAP record.
     */
    public void setBrcBetween(BorderCode field_64_brcBetween)
    {
        this.field_64_brcBetween = field_64_brcBetween;
    }

    /**
     * Get the brcBar field for the PAP record.
     */
    public BorderCode getBrcBar()
    {
        return field_65_brcBar;
    }

    /**
     * Set the brcBar field for the PAP record.
     */
    public void setBrcBar(BorderCode field_65_brcBar)
    {
        this.field_65_brcBar = field_65_brcBar;
    }

    /**
     * Get the shd field for the PAP record.
     */
    public ShadingDescriptor getShd()
    {
        return field_66_shd;
    }

    /**
     * Set the shd field for the PAP record.
     */
    public void setShd(ShadingDescriptor field_66_shd)
    {
        this.field_66_shd = field_66_shd;
    }

    /**
     * Get the anld field for the PAP record.
     */
    public byte[] getAnld()
    {
        return field_67_anld;
    }

    /**
     * Set the anld field for the PAP record.
     */
    public void setAnld(byte[] field_67_anld)
    {
        this.field_67_anld = field_67_anld;
    }

    /**
     * Get the phe field for the PAP record.
     */
    public byte[] getPhe()
    {
        return field_68_phe;
    }

    /**
     * Set the phe field for the PAP record.
     */
    public void setPhe(byte[] field_68_phe)
    {
        this.field_68_phe = field_68_phe;
    }

    /**
     * Get the fPropRMark field for the PAP record.
     */
    public int getFPropRMark()
    {
        return field_69_fPropRMark;
    }

    /**
     * Set the fPropRMark field for the PAP record.
     */
    public void setFPropRMark(int field_69_fPropRMark)
    {
        this.field_69_fPropRMark = field_69_fPropRMark;
    }

    /**
     * Get the ibstPropRMark field for the PAP record.
     */
    public int getIbstPropRMark()
    {
        return field_70_ibstPropRMark;
    }

    /**
     * Set the ibstPropRMark field for the PAP record.
     */
    public void setIbstPropRMark(int field_70_ibstPropRMark)
    {
        this.field_70_ibstPropRMark = field_70_ibstPropRMark;
    }

    /**
     * Get the dttmPropRMark field for the PAP record.
     */
    public DateAndTime getDttmPropRMark()
    {
        return field_71_dttmPropRMark;
    }

    /**
     * Set the dttmPropRMark field for the PAP record.
     */
    public void setDttmPropRMark(DateAndTime field_71_dttmPropRMark)
    {
        this.field_71_dttmPropRMark = field_71_dttmPropRMark;
    }

    /**
     * Get the itbdMac field for the PAP record.
     */
    public int getItbdMac()
    {
        return field_72_itbdMac;
    }

    /**
     * Set the itbdMac field for the PAP record.
     */
    public void setItbdMac(int field_72_itbdMac)
    {
        this.field_72_itbdMac = field_72_itbdMac;
    }

    /**
     * Get the rgdxaTab field for the PAP record.
     */
    public int[] getRgdxaTab()
    {
        return field_73_rgdxaTab;
    }

    /**
     * Set the rgdxaTab field for the PAP record.
     */
    public void setRgdxaTab(int[] field_73_rgdxaTab)
    {
        this.field_73_rgdxaTab = field_73_rgdxaTab;
    }

    /**
     * Get the rgtbd field for the PAP record.
     */
    public byte[] getRgtbd()
    {
        return field_74_rgtbd;
    }

    /**
     * Set the rgtbd field for the PAP record.
     */
    public void setRgtbd(byte[] field_74_rgtbd)
    {
        this.field_74_rgtbd = field_74_rgtbd;
    }

    /**
     * Get the numrm field for the PAP record.
     */
    public byte[] getNumrm()
    {
        return field_75_numrm;
    }

    /**
     * Set the numrm field for the PAP record.
     */
    public void setNumrm(byte[] field_75_numrm)
    {
        this.field_75_numrm = field_75_numrm;
    }

    /**
     * Get the ptap field for the PAP record.
     */
    public byte[] getPtap()
    {
        return field_76_ptap;
    }

    /**
     * Set the ptap field for the PAP record.
     */
    public void setPtap(byte[] field_76_ptap)
    {
        this.field_76_ptap = field_76_ptap;
    }

    /**
     * Get the tableLevel field for the PAP record.
     */
    public byte getTableLevel()
    {
        return field_77_tableLevel;
    }

    /**
     * Set the tableLevel field for the PAP record.
     */
    public void setTableLevel(byte field_77_tableLevel)
    {
        this.field_77_tableLevel = field_77_tableLevel;
    }

    /**
     * Get the fTtpEmbedded field for the PAP record.
     */
    public byte getFTtpEmbedded()
    {
        return field_78_fTtpEmbedded;
    }

    /**
     * Set the fTtpEmbedded field for the PAP record.
     */
    public void setFTtpEmbedded(byte field_78_fTtpEmbedded)
    {
        this.field_78_fTtpEmbedded = field_78_fTtpEmbedded;
    }

    /**
     * Get the embeddedCellMark field for the PAP record.
     */
    public byte getEmbeddedCellMark()
    {
        return field_79_embeddedCellMark;
    }

    /**
     * Set the embeddedCellMark field for the PAP record.
     */
    public void setEmbeddedCellMark(byte field_79_embeddedCellMark)
    {
        this.field_79_embeddedCellMark = field_79_embeddedCellMark;
    }

    /**
     * Sets the fVertical field value.
     * 
     */
    public void setFVertical(boolean value)
    {
        field_40_fontAlign = (short)fVertical.setBoolean(field_40_fontAlign, value);

        
    }

    /**
     * 
     * @return  the fVertical field value.
     */
    public boolean isFVertical()
    {
        return fVertical.isSet(field_40_fontAlign);
        
    }

    /**
     * Sets the fBackward field value.
     * 
     */
    public void setFBackward(boolean value)
    {
        field_40_fontAlign = (short)fBackward.setBoolean(field_40_fontAlign, value);

        
    }

    /**
     * 
     * @return  the fBackward field value.
     */
    public boolean isFBackward()
    {
        return fBackward.isSet(field_40_fontAlign);
        
    }

    /**
     * Sets the fRotateFont field value.
     * 
     */
    public void setFRotateFont(boolean value)
    {
        field_40_fontAlign = (short)fRotateFont.setBoolean(field_40_fontAlign, value);

        
    }

    /**
     * 
     * @return  the fRotateFont field value.
     */
    public boolean isFRotateFont()
    {
        return fRotateFont.isSet(field_40_fontAlign);
        
    }


}  // END OF CLASS




