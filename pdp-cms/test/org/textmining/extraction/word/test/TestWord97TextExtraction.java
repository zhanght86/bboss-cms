/*
 * Textmining.org text extractors
 * 
 * Copyright (C) 2008 Benryan Software Inc.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 */
package org.textmining.extraction.word.test;

import java.io.FileInputStream;
import java.io.IOException;

import org.textmining.extraction.TextExtractor;
import org.textmining.extraction.word.PasswordProtectedException;
import org.textmining.extraction.word.Word97TextExtractor;

public class TestWord97TextExtraction
  extends TestWordExtraction
{
  public void testSimple() throws IOException, PasswordProtectedException
  {
    textExtractionTest("./test/text/winword97/simple.doc", "This is a simple test of text extraction for Word documents.");
  }    

  public void testFastSaved() throws IOException, PasswordProtectedException
  {
    textExtractionTest("./test/text/winword97/fastSaved.doc", "This is a simple test of text extraction for Word documents. This document is a fast saved Word document.");
  }
  
  public void testWithDeletedChanges() throws IOException, PasswordProtectedException 
  {
    textExtractionTest("./test/text/winword97/revisions.doc", "This is a simple test of text extraction for Word documents.");
  }
  
  public void testFieldCodes() throws IOException, PasswordProtectedException
  {
    textExtractionTest("./test/text/winword97/fieldCode.doc", "This is a simple test of text extraction for Word documents. Benryan's website");
  }


  protected TextExtractor getTextExtractor(FileInputStream in) throws IOException, PasswordProtectedException
  {
    return new Word97TextExtractor(in);
  }
}
