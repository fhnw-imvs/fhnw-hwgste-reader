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

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

/**
 * Test for {@link HwgSteFetcher}.
 *
 * @author mluppi
 */
public class HwgSteFetcherTest {

    private static final String TEST_XML_RESOURCE = "test-sensor-result.xml";

    @Test
    public void testFetch() throws IOException {
        final URL textXmlResource = getClass().getClassLoader().getResource(TEST_XML_RESOURCE);
        final File xmlFile = new File(Objects.requireNonNull(textXmlResource).getFile());
        assertNotNull(xmlFile);

        final URLConnection connection = mock(URLConnection.class);
        when(connection.getInputStream()).thenAnswer((invocation -> new FileInputStream(xmlFile)));

        final List<HwgSteNode> nodes = new ArrayList<>();
        final HwgSteNode node1 = new HwgSteNode("node-1", "127.0.0.1");
        nodes.add(node1);
        final HwgSteNode node2 = new HwgSteNode("node-2", "127.0.0.2");
        nodes.add(node2);

        final HwgSteFetcher fetcher = spy(new HwgSteFetcher());
        when(fetcher.connectToUrl(node1)).thenReturn(connection);
        when(fetcher.connectToUrl(node2)).thenReturn(connection);

        final List<HwgSteReading> readings = fetcher.fetch(nodes);
        assertNotNull(readings);
        assertEquals(2, readings.size());

        final HwgSteReading reading1 = readings.get(0);
        assertNotNull(reading1);
        assertEquals(node1, reading1.getNode());
        assertEquals(HwgSteFetchStatus.SUCCESS, reading1.getStatus());

        final HwgSteReading reading2 = readings.get(1);
        assertNotNull(reading2);
        assertEquals(node2, reading2.getNode());
        assertEquals(HwgSteFetchStatus.SUCCESS, reading2.getStatus());
    }

    @Test
    public void testConnectTimeout() {
        final HwgSteFetcher fetcher = new HwgSteFetcher();
        fetcher.setConnectTimeout(123);
        assertEquals(123, fetcher.getConnectTimeout());
    }

    @Test
    public void testReadTimeout() {
        final HwgSteFetcher fetcher = new HwgSteFetcher();
        fetcher.setReadTimeout(456);
        assertEquals(456, fetcher.getReadTimeout());
    }
}