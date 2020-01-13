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
package ch.fhnw.imvs.hwgstereader.reader;

import ch.fhnw.imvs.hwgstereader.api.HwgSteFetchStatus;
import ch.fhnw.imvs.hwgstereader.api.HwgSteFetcher;
import ch.fhnw.imvs.hwgstereader.api.HwgSteNode;
import ch.fhnw.imvs.hwgstereader.api.HwgSteReading;
import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.LongAdder;

import static org.awaitility.Awaitility.await;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Test for {@link HwgStePeriodicReader}.
 *
 * @author mluppi
 */
public class HwgStePeriodicReaderTest {

    private static final int POLL_DURATION = 1; // seconds

    private HwgStePeriodicReader reader;
    private LongAdder adder = new LongAdder();

    @Before
    public void setUp() {
        final List<HwgSteNode> nodeList = new ArrayList<>();
        final HwgSteNode node1 = new HwgSteNode("node-1", "127.0.0.1");
        nodeList.add(node1);
        final HwgSteNode node2 = new HwgSteNode("node-2", "127.0.0.2");
        nodeList.add(node2);

        final List<HwgSteReading>readingList = new ArrayList<>();
        readingList.add(new HwgSteReading(node1, HwgSteFetchStatus.SUCCESS, null, null));
        readingList.add(new HwgSteReading(node2, HwgSteFetchStatus.SUCCESS, null, null));

        final HwgSteFetcher fetcher = mock(HwgSteFetcher.class);
        when(fetcher.fetch(nodeList)).thenReturn(readingList);

        reader = new HwgStePeriodicReader(fetcher,  r -> adder.increment(), POLL_DURATION);
        reader.attachNodes(nodeList);
    }

    @Test
    public void testStart() {
        final int cutoffCount = 1;
        reader.start();
        await().until(() -> adder.intValue() == cutoffCount);
        assertEquals(cutoffCount, adder.intValue());
    }

    @Test
    public void testStop() {
        final int cutoffCount = 3;
        reader.start();
        await().until(() -> adder.intValue() == cutoffCount);
        reader.stop();
        await().atLeast(Duration.ofSeconds(2 * POLL_DURATION));
        assertEquals(cutoffCount, adder.intValue());
    }
}