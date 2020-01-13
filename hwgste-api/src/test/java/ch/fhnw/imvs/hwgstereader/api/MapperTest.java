/*
Copyright 2019 FHNW (University of Applied Sciences and Arts Northwestern Switzerland)

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
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test for {@link Mapper}.
 *
 * @author mluppi
 */
@SuppressWarnings("squid:S3655")
public class MapperTest {

    private static final String TEST_XML_RESOURCE = "test-sensor-result.xml";

    @Test
    public void testWithXmlFile() throws FileNotFoundException {
        final URL textXmlResource = getClass().getClassLoader().getResource(TEST_XML_RESOURCE);
        final File xmlFile = new File(Objects.requireNonNull(textXmlResource).getFile());
        assertNotNull(xmlFile);

        final SensorRootType root = Parser.parseXml(new FileInputStream(xmlFile), ObjectFactory.class);
        assertNotNull(root);

        final HwgSteMetaInformation metaInformation = Mapper.mapMetaInformation(root);
        assertEquals("HWg-STE", metaInformation.getDeviceName());
        assertEquals("192.168.200.138", metaInformation.getIpAddress());
        assertEquals("00:0A:60:04:10:81", metaInformation.getMacAddress());
        assertEquals("SystemName", metaInformation.getSystemName());
        assertEquals("SystemLocation", metaInformation.getSystemLocation());

        final Optional<List<HwgSteMeasurement>> measurements = Mapper.mapMeasurements(root);
        assertTrue(measurements.isPresent());

        final HwgSteMeasurement measurement1 = measurements.get().get(0);
        assertEquals(HwgSteMeasurementType.TEMPERATURE, measurement1.getType());
        assertEquals(new BigDecimal("25.0"), measurement1.getValue());
        assertEquals(new BigDecimal("10.0"), measurement1.getMin());
        assertEquals(new BigDecimal("60.0"), measurement1.getMax());
        assertEquals(HwgSteMeasurementStatus.VALID, measurement1.getStatus());

        final HwgSteMeasurement measurement2 = measurements.get().get(1);
        assertEquals(HwgSteMeasurementType.HUMIDITY, measurement2.getType());
        assertEquals(new BigDecimal("46.7"), measurement2.getValue());
        assertEquals(new BigDecimal("10.0"), measurement2.getMin());
        assertEquals(new BigDecimal("60.0"), measurement2.getMax());
        assertEquals(HwgSteMeasurementStatus.INVALID, measurement2.getStatus());
    }

    @Test
    public void testSensorRootTypeNull() {
        final Optional<List<HwgSteMeasurement>> measurements = Mapper.mapMeasurements(null);
        assertFalse(measurements.isPresent());
    }

    @Test
    public void testEntryNull() {
        final ObjectFactory factory = new ObjectFactory();

        final SensorRootType.SenSet senSet = factory.createSensorRootTypeSenSet();
        senSet.getEntry().add(null);

        final SensorRootType root = factory.createSensorRootType();
        root.setSenSet(senSet);

        final HwgSteMetaInformation metaInformation = Mapper.mapMetaInformation(root);
        assertNull(metaInformation);

        final Optional<List<HwgSteMeasurement>> measurements = Mapper.mapMeasurements(root);
        assertTrue(measurements.isPresent());
        assertNotNull(measurements.get());
        assertTrue(measurements.get().isEmpty());
    }

    @Test
    public void testInvalidType() {
        final ObjectFactory factory = new ObjectFactory();

        final SensorRootType.SenSet.Entry entry = factory.createSensorRootTypeSenSetEntry();
        entry.setID(0);

        final SensorRootType.SenSet senSet = factory.createSensorRootTypeSenSet();
        senSet.getEntry().add(entry);

        final SensorRootType root = factory.createSensorRootType();
        root.setSenSet(senSet);

        final Optional<List<HwgSteMeasurement>> measurements = Mapper.mapMeasurements(root);
        assertTrue(measurements.isPresent());
        assertNotNull(measurements.get().get(0));

        final HwgSteMeasurement measurement = measurements.get().get(0);
        assertEquals(HwgSteMeasurementStatus.ERROR, measurement.getStatus());
    }
}
