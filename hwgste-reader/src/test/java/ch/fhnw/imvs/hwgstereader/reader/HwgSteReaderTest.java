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
package ch.fhnw.imvs.hwgstereader.reader;

import ch.fhnw.imvs.hwgstereader.api.HwgSteFetchStatus;
import ch.fhnw.imvs.hwgstereader.api.HwgSteFetcher;
import ch.fhnw.imvs.hwgstereader.api.HwgSteMeasurement;
import ch.fhnw.imvs.hwgstereader.api.HwgSteMetaInformation;
import ch.fhnw.imvs.hwgstereader.api.HwgSteNode;
import ch.fhnw.imvs.hwgstereader.api.HwgSteReading;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test for {@link HwgSteReader}.
 *
 * @author mluppi
 */
class HwgSteReaderTest {

    private static List<HwgSteReading> readingList;
    private static HwgSteReader reader;

    @BeforeAll
    public static void setUp() {
        final List<HwgSteNode> nodeList = new ArrayList<>();
        final HwgSteNode node1 = new HwgSteNode("node-1", "127.0.0.1");
        nodeList.add(node1);
        final HwgSteNode node2 = new HwgSteNode("node-2", "127.0.0.2");
        nodeList.add(node2);

        final HwgSteMetaInformation metaInformation1 = new HwgSteMetaInformation("TestDevice1", "127.0.0.1",
                "00:0A:60:04:10:81", "SystemName1", "SystemLocation1");
        final List<HwgSteMeasurement> measurements1 = new ArrayList<>();
        final HwgSteReading reading1 = new HwgSteReading(node1, HwgSteFetchStatus.SUCCESS, metaInformation1, measurements1);

        final HwgSteMetaInformation metaInformation2 = new HwgSteMetaInformation("TestDevice2", "127.0.0.2",
                "00:0A:60:04:10:82", "SystemName2", "SystemLocation1");
        final List<HwgSteMeasurement> measurements2 = new ArrayList<>();
        final HwgSteReading reading2 = new HwgSteReading(node2, HwgSteFetchStatus.SUCCESS, metaInformation2, measurements2);

        readingList = new ArrayList<>();
        readingList.add(reading1);
        readingList.add(reading2);

        final HwgSteFetcher fetcher = mock(HwgSteFetcher.class);
        when(fetcher.fetch(nodeList)).thenReturn(readingList);

        reader = new HwgSteReader(fetcher);
        reader.attachNodes(nodeList);
    }

    @Test
    void testRead() {
        final List<HwgSteReading> readingList = reader.read();
        assertNotNull(HwgSteReaderTest.readingList);
        assertEquals(2, HwgSteReaderTest.readingList.size());
        assertEquals(HwgSteReaderTest.readingList.get(0), readingList.get(0));
        assertEquals(HwgSteReaderTest.readingList.get(1), readingList.get(1));
    }

    @Test
    void testConnectTimeout() {
        final HwgSteFetcher fetcher = new HwgSteFetcher();
        fetcher.setConnectTimeout(123);
        assertEquals(123, fetcher.getConnectTimeout());
    }

    @Test
    void testReadTimeout() {
        final HwgSteFetcher fetcher = new HwgSteFetcher();
        fetcher.setReadTimeout(456);
        assertEquals(456, fetcher.getReadTimeout());
    }

}
