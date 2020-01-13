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

import static org.junit.Assert.assertEquals;

/**
 * Test for {@link HwgSteNode}.
 *
 * @author mluppi
 */
public class HwgSteNodeTest {

    public static final String TEST_NAME = "test-id";
    public static final String TEST_HOSTNAME = "0.0.0.0";

    @Test
    public void testGetName() {
        final HwgSteNode node = new HwgSteNode(TEST_NAME, TEST_HOSTNAME);
        assertEquals(TEST_NAME, node.getName());
    }

    @Test
    public void testGetHostname() {
        final HwgSteNode node = new HwgSteNode(TEST_NAME, TEST_HOSTNAME);
        assertEquals(TEST_HOSTNAME, node.getHostname());
    }
}