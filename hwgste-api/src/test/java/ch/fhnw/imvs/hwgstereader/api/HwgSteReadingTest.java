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

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Test for {@link HwgSteReading}.
 *
 * @author mluppi
 */
class HwgSteReadingTest {

    @Test
    void testConstructors() {
        final HwgSteNode node = new HwgSteNode("test-id", "0.0.0.0");

        final HwgSteMetaInformation metaInformation = new HwgSteMetaInformation("TestDevice", "192.268.100.1",
                "00:0A:60:04:10:81", "SystemName", "SystemLocation");

        final List<HwgSteMeasurement> measurements = new ArrayList<>();
        measurements.add(new HwgSteMeasurement(HwgSteMeasurementType.UNKNOWN, HwgSteMeasurementStatus.ERROR, null, null, null));

        final HwgSteReading reading1 = new HwgSteReading(node, HwgSteFetchStatus.ERROR);
        assertEquals(node, reading1.getNode());
        assertNotNull(reading1.getMeasurements());
        assertTrue(reading1.getMeasurements().isEmpty());
        assertEquals(HwgSteFetchStatus.ERROR, reading1.getStatus());

        final HwgSteReading reading2 = new HwgSteReading(node, HwgSteFetchStatus.SUCCESS, metaInformation, measurements);
        assertEquals(node, reading2.getNode());
        assertNotNull(reading2.getMeasurements());
        assertEquals(1, reading2.getMeasurements().size());
        assertEquals(HwgSteMeasurementStatus.ERROR, reading2.getMeasurements().get(0).getStatus());
        assertEquals(HwgSteFetchStatus.SUCCESS, reading2.getStatus());
    }

}
