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

package org.apache.poi.poifs.storage;

import static org.apache.poi.poifs.storage.HeaderBlockConstants._bat_array_offset;
import static org.apache.poi.poifs.storage.HeaderBlockConstants._bat_count_offset;
import static org.apache.poi.poifs.storage.HeaderBlockConstants._max_bats_in_header;
import static org.apache.poi.poifs.storage.HeaderBlockConstants._property_start_offset;
import static org.apache.poi.poifs.storage.HeaderBlockConstants._sbat_start_offset;
import static org.apache.poi.poifs.storage.HeaderBlockConstants._sbat_block_count_offset;
import static org.apache.poi.poifs.storage.HeaderBlockConstants._signature;
import static org.apache.poi.poifs.storage.HeaderBlockConstants._signature_offset;
import static org.apache.poi.poifs.storage.HeaderBlockConstants._xbat_count_offset;
import static org.apache.poi.poifs.storage.HeaderBlockConstants._xbat_start_offset;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import org.apache.poi.poifs.common.POIFSBigBlockSize;
import org.apache.poi.poifs.common.POIFSConstants;
import org.apache.poi.poifs.filesystem.OfficeXmlFileException;
import org.apache.poi.util.HexDump;
import org.apache.poi.util.IOUtils;
import org.apache.poi.util.LittleEndian;
import org.apache.poi.util.LittleEndianConsts;

/**
 * The block containing the archive header
 *
 * @author Marc Johnson (mjohnson at apache dot org)
 */
public final class HeaderBlockReader {
	/**
	 * What big block size the file uses. Most files
	 *  use 512 bytes, but a few use 4096
	 */
	private final POIFSBigBlockSize bigBlockSize;

	/** 
	 * number of big block allocation table blocks (int).
	 * (Number of FAT Sectors in Microsoft parlance) 
	 */
	private final int _bat_count;

	/** 
	 * Start of the property set block (int index of the property set
	 * chain's first big block).
	 */
	private final int _property_start;

	/** 
	 * start of the small block allocation table (int index of small
	 * block allocation table's first big block)
	 */
	private final int _sbat_start;
	/**
	 * Number of small block allocation table blocks (int)
	 * (Number of MiniFAT Sectors in Microsoft parlance)
	 */
	private final int _sbat_count;

	/** 
	 * Big block index for extension to the big block allocation table
	 */
	private final int _xbat_start;
	/**
	 * Number of big block allocation table blocks (int)
	 * (Number of DIFAT Sectors in Microsoft parlance)
	 */
	private final int _xbat_count;
	
	/**
	 * The data
	 */
	private final byte[] _data;

	/**
	 * create a new HeaderBlockReader from an InputStream
	 *
	 * @param stream the source InputStream
	 *
	 * @exception IOException on errors or bad data
	 */
	public HeaderBlockReader(InputStream stream) throws IOException {
		// Grab the first 512 bytes
	   // (For 4096 sized blocks, the remaining 3584 bytes are zero)
		// Then, process the contents
		this(readFirst512(stream));
		
		// Fetch the rest of the block if needed
		if(bigBlockSize.getBigBlockSize() != 512) {
		   int rest = bigBlockSize.getBigBlockSize() - 512;
		   byte[] tmp = new byte[rest];
		   IOUtils.readFully(stream, tmp);
		}
	}
	
	public HeaderBlockReader(ByteBuffer buffer) throws IOException {
	   this(buffer.array());
	}
	
