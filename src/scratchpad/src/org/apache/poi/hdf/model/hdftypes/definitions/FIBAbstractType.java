
/* ====================================================================
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2002 The Apache Software Foundation.  All rights
 * reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution,
 *    if any, must include the following acknowledgment:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowledgment may appear in the software itself,
 *    if and wherever such third-party acknowledgments normally appear.
 *
 * 4. The names "Apache" and "Apache Software Foundation" and
 *    "Apache POI" must not be used to endorse or promote products
 *    derived from this software without prior written permission. For
 *    written permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache",
 *    "Apache POI", nor may "Apache" appear in their name, without
 *    prior written permission of the Apache Software Foundation.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */


package org.apache.poi.hdf.model.hdftypes.definitions;



import org.apache.poi.util.BitField;
import org.apache.poi.util.LittleEndian;
import org.apache.poi.util.StringUtil;
import org.apache.poi.util.HexDump;
import org.apache.poi.hdf.model.hdftypes.HDFType;

/**
 * File information Block.
 * NOTE: This source is automatically generated please do not modify this file.  Either subclass or
 *       remove the record in src/records/definitions.

 * @author Andrew C. Oliver
 */
public class FIBType
    implements HDFType
{

    private  short      field_1_id;
    private  short      field_2_version;
    private  short      field_3_productVersion;
    private  short      field_4_languageStamp;
    private  short      field_5_unknown1;
    private  short      field_6_options;
    private BitField   template                                   = new BitField(0x0001);
    private BitField   glossary                                   = new BitField(0x0002);
    private BitField   quicksave                                  = new BitField(0x0004);
    private BitField   haspictr                                   = new BitField(0x0008);
    private BitField   nquicksaves                                = new BitField(0x00F0);
    private BitField   encrypted                                  = new BitField(0x0100);
    private BitField   tabletype                                  = new BitField(0x0200);
    private BitField   readonly                                   = new BitField(0x0400);
    private BitField   writeReservation                           = new BitField(0x0800);
    private BitField   extendedCharacter                          = new BitField(0x1000);
    private BitField   loadOverride                               = new BitField(0x2000);
    private BitField   farEast                                    = new BitField(0x4000);
    private BitField   crypto                                     = new BitField(0x8000);
    private  short      field_7_minversion;
    private  short      field_8_encryptedKey;
    private  short      field_9_environment;
    private  short      field_10_history;
    private BitField   historyMac                                 = new BitField(0x0001);
    private BitField   emptySpecial                               = new BitField(0x0002);
    private BitField   loadOverrideHist                           = new BitField(0x0004);
    private BitField   featureUndo                                = new BitField(0x0008);
    private BitField   v97Saved                                   = new BitField(0x0010);
    private BitField   spare                                      = new BitField(0x00FE);
    private  short      field_11_defaultCharset;
    private  short      field_12_defaultExtcharset;
    private  int        field_13_offsetFirstChar;
    private  int        field_14_offsetLastChar;
    private  short      field_15_countShorts;
    private  short      field_16_creatorIdOrBegShorts;
    private  short      field_17_revisorId;
    private  short      field_18_creatorPrivate;
    private  short      field_19_revisorPrivate;
    private  short      field_20_unused;
    private  short      field_21_unused;
    private  short      field_22_unused;
    private  short      field_23_unused;
    private  short      field_24_unused;
    private  short      field_25_unused;
    private  short      field_26_unused;
    private  short      field_27_unused;
    private  short      field_28_unused;
    private  short      field_29_fareastid;
    private  short      field_30_countints;
    private  int        field_31_lastByteOrBegInts;
    private  int        field_32_creatorBuildDate;
    private  int        field_33_revisorBuildDate;
    private  int        field_34_mainStreamlen;
    private  int        field_35_footnoteStreamlen;
    private  int        field_36_headerStreamlen;
    private  int        field_37_macroStreamlen;
    private  int        field_38_annotationStreamlen;
    private  int        field_39_endnoteStreamlen;
    private  int        field_40_textboxStreamlen;
    private  int        field_41_headboxStreamlen;
    private  int        field_42_ptrToPlcListChp;
    private  int        field_43_firstChp;
    private  int        field_44_countChps;
    private  int        field_45_ptrToPlcListPap;
    private  int        field_46_firstPap;
    private  int        field_47_countPaps;
    private  int        field_48_ptrToPlcListLvc;
    private  int        field_49_firstLvc;
    private  int        field_50_countLvc;
    private  int        field_51_unknown;
    private  int        field_52_unknown;
    private  short      field_53_lcbArraySize;
    private  int        field_54_originalStylesheetOffset;
    private  int        field_55_originalStylesheetSize;
    private  int        field_56_stylesheetOffset;
    private  int        field_57_stylesheetSize;
    private  int        field_58_footnoteRefOffset;
    private  int        field_59_footnoteRefSize;
    private  int        field_60_plcOffset;
    private  int        field_61_plcSize;
    private  int        field_62_annotationRefOffset;
    private  int        field_63_annotationRefSize;
    private  int        field_64_annotationPlcOffset;
    private  int        field_65_annotationPlcSize;
    private  int        field_66_sectionPlcOffset;
    private  int        field_67_sectionPlcSize;
    private  int        field_68_unused;
    private  int        field_69_unused;
    private  int        field_70_pheplcOffset;
    private  int        field_71_pheplcSize;
    private  int        field_72_glossarySTOffset;
    private  int        field_73_glossarySTSize;
    private  int        field_74_glossaryPLCOffset;
    private  int        field_75_glossaryPLCSize;
    private  int        field_76_headerPLCOffset;
    private  int        field_77_headerPLCSize;
    private  int        field_78_chp_bin_table_offset;
    private  int        field_79_chp_bin_table_size;
    private  int        field_80_pap_bin_table_offset;
    private  int        field_81_pap_bin_table_size;
    private  int        field_82_sea_bin_table_offset;
    private  int        field_83_sea_bin_table_size;
    private  int        field_84_fonts_bin_table_offset;
    private  int        field_85_fonts_bin_table_size;
    private  int        field_86_main_fields_offset;
    private  int        field_87_main_fields_size;
    private  int        field_88_header_fields_offset;
    private  int        field_89_header_fields_size;
    private  int        field_90_footnote_fields_offset;
    private  int        field_91_footnote_fields_size;
    private  int        field_92_ann_fields_offset;
    private  int        field_93_ann_fields_size;
    private  int        field_94_unused;
    private  int        field_95_unused;
    private  int        field_96_bookmark_names_offset;
    private  int        field_97_bookmark_names_size;
    private  int        field_98_bookmark_offsets_offset;
    private  int        field_99_bookmark_offsets_size;
    private  int        field_100_macros_offset;
    private  int        field_101_macros_size;
    private  int        field_102_unused;
    private  int        field_103_unused;
    private  int        field_104_unused;
    private  int        field_105_unused;
    private  int        field_106_printerOffset;
    private  int        field_107_printerSize;
    private  int        field_108_printerPortraitOffset;
    private  int        field_109_printerPortraitSize;
    private  int        field_110_printerLandscapeOffset;
    private  int        field_111_printerLandscapeSize;
    private  int        field_112_wssOffset;
    private  int        field_113_wssSize;
    private  int        field_114_DOPOffset;
    private  int        field_115_DOPSize;
    private  int        field_116_sttbfassoc_offset;
    private  int        field_117_sttbfassoc_size;
    private  int        field_118_textPieceTableOffset;
    private  int        field_119_textPieceTableSize;
    private  int        field_120_unused;
    private  int        field_121_unused;
    private  int        field_122_offsetAutosaveSource;
    private  int        field_123_countAutosaveSource;
    private  int        field_124_offsetGrpXstAtnOwners;
    private  int        field_125_countGrpXstAtnOwners;
    private  int        field_126_offsetSttbfAtnbkmk;
    private  int        field_127_lengthSttbfAtnbkmk;
    private  int        field_128_unused;
    private  int        field_129_unused;
    private  int        field_130_unused;
    private  int        field_131_unused;
    private  int        field_132_offsetPlcspaMom;
    private  int        field_133_lengthPlcspaMom;
    private  int        field_134_offsetPlcspaHdr;
    private  int        field_135_lengthPlcspaHdr;
    private  int        field_136_lengthPlcfBookmarkFirst;
    private  int        field_137_offsetPlcfBookmarkFirst;


    public FIBType()
    {

    }

    protected void fillFields(byte [] data, short size, int offset)
    {
        field_1_id                      = LittleEndian.getShort(data, 0x0 + offset);
        field_2_version                 = LittleEndian.getShort(data, 0x2 + offset);
        field_3_productVersion          = LittleEndian.getShort(data, 0x4 + offset);
        field_4_languageStamp           = LittleEndian.getShort(data, 0x6 + offset);
        field_5_unknown1                = LittleEndian.getShort(data, 0x8 + offset);
        field_6_options                 = LittleEndian.getShort(data, 0xa + offset);
        field_7_minversion              = LittleEndian.getShort(data, 0xc + offset);
        field_8_encryptedKey            = LittleEndian.getShort(data, 0xe + offset);
        field_9_environment             = LittleEndian.getShort(data, 0x10 + offset);
        field_10_history                = LittleEndian.getShort(data, 0x12 + offset);
        field_11_defaultCharset         = LittleEndian.getShort(data, 0x14 + offset);
        field_12_defaultExtcharset      = LittleEndian.getShort(data, 0x16 + offset);
        field_13_offsetFirstChar        = LittleEndian.getInt(data, 0x18 + offset);
        field_14_offsetLastChar         = LittleEndian.getInt(data, 0x1c + offset);
        field_15_countShorts            = LittleEndian.getShort(data, 0x20 + offset);
        field_16_creatorIdOrBegShorts   = LittleEndian.getShort(data, 0x22 + offset);
        field_17_revisorId              = LittleEndian.getShort(data, 0x24 + offset);
        field_18_creatorPrivate         = LittleEndian.getShort(data, 0x26 + offset);
        field_19_revisorPrivate         = LittleEndian.getShort(data, 0x28 + offset);
        field_20_unused                 = LittleEndian.getShort(data, 0x2a + offset);
        field_21_unused                 = LittleEndian.getShort(data, 0x2c + offset);
        field_22_unused                 = LittleEndian.getShort(data, 0x2e + offset);
        field_23_unused                 = LittleEndian.getShort(data, 0x30 + offset);
        field_24_unused                 = LittleEndian.getShort(data, 0x32 + offset);
        field_25_unused                 = LittleEndian.getShort(data, 0x34 + offset);
        field_26_unused                 = LittleEndian.getShort(data, 0x36 + offset);
        field_27_unused                 = LittleEndian.getShort(data, 0x38 + offset);
        field_28_unused                 = LittleEndian.getShort(data, 0x3a + offset);
        field_29_fareastid              = LittleEndian.getShort(data, 0x3c + offset);
        field_30_countints              = LittleEndian.getShort(data, 0x3e + offset);
        field_31_lastByteOrBegInts      = LittleEndian.getInt(data, 0x40 + offset);
        field_32_creatorBuildDate       = LittleEndian.getInt(data, 0x44 + offset);
        field_33_revisorBuildDate       = LittleEndian.getInt(data, 0x48 + offset);
        field_34_mainStreamlen          = LittleEndian.getInt(data, 0x4c + offset);
        field_35_footnoteStreamlen      = LittleEndian.getInt(data, 0x50 + offset);
        field_36_headerStreamlen        = LittleEndian.getInt(data, 0x54 + offset);
        field_37_macroStreamlen         = LittleEndian.getInt(data, 0x58 + offset);
        field_38_annotationStreamlen    = LittleEndian.getInt(data, 0x5c + offset);
        field_39_endnoteStreamlen       = LittleEndian.getInt(data, 0x60 + offset);
        field_40_textboxStreamlen       = LittleEndian.getInt(data, 0x64 + offset);
        field_41_headboxStreamlen       = LittleEndian.getInt(data, 0x68 + offset);
        field_42_ptrToPlcListChp        = LittleEndian.getInt(data, 0x6c + offset);
        field_43_firstChp               = LittleEndian.getInt(data, 0x70 + offset);
        field_44_countChps              = LittleEndian.getInt(data, 0x74 + offset);
        field_45_ptrToPlcListPap        = LittleEndian.getInt(data, 0x78 + offset);
        field_46_firstPap               = LittleEndian.getInt(data, 0x7c + offset);
        field_47_countPaps              = LittleEndian.getInt(data, 0x80 + offset);
        field_48_ptrToPlcListLvc        = LittleEndian.getInt(data, 0x84 + offset);
        field_49_firstLvc               = LittleEndian.getInt(data, 0x88 + offset);
        field_50_countLvc               = LittleEndian.getInt(data, 0x8c + offset);
        field_51_unknown                = LittleEndian.getInt(data, 0x90 + offset);
        field_52_unknown                = LittleEndian.getInt(data, 0x94 + offset);
        field_53_lcbArraySize           = LittleEndian.getShort(data, 0x98 + offset);
        field_54_originalStylesheetOffset  = LittleEndian.getInt(data, 0x9a + offset);
        field_55_originalStylesheetSize  = LittleEndian.getInt(data, 0x9e + offset);
        field_56_stylesheetOffset       = LittleEndian.getInt(data, 0xa2 + offset);
        field_57_stylesheetSize         = LittleEndian.getInt(data, 0xa6 + offset);
        field_58_footnoteRefOffset      = LittleEndian.getInt(data, 0xaa + offset);
        field_59_footnoteRefSize        = LittleEndian.getInt(data, 0xae + offset);
        field_60_plcOffset              = LittleEndian.getInt(data, 0xb2 + offset);
        field_61_plcSize                = LittleEndian.getInt(data, 0xb6 + offset);
        field_62_annotationRefOffset    = LittleEndian.getInt(data, 0xba + offset);
        field_63_annotationRefSize      = LittleEndian.getInt(data, 0xbe + offset);
        field_64_annotationPlcOffset    = LittleEndian.getInt(data, 0xc2 + offset);
        field_65_annotationPlcSize      = LittleEndian.getInt(data, 0xc6 + offset);
        field_66_sectionPlcOffset       = LittleEndian.getInt(data, 0xca + offset);
        field_67_sectionPlcSize         = LittleEndian.getInt(data, 0xce + offset);
        field_68_unused                 = LittleEndian.getInt(data, 0xd2 + offset);
        field_69_unused                 = LittleEndian.getInt(data, 0xd6 + offset);
        field_70_pheplcOffset           = LittleEndian.getInt(data, 0xda + offset);
        field_71_pheplcSize             = LittleEndian.getInt(data, 0xde + offset);
        field_72_glossarySTOffset       = LittleEndian.getInt(data, 0xe2 + offset);
        field_73_glossarySTSize         = LittleEndian.getInt(data, 0xe6 + offset);
        field_74_glossaryPLCOffset      = LittleEndian.getInt(data, 0xea + offset);
        field_75_glossaryPLCSize        = LittleEndian.getInt(data, 0xee + offset);
        field_76_headerPLCOffset        = LittleEndian.getInt(data, 0xf2 + offset);
        field_77_headerPLCSize          = LittleEndian.getInt(data, 0xf6 + offset);
        field_78_chp_bin_table_offset   = LittleEndian.getInt(data, 0xfa + offset);
        field_79_chp_bin_table_size     = LittleEndian.getInt(data, 0xfe + offset);
        field_80_pap_bin_table_offset   = LittleEndian.getInt(data, 0x102 + offset);
        field_81_pap_bin_table_size     = LittleEndian.getInt(data, 0x106 + offset);
        field_82_sea_bin_table_offset   = LittleEndian.getInt(data, 0x10a + offset);
        field_83_sea_bin_table_size     = LittleEndian.getInt(data, 0x10e + offset);
        field_84_fonts_bin_table_offset  = LittleEndian.getInt(data, 0x112 + offset);
        field_85_fonts_bin_table_size   = LittleEndian.getInt(data, 0x116 + offset);
        field_86_main_fields_offset     = LittleEndian.getInt(data, 0x11a + offset);
        field_87_main_fields_size       = LittleEndian.getInt(data, 0x11e + offset);
        field_88_header_fields_offset   = LittleEndian.getInt(data, 0x122 + offset);
        field_89_header_fields_size     = LittleEndian.getInt(data, 0x126 + offset);
        field_90_footnote_fields_offset  = LittleEndian.getInt(data, 0x12a + offset);
        field_91_footnote_fields_size   = LittleEndian.getInt(data, 0x12e + offset);
        field_92_ann_fields_offset      = LittleEndian.getInt(data, 0x132 + offset);
        field_93_ann_fields_size        = LittleEndian.getInt(data, 0x136 + offset);
        field_94_unused                 = LittleEndian.getInt(data, 0x13a + offset);
        field_95_unused                 = LittleEndian.getInt(data, 0x13e + offset);
        field_96_bookmark_names_offset  = LittleEndian.getInt(data, 0x142 + offset);
        field_97_bookmark_names_size    = LittleEndian.getInt(data, 0x146 + offset);
        field_98_bookmark_offsets_offset  = LittleEndian.getInt(data, 0x14a + offset);
        field_99_bookmark_offsets_size  = LittleEndian.getInt(data, 0x14e + offset);
        field_100_macros_offset         = LittleEndian.getInt(data, 0x152 + offset);
        field_101_macros_size           = LittleEndian.getInt(data, 0x156 + offset);
        field_102_unused                = LittleEndian.getInt(data, 0x15a + offset);
        field_103_unused                = LittleEndian.getInt(data, 0x15e + offset);
        field_104_unused                = LittleEndian.getInt(data, 0x162 + offset);
        field_105_unused                = LittleEndian.getInt(data, 0x166 + offset);
        field_106_printerOffset         = LittleEndian.getInt(data, 0x16a + offset);
        field_107_printerSize           = LittleEndian.getInt(data, 0x16e + offset);
        field_108_printerPortraitOffset  = LittleEndian.getInt(data, 0x172 + offset);
        field_109_printerPortraitSize   = LittleEndian.getInt(data, 0x176 + offset);
        field_110_printerLandscapeOffset  = LittleEndian.getInt(data, 0x17a + offset);
        field_111_printerLandscapeSize  = LittleEndian.getInt(data, 0x17e + offset);
        field_112_wssOffset             = LittleEndian.getInt(data, 0x182 + offset);
        field_113_wssSize               = LittleEndian.getInt(data, 0x186 + offset);
        field_114_DOPOffset             = LittleEndian.getInt(data, 0x18a + offset);
        field_115_DOPSize               = LittleEndian.getInt(data, 0x18e + offset);
        field_116_sttbfassoc_offset     = LittleEndian.getInt(data, 0x192 + offset);
        field_117_sttbfassoc_size       = LittleEndian.getInt(data, 0x196 + offset);
        field_118_textPieceTableOffset  = LittleEndian.getInt(data, 0x19a + offset);
        field_119_textPieceTableSize    = LittleEndian.getInt(data, 0x19e + offset);
        field_120_unused                = LittleEndian.getInt(data, 0x1a2 + offset);
        field_121_unused                = LittleEndian.getInt(data, 0x1a6 + offset);
        field_122_offsetAutosaveSource  = LittleEndian.getInt(data, 0x1aa + offset);
        field_123_countAutosaveSource   = LittleEndian.getInt(data, 0x1ae + offset);
        field_124_offsetGrpXstAtnOwners  = LittleEndian.getInt(data, 0x1b2 + offset);
        field_125_countGrpXstAtnOwners  = LittleEndian.getInt(data, 0x1b6 + offset);
        field_126_offsetSttbfAtnbkmk    = LittleEndian.getInt(data, 0x1ba + offset);
        field_127_lengthSttbfAtnbkmk    = LittleEndian.getInt(data, 0x1be + offset);
        field_128_unused                = LittleEndian.getInt(data, 0x1c2 + offset);
        field_129_unused                = LittleEndian.getInt(data, 0x1c6 + offset);
        field_130_unused                = LittleEndian.getInt(data, 0x1ca + offset);
        field_131_unused                = LittleEndian.getInt(data, 0x1ce + offset);
        field_132_offsetPlcspaMom       = LittleEndian.getInt(data, 0x1d2 + offset);
        field_133_lengthPlcspaMom       = LittleEndian.getInt(data, 0x1d6 + offset);
        field_134_offsetPlcspaHdr       = LittleEndian.getInt(data, 0x1da + offset);
        field_135_lengthPlcspaHdr       = LittleEndian.getInt(data, 0x1de + offset);
        field_136_lengthPlcfBookmarkFirst  = LittleEndian.getInt(data, 0x1e2 + offset);
        field_137_offsetPlcfBookmarkFirst  = LittleEndian.getInt(data, 0x1e6 + offset);

    }

    public String toString()
    {
        StringBuffer buffer = new StringBuffer();

        buffer.append("[FIB]\n");

        buffer.append("    .id                   = ")
            .append("0x")
            .append(HexDump.toHex((short)getId()))
            .append(" (").append(getId()).append(" )\n");

        buffer.append("    .version              = ")
            .append("0x")
            .append(HexDump.toHex((short)getVersion()))
            .append(" (").append(getVersion()).append(" )\n");

        buffer.append("    .productVersion       = ")
            .append("0x")
            .append(HexDump.toHex((short)getProductVersion()))
            .append(" (").append(getProductVersion()).append(" )\n");

        buffer.append("    .languageStamp        = ")
            .append("0x")
            .append(HexDump.toHex((short)getLanguageStamp()))
            .append(" (").append(getLanguageStamp()).append(" )\n");

        buffer.append("    .unknown1             = ")
            .append("0x")
            .append(HexDump.toHex((short)getUnknown1()))
            .append(" (").append(getUnknown1()).append(" )\n");

        buffer.append("    .options              = ")
            .append("0x")
            .append(HexDump.toHex((short)getOptions()))
            .append(" (").append(getOptions()).append(" )\n");
        buffer.append("         .template                 = ").append(isTemplate            ()).append('\n');
        buffer.append("         .glossary                 = ").append(isGlossary            ()).append('\n');
        buffer.append("         .quicksave                = ").append(isQuicksave           ()).append('\n');
        buffer.append("         .haspictr                 = ").append(isHaspictr            ()).append('\n');
        buffer.append("         .nquicksaves              = ").append(isNquicksaves         ()).append('\n');
        buffer.append("         .encrypted                = ").append(isEncrypted           ()).append('\n');
        buffer.append("         .tabletype                = ").append(isTabletype           ()).append('\n');
        buffer.append("         .readonly                 = ").append(isReadonly            ()).append('\n');
        buffer.append("         .writeReservation         = ").append(isWriteReservation    ()).append('\n');
        buffer.append("         .extendedCharacter        = ").append(isExtendedCharacter   ()).append('\n');
        buffer.append("         .loadOverride             = ").append(isLoadOverride        ()).append('\n');
        buffer.append("         .farEast                  = ").append(isFarEast             ()).append('\n');
        buffer.append("         .crypto                   = ").append(isCrypto              ()).append('\n');

        buffer.append("    .minversion           = ")
            .append("0x")
            .append(HexDump.toHex((short)getMinversion()))
            .append(" (").append(getMinversion()).append(" )\n");

        buffer.append("    .encryptedKey         = ")
            .append("0x")
            .append(HexDump.toHex((short)getEncryptedKey()))
            .append(" (").append(getEncryptedKey()).append(" )\n");

        buffer.append("    .environment          = ")
            .append("0x")
            .append(HexDump.toHex((short)getEnvironment()))
            .append(" (").append(getEnvironment()).append(" )\n");

        buffer.append("    .history              = ")
            .append("0x")
            .append(HexDump.toHex((short)getHistory()))
            .append(" (").append(getHistory()).append(" )\n");
        buffer.append("         .historyMac               = ").append(isHistoryMac          ()).append('\n');
        buffer.append("         .emptySpecial             = ").append(isEmptySpecial        ()).append('\n');
        buffer.append("         .loadOverrideHist         = ").append(isLoadOverrideHist    ()).append('\n');
        buffer.append("         .featureUndo              = ").append(isFeatureUndo         ()).append('\n');
        buffer.append("         .v97Saved                 = ").append(isV97Saved            ()).append('\n');
        buffer.append("         .spare                    = ").append(isSpare               ()).append('\n');

        buffer.append("    .defaultCharset       = ")
            .append("0x")
            .append(HexDump.toHex((short)getDefaultCharset()))
            .append(" (").append(getDefaultCharset()).append(" )\n");

        buffer.append("    .defaultExtcharset    = ")
            .append("0x")
            .append(HexDump.toHex((short)getDefaultExtcharset()))
            .append(" (").append(getDefaultExtcharset()).append(" )\n");

        buffer.append("    .offsetFirstChar      = ")
            .append("0x")
            .append(HexDump.toHex((int)getOffsetFirstChar()))
            .append(" (").append(getOffsetFirstChar()).append(" )\n");

        buffer.append("    .offsetLastChar       = ")
            .append("0x")
            .append(HexDump.toHex((int)getOffsetLastChar()))
            .append(" (").append(getOffsetLastChar()).append(" )\n");

        buffer.append("    .countShorts          = ")
            .append("0x")
            .append(HexDump.toHex((short)getCountShorts()))
            .append(" (").append(getCountShorts()).append(" )\n");

        buffer.append("    .creatorIdOrBegShorts = ")
            .append("0x")
            .append(HexDump.toHex((short)getCreatorIdOrBegShorts()))
            .append(" (").append(getCreatorIdOrBegShorts()).append(" )\n");

        buffer.append("    .revisorId            = ")
            .append("0x")
            .append(HexDump.toHex((short)getRevisorId()))
            .append(" (").append(getRevisorId()).append(" )\n");

        buffer.append("    .creatorPrivate       = ")
            .append("0x")
            .append(HexDump.toHex((short)getCreatorPrivate()))
            .append(" (").append(getCreatorPrivate()).append(" )\n");

        buffer.append("    .revisorPrivate       = ")
            .append("0x")
            .append(HexDump.toHex((short)getRevisorPrivate()))
            .append(" (").append(getRevisorPrivate()).append(" )\n");

        buffer.append("    .unused               = ")
            .append("0x")
            .append(HexDump.toHex((short)getUnused()))
            .append(" (").append(getUnused()).append(" )\n");

        buffer.append("    .unused               = ")
            .append("0x")
            .append(HexDump.toHex((short)getUnused()))
            .append(" (").append(getUnused()).append(" )\n");

        buffer.append("    .unused               = ")
            .append("0x")
            .append(HexDump.toHex((short)getUnused()))
            .append(" (").append(getUnused()).append(" )\n");

        buffer.append("    .unused               = ")
            .append("0x")
            .append(HexDump.toHex((short)getUnused()))
            .append(" (").append(getUnused()).append(" )\n");

        buffer.append("    .unused               = ")
            .append("0x")
            .append(HexDump.toHex((short)getUnused()))
            .append(" (").append(getUnused()).append(" )\n");

        buffer.append("    .unused               = ")
            .append("0x")
            .append(HexDump.toHex((short)getUnused()))
            .append(" (").append(getUnused()).append(" )\n");

        buffer.append("    .unused               = ")
            .append("0x")
            .append(HexDump.toHex((short)getUnused()))
            .append(" (").append(getUnused()).append(" )\n");

        buffer.append("    .unused               = ")
            .append("0x")
            .append(HexDump.toHex((short)getUnused()))
            .append(" (").append(getUnused()).append(" )\n");

        buffer.append("    .unused               = ")
            .append("0x")
            .append(HexDump.toHex((short)getUnused()))
            .append(" (").append(getUnused()).append(" )\n");

        buffer.append("    .fareastid            = ")
            .append("0x")
            .append(HexDump.toHex((short)getFareastid()))
            .append(" (").append(getFareastid()).append(" )\n");

        buffer.append("    .countints            = ")
            .append("0x")
            .append(HexDump.toHex((short)getCountints()))
            .append(" (").append(getCountints()).append(" )\n");

        buffer.append("    .lastByteOrBegInts    = ")
            .append("0x")
            .append(HexDump.toHex((int)getLastByteOrBegInts()))
            .append(" (").append(getLastByteOrBegInts()).append(" )\n");

        buffer.append("    .creatorBuildDate     = ")
            .append("0x")
            .append(HexDump.toHex((int)getCreatorBuildDate()))
            .append(" (").append(getCreatorBuildDate()).append(" )\n");

        buffer.append("    .revisorBuildDate     = ")
            .append("0x")
            .append(HexDump.toHex((int)getRevisorBuildDate()))
            .append(" (").append(getRevisorBuildDate()).append(" )\n");

        buffer.append("    .mainStreamlen        = ")
            .append("0x")
            .append(HexDump.toHex((int)getMainStreamlen()))
            .append(" (").append(getMainStreamlen()).append(" )\n");

        buffer.append("    .footnoteStreamlen    = ")
            .append("0x")
            .append(HexDump.toHex((int)getFootnoteStreamlen()))
            .append(" (").append(getFootnoteStreamlen()).append(" )\n");

        buffer.append("    .headerStreamlen      = ")
            .append("0x")
            .append(HexDump.toHex((int)getHeaderStreamlen()))
            .append(" (").append(getHeaderStreamlen()).append(" )\n");

        buffer.append("    .macroStreamlen       = ")
            .append("0x")
            .append(HexDump.toHex((int)getMacroStreamlen()))
            .append(" (").append(getMacroStreamlen()).append(" )\n");

        buffer.append("    .annotationStreamlen  = ")
            .append("0x")
            .append(HexDump.toHex((int)getAnnotationStreamlen()))
            .append(" (").append(getAnnotationStreamlen()).append(" )\n");

        buffer.append("    .endnoteStreamlen     = ")
            .append("0x")
            .append(HexDump.toHex((int)getEndnoteStreamlen()))
            .append(" (").append(getEndnoteStreamlen()).append(" )\n");

        buffer.append("    .textboxStreamlen     = ")
            .append("0x")
            .append(HexDump.toHex((int)getTextboxStreamlen()))
            .append(" (").append(getTextboxStreamlen()).append(" )\n");

        buffer.append("    .headboxStreamlen     = ")
            .append("0x")
            .append(HexDump.toHex((int)getHeadboxStreamlen()))
            .append(" (").append(getHeadboxStreamlen()).append(" )\n");

        buffer.append("    .ptrToPlcListChp      = ")
            .append("0x")
            .append(HexDump.toHex((int)getPtrToPlcListChp()))
            .append(" (").append(getPtrToPlcListChp()).append(" )\n");

        buffer.append("    .firstChp             = ")
            .append("0x")
            .append(HexDump.toHex((int)getFirstChp()))
            .append(" (").append(getFirstChp()).append(" )\n");

        buffer.append("    .countChps            = ")
            .append("0x")
            .append(HexDump.toHex((int)getCountChps()))
            .append(" (").append(getCountChps()).append(" )\n");

        buffer.append("    .ptrToPlcListPap      = ")
            .append("0x")
            .append(HexDump.toHex((int)getPtrToPlcListPap()))
            .append(" (").append(getPtrToPlcListPap()).append(" )\n");

        buffer.append("    .firstPap             = ")
            .append("0x")
            .append(HexDump.toHex((int)getFirstPap()))
            .append(" (").append(getFirstPap()).append(" )\n");

        buffer.append("    .countPaps            = ")
            .append("0x")
            .append(HexDump.toHex((int)getCountPaps()))
            .append(" (").append(getCountPaps()).append(" )\n");

        buffer.append("    .ptrToPlcListLvc      = ")
            .append("0x")
            .append(HexDump.toHex((int)getPtrToPlcListLvc()))
            .append(" (").append(getPtrToPlcListLvc()).append(" )\n");

        buffer.append("    .firstLvc             = ")
            .append("0x")
            .append(HexDump.toHex((int)getFirstLvc()))
            .append(" (").append(getFirstLvc()).append(" )\n");

        buffer.append("    .countLvc             = ")
            .append("0x")
            .append(HexDump.toHex((int)getCountLvc()))
            .append(" (").append(getCountLvc()).append(" )\n");

        buffer.append("    .unknown              = ")
            .append("0x")
            .append(HexDump.toHex((int)getUnknown()))
            .append(" (").append(getUnknown()).append(" )\n");

        buffer.append("    .unknown              = ")
            .append("0x")
            .append(HexDump.toHex((int)getUnknown()))
            .append(" (").append(getUnknown()).append(" )\n");

        buffer.append("    .lcbArraySize         = ")
            .append("0x")
            .append(HexDump.toHex((short)getLcbArraySize()))
            .append(" (").append(getLcbArraySize()).append(" )\n");

        buffer.append("    .originalStylesheetOffset = ")
            .append("0x")
            .append(HexDump.toHex((int)getOriginalStylesheetOffset()))
            .append(" (").append(getOriginalStylesheetOffset()).append(" )\n");

        buffer.append("    .originalStylesheetSize = ")
            .append("0x")
            .append(HexDump.toHex((int)getOriginalStylesheetSize()))
            .append(" (").append(getOriginalStylesheetSize()).append(" )\n");

        buffer.append("    .stylesheetOffset     = ")
            .append("0x")
            .append(HexDump.toHex((int)getStylesheetOffset()))
            .append(" (").append(getStylesheetOffset()).append(" )\n");

        buffer.append("    .stylesheetSize       = ")
            .append("0x")
            .append(HexDump.toHex((int)getStylesheetSize()))
            .append(" (").append(getStylesheetSize()).append(" )\n");

        buffer.append("    .footnoteRefOffset    = ")
            .append("0x")
            .append(HexDump.toHex((int)getFootnoteRefOffset()))
            .append(" (").append(getFootnoteRefOffset()).append(" )\n");

        buffer.append("    .footnoteRefSize      = ")
            .append("0x")
            .append(HexDump.toHex((int)getFootnoteRefSize()))
            .append(" (").append(getFootnoteRefSize()).append(" )\n");

        buffer.append("    .plcOffset            = ")
            .append("0x")
            .append(HexDump.toHex((int)getPlcOffset()))
            .append(" (").append(getPlcOffset()).append(" )\n");

        buffer.append("    .plcSize              = ")
            .append("0x")
            .append(HexDump.toHex((int)getPlcSize()))
            .append(" (").append(getPlcSize()).append(" )\n");

        buffer.append("    .annotationRefOffset  = ")
            .append("0x")
            .append(HexDump.toHex((int)getAnnotationRefOffset()))
            .append(" (").append(getAnnotationRefOffset()).append(" )\n");

        buffer.append("    .annotationRefSize    = ")
            .append("0x")
            .append(HexDump.toHex((int)getAnnotationRefSize()))
            .append(" (").append(getAnnotationRefSize()).append(" )\n");

        buffer.append("    .annotationPlcOffset  = ")
            .append("0x")
            .append(HexDump.toHex((int)getAnnotationPlcOffset()))
            .append(" (").append(getAnnotationPlcOffset()).append(" )\n");

        buffer.append("    .annotationPlcSize    = ")
            .append("0x")
            .append(HexDump.toHex((int)getAnnotationPlcSize()))
            .append(" (").append(getAnnotationPlcSize()).append(" )\n");

        buffer.append("    .sectionPlcOffset     = ")
            .append("0x")
            .append(HexDump.toHex((int)getSectionPlcOffset()))
            .append(" (").append(getSectionPlcOffset()).append(" )\n");

        buffer.append("    .sectionPlcSize       = ")
            .append("0x")
            .append(HexDump.toHex((int)getSectionPlcSize()))
            .append(" (").append(getSectionPlcSize()).append(" )\n");

        buffer.append("    .unused               = ")
            .append("0x")
            .append(HexDump.toHex((int)getUnused()))
            .append(" (").append(getUnused()).append(" )\n");

        buffer.append("    .unused               = ")
            .append("0x")
            .append(HexDump.toHex((int)getUnused()))
            .append(" (").append(getUnused()).append(" )\n");

        buffer.append("    .pheplcOffset         = ")
            .append("0x")
            .append(HexDump.toHex((int)getPheplcOffset()))
            .append(" (").append(getPheplcOffset()).append(" )\n");

        buffer.append("    .pheplcSize           = ")
            .append("0x")
            .append(HexDump.toHex((int)getPheplcSize()))
            .append(" (").append(getPheplcSize()).append(" )\n");

        buffer.append("    .glossarySTOffset     = ")
            .append("0x")
            .append(HexDump.toHex((int)getGlossarySTOffset()))
            .append(" (").append(getGlossarySTOffset()).append(" )\n");

        buffer.append("    .glossarySTSize       = ")
            .append("0x")
            .append(HexDump.toHex((int)getGlossarySTSize()))
            .append(" (").append(getGlossarySTSize()).append(" )\n");

        buffer.append("    .glossaryPLCOffset    = ")
            .append("0x")
            .append(HexDump.toHex((int)getGlossaryPLCOffset()))
            .append(" (").append(getGlossaryPLCOffset()).append(" )\n");

        buffer.append("    .glossaryPLCSize      = ")
            .append("0x")
            .append(HexDump.toHex((int)getGlossaryPLCSize()))
            .append(" (").append(getGlossaryPLCSize()).append(" )\n");

        buffer.append("    .headerPLCOffset      = ")
            .append("0x")
            .append(HexDump.toHex((int)getHeaderPLCOffset()))
            .append(" (").append(getHeaderPLCOffset()).append(" )\n");

        buffer.append("    .headerPLCSize        = ")
            .append("0x")
            .append(HexDump.toHex((int)getHeaderPLCSize()))
            .append(" (").append(getHeaderPLCSize()).append(" )\n");

        buffer.append("    .chp_bin_table_offset = ")
            .append("0x")
            .append(HexDump.toHex((int)getChp_bin_table_offset()))
            .append(" (").append(getChp_bin_table_offset()).append(" )\n");

        buffer.append("    .chp_bin_table_size   = ")
            .append("0x")
            .append(HexDump.toHex((int)getChp_bin_table_size()))
            .append(" (").append(getChp_bin_table_size()).append(" )\n");

        buffer.append("    .pap_bin_table_offset = ")
            .append("0x")
            .append(HexDump.toHex((int)getPap_bin_table_offset()))
            .append(" (").append(getPap_bin_table_offset()).append(" )\n");

        buffer.append("    .pap_bin_table_size   = ")
            .append("0x")
            .append(HexDump.toHex((int)getPap_bin_table_size()))
            .append(" (").append(getPap_bin_table_size()).append(" )\n");

        buffer.append("    .sea_bin_table_offset = ")
            .append("0x")
            .append(HexDump.toHex((int)getSea_bin_table_offset()))
            .append(" (").append(getSea_bin_table_offset()).append(" )\n");

        buffer.append("    .sea_bin_table_size   = ")
            .append("0x")
            .append(HexDump.toHex((int)getSea_bin_table_size()))
            .append(" (").append(getSea_bin_table_size()).append(" )\n");

        buffer.append("    .fonts_bin_table_offset = ")
            .append("0x")
            .append(HexDump.toHex((int)getFonts_bin_table_offset()))
            .append(" (").append(getFonts_bin_table_offset()).append(" )\n");

        buffer.append("    .fonts_bin_table_size = ")
            .append("0x")
            .append(HexDump.toHex((int)getFonts_bin_table_size()))
            .append(" (").append(getFonts_bin_table_size()).append(" )\n");

        buffer.append("    .main_fields_offset   = ")
            .append("0x")
            .append(HexDump.toHex((int)getMain_fields_offset()))
            .append(" (").append(getMain_fields_offset()).append(" )\n");

        buffer.append("    .main_fields_size     = ")
            .append("0x")
            .append(HexDump.toHex((int)getMain_fields_size()))
            .append(" (").append(getMain_fields_size()).append(" )\n");

        buffer.append("    .header_fields_offset = ")
            .append("0x")
            .append(HexDump.toHex((int)getHeader_fields_offset()))
            .append(" (").append(getHeader_fields_offset()).append(" )\n");

        buffer.append("    .header_fields_size   = ")
            .append("0x")
            .append(HexDump.toHex((int)getHeader_fields_size()))
            .append(" (").append(getHeader_fields_size()).append(" )\n");

        buffer.append("    .footnote_fields_offset = ")
            .append("0x")
            .append(HexDump.toHex((int)getFootnote_fields_offset()))
            .append(" (").append(getFootnote_fields_offset()).append(" )\n");

        buffer.append("    .footnote_fields_size = ")
            .append("0x")
            .append(HexDump.toHex((int)getFootnote_fields_size()))
            .append(" (").append(getFootnote_fields_size()).append(" )\n");

        buffer.append("    .ann_fields_offset    = ")
            .append("0x")
            .append(HexDump.toHex((int)getAnn_fields_offset()))
            .append(" (").append(getAnn_fields_offset()).append(" )\n");

        buffer.append("    .ann_fields_size      = ")
            .append("0x")
            .append(HexDump.toHex((int)getAnn_fields_size()))
            .append(" (").append(getAnn_fields_size()).append(" )\n");

        buffer.append("    .unused               = ")
            .append("0x")
            .append(HexDump.toHex((int)getUnused()))
            .append(" (").append(getUnused()).append(" )\n");

        buffer.append("    .unused               = ")
            .append("0x")
            .append(HexDump.toHex((int)getUnused()))
            .append(" (").append(getUnused()).append(" )\n");

        buffer.append("    .bookmark_names_offset = ")
            .append("0x")
            .append(HexDump.toHex((int)getBookmark_names_offset()))
            .append(" (").append(getBookmark_names_offset()).append(" )\n");

        buffer.append("    .bookmark_names_size  = ")
            .append("0x")
            .append(HexDump.toHex((int)getBookmark_names_size()))
            .append(" (").append(getBookmark_names_size()).append(" )\n");

        buffer.append("    .bookmark_offsets_offset = ")
            .append("0x")
            .append(HexDump.toHex((int)getBookmark_offsets_offset()))
            .append(" (").append(getBookmark_offsets_offset()).append(" )\n");

        buffer.append("    .bookmark_offsets_size = ")
            .append("0x")
            .append(HexDump.toHex((int)getBookmark_offsets_size()))
            .append(" (").append(getBookmark_offsets_size()).append(" )\n");

        buffer.append("    .macros_offset        = ")
            .append("0x")
            .append(HexDump.toHex((int)getMacros_offset()))
            .append(" (").append(getMacros_offset()).append(" )\n");

        buffer.append("    .macros_size          = ")
            .append("0x")
            .append(HexDump.toHex((int)getMacros_size()))
            .append(" (").append(getMacros_size()).append(" )\n");

        buffer.append("    .unused               = ")
            .append("0x")
            .append(HexDump.toHex((int)getUnused()))
            .append(" (").append(getUnused()).append(" )\n");

        buffer.append("    .unused               = ")
            .append("0x")
            .append(HexDump.toHex((int)getUnused()))
            .append(" (").append(getUnused()).append(" )\n");

        buffer.append("    .unused               = ")
            .append("0x")
            .append(HexDump.toHex((int)getUnused()))
            .append(" (").append(getUnused()).append(" )\n");

        buffer.append("    .unused               = ")
            .append("0x")
            .append(HexDump.toHex((int)getUnused()))
            .append(" (").append(getUnused()).append(" )\n");

        buffer.append("    .printerOffset        = ")
            .append("0x")
            .append(HexDump.toHex((int)getPrinterOffset()))
            .append(" (").append(getPrinterOffset()).append(" )\n");

        buffer.append("    .printerSize          = ")
            .append("0x")
            .append(HexDump.toHex((int)getPrinterSize()))
            .append(" (").append(getPrinterSize()).append(" )\n");

        buffer.append("    .printerPortraitOffset = ")
            .append("0x")
            .append(HexDump.toHex((int)getPrinterPortraitOffset()))
            .append(" (").append(getPrinterPortraitOffset()).append(" )\n");

        buffer.append("    .printerPortraitSize  = ")
            .append("0x")
            .append(HexDump.toHex((int)getPrinterPortraitSize()))
            .append(" (").append(getPrinterPortraitSize()).append(" )\n");

        buffer.append("    .printerLandscapeOffset = ")
            .append("0x")
            .append(HexDump.toHex((int)getPrinterLandscapeOffset()))
            .append(" (").append(getPrinterLandscapeOffset()).append(" )\n");

        buffer.append("    .printerLandscapeSize = ")
            .append("0x")
            .append(HexDump.toHex((int)getPrinterLandscapeSize()))
            .append(" (").append(getPrinterLandscapeSize()).append(" )\n");

        buffer.append("    .wssOffset            = ")
            .append("0x")
            .append(HexDump.toHex((int)getWssOffset()))
            .append(" (").append(getWssOffset()).append(" )\n");

        buffer.append("    .wssSize              = ")
            .append("0x")
            .append(HexDump.toHex((int)getWssSize()))
            .append(" (").append(getWssSize()).append(" )\n");

        buffer.append("    .DOPOffset            = ")
            .append("0x")
            .append(HexDump.toHex((int)getDOPOffset()))
            .append(" (").append(getDOPOffset()).append(" )\n");

        buffer.append("    .DOPSize              = ")
            .append("0x")
            .append(HexDump.toHex((int)getDOPSize()))
            .append(" (").append(getDOPSize()).append(" )\n");

        buffer.append("    .sttbfassoc_offset    = ")
            .append("0x")
            .append(HexDump.toHex((int)getSttbfassoc_offset()))
            .append(" (").append(getSttbfassoc_offset()).append(" )\n");

        buffer.append("    .sttbfassoc_size      = ")
            .append("0x")
            .append(HexDump.toHex((int)getSttbfassoc_size()))
            .append(" (").append(getSttbfassoc_size()).append(" )\n");

        buffer.append("    .textPieceTableOffset = ")
            .append("0x")
            .append(HexDump.toHex((int)getTextPieceTableOffset()))
            .append(" (").append(getTextPieceTableOffset()).append(" )\n");

        buffer.append("    .textPieceTableSize   = ")
            .append("0x")
            .append(HexDump.toHex((int)getTextPieceTableSize()))
            .append(" (").append(getTextPieceTableSize()).append(" )\n");

        buffer.append("    .unused               = ")
            .append("0x")
            .append(HexDump.toHex((int)getUnused()))
            .append(" (").append(getUnused()).append(" )\n");

        buffer.append("    .unused               = ")
            .append("0x")
            .append(HexDump.toHex((int)getUnused()))
            .append(" (").append(getUnused()).append(" )\n");

        buffer.append("    .offsetAutosaveSource = ")
            .append("0x")
            .append(HexDump.toHex((int)getOffsetAutosaveSource()))
            .append(" (").append(getOffsetAutosaveSource()).append(" )\n");

        buffer.append("    .countAutosaveSource  = ")
            .append("0x")
            .append(HexDump.toHex((int)getCountAutosaveSource()))
            .append(" (").append(getCountAutosaveSource()).append(" )\n");

        buffer.append("    .offsetGrpXstAtnOwners = ")
            .append("0x")
            .append(HexDump.toHex((int)getOffsetGrpXstAtnOwners()))
            .append(" (").append(getOffsetGrpXstAtnOwners()).append(" )\n");

        buffer.append("    .countGrpXstAtnOwners = ")
            .append("0x")
            .append(HexDump.toHex((int)getCountGrpXstAtnOwners()))
            .append(" (").append(getCountGrpXstAtnOwners()).append(" )\n");

        buffer.append("    .offsetSttbfAtnbkmk   = ")
            .append("0x")
            .append(HexDump.toHex((int)getOffsetSttbfAtnbkmk()))
            .append(" (").append(getOffsetSttbfAtnbkmk()).append(" )\n");

        buffer.append("    .lengthSttbfAtnbkmk   = ")
            .append("0x")
            .append(HexDump.toHex((int)getLengthSttbfAtnbkmk()))
            .append(" (").append(getLengthSttbfAtnbkmk()).append(" )\n");

        buffer.append("    .unused               = ")
            .append("0x")
            .append(HexDump.toHex((int)getUnused()))
            .append(" (").append(getUnused()).append(" )\n");

        buffer.append("    .unused               = ")
            .append("0x")
            .append(HexDump.toHex((int)getUnused()))
            .append(" (").append(getUnused()).append(" )\n");

        buffer.append("    .unused               = ")
            .append("0x")
            .append(HexDump.toHex((int)getUnused()))
            .append(" (").append(getUnused()).append(" )\n");

        buffer.append("    .unused               = ")
            .append("0x")
            .append(HexDump.toHex((int)getUnused()))
            .append(" (").append(getUnused()).append(" )\n");

        buffer.append("    .offsetPlcspaMom      = ")
            .append("0x")
            .append(HexDump.toHex((int)getOffsetPlcspaMom()))
            .append(" (").append(getOffsetPlcspaMom()).append(" )\n");

        buffer.append("    .lengthPlcspaMom      = ")
            .append("0x")
            .append(HexDump.toHex((int)getLengthPlcspaMom()))
            .append(" (").append(getLengthPlcspaMom()).append(" )\n");

        buffer.append("    .offsetPlcspaHdr      = ")
            .append("0x")
            .append(HexDump.toHex((int)getOffsetPlcspaHdr()))
            .append(" (").append(getOffsetPlcspaHdr()).append(" )\n");

        buffer.append("    .lengthPlcspaHdr      = ")
            .append("0x")
            .append(HexDump.toHex((int)getLengthPlcspaHdr()))
            .append(" (").append(getLengthPlcspaHdr()).append(" )\n");

        buffer.append("    .lengthPlcfBookmarkFirst = ")
            .append("0x")
            .append(HexDump.toHex((int)getLengthPlcfBookmarkFirst()))
            .append(" (").append(getLengthPlcfBookmarkFirst()).append(" )\n");

        buffer.append("    .offsetPlcfBookmarkFirst = ")
            .append("0x")
            .append(HexDump.toHex((int)getOffsetPlcfBookmarkFirst()))
            .append(" (").append(getOffsetPlcfBookmarkFirst()).append(" )\n");

        buffer.append("[/FIB]\n");
        return buffer.toString();
    }

    /**
     * Size of record (exluding 4 byte header)
     */
    public int getSize()
    {
        return 4 + 2 + 2 + 2 + 2 + 2 + 2 + 2 + 2 + 2 + 2 + 2 + 2 + 4 + 4 + 2 + 2 + 2 + 2 + 2 + 2 + 2 + 2 + 2 + 2 + 2 + 2 + 2 + 2 + 2 + 2 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 2 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4 + 4;
    }



    /**
     * Get the id field for the FIB record.
     */
    public short getId()
    {
        return field_1_id;
    }

    /**
     * Set the id field for the FIB record.
     */
    public void setId(short field_1_id)
    {
        this.field_1_id = field_1_id;
    }

    /**
     * Get the version field for the FIB record.
     */
    public short getVersion()
    {
        return field_2_version;
    }

    /**
     * Set the version field for the FIB record.
     */
    public void setVersion(short field_2_version)
    {
        this.field_2_version = field_2_version;
    }

    /**
     * Get the product version field for the FIB record.
     */
    public short getProductVersion()
    {
        return field_3_productVersion;
    }

    /**
     * Set the product version field for the FIB record.
     */
    public void setProductVersion(short field_3_productVersion)
    {
        this.field_3_productVersion = field_3_productVersion;
    }

    /**
     * Get the language stamp field for the FIB record.
     */
    public short getLanguageStamp()
    {
        return field_4_languageStamp;
    }

    /**
     * Set the language stamp field for the FIB record.
     */
    public void setLanguageStamp(short field_4_languageStamp)
    {
        this.field_4_languageStamp = field_4_languageStamp;
    }

    /**
     * Get the unknown 1 field for the FIB record.
     */
    public short getUnknown1()
    {
        return field_5_unknown1;
    }

    /**
     * Set the unknown 1 field for the FIB record.
     */
    public void setUnknown1(short field_5_unknown1)
    {
        this.field_5_unknown1 = field_5_unknown1;
    }

    /**
     * Get the options field for the FIB record.
     */
    public short getOptions()
    {
        return field_6_options;
    }

    /**
     * Set the options field for the FIB record.
     */
    public void setOptions(short field_6_options)
    {
        this.field_6_options = field_6_options;
    }

    /**
     * Get the minversion field for the FIB record.
     */
    public short getMinversion()
    {
        return field_7_minversion;
    }

    /**
     * Set the minversion field for the FIB record.
     */
    public void setMinversion(short field_7_minversion)
    {
        this.field_7_minversion = field_7_minversion;
    }

    /**
     * Get the encrypted key field for the FIB record.
     */
    public short getEncryptedKey()
    {
        return field_8_encryptedKey;
    }

    /**
     * Set the encrypted key field for the FIB record.
     */
    public void setEncryptedKey(short field_8_encryptedKey)
    {
        this.field_8_encryptedKey = field_8_encryptedKey;
    }

    /**
     * Get the environment field for the FIB record.
     */
    public short getEnvironment()
    {
        return field_9_environment;
    }

    /**
     * Set the environment field for the FIB record.
     */
    public void setEnvironment(short field_9_environment)
    {
        this.field_9_environment = field_9_environment;
    }

    /**
     * Get the history field for the FIB record.
     */
    public short getHistory()
    {
        return field_10_history;
    }

    /**
     * Set the history field for the FIB record.
     */
    public void setHistory(short field_10_history)
    {
        this.field_10_history = field_10_history;
    }

    /**
     * Get the default charset field for the FIB record.
     */
    public short getDefaultCharset()
    {
        return field_11_defaultCharset;
    }

    /**
     * Set the default charset field for the FIB record.
     */
    public void setDefaultCharset(short field_11_defaultCharset)
    {
        this.field_11_defaultCharset = field_11_defaultCharset;
    }

    /**
     * Get the default extcharset field for the FIB record.
     */
    public short getDefaultExtcharset()
    {
        return field_12_defaultExtcharset;
    }

    /**
     * Set the default extcharset field for the FIB record.
     */
    public void setDefaultExtcharset(short field_12_defaultExtcharset)
    {
        this.field_12_defaultExtcharset = field_12_defaultExtcharset;
    }

    /**
     * Get the offset first char field for the FIB record.
     */
    public int getOffsetFirstChar()
    {
        return field_13_offsetFirstChar;
    }

    /**
     * Set the offset first char field for the FIB record.
     */
    public void setOffsetFirstChar(int field_13_offsetFirstChar)
    {
        this.field_13_offsetFirstChar = field_13_offsetFirstChar;
    }

    /**
     * Get the offset last char field for the FIB record.
     */
    public int getOffsetLastChar()
    {
        return field_14_offsetLastChar;
    }

    /**
     * Set the offset last char field for the FIB record.
     */
    public void setOffsetLastChar(int field_14_offsetLastChar)
    {
        this.field_14_offsetLastChar = field_14_offsetLastChar;
    }

    /**
     * Get the count shorts field for the FIB record.
     */
    public short getCountShorts()
    {
        return field_15_countShorts;
    }

    /**
     * Set the count shorts field for the FIB record.
     */
    public void setCountShorts(short field_15_countShorts)
    {
        this.field_15_countShorts = field_15_countShorts;
    }

    /**
     * Get the creator id or beg shorts field for the FIB record.
     */
    public short getCreatorIdOrBegShorts()
    {
        return field_16_creatorIdOrBegShorts;
    }

    /**
     * Set the creator id or beg shorts field for the FIB record.
     */
    public void setCreatorIdOrBegShorts(short field_16_creatorIdOrBegShorts)
    {
        this.field_16_creatorIdOrBegShorts = field_16_creatorIdOrBegShorts;
    }

    /**
     * Get the revisor id field for the FIB record.
     */
    public short getRevisorId()
    {
        return field_17_revisorId;
    }

    /**
     * Set the revisor id field for the FIB record.
     */
    public void setRevisorId(short field_17_revisorId)
    {
        this.field_17_revisorId = field_17_revisorId;
    }

    /**
     * Get the creator private field for the FIB record.
     */
    public short getCreatorPrivate()
    {
        return field_18_creatorPrivate;
    }

    /**
     * Set the creator private field for the FIB record.
     */
    public void setCreatorPrivate(short field_18_creatorPrivate)
    {
        this.field_18_creatorPrivate = field_18_creatorPrivate;
    }

    /**
     * Get the revisor private field for the FIB record.
     */
    public short getRevisorPrivate()
    {
        return field_19_revisorPrivate;
    }

    /**
     * Set the revisor private field for the FIB record.
     */
    public void setRevisorPrivate(short field_19_revisorPrivate)
    {
        this.field_19_revisorPrivate = field_19_revisorPrivate;
    }

    /**
     * Get the unused field for the FIB record.
     */
    public short getUnused()
    {
        return field_20_unused;
    }

    /**
     * Set the unused field for the FIB record.
     */
    public void setUnused(short field_20_unused)
    {
        this.field_20_unused = field_20_unused;
    }

    /**
     * Get the unused field for the FIB record.
     */
    public short getUnused()
    {
        return field_21_unused;
    }

    /**
     * Set the unused field for the FIB record.
     */
    public void setUnused(short field_21_unused)
    {
        this.field_21_unused = field_21_unused;
    }

    /**
     * Get the unused field for the FIB record.
     */
    public short getUnused()
    {
        return field_22_unused;
    }

    /**
     * Set the unused field for the FIB record.
     */
    public void setUnused(short field_22_unused)
    {
        this.field_22_unused = field_22_unused;
    }

    /**
     * Get the unused field for the FIB record.
     */
    public short getUnused()
    {
        return field_23_unused;
    }

    /**
     * Set the unused field for the FIB record.
     */
    public void setUnused(short field_23_unused)
    {
        this.field_23_unused = field_23_unused;
    }

    /**
     * Get the unused field for the FIB record.
     */
    public short getUnused()
    {
        return field_24_unused;
    }

    /**
     * Set the unused field for the FIB record.
     */
    public void setUnused(short field_24_unused)
    {
        this.field_24_unused = field_24_unused;
    }

    /**
     * Get the unused field for the FIB record.
     */
    public short getUnused()
    {
        return field_25_unused;
    }

    /**
     * Set the unused field for the FIB record.
     */
    public void setUnused(short field_25_unused)
    {
        this.field_25_unused = field_25_unused;
    }

    /**
     * Get the unused field for the FIB record.
     */
    public short getUnused()
    {
        return field_26_unused;
    }

    /**
     * Set the unused field for the FIB record.
     */
    public void setUnused(short field_26_unused)
    {
        this.field_26_unused = field_26_unused;
    }

    /**
     * Get the unused field for the FIB record.
     */
    public short getUnused()
    {
        return field_27_unused;
    }

    /**
     * Set the unused field for the FIB record.
     */
    public void setUnused(short field_27_unused)
    {
        this.field_27_unused = field_27_unused;
    }

    /**
     * Get the unused field for the FIB record.
     */
    public short getUnused()
    {
        return field_28_unused;
    }

    /**
     * Set the unused field for the FIB record.
     */
    public void setUnused(short field_28_unused)
    {
        this.field_28_unused = field_28_unused;
    }

    /**
     * Get the fareastid field for the FIB record.
     */
    public short getFareastid()
    {
        return field_29_fareastid;
    }

    /**
     * Set the fareastid field for the FIB record.
     */
    public void setFareastid(short field_29_fareastid)
    {
        this.field_29_fareastid = field_29_fareastid;
    }

    /**
     * Get the countints field for the FIB record.
     */
    public short getCountints()
    {
        return field_30_countints;
    }

    /**
     * Set the countints field for the FIB record.
     */
    public void setCountints(short field_30_countints)
    {
        this.field_30_countints = field_30_countints;
    }

    /**
     * Get the last byte or beg ints field for the FIB record.
     */
    public int getLastByteOrBegInts()
    {
        return field_31_lastByteOrBegInts;
    }

    /**
     * Set the last byte or beg ints field for the FIB record.
     */
    public void setLastByteOrBegInts(int field_31_lastByteOrBegInts)
    {
        this.field_31_lastByteOrBegInts = field_31_lastByteOrBegInts;
    }

    /**
     * Get the creator build date field for the FIB record.
     */
    public int getCreatorBuildDate()
    {
        return field_32_creatorBuildDate;
    }

    /**
     * Set the creator build date field for the FIB record.
     */
    public void setCreatorBuildDate(int field_32_creatorBuildDate)
    {
        this.field_32_creatorBuildDate = field_32_creatorBuildDate;
    }

    /**
     * Get the revisor build date field for the FIB record.
     */
    public int getRevisorBuildDate()
    {
        return field_33_revisorBuildDate;
    }

    /**
     * Set the revisor build date field for the FIB record.
     */
    public void setRevisorBuildDate(int field_33_revisorBuildDate)
    {
        this.field_33_revisorBuildDate = field_33_revisorBuildDate;
    }

    /**
     * Get the main streamlen field for the FIB record.
     */
    public int getMainStreamlen()
    {
        return field_34_mainStreamlen;
    }

    /**
     * Set the main streamlen field for the FIB record.
     */
    public void setMainStreamlen(int field_34_mainStreamlen)
    {
        this.field_34_mainStreamlen = field_34_mainStreamlen;
    }

    /**
     * Get the footnote streamlen field for the FIB record.
     */
    public int getFootnoteStreamlen()
    {
        return field_35_footnoteStreamlen;
    }

    /**
     * Set the footnote streamlen field for the FIB record.
     */
    public void setFootnoteStreamlen(int field_35_footnoteStreamlen)
    {
        this.field_35_footnoteStreamlen = field_35_footnoteStreamlen;
    }

    /**
     * Get the header streamlen field for the FIB record.
     */
    public int getHeaderStreamlen()
    {
        return field_36_headerStreamlen;
    }

    /**
     * Set the header streamlen field for the FIB record.
     */
    public void setHeaderStreamlen(int field_36_headerStreamlen)
    {
        this.field_36_headerStreamlen = field_36_headerStreamlen;
    }

    /**
     * Get the macro streamlen field for the FIB record.
     */
    public int getMacroStreamlen()
    {
        return field_37_macroStreamlen;
    }

    /**
     * Set the macro streamlen field for the FIB record.
     */
    public void setMacroStreamlen(int field_37_macroStreamlen)
    {
        this.field_37_macroStreamlen = field_37_macroStreamlen;
    }

    /**
     * Get the annotation streamlen field for the FIB record.
     */
    public int getAnnotationStreamlen()
    {
        return field_38_annotationStreamlen;
    }

    /**
     * Set the annotation streamlen field for the FIB record.
     */
    public void setAnnotationStreamlen(int field_38_annotationStreamlen)
    {
        this.field_38_annotationStreamlen = field_38_annotationStreamlen;
    }

    /**
     * Get the endnote streamlen field for the FIB record.
     */
    public int getEndnoteStreamlen()
    {
        return field_39_endnoteStreamlen;
    }

    /**
     * Set the endnote streamlen field for the FIB record.
     */
    public void setEndnoteStreamlen(int field_39_endnoteStreamlen)
    {
        this.field_39_endnoteStreamlen = field_39_endnoteStreamlen;
    }

    /**
     * Get the textbox streamlen field for the FIB record.
     */
    public int getTextboxStreamlen()
    {
        return field_40_textboxStreamlen;
    }

    /**
     * Set the textbox streamlen field for the FIB record.
     */
    public void setTextboxStreamlen(int field_40_textboxStreamlen)
    {
        this.field_40_textboxStreamlen = field_40_textboxStreamlen;
    }

    /**
     * Get the headbox streamlen field for the FIB record.
     */
    public int getHeadboxStreamlen()
    {
        return field_41_headboxStreamlen;
    }

    /**
     * Set the headbox streamlen field for the FIB record.
     */
    public void setHeadboxStreamlen(int field_41_headboxStreamlen)
    {
        this.field_41_headboxStreamlen = field_41_headboxStreamlen;
    }

    /**
     * Get the ptr to plc list chp field for the FIB record.
     */
    public int getPtrToPlcListChp()
    {
        return field_42_ptrToPlcListChp;
    }

    /**
     * Set the ptr to plc list chp field for the FIB record.
     */
    public void setPtrToPlcListChp(int field_42_ptrToPlcListChp)
    {
        this.field_42_ptrToPlcListChp = field_42_ptrToPlcListChp;
    }

    /**
     * Get the first chp field for the FIB record.
     */
    public int getFirstChp()
    {
        return field_43_firstChp;
    }

    /**
     * Set the first chp field for the FIB record.
     */
    public void setFirstChp(int field_43_firstChp)
    {
        this.field_43_firstChp = field_43_firstChp;
    }

    /**
     * Get the count chps field for the FIB record.
     */
    public int getCountChps()
    {
        return field_44_countChps;
    }

    /**
     * Set the count chps field for the FIB record.
     */
    public void setCountChps(int field_44_countChps)
    {
        this.field_44_countChps = field_44_countChps;
    }

    /**
     * Get the ptr to plc list pap field for the FIB record.
     */
    public int getPtrToPlcListPap()
    {
        return field_45_ptrToPlcListPap;
    }

    /**
     * Set the ptr to plc list pap field for the FIB record.
     */
    public void setPtrToPlcListPap(int field_45_ptrToPlcListPap)
    {
        this.field_45_ptrToPlcListPap = field_45_ptrToPlcListPap;
    }

    /**
     * Get the first pap field for the FIB record.
     */
    public int getFirstPap()
    {
        return field_46_firstPap;
    }

    /**
     * Set the first pap field for the FIB record.
     */
    public void setFirstPap(int field_46_firstPap)
    {
        this.field_46_firstPap = field_46_firstPap;
    }

    /**
     * Get the count paps field for the FIB record.
     */
    public int getCountPaps()
    {
        return field_47_countPaps;
    }

    /**
     * Set the count paps field for the FIB record.
     */
    public void setCountPaps(int field_47_countPaps)
    {
        this.field_47_countPaps = field_47_countPaps;
    }

    /**
     * Get the ptr to plc list lvc field for the FIB record.
     */
    public int getPtrToPlcListLvc()
    {
        return field_48_ptrToPlcListLvc;
    }

    /**
     * Set the ptr to plc list lvc field for the FIB record.
     */
    public void setPtrToPlcListLvc(int field_48_ptrToPlcListLvc)
    {
        this.field_48_ptrToPlcListLvc = field_48_ptrToPlcListLvc;
    }

    /**
     * Get the first lvc field for the FIB record.
     */
    public int getFirstLvc()
    {
        return field_49_firstLvc;
    }

    /**
     * Set the first lvc field for the FIB record.
     */
    public void setFirstLvc(int field_49_firstLvc)
    {
        this.field_49_firstLvc = field_49_firstLvc;
    }

    /**
     * Get the count lvc field for the FIB record.
     */
    public int getCountLvc()
    {
        return field_50_countLvc;
    }

    /**
     * Set the count lvc field for the FIB record.
     */
    public void setCountLvc(int field_50_countLvc)
    {
        this.field_50_countLvc = field_50_countLvc;
    }

    /**
     * Get the unknown field for the FIB record.
     */
    public int getUnknown()
    {
        return field_51_unknown;
    }

    /**
     * Set the unknown field for the FIB record.
     */
    public void setUnknown(int field_51_unknown)
    {
        this.field_51_unknown = field_51_unknown;
    }

    /**
     * Get the unknown field for the FIB record.
     */
    public int getUnknown()
    {
        return field_52_unknown;
    }

    /**
     * Set the unknown field for the FIB record.
     */
    public void setUnknown(int field_52_unknown)
    {
        this.field_52_unknown = field_52_unknown;
    }

    /**
     * Get the lcb array size field for the FIB record.
     */
    public short getLcbArraySize()
    {
        return field_53_lcbArraySize;
    }

    /**
     * Set the lcb array size field for the FIB record.
     */
    public void setLcbArraySize(short field_53_lcbArraySize)
    {
        this.field_53_lcbArraySize = field_53_lcbArraySize;
    }

    /**
     * Get the original stylesheet offset field for the FIB record.
     */
    public int getOriginalStylesheetOffset()
    {
        return field_54_originalStylesheetOffset;
    }

    /**
     * Set the original stylesheet offset field for the FIB record.
     */
    public void setOriginalStylesheetOffset(int field_54_originalStylesheetOffset)
    {
        this.field_54_originalStylesheetOffset = field_54_originalStylesheetOffset;
    }

    /**
     * Get the original stylesheet size field for the FIB record.
     */
    public int getOriginalStylesheetSize()
    {
        return field_55_originalStylesheetSize;
    }

    /**
     * Set the original stylesheet size field for the FIB record.
     */
    public void setOriginalStylesheetSize(int field_55_originalStylesheetSize)
    {
        this.field_55_originalStylesheetSize = field_55_originalStylesheetSize;
    }

    /**
     * Get the stylesheet offset field for the FIB record.
     */
    public int getStylesheetOffset()
    {
        return field_56_stylesheetOffset;
    }

    /**
     * Set the stylesheet offset field for the FIB record.
     */
    public void setStylesheetOffset(int field_56_stylesheetOffset)
    {
        this.field_56_stylesheetOffset = field_56_stylesheetOffset;
    }

    /**
     * Get the stylesheet size field for the FIB record.
     */
    public int getStylesheetSize()
    {
        return field_57_stylesheetSize;
    }

    /**
     * Set the stylesheet size field for the FIB record.
     */
    public void setStylesheetSize(int field_57_stylesheetSize)
    {
        this.field_57_stylesheetSize = field_57_stylesheetSize;
    }

    /**
     * Get the footnote ref offset field for the FIB record.
     */
    public int getFootnoteRefOffset()
    {
        return field_58_footnoteRefOffset;
    }

    /**
     * Set the footnote ref offset field for the FIB record.
     */
    public void setFootnoteRefOffset(int field_58_footnoteRefOffset)
    {
        this.field_58_footnoteRefOffset = field_58_footnoteRefOffset;
    }

    /**
     * Get the footnote ref size field for the FIB record.
     */
    public int getFootnoteRefSize()
    {
        return field_59_footnoteRefSize;
    }

    /**
     * Set the footnote ref size field for the FIB record.
     */
    public void setFootnoteRefSize(int field_59_footnoteRefSize)
    {
        this.field_59_footnoteRefSize = field_59_footnoteRefSize;
    }

    /**
     * Get the plc offset field for the FIB record.
     */
    public int getPlcOffset()
    {
        return field_60_plcOffset;
    }

    /**
     * Set the plc offset field for the FIB record.
     */
    public void setPlcOffset(int field_60_plcOffset)
    {
        this.field_60_plcOffset = field_60_plcOffset;
    }

    /**
     * Get the plc size field for the FIB record.
     */
    public int getPlcSize()
    {
        return field_61_plcSize;
    }

    /**
     * Set the plc size field for the FIB record.
     */
    public void setPlcSize(int field_61_plcSize)
    {
        this.field_61_plcSize = field_61_plcSize;
    }

    /**
     * Get the annotation ref offset field for the FIB record.
     */
    public int getAnnotationRefOffset()
    {
        return field_62_annotationRefOffset;
    }

    /**
     * Set the annotation ref offset field for the FIB record.
     */
    public void setAnnotationRefOffset(int field_62_annotationRefOffset)
    {
        this.field_62_annotationRefOffset = field_62_annotationRefOffset;
    }

    /**
     * Get the annotation ref size field for the FIB record.
     */
    public int getAnnotationRefSize()
    {
        return field_63_annotationRefSize;
    }

    /**
     * Set the annotation ref size field for the FIB record.
     */
    public void setAnnotationRefSize(int field_63_annotationRefSize)
    {
        this.field_63_annotationRefSize = field_63_annotationRefSize;
    }

    /**
     * Get the annotation plc offset field for the FIB record.
     */
    public int getAnnotationPlcOffset()
    {
        return field_64_annotationPlcOffset;
    }

    /**
     * Set the annotation plc offset field for the FIB record.
     */
    public void setAnnotationPlcOffset(int field_64_annotationPlcOffset)
    {
        this.field_64_annotationPlcOffset = field_64_annotationPlcOffset;
    }

    /**
     * Get the annotation plc size field for the FIB record.
     */
    public int getAnnotationPlcSize()
    {
        return field_65_annotationPlcSize;
    }

    /**
     * Set the annotation plc size field for the FIB record.
     */
    public void setAnnotationPlcSize(int field_65_annotationPlcSize)
    {
        this.field_65_annotationPlcSize = field_65_annotationPlcSize;
    }

    /**
     * Get the section plc offset field for the FIB record.
     */
    public int getSectionPlcOffset()
    {
        return field_66_sectionPlcOffset;
    }

    /**
     * Set the section plc offset field for the FIB record.
     */
    public void setSectionPlcOffset(int field_66_sectionPlcOffset)
    {
        this.field_66_sectionPlcOffset = field_66_sectionPlcOffset;
    }

    /**
     * Get the section plc size field for the FIB record.
     */
    public int getSectionPlcSize()
    {
        return field_67_sectionPlcSize;
    }

    /**
     * Set the section plc size field for the FIB record.
     */
    public void setSectionPlcSize(int field_67_sectionPlcSize)
    {
        this.field_67_sectionPlcSize = field_67_sectionPlcSize;
    }

    /**
     * Get the unused field for the FIB record.
     */
    public int getUnused()
    {
        return field_68_unused;
    }

    /**
     * Set the unused field for the FIB record.
     */
    public void setUnused(int field_68_unused)
    {
        this.field_68_unused = field_68_unused;
    }

    /**
     * Get the unused field for the FIB record.
     */
    public int getUnused()
    {
        return field_69_unused;
    }

    /**
     * Set the unused field for the FIB record.
     */
    public void setUnused(int field_69_unused)
    {
        this.field_69_unused = field_69_unused;
    }

    /**
     * Get the pheplc offset field for the FIB record.
     */
    public int getPheplcOffset()
    {
        return field_70_pheplcOffset;
    }

    /**
     * Set the pheplc offset field for the FIB record.
     */
    public void setPheplcOffset(int field_70_pheplcOffset)
    {
        this.field_70_pheplcOffset = field_70_pheplcOffset;
    }

    /**
     * Get the pheplc size field for the FIB record.
     */
    public int getPheplcSize()
    {
        return field_71_pheplcSize;
    }

    /**
     * Set the pheplc size field for the FIB record.
     */
    public void setPheplcSize(int field_71_pheplcSize)
    {
        this.field_71_pheplcSize = field_71_pheplcSize;
    }

    /**
     * Get the glossaryST offset field for the FIB record.
     */
    public int getGlossarySTOffset()
    {
        return field_72_glossarySTOffset;
    }

    /**
     * Set the glossaryST offset field for the FIB record.
     */
    public void setGlossarySTOffset(int field_72_glossarySTOffset)
    {
        this.field_72_glossarySTOffset = field_72_glossarySTOffset;
    }

    /**
     * Get the glossaryST size field for the FIB record.
     */
    public int getGlossarySTSize()
    {
        return field_73_glossarySTSize;
    }

    /**
     * Set the glossaryST size field for the FIB record.
     */
    public void setGlossarySTSize(int field_73_glossarySTSize)
    {
        this.field_73_glossarySTSize = field_73_glossarySTSize;
    }

    /**
     * Get the glossaryPLC offset field for the FIB record.
     */
    public int getGlossaryPLCOffset()
    {
        return field_74_glossaryPLCOffset;
    }

    /**
     * Set the glossaryPLC offset field for the FIB record.
     */
    public void setGlossaryPLCOffset(int field_74_glossaryPLCOffset)
    {
        this.field_74_glossaryPLCOffset = field_74_glossaryPLCOffset;
    }

    /**
     * Get the glossaryPLC size field for the FIB record.
     */
    public int getGlossaryPLCSize()
    {
        return field_75_glossaryPLCSize;
    }

    /**
     * Set the glossaryPLC size field for the FIB record.
     */
    public void setGlossaryPLCSize(int field_75_glossaryPLCSize)
    {
        this.field_75_glossaryPLCSize = field_75_glossaryPLCSize;
    }

    /**
     * Get the headerPLC offset field for the FIB record.
     */
    public int getHeaderPLCOffset()
    {
        return field_76_headerPLCOffset;
    }

    /**
     * Set the headerPLC offset field for the FIB record.
     */
    public void setHeaderPLCOffset(int field_76_headerPLCOffset)
    {
        this.field_76_headerPLCOffset = field_76_headerPLCOffset;
    }

    /**
     * Get the headerPLC size field for the FIB record.
     */
    public int getHeaderPLCSize()
    {
        return field_77_headerPLCSize;
    }

    /**
     * Set the headerPLC size field for the FIB record.
     */
    public void setHeaderPLCSize(int field_77_headerPLCSize)
    {
        this.field_77_headerPLCSize = field_77_headerPLCSize;
    }

    /**
     * Get the chp_bin_table_offset field for the FIB record.
     */
    public int getChp_bin_table_offset()
    {
        return field_78_chp_bin_table_offset;
    }

    /**
     * Set the chp_bin_table_offset field for the FIB record.
     */
    public void setChp_bin_table_offset(int field_78_chp_bin_table_offset)
    {
        this.field_78_chp_bin_table_offset = field_78_chp_bin_table_offset;
    }

    /**
     * Get the chp_bin_table_size field for the FIB record.
     */
    public int getChp_bin_table_size()
    {
        return field_79_chp_bin_table_size;
    }

    /**
     * Set the chp_bin_table_size field for the FIB record.
     */
    public void setChp_bin_table_size(int field_79_chp_bin_table_size)
    {
        this.field_79_chp_bin_table_size = field_79_chp_bin_table_size;
    }

    /**
     * Get the pap_bin_table_offset field for the FIB record.
     */
    public int getPap_bin_table_offset()
    {
        return field_80_pap_bin_table_offset;
    }

    /**
     * Set the pap_bin_table_offset field for the FIB record.
     */
    public void setPap_bin_table_offset(int field_80_pap_bin_table_offset)
    {
        this.field_80_pap_bin_table_offset = field_80_pap_bin_table_offset;
    }

    /**
     * Get the pap_bin_table_size field for the FIB record.
     */
    public int getPap_bin_table_size()
    {
        return field_81_pap_bin_table_size;
    }

    /**
     * Set the pap_bin_table_size field for the FIB record.
     */
    public void setPap_bin_table_size(int field_81_pap_bin_table_size)
    {
        this.field_81_pap_bin_table_size = field_81_pap_bin_table_size;
    }

    /**
     * Get the sea_bin_table_offset field for the FIB record.
     */
    public int getSea_bin_table_offset()
    {
        return field_82_sea_bin_table_offset;
    }

    /**
     * Set the sea_bin_table_offset field for the FIB record.
     */
    public void setSea_bin_table_offset(int field_82_sea_bin_table_offset)
    {
        this.field_82_sea_bin_table_offset = field_82_sea_bin_table_offset;
    }

    /**
     * Get the sea_bin_table_size field for the FIB record.
     */
    public int getSea_bin_table_size()
    {
        return field_83_sea_bin_table_size;
    }

    /**
     * Set the sea_bin_table_size field for the FIB record.
     */
    public void setSea_bin_table_size(int field_83_sea_bin_table_size)
    {
        this.field_83_sea_bin_table_size = field_83_sea_bin_table_size;
    }

    /**
     * Get the fonts_bin_table_offset field for the FIB record.
     */
    public int getFonts_bin_table_offset()
    {
        return field_84_fonts_bin_table_offset;
    }

    /**
     * Set the fonts_bin_table_offset field for the FIB record.
     */
    public void setFonts_bin_table_offset(int field_84_fonts_bin_table_offset)
    {
        this.field_84_fonts_bin_table_offset = field_84_fonts_bin_table_offset;
    }

    /**
     * Get the fonts_bin_table_size field for the FIB record.
     */
    public int getFonts_bin_table_size()
    {
        return field_85_fonts_bin_table_size;
    }

    /**
     * Set the fonts_bin_table_size field for the FIB record.
     */
    public void setFonts_bin_table_size(int field_85_fonts_bin_table_size)
    {
        this.field_85_fonts_bin_table_size = field_85_fonts_bin_table_size;
    }

    /**
     * Get the main_fields_offset field for the FIB record.
     */
    public int getMain_fields_offset()
    {
        return field_86_main_fields_offset;
    }

    /**
     * Set the main_fields_offset field for the FIB record.
     */
    public void setMain_fields_offset(int field_86_main_fields_offset)
    {
        this.field_86_main_fields_offset = field_86_main_fields_offset;
    }

    /**
     * Get the main_fields_size field for the FIB record.
     */
    public int getMain_fields_size()
    {
        return field_87_main_fields_size;
    }

    /**
     * Set the main_fields_size field for the FIB record.
     */
    public void setMain_fields_size(int field_87_main_fields_size)
    {
        this.field_87_main_fields_size = field_87_main_fields_size;
    }

    /**
     * Get the header_fields_offset field for the FIB record.
     */
    public int getHeader_fields_offset()
    {
        return field_88_header_fields_offset;
    }

    /**
     * Set the header_fields_offset field for the FIB record.
     */
    public void setHeader_fields_offset(int field_88_header_fields_offset)
    {
        this.field_88_header_fields_offset = field_88_header_fields_offset;
    }

    /**
     * Get the header_fields_size field for the FIB record.
     */
    public int getHeader_fields_size()
    {
        return field_89_header_fields_size;
    }

    /**
     * Set the header_fields_size field for the FIB record.
     */
    public void setHeader_fields_size(int field_89_header_fields_size)
    {
        this.field_89_header_fields_size = field_89_header_fields_size;
    }

    /**
     * Get the footnote_fields_offset field for the FIB record.
     */
    public int getFootnote_fields_offset()
    {
        return field_90_footnote_fields_offset;
    }

    /**
     * Set the footnote_fields_offset field for the FIB record.
     */
    public void setFootnote_fields_offset(int field_90_footnote_fields_offset)
    {
        this.field_90_footnote_fields_offset = field_90_footnote_fields_offset;
    }

    /**
     * Get the footnote_fields_size field for the FIB record.
     */
    public int getFootnote_fields_size()
    {
        return field_91_footnote_fields_size;
    }

    /**
     * Set the footnote_fields_size field for the FIB record.
     */
    public void setFootnote_fields_size(int field_91_footnote_fields_size)
    {
        this.field_91_footnote_fields_size = field_91_footnote_fields_size;
    }

    /**
     * Get the ann_fields_offset field for the FIB record.
     */
    public int getAnn_fields_offset()
    {
        return field_92_ann_fields_offset;
    }

    /**
     * Set the ann_fields_offset field for the FIB record.
     */
    public void setAnn_fields_offset(int field_92_ann_fields_offset)
    {
        this.field_92_ann_fields_offset = field_92_ann_fields_offset;
    }

    /**
     * Get the ann_fields_size field for the FIB record.
     */
    public int getAnn_fields_size()
    {
        return field_93_ann_fields_size;
    }

    /**
     * Set the ann_fields_size field for the FIB record.
     */
    public void setAnn_fields_size(int field_93_ann_fields_size)
    {
        this.field_93_ann_fields_size = field_93_ann_fields_size;
    }

    /**
     * Get the unused field for the FIB record.
     */
    public int getUnused()
    {
        return field_94_unused;
    }

    /**
     * Set the unused field for the FIB record.
     */
    public void setUnused(int field_94_unused)
    {
        this.field_94_unused = field_94_unused;
    }

    /**
     * Get the unused field for the FIB record.
     */
    public int getUnused()
    {
        return field_95_unused;
    }

    /**
     * Set the unused field for the FIB record.
     */
    public void setUnused(int field_95_unused)
    {
        this.field_95_unused = field_95_unused;
    }

    /**
     * Get the bookmark_names_offset field for the FIB record.
     */
    public int getBookmark_names_offset()
    {
        return field_96_bookmark_names_offset;
    }

    /**
     * Set the bookmark_names_offset field for the FIB record.
     */
    public void setBookmark_names_offset(int field_96_bookmark_names_offset)
    {
        this.field_96_bookmark_names_offset = field_96_bookmark_names_offset;
    }

    /**
     * Get the bookmark_names_size field for the FIB record.
     */
    public int getBookmark_names_size()
    {
        return field_97_bookmark_names_size;
    }

    /**
     * Set the bookmark_names_size field for the FIB record.
     */
    public void setBookmark_names_size(int field_97_bookmark_names_size)
    {
        this.field_97_bookmark_names_size = field_97_bookmark_names_size;
    }

    /**
     * Get the bookmark_offsets_offset field for the FIB record.
     */
    public int getBookmark_offsets_offset()
    {
        return field_98_bookmark_offsets_offset;
    }

    /**
     * Set the bookmark_offsets_offset field for the FIB record.
     */
    public void setBookmark_offsets_offset(int field_98_bookmark_offsets_offset)
    {
        this.field_98_bookmark_offsets_offset = field_98_bookmark_offsets_offset;
    }

    /**
     * Get the bookmark_offsets_size field for the FIB record.
     */
    public int getBookmark_offsets_size()
    {
        return field_99_bookmark_offsets_size;
    }

    /**
     * Set the bookmark_offsets_size field for the FIB record.
     */
    public void setBookmark_offsets_size(int field_99_bookmark_offsets_size)
    {
        this.field_99_bookmark_offsets_size = field_99_bookmark_offsets_size;
    }

    /**
     * Get the macros_offset field for the FIB record.
     */
    public int getMacros_offset()
    {
        return field_100_macros_offset;
    }

    /**
     * Set the macros_offset field for the FIB record.
     */
    public void setMacros_offset(int field_100_macros_offset)
    {
        this.field_100_macros_offset = field_100_macros_offset;
    }

    /**
     * Get the macros_size field for the FIB record.
     */
    public int getMacros_size()
    {
        return field_101_macros_size;
    }

    /**
     * Set the macros_size field for the FIB record.
     */
    public void setMacros_size(int field_101_macros_size)
    {
        this.field_101_macros_size = field_101_macros_size;
    }

    /**
     * Get the unused field for the FIB record.
     */
    public int getUnused()
    {
        return field_102_unused;
    }

    /**
     * Set the unused field for the FIB record.
     */
    public void setUnused(int field_102_unused)
    {
        this.field_102_unused = field_102_unused;
    }

    /**
     * Get the unused field for the FIB record.
     */
    public int getUnused()
    {
        return field_103_unused;
    }

    /**
     * Set the unused field for the FIB record.
     */
    public void setUnused(int field_103_unused)
    {
        this.field_103_unused = field_103_unused;
    }

    /**
     * Get the unused field for the FIB record.
     */
    public int getUnused()
    {
        return field_104_unused;
    }

    /**
     * Set the unused field for the FIB record.
     */
    public void setUnused(int field_104_unused)
    {
        this.field_104_unused = field_104_unused;
    }

    /**
     * Get the unused field for the FIB record.
     */
    public int getUnused()
    {
        return field_105_unused;
    }

    /**
     * Set the unused field for the FIB record.
     */
    public void setUnused(int field_105_unused)
    {
        this.field_105_unused = field_105_unused;
    }

    /**
     * Get the printer offset field for the FIB record.
     */
    public int getPrinterOffset()
    {
        return field_106_printerOffset;
    }

    /**
     * Set the printer offset field for the FIB record.
     */
    public void setPrinterOffset(int field_106_printerOffset)
    {
        this.field_106_printerOffset = field_106_printerOffset;
    }

    /**
     * Get the printer size field for the FIB record.
     */
    public int getPrinterSize()
    {
        return field_107_printerSize;
    }

    /**
     * Set the printer size field for the FIB record.
     */
    public void setPrinterSize(int field_107_printerSize)
    {
        this.field_107_printerSize = field_107_printerSize;
    }

    /**
     * Get the printer portrait offset field for the FIB record.
     */
    public int getPrinterPortraitOffset()
    {
        return field_108_printerPortraitOffset;
    }

    /**
     * Set the printer portrait offset field for the FIB record.
     */
    public void setPrinterPortraitOffset(int field_108_printerPortraitOffset)
    {
        this.field_108_printerPortraitOffset = field_108_printerPortraitOffset;
    }

    /**
     * Get the printer portrait size field for the FIB record.
     */
    public int getPrinterPortraitSize()
    {
        return field_109_printerPortraitSize;
    }

    /**
     * Set the printer portrait size field for the FIB record.
     */
    public void setPrinterPortraitSize(int field_109_printerPortraitSize)
    {
        this.field_109_printerPortraitSize = field_109_printerPortraitSize;
    }

    /**
     * Get the printer landscape offset field for the FIB record.
     */
    public int getPrinterLandscapeOffset()
    {
        return field_110_printerLandscapeOffset;
    }

    /**
     * Set the printer landscape offset field for the FIB record.
     */
    public void setPrinterLandscapeOffset(int field_110_printerLandscapeOffset)
    {
        this.field_110_printerLandscapeOffset = field_110_printerLandscapeOffset;
    }

    /**
     * Get the printer landscape size field for the FIB record.
     */
    public int getPrinterLandscapeSize()
    {
        return field_111_printerLandscapeSize;
    }

    /**
     * Set the printer landscape size field for the FIB record.
     */
    public void setPrinterLandscapeSize(int field_111_printerLandscapeSize)
    {
        this.field_111_printerLandscapeSize = field_111_printerLandscapeSize;
    }

    /**
     * Get the wss offset field for the FIB record.
     */
    public int getWssOffset()
    {
        return field_112_wssOffset;
    }

    /**
     * Set the wss offset field for the FIB record.
     */
    public void setWssOffset(int field_112_wssOffset)
    {
        this.field_112_wssOffset = field_112_wssOffset;
    }

    /**
     * Get the wss size field for the FIB record.
     */
    public int getWssSize()
    {
        return field_113_wssSize;
    }

    /**
     * Set the wss size field for the FIB record.
     */
    public void setWssSize(int field_113_wssSize)
    {
        this.field_113_wssSize = field_113_wssSize;
    }

    /**
     * Get the DOP offset field for the FIB record.
     */
    public int getDOPOffset()
    {
        return field_114_DOPOffset;
    }

    /**
     * Set the DOP offset field for the FIB record.
     */
    public void setDOPOffset(int field_114_DOPOffset)
    {
        this.field_114_DOPOffset = field_114_DOPOffset;
    }

    /**
     * Get the DOP size field for the FIB record.
     */
    public int getDOPSize()
    {
        return field_115_DOPSize;
    }

    /**
     * Set the DOP size field for the FIB record.
     */
    public void setDOPSize(int field_115_DOPSize)
    {
        this.field_115_DOPSize = field_115_DOPSize;
    }

    /**
     * Get the sttbfassoc_offset field for the FIB record.
     */
    public int getSttbfassoc_offset()
    {
        return field_116_sttbfassoc_offset;
    }

    /**
     * Set the sttbfassoc_offset field for the FIB record.
     */
    public void setSttbfassoc_offset(int field_116_sttbfassoc_offset)
    {
        this.field_116_sttbfassoc_offset = field_116_sttbfassoc_offset;
    }

    /**
     * Get the sttbfassoc_size field for the FIB record.
     */
    public int getSttbfassoc_size()
    {
        return field_117_sttbfassoc_size;
    }

    /**
     * Set the sttbfassoc_size field for the FIB record.
     */
    public void setSttbfassoc_size(int field_117_sttbfassoc_size)
    {
        this.field_117_sttbfassoc_size = field_117_sttbfassoc_size;
    }

    /**
     * Get the textPieceTable offset field for the FIB record.
     */
    public int getTextPieceTableOffset()
    {
        return field_118_textPieceTableOffset;
    }

    /**
     * Set the textPieceTable offset field for the FIB record.
     */
    public void setTextPieceTableOffset(int field_118_textPieceTableOffset)
    {
        this.field_118_textPieceTableOffset = field_118_textPieceTableOffset;
    }

    /**
     * Get the textPieceTable size field for the FIB record.
     */
    public int getTextPieceTableSize()
    {
        return field_119_textPieceTableSize;
    }

    /**
     * Set the textPieceTable size field for the FIB record.
     */
    public void setTextPieceTableSize(int field_119_textPieceTableSize)
    {
        this.field_119_textPieceTableSize = field_119_textPieceTableSize;
    }

    /**
     * Get the unused field for the FIB record.
     */
    public int getUnused()
    {
        return field_120_unused;
    }

    /**
     * Set the unused field for the FIB record.
     */
    public void setUnused(int field_120_unused)
    {
        this.field_120_unused = field_120_unused;
    }

    /**
     * Get the unused field for the FIB record.
     */
    public int getUnused()
    {
        return field_121_unused;
    }

    /**
     * Set the unused field for the FIB record.
     */
    public void setUnused(int field_121_unused)
    {
        this.field_121_unused = field_121_unused;
    }

    /**
     * Get the offset AutosaveSource field for the FIB record.
     */
    public int getOffsetAutosaveSource()
    {
        return field_122_offsetAutosaveSource;
    }

    /**
     * Set the offset AutosaveSource field for the FIB record.
     */
    public void setOffsetAutosaveSource(int field_122_offsetAutosaveSource)
    {
        this.field_122_offsetAutosaveSource = field_122_offsetAutosaveSource;
    }

    /**
     * Get the count AutosaveSource field for the FIB record.
     */
    public int getCountAutosaveSource()
    {
        return field_123_countAutosaveSource;
    }

    /**
     * Set the count AutosaveSource field for the FIB record.
     */
    public void setCountAutosaveSource(int field_123_countAutosaveSource)
    {
        this.field_123_countAutosaveSource = field_123_countAutosaveSource;
    }

    /**
     * Get the offset GrpXstAtnOwners field for the FIB record.
     */
    public int getOffsetGrpXstAtnOwners()
    {
        return field_124_offsetGrpXstAtnOwners;
    }

    /**
     * Set the offset GrpXstAtnOwners field for the FIB record.
     */
    public void setOffsetGrpXstAtnOwners(int field_124_offsetGrpXstAtnOwners)
    {
        this.field_124_offsetGrpXstAtnOwners = field_124_offsetGrpXstAtnOwners;
    }

    /**
     * Get the count GrpXstAtnOwners field for the FIB record.
     */
    public int getCountGrpXstAtnOwners()
    {
        return field_125_countGrpXstAtnOwners;
    }

    /**
     * Set the count GrpXstAtnOwners field for the FIB record.
     */
    public void setCountGrpXstAtnOwners(int field_125_countGrpXstAtnOwners)
    {
        this.field_125_countGrpXstAtnOwners = field_125_countGrpXstAtnOwners;
    }

    /**
     * Get the offset SttbfAtnbkmk field for the FIB record.
     */
    public int getOffsetSttbfAtnbkmk()
    {
        return field_126_offsetSttbfAtnbkmk;
    }

    /**
     * Set the offset SttbfAtnbkmk field for the FIB record.
     */
    public void setOffsetSttbfAtnbkmk(int field_126_offsetSttbfAtnbkmk)
    {
        this.field_126_offsetSttbfAtnbkmk = field_126_offsetSttbfAtnbkmk;
    }

    /**
     * Get the length SttbfAtnbkmk field for the FIB record.
     */
    public int getLengthSttbfAtnbkmk()
    {
        return field_127_lengthSttbfAtnbkmk;
    }

    /**
     * Set the length SttbfAtnbkmk field for the FIB record.
     */
    public void setLengthSttbfAtnbkmk(int field_127_lengthSttbfAtnbkmk)
    {
        this.field_127_lengthSttbfAtnbkmk = field_127_lengthSttbfAtnbkmk;
    }

    /**
     * Get the unused field for the FIB record.
     */
    public int getUnused()
    {
        return field_128_unused;
    }

    /**
     * Set the unused field for the FIB record.
     */
    public void setUnused(int field_128_unused)
    {
        this.field_128_unused = field_128_unused;
    }

    /**
     * Get the unused field for the FIB record.
     */
    public int getUnused()
    {
        return field_129_unused;
    }

    /**
     * Set the unused field for the FIB record.
     */
    public void setUnused(int field_129_unused)
    {
        this.field_129_unused = field_129_unused;
    }

    /**
     * Get the unused field for the FIB record.
     */
    public int getUnused()
    {
        return field_130_unused;
    }

    /**
     * Set the unused field for the FIB record.
     */
    public void setUnused(int field_130_unused)
    {
        this.field_130_unused = field_130_unused;
    }

    /**
     * Get the unused field for the FIB record.
     */
    public int getUnused()
    {
        return field_131_unused;
    }

    /**
     * Set the unused field for the FIB record.
     */
    public void setUnused(int field_131_unused)
    {
        this.field_131_unused = field_131_unused;
    }

    /**
     * Get the offset PlcspaMom field for the FIB record.
     */
    public int getOffsetPlcspaMom()
    {
        return field_132_offsetPlcspaMom;
    }

    /**
     * Set the offset PlcspaMom field for the FIB record.
     */
    public void setOffsetPlcspaMom(int field_132_offsetPlcspaMom)
    {
        this.field_132_offsetPlcspaMom = field_132_offsetPlcspaMom;
    }

    /**
     * Get the length PlcspaMom field for the FIB record.
     */
    public int getLengthPlcspaMom()
    {
        return field_133_lengthPlcspaMom;
    }

    /**
     * Set the length PlcspaMom field for the FIB record.
     */
    public void setLengthPlcspaMom(int field_133_lengthPlcspaMom)
    {
        this.field_133_lengthPlcspaMom = field_133_lengthPlcspaMom;
    }

    /**
     * Get the offset PlcspaHdr field for the FIB record.
     */
    public int getOffsetPlcspaHdr()
    {
        return field_134_offsetPlcspaHdr;
    }

    /**
     * Set the offset PlcspaHdr field for the FIB record.
     */
    public void setOffsetPlcspaHdr(int field_134_offsetPlcspaHdr)
    {
        this.field_134_offsetPlcspaHdr = field_134_offsetPlcspaHdr;
    }

    /**
     * Get the length PlcspaHdr field for the FIB record.
     */
    public int getLengthPlcspaHdr()
    {
        return field_135_lengthPlcspaHdr;
    }

    /**
     * Set the length PlcspaHdr field for the FIB record.
     */
    public void setLengthPlcspaHdr(int field_135_lengthPlcspaHdr)
    {
        this.field_135_lengthPlcspaHdr = field_135_lengthPlcspaHdr;
    }

    /**
     * Get the length Plcf Bookmark First field for the FIB record.
     */
    public int getLengthPlcfBookmarkFirst()
    {
        return field_136_lengthPlcfBookmarkFirst;
    }

    /**
     * Set the length Plcf Bookmark First field for the FIB record.
     */
    public void setLengthPlcfBookmarkFirst(int field_136_lengthPlcfBookmarkFirst)
    {
        this.field_136_lengthPlcfBookmarkFirst = field_136_lengthPlcfBookmarkFirst;
    }

    /**
     * Get the offset Plcf Bookmark First field for the FIB record.
     */
    public int getOffsetPlcfBookmarkFirst()
    {
        return field_137_offsetPlcfBookmarkFirst;
    }

    /**
     * Set the offset Plcf Bookmark First field for the FIB record.
     */
    public void setOffsetPlcfBookmarkFirst(int field_137_offsetPlcfBookmarkFirst)
    {
        this.field_137_offsetPlcfBookmarkFirst = field_137_offsetPlcfBookmarkFirst;
    }

    /**
     * Sets the template field value.
     * 
     */
    public void setTemplate(boolean value)
    {
        field_6_options = template.setShortBoolean(field_6_options, value);
    }

    /**
     * 
     * @return  the template field value.
     */
    public boolean isTemplate()
    {
        return template.isSet(field_6_options);
    }

    /**
     * Sets the glossary field value.
     * 
     */
    public void setGlossary(boolean value)
    {
        field_6_options = glossary.setShortBoolean(field_6_options, value);
    }

    /**
     * 
     * @return  the glossary field value.
     */
    public boolean isGlossary()
    {
        return glossary.isSet(field_6_options);
    }

    /**
     * Sets the quicksave field value.
     * 
     */
    public void setQuicksave(boolean value)
    {
        field_6_options = quicksave.setShortBoolean(field_6_options, value);
    }

    /**
     * 
     * @return  the quicksave field value.
     */
    public boolean isQuicksave()
    {
        return quicksave.isSet(field_6_options);
    }

    /**
     * Sets the haspictr field value.
     * 
     */
    public void setHaspictr(boolean value)
    {
        field_6_options = haspictr.setShortBoolean(field_6_options, value);
    }

    /**
     * 
     * @return  the haspictr field value.
     */
    public boolean isHaspictr()
    {
        return haspictr.isSet(field_6_options);
    }

    /**
     * Sets the nquicksaves field value.
     * 
     */
    public void setNquicksaves(boolean value)
    {
        field_6_options = nquicksaves.setShortBoolean(field_6_options, value);
    }

    /**
     * 
     * @return  the nquicksaves field value.
     */
    public boolean isNquicksaves()
    {
        return nquicksaves.isSet(field_6_options);
    }

    /**
     * Sets the encrypted field value.
     * 
     */
    public void setEncrypted(boolean value)
    {
        field_6_options = encrypted.setShortBoolean(field_6_options, value);
    }

    /**
     * 
     * @return  the encrypted field value.
     */
    public boolean isEncrypted()
    {
        return encrypted.isSet(field_6_options);
    }

    /**
     * Sets the tabletype field value.
     * 
     */
    public void setTabletype(boolean value)
    {
        field_6_options = tabletype.setShortBoolean(field_6_options, value);
    }

    /**
     * 
     * @return  the tabletype field value.
     */
    public boolean isTabletype()
    {
        return tabletype.isSet(field_6_options);
    }

    /**
     * Sets the readonly field value.
     * 
     */
    public void setReadonly(boolean value)
    {
        field_6_options = readonly.setShortBoolean(field_6_options, value);
    }

    /**
     * 
     * @return  the readonly field value.
     */
    public boolean isReadonly()
    {
        return readonly.isSet(field_6_options);
    }

    /**
     * Sets the writeReservation field value.
     * 
     */
    public void setWriteReservation(boolean value)
    {
        field_6_options = writeReservation.setShortBoolean(field_6_options, value);
    }

    /**
     * 
     * @return  the writeReservation field value.
     */
    public boolean isWriteReservation()
    {
        return writeReservation.isSet(field_6_options);
    }

    /**
     * Sets the extendedCharacter field value.
     * 
     */
    public void setExtendedCharacter(boolean value)
    {
        field_6_options = extendedCharacter.setShortBoolean(field_6_options, value);
    }

    /**
     * 
     * @return  the extendedCharacter field value.
     */
    public boolean isExtendedCharacter()
    {
        return extendedCharacter.isSet(field_6_options);
    }

    /**
     * Sets the loadOverride field value.
     * 
     */
    public void setLoadOverride(boolean value)
    {
        field_6_options = loadOverride.setShortBoolean(field_6_options, value);
    }

    /**
     * 
     * @return  the loadOverride field value.
     */
    public boolean isLoadOverride()
    {
        return loadOverride.isSet(field_6_options);
    }

    /**
     * Sets the farEast field value.
     * 
     */
    public void setFarEast(boolean value)
    {
        field_6_options = farEast.setShortBoolean(field_6_options, value);
    }

    /**
     * 
     * @return  the farEast field value.
     */
    public boolean isFarEast()
    {
        return farEast.isSet(field_6_options);
    }

    /**
     * Sets the crypto field value.
     * 
     */
    public void setCrypto(boolean value)
    {
        field_6_options = crypto.setShortBoolean(field_6_options, value);
    }

    /**
     * 
     * @return  the crypto field value.
     */
    public boolean isCrypto()
    {
        return crypto.isSet(field_6_options);
    }

    /**
     * Sets the history mac field value.
     * 
     */
    public void setHistoryMac(boolean value)
    {
        field_10_history = historyMac.setShortBoolean(field_10_history, value);
    }

    /**
     * 
     * @return  the history mac field value.
     */
    public boolean isHistoryMac()
    {
        return historyMac.isSet(field_10_history);
    }

    /**
     * Sets the empty special field value.
     * 
     */
    public void setEmptySpecial(boolean value)
    {
        field_10_history = emptySpecial.setShortBoolean(field_10_history, value);
    }

    /**
     * 
     * @return  the empty special field value.
     */
    public boolean isEmptySpecial()
    {
        return emptySpecial.isSet(field_10_history);
    }

    /**
     * Sets the load override hist field value.
     * 
     */
    public void setLoadOverrideHist(boolean value)
    {
        field_10_history = loadOverrideHist.setShortBoolean(field_10_history, value);
    }

    /**
     * 
     * @return  the load override hist field value.
     */
    public boolean isLoadOverrideHist()
    {
        return loadOverrideHist.isSet(field_10_history);
    }

    /**
     * Sets the feature undo field value.
     * 
     */
    public void setFeatureUndo(boolean value)
    {
        field_10_history = featureUndo.setShortBoolean(field_10_history, value);
    }

    /**
     * 
     * @return  the feature undo field value.
     */
    public boolean isFeatureUndo()
    {
        return featureUndo.isSet(field_10_history);
    }

    /**
     * Sets the v97 saved field value.
     * 
     */
    public void setV97Saved(boolean value)
    {
        field_10_history = v97Saved.setShortBoolean(field_10_history, value);
    }

    /**
     * 
     * @return  the v97 saved field value.
     */
    public boolean isV97Saved()
    {
        return v97Saved.isSet(field_10_history);
    }

    /**
     * Sets the spare field value.
     * 
     */
    public void setSpare(boolean value)
    {
        field_10_history = spare.setShortBoolean(field_10_history, value);
    }

    /**
     * 
     * @return  the spare field value.
     */
    public boolean isSpare()
    {
        return spare.isSet(field_10_history);
    }


}  // END OF CLASS




