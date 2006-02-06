
/* ====================================================================
   Copyright 2002-2004   Apache Software Foundation

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
==================================================================== */
        


package org.apache.poi.hslf.record;


import junit.framework.TestCase;
import java.io.ByteArrayOutputStream;

/**
 * Tests that Comment2000 works properly.
 * TODO: Test Comment200Atom within
 *
 * @author Nick Burch (nick at torchbox dot com)
 */
public class TestComment2000 extends TestCase {
	// From a real file
	private byte[] data_a = new byte[] { 
		0x0F, 00, 0xE0-256, 0x2E, 0x9C-256, 00, 00, 00,
		00, 00, 0xBA-256, 0x0F, 0x14, 00, 00, 00,
		0x44, 00, 0x75, 00, 0x6D, 00, 0x62, 00,
		0x6C, 00, 0x65, 00, 0x64, 00, 0x6F, 00, 
		0x72, 00, 0x65, 00,
		0x10, 00, 0xBA-256, 0x0F, 0x4A, 00, 00, 00, 
		0x59, 00, 0x65, 00, 0x73, 00, 0x2C, 00, 
		0x20, 00, 0x74, 00, 0x68, 00, 0x65, 00,
		0x79, 00, 0x20, 00, 0x63, 00, 0x65, 00,
		0x72, 00, 0x74, 00,	0x61, 00, 0x69, 00,
		0x6E, 00, 0x6C, 00, 0x79, 00, 0x20, 00,
		0x61, 00, 0x72, 00, 0x65, 00, 0x2C, 00, 
		0x20, 00, 0x61, 00, 0x72, 00, 0x65, 00, 
		0x6E, 00, 0x27, 00, 0x74, 00, 0x20, 00,
		0x74, 00, 0x68, 00, 0x65, 00, 0x79, 00, 0x21, 00,
		0x20, 00, 0xBA-256, 0x0F, 0x02, 00, 00, 00,
		0x44, 00,
		00, 00, 0xE1-256, 0x2E, 0x1C, 00, 00, 00,
		01, 00, 00, 00, 0xD6-256, 07, 01, 00,
		02, 00, 0x18, 00, 0x0A, 00, 0x1A, 00, 
		0x0F, 00, 0xCD-256, 00, 0x92-256, 00, 
		00,	00, 0x92-256, 00, 00, 00
	};
	private byte[] data_b = new byte[] { 
		0x0F, 00, 0xE0-256, 0x2E, 0xAC-256, 00, 00, 00, 
		00, 00, 0xBA-256, 0x0F, 0x10, 00, 00, 00,
		0x48, 00, 0x6F, 00, 0x67, 00, 0x77, 00,
		0x61, 00, 0x72, 00, 0x74, 00, 0x73, 00,
		0x10, 00, 0xBA-256, 0x0F, 0x5E, 00, 00, 00,
		0x43, 00, 0x6F, 00, 0x6D, 00, 0x6D, 00,
		0x65, 00, 0x6E, 00, 0x74, 00, 0x73, 00,
		0x20, 00, 0x61, 00, 0x72, 00, 0x65, 00,
		0x20, 00, 0x66, 00, 0x75, 00, 0x6E, 00,
		0x20, 00, 0x74, 00, 0x68, 00, 0x69, 00,
		0x6E, 00, 0x67, 00, 0x73, 00, 0x20, 00,
		0x74, 00, 0x6F, 00, 0x20, 00, 0x61, 00,
		0x64, 00, 0x64, 00, 0x20, 00, 0x69, 00,
		0x6E, 00, 0x2C, 00, 0x20, 00, 0x61, 00,
		0x72, 00, 0x65, 00, 0x6E, 00, 0x27, 00,
		0x74, 00, 0x20, 00, 0x74, 00, 0x68, 00,
		0x65, 00, 0x79, 00, 0x3F, 00,
		0x20, 00, 0xBA-256, 0x0F, 0x02, 00, 00, 00, 
		0x48, 00,
		00, 00, 0xE1-256, 0x2E, 0x1C, 00, 00, 00,
		01, 00, 00, 00, 0xD6-256, 0x07, 01, 00, 
		02, 00, 0x18, 00, 0x0A, 00, 0x19, 00, 03,
		00, 0xD5-256, 02, 0x0A, 00, 00, 00, 
		0x0A, 00, 00, 00
		};
	
    public void testRecordType() throws Exception {
		Comment2000 ca = new Comment2000(data_a, 0, data_a.length);
		assertEquals(12000l, ca.getRecordType());
	}
	public void testAuthor() throws Exception {
		Comment2000 ca = new Comment2000(data_a, 0, data_a.length);
		assertEquals("Dumbledore", ca.getAuthor());
		assertEquals("D", ca.getAuthorInitials());
	}
	public void testText() throws Exception {
		Comment2000 ca = new Comment2000(data_a, 0, data_a.length);
		assertEquals("Yes, they certainly are, aren't they!", ca.getText());
	}

	public void testWrite() throws Exception {
		Comment2000 ca = new Comment2000(data_a, 0, data_a.length);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ca.writeOut(baos);
		byte[] b = baos.toByteArray();

		assertEquals(data_a.length, b.length);
		for(int i=0; i<data_a.length; i++) {
			assertEquals(data_a[i],b[i]);
		}
	}
	
	// Change a few things
	public void testChange() throws Exception {
		Comment2000 ca = new Comment2000(data_a, 0, data_a.length);
		Comment2000 cb = new Comment2000(data_b, 0, data_b.length);
		ca.setAuthor("Hogwarts");
		ca.setAuthorInitials("H");
		ca.setText("Comments are fun things to add in, aren't they?");
		
		// TODO: Make me a proper copy of the Comment2000Atom
		ca._children[3] = cb._children[3];
		
		// Check now the same
		assertEquals(ca.getText(), cb.getText());
		assertEquals(ca.getAuthor(), cb.getAuthor());
		assertEquals(ca.getAuthorInitials(), cb.getAuthorInitials());
		
		// Check bytes weren't the same
		try {
			for(int i=0; i<data_a.length; i++) {
				assertEquals(data_a[i],data_b[i]);
			}
			fail();
		} catch(Error e) {
			// Good, they're not the same
		}
		
		// Check bytes are now the same
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ca.writeOut(baos);
		byte[] b = baos.toByteArray();
		
		// Should now be the same
		assertEquals(data_b.length, b.length);
		for(int i=0; i<data_b.length; i++) {
			assertEquals(data_b[i],b[i]);
		}
	}
}
