/*
Copyright 2019-2022 FHNW (University of Applied Sciences and Arts Northwestern Switzerland)

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
package ch.fhnw.imvs.hwgstereader.api;

import ch.fhnw.imvs.hwgstereader.api.schemas.ObjectFactory;
import ch.fhnw.imvs.hwgstereader.api.schemas.SensorRootType;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Test for {@link Parser}.
 *
 * @author mluppi
 */
class ParserTest {

    private static final String TEST_XML_RESOURCE = "test-sensor-result.xml";

    @Test
    void testParseXml() throws FileNotFoundException {
        final URL textXmlResource = getClass().getClassLoader().getResource(TEST_XML_RESOURCE);
        final File xmlFile = new File(Objects.requireNonNull(textXmlResource).getFile());
        assertNotNull(xmlFile);

        final SensorRootType root = Parser.parseXml(new FileInputStream(xmlFile), ObjectFactory.class);
        assertNotNull(root);
        assertNotNull(root.getSenSet());
        assertNotNull(root.getSenSet().getEntry());

        final List<SensorRootType.SenSet.Entry> entries = root.getSenSet().getEntry();
        assertEquals(2, entries.size());

        final SensorRootType.SenSet.Entry entry215 = entries.get(0);
        assertNotNull(entry215);
        assertEquals(215, entry215.getID());
        assertEquals("Sensor 215", entry215.getName());
        assertEquals("C", entry215.getUnits());
        assertEquals(25, entry215.getValue().doubleValue(), 0);
        assertEquals(10, entry215.getMin().doubleValue(), 0);
        assertEquals(60, entry215.getMax().doubleValue(), 0);
        assertEquals(0, entry215.getHyst().doubleValue(), 0);
        assertEquals(0, entry215.getEmailSMS());
        assertEquals(1, entry215.getState());

        final SensorRootType.SenSet.Entry entry216 = entries.get(1);
        assertNotNull(entry216);
        assertEquals(216, entry216.getID());
        assertEquals("Sensor 216", entry216.getName());
        assertEquals("%RH", entry216.getUnits());
        assertEquals(46.7, entry216.getValue().doubleValue(), 0);
        assertEquals(10, entry216.getMin().doubleValue(), 0);
        assertEquals(60, entry216.getMax().doubleValue(), 0);
        assertEquals(0, entry216.getHyst().doubleValue(), 0);
        assertEquals(0, entry216.getEmailSMS());
        assertEquals(0, entry216.getState());
    }

    @Test
    void testException() {
        final SensorRootType root = Parser.parseXml(null, ObjectFactory.class);
        assertNull(root);
    }

}
