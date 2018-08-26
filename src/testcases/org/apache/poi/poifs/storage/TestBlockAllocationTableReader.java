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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import junit.framework.AssertionFailedError;
import org.apache.poi.poifs.common.POIFSBigBlockSize;
import org.apache.poi.poifs.common.POIFSConstants;
import org.apache.poi.util.HexRead;
import org.apache.poi.util.LittleEndian;
import org.apache.poi.util.LittleEndianConsts;
import org.junit.Test;

/**
 * Class to test BlockAllocationTableReader functionality
 */
public class TestBlockAllocationTableReader {

	/**
	 * Test small block allocation table constructor
	 */
	@Test
	public void testSmallBATConstructor() throws IOException {

		// need to create an array of raw blocks containing the SBAT,
		// and a small document block list
		final String sbat_data = "H4sIAAAAAAAAAPv/nzjwj4ZYiYGBAZfcKKAtAAC/sexrAAIAAA==";

		RawDataBlock[] sbats = { new RawDataBlock(new ByteArrayInputStream(RawDataUtil.decompress(sbat_data))) };

		final String sbt_data =
			"H4sIAAAAAAAAAONg0GDISsxNLdYNNTc3Mrc00tUwNNP1Ty7RNTIwMHQAsk0MdY2NNfWiXNwYsAB2MNmg/sgBmyxhQB395AMm" +
			"BkaK9HNQaD83hfqZKXY/E4OCIQcDK0NwYllqCgeDOEOwnkdocLCjp5+Co4KLa5iCv5tbkEKoNwfQrUhJA6TFVM9Yz4gy94OM" +
			"Aac/svVTaj8zg7tTAAX6ZRk0HDWRAkahJF8BiUtQPyMDITX4ABMFegeDfsrjjzLAxCBBoX7KwED7n/LwG2j7KSv/Bt79A2s/" +
			"NdzPQUWaVDDQ/h/o+meop5+hrx9ng4ku9jOhYVIBM4X2j4KhDQAtwD4rAA4AAA==";

		InputStream sbt_input = new ByteArrayInputStream(RawDataUtil.decompress(sbt_data));

		BlockListImpl small_blocks = new RawDataBlockList(sbt_input, POIFSConstants.SMALLER_BIG_BLOCK_SIZE_DETAILS);
		int blockCount = small_blocks.blockCount();
		ListManagedBlock[] lmb = new ListManagedBlock[7*blockCount];
		for (int i=0; i<lmb.length; i++) {
			lmb[i] = small_blocks.get(i % blockCount);
		}
		small_blocks.setBlocks(lmb);

		BlockAllocationTableReader sbat = new BlockAllocationTableReader(
		      POIFSConstants.SMALLER_BIG_BLOCK_SIZE_DETAILS, sbats, small_blocks);
		int[] nextIndex = {
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -2, -2, -2, -2, -2, -2,
			-2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2, -2,
			-2, 34, -2, -2, -2, -2, -2, -2, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1,
			-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1
		};

		for (int j = 0; j < 128; j++) {
			final boolean isUsed = nextIndex[j] != -1;
			assertEquals("checking usage of block " + j, isUsed, sbat.isUsed(j));

			if (isUsed) {
				assertEquals("checking usage of block " + j, nextIndex[j], sbat.getNextBlockIndex(j));
				small_blocks.remove(j);
			} else {
				try {
					small_blocks.remove(j);
					fail("removing block " + j + " should have failed");
				} catch (IOException ignored) {
					// expected during successful test
				}
			}
		}
	}

	@Test
	public void testReadingConstructor() throws IOException {

		// create a document, minus the header block, and use that to
		// create a RawDataBlockList. The document will exist entire
		// of BATBlocks and XBATBlocks
		//
		// we will create two XBAT blocks, which will encompass 128
		// BAT blocks between them, and two extra BAT blocks which
		// will be in the block array passed to the constructor. This
		// makes a total of 130 BAT blocks, which will encompass
		// 16,640 blocks, for a file size of some 8.5 megabytes.
		//
		// Naturally, we'll fake that out ...
		//
		// map of blocks:
		// block 0: xbat block 0
		// block 1: xbat block 1
		// block 2: bat block 0
		// block 3: bat block 1
		// blocks 4-130: bat blocks 2-128, contained in xbat block 0
		// block 131: bat block 129, contained in xbat block 1
		// blocks 132-16639: fictitious blocks, faked out. All blocks
		// whose index is evenly divisible by 256
		// will be unused
		LocalRawDataBlockList list = new LocalRawDataBlockList();

		list.createNewXBATBlock(4, 130, 1);
		list.createNewXBATBlock(131, 131, -2);
		for (int j = 0; j < 130; j++) {
			list.createNewBATBlock(j * 128);
		}
		list.fill(132);
		int[] blocks = { 2, 3 };
		BlockAllocationTableReader table = new BlockAllocationTableReader(
		      POIFSConstants.SMALLER_BIG_BLOCK_SIZE_DETAILS, 130, blocks, 2, 0, list);

		for (int i = 0; i < (130 * 128); i++) {
			if (i % 256 == 0) {
				assertTrue("verifying block " + i + " is unused", !table.isUsed(i));
			} else if (i % 256 == 255) {
				assertEquals("Verify end of chain for block " + i, POIFSConstants.END_OF_CHAIN,
						table.getNextBlockIndex(i));
			} else {
				assertEquals("Verify next index for block " + i, i + 1, table.getNextBlockIndex(i));
			}
		}
	}