	private HeaderBlockReader(byte[] data) throws IOException {
	   this._data = data;
	   
		// verify signature
		long signature = LittleEndian.getLong(_data, _signature_offset);

		if (signature != _signature) {
			// Is it one of the usual suspects?
			byte[] OOXML_FILE_HEADER = POIFSConstants.OOXML_FILE_HEADER;
			if(_data[0] == OOXML_FILE_HEADER[0] &&
				_data[1] == OOXML_FILE_HEADER[1] &&
				_data[2] == OOXML_FILE_HEADER[2] &&
				_data[3] == OOXML_FILE_HEADER[3]) {
				throw new OfficeXmlFileException("The supplied data appears to be in the Office 2007+ XML. You are calling the part of POI that deals with OLE2 Office Documents. You need to call a different part of POI to process this data (eg XSSF instead of HSSF)");
			}
			if ((signature & 0xFF8FFFFFFFFFFFFFL) == 0x0010000200040009L) {
				// BIFF2 raw stream starts with BOF (sid=0x0009, size=0x0004, data=0x00t0)
				throw new IllegalArgumentException("The supplied data appears to be in BIFF2 format.  "
						+ "POI only supports BIFF8 format");
			}

			// Give a generic error
			throw new IOException("Invalid header signature; read "
				                  + longToHex(signature) + ", expected "
				                  + longToHex(_signature));
		}


		// Figure out our block size
		switch (_data[30]) {
			case 12:
				bigBlockSize = POIFSConstants.LARGER_BIG_BLOCK_SIZE_DETAILS; break;
			case  9:
				bigBlockSize = POIFSConstants.SMALLER_BIG_BLOCK_SIZE_DETAILS; break;
			default:
				throw new IOException("Unsupported blocksize  (2^"
						+ _data[30] + "). Expected 2^9 or 2^12.");
		}

		_bat_count      = getInt(_bat_count_offset, _data);
		_property_start = getInt(_property_start_offset, _data);
		_sbat_start     = getInt(_sbat_start_offset, _data);
		_sbat_count     = getInt(_sbat_block_count_offset, _data);
		_xbat_start     = getInt(_xbat_start_offset, _data);
		_xbat_count     = getInt(_xbat_count_offset, _data);
	}
	
	private static byte[] readFirst512(InputStream stream) throws IOException {
      // Grab the first 512 bytes
      // (For 4096 sized blocks, the remaining 3584 bytes are zero)
      byte[] data = new byte[512];
      int bsCount = IOUtils.readFully(stream, data);
      if(bsCount != 512) {
         throw alertShortRead(bsCount, 512);
      }
      return data;
	}

	private static int getInt(int offset, byte[] data) {
		return LittleEndian.getInt(data, offset);
	}

	private static String longToHex(long value) {
		return new String(HexDump.longToHex(value));
	}

	private static IOException alertShortRead(int pRead, int expectedReadSize) {
		int read;
		if (pRead < 0) {
			//Can't have -1 bytes read in the error message!
			read = 0;
		} else {
			read = pRead;
		}
		String type = " byte" + (read == 1 ? (""): ("s"));

		return new IOException("Unable to read entire header; "
				+ read + type + " read; expected "
				+ expectedReadSize + " bytes");
	}

	/**
	 * get start of Property Table
	 *
	 * @return the index of the first block of the Property Table
	 */
	public int getPropertyStart() {
		return _property_start;
	}

	/**
	 * @return start of small block (MiniFAT) allocation table
	 */
	public int getSBATStart() {
		return _sbat_start;
	}
	public int getSBATCount() {
	   return _sbat_count;
	}

	/**
	 * @return number of BAT blocks
	 */
	public int getBATCount() {
		return _bat_count;
	}

	/**
	 * Returns the offsets to the first (up to) 109
	 *  BAT sectors.
	 * Any additional BAT sectors 
	 * @return BAT offset array
	 */
	public int[] getBATArray() {
		int[] result = new int[ _max_bats_in_header ];
		int   offset = _bat_array_offset;

		for (int j = 0; j < _max_bats_in_header; j++) {
			result[ j ] = LittleEndian.getInt(_data, offset);
			offset     += LittleEndianConsts.INT_SIZE;
		}
		return result;
	}

	/**
	 * @return XBAT (DIFAT) count
	 */
	public int getXBATCount() {
		return _xbat_count;
	}

	/**
	 * @return XBAT (DIFAT) index
	 */
	public int getXBATIndex() {
		return _xbat_start;
	}

	/**
	 * @return The Big Block size, normally 512 bytes, sometimes 4096 bytes
	 */
	public POIFSBigBlockSize getBigBlockSize() {
		return bigBlockSize;
	}
}
