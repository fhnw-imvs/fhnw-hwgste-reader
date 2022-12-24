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

import ch.fhnw.imvs.hwgstereader.api.HwgSteFetcher;
import ch.fhnw.imvs.hwgstereader.api.HwgSteNode;
import ch.fhnw.imvs.hwgstereader.api.HwgSteReading;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Reader for HWg-STE sensor nodes.
 *
 * @author mluppi
 */
public class HwgSteReader {

    private final HwgSteFetcher fetcher;
    private final List<HwgSteNode> nodes = Collections.synchronizedList(new ArrayList<>());

    /**
     * Constructs a {@link HwgSteReader}.
     */
    public HwgSteReader() {
        fetcher = new HwgSteFetcher();
    }

    /**
     * Constructs a {@link HwgSteReader} with a custom fetcher (for testing purposes).
     *
     * @param fetcher a custom fetcher to be used internally
     */
    HwgSteReader(final HwgSteFetcher fetcher) {
        this.fetcher = fetcher;
    }

    /**
     * Reads the data from the attached nodes.
     *
     * @return a {@link List<HwgSteReading>} containing data fetched from the attached nodes
     */
    public List<HwgSteReading> read() {
        return fetcher.fetch(nodes);
    }

    /**
     * Attaches a node to the reader.
     *
     * @param node the node to add to the reader
     */
    public void attachNode(final HwgSteNode node) {
        this.nodes.add(node);
    }

    /**
     * Attaches all nodes in the collection to the reader.
     *
     * @param nodes a {@link Collection} with nodes to add to the reader
     */
    public void attachNodes(final Collection<HwgSteNode> nodes) {
        this.nodes.addAll(nodes);
    }

    /**
     * Detaches a node to the reader.
     *
     * @param node the node to remove from the reader
     */
    public void detachNode(final HwgSteNode node) {
        this.nodes.remove(node);
    }

    /**
     * Detaches all nodes in the collection to the reader.
     *
     * @param nodes a {@link Collection} with nodes to remove from the reader
     */
    public void detachNodes(final Collection<HwgSteNode> nodes) {
        this.nodes.removeAll(nodes);
    }

    /**
     * Detaches all nodes currently registered nodes.
     */
    public void detachAllNodes() {
        this.nodes.clear();
    }

    /**
     * Returns the number of currently attached nodes.
     *
     * @return an {@code int} representing the number of currently attached nodes
     */
    public int getAttachesNodesCount() {
        return nodes.size();
    }

    /**
     * Returns the currently attached nodes.
     *
     * @return a {@link List<HwgSteNode>} with the currently attached nodes
     */
    public List<HwgSteNode> getAttachedNodes() {
        return Collections.unmodifiableList(nodes);
    }

    /**
     * Returns the setting for connect timeout.
     *
     * @return an {@code int} that indicates the connect timeout value in milliseconds.
     */
    public int getConnectTimeout() {
        return fetcher.getConnectTimeout();
    }

    /**
     * Specifies the read timeout in milliseconds to be used when opening a communications link to a sensor.
     * A timeout of zero is interpreted as an infinite timeout.
     *
     * @param connectTimeout an {@code int} that specifies the timeout value to be used in milliseconds
     */
    public void setConnectTimeout(int connectTimeout) {
        fetcher.setConnectTimeout(connectTimeout);
    }

    /**
     * Returns the setting for read timeout.
     *
     * @return An {@code int} that indicates the read timeout value in milliseconds.
     */
    public int getReadTimeout() {
        return fetcher.getReadTimeout();
    }

    /**
     * Specifies the read timeout in milliseconds to be used when reading from a sensor.
     * A timeout of zero is interpreted as an infinite timeout.
     *
     * @param readTimeout an {@code int} that specifies the timeout value to be used in milliseconds
     */
    public void setReadTimeout(int readTimeout) {
        fetcher.setReadTimeout(readTimeout);
    }

}