	@Test
	public void testFetchBlocks() throws IOException {

		// strategy:
		//
		// 1. set up a single BAT block from which to construct a
		// BAT. create nonsense blocks in the raw data block list
		// corresponding to the indices in the BAT block.
		// 2. The indices will include very short documents (0 and 1
		// block in length), longer documents, and some screwed up
		// documents (one with a loop, one that will peek into
		// another document's data, one that includes an unused
		// document, one that includes a reserved (BAT) block, one
		// that includes a reserved (XBAT) block, and one that
		// points off into space somewhere
		LocalRawDataBlockList list = new LocalRawDataBlockList();
		byte[] data = new byte[512];
		int offset = 0;

		LittleEndian.putInt(data, offset, -3); // for the BAT block itself
		offset += LittleEndianConsts.INT_SIZE;

		// document 1: is at end of file already; start block = -2
		// document 2: has only one block; start block = 1
		LittleEndian.putInt(data, offset, -2);
		offset += LittleEndianConsts.INT_SIZE;

		// document 3: has a loop in it; start block = 2
		LittleEndian.putInt(data, offset, 2);
		offset += LittleEndianConsts.INT_SIZE;

		// document 4: peeks into document 2's data; start block = 3
		LittleEndian.putInt(data, offset, 4);
		offset += LittleEndianConsts.INT_SIZE;
		LittleEndian.putInt(data, offset, 1);
		offset += LittleEndianConsts.INT_SIZE;

		// document 5: includes an unused block; start block = 5
		LittleEndian.putInt(data, offset, 6);
		offset += LittleEndianConsts.INT_SIZE;
		LittleEndian.putInt(data, offset, -1);
		offset += LittleEndianConsts.INT_SIZE;

		// document 6: includes a BAT block; start block = 7
		LittleEndian.putInt(data, offset, 8);
		offset += LittleEndianConsts.INT_SIZE;
		LittleEndian.putInt(data, offset, 0);
		offset += LittleEndianConsts.INT_SIZE;

		// document 7: includes an XBAT block; start block = 9
		LittleEndian.putInt(data, offset, 10);
		offset += LittleEndianConsts.INT_SIZE;
		LittleEndian.putInt(data, offset, -4);
		offset += LittleEndianConsts.INT_SIZE;

		// document 8: goes off into space; start block = 11;
		LittleEndian.putInt(data, offset, 1000);
		offset += LittleEndianConsts.INT_SIZE;

		// document 9: no screw ups; start block = 12;
		int index = 13;

		for (; offset < 508; offset += LittleEndianConsts.INT_SIZE) {
			LittleEndian.putInt(data, offset, index++);
		}
		LittleEndian.putInt(data, offset, -2);
		list.add(new RawDataBlock(new ByteArrayInputStream(data)));
		list.fill(1);
		int[] blocks = { 0 };
		BlockAllocationTableReader table = new BlockAllocationTableReader(
		      POIFSConstants.SMALLER_BIG_BLOCK_SIZE_DETAILS, 1, blocks, 0, -2, list);
		int[] start_blocks = { -2, 1, 2, 3, 5, 7, 9, 11, 12 };
		int[] expected_length = { 0, 1, -1, -1, -1, -1, -1, -1, 116 };

		for (int j = 0; j < start_blocks.length; j++) {
			try {
				ListManagedBlock[] dataBlocks = table.fetchBlocks(start_blocks[j], -1, list);

				if (expected_length[j] == -1) {
					fail("document " + j + " should have failed, but found a length of "
							+ dataBlocks.length);
				} else {
					assertEquals(expected_length[j], dataBlocks.length);
				}
			} catch (IOException e) {
				if (expected_length[j] != -1) {
					// -1 would be a expected failure here, anything else not
					throw e;
				}
			}
		}
	}

	/**
	 * Bugzilla 48085 describes an error where a corrupted Excel file causes POI to throw an
	 * {@link OutOfMemoryError}.
	 */
	@Test
	public void testBadSectorAllocationTableSize_bug48085() {
		int BLOCK_SIZE = 512;
		POIFSBigBlockSize bigBlockSize = POIFSConstants.SMALLER_BIG_BLOCK_SIZE_DETAILS;
		assertEquals(BLOCK_SIZE, bigBlockSize.getBigBlockSize());
		
		// 512 bytes take from the start of bugzilla attachment 24444
		byte[] initData = HexRead.readFromString(

		"D0 CF 11 E0 A1 B1 1A E1 20 20 20 20 20 20 20 20  20 20 20 20 20 20 20 20 3E 20 03 20 FE FF 09 20" +
		"06 20 20 20 20 20 20 20 20 20 20 20 01 20 20 20  01 20 20 20 20 20 20 20 20 10 20 20 02 20 20 20" +
		"02 20 20 20 FE FF FF FF 20 20 20 20 20 20 20 20  "
		);
		// the rest of the block is 'FF'
		byte[] data = new byte[BLOCK_SIZE];
		Arrays.fill(data, (byte)0xFF);
		System.arraycopy(initData, 0, data, 0, initData.length);

		// similar code to POIFSFileSystem.<init>:
		InputStream stream = new ByteArrayInputStream(data);
		HeaderBlock hb;
		RawDataBlockList dataBlocks;
		try {
			hb = new HeaderBlock(stream);
			dataBlocks = new RawDataBlockList(stream, bigBlockSize);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		try {
			new BlockAllocationTableReader(POIFSConstants.SMALLER_BIG_BLOCK_SIZE_DETAILS, 
			      hb.getBATCount(), hb.getBATArray(), hb.getXBATCount(),
					hb.getXBATIndex(), dataBlocks);
		} catch (IOException e) {
			// expected during successful test
         assertEquals("Block count 538976257 is too high. POI maximum is 65535.", e.getMessage());
		} catch (OutOfMemoryError e) {
			if (e.getStackTrace()[1].getMethodName().equals("testBadSectorAllocationTableSize")) {
				throw new AssertionFailedError("Identified bug 48085");
			}
		}
	}
}
